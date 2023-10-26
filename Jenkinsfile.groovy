pipeline {
    agent {
        label 'Built-In Node'
    }
    tools {
        maven 'Maven'
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Ayoyinka2456/Jenkins-pipeline1.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Deploy to Tomcat') {
            agent {
                label 'node2_tomcat'
            }
            steps {
                sh 'sudo cp target/*.war ~/apache*/webapps/'
                sh 'sudo ~/apache*/bin/shutdown.sh && sudo ~/apache*/bin/startup.sh'
            }
        }
    }
}


