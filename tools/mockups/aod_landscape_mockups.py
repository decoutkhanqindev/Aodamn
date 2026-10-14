"""Mock bố cục AOD màn ngang (873 x 393 dp, máy 2400 x 1080) dạng đoạn HTML + SVG, để chốt bố cục với user trước khi code.

Mỗi chủ đề một file w_<chủ đề>.html: đoạn HTML gọn (dùng lại hình bằng <use>, font Plus Jakarta Sans từ Google Fonts, chữ số Số áo lấy
đường viền thật từ font bằng fontTools) để vẽ thẳng trong khung chat; user muốn xem ngay trong chat, không mở file HTML.
Chủ đề mới: thêm một khối "frames" như CỔ ĐIỂN / SỐ ÁO / SỎI dưới đây, tái dùng các mảnh vẽ khối dùng chung (thời tiết, sự kiện,
ghi nhớ, nét vẽ, nhạc, dòng pin). Cách chọn bố cục theo khối đang hiện: README › Thêm chủ đề mới › Bố cục ngang.

    pip install fonttools
    python tools/mockups/aod_landscape_mockups.py <thư mục ra>
"""
import math
import sys
from pathlib import Path

from fontTools.pens.boundsPen import BoundsPen
from fontTools.pens.svgPathPen import SVGPathPen
from fontTools.pens.transformPen import TransformPen
from fontTools.ttLib import TTFont

ROOT = Path(__file__).resolve().parents[2]
OUT = Path(sys.argv[1]) if len(sys.argv) > 1 else Path.cwd()

W, H = 873, 393
CY = H / 2
G = "#B4B4B4"
GE = "#EDEDED"
DV = "#3A3A3A"
DM = "#7A7A7A"
LAV = "#BAB8F8"
PU = "#C3A6FF"
MINT = "#7FD1AE"
FONT_LINK = ('<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@200;300;400;500;600;700'
             '&display=swap">')


def f(v):
    return f"{v:.1f}".rstrip("0").rstrip(".")


def txt(x, y, s, size, color=G, weight=400, anchor="start", extra=""):
    a = "" if anchor == "start" else f' text-anchor="{anchor}"'
    c = "" if color == G else f' fill="{color}"'
    w = "" if weight == 400 else f' font-weight="{weight}"'
    return f'<text x="{f(x)}" y="{f(y)}" font-size="{f(size)}"{c}{w}{a}{(" " + extra) if extra else ""}>{s}</text>'


def use(ref, x=0, y=0, extra=""):
    return f'<use href="#{ref}" x="{f(x)}" y="{f(y)}"{(" " + extra) if extra else ""}/>'


def frame(label, body):
    return (f'<p style="font-size:13px;color:var(--text-secondary);margin:1.25rem 0 6px">{label}</p>'
            f'<svg viewBox="0 0 {W} {H}" width="100%" role="img" aria-label="{label}" '
            f'style="display:block;font-family:\'Plus Jakarta Sans\',sans-serif" fill="{G}">'
            f'<rect width="{W}" height="{H}" rx="30" fill="#000"/>{body}</svg>')


def defs_svg(defs):
    return f'<svg width="0" height="0" style="position:absolute" aria-hidden="true" font-family="\'Plus Jakarta Sans\',sans-serif"><defs>{defs}</defs></svg>'


def widget(summary, defs, frames):
    return (f'<h2 class="sr-only">{summary}</h2>{FONT_LINK}{defs_svg(defs)}'
            f'<div style="padding:0 0 .5rem">{"".join(frames)}</div>')


# ------------------------------------------------------------------ mảnh vẽ tại gốc toạ độ (để dùng lại bằng <use>)
def cloud(x, y, k=1.0, color=G, sw=1.8):
    return (f'<path d="M{f(x + 8 * k)},{f(y - 4 * k)} a{f(8 * k)},{f(8 * k)} 0 0 1 {f(2 * k)},{f(-15.5 * k)} '
            f'a{f(11 * k)},{f(11 * k)} 0 0 1 {f(21 * k)},{f(-2 * k)} a{f(7.5 * k)},{f(7.5 * k)} 0 0 1 {f(3 * k)},{f(17.5 * k)} Z" '
            f'fill="none" stroke="{color}" stroke-width="{f(sw)}" stroke-linejoin="round"/>')


