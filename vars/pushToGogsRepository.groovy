import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def fullRepoUrlWithCredentials
    
def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    ORGANIZATION_FORMATTED = pipelinePlaceholders.getOrganizationFormatted()

    if(pipelinePlaceholders.getApiAssetId()) { // IMPLIES THIS IS AN API PROJECT
        API_NAME_FORMATTED = pipelinePlaceholders.getApiAssetIdFormatted()
        REPO_NAME = "${API_NAME_FORMATTED}"  
    }
    else { // IMPLIES THIS IS A DOMAIN PROJECT
        REPO_NAME = "${ORGANIZATION_FORMATTED}-domain"
    }

    withCredentials([usernameColonPassword(credentialsId: Constants.GOGS_CREDENTIALS_ID, variable: 'scm_credentials')]) {
        fullRepoUrlWithCredentials = "https://" + scm_credentials + "@" + Constants.GOGS_DOMAIN + "/" + pipelinePlaceholders.getScmRepoFullName()
    }

    sh "git init"
    sh "git remote add origin ${fullRepoUrlWithCredentials}"

    push("Initial commit", "master")
    
}

def push(commitMessage, branch) {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    ORGANIZATION_FORMATTED = pipelinePlaceholders.getOrganizationFormatted()

    if(pipelinePlaceholders.getApiAssetId()) { // IMPLIES THIS IS AN API PROJECT
        API_NAME_FORMATTED = pipelinePlaceholders.getApiAssetIdFormatted()
        REPO_NAME = "${API_NAME_FORMATTED}"  
    }
    else { // IMPLIES THIS IS A DOMAIN PROJECT
        REPO_NAME = "${ORGANIZATION_FORMATTED}-domain"
    }

    // THIS RE-SETTING OF THE URL NEEDS IMPROVEMENT
    
    withCredentials([usernameColonPassword(credentialsId: Constants.GOGS_CREDENTIALS_ID, variable: 'scm_credentials')]) {
        fullRepoUrlWithCredentials = "https://" + scm_credentials + "@" + Constants.GOGS_DOMAIN + "/" + pipelinePlaceholders.getScmRepoFullName()
    }

    sh "git remote set-url origin ${fullRepoUrlWithCredentials}"
    sh "git add ."
    sh "git config user.name 'jenkins'" // SHOULD NOT BE HARDCODED
    sh "git config user.email 'maschenkeveld+jenkins@gmail.com'" // SHOULD NOT BE HARDCODED
    sh "git commit -m '${commitMessage}'"
    sh "git push -u origin ${branch.toLowerCase()}"
}