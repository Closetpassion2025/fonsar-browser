package com.cookiegames.smartcookie.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PageTranslatorTest {

    @Test
    fun encodeHostForTranslateGoog_escapesExistingHyphensBeforeDots() {
        assertEquals("some--site-com", PageTranslator.encodeHostForTranslateGoog("some-site.com"))
        assertEquals("example-com", PageTranslator.encodeHostForTranslateGoog("example.com"))
        assertEquals("en-m-wikipedia-org", PageTranslator.encodeHostForTranslateGoog("en.m.wikipedia.org"))
    }

    @Test
    fun mapTargetLanguageTag_usesLanguageCodeWithExplicitExceptions() {
        assertEquals("pt", PageTranslator.mapTargetLanguageTag("pt-PT"))
        assertEquals("pt", PageTranslator.mapTargetLanguageTag("pt"))
        assertEquals("pt-BR", PageTranslator.mapTargetLanguageTag("pt-BR"))
        assertEquals("en", PageTranslator.mapTargetLanguageTag("en-US"))
        assertEquals("zh-CN", PageTranslator.mapTargetLanguageTag("zh-CN"))
        assertEquals("zh-CN", PageTranslator.mapTargetLanguageTag("zh-Hans"))
        assertEquals("zh-TW", PageTranslator.mapTargetLanguageTag("zh-TW"))
        assertEquals("zh-TW", PageTranslator.mapTargetLanguageTag("zh-Hant-TW"))
    }

    @Test
    fun buildTranslateGoogUrl_rewritesHostAndAddsParams() {
        assertEquals(
            "https://example-com.translate.goog/path?q=1&_x_tr_sl=auto&_x_tr_tl=pt",
            PageTranslator.buildTranslateGoogUrl(
                "https://example.com/path?q=1",
                "pt-PT"
            )
        )
    }

    @Test
    fun buildTranslateGoogUrl_hyphenatedDomain() {
        assertEquals(
            "https://some--site-com.translate.goog/?_x_tr_sl=auto&_x_tr_tl=en",
            PageTranslator.buildTranslateGoogUrl("https://some-site.com/", "en")
        )
    }

    @Test
    fun buildTranslateGoogUrl_multiSubdomainAndPath() {
        assertEquals(
            "https://en-m-wikipedia-org.translate.goog/wiki/Portugal?_x_tr_sl=auto&_x_tr_tl=pt",
            PageTranslator.buildTranslateGoogUrl(
                "https://en.m.wikipedia.org/wiki/Portugal",
                "pt-PT"
            )
        )
    }

    @Test
    fun buildTranslateGoogUrl_preservesQueryAndAppendsTranslationParams() {
        assertEquals(
            "https://example-com.translate.goog/search?q=foo&page=2&_x_tr_sl=auto&_x_tr_tl=pt",
            PageTranslator.buildTranslateGoogUrl(
                "https://example.com/search?q=foo&page=2",
                "pt-PT"
            )
        )
    }

    @Test
    fun buildTranslateGoogUrl_preservesFragment() {
        assertEquals(
            "https://example-com.translate.goog/search?q=foo&page=2&_x_tr_sl=auto&_x_tr_tl=pt#results",
            PageTranslator.buildTranslateGoogUrl(
                "https://example.com/search?q=foo&page=2#results",
                "pt-PT"
            )
        )
    }

    @Test
    fun buildTranslateGoogUrl_rejectsExplicitPort() {
        assertNull(PageTranslator.buildTranslateGoogUrl("https://example.com:8080/path"))
        assertNull(PageTranslator.buildTranslateGoogUrl("http://example.com:8080/path"))
    }

    @Test
    fun buildTranslateGoogUrl_allowsDefaultPorts() {
        assertEquals(
            "https://example-com.translate.goog/path?_x_tr_sl=auto&_x_tr_tl=en",
            PageTranslator.buildTranslateGoogUrl("http://example.com:80/path", "en")
        )
        assertEquals(
            "https://example-com.translate.goog/path?_x_tr_sl=auto&_x_tr_tl=en",
            PageTranslator.buildTranslateGoogUrl("https://example.com:443/path", "en")
        )
    }

    @Test
    fun buildTranslateGoogUrl_rejectsPunycodeIdn() {
        assertNull(PageTranslator.buildTranslateGoogUrl("https://xn--mnchen-3ya.de/"))
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