def battery_h(x, y, color=G, level=0.72):
    return (f'<rect x="{f(x)}" y="{f(y)}" width="20" height="11" rx="2.5" fill="none" stroke="{color}" stroke-width="1.4"/>'
            f'<rect x="{f(x + 20.8)}" y="{f(y + 3.5)}" width="1.8" height="4" rx=".8" fill="{color}"/>'
            f'<rect x="{f(x + 2.2)}" y="{f(y + 2.2)}" width="{f(15.6 * level)}" height="6.6" rx="1.2" fill="{color}"/>')


def icon_shapes(color):
    return [
        lambda X, Y, s: f'<path d="M{f(X)},{f(Y + s * .15)} h{f(s)} v{f(s * .6)} h-{f(s * .55)} l-{f(s * .25)},{f(s * .25)} v-{f(s * .25)} h-{f(s * .2)} Z" fill="{color}"/>',
        lambda X, Y, s: (f'<rect x="{f(X)}" y="{f(Y + s * .18)}" width="{f(s)}" height="{f(s * .66)}" rx="1.5" fill="none" stroke="{color}" stroke-width="1.5"/>'
                         f'<path d="M{f(X)},{f(Y + s * .2)} L{f(X + s / 2)},{f(Y + s * .55)} L{f(X + s)},{f(Y + s * .2)}" fill="none" stroke="{color}" stroke-width="1.5"/>'),
        lambda X, Y, s: f'<path d="M{f(X)},{f(Y + s * .45)} L{f(X + s)},{f(Y + s * .05)} L{f(X + s * .72)},{f(Y + s * .95)} L{f(X + s * .45)},{f(Y + s * .6)} Z" fill="{color}"/>',
    ]


def noti_icons(x, cy, color=G, n=3, size=15, gap=8):
    shapes = icon_shapes(color)
    return "".join(shapes[i % 3](x + i * (size + gap), cy - size / 2, size) for i in range(n))


def status_row(cx, cy, color=G, n=3):
    w = 22 + 6 + 30 + (16 + n * 23 - 8 if n else 0)
    x = cx - w / 2
    s = battery_h(x, cy - 5.5, color) + txt(x + 28, cy + 5, "72%", 14, color, 500)
    if n:
        s += f'<circle cx="{f(x + 66)}" cy="{f(cy)}" r="2" fill="{color}"/>' + noti_icons(x + 76, cy, color, n)
    return s


def date_spaced(cx, y, weather=False):
    parts = ('<tspan font-size="14" font-weight="600" letter-spacing="1.5">THỨ BA</tspan>'
             '<tspan dx="8">•</tspan><tspan dx="8" font-size="15">13 tháng 10</tspan>')
    if not weather:
        return f'<text x="{f(cx)}" y="{f(y)}" text-anchor="middle">{parts}</text>'
    return (f'<text x="{f(cx - 34)}" y="{f(y)}" text-anchor="middle">{parts}</text>'
            + cloud(cx + 62, y + 1, 0.6, sw=1.4) + txt(cx + 88, y, "24°", 15))


def classic_hero(weather=False, n=3):
    """Đồng hồ 10:08 + ngày + dòng pin / thông báo, tâm khối ở (0, 0)."""
    base = -66 + 76 * 0.74
    return (txt(0, base, "10:08", 76, G, 200, "middle", 'letter-spacing="-1"')
            + date_spaced(0, base + 36, weather) + status_row(0, base + 72, n=n))


