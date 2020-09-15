import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    ORGANIZATION_FORMATTED = pipelinePlaceholders.getOrganizationFormatted()
    def url

    if(pipelinePlaceholders.getApiAssetId()) {      // IMPLIES THIS IS AN API PROJECT 
        API_NAME_FORMATTED = pipelinePlaceholders.getApiAssetIdFormatted()
        url = "https://${Constants.MULE_DOMAIN}/api/v1/designCenter?name=${API_NAME_FORMATTED}"
    }
    

    echo url

    def response = httpRequest (
            httpMode: "GET",
            url: url,
            customHeaders: [[name: 'Authorization', value: "Basic OWNkZjYyYzJlOWY1NGQ5MDgxYThmNjA5YzExOWVlNjM6OGVBMjBlQjM0MjlmNGIzQ2IzMzJBMTY1MGQxOWZiRUY="], [name: 'Content-Type', value: 'application/json']],
            quiet: true
        )

    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    echo responseMap.apiId
    pipelinePlaceholders.setApiAutoDiscoveryId(responseMap.apiId)
    //echo responseMap
}