import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call() {
    withMaven(
        globalMavenSettingsConfig: Constants.MAVEN_GLOBAL_SETTINGS
    ) {
        sh "mvn -B release:clean"
        sh "mvn -B release:prepare -Dmule.skipDeploy=true -DskipMunitTests"
    }
}