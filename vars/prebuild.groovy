def call(String repoUrl){
  stage('Git Checkout') {
        git repoUrl
    }

    stage('Code Compile') {
        sh 'mvn clean compile'
    }

    stage('Unit Tests') {
        sh 'mvn test'
    }
}
