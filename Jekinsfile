pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'incident-api'
        DOCKER_IMAGE_FRONTEND = 'incident-frontend'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                echo 'Building Spring Boot...'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                echo 'Building backend Docker image...'
                sh "docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} ."
                sh "docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                echo 'Building frontend Docker image...'
                sh "docker build -t ${DOCKER_IMAGE_FRONTEND}:${BUILD_NUMBER} ./frontend"
                sh "docker tag ${DOCKER_IMAGE_FRONTEND}:${BUILD_NUMBER} ${DOCKER_IMAGE_FRONTEND}:latest"
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo 'Deploying...'
                sh '''
                docker-compose down || true
                docker-compose up -d --build
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Deployment successful!'
        }
        failure {
            echo '❌ Deployment failed!'
        }
    }
}