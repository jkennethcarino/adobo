# Adobo

This repository contains a collection of patches for [Morphe](https://morphe.software/), an
open-source Android patching tool for modifying apps like YouTube and Reddit to block ads and add
new features.

## Features

Some of the features included in Adobong Morphe patches are:

- **Block ads, trackers, and analytics**: Enjoy a distraction-free experience by removing unwanted
  ads and data collection in your favorite apps and games.
- **Remove internet permission**: Remove unnecessary internet permission from apps that work fully
  offline.
- **Disable WebView metrics collection**: Prevent the collection of diagnostic data or usage
  statistics that are sent to Google.
- **Spoof signature verification**: Bypass signature checks in apps that require them, allowing you
  to use modified versions without issues.
- **Always-incognito mode for Gboard**: Make Gboard always open in incognito mode to disable typing
  history collection and personalization.
- Everything in
  the [Privacy ReVanced patches](https://github.com/jkennethcarino/privacy-revanced-patches),
  and much more!

## Getting Started

You can use [Morphe CLI](https://github.com/MorpheApp/morphe-cli)
or [Morphe Manager](https://github.com/MorpheApp/morphe-manager) to use Adobong Morphe patches.

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

To build Adobong Morphe patches, you can follow
the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation).

## Disclaimer

These patches are provided as-is for personal use.

Use them at your own risk. The author is not responsible for any potential issues, including app
instability, crashes, or violations of terms of service that can lead to account bans or other
consequences.

## License

Adobo is licensed under
the [GNU General Public License v3.0 (GPL-3.0)](https://www.gnu.org/licenses/gpl.html).
See the [LICENSE](LICENSE) file for more details.
