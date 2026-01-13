def call(Map gitCfg) {
    git branch: gitCfg.BRANCH,
        url: gitCfg.URL,
        credentialsId: gitCfg.CREDENTIALS_ID
}
