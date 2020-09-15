import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Constants

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()

    sh "docker push ${Constants.DOCKER_REGISTRY}/${pipelinePlaceholders.getMvnArtifactId()}:${pipelinePlaceholders.getMvnArtifactVersion()}"

}