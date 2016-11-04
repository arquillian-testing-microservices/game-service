node {

   stage('Preparation') { // for display purposes
      // Get some code from a GitHub repository
      git checkout
   }
   stage('Build') {
      sh "./gradlew clean compileTestJava"
   }
   stage('Consumer Tests') {
      sh "./gradlew testJava"
   }

   stage('Publish Results') {
      junit '**/target/surefire-reports/TEST-*.xml'
   }
}