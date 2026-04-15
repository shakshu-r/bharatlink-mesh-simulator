package models

enum class MessageStatus {
    SENT,
    FORWARDED,
    DELIVERED
}

data class Message(
    val id: String,
    val sender: String,
    val receiver: String,
    val content: String,
    var ttl: Int = 5,
    var hops: Int = 0,
    var status: MessageStatus = MessageStatus.SENT
)
