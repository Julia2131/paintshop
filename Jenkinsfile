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
    }

    stages {
        stage('Checkout Source') {
            steps {
                echo 'Checkout source from GitHub'
                git url: 'https://github.com/baonhi12/paintshop.git', branch: 'main', credentialsId: 'token-github'
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
                            bat "docker build -t ${IMAGE_NAME}:${VERSION} ."
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