def media_row(w=296):
    s = txt(0, 16, "That Cool Song", 15, G, 600) + txt(0, 36, "It's Artist", 14, DM)
    bx = w - 112
    s += f'<path d="M{bx + 6},12 v16 M{bx + 22},12 l-12,8 l12,8 Z" stroke="{G}" stroke-width="2" stroke-linejoin="round"/>'
    s += f'<circle cx="{bx + 56}" cy="20" r="18" fill="none" stroke="{G}" stroke-width="1.5"/><path d="M{bx + 50},11 l14,9 l-14,9 Z"/>'
    s += f'<path d="M{bx + 90},12 l12,8 l-12,8 Z M{bx + 106},12 v16" stroke="{G}" stroke-width="2" stroke-linejoin="round"/>'
    s += f'<line x1="0" y1="58" x2="{w}" y2="58" stroke="{DV}" stroke-width="3" stroke-linecap="round"/>'
    s += f'<line x1="0" y1="58" x2="{f(w * .42)}" y2="58" stroke="{G}" stroke-width="3" stroke-linecap="round"/>'
    return s + txt(0, 80, "1:24", 14, DM) + txt(w, 80, "3:18", 14, DM, 400, "end")


def media_compact():
    s = f'<circle cx="16" cy="0" r="16" fill="none" stroke="{G}" stroke-width="1.5"/><path d="M11,-8 l13,8 l-13,8 Z"/>'
    s += txt(42, 5, "That Cool Song  ·  It's Artist", 14)
    return s + f'<path d="M248,-7 l11,7 l-11,7 Z M263,-7 v14" stroke="{G}" stroke-width="2" stroke-linejoin="round"/>'


def weather_header():
    return cloud(0, 0) + txt(46, 0, "24°", 30, G, 300) + txt(100, -2, "Nhiều mây", 14, DM)


EVENTS = [("09:00", "Họp nhóm thiết kế"), ("12:30", "Ăn trưa với Lan"), ("19:00", "Tập gym")]


def events_list(n):
    s = (f'<rect x="0" y="-13" width="15" height="14" rx="2.5" fill="none" stroke="{G}" stroke-width="1.5"/>'
         f'<line x1="0" y1="-9" x2="15" y2="-9" stroke="{G}" stroke-width="1.5"/>')
    for i, (t, name) in enumerate(EVENTS[:n]):
        s += txt(28, i * 24, t, 14, G, 600) + txt(76, i * 24, name, 14)
    return s


def events_timeline():
    s = ""
    for i, (t, name) in enumerate(EVENTS):
        y = i * 48
        s += f'<circle cx="3.5" cy="{y - 8}" r="3.5"/>'
        if i < 2:
            s += f'<line x1="3.5" y1="{y - 1}" x2="3.5" y2="{y + 32}" stroke="{G}" stroke-opacity=".4"/>'
        s += txt(16, y - 3, t, 14, G, 700) + txt(16, y + 16, name, 14)
    return s


def memo_icon():
    return (f'<rect x="0" y="-13" width="14" height="15" rx="2" fill="none" stroke="{G}" stroke-width="1.5"/>'
            f'<path d="M3,-7 h8 M3,-3 h6" stroke="{G}" stroke-width="1.3"/>' + txt(28, 0, "Mua sữa trên đường về", 14))


def drawing():
    """Nét vẽ tay 120 x 120 tại gốc."""
    return ('<path d="M60,98 C20,70 10,44 26,30 C40,18 56,26 60,40 C64,26 80,18 94,30 C110,44 100,70 60,98" '
            f'fill="none" stroke="{G}" stroke-width="3" stroke-linecap="round"/>'
            f'<path d="M84,6 l6,8 M100,4 l-2,12 M112,12 l-9,6" fill="none" stroke="{G}" stroke-width="2.5" stroke-linecap="round"/>')


def notification_card():
    return (f'<circle cx="14" cy="2" r="14" fill="none" stroke="{G}" stroke-width="1.5"/>' + txt(14, 7, "L", 14, G, 700, "middle")
            + txt(40, -2, "Lan  ·  Messenger", 14, G, 600) + txt(40, 18, "Tối nay đi ăn lẩu không? Mình", 14)
            + txt(40, 37, "đặt bàn lúc 7 giờ nhé", 14))


