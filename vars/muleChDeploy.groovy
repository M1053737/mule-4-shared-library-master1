import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call(String jarFileName, String environment) {
  pipelinePlaceholders = PipelinePlaceholders.getInstance()
  authorizationToken = pipelinePlaceholders.getAuthorizationToken()
  
  def artifactId = pipelinePlaceholders.getMvnArtifactId()
  def organizationId = pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()].id
  def environmentId = pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()]["${environment}"].id

  empo = """
  {
    "domain": "${Constants.FQN_PREFIX.replace(".", "-")}-${artifactId}",
    "muleVersion": {
      "version": "4.2.0"
    },
    "region": "us-east-1",
    "monitoringEnabled": true,
    "monitoringAutoRestart": true,
    "workers": {
      "amount": 1,
      "type": {
        "name": "Medium",
        "weight": 1,
        "cpu": "1 vCore",
        "memory": "1.5 GB memory"
      }
    },
    "loggingNgEnabled": true,
    "persistentQueues": true,
    "objectStoreV1": true,
    "properties": { 
        "env": "${Constants.PROPERTY_FILE_NAMES[environment]}",
        "apiAutoDiscoveryId": "${pipelinePlaceholders.getApiAutoDiscoveryId()}" 
    }
  }
  """

  echo empo

//  def response = httpRequest ( 
//      httpMode: "POST",
//      requestBody: payload,
//      url: "${Constants.ANYPOINT_URL}/cloudhub/api/v2/applications",
//      customHeaders: [[name: "Authorization", value: authorizationToken], [name: "x-anypnt-org-id", value: organizationId], [name: "x-anypnt-env-id", value: environmentId]]
//  )
//  def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
// 
//  currentDeploymentDetails = responseMap.data.find { it.name == artifactId.toLowerCase() }
// 
//  if(currentDeploymentDetails && currentDeploymentDetails.size() > 0) {
//    id = Integer.toString(currentDeploymentDetails.id)
//    sh "curl -X PATCH ${Constants.ANYPOINT_URL}/hybrid/api/v1/applications/${id} -H 'Authorization: ${authorizationToken}' -H 'content-type: multipart/form-data' -H 'x-anypnt-org-id: ${organizationId}' -H 'x-anypnt-env-id: ${environmentId}' -F 'file=@${jarFileName}' -F 'artifactName=${artifactId.toLowerCase()}' -F 'targetId=${targetId}'"
//  }
//  else {
    sh "curl -X POST ${Constants.ANYPOINT_URL}/cloudhub/api/v2/applications -H 'Authorization: ${authorizationToken}' -H 'content-type: multipart/form-data' -H 'x-anypnt-org-id: ${organizationId}' -H 'x-anypnt-env-id: ${environmentId}' -F 'file=@${jarFileName}' -F 'autoStart=true'  -F 'appInfoJson=${empo}'"
//  }
}