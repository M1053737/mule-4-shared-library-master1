import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    ORGANIZATION_FORMATTED = pipelinePlaceholders.getOrganizationFormatted()
    def url

    if(pipelinePlaceholders.getApiAssetId()) {      // IMPLIES THIS IS AN API PROJECT 
        API_NAME_FORMATTED = pipelinePlaceholders.getApiAssetIdFormatted()
        url = "https://${Constants.GITLAB_DOMAIN}/api/v4/projects?name=${API_NAME_FORMATTED}&namespace_id=${pipelinePlaceholders.getGitlabGroupNamespaceId()}"
    }
    else {      // IMPLIES THIS IS A DOMAIN PROJECT
        url = "https://${Constants.GITLAB_DOMAIN}/api/v4/projects?name=${ORGANIZATION_FORMATTED}-domain&namespace_id=${pipelinePlaceholders.getGitlabGroupNamespaceId()}"
    }

    echo url

    def response = httpRequest (
            httpMode: "POST",
            url: url,
            customHeaders: [[name: 'Private-Token', value: pipelinePlaceholders.getVaultSecret("gitlab-access-token")]],
            quiet: true
        )

    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    pipelinePlaceholders.setSshUrlToRepo(responseMap.ssh_url_to_repo)
    pipelinePlaceholders.setHttpUrlToRepo(responseMap.http_url_to_repo)
}