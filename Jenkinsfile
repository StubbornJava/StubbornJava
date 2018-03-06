pipeline {
  agent any
  stages {
    stage('Build') {
      parallel {
        stage('Build') {
          steps {
            sh './gradlew clean shadowJar --stacktrace --debug'
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
  environment {
    PATH = '/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin:/opt/aws/bin:/home/ec2-user/.local/bin:/home/ec2-user/bin'
    JAVA_HOME = '/usr/lib/jvm/jre-1.8.0-openjdk.x86_64'
  }
}