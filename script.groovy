def buildJar() {
    echo 'building the application...'
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t nanatwn/demo-app:jma-2.0 .'
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push nanatwn/demo-app:jma-2.0'
    }
}

def deployApp() {
    environment {
        AWS_ACCESS_KEY_ID= credentials('jenkins_aws_access_key_id')
AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws_secret_access_key')
    }
    steps {
        echo 'deploying the application...'
    sh 'kubectl create deployment ngnix-deployment --image nginx'
    }
    
}

return this
