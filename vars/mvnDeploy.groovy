import com.mulesoft.Constants

def call() {
    withMaven(globalMavenSettingsConfig: Constants.MAVEN_GLOBAL_SETTINGS) {
        sh "mvn clean package deploy:deploy"
    }

    pom = readMavenPom()

	def jarFileName = "target/${pom.getArtifactId()}-${pom.getVersion()}-mule-application.jar"

	return jarFileName
}