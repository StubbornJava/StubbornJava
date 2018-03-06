pipeline {
  agent any
  stages {
    stage('Clone') {
      steps {
        git(url: 'https://github.com/StubbornJava/StubbornJava.git', branch: 'master', credentialsId: '2ed332b1-8655-4d97-9cfe-dddd64bb5f4d', poll: true)
      }
    }
  }
}