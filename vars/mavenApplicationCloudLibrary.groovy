#!usr/bin/groovy
def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
	
	node {
		stage('Checkout'){
			checkout scm
		}
		
		stage('Build'){
			withEnv(["PATH+MAVEN=${tool 'M3'}"]){
				sh 'mvn clean install'
			}
		}
		
		stage('Deploy to Cloud Foundry'){
			sh '''
				password=`cat pw`
				cf login -a "${config.apiEndPoint}" -o "devops-app-test" -s "development" -u "${config.cloudUsername}" -p "$password"
				cf push "${config.cloudApplicationName}" -m 512M -p target/backend-template-0.0.1-SNAPSHOT.jar
				cf start "${config.cloudApplicationName}"
			   '''
		}
		
		stage('Application Status'){
			sh "cf apps"
		}
	}
}
