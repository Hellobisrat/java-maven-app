def buildJar() {
    echo 'building the application ...'
}

def buildImage() {
    echo "building the docker image new update..."
}

def deployApp() {

    echo 'deploying step changed'

    withCredentials([
        string(credentialsId: 'jenkins_aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
        string(credentialsId: 'jenkins-aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY')
    ]) {

        sh '''
            echo "Using AWS credentials..."
            kubectl create deployment nginx-deployment --image nginx || true
        '''
    }
}

return this
