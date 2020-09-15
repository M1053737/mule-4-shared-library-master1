import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Constants

def call() {
    withCredentials([usernamePassword(credentialsId: Constants.ANYPOINT_CREDENTIALS_ID, passwordVariable: 'password', usernameVariable: 'username')]) {
        def payload = "{ \"username\": \"${username}\", \"password\": \"${password}\" }"
        echo payload
        def response = httpRequest (
            url: "${Constants.ANYPOINT_URL}/accounts/login"
            , httpMode: 'POST'
            , requestBody: payload
            , contentType: 'APPLICATION_JSON'
        )
        def responseJson = new groovy.json.JsonSlurper().parseText(response.content)
        return responseJson.token_type + ' ' + responseJson.access_token
    }
}