import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Constants

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    authorizationToken = pipelinePlaceholders.getAuthorizationToken()
    organizationId = pipelinePlaceholders.getOrganizationId()
    
    def response = httpRequest ( 
        httpMode: "GET" 
        , url: "${Constants.ANYPOINT_URL}/apiplatform/repository/v2/organizations/${organizationId}/environments" 
        , customHeaders: [[name: "Authorization", value: authorizationToken]]
    )
    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    def environments = [:]
    for (env in responseMap) {
        echo env.name
        echo env.id
        environments.put(env.name, env.id)
    }
    return environments   
}