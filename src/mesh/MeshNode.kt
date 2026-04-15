package mesh

import models.Device
import models.Message
import models.MessageStatus
import encryption.AESUtil

class MeshNode(val device: Device) {

    private val seenMessages = mutableSetOf<String>()

    fun receive(message: Message) {

        // Prevent duplicate messages
        if (seenMessages.contains(message.id)) return
        seenMessages.add(message.id)

        // Decrypt message
        val decrypted = AESUtil.decrypt(message.content)

        println("📡 Message ${message.id} reached ${device.id} (Hops: ${message.hops})")

        // If message reached destination
        if (device.id == message.receiver) {
            message.status = MessageStatus.DELIVERED
            println("📩 ${device.id} received: $decrypted")
            println("✅ Message Delivered in ${message.hops} hops\n")
            return
        }

        // Forward message if TTL allows
        if (message.ttl > 0) {
            message.ttl--
            message.hops++
            message.status = MessageStatus.FORWARDED

            forward(message)
        } else {
            println("❌ Message ${message.id} dropped (TTL expired)\n")
        }
    }

    private fun forward(message: Message) {
        for (neighbor in device.neighbors) {

            println("🔁 ${device.id} → forwarding to ${neighbor.id}")

            MeshNode(neighbor).receive(message)
        }
    }
}
