package mesh

import models.Device
import models.Message
import encryption.AESUtil

class MeshNode(val device: Device) {

    private val seenMessages = mutableSetOf<String>()

    fun receive(message: Message) {

        if (seenMessages.contains(message.id)) return
        seenMessages.add(message.id)

        val decrypted = AESUtil.decrypt(message.content)

        if (device.id == message.receiver) {
            println("📩 ${device.id} received: $decrypted")
            return
        }

        if (message.ttl > 0) {
            message.ttl--
            forward(message)
        }
    }

    private fun forward(message: Message) {
        for (neighbor in device.neighbors) {
            println("🔁 ${device.id} → forwarding to ${neighbor.id}")
            MeshNode(neighbor).receive(message)
        }
    }
}
