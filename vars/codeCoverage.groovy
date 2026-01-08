def call() {
    sh 'mvn jacoco:prepare-agent test jacoco:report'
    publishHTML(target: [
        reportDir: 'target/site/jacoco',
        reportFiles: 'index.html',
        reportName: 'JaCoCo Coverage Report'
    ])
}
