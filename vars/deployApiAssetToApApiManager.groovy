import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Constants

def call(exchangeAssetId, exchangeAssetVersion, exchangeAssetGroupId, targetEnvironment) {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    authorizationToken = pipelinePlaceholders.getAuthorizationToken()
    organizationId = pipelinePlaceholders.getOrganizationId()
    
    def payload = """
        {
            \"endpoint\": {
                \"type\":\"rest-api\",
                \"uri\":null,
                \"proxyUri\":null,
                \"isCloudHub\":null,
                \"referencesUserDomain\":null,
                \"responseTimeout\":null,
                \"muleVersion4OrAbove\":null
            },
            \"instanceLabel\":null, 
            \"spec\": {
                \"assetId\":\"$exchangeAssetId\",
                \"version\":\"$exchangeAssetVersion\",
                \"groupId\":\"$exchangeAssetGroupId\"
            }
        }
    """ // instanceLabel possible improvement!
    
    def response = httpRequest ( 
        httpMode: "POST"
        , url: "${Constants.ANYPOINT_URL}/apimanager/api/v1/organizations/$organizationId/environments/${pipelinePlaceholders.getEnvironmentId(targetEnvironment)}/apis"
        , requestBody: payload
        , customHeaders: [[name: 'Content-Type', value: 'application/json'], [name: "Authorization", value: authorizationToken]]
    )

    echo response.content

    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)

    autoDiscoveryApiName = "groupId:${responseMap.groupId}:assetId:${responseMap.assetId}"
    autoDiscoveryApiVersion = responseMap.autodiscoveryInstanceName
    
    pipelinePlaceholders.setAutoDiscoveryApiName(targetEnvironment, autoDiscoveryApiName)
    pipelinePlaceholders.setAutoDiscoveryApiVersion(targetEnvironment, autoDiscoveryApiVersion)
}