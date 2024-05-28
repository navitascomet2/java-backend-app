pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        AWS_ACCOUNT_ID = "891377337960"
        AWS_DEFAULT_REGION = "us-east-1"
        IMAGE_REPO_NAME = "back-end-mock-1"
        IMAGE_TAG = "latest"
        REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"
        GIT_REPO_URL = "https://github.com/navitascomet2/Back-End-Mock-1"
        K8S_CREDENTIALS_ID = "K8S-3"
    }

    stages {
        stage('Cloning Git') {
            steps {
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: '*/fb-devops-GCIBTC-42-new']], 
                    doGenerateSubmoduleConfigurations: false, 
                    extensions: [], submoduleCfg: [],
                    userRemoteConfigs: [
                        [
                            credentialsId: 'github', 
                            url: "${GIT_REPO_URL}"
                        ]
                    ]
                ])     
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
                sh 'mvn test'
            }
        }

        stage('Building image') {
            steps {
                script {
                    dockerImage = docker.build "${REPOSITORY_URI}:${IMAGE_TAG}"
                }
            }
        }

        stage('Pushing to ECR') {
            steps {  
                script {
                    sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${REPOSITORY_URI}"
                    sh "docker tag ${REPOSITORY_URI}:${IMAGE_TAG} ${REPOSITORY_URI}:${BUILD_NUMBER}"
                    sh "docker push ${REPOSITORY_URI}:${IMAGE_TAG}"
                    sh "docker push ${REPOSITORY_URI}:${BUILD_NUMBER}"
                }
            }
        }

        stage('K8S Deploy') {
            steps {   
                script {
                    withKubeConfig([credentialsId: "${K8S_CREDENTIALS_ID}", serverUrl: '']) {
                        sh 'kubectl apply -f eks-deploy-k8s.yaml'
                        sh 'kubectl rollout restart deployment back-end-mock-1'
                    }
                }
            }
        }
    }
}
