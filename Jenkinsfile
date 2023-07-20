pipeline {
    agent {
        docker {
            image 'eddevopsd2/maven-java-npm-docker:mvn3.8.5-jdk17-node16.19.0'
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
        stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv('sonar'){
                    sh 'mvn sonar:sonar -Dsonar.projectName=Qlack-Java -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_KEY_QLACKJAVA}'
                }
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
                        "project": "8cc5ec7d-5c51-455e-a374-7f52da56f0b4",
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
    post {
        changed {
            emailext subject: '$DEFAULT_SUBJECT',
                body: '$DEFAULT_CONTENT',
                to: 'qlack@eurodyn.com'
            script {
                if (currentBuild.result == 'SUCCESS') {
                    rocketSend avatar: "http://d2-np.eurodyn.com/jenkins/jenkins.png", channel: 'qlack', message: ":white_check_mark: | ${BUILD_URL} \n\nBuild succeeded. *${env.BRANCH_NAME}* \nChangelog: ${getChangeString(10)}", rawMessage: true
                }
                if (currentBuild.result == 'FAILURE') {
                   rocketSend avatar: "http://d2-np.eurodyn.com/jenkins/jenkins.png", channel: 'qlack', message: ":x: | ${BUILD_URL} \n\nBuild failed. *${env.BRANCH_NAME}* \nChangelog: ${getChangeString(10)}", rawMessage: true
                }
            }
        }
    }
}

@NonCPS
def getChangeString(maxMessages) {
    MAX_MSG_LEN = 100
    def changeString = ""

    def changeLogSets = currentBuild.changeSets

    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length && i + j < maxMessages; j++) {
            def entry = entries[j]
            truncated_msg = entry.msg.take(MAX_MSG_LEN)
            changeString += "*${truncated_msg}* _by author ${entry.author}_\n"
        }
    }

    if (!changeString) {
        changeString = " There have not been any changes since the last build"
    }

    return changeString
}