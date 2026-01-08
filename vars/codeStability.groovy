def call() {
    sh 'mvn test'
    junit '**/target/surefire-reports/*.xml'
}
