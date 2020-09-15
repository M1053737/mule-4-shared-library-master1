def call() {
    response = httpRequest (
        httpMode: "GET",
        url: "http://127.0.0.1:8200/v1/kv/data/credentials",
        customHeaders: [[name: "X-Vault-Token", value: "s.PHqsUyEswrMq164YfwskYJF6"]],
        quiet: true
    )
    responseJson = new groovy.json.JsonSlurperClassic().parseText(response.content)
    return responseJson.data.data
}

//  curl --header "X-Vault-Token: s.PHqsUyEswrMq164YfwskYJF6" --request GET http://127.0.0.1:8200/v1/kv/data/credentials | jq '.data.data'