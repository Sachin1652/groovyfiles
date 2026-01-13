def call(String environment, boolean enabled) {
    if (enabled) {
        input message: "Approve deployment to ${environment}?",
              ok: "Deploy"
    }
}
