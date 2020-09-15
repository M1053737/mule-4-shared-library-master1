import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Constants

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    authorizationToken = pipelinePlaceholders.getAuthorizationToken()
    
    def deploymentTargets = [:]

    pipelinePlaceholders.getEnvironments().each {

        def deploymentTargetsForEnvironment = [:]

        def serversResponse = httpRequest ( 
            httpMode: "GET" 
            , url: "${Constants.ANYPOINT_URL}/hybrid/api/v1/servers" 
            , customHeaders: [[name: "Authorization", value: authorizationToken], [name: "x-anypnt-org-id", value: pipelinePlaceholders.getOrganizationId()], [name: "x-anypnt-env-id", value: it.value]]
        )
        def serversResponseMap = new groovy.json.JsonSlurperClassic().parseText(serversResponse.content)

        serversResponseMap.data.each {
            deploymentTargetsForEnvironment[it.name] = Integer.toString(it.id)
        }

        def serverGroupsResponse = httpRequest ( 
            httpMode: "GET" 
            , url: "${Constants.ANYPOINT_URL}/hybrid/api/v1/serverGroups" 
            , customHeaders: [[name: "Authorization", value: authorizationToken], [name: "x-anypnt-org-id", value: pipelinePlaceholders.getOrganizationId()], [name: "x-anypnt-env-id", value: it.value]]
        )
        def serverGroupsResponseMap = new groovy.json.JsonSlurperClassic().parseText(serverGroupsResponse.content)

        serverGroupsResponseMap.data.each {     
            deploymentTargetsForEnvironment[it.name] = Integer.toString(it.id)
        }

        deploymentTargets[it.key] = deploymentTargetsForEnvironment
    }
    
    return deploymentTargets
}