pipeline {
    agent {
        docker {
            image 'imousmoutis/maven3-jdk8:1.0.1'
            args '-v /root/.m2/Qlack-Java:/root/.m2'
        }
    }
    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install -Pfull-build'
            }
        }
        stage('Dependencies Check') {
            steps {
                sh 'mvn org.owasp:dependency-check-maven:aggregate'
            }
        }
        stage('Sonar Analysis') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.projectName=Qlack-Java -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_KEY_QLACKJAVA}'
            }
        }
        stage('Produce bom.xml'){
            steps{
                sh 'mvn org.cyclonedx:cyclonedx-maven-plugin:makeAggregateBom'
            }
        }
        stage('Dependency-Track Analysis'){
            steps{
                sh '''
                    cat > payload.json <<__HERE__
                    {
                        "project": "19fa197a-eccc-4d1a-9811-93afda4679ee",
                        "bom": "$(cat target/bom.xml |base64 -w 0 -)"
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