def vline(x, y0=126, y1=266):
    return f'<line x1="{f(x)}" y1="{y0}" x2="{f(x)}" y2="{y1}" stroke="{DV}"/>'


# ================================================================== CỔ ĐIỂN
classic_defs = "".join([
    f'<g id="ch">{classic_hero()}</g>',
    f'<g id="chw">{classic_hero(weather=True)}</g>',
    f'<g id="cmr">{media_row()}</g>',
    f'<g id="cmr2">{media_row(250)}</g>',
    f'<g id="cwh" fill="{G}">{weather_header()}</g>',
    f'<g id="cev3">{events_list(3)}</g>',
    f'<g id="cev2">{events_list(2)}</g>',
    f'<g id="cmemo">{memo_icon()}</g>',
    f'<g id="cdraw">{drawing()}</g>',
])

classic = [
    frame("C1 · Tối giản", use("chw", W / 2, CY)),
    frame("C2 · Nhạc", use("ch", 240, CY) + vline(436) + use("cmr", 498, CY - 40)),
    frame("C3 · Lịch trình", use("ch", 240, CY) + vline(436, 96, 296) + use("cwh", 498, 128) + use("cev3", 498, 178) + use("cmemo", 498, 270)),
    frame("C4 · Bảng vẽ", use("ch", 240, CY) + vline(436, 96, 296)
          + use("cdraw", 0, 0, f'transform="translate(484 {f(CY - 75)}) scale(1.25)"') + f'<g transform="translate(664 {f(CY - 38)})">{events_timeline()}</g>'),
    frame("C5 · Tin nhắn", use("ch", 240, CY) + vline(436) + f'<g transform="translate(498 {f(CY - 40)})">{notification_card()}</g>'
          + txt(638, CY + 58, "“", 40, G, 400, "middle") + txt(638, CY + 80, "Mua sữa trên đường về", 15, G, 400, "middle")),
    frame("C6 · Ba cột", use("cwh", 36, 140) + use("cev3", 36, 186) + vline(316, 96, 296) + use("ch", W / 2, CY)
          + vline(557, 96, 296) + use("cmr2", 587, CY - 40)),
    frame("C7 · Đầy đủ", use("cwh", 36, 112) + use("cev2", 36, 160) + use("cmemo", 36, 230) + vline(316, 86, 306) + use("ch", W / 2, CY)
          + vline(557, 86, 306) + use("cdraw", 0, 0, 'transform="translate(642 70) scale(.92)"')
          + f'<g transform="translate(587 262)">{media_compact()}</g>'),
]

# ================================================================== SỐ ÁO
BIG = TTFont(ROOT / "app/src/main/res/font/big_shoulders_display_black.ttf")
GLYPHS = BIG.getGlyphSet()
CMAP = BIG.getBestCmap()


def bounds(ch):
    pen = BoundsPen(GLYPHS)
    GLYPHS[CMAP[ord(ch)]].draw(pen)
    return pen.bounds


CAP = bounds("1")[3] - bounds("1")[1]
CONDENSE, PITCH, STROKE = 0.685, 0.4006, 0.0082
RU = 100.0  # chữ số vẽ ở R = 100 rồi phóng bằng scale (nét phóng theo, đúng tỉ lệ 0,0082 R)


