package com.sachin.ansible

class AnsibleRunner {
    def steps
    AnsibleRunner(steps) { this.steps = steps }

    def run(Map config) {
        steps.dir(config.CODE_BASE_PATH) {
            steps.withCredentials([
                [$class: 'AmazonWebServicesCredentialsBinding',
                 credentialsId: config.AWS_CREDENTIALS,
                 accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                 secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']
            ]) {
                steps.sh """
                ansible-playbook playbook.yml \
                  -i aws_ec2.aws_ec2.yml \
                  -u ubuntu \
                  --private-key ${config.SSH_KEY_PATH}
                """
            }
        }
    }
}
