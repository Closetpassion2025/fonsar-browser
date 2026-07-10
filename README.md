<p align="center"><img src="https://user-images.githubusercontent.com/44752343/112613474-0c339b00-8e18-11eb-8106-202c0c132715.png" width="150" alt="Fonsar Browser"></p>
<h2 align="center"><b>Fonsar Browser</b></h2>
<h4 align="center">A secure, free and open source WebView-based web browser for Android.</h4>

<p align="center">
<a href="https://github.com/Closetpassion2025/fonsar-browser/blob/master/NOTICE" alt="MPL-2.0 License"><img src="https://img.shields.io/badge/License-MPL--2.0-brightgreen.svg"></a>
<a href="https://github.com/Closetpassion2025/fonsar-browser/releases" alt="GitHub Release"><img src="https://img.shields.io/github/v/release/Closetpassion2025/fonsar-browser.svg"></a>
</p>
<hr>

---

## Support

Need help?

- [Open an issue](https://github.com/Closetpassion2025/fonsar-browser/issues) on this repository
- Read the [privacy policy](https://closetpassion2025.github.io/fonsar-browser/privacy-policy.html) (English and Português)

---

## Installation

Build from source with Android Studio or Gradle:

```bash
./gradlew :app:assembleScMainDebug
```

Release APK/AAB:

```bash
./gradlew :app:assembleScMainRelease
```

Release signing credentials go in `local.properties` (see `app/build.gradle`).

---

## Features

- Modern, clean UI
- Small download size
- Extensive settings
- Blocks ads and trackers
- Optional App Lock (PIN / biometric)

---

## Contributing

Contributions are welcome via [pull requests](https://github.com/Closetpassion2025/fonsar-browser/pulls) and [issues](https://github.com/Closetpassion2025/fonsar-browser/issues) on this fork.

---

## License

Fonsar Browser is a derivative work based on [SmartCookieWeb](https://github.com/CookieJarApps/SmartCookieWeb) by CookieJarApps, which is licensed under the **[Mozilla Public License 2.0 (MPL-2.0)](https://www.mozilla.org/en-US/MPL/2.0/)**. SmartCookieWeb is a fork of Lightning Browser (Copyright A.C.R. Development), also under MPL-2.0.

Modifications in this fork are licensed under MPL-2.0. Upstream copyright notices in source files are retained as required by the license. See [NOTICE](NOTICE) for attribution details.

Modified source: https://github.com/Closetpassion2025/fonsar-browser

Please read the MPL-2.0 before forking or redistributing.

---

## Online services

Fonsar Browser does not operate first-party online services. See [ONLINE.md](ONLINE.md) for details on user-initiated network activity (page loads, search engines, ad-block lists, and optional page translation).

---

## Lightning Browser

SmartCookieWeb is a fork of Lightning Browser intended to continue development. Here are some of the changes inherited from upstream:

- New, more modern, UI
- Parental controls
- Malware blocker
- Bottom navbar
- Custom navbar colour
- Bug fixes
- 'Force Zoom' option
- 'Inject JavaScript' option
- 'Force HTTPS' option
- 'Always incognito' option
- Cookie dialog blocker
