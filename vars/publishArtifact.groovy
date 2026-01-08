def call() {
    sh 'mvn package'
    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
}
