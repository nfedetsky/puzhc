def appVersion = 'UNKNOWN'
pipeline {
    agent { label 'master' }
    environment {
        ANSIBLE_HOST_KEY_CHECKING = false
    }
    stages {
        stage('Build') {
            steps {
                script {
                    echo 'Pulling... ' + env.BRANCH_NAME
                    def targetVersion = getDevVersion()
                    appVersion = getAppVersion()
                    sh "./gradlew clean build"
                }
            }
        }
        stage ('Starting deployment to host') {
             steps {
                   build job: "suvv-${params.ENV_PREFIX}-deployment",
                   parameters: [
                        string(name: 'APP_NAME', value: 'csrv'),
                        string(name: 'ARTIFACT_VERSION', value: appVersion),
                        string(name: 'JAR_PATH', value: "${env.WORKSPACE}/build/libs/csrv-${appVersion}.jar"),
                        ], wait: false
             }
        }
    }
    post {
        success {
            print "Successful"
        }
        unstable {
            print "Unstable"
        }
        failure {
            print "Failed"
        }
    }
}

def getDevVersion() {
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    def versionNumber;
    if (gitCommit == null) {
        versionNumber = 'UNKNOWN';
    } else {
        versionNumber = gitCommit.take(8);
    }
    print 'build versions...'
    print versionNumber
    return versionNumber
}

def getAppVersion() {
    def version = '0.0.1-SNAPSHOT'
    print 'app version... '
    version = sh (
        script: "./gradlew properties -q | grep \"version:\" | awk '{print \$2}'",
        returnStdout: true
    ).trim()
    print version
    return version
}