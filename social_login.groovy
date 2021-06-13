#!/groovy
pipeline {
  agent any
  parameters {
    gitParameter(branch: '', branchFilter: '.*', defaultValue: 'master',
      description: 'enter the branch name',
      name: 'branch', quickFilterEnabled: false,
      selectedValue: 'NONE', sortMode: 'NONE',
      tagFilter: '*', type: 'PT_BRANCH')

    string(name: 'PERSON', defaultValue: 'Mr Manikandan', description: 'Running from my local branch')

    choice(name: 'CHOICE', choices: ['DEV', 'QA', 'UAT'], description: 'Pick QA')

    booleanParam(name: 'BUILD', defaultValue: false, description: '')
  }
  tools {
    jdk 'java_home'
    maven 'maven_home'
  }
  stages {
    stage('checkout') {
      steps {
        echo "checkout"
        checkout([$class: 'GitSCM',
          branches: [
            [name: "${params.branch}"]
          ],
          extensions: [],
          userRemoteConfigs: [
            [url: 'https://github.com/manikandanchellam/webapplication.git']
          ]
        ])
      }
    }
    stage('Unit test') {
      steps {
        echo "Unit test skipped"
        echo "HELLO ${params.PERSON}"
      }
    }
    stage('Code quality') {
      steps {
        echo "Code quality skipped"
        echo "Choice: ${params.CHOICE}"
      }
    }
    stage('Publish reports') {
      steps {
        echo "Publish reports skipped"
      }
    }
    stage('Build') {
      when{
        expression {return params.BUILD}
      }
      steps {
        echo "Build  ${params.BUILD}"
        sh 'mvn clean install'
      }
    }
    stage('publish to Artifcate') {
      steps {
        echo "publishing"
      }
    }
    stage('build docker') {
      steps {
        echo "create doceker image"
      }
    }
      stage('deploy') 
        {
        when {
         expression { params.BUILD == true }
                }
         steps {
                 sh 'cp target/java-tomcat-maven-example.war  /var/lib/tomcat/webapps/'
                }
        }
    }
}
