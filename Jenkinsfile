@Library('jenkins-shared-library') _

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

        stage('init') {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }

        stage('test') {
            when {
                expression { params.executeTests }
            }
            steps {
                echo "Running tests after change for the third ....."
            }
        }
      stage('increment version') {
    steps {
        script {
            echo 'incrementing app version...'

            sh '''
                mvn build-helper:parse-version
                mvn versions:set -DnewVersion=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)-${BUILD_NUMBER}
                mvn versions:commit
            '''

            def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
            def version = matcher[0][1]
            env.IMAGE_NAME = "${version}"
        }
    }
}



        stage('build app'){
            steps {
                script {
                    echo 'building the application...'
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build jar') {
            steps {
                buildJar()
            }
        }

        stage('build image') {
            steps {
                script {
                    buildImage "bisrat1/java-maven-app:${IMAGE_NAME}"
                    dockerLogin()
                    dockerPush "bisrat1/java-maven-app:${IMAGE_NAME}"
                }
            }
        }
        

        stage('Deploy') {
            steps {
                script {
                    deployApp()
                }
            }
        }
        stage('commit version update') {
    steps {
        script {
            withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {

                sh 'git config user.email "jenkins@example.com"'
                sh 'git config user.name "jenkins"'

                sh "git remote set-url origin https://${USER}:${PASS}@github.com/Hellobisrat/java-maven-app.git"

                
                sh 'git add .'
                sh 'git commit -m "ci: version bump" || echo "No changes to commit"'
                sh 'git push origin HEAD:main'
            }
        }
    }
}


    }
}
