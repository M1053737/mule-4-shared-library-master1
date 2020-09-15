import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call(String environment) {
	pipelinePlaceholders = PipelinePlaceholders.getInstance()
	authorizationToken = pipelinePlaceholders.getAuthorizationToken()

	// TARGETS ACHTERHALEN: 	https://anypoint.mulesoft.com/amc/target-registry/api/v1/organizations/c41cae46-dbd6-46b6-8ae6-38db9442638a/providers/MC/targets/7ce55360-5574-4882-9eea-ba6000633357

	// = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = 

	response = httpRequest ( 
			httpMode: "GET",
			url: "${Constants.ANYPOINT_URL}/hybrid/api/v2/organizations/${pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()].id}/environments/${pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()]["${environment}"].id}/deployments",
			customHeaders: [[name: "Authorization", value: authorizationToken]]
	)

	// - RR for Runtime Manager
	// - MC for Mule Cloud
	// - PCF for Private Cloud Foundry

	responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
	currentDeploymentDetails = responseMap.items.find { it.name == pipelinePlaceholders.getMvnArtifactId().toLowerCase() && it.target.provider == "MC" }
 
	if(currentDeploymentDetails && currentDeploymentDetails.size() > 0) {
        payload = """
            {
                \"application\": {
                    \"ref\": {
                        \"groupId\": \"${pipelinePlaceholders.getMvnGroupId()}\",
                        \"artifactId\": \"${pipelinePlaceholders.getMvnArtifactId()}\",
                        \"version\": \"${pipelinePlaceholders.getMvnArtifactVersion().minus("-SNAPSHOT")}\",
                        \"packaging\": \"jar\"
                    }
                }
            }
        """
        response = httpRequest ( 
            httpMode: "PATCH",
            url: "${Constants.ANYPOINT_URL}/hybrid/api/v2/organizations/${pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()].id}/environments/${pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()]["${environment}"].id}/deployments/${currentDeploymentDetails.id}",
            requestBody: payload,
            customHeaders: [[name: "Authorization", value: authorizationToken], [name: "Content-Type", value: "application/json"]]
    	)
    	responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    }
    else {
        payload = """
            {
                \"name\": \"${pipelinePlaceholders.getMvnArtifactId()}\",
                \"labels\": [
                    \"beta\"
                ],
                \"target\": {
                    \"provider\": \"MC\",
                    \"targetId\": \"7ce55360-5574-4882-9eea-ba6000633357\",
                    \"deploymentSettings\": {
                        \"resources\": {
                            \"cpu\": {
                                \"reserved\": \"500m\",
                                \"limit\": \"500m\"
                            },
                            \"memory\": {
                                \"reserved\": \"700Mi\",
                                \"limit\": \"700Mi\"
                            }
                        },
                        \"clustered\": false,
                        \"http\": {
                            \"inbound\": {
                                \"publicUrl\": \"mule-rtf-controller.mschnkvld.com/${pipelinePlaceholders.getMvnArtifactId()}\"
                            }
                        },
                        \"runtimeVersion\": \"4.2.1:v1.2.9\",
                        \"lastMileSecurity\": false,
                        \"updateStrategy\": \"rolling\"
                    },
                    \"replicas\": 1
                },
                \"application\": {
                    \"ref\": {
                        \"groupId\": \"${pipelinePlaceholders.getMvnGroupId()}\",
                        \"artifactId\": \"${pipelinePlaceholders.getMvnArtifactId()}\",
                        \"version\": \"${pipelinePlaceholders.getMvnArtifactVersion().minus("-SNAPSHOT")}\",
                        \"packaging\": \"jar\"
                    },
                    \"assets\": [],
                    \"desiredState\": \"STARTED\",
                    \"configuration\": {
                        \"mule.agent.application.properties.service\": {
                            \"applicationName\": \"${pipelinePlaceholders.getMvnArtifactId()}\",
                            \"properties\": {
                                \"env\": \"dev\",
                                \"apiAutoDiscoveryId\": \"${pipelinePlaceholders.getApiAutoDiscoveryId()}\",
                                \"anypoint.platform.client_id\": \"d4f29d51e34d4297b0888d29df5ea240\",
                                \"anypoint.platform.client_secret\": \"F1FfDc8ca8C0474A84cA360519AD4A88\"
                            }
                        }
                    }
                }
            }
        """
    	response = httpRequest ( 
    			httpMode: "POST",
    			url: "${Constants.ANYPOINT_URL}/hybrid/api/v2/organizations/${pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()].id}/environments/${pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()]["${environment}"].id}/deployments",
    			requestBody: payload,
    			customHeaders: [[name: "Authorization", value: authorizationToken], [name: "Content-Type", value: "application/json"]]
    	)
    	responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    }
}