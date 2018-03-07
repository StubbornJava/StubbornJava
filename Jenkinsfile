pipeline {
  agent any
  stages {
    stage('Build') {
      parallel {
        stage('Build') {
          steps {
            sh './gradlew clean shadowJar --no-daemon'
          }
        }
        stage('echo') {
          steps {
            sh 'echo $JAVA_HOME'
          }
        }
      }
    }
  }
}