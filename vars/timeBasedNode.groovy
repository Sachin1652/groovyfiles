def call(String agentLabel, Closure body) {

    def hour = new Date().format(
        'HH',
        TimeZone.getTimeZone('Asia/Kolkata')
    ) as int

    echo "Current Hour (IST): ${hour}"

    if (hour >= 9 && hour < 18) {
        node(agentLabel) {
            body()
        }
    } else {
        node('built-in') {
            body()
        }
    }
}
