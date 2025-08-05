pipeline {
    agent any

    environment {
        DEPLOY_USER = 'it23'
        DEPLOY_HOST = '101.99.23.156'
        DEPLOY_PORT = '24001'
        DEPLOY_SCRIPT = '/home/it23/deploy.sh'
        WEBHOOK_URL = 'https://c6f710908854.ngrok-free.app/github-webhook'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '📥 Checkout source code...'
                checkout scm
            }
        }

        stage('Restore') {
            steps {
                echo '🔧 Restoring dependencies...'
                bat 'dotnet restore'
            }
        }

        stage('Build') {
            steps {
                echo '🏗 Building project...'
                bat 'dotnet build --configuration Release'
            }
        }

        stage('Publish') {
            steps {
                echo '📦 Publishing app...'
                bat 'dotnet publish -c Release -o published'
            }
        }

        stage('Archive Artifact') {
            steps {
                echo '🗃 Archiving published output...'
                archiveArtifacts artifacts: 'published/**', fingerprint: true
            }
        }

        stage('Deploy to Server') {
            steps {
                echo '🚀 Deploying to production server via SSH...'
                sshagent (credentials: ['my-ssh-key']) {
                    sh "ssh -p $DEPLOY_PORT $DEPLOY_USER@$DEPLOY_HOST 'bash $DEPLOY_SCRIPT'"
                }
            }
        }

        stage('Notify Webhook') {
            steps {
                echo '📡 Sending webhook notification...'
                sh "curl -X POST $WEBHOOK_URL -H 'Content-Type: application/json' -d '{\"status\": \"success\", \"job\": \"Paintshop CI/CD\"}'"
            }
        }
    }

    post {
        failure {
            echo '❌ Build failed. Notifying webhook...'
            sh "curl -X POST $WEBHOOK_URL -H 'Content-Type: application/json' -d '{\"status\": \"failed\", \"job\": \"Paintshop CI/CD\"}'"
        }
    }
}


//tạo dockerfile và docker compose, xong push lên vào chạy jenkins