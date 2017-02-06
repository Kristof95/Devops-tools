def call(body) {
// evaluate the body block, and collect configuration into the object
def config = [:]
body.resolveStrategy = Closure.DELEGATE_FIRST
body.delegate = config
body()
	node{

		stage('Hello'){
			echo "master"
		}
		
		stage('HEAD Rev'){
			sh 'git rev-parse HEAD > commit'
			def commit = readFile('commit').trim()
			println commit
		}
	}
}
