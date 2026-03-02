pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Shared Contracts') {
            steps {
                bat 'cd SOPstoyak && mvn clean install'
                bat 'cd events-contract && mvn clean install'
            }
        }

        stage('Build Services') {
            steps {
                bat 'cd demo-rest && mvn clean package -DskipTests'
                bat 'cd audit-service && mvn clean package -DskipTests'
                bat 'cd notificationservice && mvn clean package -DskipTests'
                bat 'cd grpc-server && mvn clean package -DskipTests'
            }
        }

        stage('Docker Deployment') {
            steps {

                bat 'docker-compose down'
                bat 'docker-compose up --build -d'
            }
        }
    }
}