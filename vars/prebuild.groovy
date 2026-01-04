def call(String repoUrl) {

    stage('Git Checkout') {
        git url: repoUrl
    }

    stage('Code Compile') {
        sh 'mvn clean compile'
    }

    stage('Unit Tests') {
        sh 'mvn test'
    }
}
