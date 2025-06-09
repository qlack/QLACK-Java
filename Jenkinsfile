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
                    fsGroup: 0
                priorityClassName: "jenkins-low-priority"
                containers:
                - name: qlack-java-builder
                  image: eddevopsd2/maven-java-npm-docker:mvn3.9.6-jdk21-node18-docker-npm8.0.0
                  volumeMounts:
                  - name: maven
                    mountPath: /root/.m2/
                    subPath: qlack-java
                  tty: true
                  securityContext:
                    privileged: true
                    runAsUser: 0
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
                    emailext subject: '$DEFAULT_SUBJECT',
                        body: '$DEFAULT_CONTENT',
                        to: 'dd74bf6f.ed.eurodyn.com@emea.teams.ms'
                }
                if (currentBuild.result == 'FAILURE') {
                    emailext subject: '$DEFAULT_SUBJECT',
                        body: '$DEFAULT_CONTENT',
                        to: 'dd74bf6f.ed.eurodyn.com@emea.teams.ms'
                }
            }
        }
    }
}
