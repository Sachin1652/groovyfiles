def call() {

    def config = readYaml(text: libraryResource('ansible-config.yaml'))

    pipeline {
        agent any

        environment {
            ANSIBLE_FORCE_COLOR = 'true'
            ANSIBLE_HOST_KEY_CHECKING = 'False'
            AWS_DEFAULT_REGION = "${config.AWS_REGION}"
            SLACK_CHANNEL = "${config.SLACK_CHANNEL_NAME}"
        }

        stages {

            stage('Clone') {
                steps {
                    checkout scm
                }
            }

            stage('User Approval') {
                when {
                    expression { config.KEEP_APPROVAL_STAGE == true }
                }
                steps {
                    input message: "Approve deployment to ${config.ENVIRONMENT}?"
                }
            }

            stage('Run Ansible') {
                steps {
                    dir(config.ANSIBLE_DIR) {
                        sh "ls -l"
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

        post {
            always {
                echo "Pipeline completed"
            }
        }
    }
}
