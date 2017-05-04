import util.SharedLibBranchUtility

def call(body) {
// evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    node {

        stage('Hello') {
            echo "Hy from master branch"
        }

        stage('Set shared library branch') {
            def branch = "$env.BRANCH_NAME"
            if (branch ==~ /^(feature).+/) {
                SharedLibBranchUtility.addSharedLibraryBranch(branch)
            }
            echo "Skipping shared library branch settings!"
        }
    }
}