def jersey_glyph(ch):
    cell = PITCH * RU
    st = STROKE * RU
    if ch == "1":
        w, fw, fh = 0.2078 * RU, 0.0359 * RU, 0.172 * RU
        left, hs = (cell - w) / 2, st / 2
        pts = [(left + hs, hs), (left + w - hs, hs), (left + w - hs, RU - hs), (left + fw + hs, RU - hs), (left + fw + hs, fh - hs), (left + hs, fh - hs)]
        d = "M" + " L".join(f"{f(a)},{f(b)}" for a, b in pts) + "Z"
    else:
        x0, _, x1, _ = bounds(ch)
        sy = (RU - st) / CAP
        sx = sy * CONDENSE
        pen = SVGPathPen(GLYPHS, ntos=f)
        GLYPHS[CMAP[ord(ch)]].draw(TransformPen(pen, (sx, 0, 0, -sy, cell / 2 - (x0 + x1) / 2 * sx, RU - st / 2)))
        d = pen.getCommands()
    return f'<path id="j{ch}" d="{d}" fill="none" stroke-width="{f(st)}"/>'


U = 0.3506 * W  # cỡ chữ ngày / dòng pin = R của màn dọc


def jersey_date_row(cx, top):
    fs = 0.0481 * U
    pill_h = 0.0837 * U
    date_w = fs * 3.3
    pill_w = 6 * fs * 0.78 + 2 * 0.033 * U + 8
    gap = 0.0628 * U
    x = cx - (date_w + gap + pill_w) / 2
    base = top + pill_h / 2 + fs * 0.37
    px = x + date_w + gap
    bw = 1.6
    return (txt(x, base, "13 / 10", fs, LAV, 700)
            + f'<rect x="{f(px)}" y="{f(top + bw / 2)}" width="{f(pill_w)}" height="{f(pill_h - bw)}" rx="{f(pill_h / 2)}" fill="none" stroke="{LAV}" stroke-width="{bw}"/>'
            + txt(px + pill_w / 2, base, "THỨ BA", fs * 0.92, LAV, 700, "middle", f'letter-spacing="{f(0.056 * fs)}"'))


def jersey_status(cx, cy, noti=True, charging=False):
    gw, gh, fs, icon = 0.024 * U, 0.046 * U, 0.0421 * U, 0.045 * U
    text_w = fs * 1.9
    w = gw + 0.033 * U + text_w + ((0.052 + 0.049 + 0.0105) * U + 3 * icon + 2 * 0.052 * U if noti else 0)
    x, top = cx - w / 2, cy - gh / 2
    if charging:
        s = (f'<rect x="{f(x)}" y="{f(top + gh * .1)}" width="{f(gw)}" height="{f(gh * .9)}" rx="{f(gw * .25)}" fill="{GE}"/>'
             f'<rect x="{f(x + gw * .28)}" y="{f(top)}" width="{f(gw * .44)}" height="{f(gh * .12)}" fill="{GE}"/>'
             f'<path d="M{f(x + gw * .62)},{f(top + gh * .2)} L{f(x + gw * .22)},{f(top + gh * .6)} L{f(x + gw * .48)},{f(top + gh * .6)} '
             f'L{f(x + gw * .38)},{f(top + gh * .92)} L{f(x + gw * .8)},{f(top + gh * .48)} L{f(x + gw * .54)},{f(top + gh * .48)} Z" fill="#000"/>')
    else:
        sw = gw * .14
        inner = (gh * .9 - sw * 3.2) * .72
        s = (f'<rect x="{f(x + sw / 2)}" y="{f(top + gh * .1 + sw / 2)}" width="{f(gw - sw)}" height="{f(gh * .9 - sw)}" rx="{f(gw * .25)}" fill="none" stroke="{GE}" stroke-width="{f(sw)}"/>'
             f'<rect x="{f(x + gw * .28)}" y="{f(top)}" width="{f(gw * .44)}" height="{f(gh * .12)}" fill="{GE}"/>'
             f'<rect x="{f(x + sw * 1.6)}" y="{f(top + gh - sw * 1.6 - inner)}" width="{f(gw - sw * 3.2)}" height="{f(inner)}" fill="{GE}"/>')
    tx = x + gw + 0.033 * U
    s += txt(tx, cy + fs * 0.36, "72%", fs, GE, 500)
    if noti:
        dx = tx + text_w + 0.052 * U
        s += f'<circle cx="{f(dx)}" cy="{f(cy)}" r="{f(0.0105 * U / 2)}" fill="{GE}"/>'
        s += noti_icons(dx + 0.0105 * U / 2 + 0.049 * U, cy, GE, 3, size=icon, gap=0.052 * U)
    return s


