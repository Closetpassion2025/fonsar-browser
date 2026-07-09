package com.cookiegames.smartcookie.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class PinHasherTest {

    @Test
    fun hash_isDeterministicForSamePinAndSalt() {
        val salt = PinHasher.generateSalt()
        assertEquals(
            PinHasher.hash("1234", salt),
            PinHasher.hash("1234", salt)
        )
    }

    @Test
    fun hash_differsWhenPinOrSaltChanges() {
        val salt = PinHasher.generateSalt()
        val baseline = PinHasher.hash("1234", salt)
        assertNotEquals(baseline, PinHasher.hash("4321", salt))
        assertNotEquals(baseline, PinHasher.hash("1234", PinHasher.generateSalt()))
    }
}
