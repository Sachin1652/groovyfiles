package com.sachin.ansible

class Approval {
    def steps
    Approval(steps) { this.steps = steps }

    def run(Map config) {
        steps.input message: "Approve Ansible execution for ${config.ENVIRONMENT} environment?"
    }
}
