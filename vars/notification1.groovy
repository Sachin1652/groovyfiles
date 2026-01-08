def call(String status) {

    def colorMap = [
        'SUCCESS' : '#2ecc71',   // green
        'FAILURE' : '#e74c3c',   // red
        'UNSTABLE': '#f39c12'    // orange
    ]

    def color = colorMap.get(status, '#3498db')

    def buildUrl = env.BUILD_URL
    def jobName  = env.JOB_NAME
    def buildNo  = env.BUILD_NUMBER
    def user     = env.BUILD_USER ?: 'Jenkins'

    /* ================= EMAIL ================= */
    emailext(
        to: 'sachinraj1652@gmail.com',
        subject: "🚀 Jenkins Build ${status} | ${jobName} #${buildNo}",
        mimeType: 'text/html',
        body: """
        <html>
        <body style="font-family: Arial, sans-serif; background-color:#f4f6f8; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#ffffff; border-radius:8px; overflow:hidden;">
                
                <div style="background:${color}; padding:16px; text-align:center; color:white;">
                    <h2>🚀 Jenkins CI Build Report</h2>
                </div>

                <div style="padding:20px;">
                    <p><b>Project:</b> ${jobName}</p>
                    <p><b>Build Number:</b> #${buildNo}</p>
                    <p><b>Triggered By:</b> ${user}</p>

                    <p style="font-size:16px;">
                        <b>Status:</b>
                        <span style="color:${color}; font-weight:bold;">${status}</span>
                    </p>

                    <hr/>

                    <p>📦 <b>Generated Reports:</b></p>
                    <ul>
                        <li>Unit Test Report</li>
                        <li>Code Quality (SonarQube)</li>
                        <li>Code Coverage</li>
                        <li>Artifact Packaging</li>
                    </ul>

                    <div style="text-align:center; margin-top:20px;">
                        <a href="${buildUrl}"
                           style="background:${color}; color:white; padding:10px 18px;
                                  text-decoration:none; border-radius:6px;">
                            🔗 View Build in Jenkins
                        </a>
                    </div>
                </div>

                <div style="background:#f1f1f1; padding:10px; text-align:center; font-size:12px;">
                    Jenkins CI – Automated Notification
                </div>
            </div>
        </body>
        </html>
        """
    )

    /* ================= SLACK ================= */
    slackSend(
        channel: '#notifications',
        color: status == 'SUCCESS' ? 'good' : (status == 'FAILURE' ? 'danger' : 'warning'),
        message: """
🚀 *Jenkins Build ${status}*
*Project:* ${jobName}
*Build:* #${buildNo}
*Triggered By:* ${user}
🔗 ${buildUrl}
"""
    )
}
