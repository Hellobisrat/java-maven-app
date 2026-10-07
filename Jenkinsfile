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

        stage('Test') {
            
            steps {
                echo "Running tests for version ${VERSION}"
                echo "Executing pipeline for branch ${env.BRANCH_NAME}"
                sh "mvn test"
            }
        }

        stage('Build jar') {
          
            steps {
                echo "Building the application version ${VERSION}"
                sh 'mvn clean package'
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
                        docker build -t bisrat1/java-maven-app:${VERSION} .
                        echo \$PASSWORD | docker login -u \$USERNAME --password-stdin
                        docker push bisrat1/java-maven-app:${VERSION}
                    """
                }
            }
        }

        stage('Deploy') {
           steps {
              script {
                  echo 'deploying docker image to EC2...'
                  
                  def dockerComposeCmd ="docker-compose -f docker.compose.yaml up --detach"
                  sshagent(credentials: ['ec2-server-key'], executable: '') {
                    // some block
                     sh "scp docker-compose.yaml  ec2-user@54.85.3.217:/home/ec2-user"
                     sh "ssh -o StrictHostKeyChecking=no ec2-user@54.85.3.217 ${dockerComposeCmd}"
                     }
                   }
              }
        }
    }
}
