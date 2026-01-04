def call() {
    stage('Deploy') {
        sh '''
        echo "Deploying Spring3Hibernate Application"
        cp target/*.war /opt/tomcat/webapps/
        '''
    }
}
