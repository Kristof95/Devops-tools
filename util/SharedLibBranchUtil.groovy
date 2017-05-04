class SharedLibBranchUtility{

    static void addSharedLibraryBranch(String branch) {
        def issueNumber = branch.split("/")[1]
        def sharedLibBranch = """@Library("pipeline-shared-lib@$branch")"""
        def jenkinsFile = readFile "Jenkinsfile"
        def newContent = jenkinsFile.replace(jenkinsFile, "$sharedLibBranch\n$jenkinsFile")
        def credential = readFile 'cred'
        def pw = credential.split("/")[1]
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
}