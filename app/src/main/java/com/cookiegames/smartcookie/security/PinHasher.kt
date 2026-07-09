package com.cookiegames.smartcookie.security

import java.security.MessageDigest
import java.security.SecureRandom

/**
 * One-way hashing for locally stored PIN credentials.
 */
object PinHasher {

    fun generateSalt(length: Int = 16): ByteArray = ByteArray(length).also {
        SecureRandom().nextBytes(it)
    }

    fun hash(pin: String, salt: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(salt)
        digest.update(pin.toByteArray(Charsets.UTF_8))
        return digest.digest().joinToString(separator = "") { byte ->
            "%02x".format(byte)
        }
    }
}
