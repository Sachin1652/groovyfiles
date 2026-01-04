def call() {
    stage('Deploy Application') {
        sh '''
        echo "Deploying WAR to Tomcat"
        sudo cp target/*.war /var/lib/tomcat9/webapps/
        sudo systemctl restart tomcat9
        '''
    }
}
