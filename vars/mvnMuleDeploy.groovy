import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call(String deploymentType, String targetEnvironment) {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()

    if(deploymentType == "deploySnapshotFromLocal") {
        fileSpecifier = ""
    }
    else if(deploymentType == "deployReleaseFromLocal") {
        fileSpecifier = "-f target/checkout/pom.xml"
    }

    withCredentials([
        usernamePassword(credentialsId: Constants.ANYPOINT_CREDENTIALS_ID, passwordVariable: 'apPassword', usernameVariable: 'apUsername')
    ])
    {
        apDetailsIdentifier = pipelinePlaceholders.getOrganizationNoSpaces() + "_" + targetEnvironment
        withCredentials([
            usernamePassword(credentialsId: Constants.ANYPOINT_CREDENTIALS_IDS[apDetailsIdentifier], passwordVariable: 'apSecret', usernameVariable: 'apClientId')
        ])
        {
            withMaven(
                globalMavenSettingsConfig: Constants.MAVEN_GLOBAL_SETTINGS
            ) 
            {
                sh "mvn deploy ${fileSpecifier} -Dmaven.skipDeploy=true -DskipTests -Danypoint.platform.username=${apUsername} -Danypoint.platform.password=${apPassword} -Danypoint.platform.client_id=${apClientId} -Danypoint.platform.client_secret=${apSecret} -Danypoint.platform.environment=${targetEnvironment} -Danypoint.platform.target=${Constants.ANYPOINT_TARGETS[apDetailsIdentifier]} -Danypoint.platform.target.type=${Constants.ANYPOINT_TARGET_TYPES[apDetailsIdentifier]} -Danypoint.platform.redeploy=true"
            }
        }
    
    }
}