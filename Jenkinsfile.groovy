pipeline {
    agent any

    stages {
            agent {
                label 'Built-In Node'
            }
        stage('Checkout') {
            steps {
                // Checkout your source code from Git
                git 'https://github.com/Ayoyinka2456/Jenkins-pipeline1.git'
                stash (name: 'source_code')
            }
        }

        stage('Build') {
            agent {
                label 'node1'
            }
            steps {
                // Use Maven or another build tool to build your project
                unstash 'source_code'
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            agent {
                label 'node1'
            }
            steps {
                // Run your tests (e.g., JUnit or other testing frameworks)
                sh 'mvn test'
                stash (name:'packaged_code' , includes: 'target/*.war')
            }
        }

        stage('Deploy to Tomcat') {
            agent {
                label 'node2_tomcat'
            }
            steps {
                // Copy the war file to Tomcat's webapps directory
                unstash 'packaged_code'
                // sh 'cp target/your-web-app.war /path/to/tomcat/webapps/'
                sh 'cp target/*.war apache-tomcat-7.0.94/webapps/'
                // Restart Tomcat to deploy the application
                sh 'sudo systemctl restart tomcat'
            }
        }
    }
}