def jersey_column(date=True, noti=True, extra_h=0, extra=None, charging=False):
    date_h, gap_date, gap_status, status_h = 0.0837 * U, 0.0665 * U, 22, 0.046 * U
    head = date_h + gap_date if date else 0
    R = min(H * 0.62, H * 0.88 - head - gap_status - status_h - extra_h)
    top = (H - (head + R + gap_status + status_h + extra_h)) / 2
    s = ""
    if date:
        s += jersey_date_row(W / 2, top)
        top += head
    pitch, side = PITCH * R, 0.3 * R
    x = W / 2 - (4 * pitch + side) / 2
    k = R / RU
    for i, (ch, col) in enumerate(zip("1008", [LAV, LAV, GE, GE])):
        s += f'<use href="#j{ch}" transform="translate({f(x + i * pitch + (side if i >= 2 else 0))} {f(top)}) scale({k:.3f})" stroke="{col}"/>'
    top += R + gap_status
    s += jersey_status(W / 2, top + status_h / 2, noti, charging)
    if extra:
        s += extra(top + status_h)
    return s


def jersey_extra(top):
    fs = 0.0421 * U
    y = top + 30
    return (f'<circle cx="{f(W / 2 - 150)}" cy="{f(y - fs * .36)}" r="11" fill="none" stroke="{LAV}" stroke-width="1.5"/>'
            f'<path d="M{f(W / 2 - 153)},{f(y - fs * .36 - 6)} l9,6 l-9,6 Z" fill="{LAV}"/>'
            + txt(W / 2 - 128, y, "That Cool Song  ·  It's Artist", fs, GE, 500)
            + txt(W / 2 + 152, y, "09:00 Họp nhóm", fs, LAV, 700, "end"))


jersey_defs = jersey_glyph("1") + jersey_glyph("0") + jersey_glyph("8")
jersey = [
    frame("J1 · Số áo ngang", jersey_column()),
    frame("J2 · Tắt ngày, không thông báo (đang sạc)", jersey_column(date=False, noti=False, charging=True)),
    frame("J3 · Freestyle khi có thêm khối (tuỳ chọn)", jersey_column(extra_h=44, extra=jersey_extra)),
]


# ================================================================== SỎI
def superellipse(a, n, minor=1.0):
    return (abs(math.cos(a)) ** n + (abs(math.sin(a)) / minor) ** n) ** (-1 / n)


def radius(shape, a):
    if shape == "squircle":
        return superellipse(a, 5.0)
    if shape == "clover":
        return superellipse(a, 3.2) * (1 - 0.12 * (1 + math.cos(4 * a)) / 2)
    if shape == "pebble":
        return superellipse(a + math.pi / 4, 2.3, 0.74)
    return 1.0


def unit_shape(shape, inset=0.0, n=96):
    pts = [(math.cos(2 * math.pi * i / n) * radius(shape, 2 * math.pi * i / n),
            math.sin(2 * math.pi * i / n) * radius(shape, 2 * math.pi * i / n)) for i in range(n)]
    mx, my = max(abs(p[0]) for p in pts), max(abs(p[1]) for p in pts)
    r = 50 - inset
    return "M" + " L".join(f"{f(p[0] / mx * r)},{f(p[1] / my * r)}" for p in pts) + "Z"


def tile(text, filled, cx, cy, size, digit_color):
    k = size / 100
    shape = (f'<use href="#psq" transform="translate({f(cx)} {f(cy)}) scale({k:.3f})" fill="{PU}"/>' if filled else
             f'<use href="#pcl" transform="translate({f(cx)} {f(cy)}) scale({k:.3f})" fill="none" stroke="{PU}" stroke-width="3.5"/>')
    fs = size * 50 / 112
    return shape + txt(cx, cy + fs * 0.36, text, fs, digit_color, 700, "middle")


