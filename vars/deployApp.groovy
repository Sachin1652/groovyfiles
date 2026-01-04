def call() {
    stage('Deploy Application') {
        sh '''
        echo "Deploying WAR to Tomcat"

        cp target/Spring3HibernateApp.war /var/lib/tomcat9/webapps/

        systemctl restart tomcat9
        '''
    }
}
