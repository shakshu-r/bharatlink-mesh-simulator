package mesh

import models.Device
import models.Message
import encryption.AESUtil
import java.util.*

class MeshNetwork {

    private val nodes = mutableMapOf<String, MeshNode>()

    fun addDevice(device: Device) {
        nodes[device.id] = MeshNode(device)
    }

    fun sendMessage(sender: String, receiver: String, text: String) {

        val encrypted = AESUtil.encrypt(text)

        val message = Message(
            id = UUID.randomUUID().toString(),
            sender = sender,
            receiver = receiver,
            content = encrypted
        )

        println("\n🚀 NEW MESSAGE INITIATED")
        println("📤 From: $sender → To: $receiver")
        println("🔐 Encrypted Payload: $encrypted\n")

        nodes[sender]?.receive(message)
    }
}
