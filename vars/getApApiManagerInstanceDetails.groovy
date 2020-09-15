import com.mulesoft.PipelinePlaceholders

def call(apiAssetDetails) {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    authorizationToken = pipelinePlaceholders.getAuthorizationToken()
    apiAssetDetailsForVersion = apiAssetDetails.versions.find { it.version == pipelinePlaceholders.getApiAssetVersion() }
    
    if(!apiAssetDetailsForVersion){
        return false
    }
    else {
        for (targetEnvironment in pipelinePlaceholders.getTargetEnvironments()) {
            apiInstanceDetails = apiAssetDetails.instances.findAll { 
                it.version == pipelinePlaceholders.getApiAssetVersion() && 
                it.type == "managed" && 
                it.environmentId == pipelinePlaceholders.getEnvironmentId(targetEnvironment)
            }
        
            if(apiInstanceDetails.size() > 1) {
                println "Multiple deployed instances of API asset found for this version, please check"
            }

            if(apiInstanceDetails.size() == 0) {
                deployApiAssetToApApiManager(
                    authorizationToken, 
                    apiAssetDetailsForVersion.assetId, 
                    apiAssetDetailsForVersion.version, 
                    apiAssetDetailsForVersion.groupId, 
                    targetEnvironment
                )
            }
            else {
                apiInstanceDetails = apiInstanceDetails[0]
                autoDiscoveryApiName = "groupId:" + apiInstanceDetails.groupId + ":assetId:" + apiInstanceDetails.assetId
                autoDiscoveryApiVersion = apiInstanceDetails.name

                pipelinePlaceholders.setAutoDiscoveryApiName(targetEnvironment, autoDiscoveryApiName)
                pipelinePlaceholders.setAutoDiscoveryApiVersion(targetEnvironment, autoDiscoveryApiVersion)
            }
        }
        return true
    }
}