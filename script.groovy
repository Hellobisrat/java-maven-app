def buildJar() {
    echo 'building the application...'
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."

    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {

        sh """
            docker build -t bisrat1/demo-app:${IMAGE_NAME} .
            echo $PASS | docker login -u $USER --password-stdin
            docker push bisrat1/demo-app:${IMAGE_NAME}
        """
    }
}


def deployApp() {
    echo 'deploying the application...'
}

return this
