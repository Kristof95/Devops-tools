#!usr/bin/groovy
def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
	
	def apiEndPoint = config.apiEndPoint
	def cloudUsername = config.cloudUsername
	def cloudApplicationName = config.cloudApplicationName
	
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
			echo "$apiEndPoint"
			echo "$cloudUsername"
			echo "$cloudApplicationName"
			sh '''
				password=`cat pw`
				cf login -a "$apiEndPoint" -o "devops-app-test -s "development" -u "$cloudUsername" -p "$password"
				cf push "$cloudApplicationName" -m 512M -p target/backend-template-0.0.1-SNAPSHOT.jar
				cf start "$cloudApplicationName"
			   '''
			
		}
		
		stage('Application Status'){
			sh "cf apps"
		}
	}
}
