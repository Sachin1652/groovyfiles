def call(String projectName, String projectKey) {

    stage('SonarQube Analysis') {
        withSonarQubeEnv('sonar') {
            sh """
            sonar-scanner \
            -Dsonar.projectName=${projectName} \
            -Dsonar.projectKey=${projectKey} \
            -Dsonar.sources=. \
            -Dsonar.java.binaries=target
            """
        }
    }
}
