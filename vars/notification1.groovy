def call(String status) {

    // Colors for Slack
    def color = (status == 'SUCCESS') ? 'good' : 'danger'
    def emoji = (status == 'SUCCESS') ? '✅' : '❌'

    /* =========================
       SLACK NOTIFICATION
    ========================== */
    slackSend(
        channel: '#notifications',
        color: color,
        message: """
${emoji} *Build ${status}*
*Job:* ${env.JOB_NAME}
*Build Number:* ${env.BUILD_NUMBER}
*Build URL:* ${env.BUILD_URL}
"""
    )

    /* =========================
       EMAIL NOTIFICATION
    ========================== */
    mail(
        to: 'sachinraj1652@gmail.com',
        subject: "${emoji} Build ${status}: ${env.JOB_NAME}",
        body: """
Hello Team,

Build Status: ${status}
Job Name    : ${env.JOB_NAME}
Build No    : ${env.BUILD_NUMBER}

Build URL:
${env.BUILD_URL}

Regards,
Jenkins CI
"""
    )
}
