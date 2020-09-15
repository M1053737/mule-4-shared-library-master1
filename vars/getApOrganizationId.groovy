import groovy.json.JsonSlurper
import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    authorizationToken = pipelinePlaceholders.getAuthorizationToken()
    def response = httpRequest ( 
        httpMode: "GET" 
        , url: "${Constants.ANYPOINT_URL}/accounts/api/profile" 
        , customHeaders: [[name: "Authorization", value: authorizationToken]]
    )
    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    organizationDetails = responseMap.memberOfOrganizations.find { it.name == pipelinePlaceholders.getOrganization() }
    return organizationDetails.id
}