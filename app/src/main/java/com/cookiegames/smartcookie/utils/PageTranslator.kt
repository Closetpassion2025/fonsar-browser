package com.cookiegames.smartcookie.utils

import android.net.Uri
import android.os.Build
import java.util.Locale

/**
 * Builds Google Translate proxy URLs (translate.goog host rewrite).
 */
object PageTranslator {

    private const val TRANSLATE_GOOG_DOMAIN = ".translate.goog"
    private const val PARAM_SOURCE_LANG = "_x_tr_sl"
    private const val PARAM_TARGET_LANG = "_x_tr_tl"
    private const val SOURCE_LANG_AUTO = "auto"

    fun targetLanguageTag(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Locale.getDefault().toLanguageTag()
        } else {
            @Suppress("DEPRECATION")
            Locale.getDefault().language
        }

    /**
     * Rewrites [originalUrl] to https://{host-with-dashes}.translate.goog/... with translation params.
     */
    fun buildTranslateGoogUrl(
        originalUrl: String?,
        targetLanguageTag: String = targetLanguageTag()
    ): String? {
        if (originalUrl.isNullOrBlank()) return null

        val uri = Uri.parse(originalUrl)
        val scheme = uri.scheme?.lowercase(Locale.US)
        if (scheme != "http" && scheme != "https") return null

        val host = uri.host ?: return null
        if (host.contains(TRANSLATE_GOOG_DOMAIN)) return null

        val translatedAuthority = host.replace('.', '-') + TRANSLATE_GOOG_DOMAIN
        val builder = Uri.Builder()
            .scheme("https")
            .authority(translatedAuthority)

        uri.path?.takeIf { it.isNotEmpty() }?.let { builder.path(it) }
        uri.fragment?.let { builder.fragment(it) }

        uri.queryParameterNames
            .filter { it != PARAM_SOURCE_LANG && it != PARAM_TARGET_LANG }
            .forEach { name ->
                uri.getQueryParameters(name).forEach { value ->
                    builder.appendQueryParameter(name, value)
                }
            }

        builder.appendQueryParameter(PARAM_SOURCE_LANG, SOURCE_LANG_AUTO)
        builder.appendQueryParameter(PARAM_TARGET_LANG, targetLanguageTag)

        return builder.build().toString()
    }

    fun isTranslateGoogUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        return Uri.parse(url).host?.contains(TRANSLATE_GOOG_DOMAIN) == true
    }
}
