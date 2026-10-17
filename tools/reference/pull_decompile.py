#!/usr/bin/env python3
"""Kéo APK của app mẫu đang cài trên máy, dịch ngược và lập chỉ mục để tìm các chủ đề AOD cần clone.

Chạy ở gốc repo:  python -I -X utf8 tools/reference/pull_decompile.py <package> [--serial SERIAL] [--out THƯ_MỤC]

Cần REA (`rea` trên PATH), JDK 17+ (mặc định JBR của Android Studio), jar jadx-headless-mcp 0.7.1 (biến REA_JADX_MCP_JAR
hoặc ~/.local/share/rea/android/), adb và aapt2 của Android SDK; cài đặt ở tools/reference/README.md.
Thư mục ra mặc định ~/.local/share/rea/work/<package>/ (ngoài repo vì là mã của người khác):
  apk/<package>/*.apk   bộ APK (base + split) kéo bằng REA
  src/sources/          mã nguồn dịch ngược (JADX, không kèm resource)
  res-<apk>.txt         bảng resource (aapt2 dump resources) của từng APK
  badging.txt           tên gói, phiên bản, SDK
  index.md              tóm tắt để đọc trước: class nghi là chủ đề / đồng hồ, file tra cứu, font, layout, nhóm chuỗi
Bước nào đã có kết quả thì bỏ qua (APK, mã nguồn); muốn làm lại thì đổi --out.
"""
import argparse
import os
import re
import shutil
import subprocess
import sys
from collections import Counter
from pathlib import Path

HERE = Path(__file__).resolve().parent
KEYWORDS = ("aod", "clock", "theme", "style", "screen", "face", "watch", "dial", "widget", "design", "template",
            "preset", "skin", "wallpaper", "digit")
LIBRARY_DIRS = ("android", "androidx", "com/google", "kotlin", "kotlinx", "okhttp3", "okio", "org", "j$", "io",
                "com/facebook", "com/airbnb", "dagger", "javax", "retrofit2", "coil", "com/bumptech")
LIBRARY_RES = ("abc_", "mtrl_", "material_", "design_", "m3_", "notification_", "select_dialog_", "support_", "browser_",
               "exo_", "admob_", "gnt_", "com_google_", "common_", "fui_", "ump_")


def fail(message):
    sys.exit("Lỗi: " + message)


def run(cmd, stdout=subprocess.DEVNULL):
    print("$", " ".join(str(c) for c in cmd), flush=True)
    result = subprocess.run([str(c) for c in cmd], stdout=stdout, stderr=subprocess.PIPE, text=True, errors="replace")
    if result.returncode != 0:
        fail("lệnh trên lỗi (%d):\n%s" % (result.returncode, "\n".join(result.stderr.splitlines()[-15:])))


def sdk_dir():
    for key in ("ANDROID_HOME", "ANDROID_SDK_ROOT"):
        if os.environ.get(key):
            return Path(os.environ[key])
    return Path(os.environ.get("LOCALAPPDATA", Path.home())) / "Android" / "Sdk"


def find_adb():
    adb = shutil.which("adb") or next(iter(sdk_dir().glob("platform-tools/adb*")), None)
    return adb or fail("không thấy adb (Android SDK platform-tools)")


def find_aapt2():
    tools = sorted(sdk_dir().glob("build-tools/*/aapt2*"), key=lambda p: [int(x) for x in re.findall(r"\d+", p.parent.name)])
    return tools[-1] if tools else fail("không thấy aapt2 trong Android SDK build-tools")


def find_java():
    homes = [os.environ.get("JAVA_HOME"), r"C:\Program Files\Android\Android Studio\jbr"]
    for home in filter(None, homes):
        for name in ("java.exe", "java"):
            if (Path(home) / "bin" / name).exists():
                return Path(home) / "bin" / name
    return shutil.which("java") or fail("không thấy java (JDK 17+); đặt JAVA_HOME")


def find_jadx_jar():
    jar = Path(os.environ.get("REA_JADX_MCP_JAR") or Path.home() / ".local/share/rea/android/jadx-headless-mcp-0.7.1-all.jar")
    return jar if jar.exists() else fail("không thấy jar JADX %s (xem tools/reference/README.md)" % jar)


def rea_command():
    rea = shutil.which("rea") or fail("không thấy REA: npm install -g rea-agents --ignore-scripts")
    mjs = Path(rea).resolve()
    if mjs.suffix != ".mjs":
        mjs = Path(rea).parent / "node_modules" / "rea-agents" / "scripts" / "rea.mjs"
    return [shutil.which("node"), mjs] if mjs.exists() else [rea]


def pick_serial(adb, serial):
    if serial:
        return serial
    out = subprocess.run([str(adb), "devices"], capture_output=True, text=True, check=True).stdout
    devices = [line.split()[0] for line in out.splitlines()[1:] if line.strip().endswith("device")]
    if len(devices) != 1:
        fail("cần đúng một máy đang kết nối (đang có %d); chọn bằng --serial" % len(devices))
    return devices[0]


