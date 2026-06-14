# Adobo

[![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/jkennethcarino/adobo/release.yml)][gh-actions]
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)][gpl-3.0]
![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)

This repository contains patches for [Morphe](https://morphe.software/), an open-source Android
patching tool for modifying apps like YouTube and Reddit to block ads and add new features.

[![Import](https://img.shields.io/badge/Import-blue?style=for-the-badge&label=Morphe%20Manager)][import-adobo]

## Features

- Block ads, trackers, and analytics from your favorite apps and games
- Remove internet permission from apps and games that don't need it
- Disable WebView metrics collection sent to Google
- Bypass signature verification checks
- Force Gboard into incognito mode to disable typing history and personalization
- Everything in the [Privacy ReVanced patches](https://github.com/jkennethcarino/privacy-revanced-patches),
  and much more!

## Patches

<!-- PATCHES_START -->
[![Branch](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fraw.githubusercontent.com%2Fjkennethcarino%2Fadobo%2Frefs%2Fheads%2Fmain%2Fpatches-list.json&query=version&label=main)][releases] ![Patches](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fraw.githubusercontent.com%2Fjkennethcarino%2Fadobo%2Frefs%2Fheads%2Fmain%2Fpatches-list.json&query=%24.patches.length&style=social&label=Patches)
<details>
<summary>📦 Gboard&nbsp;&nbsp;•&nbsp;&nbsp;7 patches</summary>
<br>

**Package name:** `com.google.android.inputmethod.latin`

| Patch Name | Description | Options |
|------------|-------------|---------|
| Always-incognito mode | Always opens Gboard in incognito mode to disable typing history collection and personalization. |  |
| Enable OCR feature | Enables OCR feature to extract text from images and insert it into text fields. |  |
| Enable Undo feature | Enables undo feature to quickly undo or correct typing mistakes. |  |
| Enable clipboard in incognito | Enables clipboard support in incognito mode. |  |
| Enable key shape selection | Enables an option to select a key shape from the theme. |  |
| Enable voice typing in incognito | Enables voice typing in incognito mode. |  |
| Toggle feature flags | Toggles Gboard feature flags to enable or disable experimental or hidden features. | • Feature flags<br>• Enable feature flags |

</details>

<details>
<summary>📦 Reddit&nbsp;&nbsp;•&nbsp;&nbsp;17 patches</summary>
<br>

**Package name:** `com.reddit.frontpage`

| Patch Name | Description | Options |
|------------|-------------|---------|
| Colorize comment indent lines | Replaces the default gray comment indent lines with color-coded lines. | • Line color 1<br>• Line color 2<br>• Line color 3<br>• Line color 4<br>• Line color 5<br>• Line color 6<br>• Line color 7<br>• Line color 8 |
| Disable home feed swipe | Disables the horizontal page swipe gesture used to switch feeds. |  |
| Disable home screen redirect | Disables the automatic redirect to the home screen after being away from the app or returning from the background. |  |
| Disable post detail swipe | Disables the horizontal swipe gesture used to navigate between posts. |  |
| Disable screenshot banner | Disables the banner that shows up after taking a screenshot. |  |
| Hide Ask button from search bar | Hides the Ask button (Reddit Answers) from the search bar. |  |
| Hide awards | Hides the awards on Reddit posts and comments. |  |
| Hide community highlights | Hides the community highlights section. |  |
| Hide post view counts | Removes the "views" indicator from the post metadata. |  |
| Hide prominent search bar | Hides the new prominent search bar on the main screen. |  |
| Hide share count | Hides the share count on Reddit posts. |  |
| Hide upvote scores | Hides the scores on Reddit posts and comments. | • Hide post scores<br>• Hide comment scores |
| Hide user community badges | Hides the user community badges on Reddit comments. |  |
| Hide user flairs | Hides the user flairs on Reddit comments. |  |
| Open external links directly | Opens external links directly without going through out.reddit.com. |  |
| Remove ads and telemetry | Removes ads and telemetry everywhere. |  |
| Sanitize share links | Unshortens and removes the tracking query parameters from shared links. |  |

</details>

<details>
<summary>📦 9GAG&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**Package name:** `com.ninegag.android.app`

**Supported versions:**

| 8.17.5 | 8.17.4 |
| :---: | :---: |

| Patch Name | Description | Options |
|------------|-------------|---------|
| Remove 9GAG's ads, trackers, and analytics | Removes ads, trackers, and analytics in the 9GAG app. |  |

</details>

<details>
<summary>📦 IMDb&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**Package name:** `com.imdb.mobile`

| Patch Name | Description | Options |
|------------|-------------|---------|
| Remove IMDb's ads, trackers, and analytics | Removes ads, trackers, and analytics in the IMDb app. |  |

</details>

<details>
<summary>🌐 Universal&nbsp;&nbsp;•&nbsp;&nbsp;11 patches</summary>
<br>

| Patch Name | Description | Options |
|------------|-------------|---------|
| Block ads, trackers, and analytics | Blocks ads, trackers, analytics, and unwanted content in apps and games using a hosts file. | • Hosts file<br>• Redirection IP<br>• Wildcard blocking |
| Change package name | Appends ".adobo" to the package name by default. Changing the package name of the app can lead to unexpected issues. | • Package name<br>• Update permissions<br>• Update other permissions<br>• Update content providers |
| Deactivate Firebase Analytics | Deactivates Firebase Analytics and removes its associated broadcast receivers and services. |  |
| Deactivate Firebase Performance Monitoring | Deactivates the collection of performance data on app start up time, network requests, and other related metrics. |  |
| Disable Google Safe Browsing in WebView | Disables the Google Safe Browsing checks in WebView. This doesn't apply to WebView within the SDK Runtime. |  |
| Disable metrics collection in WebView | Disables the collection of diagnostic data or usage statistics that are sent to Google. |  |
| Disable mobile ads | Disables banner, interstitial, and other ad formats. | • AppLovin MAX<br>• BIGO<br>• Google AdMob<br>• Meta Audience Network<br>• Mintegral<br>• myTarget<br>• Pangle<br>• TopOn<br>• Unity<br>• Liftoff Monetize<br>• Yandex Advertising Network |
| Remove internet permission | Removes unnecessary internet permission from apps that can still work without internet access. |  |
| Spoof Advertising ID | Spoofs the device's advertising ID with a string of zeros. |  |
| Spoof Firebase certificate hash | Spoofs the app's package certificate hash used by Firebase Installations so that push notifications, remote config, and other Firebase services continue to work as expected. | • Certificate hash |
| Spoof signature verification | Spoofs the signature verification when the app starts up. It is recommended to use the unmodified app to work properly. | • Package name<br>• Base64-encoded signature |

</details>

<!-- PATCHES_END -->

## Getting Started

You'll need [Morphe CLI](https://github.com/MorpheApp/morphe-cli)
or [Morphe Manager](https://github.com/MorpheApp/morphe-manager).

### Morphe Manager

#### Option A: One-click import (requires v1.11.0 or later)

Tap [this link](https://morphe.software/add-source?github=jkennethcarino/adobo) to import Adobong
Morphe patches directly into Morphe Manager in just a click!

#### Option B: Manual import

1. Open the **Morphe Manager** app.
2. On the main screen, tap the _folder_ icon in the bottom-left corner.
3. Tap the "**+**" icon next to **Patch Sources**.
4. On the **Add patch source** screen, select the **Remote** tab (selected by default).
5. Set the patch source URL to the following, then tap **Add**:

```
https://github.com/jkennethcarino/adobo
```

<details>
  <summary><h3>Universal ReVanced Manager</h3></summary>
  <ol>
    <li>Open the <strong><a href="https://github.com/Jman-Github/Universal-ReVanced-Manager">URV Manager</a></strong> app.</li>
    <li>Switch to the <strong>Patch Bundles</strong> tab.</li>
    <li>Tap the <em>globe</em> icon in the bottom-right corner.</li>
    <li>On the <strong>Discover patch bundles</strong> screen, search for "Adobo" in the <strong>Search by bundle name</strong> text field.</li>
    <li>In the search results, tap the <strong>Import</strong> button for the <code>jkennethcarino/adobo</code> patch bundle.</li>
  </ol>
</details>

## Building

You can follow the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation) to
build these patches.

## Contributing

I'm not accepting pull requests. If you want to make changes, feel free to fork this repository and
maintain your own version.

I do welcome feature requests and bug reports. I won't implement patches that bypass payments or
premium features; the only exception is removing ads, annoyances, and analytics. Since I work on
this in my spare time, I can't commit to implementing patches or fixes quickly. Open an
[issue](https://github.com/jkennethcarino/adobo/issues) here to report a bug or suggest a feature.

## Disclaimer

> [!WARNING]  
> These patches are provided as-is for personal use.
>
> Use them at your own risk. I'm not responsible for any potential issues, including app
instability, crashes, or violations of terms of service that can lead to account bans or other
consequences.

## License

Adobo is licensed under the [GNU General Public License v3.0 (GPL-3.0)][gpl-3.0].
See [LICENSE](LICENSE) for details.

[import-adobo]: https://morphe.software/add-source?github=jkennethcarino/adobo
[gh-actions]: https://github.com/jkennethcarino/adobo/actions
[releases]: https://github.com/jkennethcarino/adobo/releases
[gpl-3.0]: https://www.gnu.org/licenses/gpl-3.0.html
