pipeline {
    agent {
        kubernetes {
            yaml '''
              apiVersion: v1
              kind: Pod
              metadata:
                name: qlack-java
                namespace: jenkins
              spec:
                affinity:
                        podAntiAffinity:
                          preferredDuringSchedulingIgnoredDuringExecution:
                          - weight: 50
                            podAffinityTerm:
                              labelSelector:
                                matchExpressions:
                                - key: jenkins/jenkins-jenkins-agent
                                  operator: In
                                  values:
                                  - "true"
                              topologyKey: kubernetes.io/hostname
                securityContext:
                    runAsUser: 0
                    runAsGroup: 0
                containers:
                - name: qlack-java-builder
                  image: eddevopsd2/maven-java-npm-docker:mvn3.8.5-jdk17-npm8.5.0-docker
                  volumeMounts:
                  - name: maven
                    mountPath: /root/.m2/
                    subPath: qlack-java
                  tty: true
                  securityContext:
                    privileged: true
                    runAsUser: 0
                    fsGroup: 0
                imagePullSecrets:
                - name: regcred
                volumes:
                - name: maven
                  persistentVolumeClaim:
                    claimName: maven-nfs-pvc
            '''
            workspaceVolume persistentVolumeClaimWorkspaceVolume(claimName: 'workspace-nfs-pvc', readOnly: false)
        }
    }
    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    stages {
        stage('Build') {
            steps {
                container (name: 'qlack-java-builder'){
                    sh 'mvn clean install -Pfull-build'
                }
            }
        }                      
        stage('Sonar Analysis') {
            steps {
                container (name: 'qlack-java-builder'){
                    withSonarQubeEnv('sonar'){
                        sh 'mvn sonar:sonar -Dsonar.projectName=Qlack-Java -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.token=${SONAR_GLOBAL_KEY} -Dsonar.working.directory="/tmp"'
                    }
                }
            }
        }
        stage('Produce bom.xml'){
            steps{
                container (name: 'qlack-java-builder'){
                    sh 'mvn org.cyclonedx:cyclonedx-maven-plugin:makeAggregateBom'
                }
            }
        }
        stage('Dependency-Track Analysis'){
            steps{
                container (name: 'qlack-java-builder'){
                    sh '''
                        echo '{"project": "80c8d4aa-11fc-4841-af7e-bdd7b24f2d27", "bom": "'"$(cat target/bom.xml | base64 -w 0)"'"}' > payload.json
                    '''

                    sh '''
                        curl -X "PUT" ${DEPENDENCY_TRACK_URL} -H 'Content-Type: application/json' -H 'X-API-Key: '${DEPENDENCY_TRACK_API_KEY} -d @payload.json
                    '''
                }
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
