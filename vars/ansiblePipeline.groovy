import com.sachin.ansible.Clone
import com.sachin.ansible.Approval
import com.sachin.ansible.AnsibleRunner
import com.sachin.ansible.Notifier

def call(Map config) {

    pipeline {
        agent any

        environment {
            ANSIBLE_FORCE_COLOR = 'true'
            ANSIBLE_HOST_KEY_CHECKING = 'False'
            AWS_DEFAULT_REGION = 'ap-south-1'
            SLACK_CHANNEL = config.SLACK_CHANNEL
        }

        stages {

            stage('Checkout Code') {
                steps {
                    script {
                        new Clone(this).run(config)
                    }
                }
            }

            stage('User Approval') {
                when {
                    expression { config.KEEP_APPROVAL_STAGE == true }
                }
                steps {
                    script {
                        new Approval(this).run(config)
                    }
                }
            }

            stage('Ansible Execution') {
                steps {
                    script {
                        new AnsibleRunner(this).run(config)
                    }
                }
            }
        }

        post {
            success {
                script {
                    new Notifier(this).success(config)
                }
            }
            failure {
                script {
                    new Notifier(this).failure(config)
                }
            }
        }
    }
}
