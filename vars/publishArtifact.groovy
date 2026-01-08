def call() {
    sh 'mvn package'
    archiveArtifacts artifacts: 'target/*.war,target/*.jar',
                     fingerprint: true,
                     allowEmptyArchive: false
}
