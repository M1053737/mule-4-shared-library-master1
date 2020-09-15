import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Constants

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    
    def response = httpRequest ( 
        httpMode: "GET",
        url: "http://anypoint-structure.de-c1.cloudhub.io/api/structure-direct",
        customHeaders: [[name: "Cache-Control", value: "cache"], [name: "client_id", value: "${pipelinePlaceholders.getVaultSecret("jenkins-pipeline-client-id")}"], [name: "client_secret", value: "${pipelinePlaceholders.getVaultSecret("jenkins-pipeline-client-secret")}"]]
    )

    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)

    return responseMap   
}