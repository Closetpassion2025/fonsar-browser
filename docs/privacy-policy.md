# Fonsar Browser — Privacy Policy

**Last updated:** 10 July 2026  
**Application ID:** `com.fonsar.browser`  
**Contact:** [A CONFIRMAR — privacy contact email]

This document describes how **Fonsar Browser** handles information. It reflects the behaviour of the app as shipped in this repository.

---

## English

### Summary

Fonsar Browser does **not** operate first-party online services. Data stays on your device unless **you** initiate a network request (for example by loading a webpage, searching, or updating an ad-block list). We do not embed Firebase, Google Analytics, AdMob, or similar third-party analytics or advertising SDKs in the current codebase.

### Data we do not collect

Fonsar Browser does **not**:

- Run Fonsar-operated servers that receive your browsing data
- Collect analytics or crash reports to Fonsar-operated endpoints
- Sell or share your personal data with Fonsar for marketing

### Data stored on your device

The app may store the following **locally** on your device:

| Category | Purpose | Location |
|----------|---------|----------|
| Browsing history | Let you revisit pages | Local database |
| Bookmarks | Saved pages | Local database |
| Cookies & site data | Normal browser behaviour | WebView / app storage |
| Downloads | Files you choose to save | Device storage |
| Settings & preferences | App configuration | SharedPreferences |
| App Lock credentials | PIN (salted hash) and/or biometric gate | EncryptedSharedPreferences + Android Keystore |

You can clear history, cookies, cache, and other data from the in-app privacy settings.

### Network activity initiated by you

When you use the browser, network requests may be sent to **third parties** you interact with:

1. **Websites you visit** — The app loads pages you request. Those sites may collect data according to their own policies.
2. **Search engines** — Queries are sent to the search engine you select in Settings (e.g. Google, DuckDuckGo, Bing, Yahoo, Baidu, Naver, Startpage, Ecosia, Yandex, Ask, Ekoru, Searx, or a custom URL).
3. **Search suggestions** — If enabled, partial queries may be sent to Google, DuckDuckGo, Baidu, or Naver (configurable; can be disabled).
4. **Ad-block / hosts lists** — By default, blocking rules are loaded from bundled assets. You may optionally configure a remote hosts file URL; downloading that list contacts the server you specify.
5. **Page translation (optional)** — If you enable **Translate page** in General settings, a **Translate page** entry appears in the ⋮ menu. When you tap it, the app rewrites the current page URL to Google’s `translate.goog` proxy (with `_x_tr_sl=auto` and `_x_tr_tl` set to your device language). Google receives the full page URL to fetch and translate content. This feature is off by default.

Malware blocking uses a bundled local list (`malware.txt`); it does not phone home to Fonsar.

### Permissions

The app may request Android permissions so features work when you use them:

| Permission | Why |
|------------|-----|
| `INTERNET` | Load web pages |
| `ACCESS_NETWORK_STATE` | Detect connectivity |
| `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` | Only if a website requests geolocation and you grant it |
| `READ/WRITE_EXTERNAL_STORAGE` | Downloads and file access (legacy storage model on older Android versions) |
| `CAMERA` / `RECORD_AUDIO` / `MODIFY_AUDIO_SETTINGS` | Only if a website requests camera/microphone and you grant it |
| `POST_NOTIFICATIONS` | Download completion notifications (Android 13+) |
| `INSTALL_SHORTCUT` | Optional home-screen shortcuts |

Permissions are used in context; the app does not continuously track location in the background for Fonsar.

### Android backup

The app allows Android’s backup mechanism (`android:allowBackup="true"`) for general settings and browsing data. App Lock PIN credentials are **excluded** from cloud backup and device transfer via `fullBackupContent` / `dataExtractionRules`, because they are bound to the device keystore and must not leave the device. Biometric App Lock relies on your device’s screen lock.

### App Lock

Optional App Lock protects access with a custom PIN and/or Android biometric APIs (`BiometricPrompt`). PINs are stored on-device as salted hashes inside `EncryptedSharedPreferences` (Android Keystore-backed); plaintext PINs are not kept. Biometric unlock uses the system credential store only. Credentials are not sent to Fonsar.

### WebView metrics

The app sets `android.webkit.WebView.MetricsOptOut` to opt out of WebView metrics collection where supported by the system WebView provider.

### Debug builds only

Debug builds may include **LeakCanary** (memory-leak detection), enabled from Developer settings. LeakCanary runs locally and is not included in release builds by default.

### Children

Fonsar Browser includes parental-control features (upstream). Configuration is local. We do not knowingly collect personal information from children on Fonsar-operated servers because none exist.

### Changes

We may update this policy when the app changes. The “Last updated” date at the top will be revised accordingly.

### Contact

Questions about privacy: [A CONFIRMAR — privacy contact email]

---

## Português (Portugal)

### Resumo

O **Fonsar Browser** **não** opera serviços online próprios. Os dados permanecem no seu dispositivo, excepto quando **você** inicia um pedido de rede (por exemplo, ao abrir uma página, pesquisar ou actualizar uma lista de bloqueio de anúncios). O código actual **não** inclui Firebase, Google Analytics, AdMob ou SDKs similares de analítica ou publicidade.

