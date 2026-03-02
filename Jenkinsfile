pipeline {
    agent any

    tools {
        maven 'maven-3.9' // Имя должно совпадать с настройками в Manage Jenkins -> Tools
    }

    stages {
        stage('Build Contracts') {
            steps {
                // Устанавливаем библиотеки в локальный репозиторий .m2
                sh 'cd SOPstoyak/stoyak-api-contract && mvn clean install'
                sh 'cd events-contract && mvn clean install'
            }
        }

        stage('Build Services') {
            steps {
                // Сборка всех 4-х микросервисов (создание папок target)
                sh 'cd demo-rest && mvn clean package -DskipTests'
                sh 'cd audit-service && mvn clean package -DskipTests'
                sh 'cd notificationservice && mvn clean package -DskipTests'
                sh 'cd grpc-server && mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                // Перезапуск всей системы в Docker
                sh 'docker-compose down'
                sh 'docker-compose up --build -d'
            }
        }

        stage('Smoke Test') {
            steps {
                echo 'Ожидание запуска (20s)...'
                sleep 20
                // Простая проверка: отвечает ли API код 200
                sh 'curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/api | grep 200'
            }
        }
        
        stage('Manual Approval') {
            steps {
                // Пауза для проверки Grafana и Zipkin перед завершением
                input message: 'Метрики в Grafana (3000) отображаются?', ok: 'Да!'
            }
        }
    }
}pipeline {
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

        stage('Build & Install') {
            steps {
                echo 'Building all projects...'
                // Сборка контрактов и сервисов
                sh 'cd SOPstoyak && mvn clean install'
                sh 'cd events-contract && mvn clean install'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy Infrastructure') {
            steps {
                echo 'Starting Docker Compose...'
                sh 'docker-compose down'
                sh 'docker-compose up --build -d'
            }
        }

        // === СТАДИЯ СКРИПТОВОГО ТЕСТИРОВАНИЯ ===
        stage('API Integration Test') {
            steps {
                script {
                    echo 'Waiting for services to initialize (30s)...'
                    sleep 30
                    
                    // 1. Тестируем REST + gRPC (вызов аналитики через основной сервис)
                    echo 'Testing User Rating flow (REST -> gRPC)...'
                    def response = sh(
                        script: 'curl -s http://localhost:8081/api/users/123/rate', 
                        returnStdout: true
                    ).trim()
                    
                    echo "Server response: ${response}"
                    
                    if (response.contains("Rating calculated")) {
                        echo "SUCCESS: gRPC communication between demo-rest and analytics-service is working!"
                    } else {
                        error "FAILURE: API returned unexpected response: ${response}"
                    }

                    // 2. Тестируем точку входа HATEOAS
                    echo 'Testing HATEOAS Entry Point...'
                    def hateoasCheck = sh(
                        script: 'curl -s http://localhost:8081/api', 
                        returnStdout: true
                    ).trim()
                    
                    if (hateoasCheck.contains("_links")) {
                        echo "SUCCESS: HATEOAS links are present."
                    } else {
                        error "FAILURE: HATEOAS links not found in /api"
                    }
                }
            }
        }

        // === СТАДИЯ РУЧНОГО ПОДТВЕРЖДЕНИЯ (Manual Test) ===
        stage('Manual Approval') {
            steps {
                echo '--- ПРОВЕРКА МЕТРИК ---'
                echo 'Зайдите на http://localhost:3000 (Grafana)'
                echo 'Зайдите на http://localhost:9411 (Zipkin)'
                
                // Это остановит выполнение и будет ждать вашего клика в Jenkins UI
                input message: 'Вы проверили метрики в Grafana и трейсы в Zipkin? Всё в порядке?', ok: 'Да, подтверждаю'
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            echo 'All tests passed and system is healthy!'
        }
        failure {
            echo 'Pipeline failed. Check logs for details.'
        }
    }
}
