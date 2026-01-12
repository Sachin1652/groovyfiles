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
                        withCredentials([
                            [$class: 'AmazonWebServicesCredentialsBinding',
                             credentialsId: 'aws-cred',
                             accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                             secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']
                        ]) {
                            sh """
                            echo "Testing AWS credentials..."
                            aws sts get-caller-identity

                            export AWS_ACCESS_KEY_ID=\$AWS_ACCESS_KEY_ID
                            export AWS_SECRET_ACCESS_KEY=\$AWS_SECRET_ACCESS_KEY
                            export AWS_DEFAULT_REGION=${config.AWS_REGION}

                            echo "Checking inventory..."
                            ansible-inventory -i ${config.INVENTORY} --graph

                            echo "Running Ansible Playbook..."
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

        post {
            always {
                script {
                    def status = currentBuild.result ?: "SUCCESS"
                    def color  = status == "SUCCESS" ? "good" : "danger"
                    def emoji  = status == "SUCCESS" ? ":white_check_mark:" : ":x:"

                    slackSend(
                        channel: config.SLACK_CHANNEL_NAME,
                        color: color,
                        message: """
${emoji} *Ansible Deployment Status*
*Environment:* ${config.ENVIRONMENT}
*Status:* ${status}

🔗 <${env.BUILD_URL}|View Console>
"""
                    )

                    emailext(
                        subject: "Ansible Job ${env.JOB_NAME} #${env.BUILD_NUMBER} - ${status}",
                        body: """
Job: ${env.JOB_NAME}
Build: ${env.BUILD_NUMBER}
Status: ${status}
URL: ${env.BUILD_URL}
""",
                        to: config.EMAIL
                    )
                }
            }
        }
    }
}
