def call(String projectName, String projectKey) {

    stage('Code Quality - SonarQube') {

        def scannerHome = tool 'sonar-scanner'

        withSonarQubeEnv('sonar-scanner') {

            sh """
            export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
            export PATH=\$JAVA_HOME/bin:\$PATH

            echo "Using Java version:"
            java -version

            ${scannerHome}/bin/sonar-scanner \
              -Dsonar.projectName=${projectName} \
              -Dsonar.projectKey=${projectKey} \
              -Dsonar.sources=. \
              -Dsonar.java.binaries=target
            """
        }
    }
}
