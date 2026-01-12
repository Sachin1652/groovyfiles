def call(Map config = [:]) {

    pipeline {
        agent any

        environment {
            ANSIBLE_FORCE_COLOR = 'true'
            ANSIBLE_HOST_KEY_CHECKING = 'False'
            AWS_DEFAULT_REGION = config.AWS_REGION
            SLACK_CHANNEL = config.SLACK_CHANNEL_NAME
        }

        stages {

            /* ---------- CLONE ---------- */
            stage('Clone Repository') {
                steps {
                    checkout scm
                }
            }

            /* ---------- USER APPROVAL ---------- */
            stage('User Approval') {
                when {
                    expression { return config.KEEP_APPROVAL_STAGE == true }
                }
                steps {
                    input message: "Approve deployment to ${config.ENVIRONMENT} environment?"
                }
            }

            /* ---------- PLAYBOOK EXECUTION ---------- */
            stage('Run Ansible Playbook') {
                steps {
                    dir(config.ANSIBLE_DIR) {
                        withCredentials([
                            [$class: 'AmazonWebServicesCredentialsBinding',
                             credentialsId: 'aws-cred',
                             accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                             secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']
                        ]) {
                            sh """
                            ansible-playbook ${config.PLAYBOOK} \
                              -i ${config.INVENTORY} \
                              -u ${config.SSH_USER} \
                              --private-key ${config.SSH_KEY}
                            """
                        }
                    }
                }
            }
        }

        /* ---------- NOTIFICATION ---------- */
        post {
            always {
                script {
                    def status = currentBuild.result ?: "SUCCESS"
                    def color = status == "SUCCESS" ? "good" : "danger"
                    def emoji = status == "SUCCESS" ? ":white_check_mark:" : ":x:"

                    emailext(
                        subject: "Ansible Job - ${env.JOB_NAME} #${env.BUILD_NUMBER} - ${status}",
                        body: """
                        Job: ${env.JOB_NAME}
                        Build: ${env.BUILD_NUMBER}
                        Status: ${status}
                        URL: ${env.BUILD_URL}
                        """,
                        to: config.EMAIL
                    )

                    slackSend(
                        channel: config.SLACK_CHANNEL_NAME,
                        color: color,
                        message: """
${emoji} *Ansible Pipeline Status*
*Environment:* ${config.ENVIRONMENT}
*Status:* ${status}
*Message:* ${config.ACTION_MESSAGE}

🔗 <${env.BUILD_URL}|Console Output>
"""
                    )
                }
            }
        }
    }
}
