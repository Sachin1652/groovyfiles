package com.sachin.ansible

class Clone {
    def steps
    Clone(steps) { this.steps = steps }

    def run(Map config) {
        steps.git(
            url: config.REPO_URL,
            branch: config.BRANCH,
            credentialsId: config.GIT_CREDENTIALS
        )
    }
}