### Dados que não recolhemos

O Fonsar Browser **não**:

- Executa servidores Fonsar que recebam os seus dados de navegação
- Recolhe analítica ou relatórios de falhas para endpoints operados pela Fonsar
- Vende ou partilha os seus dados pessoais com a Fonsar para marketing

### Dados guardados no dispositivo

A app pode guardar localmente:

| Categoria | Finalidade | Local |
|-----------|------------|-------|
| Histórico de navegação | Revisitar páginas | Base de dados local |
| Marcadores | Páginas guardadas | Base de dados local |
| Cookies e dados de sites | Comportamento normal do browser | WebView / armazenamento da app |
| Transferências | Ficheiros que escolhe guardar | Armazenamento do dispositivo |
| Definições e preferências | Configuração da app | SharedPreferences |
| Credenciais do App Lock | PIN (hash com salt) e/ou biometria | EncryptedSharedPreferences + Android Keystore |

Pode limpar histórico, cookies, cache e outros dados nas definições de privacidade.

### Actividade de rede iniciada por si

Ao usar o browser, podem ser enviados pedidos a **terceiros** com quem interage:

1. **Sites que visita** — A app carrega as páginas que solicita. Esses sites regem-se pelas suas próprias políticas.
2. **Motores de pesquisa** — As consultas são enviadas ao motor seleccionado nas Definições (ex.: Google, DuckDuckGo, Bing, Yahoo, Baidu, Naver, Startpage, Ecosia, Yandex, Ask, Ekoru, Searx ou URL personalizado).
3. **Sugestões de pesquisa** — Se activadas, consultas parciais podem ser enviadas ao Google, DuckDuckGo, Baidu ou Naver (configurável; pode ser desactivado).
4. **Listas de bloqueio / hosts** — Por defeito, as regras vêm de ficheiros incluídos na app. Opcionalmente pode configurar um URL remoto; o download contacta o servidor que indicar.
5. **Tradução de páginas (opcional)** — Se activar **Traduzir página** nas Definições gerais, aparece a entrada no menu ⋮. Ao tocar, a app reescreve o URL actual para o proxy `translate.goog` da Google (`_x_tr_sl=auto`, `_x_tr_tl` = idioma do dispositivo). A Google recebe o URL completo da página para a traduzir. Funcionalidade desactivada por defeito.

O bloqueio de malware usa uma lista local incluída (`malware.txt`); não comunica com servidores Fonsar.

### Permissões

A app pode solicitar permissões Android para funcionalidades que utiliza:

| Permissão | Motivo |
|-----------|--------|
| `INTERNET` | Carregar páginas web |
| `ACCESS_NETWORK_STATE` | Detectar conectividade |
| `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` | Apenas se um site pedir geolocalização e você autorizar |
| `READ/WRITE_EXTERNAL_STORAGE` | Transferências e ficheiros (modelo legado em versões antigas do Android) |
| `CAMERA` / `RECORD_AUDIO` / `MODIFY_AUDIO_SETTINGS` | Apenas se um site pedir câmara/microfone e você autorizar |
| `POST_NOTIFICATIONS` | Notificações de transferências concluídas (Android 13+) |
| `INSTALL_SHORTCUT` | Atalhos opcionais no ecrã inicial |

As permissões são usadas em contexto; a app não rastreia localização em segundo plano para a Fonsar.

### Backup Android

A app permite o mecanismo de backup do Android (`android:allowBackup="true"`) para definições gerais e dados de navegação. As credenciais PIN do App Lock estão **excluídas** do backup na cloud e da transferência entre dispositivos (`fullBackupContent` / `dataExtractionRules`), por estarem ligadas ao keystore do dispositivo. O App Lock biométrico depende do bloqueio de ecrã do sistema.

### App Lock

O App Lock opcional protege o acesso com PIN personalizado e/ou APIs biométricas do Android (`BiometricPrompt`). Os PINs ficam no dispositivo como hashes com salt em `EncryptedSharedPreferences` (com Android Keystore); o PIN em texto simples não é guardado. O desbloqueio biométrico usa apenas as credenciais do sistema. Nada é enviado à Fonsar.

### Métricas WebView

A app define `android.webkit.WebView.MetricsOptOut` para optar por não participar na recolha de métricas WebView, quando suportado pelo WebView do sistema.

### Builds de debug

Builds de debug podem incluir **LeakCanary** (detecção de fugas de memória), activável nas definições de programador. O LeakCanary corre localmente e não está incluído nas builds de release por defeito.

### Menores

O Fonsar Browser inclui controlos parentais (herdados do upstream). A configuração é local. Não recolhemos intencionalmente dados pessoais de menores em servidores Fonsar porque estes não existem.

### Alterações

Esta política pode ser actualizada quando a app mudar. A data «Last updated» no topo será revista.

### Contacto

Questões sobre privacidade: [A CONFIRMAR — email de contacto]
