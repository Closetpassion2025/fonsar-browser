package com.cookiegames.smartcookie.utils

import android.os.Build
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.util.Locale

/**
 * Builds Google Translate proxy URLs (translate.goog host rewrite).
 *
 * Host encoding (see [encodeHostForTranslateGoog]):
 * 1. Lowercase the host
 * 2. Escape existing hyphens: `-` → `--`
 * 3. Replace dots with hyphens: `.` → `-`
 * Example: `some-site.com` → `some--site-com.translate.goog`
 *
 * Target language mapping for `_x_tr_tl` (see [mapTargetLanguageTag]):
 * | Device / input tag | `_x_tr_tl` | Notes |
 * |--------------------|------------|-------|
 * | pt-PT, pt          | pt         | Default: ISO 639-1 language code |
 * | pt-BR              | pt-BR      | Brazil region code required by Google |
 * | zh-CN, zh-Hans, zh | zh-CN      | Simplified Chinese |
 * | zh-TW, zh-Hant     | zh-TW      | Traditional Chinese |
 * | en-US, en-GB, en   | en         | Default: language code only |
 * | (other)            | {language} | [Locale.getLanguage] two-letter code |
 *
 * Rejected URLs: non-http(s), explicit non-default port, punycode (xn--) labels, already-proxied hosts.
 */
object PageTranslator {

    private const val TRANSLATE_GOOG_SUFFIX = "translate.goog"
    private const val PARAM_SOURCE_LANG = "_x_tr_sl"
    private const val PARAM_TARGET_LANG = "_x_tr_tl"
    private const val SOURCE_LANG_AUTO = "auto"

    fun targetLanguageTag(): String = mapTargetLanguageTag(defaultLocaleLanguageTag())

    /**
     * Maps a BCP 47 language tag to the `_x_tr_tl` value Google Translate expects.
     */
    fun mapTargetLanguageTag(languageTag: String): String {
        val tag = languageTag.trim().lowercase(Locale.US).replace('_', '-')
        if (tag.isEmpty()) return "en"

        return when {
            tag == "pt-br" || tag.startsWith("pt-br-") -> "pt-BR"
            tag == "zh-tw" || tag.startsWith("zh-tw-") || tag.contains("-hant") -> "zh-TW"
            tag == "zh-cn" || tag.startsWith("zh-cn-") || tag.contains("-hans") || tag == "zh" -> "zh-CN"
            tag.startsWith("zh") -> "zh-CN"
            else -> Locale.forLanguageTag(languageTag).language.ifBlank {
                tag.substringBefore('-').take(2)
            }
        }
    }

    /**
     * Encodes a hostname for the translate.goog proxy authority.
     * Existing hyphens are doubled first; dots become single hyphens.
     */
    internal fun encodeHostForTranslateGoog(host: String): String =
        host.lowercase(Locale.US)
            .replace("-", "--")
            .replace(".", "-")

    /**
     * Rewrites [originalUrl] to `https://{encoded-host}.translate.goog/...` with translation params.
     * Original query parameters are preserved; `_x_tr_sl` / `_x_tr_tl` are appended last.
     * URL fragments are preserved when present.
     */
    fun buildTranslateGoogUrl(
        originalUrl: String?,
        targetLanguageTag: String = targetLanguageTag()
    ): String? {
        if (originalUrl.isNullOrBlank()) return null

        val parsed = originalUrl.trim().toHttpUrlOrNull() ?: return null
        val scheme = parsed.scheme
        if (scheme != "http" && scheme != "https") return null
        if (parsed.port != HttpUrl.defaultPort(scheme)) return null

        val host = parsed.host
        if (!isValidSourceHost(host)) return null

        val translatedHost = "${encodeHostForTranslateGoog(host)}.$TRANSLATE_GOOG_SUFFIX"
        val mappedTarget = mapTargetLanguageTag(targetLanguageTag)

        val builder = HttpUrl.Builder()
            .scheme("https")
            .host(translatedHost)

        parsed.pathSegments.forEach { builder.addPathSegment(it) }

        for (i in 0 until parsed.querySize) {
            val name = parsed.queryParameterName(i)
            if (name == PARAM_SOURCE_LANG || name == PARAM_TARGET_LANG) continue
            builder.addQueryParameter(name, parsed.queryParameterValue(i))
        }
        builder.addQueryParameter(PARAM_SOURCE_LANG, SOURCE_LANG_AUTO)
        builder.addQueryParameter(PARAM_TARGET_LANG, mappedTarget)

        parsed.encodedFragment?.let { builder.encodedFragment(it) }

        return builder.build().toString()
    }

    fun isTranslateGoogUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        val host = url.toHttpUrlOrNull()?.host ?: return false
        return host == TRANSLATE_GOOG_SUFFIX || host.endsWith(".$TRANSLATE_GOOG_SUFFIX")
    }

    private fun isValidSourceHost(host: String): Boolean {
        val lower = host.lowercase(Locale.US)
        if (lower.isEmpty()) return false
        if (lower == TRANSLATE_GOOG_SUFFIX || lower.endsWith(".$TRANSLATE_GOOG_SUFFIX")) return false
        if (lower.split('.').any { it.startsWith("xn--") }) return false
        return true
    }

    private fun defaultLocaleLanguageTag(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Locale.getDefault().toLanguageTag()
        } else {
            @Suppress("DEPRECATION")
            Locale.getDefault().language
        }
}
