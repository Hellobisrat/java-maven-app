def buildJar() {
    echo 'building the application...'
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t nanatwn/demo-app:$IMAGE_NAME .'
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push Hellobisrat/demo-app:$IMAGE_NAME'
    }
}

def deployApp() {
    echo 'deploying the application...'
}

return this
