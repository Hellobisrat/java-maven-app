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

        stage('Build jar') {
            steps {
                echo "Building the application version ${VERSION}"
                sh 'mvn clean package'
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

        stage('Docker Build & Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId:'docker-hub-repo',
                    passwordVariable:'PASSWORD',
                    usernameVariable:'USERNAME'
                )]) {
                    sh """
                        docker build -t hellobisrat/java-maven-app:${VERSION} .
                        echo \$PASSWORD | docker login -u \$USERNAME --password-stdin
                        docker push hellobisrat/java-maven-app:${VERSION}
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

