#!usr/bin/groovy
def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
	
	def apiEndPoint = config.apiEndPoint
	def cloudUsername = config.cloudUsername
	def cloudApplicationName = config.cloudApplicationName
	def targetContainers = config.targetContainsers

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
		for(entry in targetContainers){
				def host = entry["host"]
				def port = entry["port"]
				def serviceName = entry["serviceName"]
				println ${host}+":"+${port}+"\t"+${serviceName}
			}
//		def password = readFile("pw")
//			sh """
//				cf login -a "$apiEndPoint" -o "devops-app-test" -s "development" -u "$cloudUsername" -p "$password"
//				cf delete "$cloudApplicationName" -f
//				cf push "$cloudApplicationName" -m 256M -p target/backend-template-0.0.1-SNAPSHOT.jar
//				cf start "$cloudApplicationName"
//			   """
		}
		
		stage('Application Status'){
//			sh "cf apps"
		}
	}
}
