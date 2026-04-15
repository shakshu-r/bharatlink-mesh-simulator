package models

data class Message(
    val id: String,
    val sender: String,
    val receiver: String,
    val content: String,
    var ttl: Int = 5
)
