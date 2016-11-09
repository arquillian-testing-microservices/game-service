node {

    stage('Preparation') { // for display purposes
       // Get some code from repository
       checkout scm
    }
    stage('Build') {
        sh "./gradlew clean compileTestJava"
    }

    stage('Tests') {
        echo 'Running Unit and Component Tests'
    }

    stage('Assemble Application') {
        withEnv(["SOURCE_BUILD_NUMBER=${env.BUILD_NUMBER}"]) {
            sh "./gradlew assemble"
        }
    }

    stage('Consumer Contract Tests') {
        def props = readProperties file: 'build/build-info.properties'
        withEnv(['publishcontracts=true', "artifactversion=${props['version']}"]) {
            sh "./gradlew test"
        }
    }

    stage('Publish Contract Results') {
        junit '**/build/test-results/TEST-*.xml'
    }

    def headResult, productionResult
    stage('Provider Contract Tests') {

        parallel (
            headRun : { headResult = build job: 'age-checker', parameters: [string(name: 'agecheckerurl', value: '')], propagate: false },
            productionRun : { productionResult = build job: 'age-checker', parameters: [string(name: 'agecheckerurl', value: 'http://192.168.99.100:8090')], propagate: false}
        )
    }

    if (productionResult.result != 'SUCCESS') {
        currentBuild.result = 'FAILURE'
    } else {
        def lineSeparator = System.lineSeparator()
        def message = "Do you want to Deploy Game Service To Production? ${lineSeparator} Contract Tests against Production \u2705 ${lineSeparator} Contract Tests against HEAD "
        def icon = headResult.result != 'SUCCESS' ? "\u274C" : "\u2705"
        message += icon
        stage('Deploy To Production?') {
            input "${message}"
            echo "deployed to Production"
        }
    }

}