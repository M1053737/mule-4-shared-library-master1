import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    ORGANIZATION_FORMATTED = pipelinePlaceholders.getOrganizationFormatted()
    def payload

    if(pipelinePlaceholders.getApiAssetId()) { // IMPLIES THIS IS AN API PROJECT
        API_NAME_FORMATTED = pipelinePlaceholders.getApiAssetIdFormatted()
        payload = "{ \"name\": \"${ORGANIZATION_FORMATTED}-${API_NAME_FORMATTED}\", \"description\": \"${ORGANIZATION_FORMATTED}-${API_NAME_FORMATTED} repository created by Jenkins\", \"private\": true }"  
    }
    else { // IMPLIES THIS IS A DOMAIN PROJECT
        payload = "{ \"name\": \"${ORGANIZATION_FORMATTED}-domain\", \"description\": \"${ORGANIZATION_FORMATTED}-domain repository created by Jenkins\", \"private\": true }"
    }
    
    def response = httpRequest (
        httpMode: "POST"
        , url: "https://${Constants.GOGS_DOMAIN}/api/v1/org/${Constants.GOGS_ORGANIZATION}/repos"
        , requestBody: payload
        , authentication: Constants.GOGS_CREDENTIALS_ID
        , customHeaders: [[name: 'Content-Type', value: 'application/json']]
    )
    
    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    pipelinePlaceholders.setScmRepoFullName(responseMap.full_name)
}