def pebble_date(cx, y):
    return txt(cx, y, "THỨ BA  •  13 THG 10", 14, G, 700, "middle", 'letter-spacing=".6"')


def pebble_hero(cx, cy, size):
    gap = size * 0.12
    top = cy - (size + 76) / 2
    return (tile("10", True, cx - (size + gap) / 2, top + size / 2, size, "#000")
            + tile("08", False, cx + (size + gap) / 2, top + size / 2, size, PU)
            + pebble_date(cx, top + size + 30) + status_row(cx, top + size + 58, n=2))


def pebble_player(w=280):
    s = txt(0, 18, "That Cool Song", 15, G, 600) + txt(0, 38, "It's Artist", 14, DM)
    bx = w - 108
    s += f'<path d="M{bx + 2},11 l-10,7 l10,7 Z" fill="{PU}"/>'
    s += f'<use href="#ppb" transform="translate({bx + 54} 18) scale(.44)" fill="{PU}"/><path d="M{bx + 48},9 l15,9 l-15,9 Z" fill="#000"/>'
    s += f'<path d="M{bx + 92},11 l10,7 l-10,7 Z" fill="{PU}"/>'
    wave = " ".join(f"L{f(i * 3)},{f(66 + 3.2 * math.sin(i * 0.9))}" for i in range(int(w * 0.45 / 3)))
    s += f'<path d="M0,66 {wave}" fill="none" stroke="{PU}" stroke-width="3" stroke-linecap="round"/>'
    return s + f'<line x1="{f(w * .47)}" y1="66" x2="{w}" y2="66" stroke="{DV}" stroke-width="3" stroke-linecap="round"/>'


pebble_defs = (f'<path id="psq" d="{unit_shape("squircle")}"/><path id="pcl" d="{unit_shape("clover", 1.75)}"/>'
               f'<path id="ppb" d="{unit_shape("pebble", n=48)}"/><g id="pply">{pebble_player()}</g>'
               f'<g id="phero">{pebble_hero(0, 0, 118)}</g>'
               f'<g id="pwh" fill="{G}">{weather_header()}</g><g id="pev3">{events_list(3)}</g><g id="pev2">{events_list(2)}</g>'
               f'<g id="pmemo">{memo_icon()}</g>')
T = 170
pebble = [
    frame("P1 · Tối giản", pebble_hero(W / 2, CY, 150)),
    frame("P2 · Nhạc (kẹp giữa)", tile("10", True, 40 + T / 2, CY, T, "#000") + tile("08", False, W - 40 - T / 2, CY, T, PU)
          + pebble_date(W / 2, CY - 70) + use("pply", W / 2 - 140, CY - 44) + status_row(W / 2, CY + 72, n=2)),
    frame("P3 · Lịch trình / nét vẽ / tin nhắn", use("phero", 236, CY) + vline(436, 96, 296) + use("pwh", 498, 128) + use("pev3", 498, 178)
          + use("pmemo", 498, 270)),
    frame("P4 · Nhạc + nhóm khác", use("phero", 236, CY) + vline(436, 70, 322) + use("pwh", 498, 100) + use("pev2", 498, 146)
          + use("pply", 500, 222)),
]

for name, summary, defs, frames in [
    ("w_classic", "Bảy bố cục ngang của chủ đề Cổ điển theo các khối đang bật", classic_defs, classic),
    ("w_jersey", "Bố cục ngang của chủ đề Số áo: ngày, bốn chữ số một hàng, dòng pin", jersey_defs, jersey),
    ("w_pebble", "Bốn bố cục ngang của chủ đề Sỏi", pebble_defs, pebble),
]:
    code = widget(summary, defs, frames)
    OUT.mkdir(parents=True, exist_ok=True)
    (OUT / f"{name}.html").write_text(code, encoding="utf-8")
    print(name, len(code))
