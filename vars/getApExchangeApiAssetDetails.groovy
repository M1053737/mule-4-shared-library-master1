import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Constants

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    authorizationToken = pipelinePlaceholders.getAuthorizationToken()
    organizationId = pipelinePlaceholders.getOrganizationId()
    apiAssetId = pipelinePlaceholders.getApiAssetId()

    def response = httpRequest ( 
        httpMode: "GET" 
        , url: "${Constants.ANYPOINT_URL}/exchange/api/v1/assets/${organizationId}/${apiAssetId}"  // IMPROVEMENT https://anypoint.mulesoft.com/exchange/api/v1/assets/64fe4806-309d-4611-90dd-9e6d22542b4d/rekondis/1.0.5
        , customHeaders: [[name: "Authorization", value: authorizationToken]]
    )
    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    return responseMap
}