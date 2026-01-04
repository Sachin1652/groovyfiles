def call() {

    stage('Package') {
        sh 'mvn clean package -DskipTests'
    }

    stage('Deploy Application') {
        sh '''
        echo "Deploying Spring3Hibernate WAR"
        cp target/*.war /opt/tomcat/webapps/
        '''
    }
}
