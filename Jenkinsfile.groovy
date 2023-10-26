pipeline {
    agent {
        label 'node1'
    }
    tools {
        maven 'Maven'
        jdk 'Java11'
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Ayoyinka2456/Jenkins-pipeline1.git'
                // stash(name: 'source_code', includes: '**/*')
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
                stash (name: 'packaged_code', includes: 'target/*.war')
            }
        }

        stage('Deploy to Tomcat') {
            agent {
                label 'node2_tomcat'
            }
            steps {
                unstash 'packaged_code'
                sh "sudo cp target/*.war ~/apache*/webapps/"
                sh "sudo ~/apache*/bin/shutdown.sh && sudo ~/apache*/bin/startup.sh"
            }
        }

    }
}
