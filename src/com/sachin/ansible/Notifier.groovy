package com.sachin.ansible

class Notifier {
    def steps
    Notifier(steps) { this.steps = steps }

    def success(Map config) {
        steps.slackSend(
            channel: config.SLACK_CHANNEL,
            color: 'good',
            message: "✅ ${config.ACTION_MESSAGE} | ENV=${config.ENVIRONMENT}"
        )
    }

    def failure(Map config) {
        steps.slackSend(
            channel: config.SLACK_CHANNEL,
            color: 'danger',
            message: "❌ Ansible failed | ENV=${config.ENVIRONMENT}"
        )
    }
}
