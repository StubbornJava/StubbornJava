pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '/usr/local/bin/gradle clean shadowJar'
      }
    }
  }
}