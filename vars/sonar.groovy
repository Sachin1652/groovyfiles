def call(String projectName, String projectKey) {

    stage('SonarQube Analysis') {

        // Resolve Sonar Scanner tool path from Jenkins
        def scannerHome = tool 'sonar-scanner'

        withSonarQubeEnv('sonar-scanner') {
            sh """
            ${scannerHome}/bin/sonar-scanner \
            -Dsonar.projectName=${projectName} \
            -Dsonar.projectKey=${projectKey} \
            -Dsonar.sources=. \
            -Dsonar.java.binaries=target
            """
        }
    }
}
