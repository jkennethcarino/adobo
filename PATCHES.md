# Patches

> Generated from `patches-list.json` — **v2.0.1** (`main`) · **55 patches** across **4 apps** · back to [README](README.md)

---

## 9GAG

**Package name:** `com.ninegag.android.app`

**Supported versions:** `8.30.04`, `8.29.08`, `8.26.1`, `8.25.1`, `8.24.8`

| Patch | Details |
|---|---|
| **Disable forced update dialog** | Disables the forced update dialog on an outdated app version. |
| **Remove 9GAG's ads, trackers, and analytics** | Removes ads, trackers, and analytics in the 9GAG app. |
| **Sanitize share links** | Removes the tracking query parameters from shared links. |

---

## Gboard

**Package name:** `com.google.android.inputmethod.latin`

| Patch | Details |
|---|---|
| **Always-incognito mode** | Always opens Gboard in incognito mode to disable typing history collection and personalization. |
| **Enable OCR feature** | Enables the Scan Text feature to extract text from images and insert them into text fields. |
| **Enable Undo feature** | Enables the undo feature to quickly undo or correct typing mistakes. |
| **Enable access points menu redesign** | Enables the redesign of the additional actions menu. |
| **Enable clipboard in incognito** | Enables clipboard support in incognito mode. |
| **Enable extended clipboard history** | Enables extending the clipboard history expiration time. |
| **Enable key shape selection** | Enables an option to select a key shape from the theme. |
| **Enable voice typing in incognito** | Enables voice typing in incognito mode. |
| **Hide incognito icon** | Replaces the incognito icon from the toolbar with the normal access point icon. Incognito mode still applies to the current input session. |
| **Toggle feature flags** | Toggles Gboard feature flags to enable or disable experimental or hidden features.<br><sub>Options: Feature flags, Enable feature flags</sub> |

---

## IMDb

**Package name:** `com.imdb.mobile`

| Patch | Details |
|---|---|
| **Remove IMDb's ads, trackers, and analytics** | Removes ads, trackers, and analytics in the IMDb app. |

---

## Reddit

**Package name:** `com.reddit.frontpage`

**Supported versions:** `2026.47.0`, `2026.46.0`, `2026.45.0`, `2026.44.0`, `2026.24.0`

| Patch | Details |
|---|---|
| **Colorize comment indent lines** | Replaces the default gray comment indent lines with color-coded lines.<br><sub>Options: Show indent line at current depth only, Line color 1, Line color 2, +6 more</sub> |
| **Disable bottom navigation bar auto-hide** | Prevents the bottom navigation bar from hiding when scrolling down. |
| **Disable home feed auto-refresh** | Disables the automatic refresh of the home feed after viewing a post or after being away from the app. |
| **Disable home feed refresh on back to exit** | Disables the back-to-top scroll and home feed refresh on back navigation to exit. |
| **Disable home feed swipe** | Disables the horizontal page swipe gesture used to switch feeds. |
| **Disable home screen redirect** | Disables the automatic redirect to the home screen after being away from the app or returning from the background. |
| **Disable post detail swipe** | Disables the horizontal swipe gesture used to navigate between posts. |
| **Disable screenshot banner** | Disables the banner that shows up after taking a screenshot. |
| **Enable guest mode** | Enables browsing Reddit without signing in. |
| **Hide Ask button from search bar** | Hides the Ask button (Reddit Answers) from the search bar. |
| **Hide awards** | Hides the awards on Reddit posts and comments. |
| **Hide community highlights** | Hides the community highlights section. |
| **Hide community menu badge** | Hides the red notification badge on the community menu (hamburger menu). |
| **Hide crosspost** | Hides the crosspost on Reddit posts. |
| **Hide mod queue's auto-enforce banner** | Hide the auto-enforce promotion banner in the mod queue. |
| **Hide post view counts** | Removes the "views" indicator from the post metadata. |
| **Hide prominent search bar** | Hides the new prominent search bar on the main screen. Applicable only to version 2026.24.0 and earlier. |
| **Hide related posts** | Hides the related posts section displayed at the bottom of a post. |
| **Hide search answers preview** | Hides the Reddit Answers preview section at the top of search results. |
| **Hide share count** | Hides the share count on Reddit posts. |
| **Hide upvote scores** | Hides the scores on Reddit posts and comments.<br><sub>Options: Hide post scores, Hide comment scores</sub> |
| **Hide user community badges** | Hides the user community badges on Reddit comments. |
| **Hide user flairs** | Hides the user flairs on Reddit comments. |
| **Hide videos for you** | Hides the "Videos for you" or video carousel recommendation section between feed posts. |
| **Make system navigation bar transparent** | Sets the system navigation bar to fully transparent. |
| **Open external links directly** | Opens external links directly without going through out.reddit.com. |
| **Remove ads and telemetry** | Removes ads and telemetry everywhere. |
| **Sanitize share links** | Unshortens and removes the tracking query parameters from shared links. |

---

## Universal

| Patch | Details |
|---|---|
| **Block ads, trackers, and analytics** | Blocks ads, trackers, analytics, and unwanted content in apps and games using a hosts file.<br><sub>Options: Hosts file, Redirection IP, Wildcard blocking</sub> |
| **Change package name** | Appends ".adobo" to the package name by default. Changing the package name of the app can lead to unexpected issues.<br><sub>Options: Package name, Update permissions, Update other permissions, +1 more</sub> |
| **Deactivate Firebase Analytics** | Deactivates Firebase Analytics and removes its associated broadcast receivers and services. |
| **Deactivate Firebase Performance Monitoring** | Deactivates the collection of performance data on app start up time, network requests, and other related metrics. |
| **Disable Google Safe Browsing in WebView** | Disables the Google Safe Browsing checks in WebView. This doesn't apply to WebView within the SDK Runtime. |
| **Disable metrics collection in WebView** | Disables the collection of diagnostic data or usage statistics that are sent to Google. |
| **Disable mobile ads** | Disables banner, interstitial, and other ad formats.<br><sub>Options: AppLovin MAX, BIGO, Google AdMob, +8 more</sub> |
| **Remove internet permission** | Removes unnecessary internet permission from apps that can still work without internet access. |
| **Remove screenshot detection** | Prevents the app from detecting when a screenshot is taken. |
| **Replace Google Maps API key** | Replaces the Google Maps API key with a custom key.<br><sub>Options: Maps API Key</sub> |
| **Spoof Advertising ID** | Spoofs the device's advertising ID with a string of zeros. |
| **Spoof Firebase certificate hash** | Spoofs the app's package certificate hash used by Firebase Installations so that push notifications, remote config, and other Firebase services continue to work as expected.<br><sub>Options: Certificate hash</sub> |
| **Spoof signature verification** | Spoofs the signature verification when the app starts up. It is recommended to use the unmodified app to work properly.<br><sub>Options: Package name, Base64-encoded signature</sub> |
