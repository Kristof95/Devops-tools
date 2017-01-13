#!usr/bin/groovy
def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
	
	def apiEndPoint = config.apiEndPoint
	def cloudUsername = config.cloudUsername
	def cloudApplicationName = config.cloudApplicationName
	def targetContainer = config.targetContainer
	def targetContainer2 = config.targetContainer2

	node {
		stage('Checkout'){
			checkout scm
		}
		
		stage('Build'){
//			withEnv(["PATH+MAVEN=${tool 'M3'}"]){
//				sh 'mvn clean install'
//			}
		}
		
		stage('Deploy to Cloud Foundry'){
//		def password = readFile("pw")
//			sh """
//				cf login -a "$apiEndPoint" -o "devops-app-test" -s "development" -u "$cloudUsername" -p "$password"
//				cf delete "$cloudApplicationName" -f
//				cf push "$cloudApplicationName" -m 256M -p target/backend-template-0.0.1-SNAPSHOT.jar
//				cf start "$cloudApplicationName"
//			   """

			if(!targetContainer)
			{
				echo "empty"
			}
			if(!targetContainer2)
			{
				echo "null"
			}
		}
		
		stage('Application Status'){
//			sh "cf apps"
		}
	}
}