def pull(package, serial, out):
    apk_dir = out / "apk"
    if not apk_dir.exists():
        run(rea_command() + ["pull-adb-package", serial, package, "--output-directory", apk_dir])
    apks = sorted(apk_dir.rglob("*.apk"))
    base = next((a for a in apks if a.name == "base.apk"), apks[0] if apks else None)
    return apks, base or fail("REA không kéo được APK nào của %s" % package)


def decompile(base, out):
    sources = out / "src" / "sources"
    if not sources.exists():
        run([find_java(), "-Xmx6g", "-cp", find_jadx_jar(), HERE / "JadxExport.java", base, out / "src"])
    return sources


def dump_resources(apks, base, out):
    aapt2 = find_aapt2()
    for apk in apks:
        target = out / ("res-%s.txt" % apk.stem)
        if target.exists():
            continue
        result = subprocess.run([str(aapt2), "dump", "resources", str(apk)], capture_output=True, text=True,
                                encoding="utf-8", errors="replace")
        if result.returncode == 0:
            target.write_text(result.stdout, encoding="utf-8")
        else:
            print("Bỏ qua %s: không có bảng resource (split chỉ chứa thư viện native)" % apk.name)
    with open(out / "badging.txt", "w", encoding="utf-8") as f:
        run([aapt2, "dump", "badging", base], stdout=f)


def resource_names(dump):
    return re.findall(r"^\s+resource 0x[0-9a-f]+ (\w+)/(\S+)$", dump, re.M)


def build_index(package, out, sources):
    badging = (out / "badging.txt").read_text(encoding="utf-8", errors="replace")
    version = re.search(r"versionName='([^']*)'", badging)
    app_dir = sources / package.replace(".", "/")
    app_classes = sorted(p.relative_to(app_dir).with_suffix("").as_posix().replace("/", ".") for p in app_dir.rglob("*.java"))
    by_package = Counter(c.rsplit(".", 1)[0] if "." in c else "(gốc)" for c in app_classes)
    hits = [c for c in app_classes if any(k in c.lower() for k in KEYWORDS)]
    hit_names = {c.rsplit(".", 1)[-1] for c in hits}

    lookup = []
    for f in sources.rglob("*.java"):
        rel = f.relative_to(sources).as_posix()
        if rel.startswith(LIBRARY_DIRS):
            continue
        text = f.read_text(encoding="utf-8", errors="replace")
        n = len({m for m in re.findall(r"^import [\w.]+\.(\w+);", text, re.M) if m in hit_names})
        if n >= 5:
            lookup.append((n, rel))
    lookup.sort(reverse=True)

    res = resource_names((out / "res-base.txt").read_text(encoding="utf-8", errors="replace"))
    fonts = sorted({n for t, n in res if t == "font"})
    layouts = sorted({n for t, n in res if t == "layout" and not n.startswith(LIBRARY_RES) and any(k in n for k in KEYWORDS)})
    raws = sorted({n for t, n in res if t == "raw"})
    prefixes = Counter(n.split("_")[0] for t, n in res if t == "string").most_common(15)

    lines = ["# %s %s" % (package, version.group(1) if version else ""), "",
             "Chỉ mục để tìm mô hình chủ đề; đọc rồi viết bộ trích theo mẫu tools/reference/extract_muviz.py.", "",
             "## Package của app (%d class)" % len(app_classes), ""]
    lines += ["- `%s`: %d" % (p, n) for p, n in sorted(by_package.items())]
    lines += ["", "## Class nghi là chủ đề / đồng hồ (%d)" % len(hits), ""]
    lines += ["- `%s`" % c for c in hits]
    lines += ["", "## File tra cứu (import ≥ 5 class ở trên: thường là bảng mã chủ đề → class, mã → nhãn)", ""]
    lines += ["- `%s`: %d" % (rel, n) for n, rel in lookup[:15]] or ["- (không có)"]
    lines += ["", "## Font (%d)" % len(fonts), "", ", ".join(fonts) or "(không có)",
              "", "## Layout khớp từ khoá (%d)" % len(layouts), "", ", ".join(layouts) or "(không có)",
              "", "## Raw (Lottie, âm thanh)", "", ", ".join(raws) or "(không có)",
              "", "## Tiền tố tên chuỗi hay gặp", ""]
    lines += ["- `%s_`: %d" % (p, n) for p, n in prefixes]
    (out / "index.md").write_text("\n".join(lines) + "\n", encoding="utf-8")


def main():
    parser = argparse.ArgumentParser(description="Kéo APK app mẫu, dịch ngược và lập chỉ mục.")
    parser.add_argument("package")
    parser.add_argument("--serial")
    parser.add_argument("--out", type=Path)
    args = parser.parse_args()
    out = (args.out or Path.home() / ".local/share/rea/work" / args.package).resolve()
    repo = HERE.parents[1]
    if out == repo or repo in out.parents:
        fail("đừng để APK và mã dịch ngược trong repo; chọn --out ngoài repo")
    out.mkdir(parents=True, exist_ok=True)
    apks, base = pull(args.package, pick_serial(find_adb(), args.serial), out)
    sources = decompile(base, out)
    dump_resources(apks, base, out)
    build_index(args.package, out, sources)
    print("Xong:", out / "index.md")


main()
