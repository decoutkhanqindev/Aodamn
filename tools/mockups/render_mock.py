"""Dựng mock chủ đề AOD thành ảnh PNG dùng đúng file font trong repo, không cần mạng.

    python tools/mockups/render_mock.py w_<chủ đề> [--out THƯ_MỤC] [--width 932]

Chạy aod_landscape_mockups.py cho đúng một mục, thay mọi liên kết font ngoài (Google Fonts, jsdelivr) bằng @font-face base64 lấy từ
app/src/main/res/font và tools/fonts/static, kiểm không còn tài nguyên ngoài và không có font nào rơi về font mặc định, rồi chụp
bằng Edge headless và cắt ảnh dài thành các mảnh đọc được ở khoảng trống giữa các khung. Lỗi font thì dừng, không chụp.

Kết quả trong THƯ_MỤC (mặc định build/mock/<w_chủ đề>): page.html (tự chứa, mở được trong trình duyệt, còn chạy chuyển động) và
mock_01.png, mock_02.png… Hiện cho user bằng ![mock](đường dẫn tuyệt đối của từng png).
"""

import argparse
import base64
import re
import subprocess
import sys
import tempfile
import time
from pathlib import Path
from urllib.parse import parse_qsl, unquote_plus, urlsplit

from fontTools.ttLib import TTFont
from PIL import Image, ImageChops

ROOT = Path(__file__).resolve().parents[2]
FONT_DIRS = (ROOT / "app/src/main/res/font", ROOT / "tools/fonts/static")
GENERATOR = ROOT / "tools/mockups/aod_landscape_mockups.py"
BACKGROUND = (30, 30, 30)
PAGE_HEAD = ("<!doctype html><meta charset=utf-8><style>body{background:#1e1e1e;color:#ededed;margin:0;padding:16px;width:900px;"
             "font-family:'Plus Jakarta Sans',sans-serif;--text-secondary:#aaa}.sr-only{display:none}</style>")
GENERIC_FAMILIES = {"sans-serif", "serif", "monospace", "cursive", "fantasy", "system-ui", "ui-sans-serif", "inherit", "initial", "unset"}
EDGE_PATHS = (
    Path("C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe"),
    Path("C:/Program Files/Microsoft/Edge/Application/msedge.exe"),
)
LINK_PATTERN = re.compile(r'<link[^>]*href="(https://fonts\.googleapis\.com/css2\?[^"]+)"[^>]*>')
FACE_PATTERN = re.compile(r"@font-face\s*\{([^}]*)\}")
FAMILY_ITEM = r"""(?:'[^']*'|"[^"]*"|[A-Za-z][\w-]*(?: [\w-]+)*)"""
FAMILY_USE_PATTERN = re.compile(rf"""font-family\s*[:=]\s*"?({FAMILY_ITEM}(?:\s*,\s*{FAMILY_ITEM})*)""")
LEFTOVER_PATTERNS = (
    ("fonts.googleapis.com", re.compile(r"fonts\.googleapis\.com")),
    ("fonts.gstatic.com", re.compile(r"fonts\.gstatic\.com")),
    ("cdn.jsdelivr.net", re.compile(r"cdn\.jsdelivr\.net")),
    ("url(http…)", re.compile(r"url\(\s*['\"]?https?://")),
    ("@import", re.compile(r"@import")),
    ("<script src>", re.compile(r"<script[^>]+\bsrc=")),
    ("<link href>", re.compile(r"<link[^>]+\bhref=")),
)


def key_of(name):
    return re.sub(r"[^a-z0-9]", "", name.lower())


def scan_local_fonts():
    faces = {}
    for directory in FONT_DIRS:
        for path in sorted(directory.glob("*.ttf")):
            font = TTFont(path, lazy=True)
            if "fvar" in font:
                continue
            names = font["name"]
            family = names.getDebugName(16) or names.getDebugName(1)
            faces.setdefault(key_of(family), {}).setdefault(font["OS/2"].usWeightClass, path)
    return faces


def pick_face(faces, family, weight):
    key = key_of(family)
    candidates = [k for k in faces if k == key or key.startswith(k)]
    if not candidates:
        return None
    weights = faces[max(candidates, key=len)]
    nearest = min(weights, key=lambda w: (abs(w - weight), w))
    return nearest, weights[nearest]


def face_css(family, weight, path, cache):
    if path not in cache:
        cache[path] = base64.b64encode(path.read_bytes()).decode("ascii")
    return (f'@font-face{{font-family:"{family}";font-weight:{weight};'
            f'src:url(data:font/ttf;base64,{cache[path]}) format("truetype")}}')


def google_requests(href):
    requests = []
    for name, value in parse_qsl(urlsplit(href).query, keep_blank_values=True):
        if name != "family":
            continue
        family, _, spec = value.partition(":")
        weights = [int(w.split(",")[-1]) for w in spec.partition("@")[2].split(";") if w.split(",")[-1].isdigit()] or [400]
        requests.append((unquote_plus(family), sorted(set(weights))))
    return requests


