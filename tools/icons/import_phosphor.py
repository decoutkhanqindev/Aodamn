#!/usr/bin/env python3
"""Tải icon Phosphor (MIT) đã ghim phiên bản và đổi sang VectorDrawable trong res/drawable.

Chạy ở gốc repo:  python -I tools/icons/import_phosphor.py
Danh sách icon nằm ở tools/icons/icons.txt. File sinh ra có dòng đầu "Sinh bằng", đừng sửa tay: sửa danh sách rồi chạy lại.
"""
import argparse
import re
import sys
import xml.etree.ElementTree as ET
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path
from urllib.request import urlopen

VERSION = "2.1.1"
URL = "https://cdn.jsdelivr.net/npm/@phosphor-icons/core@{version}/assets/{weight}/{name}{suffix}.svg"
MARKER = "Sinh bằng tools/icons/import_phosphor.py"
VIEWPORT = 256
# Icon có hướng: tự lật khi giao diện đọc từ phải sang trái (tiếng Ả Rập, Do Thái).
MIRRORED = {"arrow-left", "arrow-right", "caret-right", "arrow-u-up-left", "arrow-u-up-right", "skip-back", "skip-forward"}


def parse_manifest(path):
    entries = []
    for raw in path.read_text(encoding="utf-8").splitlines():
        line = raw.split("#", 1)[0].strip()
        if not line:
            continue
        weight, *names = line.replace(":", " ").replace(",", " ").split()
        for name in names:
            entries.append((weight, name))
    return entries


def resource_name(weight, name):
    base = "ic_" + name.replace("-", "_")
    return base if weight == "regular" else f"{base}_{weight}"


def fetch(entry):
    weight, name = entry
    suffix = "" if weight == "regular" else f"-{weight}"
    url = URL.format(version=VERSION, weight=weight, name=name, suffix=suffix)
    with urlopen(url, timeout=30) as response:
        return entry, response.read().decode("utf-8")


def to_vector(svg_text, mirrored):
    root = ET.fromstring(svg_text)
    view_box = root.get("viewBox", "").split()
    if [float(v) for v in view_box] != [0, 0, VIEWPORT, VIEWPORT]:
        raise ValueError(f"viewBox lạ: {view_box}")
    paths = []
    for element in root.iter():
        tag = element.tag.rsplit("}", 1)[-1]
        if tag == "svg":
            continue
        if tag != "path":
            raise ValueError(f"chưa hỗ trợ thẻ <{tag}>")
        alpha = element.get("opacity")
        paths.append((element.get("d"), alpha))
    lines = [
        '<?xml version="1.0" encoding="utf-8"?><!-- ' + MARKER + " (Phosphor Icons " + VERSION + ", MIT). -->",
        '<vector xmlns:android="http://schemas.android.com/apk/res/android"',
        '    android:width="24dp"',
        '    android:height="24dp"',
        f'    android:viewportWidth="{VIEWPORT}"',
        f'    android:viewportHeight="{VIEWPORT}"' + ('\n    android:autoMirrored="true">' if mirrored else ">"),
    ]
    for d, alpha in paths:
        lines.append("    <path")
        lines.append('        android:fillColor="#FF000000"')
        if alpha:
            lines.append(f'        android:fillAlpha="{alpha}"')
        lines.append(f'        android:pathData="{d}" />')
    lines.append("</vector>")
    return "\n".join(lines) + "\n"


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--manifest", default="tools/icons/icons.txt")
    parser.add_argument("--out", default="app/src/main/res/drawable")
    args = parser.parse_args()

    entries = parse_manifest(Path(args.manifest))
    out = Path(args.out)
    wanted = {resource_name(w, n) + ".xml" for w, n in entries}

    failed = []
    with ThreadPoolExecutor(max_workers=8) as pool:
        for future in [pool.submit(fetch, e) for e in entries]:
            try:
                (weight, name), svg = future.result()
            except Exception as e:  # noqa: BLE001 - báo hết lỗi mạng / 404 một lượt
                failed.append(str(e))
                continue
            (out / (resource_name(weight, name) + ".xml")).write_text(
                to_vector(svg, name in MIRRORED), encoding="utf-8", newline="\n"
            )

    for stale in out.glob("ic_*.xml"):
        if stale.name not in wanted and MARKER in stale.read_text(encoding="utf-8"):
            stale.unlink()
            print("xoá", stale.name)

    if failed:
        print("LỖI:", *failed, sep="\n  ")
        sys.exit(1)
    print(f"OK {len(entries)} icon -> {out}")


if __name__ == "__main__":
    main()
