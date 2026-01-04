def call(String projectName, String projectKey) {

    stage('SonarQube Analysis') {
        withSonarQubeEnv('sonar') {
            sh """
            mvn sonar:sonar \
            -Dsonar.projectName=${projectName} \
            -Dsonar.projectKey=${projectKey}
            """
        }
    }
}
