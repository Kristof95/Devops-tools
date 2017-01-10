
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
			echo $apiEndPoint
			echo $cloudUsername
			echo $cloudApplicationName
			
		}
		
		stage('Application Status'){
			sh "cf apps"
		}
	}
}
