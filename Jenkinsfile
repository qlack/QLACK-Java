pipeline {
    agent {
        docker {
            image 'eddevopsd2/maven-java-npm-docker:mvn3.8.5-jdk8-11-15-17-npm6.14.4-docker'
            args '-v /root/.m2/Qlack-Java:/root/.m2'
        }
    }
    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    stages {
        stage ('Copy directory')
        {
            steps{
                withCredentials([usernamePassword(credentialsId: 'Jenkins Github token',
                usernameVariable: 'Username',
                passwordVariable: 'Password')]){
                    sh 'mkdir jdk8 jdk11 jdk15 jdk17'
                    sh '''
                        git config --global user.email "devops-d2@eurodyn.com"
                        git config --global user.name "$Username"
                        git clone https://$Username:$Password@github.com/qlack/QLACK-Java.git
                    '''
                    sh '''
                        cp -a QLACK-Java/. jdk8/ 
                        cp -a QLACK-Java/. jdk11/ 
                        cp -a QLACK-Java/. jdk15/ 
                        cp -a QLACK-Java/. jdk17/
                    '''    
                }
            }
        }
        stage ('Build')
        {
            parallel {
                stage('Build jdk8') {
                    steps {
                        sh 'update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java'
                        sh 'mvn -f jdk8/pom.xml clean install -Pfull-build'
                    }
                }
                stage('Build jdk11') {
                    steps {
                        sh 'update-alternatives --set java /usr/lib/jvm/java-11-openjdk-amd64/bin/java'
                        sh 'mvn -f jdk11/pom.xml clean install -Pfull-build'
                    }
                }
                stage('Build jdk15') {
                    steps {
                        sh 'update-alternatives --set java /usr/lib/jvm/jdk-15.0.2/bin/java'
                        sh 'mvn -f jdk15/pom.xml clean install -Pfull-build'
                    }
                }
                stage('Build jdk17') {
                    steps {
                        sh 'update-alternatives --set java /usr/lib/jvm/java-17-openjdk-amd64/bin/java'
                        sh 'mvn -f jdk17/pom.xml clean install -Pfull-build'
                    }
                } 
            }
        }
                       
        stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv('sonar'){
                    sh 'mvn -f jdk17/pom.xml sonar:sonar -Dsonar.projectName=Qlack-Java -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_KEY_QLACKJAVA}'
                }
            }
        }
        stage('Produce bom.xml'){
            steps{
                sh 'mvn -f jdk17/pom.xml org.cyclonedx:cyclonedx-maven-plugin:makeAggregateBom'
            }
        }
        stage('Dependency-Track Analysis'){
            steps{
                sh '''
                    cat > payload.json <<__HERE__
                    {
                        "project": "8cc5ec7d-5c51-455e-a374-7f52da56f0b4",
                        "bom": "$(cat jdk17/target/bom.xml |base64 -w 0 -)"
                    }
                    __HERE__
                   '''

                sh '''
                    curl -X "PUT" ${DEPENDENCY_TRACK_URL} -H 'Content-Type: application/json' -H 'X-API-Key: '${DEPENDENCY_TRACK_API_KEY} -d @payload.json
                   '''
            }
        }
    }
    /*post {
        changed {
            emailext subject: '$DEFAULT_SUBJECT',
                body: '$DEFAULT_CONTENT',
                to: 'qlack@eurodyn.com'
        }
    }*/
}
