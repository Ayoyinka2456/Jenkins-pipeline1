pipeline {
    agent {
        label
    } 'node1'
    tools {
        maven 'Maven'
        jdk 'Java11'
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
                stash(name: 'packaged_code', includes: 'target/*.war')
            }
        }
        stage('Deploy to Tomcat') {
            agent {
                label 'node2_tomcat'
            }
            steps {
                unstash 'packaged_code'
                sh "sudo rm -rf ~/apache*/webapp/*.war"
                sh "sudo mv target/*.war ~/apache*/webapps/"
                sh "sudo ~/apache*/bin/shutdown.sh && sudo ~/apache*/bin/startup.sh"
            }
        }
    }
        post{
            always {
                script {
                    def frowny = '😞'
                    def smiley = '😄'
                    def what_reaction = null
                    if [[ $BUILD_STATUS == "Successful" ]]; then
                        what_reaction=$smiley
                    else
                        what_reaction=$frowny
                    fi

                    emailext body: 'Check console output at $BUILD_URL to view the results.', 
                    subject: '$what_reaction $PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', 
                    to: 'eas.adeyemi@gmail.com'
                }
            }
        }     
    }
