# Adobo

[![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/jkennethcarino/adobo/release.yml)][gh-actions]
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)][gpl-3.0]
![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)

This repository contains patches for [Morphe](https://morphe.software/), an open-source Android
patching tool for modifying apps like YouTube and Reddit to block ads and add new features.

<a href="https://morphe.software/add-source?github=jkennethcarino/adobo">
  <img src=".github/assets/add-to-morphe.svg" alt="Add to Morphe" height="48" />
</a>

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
> **[v2.0.1](https://github.com/jkennethcarino/adobo/releases/tag/v2.0.1)**&nbsp;&nbsp;&middot;&nbsp;&nbsp;`main`&nbsp;&nbsp;&middot;&nbsp;&nbsp;**55 patches** across **4 apps**&nbsp;&nbsp;&middot;&nbsp;&nbsp;[Full details](PATCHES.md)

| App | Package name | Supported versions | Patches |
|---|---|---|---|
| [**9GAG**](PATCHES.md#9gag) | [`com.ninegag.android.app`](https://play.google.com/store/apps/details?id=com.ninegag.android.app) | `8.30.04`, `8.29.08`, `8.26.1`, `8.25.1`, `8.24.8` | 3 |
| [**Gboard**](PATCHES.md#gboard) | [`com.google.android.inputmethod.latin`](https://play.google.com/store/apps/details?id=com.google.android.inputmethod.latin) | `Any` | 10 |
| [**IMDb**](PATCHES.md#imdb) | [`com.imdb.mobile`](https://play.google.com/store/apps/details?id=com.imdb.mobile) | `Any` | 1 |
| [**Reddit**](PATCHES.md#reddit) | [`com.reddit.frontpage`](https://play.google.com/store/apps/details?id=com.reddit.frontpage) | `2026.47.0`, `2026.46.0`, `2026.45.0`, `2026.44.0`, `2026.24.0` | 28 |
| [**Universal**](PATCHES.md#universal) | - | `Any` | 13 |
<!-- PATCHES_END -->

## Getting Started

You'll need [Morphe Manager](https://github.com/MorpheApp/morphe-manager)
or [Morphe Desktop](https://github.com/MorpheApp/morphe-desktop).

### Morphe Manager

#### Option A: One-click import

Tap [this link](https://morphe.software/add-source?github=jkennethcarino/adobo) to import Adobong
Morphe patches directly into Morphe Manager in just a click!

#### Option B: Manual import

1. Open the **Morphe Manager** app.
2. Switch to the **Sources** tab, then tap the **+** icon in the top-right corner.
3. On the **Add patch source** bottom sheet, copy and paste the following to the _Source URL_ text
   field:
    ```
    https://github.com/jkennethcarino/adobo
    ```
4. Optional: Toggle on _Pre-release patches_ for early access to new features.
5. Lastly, tap **Add** to import this patch source.

> [!NOTE]
> **For advanced users:** Enable **Expert mode** to choose which patches to apply and configure their
options, instead of using the default selections.

<details>
  <summary><h3>Universal Patch Manager</h3></summary>
  <ol>
    <li>Open the <strong><a href="https://github.com/Jman-Github/Universal-Patch-Manager">UP Manager</a></strong> app.</li>
    <li>Switch to the <strong>Patch Bundles</strong> tab.</li>
    <li>Tap the <em>globe</em> icon in the bottom-right corner.</li>
    <li>On the <strong>Discovery</strong> screen, search for <code>Adobo</code> in the <strong>Search bundles</strong> text field.</li>
    <li>In the search results, tap the <strong>Import</strong> button for the <code>jkennethcarino/adobo</code> patch bundle.</li>
  </ol>
</details>

## Verifying the Patch Bundle

The Adobo patch bundle on GitHub is signed with
a [Morphe signing key](https://github.com/MorpheApp/morphe-documentation/blob/533356050d990de5d681888ee79d376b0b6b02b1/docs/morphe-development/2_signing.md)
that has the following fingerprint:

```
9fc4 b658 48b7 093a
```

To check it, open the **Morphe Manager** app, switch to the **Patches** tab,
then expand the **Adobo** patch source. Verify that the **Signed by** value shown there matches the
fingerprint above (tapping on it copies
the [public key](https://github.com/jkennethcarino/adobo/blob/6ab6865fe7a3fc9306afa19e3cb42718442ffc38/morphe-signing.pub)).

## Building

You can follow the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation) to
build these patches.

## Contributing

I'm not accepting pull requests. If you want to make changes, feel free to fork this repository and
maintain your own version.

I do welcome feature requests and bug reports. I won't implement patches that bypass payments or
premium features; the only exception is removing ads, annoyances, and analytics. Since I work on
this in my free time, I can't commit to implementing patches or fixes quickly. Open an
[issue](https://github.com/jkennethcarino/adobo/issues) here to report a bug or suggest a feature.

## Disclaimer

> [!WARNING]
> These patches are provided as-is for personal use.
>
> Use them at your own risk. I'm not responsible for any potential issues, including violations of
> terms of service that can lead to account bans or other consequences.

## License

Adobo is licensed under the [GNU General Public License v3.0 (GPL-3.0)][gpl-3.0].
See [LICENSE](LICENSE) for details.

[gh-actions]: https://github.com/jkennethcarino/adobo/actions
[releases]: https://github.com/jkennethcarino/adobo/releases
[gpl-3.0]: https://www.gnu.org/licenses/gpl-3.0.html
