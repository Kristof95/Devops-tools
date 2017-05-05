
def call(body) {
// evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    node {

        stage('Hello') {
            echo "Hy from $env.BRANCH_NAME branch"
        }

        stage('Set shared library branch') {
            def branch = "$env.BRANCH_NAME"
            if (branch ==~ /^(feature).+/) {
                def issueNumber = branch.split("/")[1]
                def sharedLibBranch = """@Library("pipeline-shared-lib@$branch") _"""
                sh "touch Jenkinsfile.txt"
                def jenkinsFile = readFile "Jenkinsfile.txt"
                def newContent = jenkinsFile.replace(jenkinsFile, "$sharedLibBranch\n$jenkinsFile")
                def credential = readFile '/var/jenkins_home/workspace/cred'
                def pw = credential.split(":")[1]
                echo "Content: $newContent"
                writeFile file: "Jenkinsfile", text: "$newContent"
                sh """
                    git config remote.origin.url https://Kristof95:$pw@github.com/Kristof95/devops_test_repo.git
                    git checkout "$branch"
                    git add .
                    git commit -m "$issueNumber set shared library branch in Jenkinsfile"
                    git push
                """
            }
            echo "Skipping shared library branch settings!"
        }
    }
}
