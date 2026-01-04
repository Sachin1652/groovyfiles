def call(String status) {

    emailext(
        subject: "Spring3Hibernate Build - ${status}",
        body: """
        <h2>Build Status: ${status}</h2>
        <p>Job: ${env.JOB_NAME}</p>
        <p>Build Number: ${env.BUILD_NUMBER}</p>
        <p>URL: ${env.BUILD_URL}</p>
        """,
        mimeType: 'text/html',
        to: 'sachinraj1652@gmail.com'
    )
}
