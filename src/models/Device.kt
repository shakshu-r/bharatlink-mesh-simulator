package models

class Device(val id: String) {
    val neighbors = mutableListOf<Device>()

    fun connect(device: Device) {
        neighbors.add(device)
    }
}
