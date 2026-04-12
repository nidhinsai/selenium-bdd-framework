pipeline {
  agent any

  parameters {
    choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser')
    string(name: 'TAGS', defaultValue: '@smoke or not @skip', description: 'Cucumber tags')
    booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Headless mode')
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -v'
        sh "mvn clean test -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS} -Dcucumber.filter.tags=\"${params.TAGS}\" -DsuiteFile=testng-parallel.xml"
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'target/**/*.json,target/**/*.html', allowEmptyArchive: true
    }
  }
}