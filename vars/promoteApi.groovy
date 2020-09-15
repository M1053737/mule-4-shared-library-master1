import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call() {
  pipelinePlaceholders = PipelinePlaceholders.getInstance()
  authorizationToken = pipelinePlaceholders.getAuthorizationToken()
  

pipelinePlaceholders.getVaultSecret('jenkins-pipeline-client-id')
pipelinePlaceholders.getVaultSecret('jenkins-pipeline-client-secret')


  def artifactId = pipelinePlaceholders.getArtifactId()
  def organizationId = pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()].id
  def environmentId = pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()]["${environment}"].id
  def targetId = Integer.toString(pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()]["${environment}"]["${target}"].id)
  def targetType = pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()]["${environment}"]["${target}"].type
]
}