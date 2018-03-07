pipeline {
  agent any
  stages {
      stage('Test') {
          steps {
              sh './gradlew check --no-daemon'
          }
      }
  }
}
