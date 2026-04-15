package mesh

import models.Device
import models.Message
import models.MessageStatus
import encryption.AESUtil
import java.util.*
import kotlin.concurrent.thread

class MeshNode(val device: Device) {

    private val seenMessages = mutableSetOf<String>()
    private val queue: Queue<Message> = LinkedList()
    private var isProcessing = false

    // Receive message → add to queue
    fun receive(message: Message) {

        if (seenMessages.contains(message.id)) return
        seenMessages.add(message.id)

        queue.add(message)

        processQueue()
    }

    // Process queue one by one
    private fun processQueue() {

        if (isProcessing) return
        isProcessing = true

        thread {
            while (queue.isNotEmpty()) {

                val message = queue.poll()

                // Simulate delay (500ms–1500ms)
                Thread.sleep((500..1500).random().toLong())

                handleMessage(message)
            }
            isProcessing = false
        }
    }

    private fun handleMessage(message: Message) {

        val decrypted = AESUtil.decrypt(message.content)

        println("📡 ${device.id} processing message ${message.id} (Hops: ${message.hops})")

        // If destination reached
        if (device.id == message.receiver) {
            message.status = MessageStatus.DELIVERED

            println("📩 ${device.id} received: $decrypted")
            println("✅ Delivered in ${message.hops} hops\n")
            return
        }

        // TTL check
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

            println("🔁 ${device.id} → ${neighbor.id} (queued)")

            MeshNode(neighbor).receive(message)
        }
    }
}
