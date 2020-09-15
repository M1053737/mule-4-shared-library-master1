import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call(String jarFileName, String environment, String target) {
  pipelinePlaceholders = PipelinePlaceholders.getInstance()
  authorizationToken = pipelinePlaceholders.getAuthorizationToken()
  
  def artifactId = pipelinePlaceholders.getArtifactId()
  def organizationId = pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()].id
  def environmentId = pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()]["${environment}"].id
  def targetId = Integer.toString(pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()]["${environment}"]["${target}"].id)
  def targetType = pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()]["${environment}"]["${target}"].type

  def response = httpRequest ( 
      httpMode: "GET",
      url: "${Constants.ANYPOINT_URL}/hybrid/api/v1/applications?targetId=${targetId}",
      customHeaders: [[name: "Authorization", value: authorizationToken], [name: "x-anypnt-org-id", value: organizationId], [name: "x-anypnt-env-id", value: environmentId]]
  )
  def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
 
  currentDeploymentDetails = responseMap.data.find { it.name == artifactId.toLowerCase() }
 
  if(currentDeploymentDetails && currentDeploymentDetails.size() > 0) {
    id = Integer.toString(currentDeploymentDetails.id)
    sh "curl -X PATCH ${Constants.ANYPOINT_URL}/hybrid/api/v1/applications/${id} -H 'Authorization: ${authorizationToken}' -H 'content-type: multipart/form-data' -H 'x-anypnt-org-id: ${organizationId}' -H 'x-anypnt-env-id: ${environmentId}' -F 'file=@${jarFileName}' -F 'artifactName=${artifactId.toLowerCase()}' -F 'targetId=${targetId}'"
  }
  else {
    sh "curl -X POST ${Constants.ANYPOINT_URL}/hybrid/api/v1/applications -H 'Authorization: ${authorizationToken}' -H 'content-type: multipart/form-data' -H 'x-anypnt-org-id: ${organizationId}' -H 'x-anypnt-env-id: ${environmentId}' -F 'file=@${jarFileName}' -F 'artifactName=${artifactId.toLowerCase()}' -F 'targetId=${targetId}'"
  }
}