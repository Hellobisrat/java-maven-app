def buildJar() {
    echo 'building the application...'
  
}

def buildImage() {
    echo "building the docker image..."
   
}

def deployApp() {
    environment {
        AWS_ACCESS_KEY_ID= credentials('jenkins_aws_access_key_id')
AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws_secret_access_key')
    }
    steps {
        echo 'deploying step changed'
    sh 'kubectl create deployment ngnix-deployment --image nginx'
    }
    
}

return this
