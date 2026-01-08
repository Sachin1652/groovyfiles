def call(String status) {

    slackSend channel: '#notifications',
              message: "Build ${status}: ${env.JOB_NAME} #${env.BUILD_NUMBER}"

    mail to: 'sachinraj1652@gmail.com',
         subject: "Build ${status}: ${env.JOB_NAME}",
         body: "Job: ${env.JOB_NAME}\nBuild: ${env.BUILD_NUMBER}\nStatus: ${status}"
}
