pipeline {
  agent any
  stages {
    stage('build / test') {
      steps {
        sh './gradlew check --no-daemon'
      }
    }
  }
}