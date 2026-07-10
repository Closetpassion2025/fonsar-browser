package com.cookiegames.smartcookie.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PageTranslatorInstrumentedTest {

    @Test
    fun buildTranslateGoogUrl_rewritesHostAndAddsParams() {
        assertEquals(
            "https://example-com.translate.goog/path?q=1&_x_tr_sl=auto&_x_tr_tl=pt-PT",
            PageTranslator.buildTranslateGoogUrl(
                "https://example.com/path?q=1",
                "pt-PT"
            )
        )
    }

    @Test
    fun buildTranslateGoogUrl_rejectsSpecialAndProxyUrls() {
        assertNull(PageTranslator.buildTranslateGoogUrl("file:///android_asset/home.html"))
        assertNull(
            PageTranslator.buildTranslateGoogUrl(
                "https://example-com.translate.goog/page?_x_tr_sl=auto&_x_tr_tl=en"
            )
        )
    }

    @Test
    fun isTranslateGoogUrl_detectsProxyHost() {
        assertTrue(
            PageTranslator.isTranslateGoogUrl(
                "https://www-bbc-com.translate.goog/news?_x_tr_sl=auto&_x_tr_tl=en"
            )
        )
    }
}
