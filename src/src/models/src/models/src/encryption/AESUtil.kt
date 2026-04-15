package encryption

import java.util.Base64

object AESUtil {

    fun encrypt(data: String): String {
        return Base64.getEncoder().encodeToString(data.toByteArray())
    }

    fun decrypt(data: String): String {
        return String(Base64.getDecoder().decode(data))
    }
}
