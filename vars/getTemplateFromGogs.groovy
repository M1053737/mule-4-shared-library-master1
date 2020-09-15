import com.mulesoft.Constants

def call(String template) {
    deleteDir()
    def url = "https://${Constants.GOGS_DOMAIN}/${Constants.GOGS_TEMPLATES_ORGANIZATION}/${template}.git"
    git credentialsId: Constants.GOGS_CREDENTIALS_ID, url: url
    sh 'rm -r .git'
}

