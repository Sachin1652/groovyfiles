def call(Map config = [:]) {

    def props = readProperties file: config.configFile ?: 'ansible-config.properties'

    def SLACK_CHANNEL_NAME  = props.SLACK_CHANNEL_NAME
    def ENVIRONMENT         = props.ENVIRONMENT
    def CODE_BASE_PATH      = props.CODE_BASE_PATH
    def ACTION_MESSAGE      = props.ACTION_MESSAGE
    def KEEP_APPROVAL_STAGE = props.KEEP_APPROVAL_STAGE.toBoolean()

    pipeline {
        agent any

        environment {
            AWS_DEFAULT_REGION = 'ap-south-1'
            ANSIBLE_FORCE_COLOR = 'true'
            ANSIBLE_HOST_KEY_CHECKING = 'False'
        }

        stages {

            stage('Clone') {
                steps {
                    echo "Cloning repository..."
                    checkout scm
                }
            }

            stage('User Approval') {
                when {
                    expression { KEEP_APPROVAL_STAGE == true }
                }
                steps {
                    input message: "Approve Ansible execution for ${ENVIRONMENT} environment?"
                }
            }

            stage('Playbook Execution') {
                steps {
                    dir(CODE_BASE_PATH) {
                        withCredentials([
                            [$class: 'AmazonWebServicesCredentialsBinding',
                             credentialsId: 'aws-cred',
                             accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                             secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']
                        ]) {
                            sh '''
                            export LANG=en_US.UTF-8
                            export LC_ALL=en_US.UTF-8
                            ansible-playbook playbook.yml \
                              -i aws_ec2.aws_ec2.yml \
                              -u ubuntu \
                              --private-key ~/.ssh/aws2.pem
                            '''
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
                        channel: "#${SLACK_CHANNEL_NAME}",
                        color: color,
                        message: """
${emoji} *Ansible Shared Library Notification*
*Environment:* ${ENVIRONMENT}
*Status:* ${status}
*Message:* ${ACTION_MESSAGE}

🔗 <${BUILD_URL}|View Build>
"""
                    )
                }
            }
        }
    }
}
