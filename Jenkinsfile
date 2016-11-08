node {

    stage('Preparation') { // for display purposes
       // Get some code from repository
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

    stage('Provider Contract Tests') {
        def headResult, productionResult
        parallel (
            headRun : { headResult = build job: 'age-checker', parameters: [string(name: 'agecheckerurl', value: '')], propagate: false },
            productionRun : { productionResult = build job: 'age-checker', parameters: [string(name: 'agecheckerurl', value: 'http://192.168.99.100:8090')], propagate: false}
        )

        echo headResult
        echo productionResult
    }

}