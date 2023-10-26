pipeline {
    agent {
        label 'node1'
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
                sh 'ls && pwd'
            }
        }
        stage('Deploy to Tomcat') {
            agent {
                label 'node2_tomcat'
            }
            steps {
                sh 'ls && pwd'
                sh 'sudo cp target/*.war ~/apache*/webapps/'
                sh 'sudo ~/apache*/bin/shutdown.sh && sudo ~/apache*/bin/startup.sh'
            }
        }
    }
}
