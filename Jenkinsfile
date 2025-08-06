def version = "v0.${BUILD_NUMBER}"

pipeline {
    agent any
    environment {
        GITHUB_USERNAME = 'Julia2131'
        GITHUB_CREDENTIAL = credentials('token-github')
        DOCKERHUB_CREDENTIAL = credentials('dockerhub-token')
        IMAGE_NAME = "${DOCKERHUB_CREDENTIAL_USR}/paintshop"
        REGISTRY_URL = 'docker.io'
        VERSION = "${version}"
        SONAR_PROJECT_KEY = 'paintshop'
        SONAR_ENV = 'SonarQube'
        DB_URL = 'jdbc:mysql://localhost:3306/paintshop?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false'
        DB_USERNAME = 'root'
        DB_PASSWORD = '1234'

        DEPLOY_USER = 'it23'
        DEPLOY_HOST = '101.99.23.156'
        DEPLOY_PORT = '24001'
        DEPLOY_SCRIPT = '/home/it23/deploy.sh'
        WEBHOOK_URL = 'https://c6f710908854.ngrok-free.app/github-webhook'
    }

    stages {
        stage('Checkout Source') {
            steps {
                echo 'Cleanup and checkout source from SCM'
                script {
                    deleteDir()  // Clean workspace trước
                    checkout scm  // Sử dụng SCM tự động
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    try {
                        withSonarQubeEnv("${SONAR_ENV}") {
                            if (isUnix()) {
                                sh 'mvn clean verify sonar:sonar -DskipTests -Dsonar.projectKey=paintshop -Dsonar.projectName=paintshop'
                            } else {
                                bat 'mvn clean verify sonar:sonar -DskipTests -Dsonar.projectKey=paintshop -Dsonar.projectName=paintshop'
                            }
                        }
                        echo 'SonarQube Analysis completed'
                    } catch (Exception e) {
                        echo "SonarQube failed: ${e.getMessage()}"
                        echo 'Continuing pipeline without SonarQube...'
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    script {
                        try {
                            def qg = waitForQualityGate()
                            if (qg.status != 'OK') {
                                echo "⚠️ Quality Gate failed: ${qg.status}"
                            } else {
                                echo '✅ Quality Gate passed.'
                            }
                        } catch (Exception e) {
                            echo "Quality Gate check failed: ${e.getMessage()}"
                            echo 'Continuing pipeline...'
                        }
                    }
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                echo 'Building Docker image'
                script {
                    // Login to DockerHub using credentials
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-token', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        if (isUnix()) {
                            sh "mvn clean package -DskipTests"
                            sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                            sh "docker build -t ${IMAGE_NAME}:${VERSION} ."
                            sh "docker push ${IMAGE_NAME}:${VERSION}"
                            sh "docker logout"
                        } else {
                            bat "mvn clean package -DskipTests"
                            bat "echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin"
                            bat "docker build -t ${IMAGE_NAME}:${VERSION} -f Dockerfile ."
                            bat "docker push ${IMAGE_NAME}:${VERSION}"
                            bat "docker logout"
                        }
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo 'Deploying application using docker-compose'
                script {
                    if (isUnix()) {
                        sh "export IMAGE_TAG=${VERSION} && docker-compose -f deploy/docker-compose.yml pull"
                        sh "export IMAGE_TAG=${VERSION} && docker-compose -f deploy/docker-compose.yml up -d"
                    } else {
                        bat "set IMAGE_TAG=${VERSION} && docker-compose -f deploy\\docker-compose.yml pull"
                        bat "set IMAGE_TAG=${VERSION} && docker-compose -f deploy\\docker-compose.yml up -d"
                    }
                }
            }
        }
    }

    post {
        success {
            echo '🎉 Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed!'
        }
        always {
            echo "🔍 Build completed with version: ${VERSION}"
        }
    }
}
// pipeline {
//     agent any
//
//     environment {
//
//     }
//
//     stages {
//         stage('Checkout') {
//             steps {
//                 echo '📥 Checkout source code...'
//                 checkout scm
//             }
//         }
//
//         stage('Restore') {
//             steps {
//                 echo '🔧 Restoring dependencies...'
//                 bat 'dotnet restore'
//             }
//         }
//
//         stage('Build') {
//             steps {
//                 echo '🏗 Building project...'
//                 bat 'dotnet build --configuration Release'
//             }
//         }
//
//         stage('Publish') {
//             steps {
//                 echo '📦 Publishing app...'
//                 bat 'dotnet publish -c Release -o published'
//             }
//         }
//
//         stage('Archive Artifact') {
//             steps {
//                 echo '🗃 Archiving published output...'
//                 archiveArtifacts artifacts: 'published/**', fingerprint: true
//             }
//         }
//
//         stage('Deploy to Server') {
//             steps {
//                 echo '🚀 Deploying to production server via SSH...'
//                 sshagent (credentials: ['my-ssh-key']) {
//                     sh "ssh -p $DEPLOY_PORT $DEPLOY_USER@$DEPLOY_HOST 'bash $DEPLOY_SCRIPT'"
//                 }
//             }
//         }
//
//         stage('Notify Webhook') {
//             steps {
//                 echo '📡 Sending webhook notification...'
//                 sh "curl -X POST $WEBHOOK_URL -H 'Content-Type: application/json' -d '{\"status\": \"success\", \"job\": \"Paintshop CI/CD\"}'"
//             }
//         }
//     }
//
//     post {
//         failure {
//             echo '❌ Build failed. Notifying webhook...'
//             sh "curl -X POST $WEBHOOK_URL -H 'Content-Type: application/json' -d '{\"status\": \"failed\", \"job\": \"Paintshop CI/CD\"}'"
//         }
//     }
// }
//
//
// //tạo dockerfile và docker compose, xong push lên vào chạy jenkins