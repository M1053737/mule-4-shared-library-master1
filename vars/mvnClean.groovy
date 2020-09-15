import com.mulesoft.Constants

def call() {
    withMaven(
        globalMavenSettingsConfig: Constants.MAVEN_GLOBAL_SETTINGS
    ) {
        sh "mvn clean"
    }
}