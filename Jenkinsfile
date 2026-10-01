
pipeline {
    agent any

    parameters {
        choice(
            name: 'VERSION',
            choices: ['1.1.0', '1.2.0', '1.3.0'],
            description: 'Select version'
        )
        booleanParam(
            name: 'executeTests',
            defaultValue: true,
            description: 'Run tests?'
        )
    }

    tools {
        maven 'maven-3.9'
    }

    stages {

        stage('Build') {
            steps {
                echo "Building version ${VERSION}"
                sh "mvn clean package"
            }
        }

        stage('Test') {
            when {
                expression { return executeTests }
            }
            steps {
                echo "Running tests for version ${VERSION}"
                sh "mvn test"
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t bisrat1/demo-app:${VERSION} ."
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-repo',
                    usernameVariable: 'USERNAME',
                    passwordVariable: 'PASSWORD'
                )]) {
                    sh """
                        docker build -t  hellobisrat/java-maven-app:jma-2.0 .
                        echo \$PASSWORD | docker login -u \$USERNAME --password-stdin
                        docker push bisrat1/demo-app:${VERSION}
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying version ${VERSION}"
            }
        }
    }
}
