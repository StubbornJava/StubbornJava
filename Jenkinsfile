pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh './gradlew clean test --no-daemon'
      }
    }
  }
}