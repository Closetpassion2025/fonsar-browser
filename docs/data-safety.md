# Fonsar Browser — Google Play Data Safety

Reference for completing the **Data safety** form in Google Play Console.  
**Application ID:** `com.fonsar.browser`  
**Last updated:** 10 July 2026

---

## Summary for Play Console

| Question | Answer |
|----------|--------|
| Does your app collect or share user data? | **Yes** — user-initiated requests to third parties (websites, search engines, optional Google Translate). Fonsar does not operate collection servers. |
| Is all data encrypted in transit? | **Yes** for HTTPS traffic (browser default). User may visit HTTP sites. |
| Can users request data deletion? | Users can clear browsing data in-app (history, cookies, cache). No Fonsar account. |

---

## Data types — user-initiated third-party flows

### Page URL sent to Google (Translate page)

| Field | Value |
|-------|--------|
| **Feature** | ⋮ menu → **Translate page** (only if enabled in Settings → General → Translate page) |
| **When collected** | Only when the user taps **Translate page** |
| **What is sent** | Full URL of the current web page |
| **Recipient** | Google (`*.translate.goog` proxy) |
| **Purpose** | App functionality — machine translation of page content |
| **Required or optional** | Optional (feature off by default; user must enable setting and tap menu item) |
| **Shared or collected** | **Shared** with third party (Google); not stored by Fonsar |
| **Ephemeral** | Yes — request is between device and Google for that translation session |

**Play Console mapping (typical):**

- Data type: **Web browsing history** or **Other user-generated content** → URL of page being viewed  
- Shared: **Yes**  
- Purpose: **App functionality**  
- Collection: **Optional**, user-initiated  

### Other user-initiated network activity (documented in privacy policy)

- Search queries → selected search engine  
- Search suggestions → Google / DuckDuckGo / Baidu / Naver (if enabled)  
- Page loads → websites the user visits  
- Ad-block list updates → user-configured remote hosts URL (optional)  

---

## Data stored on device (not “collected” by Fonsar servers)

Browsing history, bookmarks, cookies, downloads, settings, App Lock credentials — **local only**. See [privacy-policy.md](privacy-policy.md).

---

## Data not collected

- No Fonsar analytics, crash reporting, or advertising SDKs in the current codebase  
- No account registration  
- No sale of personal data  

---

## Privacy policy URL

Use the published URL for [docs/privacy-policy.md](privacy-policy.md) when submitting the app listing.
