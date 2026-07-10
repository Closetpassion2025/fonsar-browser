package com.cookiegames.smartcookie.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/** Smoke test on device; full coverage lives in [PageTranslatorTest] (JVM). */
@RunWith(AndroidJUnit4::class)
class PageTranslatorInstrumentedTest {

    @Test
    fun buildTranslateGoogUrl_smokeOnDevice() {
        assertEquals(
            "https://example-com.translate.goog/path?q=1&_x_tr_sl=auto&_x_tr_tl=pt",
            PageTranslator.buildTranslateGoogUrl(
                "https://example.com/path?q=1",
                "pt-PT"
            )
        )
    }
}
