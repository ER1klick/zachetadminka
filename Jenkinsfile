pipeline {
    agent any

    tools {

        maven 'maven-3.9'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Shared Contracts') {
            steps {
                // Используем 'sh' вместо 'bat' для Linux-контейнера Jenkins
                sh 'cd SOPstoyak && mvn clean install'
                sh 'cd events-contract && mvn clean install'
            }
        }

        stage('Build Services') {
            steps {
                // Собираем все микросервисы
                sh 'cd demo-rest && mvn clean package -DskipTests'
                sh 'cd audit-service && mvn clean package -DskipTests'
                sh 'cd notificationservice && mvn clean package -DskipTests'
                sh 'cd grpc-server && mvn clean package -DskipTests'
            }
        }

        stage('Docker Deployment') {
            steps {
                // Перезапускаем систему. 
                // Jenkins внутри контейнера должен иметь доступ к docker.sock хоста
                sh 'docker-compose down'
                sh 'docker-compose up --build -d'
            }
        }
    }
}
