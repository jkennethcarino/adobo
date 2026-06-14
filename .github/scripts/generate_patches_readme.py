#!/usr/bin/env python3
"""
Generate patches documentation section from patches-list.json and inject into README.md.

This script parses patches-list.json and generates a formatted Markdown section
containing patch information grouped by compatible packages. The generated content
is then injected into README.md between <!-- PATCHES_START --> and <!-- PATCHES_END --> markers.

Spoiler sections (containing patch details) are expanded by default if:
  1. Total patch count is small (≤ AUTO_EXPAND_THRESHOLD).
  2. The README marker explicitly requests it: <!-- PATCHES_START EXPANDED -->.

Usage:
    python3 generate_patches_readme.py <owner/repo> <branch> [patches-list.json] [README.md]

Args:
    owner/repo: GitHub repository in format "owner/repo" (e.g., "MorpheApp/morphe-patches").
    branch: Git branch name for release links.
    patches-list.json: Path to patches JSON file (default: patches-list.json).
    README.md: Path to README file for injection (default: README.md).
"""

import json
import re
import sys
from pathlib import Path

# Threshold for auto-expanding spoilers (small count = expanded by default).
AUTO_EXPAND_THRESHOLD = 20

# Define regex patterns for README markers (supports optional EXPANDED flag).
START_PATTERN = r"<!-- PATCHES_START(?:\s+EXPANDED)?\s*-->"
END_MARKER = "<!-- PATCHES_END -->"


def pkg_emoji(pkg):
    """Return a standard package emoji regardless of the package name."""
    return "📦"


def patches_table(patches):
    """Generate a Markdown table of patches with metadata."""
    rows = [
        "| Patch Name | Description | Options |",
        "|------------|-------------|---------|",
    ]
    for p in sorted(patches, key=lambda x: x["name"]):
        options = p.get("options") or []

        if options:
            option_titles = [opt.get("title") or opt.get("key") or "" for opt in options]
            opts_cell = "<br>".join(f"• {title}" for title in option_titles)
        else:
            opts_cell = ""

        desc = (p.get("description") or "").replace("\n", "<br>")
        rows.append(f"| {p['name']} | {desc} | {opts_cell} |")

    return "\n".join(rows)


def versions_table(targets):
    """
    Generate a Markdown table of supported app versions.

    Experimental versions are prefixed with a 🧪 emoji. If a target has a
    description, it appears in a secondary row.

    Targets with None versions are skipped. Returns empty string if no
    valid versions are found (allowing the caller to omit the section).
    """
    if not targets:
        return ""

    cells = []
    for t in targets:
        version = t.get("version")
        if version is None:
            continue

        label = f"🧪&nbsp;{version}" if t.get("isExperimental") else version
        cells.append(label)

    # Abort if all versions were None.
    if not any(cells):
        return ""

    header = "| " + " | ".join(cells) + " |"
    separator = "| " + " | ".join(":---:" for _ in cells) + " |"
    rows = [header, separator]

    # Include description row if any target has one.
    descriptions = [(t.get("description") or "").replace("\n", "<br>") for t in targets]
    if any(descriptions):
        rows.append("| " + " | ".join(descriptions) + " |")

    return "\n".join(rows)


def spoiler(label, count, pkg_name, targets, tbl, expanded=False):
    """
    Generate a collapsible HTML details section with patches and versions.

    Creates a <details> element containing a patch table and optional versions
    table.

    Args:
        label: Summary text in the spoiler header (e.g., "📦 App Name").
        count: Number of patches in this group (for header text).
        pkg_name: Package name to display (for non-universal patches).
        targets: Version targets for the versions table.
        tbl: Pre-rendered Markdown table of patches.
        expanded: If True, details opens by default.

    Returns:
        str: HTML details element.
    """
    noun = "patch" if count == 1 else "patches"
    pkg_section = f"**Package name:** `{pkg_name}`\n\n" if pkg_name else ""
    versions_content = versions_table(targets)
    versions_section = f"**Supported versions:**\n\n{versions_content}\n\n" if versions_content else ""
    details_tag = "<details open>" if expanded else "<details>"

    return f"""{details_tag}
<summary>{label}&nbsp;&nbsp;•&nbsp;&nbsp;{count} {noun}</summary>
<br>

{pkg_section}{versions_section}{tbl}

</details>"""


def github_raw_url(owner, repo, branch):
    """Construct the URL to patches-list.json on GitHub raw content."""
    return f"https%3A%2F%2Fraw.githubusercontent.com%2F{owner}%2F{repo}%2Frefs%2Fheads%2F{branch}%2Fpatches-list.json"


