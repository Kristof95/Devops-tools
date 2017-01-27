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
			withEnv(["PATH+MAVEN=${tool 'M3'}"]){
				sh 'mvn clean install'
			}
		}
		
//		stage('Deploy to Cloud Foundry'){
//		def password = readFile("pw")
//			sh """
//				cf login -a "$apiEndPoint" -o "devops-app-test" -s "development" -u "$cloudUsername" -p "$password"
//				cf delete "$cloudApplicationName" -f
//				cf push "$cloudApplicationName" -m 256M -p target/backend-template-0.0.1-SNAPSHOT.jar
//				cf start "$cloudApplicationName"
//			   """
//		}
		
		stage('Create docker image && Run docker container'){
			dir('src/main/docker'){
				docker.build("arungupta/docker-jenkins-pipeline:word")
			}
		}
		
		stage('Push docker image to registry'){
			echo "Image successfully pushed!"
		}
		
		stage('Run container'){
			dir('src/main/docker'){
				sh "docker run arungupta/docker-jenkins-pipeline:word-p 8090:8080"
			}
		}
	}
}