def embed_fonts(html, faces):
    cache, embedded, missing, notes = {}, {}, [], []

    def pick(family, weight):
        found = pick_face(faces, family, weight)
        if found is None:
            missing.append(f"{family} {weight}")
            return None
        if found[0] != weight:
            notes.append(f"{family} {weight} không có trong repo, dùng file {found[0]}")
        return found[1]

    def add(family, weight):
        path = pick(family, weight)
        if path is None:
            return ""
        embedded.setdefault(family, set()).add(weight)
        return face_css(family, weight, path, cache)

    def replace_link(match):
        css = "".join(add(family, weight) for family, weights in google_requests(match.group(1)) for weight in weights)
        return f"<style>{css}</style>"

    def replace_face(match):
        body = match.group(1)
        if not re.search(r"url\(\s*['\"]?https?://", body):
            return match.group(0)
        family = re.search(r"font-family\s*:\s*['\"]?([^;'\"]+)", body)
        weight = re.search(r"font-weight\s*:\s*(\d+)", body)
        if not family:
            return match.group(0)
        name, value = family.group(1).strip(), int(weight.group(1)) if weight else 400
        path = pick(name, value)
        if path is None:
            return match.group(0)
        embedded.setdefault(name, set()).add(value)
        if path not in cache:
            cache[path] = base64.b64encode(path.read_bytes()).decode("ascii")
        src = f'src:url(data:font/ttf;base64,{cache[path]}) format("truetype")'
        return "@font-face{" + re.sub(r"src\s*:\s*url\([^)]*\)\s*(?:format\([^)]*\))?", lambda _: src, body) + "}"

    html = LINK_PATTERN.sub(replace_link, html)
    html = FACE_PATTERN.sub(replace_face, html)
    return html, embedded, missing, sorted(set(notes))


def problems_of(html, embedded, missing):
    problems = [f"không có file font trong repo cho: {name}" for name in sorted(set(missing))]
    for label, pattern in LEFTOVER_PATTERNS:
        if pattern.search(html):
            problems.append(f"còn tài nguyên ngoài: {label}")
    known = {key_of(name) for name in embedded}
    unknown = set()
    for match in FAMILY_USE_PATTERN.finditer(html):
        for item in match.group(1).split(","):
            name = item.strip().strip("'\"").strip()
            if name and name.lower() not in GENERIC_FAMILIES and key_of(name) not in known:
                unknown.add(name)
    problems += [f"font dùng trong mock nhưng không nhúng (chữ rơi về font mặc định): {name}" for name in sorted(unknown)]
    return problems


def find_edge():
    for path in EDGE_PATHS:
        if path.exists():
            return path
    sys.exit("Không thấy msedge.exe")


def screenshot(page, png, width, height):
    png.unlink(missing_ok=True)
    with tempfile.TemporaryDirectory() as profile:
        subprocess.run(
            [str(find_edge()), "--headless=new", "--disable-gpu", "--hide-scrollbars", "--no-first-run",
             f"--user-data-dir={profile}", f"--window-size={width},{height}", "--virtual-time-budget=4000",
             f"--screenshot={png}", page.as_uri()],
            check=False, timeout=180, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    deadline = time.time() + 30
    while not png.exists() and time.time() < deadline:
        time.sleep(0.5)
    if not png.exists():
        sys.exit("Edge không tạo ảnh chụp")
    return Image.open(png).convert("RGB")


def render_full(page, png, width):
    height = 8000
    while True:
        image = screenshot(page, png, width, height)
        content = ImageChops.difference(image, Image.new("RGB", image.size, BACKGROUND)).convert("L").point(lambda v: 255 if v > 6 else 0)
        box = content.getbbox()
        if box is None:
            sys.exit("Ảnh chụp trống")
        if box[3] < height - 40 or height >= 16000:
            return image.crop((0, 0, width, min(height, box[3] + 16))), content
        height = min(height * 2, 16000)


def cut_points(content, total, target):
    rows = content.getprojection()[1]
    cuts, start = [], 0
    while total - start > target * 1.25:
        limit = start + target
        window = [y for y in range(start + target // 2, min(total, start + int(target * 1.25))) if not rows[y]]
        gaps = [y for y in window if y > 0 and not rows[y - 1]]
        cut = min(gaps, key=lambda y: abs(y - limit)) if gaps else limit
        cuts.append(cut)
        start = cut
    return cuts


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("name", help="tên mục mock, vd w_gentle")
    parser.add_argument("--out", type=Path)
    parser.add_argument("--width", type=int, default=932)
    parser.add_argument("--slice", type=int, default=1500, help="chiều cao mục tiêu mỗi mảnh PNG")
    args = parser.parse_args()

    out = (args.out or ROOT / "build/mock" / args.name).resolve()
    out.mkdir(parents=True, exist_ok=True)
    source = out / f"{args.name}.html"
    subprocess.run([sys.executable, str(GENERATOR), str(out), args.name], check=True, cwd=ROOT, stdout=subprocess.DEVNULL)
    if not source.exists():
        sys.exit(f"Script mock không tạo {source.name}: sai tên mục?")

    html, embedded, missing, notes = embed_fonts(source.read_text(encoding="utf-8"), scan_local_fonts())
    problems = problems_of(html, embedded, missing)
    if problems:
        print("LỖI font, không chụp ảnh:")
        for problem in problems:
            print("  -", problem)
        sys.exit(2)

    page = out / "page.html"
    page.write_text(PAGE_HEAD + html, encoding="utf-8")
    image, content = render_full(page, out / "full.png", args.width)

    for old in out.glob("mock_*.png"):
        old.unlink()
    bounds = [0] + cut_points(content.crop((0, 0, image.width, image.height)), image.height, args.slice) + [image.height]
    pieces = []
    for index, (top, bottom) in enumerate(zip(bounds, bounds[1:]), start=1):
        piece = out / f"mock_{index:02d}.png"
        image.crop((0, top, image.width, bottom)).save(piece)
        pieces.append(piece)

    for note in notes:
        print("lưu ý:", note)
    print("fonts:", "; ".join(f"{name} {','.join(map(str, sorted(weights)))}" for name, weights in sorted(embedded.items())) or "không dùng font ngoài")
    print("page:", page)
    for piece in pieces:
        print("png:", piece)


if __name__ == "__main__":
    main()
