pipeline {
    agent none
    tools {
        maven "Apache Maven 3.6"
        jdk "OpenJDK 11 Latest"
    }
    stages {
        stage('Build and test') {
            matrix {
                agent {
                    label 'Worker'
                }
                axes {
                    axis {
                        name 'HSEARCH_VERSION_PROFILE'
                        values 'search5', 'search6'
                    }
                }
                stages {
                    stage('Build and test') {
                        steps {
                            checkout scm
                            sh """ \
                                mvn clean install -U -P ${HSEARCH_VERSION_PROFILE}
                            """
                        }
                    }
                }
            }
        }
    }
}