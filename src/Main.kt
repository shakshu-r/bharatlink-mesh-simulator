import mesh.MeshNetwork
import models.Device

fun main() {

    val network = MeshNetwork()

    val A = Device("A")
    val B = Device("B")
    val C = Device("C")
    val D = Device("D")

    // Connect devices (mesh)
    A.connect(B)
    B.connect(C)
    C.connect(D)

    network.addDevice(A)
    network.addDevice(B)
    network.addDevice(C)
    network.addDevice(D)

    // Send message
    network.sendMessage("A", "D", "Hello from BharatLink 🚀")
}
