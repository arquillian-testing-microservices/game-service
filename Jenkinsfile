node {

   stage('Preparation') { // for display purposes
      // Get some code from a GitHub repository
      checkout scm
   }
   stage('Build') {
      sh "./gradlew clean compileTestJava"
   }
   stage('Consumer Tests') {
      withEnv(['publishcontracts=true']) {
        sh "./gradlew test"
      }
   }

   stage('Publish Results') {
      junit '**/build/test-results/TEST-*.xml'
   }
}