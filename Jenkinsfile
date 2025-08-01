def version = "v0.${BUILD_NUMBER}"

pipeline {
    agent any

    environment {
        GITHUB_USERNAME = 'Julia2131'
        GITHUB_CREDENTIAL = credentials('token-github') // Credential ID trong Jenkins cho GitHub
        DOCKERHUB_CREDENTIAL = credentials('dockerhub-token') // Credentials cho DockerHub
        IMAGE_NAME = "${DOCKERHUB_CREDENTIAL_USR}/paintshop"
        REGISTRY_URL = 'docker.io'
        VERSION = "${version}"  // Phiên bản image
        SONAR_PROJECT_KEY = 'sonar-token'  // Thay hợp lý nếu cần
        SONAR_ENV = 'SonarQube'
    }

    stages {
        stage('Checkout Source') {
            steps {
                echo 'Checkout source from GitHub'
                git url: 'https://github.com/baonhi12/paintshop.git', branch: 'main', credentialsId: 'github-user-login'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def mvn = tool 'Default Maven';
                    withSonarQubeEnv("${SONAR_ENV}") {
                        if (isUnix()) {
                            sh 'mvn clean verify sonar:sonar'
                        } else {
                            bat 'mvn clean verify sonar:sonar -Dsonar.projectKey=paintshop -Dsonar.projectName='paintshop''
                        }
                    }
                    echo 'SonarQube Analysis completed'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error("❌ Quality Gate failed: ${qg.status}")
                        } else {
                            echo 'Quality Gate passed.'
                        }
                    }
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                echo 'Building Docker image'
                script {
                    withDockerRegistry(credentialsId: 'dockerhub-creds', url: 'https://index.docker.io/v1/') {
                        if (isUnix()) {
                            sh "mvn clean package"
                            sh "docker build -t ${IMAGE_NAME}:${VERSION} ."
                            sh "docker push ${IMAGE_NAME}:${VERSION}"
                        } else {
                            bat "mvn clean package"
                            bat "docker build -t ${IMAGE_NAME}:${VERSION} ."
                            bat "docker push ${IMAGE_NAME}:${VERSION}"
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
                        sh "docker-compose -f deploy/docker-compose.yml pull"
                        sh "docker-compose -f deploy/docker-compose.yml up -d"
                    } else {
                        bat "docker-compose -f deploy\\docker-compose.yml pull"
                        bat "docker-compose -f deploy\\docker-compose.yml up -d"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
