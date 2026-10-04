def gv
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


        stage('Build jar') {
           
            steps {
               buildJar()

            }
        }

        stage('build image') {
           
            steps {
               script{
                  buildImage 'bisrat1/java-maven-app:jama-3.0'
               }
            }
        }

        stage('Deploy') {
           
            steps {
               script{
                deployApp()
               }
            }
        }
    }
}
