pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh './gradlew check --no-daemon'
      }
    }
  }
  post {
    always {
      junit '**/build/test-results/**/*.xml'
    }
  }
}
