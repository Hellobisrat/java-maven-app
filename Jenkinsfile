#!/usr/bin/env groovy

pipeline {
    agent any

    tools {
        maven 'maven-3.9'
    }

    environment {
        APP_NAME = 'java-maven-app'
        AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
        DOCKER_REPO = 'bisrat1/java-maven-app'
        DOCKER_REPO_SERVER = 'https://index.docker.io/v1/'
    }

    stages {

       stage('increment version') {
    steps {
        script {
            echo "incrementing app version... ${env.BRANCH_NAME}"
            sh """
                mvn build-helper:parse-version versions:set \
                -DnewVersion=\\\$\\{parsedVersion.majorVersion}.\\\$\\{parsedVersion.minorVersion}.\\\$\\{parsedVersion.nextIncrementalVersion} \
                versions:commit
            """
            def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
            def version = matcher[0][1]
            env.IMAGE_NAME = "${version}-${BUILD_NUMBER}"
        }
    }
}


        stage('build app') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('build image') {
            steps {
                script {
                    echo "building the docker image...update"
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker build -t ${DOCKER_REPO}:${IMAGE_NAME} ."
                        sh "echo $PASS | docker login -u $USER --password-stdin ${DOCKER_REPO_SERVER}"
                        sh "docker push ${DOCKER_REPO}:${IMAGE_NAME}"
                    }
                }
            }
        }

        stage('deploy') {
    steps {
        echo 'deploying step changed'

        withCredentials([
            string(credentialsId: 'jenkins_aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
            string(credentialsId: 'jenkins_aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY')
        ]) {
            sh '''
                echo "Using AWS credentials..."
                export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
                export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY
                export AWS_DEFAULT_REGION=us-east-1

                kubectl version --client
                aws sts get-caller-identity

                envsubst < kubernetes/deployment.yaml | kubectl apply -f -
            '''
        }
    }
}




        stage('commit version update') {
    steps {
        script {
            withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {

                
                sh '''
                    git config user.email "zerbisrat@gmail.com"
                    git config user.name "Hellobisrat"

                    git remote set-url origin https://${USER}:${PASS}@github.com/Hellobisrat/java-maven-app.git


                    # Stash local changes (version bump)
                    git stash

                    # Pull latest changes from GitHub
                    git pull origin main --rebase

                    # Re-apply stashed changes
                    git stash pop || true

                    # Stage and commit version bump
                    git add .
                    git commit -m "ci: version bump" || true

                    # Push updated version
                    git push origin HEAD:main
                '''
            }
        }
    }
}

    }
}