def build_content(by_pkg, universal, owner, repo, branch, expanded=False):
    """Build the complete patches section for README."""
    patches_url = github_raw_url(owner, repo, branch)
    lines = [
        f"[![Branch](https://img.shields.io/badge/dynamic/json?url={patches_url}&query=version&label={branch})][releases]"
        f" ![Patches](https://img.shields.io/badge/dynamic/json?url={patches_url}&query=%24.patches.length&style=social&label=Patches)"
    ]

    for pkg, entry in by_pkg.items():
        patches = list(entry["patches"].values())
        label = f"{entry['emoji']} {entry['name']}"
        lines.append(
            spoiler(label, len(patches), pkg, entry["targets"], patches_table(patches), expanded)
        )
        lines.append("")

    if universal:
        uni_patches = list(universal.values())
        noun = "patch" if len(uni_patches) == 1 else "patches"
        details_tag = "<details open>" if expanded else "<details>"
        lines.append(f"""{details_tag}
<summary>🌐 Universal&nbsp;&nbsp;•&nbsp;&nbsp;{len(uni_patches)} {noun}</summary>
<br>

{patches_table(uni_patches)}

</details>""")
        lines.append("")

    return "\n".join(lines)


def main():
    if len(sys.argv) < 3:
        print("Usage: generate_patches_readme.py <owner/repo> <branch> [json] [readme]")
        sys.exit(1)

    repo_full = sys.argv[1]
    branch = sys.argv[2]
    json_path = Path(sys.argv[3]) if len(sys.argv) > 3 else Path("patches-list.json")
    readme_path = Path(sys.argv[4]) if len(sys.argv) > 4 else Path("README.md")

    if "/" not in repo_full:
        raise ValueError(f"Invalid repo format: {repo_full} (expected owner/repo)")

    owner, repo = repo_full.split("/", 1)

    with open(json_path, encoding="utf-8") as f:
        data = json.load(f)

    # Parse patches and group by compatible package.
    # JSON structure: compatiblePackages is a list of objects with:
    #   { packageName, name, targets: [{ version, isExperimental, description }] }
    # Patches without compatiblePackages are universal (apply to all packages).
    by_pkg = {}
    universal = {}

    for patch in data["patches"]:
        cp = patch.get("compatiblePackages")
        if not cp:
            # Deduplicate universal patches by name
            if patch["name"] not in universal:
                universal[patch["name"]] = patch
            continue

        # Group patches by their compatible packages.
        for pkg_entry in cp:
            pkg = pkg_entry["packageName"]
            pkg_label = pkg_entry.get("name") or pkg

            # Initialize package group on first encounter.
            if pkg not in by_pkg:
                by_pkg[pkg] = {
                    "name": pkg_label,
                    "emoji": pkg_emoji(pkg),
                    "patches": {},
                    "targets": pkg_entry.get("targets", []),
                }

            # Avoid duplicate patches if they span multiple compatible packages.
            if patch["name"] not in by_pkg[pkg]["patches"]:
                by_pkg[pkg]["patches"][patch["name"]] = patch

    # Build content and prepare for injection into README.
    raw_ver = data["version"]
    # Strip leading "v" if present (e.g., "v1.0.0" → "1.0.0").
    ver = raw_ver.lstrip("v")
    # Calculate total patch count across all packages and universal patches.
    total = sum(len(e["patches"]) for e in by_pkg.values()) + len(universal)

    readme = readme_path.read_text(encoding="utf-8")
    marker_match = re.search(START_PATTERN, readme)

    # Validate that README contains required injection markers.
    if not marker_match or END_MARKER not in readme:
        # Fallback: print to stdout for CI to capture (file may not have markers).
        print(build_content(by_pkg, universal, owner, repo, branch, expanded=False))
        sys.stderr.write(
            f"⚠️  Markers <!-- PATCHES_START [EXPANDED] --> / {END_MARKER} not found in {readme_path}. "
            "Printed to stdout instead.\n"
        )
        sys.exit(1)

    actual_start = marker_match.group(0)

    # Determine whether to expand spoilers based on patch count and marker.
    # Spoilers are expanded if:
    #   1. Total patch count is small (≤ AUTO_EXPAND_THRESHOLD), or
    #   2. README marker explicitly requests: <!-- PATCHES_START EXPANDED -->
    expanded = total <= AUTO_EXPAND_THRESHOLD or "EXPANDED" in actual_start

    # Generate patches section.
    generated = build_content(by_pkg, universal, owner, repo, branch, expanded)

    # Replace old patches section with new content.
    new_readme = re.sub(
        rf"{START_PATTERN}.*?{re.escape(END_MARKER)}",
        f"{actual_start}\n{generated}\n{END_MARKER}",
        readme,
        flags=re.DOTALL,
    )

    readme_path.write_text(new_readme, encoding="utf-8")
    print(f"✅ Injected patches section into {readme_path} (v{ver}, branch={branch}, {total} patches, expanded={expanded})")


if __name__ == "__main__":
    main()
