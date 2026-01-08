def call(String repoUrl) {

    stage('Git Checkout') {
        git url: repoUrl
    }

    stage('Compile') {
        sh 'mvn clean compile'
    }
}
