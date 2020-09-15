import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Constants

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    requestFilter = java.net.URLEncoder.encode("{\"name\":{\"${pipelinePlaceholders.getMvnArtifactId()}\":true}}", "UTF-8")
    def response = httpRequest ( 
        httpMode: "GET" 
        , url: "http://${Constants.DOCKER_SWARM_MANAGER}:2375/services?filters=${requestFilter}"
    )
    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    
    if(responseMap.size() == 0) {
        sh "export DOCKER_HOST=tcp://${Constants.DOCKER_SWARM_MANAGER}:2375 && docker service create --replicas 2 --name ${pipelinePlaceholders.getMvnArtifactId()} -p 8080 --with-registry-auth ${Constants.DOCKER_REGISTRY}/${pipelinePlaceholders.getMvnArtifactId()}:${pipelinePlaceholders.getMvnArtifactVersion()}"
    }
    else {
        sh "export DOCKER_HOST=tcp://${Constants.DOCKER_SWARM_MANAGER}:2375 && docker service update --with-registry-auth --image ${Constants.DOCKER_REGISTRY}/${pipelinePlaceholders.getMvnArtifactId()}:${pipelinePlaceholders.getMvnArtifactVersion()} ${pipelinePlaceholders.getMvnArtifactId()}"
    }
}
