import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Constants

def call(String jarFileName) {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    
    sh "mkdir apps"
    sh "mv ${jarFileName} apps/"

    sh "docker build . -t ${Constants.DOCKER_REGISTRY}/${pipelinePlaceholders.getMvnArtifactId()}:${pipelinePlaceholders.getMvnArtifactVersion()}"
    
}