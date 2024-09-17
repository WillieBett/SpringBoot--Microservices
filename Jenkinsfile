pipeline {
    agent any
    
        stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/WillieBett/Springbootkafka.git'
            }
        }
        stage('Build') {
            steps {
                script {
                    bat 'mvn clean install'
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    bat 'mvn test'
                }
            }
        }
        stage('Package') {
            steps {
                script {
                    bat 'mvn package'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                // Add deployment steps here (e.g., copying files or deploying to a server)
            }
        }
    }

    post {
        success {
            echo 'Build successful!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
