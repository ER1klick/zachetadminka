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
                // Собираем библиотеки-контракты
                sh 'cd SOPstoyak/stoyak-api-contract && mvn clean install'
                sh 'cd events-contract && mvn clean install'
            }
        }

        stage('Build Services') {
            steps {
                // Сборка всех 4-х микросервисов
                sh 'cd demo-rest && mvn clean package -DskipTests'
                sh 'cd audit-service && mvn clean package -DskipTests'
                sh 'cd notificationservice && mvn clean package -DskipTests'
                sh 'cd grpc-server && mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                // Перезапуск системы в Docker
                sh 'docker-compose down'
                sh 'docker-compose up --build -d'
            }
        }

        stage('API Test') {
            steps {
                echo 'Waiting for services...'
                sleep 20
                // Проверка жизнеспособности
                sh 'curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/api | grep 200'
            }
        }
    }

    post {
        success {
            echo 'Зачет получен! Все собралось и работает.'
        }
        failure {
            echo 'Что-то пошло не так. Проверь логи сборки.'
        }
    }
}
