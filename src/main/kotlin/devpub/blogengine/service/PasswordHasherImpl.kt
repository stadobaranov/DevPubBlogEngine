package devpub.blogengine.service

import org.springframework.stereotype.Service
import java.lang.StringBuilder
import java.security.MessageDigest

private const val HASH_ALGORITHM = "SHA-256"
private const val HEX_SYMBOLS = "0123456789abcdef"

@Service
open class PasswordHasherImpl: PasswordHasher {
    private val messageDigestHolder = ThreadLocal<MessageDigest>()

    override fun hash(password: String): String {
        var messageDigest = messageDigestHolder.get()

        if(messageDigest == null) {
            messageDigest = MessageDigest.getInstance(HASH_ALGORITHM)
            messageDigestHolder.set(messageDigest)
        }

        return toHex(messageDigest.digest(password.toByteArray()))
    }

    private fun toHex(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)

        for(byte in bytes) {
            val i = byte.toInt()
            sb.append(HEX_SYMBOLS[(i and 0xF0) shr 4])
            sb.append(HEX_SYMBOLS[(i and 0x0F)])
        }

        return sb.toString()
    }
}