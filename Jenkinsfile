pipeline {
  agent any

  parameters {
    choice(name: 'BROWSER',  choices: ['chrome', 'firefox', 'edge'], description: 'Target browser')
    choice(name: 'ENV',      choices: ['qa', 'staging', 'prod'],     description: 'Target environment')
    string(name: 'TAGS',     defaultValue: '@smoke and not @skip',   description: 'Cucumber tag expression')
    booleanParam(name: 'HEADLESS', defaultValue: true,               description: 'Headless mode')
    booleanParam(name: 'PARALLEL', defaultValue: false,              description: 'Run in parallel')
  }

  environment {
    SUITE = "${params.PARALLEL ? 'testng-parallel.xml' : 'testng.xml'}"
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -v && java -version'
        sh """
          mvn clean verify \
            -Dbrowser=${params.BROWSER} \
            -Dheadless=${params.HEADLESS} \
            -Denv=${params.ENV} \
            -DsuiteFile=${SUITE} \
            -Dcucumber.filter.tags="${params.TAGS}"
        """
      }
    }
  }

  post {
    always {
      // Archive test artefacts
      archiveArtifacts artifacts: 'target/**/*.json, target/**/*.html, logs/**, test-output/**', allowEmptyArchive: true
      // Allure report
      allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
    }
    failure {
      emailext(
        subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
        body: "Build URL: ${env.BUILD_URL}\nBrowser: ${params.BROWSER}\nEnv: ${params.ENV}",
        to: '${DEFAULT_RECIPIENTS}'
      )
    }
  }
}
