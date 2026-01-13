def call() {

    def cfg = loadConfig()

    pipeline {
        agent any

        stages {

            stage('Init Environment') {
                steps {
                    script {
                        env.ANSIBLE_FORCE_COLOR = 'true'
                        env.ANSIBLE_HOST_KEY_CHECKING = 'False'
                        env.ENVIRONMENT = cfg.ENVIRONMENT
                    }
                }
            }

            stage('Checkout Code') {
                steps {
                    checkoutCode(cfg.GIT)
                }
            }

            stage('User Approval') {
                steps {
                    approvalGate(cfg.ENVIRONMENT, cfg.KEEP_APPROVAL_STAGE)
                }
            }

            stage('Apply Playbook') {
                steps {
                    runPlaybook(cfg)
                }
            }
        }

        post {
            success {
                notifySlack(
                    cfg.SLACK_CHANNEL_NAME,
                    cfg.ACTION_MESSAGE,
                    "SUCCESS"
                )
            }
            failure {
                notifySlack(
                    cfg.SLACK_CHANNEL_NAME,
                    cfg.ACTION_MESSAGE,
                    "FAILED"
                )
            }
            always {
                cleanWs()
            }
        }
    }
}
