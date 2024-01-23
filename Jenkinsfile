pipeline {
    // Define the Docker agent configuration:
    // - Use the Maven image with AdoptOpenJDK 11 as the base image.
    // - Set the user to 'root' to ensure necessary privileges for Docker-related operations.
    // - Mount the Docker socket from the host to the container to enable interaction with the host's Docker daemon.
    agent {
        docker { 
            image 'maven:3.8.1-adoptopenjdk-11'
            args '--user root -v /var/run/docker.sock:/var/run/docker.sock' // mount Docker socket to access the host's Docker daemon
        }
    }

    environment {
        AWS_ACCOUNT_ID="895072465878"
        AWS_DEFAULT_REGION="us-east-1" 
        IMAGE_REPO_NAME="my-docker-repo"
        IMAGE_TAG="latest"
        REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"
    }

    stages {
    
        stage('Logging into AWS ECR') {
            steps {
                script {
                    sh 'aws ecr get-login-password - region ${AWS_DEFAULT_REGION} | docker login - username AWS - password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com'
                }
            }
        }
        
        stage('Cloning Git') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '', url: 'https://github.com/Mik3asg/Java-Springboot-App-Jenkins-Helm-EKS-Demo.git']]]) 
            }
        }

        // Building Docker image
        stage('Building image') {
            steps {
                script {
                    dockerImage = docker.build "${IMAGE_REPO_NAME}"
                }
            }
        }

        // Pushing Docker Image to AWS ECR
        stage ('Pushing to ECR') {
            steps {
                script {
                    //sh "docker tag ${IMAGE_REPO_NAME}:${IMAGE_TAG} ${REPOSITORY_URI}:$IMAGE_TAG"
                    //sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}"
                    docker.withRegistry('https://${AWS_ACCOUNT_ID}.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:aws-credentials') {                    
                    dockerImage.push("${env.BUILD_NUMBER}")
                    dockerImage.push("latest")

                    }
                }
            }
        }

        stage('Update Deployment File') {
            environment {
                GIT_REPO_NAME = "Java-Springboot-App-Jenkins-Helm-EKS-Demo"
                GIT_USER_NAME = "Mik3asg"
            }
            steps {
                withCredentials([string(credentialsId: 'github', variable: 'GITHUB_TOKEN')]) {
                    sh '''
                        git config user.email "mik3asg@gmail.com"
                        git config user.name "Mickael"
                        BUILD_NUMBER=${BUILD_NUMBER}
                        sed -i "s/replaceImageTag/${BUILD_NUMBER}/g" manifests/deployment.yml
                        git add -manifests/deployment.yml
                        git commit -m "Update deployment image to version ${BUILD_NUMBER}"
                        git push https://${GITHUB_TOKEN}@github.com/${GIT_USER_NAME}/${GIT_REPO_NAME} HEAD:main
                    '''
                }
            }
        }
    }
}

