def call(String projectName, String projectKey) {
    withSonarQubeEnv('sonar') {
        sh """
            \$SCANNER_HOME/bin/sonar-scanner \
            -Dsonar.projectName=${projectName} \
            -Dsonar.projectKey=${projectKey} \
            -Dsonar.java.binaries=.
        """
    }
}
