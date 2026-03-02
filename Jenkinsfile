pipeline {
    agent any

    // Этот блок заставит Jenkins скачать и настроить Maven перед выполнением
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
                // Заходим в папку с pom.xml и устанавливаем библиотеку
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
                // Используем docker-compose (с дефисом)
                // Добавляем || true, чтобы билд не падал, если нечего останавливать (первый запуск)
                sh 'docker-compose down || true'
                sh 'docker-compose up --build -d'
            }
        }

        stage('API Test') {
            steps {
                echo 'Waiting for services to start...'
                sleep 30
                // Проверка, что demo-rest отвечает
                sh 'curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/api | grep 200'
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: Все этапы пройдены, система развернута!'
        }
        failure {
            echo 'FAILURE: Ошибка при сборке или деплое.'
        }
    }
}
