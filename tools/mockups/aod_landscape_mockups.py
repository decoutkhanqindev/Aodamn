"""Mock bố cục AOD màn ngang (873 x 393 dp, máy 2400 x 1080; Viên nang có thêm khung dọc 393 x 873) dạng đoạn HTML + SVG, để chốt bố cục với user trước khi code.

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
from fontTools.varLib import instancer

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
FONT_LINK = ('<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@200;300;400;500;600;700;800'
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

# ================================================================== VIÊN NANG (ảnh mẫu reference/theme_3, R = chiều cao chữ số = 47,6 dp)
PJS800 = instancer.instantiateVariableFont(TTFont(ROOT / "tools/fonts/variable/plus_jakarta_sans_variable.ttf"), {"wght": 800}, inplace=False)
PJS_GLYPHS = PJS800.getGlyphSet()
PJS_CMAP = PJS800.getBestCmap()
PW, PH = 393, 873
CR = 47.6
FROST = "#C8D8D8"
CAPSULE_GREY = "#D0D0D0"
DATE_GREY = "#E0E0E0"
LIME = "#DDEB6E"
ARTIST_GREY = "#B0B0B0"


def pjs_bounds(ch):
    pen = BoundsPen(PJS_GLYPHS)
    PJS_GLYPHS[PJS_CMAP[ord(ch)]].draw(pen)
    return pen.bounds


PJS_DIGIT = pjs_bounds("0")[3] - pjs_bounds("0")[1]


def capsule_glyph(ch):
    """Chữ số Plus Jakarta Sans ExtraBold cao RU, căn giữa ô rộng 0,856 R (bước chữ số của ảnh mẫu)."""
    cell = 0.856 * RU
    x0, _, x1, _ = pjs_bounds(ch)
    y0 = pjs_bounds("0")[1]
    k = RU / PJS_DIGIT
    pen = SVGPathPen(PJS_GLYPHS, ntos=f)
    PJS_GLYPHS[PJS_CMAP[ord(ch)]].draw(TransformPen(pen, (k, 0, 0, -k, cell / 2 - (x0 + x1) / 2 * k, RU + y0 * k)))
    return f'<path id="v{ch}" d="{pen.getCommands()}"/>'


def capsule_clock(cx, cy, style="filled", digit=FROST, capsule=CAPSULE_GREY, text="10:08"):
    """Viên nang 4,76 R x 1,81 R; đặc = màu viên 12% trên nền đen, viền = nét 0,043 R. Chữ số theo ô, hai chấm là hai chấm tròn."""
    w, h = 4.76 * CR, 1.81 * CR
    if style == "filled":
        s = f'<rect x="{f(cx - w / 2)}" y="{f(cy - h / 2)}" width="{f(w)}" height="{f(h)}" rx="{f(h / 2)}" fill="{capsule}" fill-opacity=".12"/>'
    else:
        sw = 0.043 * CR
        s = (f'<rect x="{f(cx - w / 2 + sw / 2)}" y="{f(cy - h / 2 + sw / 2)}" width="{f(w - sw)}" height="{f(h - sw)}" rx="{f((h - sw) / 2)}" '
             f'fill="none" stroke="{capsule}" stroke-width="{f(sw)}"/>')
    cell, colon = 0.856 * CR, 0.434 * CR
    x = cx - (4 * cell + colon) / 2
    k = CR / RU
    hh, mm = text.split(":")
    for ch in hh:
        s += use(f"v{ch}", 0, 0, f'transform="translate({f(x)} {f(cy - CR / 2)}) scale({k:.4f})" fill="{digit}"')
        x += cell
    dot = 0.26 * CR
    s += f'<circle cx="{f(x + colon / 2)}" cy="{f(cy - 0.22 * CR)}" r="{f(dot / 2)}" fill="{digit}"/>'
    s += f'<circle cx="{f(x + colon / 2)}" cy="{f(cy + 0.22 * CR)}" r="{f(dot / 2)}" fill="{digit}"/>'
    x += colon
    for ch in mm:
        s += use(f"v{ch}", 0, 0, f'transform="translate({f(x)} {f(cy - CR / 2)}) scale({k:.4f})" fill="{digit}"')
        x += cell
    return s


def capsule_date(cx, cap_top, color=DATE_GREY, weather=False):
    size = 0.323 * CR
    base = cap_top + 0.745 * size
    if not weather:
        return txt(cx, base, "THỨ TƯ • 14 THG 10", size, color, 700, "middle")
    return (txt(cx - 30, base, "THỨ TƯ • 14 THG 10", size, color, 700, "middle")
            + cloud(cx + 70, base + 1, 0.55, color, 1.4) + txt(cx + 94, base, "24°", size, color, 700))


def capsule_battery(x, top, color):
    w, h = 0.164 * CR, 0.327 * CR
    cap = h * 0.1
    return (f'<rect x="{f(x + w * .28)}" y="{f(top)}" width="{f(w * .44)}" height="{f(cap + 1)}" rx=".8" fill="{color}"/>'
            f'<rect x="{f(x)}" y="{f(top + cap)}" width="{f(w)}" height="{f(h - cap)}" rx="{f(w * .25)}" fill="{color}"/>'
            f'<path d="M{f(x + w * .62)},{f(top + cap + (h - cap) * .12)} L{f(x + w * .22)},{f(top + cap + (h - cap) * .56)} '
            f'L{f(x + w * .48)},{f(top + cap + (h - cap) * .56)} L{f(x + w * .38)},{f(top + cap + (h - cap) * .9)} '
            f'L{f(x + w * .8)},{f(top + cap + (h - cap) * .42)} L{f(x + w * .54)},{f(top + cap + (h - cap) * .42)} Z" fill="#000"/>')


def capsule_status(cx, cy, color=DATE_GREY, n=4):
    gh, font, icon = 0.327 * CR, 0.29 * CR, 0.31 * CR
    w = 0.164 * CR + 0.2 * CR + 1.9 * font + (0.28 * CR + 0.08 * CR + 0.29 * CR + n * icon + (n - 1) * 0.24 * CR if n else 0)
    x = cx - w / 2
    s = capsule_battery(x, cy - gh / 2, color)
    x += 0.164 * CR + 0.2 * CR
    s += txt(x, cy + 0.769 * font / 2, "42%", font, color, 500)
    x += 1.9 * font
    if n:
        x += 0.28 * CR
        s += f'<circle cx="{f(x + 0.04 * CR)}" cy="{f(cy)}" r="{f(0.04 * CR)}" fill="{color}"/>'
        x += 0.08 * CR + 0.29 * CR
        shapes = icon_shapes(color)
        for i in range(n):
            s += shapes[i % 3](x, cy - icon / 2, icon)
            x += icon + 0.24 * CR
    return s


def capsule_player(w=315, accent=FROST):
    """Trình phát của ảnh mẫu: tên bài / nghệ sĩ bên trái, lùi · nút phát tròn đặc · tới bên phải, thanh tiến trình dày tách đôi."""
    s = txt(0, 15, "That Cool Song", 17, accent, 500) + txt(0, 33, "It's Artist", 13.5, ARTIST_GREY)
    r = w
    s += (f'<rect x="{f(r - 118.6)}" y="10" width="2.7" height="15" rx="1" fill="{ARTIST_GREY}"/>'
          f'<path d="M{f(r - 101)},10 L{f(r - 101)},25 L{f(r - 113)},17.5 Z" fill="{ARTIST_GREY}" stroke="{ARTIST_GREY}" stroke-width="1.5" stroke-linejoin="round"/>')
    s += (f'<circle cx="{f(r - 61.4)}" cy="17.5" r="17.6" fill="{accent}"/>'
          f'<path d="M{f(r - 66)},10.5 L{f(r - 53.5)},17.5 L{f(r - 66)},24.5 Z" fill="#000" stroke="#000" stroke-width="1.5" stroke-linejoin="round"/>')
    s += (f'<path d="M{f(r - 21.5)},10 L{f(r - 21.5)},25 L{f(r - 9.5)},17.5 Z" fill="{ARTIST_GREY}" stroke="{ARTIST_GREY}" stroke-width="1.5" stroke-linejoin="round"/>'
          f'<rect x="{f(r - 6.6)}" y="10" width="2.7" height="15" rx="1" fill="{ARTIST_GREY}"/>')
    split = w * 0.7
    s += f'<line x1="3" y1="55" x2="{f(split - 3)}" y2="55" stroke="{accent}" stroke-width="6" stroke-linecap="round"/>'
    s += f'<line x1="{f(split + 6)}" y1="55" x2="{f(w - 3)}" y2="55" stroke="{DV}" stroke-width="6" stroke-linecap="round"/>'
    return s


def capsule_column(style="filled", digit=FROST, capsule=CAPSULE_GREY, accent=FROST, extras=None, extras_h=0, media=True):
    """Màn dọc: viên nang → ngày → khối thêm → nhạc → dòng pin, cả cột căn giữa màn hình."""
    total = 1.81 * CR + 0.41 * CR + 0.323 * CR * 0.745 + 36 + extras_h + (59 + 40 if media else 40) + 0.327 * CR
    top = (PH - total) / 2
    cx = PW / 2
    s = capsule_clock(cx, top + 1.81 * CR / 2, style, digit, capsule)
    y = top + 1.81 * CR + 0.41 * CR
    s += capsule_date(cx, y)
    y += 0.323 * CR * 0.745 + 36
    if extras:
        s += extras(y)
        y += extras_h
    if media:
        s += f'<g transform="translate({f((PW - 315) / 2)} {f(y)})">{capsule_player(accent=accent)}</g>'
        y += 59 + 40
    else:
        y += 40 - 36 + 12
    return s + capsule_status(cx, y + 0.327 * CR / 2)


def capsule_hero(cx, cy, style="filled", weather=False, n=4):
    """Màn ngang: viên nang + ngày + dòng pin, tâm cụm ở (cx, cy)."""
    total = 1.81 * CR + 0.41 * CR + 0.323 * CR * 0.745 + 0.6 * CR + 0.327 * CR
    top = cy - total / 2
    s = capsule_clock(cx, top + 1.81 * CR / 2, style)
    y = top + 1.81 * CR + 0.41 * CR
    s += capsule_date(cx, y, weather=weather)
    y += 0.323 * CR * 0.745 + 0.6 * CR
    return s + capsule_status(cx, y + 0.327 * CR / 2, n=n)


def frame_portrait(label, body):
    return (f'<div style="display:inline-block;width:calc(50% - 8px);vertical-align:top;margin:0 4px">'
            f'<p style="font-size:13px;color:var(--text-secondary);margin:1.25rem 0 6px">{label}</p>'
            f'<svg viewBox="0 0 {PW} {PH}" width="100%" role="img" aria-label="{label}" '
            f'style="display:block;font-family:\'Plus Jakarta Sans\',sans-serif" fill="{G}">'
            f'<rect width="{PW}" height="{PH}" rx="34" fill="#000"/>{body}</svg></div>')


def capsule_extras(y):
    return (f'<g transform="translate(64 {f(y + 14)})">{weather_header()}</g>'
            f'<g transform="translate(64 {f(y + 62)})">{events_list(3)}</g>'
            f'<g transform="translate(64 {f(y + 150)})">{memo_icon()}</g>')


capsule_defs = "".join(capsule_glyph(ch) for ch in "0189") + f'<g id="vply">{capsule_player()}</g><g id="vply2">{capsule_player(250)}</g>'
capsule = [
    '<div style="font-size:0">',
    frame_portrait("V1 · Dọc, kiểu đặc (mặc định)", capsule_column("filled")),
    frame_portrait("V2 · Dọc, kiểu viền", capsule_column("outline")),
    frame_portrait("V3 · Dọc, thêm khối (thời tiết, sự kiện, ghi nhớ)", capsule_column("filled", extras=capsule_extras, extras_h=176, media=False)),
    frame_portrait("V4 · Dọc, đổi màu (số, viên, nhạc)", capsule_column("outline", LIME, LIME, LIME)),
    '</div>',
    frame("N1 · Ngang, tối giản (thời tiết cạnh ngày)", capsule_hero(W / 2, CY, weather=True)),
    frame("N2 · Ngang, nhạc", capsule_hero(240, CY) + vline(436) + use("vply", 498, CY - 32)),
    frame("N3 · Ngang, lịch trình", capsule_hero(240, CY) + vline(436, 96, 296) + f'<g fill="{G}" transform="translate(498 128)">{weather_header()}</g>'
          + f'<g transform="translate(498 178)">{events_list(3)}</g>' + f'<g transform="translate(498 270)">{memo_icon()}</g>'),
    frame("N4 · Ngang, nhạc + nhóm khác (ba cột)", f'<g fill="{G}" transform="translate(36 140)">{weather_header()}</g>'
          + f'<g transform="translate(36 186)">{events_list(3)}</g>' + vline(316, 96, 296) + capsule_hero(W / 2, CY, "outline")
          + vline(557, 96, 296) + use("vply2", 590, CY - 32)),
]

# ================================================================== THẺ (M3 Expressive của Muviz Edge; số đo lấy từ layout + code dịch ngược và
# thông số font trong APK, không từ ảnh chụp: R = chiều cao số giờ, 190 dp ở cỡ 100 vì chữ số font mẫu cao đúng 1 em)
ER = 190.0
EX = 0.55  # Big Shoulders Display Black nén ngang: số 0 rộng 0,281 R, nét 0,095 R (font mẫu 0,286 / 0,105)
E_PITCH, E_INK = 0.358, 0.284
EC1, EC2 = "#B0BEC5", "#E4E4E4"
E_TRACK = "#505257"


def ex_digit_glyph(ch):
    """Chữ số cao RU, nén ngang EX, căn giữa ô rộng E_PITCH R (bước chữ số của font mẫu)."""
    x0, _, x1, _ = bounds(ch)
    _, z0, _, z1 = bounds("0")
    k = RU / (z1 - z0)
    pen = SVGPathPen(GLYPHS, ntos=f)
    GLYPHS[CMAP[ord(ch)]].draw(TransformPen(pen, (k * EX, 0, 0, -k, E_PITCH * RU / 2 - (x0 + x1) / 2 * k * EX, RU + z0 * k)))
    return f'<path id="e{ch}" d="{pen.getCommands()}"/>'


def ex_word_glyph(word):
    """Chữ in hoa (AM / PM, HRS lúc 24 giờ; app mẫu luôn ghi tiếng Anh) cao chữ hoa RU, nén ngang EX; trả về (path, bề rộng theo RU)."""
    _, h0, _, h1 = bounds("H")
    k = RU / (h1 - h0)
    pen = SVGPathPen(GLYPHS, ntos=f)
    x = 0.0
    for ch in word:
        name = CMAP[ord(ch)]
        GLYPHS[name].draw(TransformPen(pen, (k * EX, 0, 0, -k, x, RU + h0 * k)))
        x += BIG["hmtx"][name][0] * k * EX
    x0 = bounds(word[0])[0] * k * EX
    x1 = x - (BIG["hmtx"][CMAP[ord(word[-1])]][0] - bounds(word[-1])[2]) * k * EX
    return f'<path id="ew{word}" d="{pen.getCommands()}" transform="translate({f(-x0)} 0)"/>', x1 - x0


EX_WORDS = {w: ex_word_glyph(w) for w in ("AM", "PM", "HRS")}


def ex_digits(text, x_ink, top, h, color, outline):
    k = h / RU
    paint = (f'fill="none" stroke="{color}" stroke-width="1"' if outline else f'fill="{color}"')
    x = x_ink - (E_PITCH - E_INK) / 2 * h
    s = ""
    for ch in text:
        s += use(f"e{ch}", 0, 0, f'transform="translate({f(x)} {f(top)}) scale({k:.4f})" {paint}')
        x += E_PITCH * h
    return s


def ex_clock(cx, cy, r=ER, style="filled", bg="filled", c1=EC1, c2=EC2, hh="10", mm="08", tag="AM"):
    """Thẻ bo góc 0,21 R, đệm 0,15 R quanh chữ số; giờ cao R, phút 0,5 R trên ô nhãn (cao 0,42 R, bo 0,079 R) kéo tới đáy số giờ."""
    hw, gap, colw = (E_PITCH + E_INK) * r, 0.073 * r, 0.33 * r
    left, top = cx - (hw + gap + colw) / 2, cy - r / 2
    pad = 0.15 * r
    cw, chh = hw + gap + colw + 2 * pad, r + 2 * pad
    s = ""
    if bg == "filled":
        s += f'<rect x="{f(left - pad)}" y="{f(top - pad)}" width="{f(cw)}" height="{f(chh)}" rx="{f(0.21 * r)}" fill="{c1}" fill-opacity=".118"/>'
    elif bg == "stroke":
        sw = 0.013 * r
        s += (f'<rect x="{f(left - pad + sw / 2)}" y="{f(top - pad + sw / 2)}" width="{f(cw - sw)}" height="{f(chh - sw)}" rx="{f(0.21 * r)}" '
              f'fill="none" stroke="{c1}" stroke-opacity=".235" stroke-width="{f(sw)}"/>')
    outline = style == "outline"
    s += ex_digits(hh, left, top, r, c1, outline)
    col = left + hw + gap
    mh = 0.5 * r
    s += ex_digits(mm, col + (colw - (E_PITCH + E_INK) * mh) / 2, top, mh, c2, outline)
    by, bh = top + 0.58 * r, 0.42 * r
    if outline:
        sw = 0.0105 * r
        s += (f'<rect x="{f(col + sw / 2)}" y="{f(by + sw / 2)}" width="{f(colw - sw)}" height="{f(bh - sw)}" rx="{f(0.079 * r)}" '
              f'fill="none" stroke="{c2}" stroke-opacity=".31" stroke-width="{f(sw)}"/>')
    else:
        s += f'<rect x="{f(col)}" y="{f(by)}" width="{f(colw)}" height="{f(bh)}" rx="{f(0.079 * r)}" fill="{c2}" fill-opacity=".196"/>'
    th = 0.207 * r
    tw = EX_WORDS[tag][1] * th / RU
    s += use(f"ew{tag}", 0, 0, f'transform="translate({f(col + (colw - tw) / 2)} {f(by + (bh - th) / 2)}) scale({th / RU:.4f})" fill="{c2}"')
    return s


def ex_date(cx, cap_top, r=ER, color=EC2, weather=False):
    size = 0.059 * r / 0.745
    base = cap_top + 0.059 * r
    if not weather:
        return txt(cx, base, "THỨ BẢY • 17 THG 10", size, color, 700, "middle")
    return (txt(cx - 2.2 * size, base, "THỨ BẢY • 17 THG 10", size, color, 700, "middle")
            + cloud(cx + 3.9 * size, base + 1, 0.55, color, 1.4) + txt(cx + 5.7 * size, base, "24°", size, color, 700))


def ex_battery(x, top, color, h=15):
    w, cap = h * 0.56, h * 0.12
    return (f'<rect x="{f(x + w * .3)}" y="{f(top)}" width="{f(w * .4)}" height="{f(cap + 1)}" rx=".8" fill="{color}"/>'
            f'<rect x="{f(x)}" y="{f(top + cap)}" width="{f(w)}" height="{f(h - cap)}" rx="{f(w * .22)}" fill="{color}"/>')


def ex_status(cx, cy, color=EC2, n=3):
    """Dòng pin của layout mẫu: pin 16 dp, chữ 16 sp, chấm cách 12 dp, icon thông báo 18 dp."""
    w = 8.4 + 5 + 34 + (12 + 4 + 12 + n * 18 + (n - 1) * 8 if n else 0)
    x = cx - w / 2
    s = ex_battery(x, cy - 7.5, color) + txt(x + 13.4, cy + 6, "42%", 16, color, 500)
    if n:
        x += 8.4 + 5 + 34 + 12
        s += f'<circle cx="{f(x + 2)}" cy="{f(cy)}" r="2" fill="{color}"/>'
        x += 4 + 12
        shapes = icon_shapes(color)
        for i in range(n):
            s += shapes[i % 3](x, cy - 9, 18)
            x += 18 + 8
    return s


def ex_player(w=313, c2=EC2, title="That Cool Song"):
    """Trình phát của layout mẫu: tên 17 sp / nghệ sĩ 14 sp (80%, một dòng, cắt "…"), lùi · nút phát tròn 48 dp · tới,
    thanh M3 gợn sóng (biên 3, bước 40, nét 6)."""
    s = txt(2, 24, title, 17, c2, 500) + txt(2, 43, "It's Artist", 14, c2, 400, extra='fill-opacity=".8"')
    for cxb, flip in ((w - 122.5, True), (w - 21.5, False)):
        d = -1 if flip else 1
        s += (f'<path d="M{f(cxb - 6 * d)},22 L{f(cxb + 4 * d)},29 L{f(cxb - 6 * d)},36 Z" fill="{c2}" fill-opacity=".8" stroke="{c2}" '
              f'stroke-opacity=".8" stroke-width="1.5" stroke-linejoin="round"/>'
              f'<rect x="{f(cxb + 6 * d - (2.4 if flip else 0))}" y="22" width="2.4" height="14" rx="1" fill="{c2}" fill-opacity=".8"/>')
    s += (f'<circle cx="{f(w - 72)}" cy="29" r="24" fill="{c2}"/>'
          f'<rect x="{f(w - 78)}" y="21.5" width="4" height="15" rx="1.2" fill="#000"/><rect x="{f(w - 70)}" y="21.5" width="4" height="15" rx="1.2" fill="#000"/>')
    y, split = 73, 0.7 * w
    wave = "q10,-6 20,0" + " t20,0" * (int((split - 3) / 20) - 1)
    s += f'<path d="M3,{y} {wave}" fill="none" stroke="{c2}" stroke-width="6" stroke-linecap="round"/>'
    return s + f'<line x1="{f(split + 11)}" y1="{y}" x2="{f(w - 3)}" y2="{y}" stroke="{E_TRACK}" stroke-width="6" stroke-linecap="round"/>'


def ex_column(r=ER, style="filled", bg="filled", c1=EC1, c2=EC2, tag="AM", extras=None, extras_h=0, media=True):
    """Màn dọc: thẻ → ngày → khối thêm → nhạc → dòng pin, cả cột căn giữa màn hình (như Viên nang)."""
    card_h = 1.3 * r
    total = card_h + 0.118 * r + 0.059 * r + 36 + extras_h + (79 + 40 if media else 16) + 9
    top = (PH - total) / 2
    cx = PW / 2
    s = ex_clock(cx, top + card_h / 2, r, style, bg, c1, c2, tag=tag)
    y = top + card_h + 0.118 * r
    s += ex_date(cx, y, r, c2)
    y += 0.059 * r + 36
    if extras:
        s += extras(y)
        y += extras_h
    if media:
        s += use("eplyw" if c2 != EC2 else "eplyp", (PW - 313) / 2, y)
        y += 79 + 40
    else:
        y += 16
    return s + ex_status(cx, y, c2)


ERL = 0.9 * ER  # màn ngang: app mẫu thu đồng hồ còn 0,9


def ex_hero(cx, cy, style="filled", bg="filled", weather=False, n=3):
    """Màn ngang: thẻ + ngày (+ thời tiết cạnh ngày) + dòng pin, tâm cụm ở (cx, cy)."""
    r = ERL
    total = 1.3 * r + 0.118 * r + 0.059 * r + 26 + 9
    top = cy - total / 2
    s = ex_clock(cx, top + 0.65 * r, r, style, bg)
    y = top + 1.3 * r + 0.118 * r
    s += ex_date(cx, y, r, weather=weather)
    return s + ex_status(cx, y + 0.059 * r + 26, n=n)


expressive_defs = ("".join(ex_digit_glyph(ch) for ch in "0189") + "".join(p for p, _ in EX_WORDS.values())
                   + f'<g id="eply">{ex_player(296)}</g><g id="eply2">{ex_player(250, title="That Cool…")}</g>'
                   + f'<g id="eplyp">{ex_player()}</g><g id="eplyw">{ex_player(c2="#FFFFFF")}</g>')
expressive = [
    '<div style="font-size:0">',
    frame_portrait("T1 · Dọc, số đặc, nền thẻ mờ (mặc định)", ex_column()),
    frame_portrait("T2 · Dọc, số viền, nền thẻ viền", ex_column(style="outline", bg="stroke")),
    frame_portrait("T3 · Dọc, thêm khối (thời tiết, sự kiện, ghi nhớ)", ex_column(extras=capsule_extras, extras_h=176, media=False)),
    frame_portrait("T4 · Dọc, không nền thẻ, đổi màu, buổi chiều (PM)", ex_column(bg="none", c1=LAV, c2="#FFFFFF", tag="PM")),
    '</div>',
    frame("N1 · Ngang, tối giản (thời tiết cạnh ngày)", ex_hero(W / 2, CY, weather=True)),
    frame("N2 · Ngang, nhạc", ex_hero(236, CY) + vline(436) + use("eply", 498, CY - 40)),
    frame("N3 · Ngang, lịch trình", ex_hero(236, CY) + vline(436, 96, 296) + f'<g fill="{G}" transform="translate(498 128)">{weather_header()}</g>'
          + f'<g transform="translate(498 178)">{events_list(3)}</g>' + f'<g transform="translate(498 270)">{memo_icon()}</g>'),
    frame("N4 · Ngang, nhạc + nhóm khác (ba cột)", f'<g fill="{G}" transform="translate(36 140)">{weather_header()}</g>'
          + f'<g transform="translate(36 186)">{events_list(3)}</g>' + vline(300, 96, 296) + ex_hero(W / 2, CY, "outline", "stroke")
          + vline(573, 96, 296) + use("eply2", 600, CY - 40)),
]

# ================================================================== THÁP (Long HyperOS của Muviz Edge; số đo lấy từ layout feb24_screen_layout dọc / ngang,
# code dịch ngược và thông số font trong APK, không từ ảnh chụp). R = chiều cao chữ số = 0,768 cỡ chữ; cỡ chữ = 1,25 × cạnh ngắn × Cỡ / 100
# (màn ngang lấy 0,3 cạnh dài) → R = 377 dp màn dọc, 251 dp màn ngang ở cỡ 100.
LR = 0.768 * 1.25 * PW
LRL = 0.768 * 1.25 * min(0.3 * W, H)
LX = 0.312  # Big Shoulders Display Black nén ngang: số 0 rộng 0,193 R như font mẫu (nét 0,056 R, font mẫu 0,083 R)
L_PITCH, L_INK = 0.215, 0.193  # bước chữ số của font mẫu + giãn chữ 0,02 em
L_STROKE = 0.65  # nét viền = 0,5% cỡ chữ = 0,0065 R (đơn vị RU)
L_TOP = 52  # thanh trạng thái + 20 dp
L_STATUS_CY = 775  # dòng pin ở đáy phần 0,8 của khoảng dưới ngày (layout mẫu: trọng số 0,8 / 0,2)
L_DATE = "THỨ BẢY / 17 THG 10"


def lh_digit_glyph(ch):
    x0, _, x1, _ = bounds(ch)
    _, z0, _, z1 = bounds("0")
    k = RU / (z1 - z0)
    pen = SVGPathPen(GLYPHS, ntos=f)
    GLYPHS[CMAP[ord(ch)]].draw(TransformPen(pen, (k * LX, 0, 0, -k, L_PITCH * RU / 2 - (x0 + x1) / 2 * k * LX, RU + z0 * k)))
    return f'<path id="l{ch}" d="{pen.getCommands()}"/>'


def lh_gradients(colors):
    """Kiểu 2 / 3: màu nét nhạt dần (15 / 255 rồi 60 / 255 ở một phần tư, đậm từ giữa) về phía đáy hoặc đỉnh chữ số."""
    s = ""
    for c in colors:
        for gid, (y1, y2) in (("d", (1, 0)), ("u", (0, 1))):
            s += (f'<linearGradient id="lg{gid}{c[1:]}" x1="0" y1="{y1}" x2="0" y2="{y2}">'
                  f'<stop offset="0" stop-color="{c}" stop-opacity=".06"/><stop offset=".25" stop-color="{c}" stop-opacity=".24"/>'
                  f'<stop offset=".5" stop-color="{c}"/></linearGradient>')
    return s


def lh_digits(text, x, top, h, color, paint):
    k = h / RU
    attr = {
        "fill": f'fill="{color}"',
        "outline": f'fill="none" stroke="{color}" stroke-width="{L_STROKE}"',
        "fade_down": f'fill="none" stroke="url(#lgd{color[1:]})" stroke-width="{L_STROKE}"',
        "fade_up": f'fill="none" stroke="url(#lgu{color[1:]})" stroke-width="{L_STROKE}"',
    }[paint]
    s = ""
    for ch in text:
        s += use(f"l{ch}", 0, 0, f'transform="translate({f(x)} {f(top)}) scale({k:.4f})" {attr}')
        x += L_PITCH * h
    return s


L_STYLES = {
    "outline": ("outline", "outline"),
    "fade_down": ("fade_down", "fade_down"),
    "fade_up": ("fade_up", "fade_up"),
    "fill": ("fill", "fill"),
    "hour_fill": ("fill", "outline"),
    "minute_fill": ("outline", "fill"),
}


def lh_clock_w(r):
    return 4 * L_PITCH * r + 10 - (L_PITCH - L_INK) * r


def lh_clock(cx, top, r, style="outline", hc=GE, mc=LAV, hh="10", mm="08"):
    """Giờ và phút một hàng, mỗi cặp hai ô rộng cố định; nét chữ của giờ và phút cách nhau 10 dp như layout mẫu."""
    hp, mp = L_STYLES[style]
    x = cx - lh_clock_w(r) / 2
    s = lh_digits(hh, x, top, r, hc, hp)
    return s + lh_digits(mm, x + 2 * L_PITCH * r + 10 - (L_PITCH - L_INK) * r, top, r, mc, mp)


def lh_date(cx, cap_top, size, color=GE, weather=False):
    """Thứ / ngày in hoa giãn chữ 0,48 em; thời tiết (nếu có) cùng kiểu chữ đứng sau."""
    base = cap_top + 0.72 * size
    track = 0.48 * size
    dw = len(L_DATE) * (0.66 + 0.48) * size - track
    if not weather:
        return txt(cx + track / 2, base, L_DATE, size, color, 400, "middle", f'letter-spacing="{f(track)}"')
    ww = 3 * (0.62 + 0.48) * size
    x = cx - (dw + 16 + 26 + 6 + ww) / 2
    return (txt(x, base, L_DATE, size, color, 400, "start", f'letter-spacing="{f(track)}"')
            + cloud(x + dw + 16, base + 1, 0.55, color, 1.3) + txt(x + dw + 16 + 32, base, "24°", size, color, 400, "start", f'letter-spacing="{f(track)}"'))


def lh_status(cx, cy, color=GE, n=3):
    """Dòng pin của layout mẫu: pin 15 dp, cách 5, chữ 14 sp giãn 0,1 em; chấm 13 sp cách hai bên 15; icon thông báo 17 dp, cách 15."""
    tw = 3 * 0.62 * 14 + 2 * 1.4
    w = 8.4 + 5 + tw + (15 + 4 + 15 + n * 32 - 15 if n else 0)
    x = cx - w / 2
    s = ex_battery(x, cy - 7.5, color) + txt(x + 13.4, cy + 5, "72%", 14, color, 400, extra='letter-spacing="1.4"')
    if n:
        x += 8.4 + 5 + tw + 15
        s += f'<circle cx="{f(x + 2)}" cy="{f(cy)}" r="2" fill="{color}"/>'
        x += 4 + 15
        shapes = icon_shapes(color)
        for i in range(n):
            s += shapes[i % 3](x, cy - 8.5, 17)
            x += 17 + 15
    return s


def lh_player(w=PW, color=GE, title="That Cool Song"):
    """Trình phát của layout mẫu: lùi · tên 13 sp / nghệ sĩ 11,5 sp giãn 0,38 em (giữa) · tới, mỗi nút ô 40 dp cách mép 20; thanh tiến trình
    mảnh cách 30 dp, lùi vào 30 dp hai bên."""
    s = ""
    for bx, d in ((40, -1), (w - 40, 1)):
        s += (f'<path d="M{f(bx - 5 * d)},14 L{f(bx + 4 * d)},20 L{f(bx - 5 * d)},26 Z" fill="{color}" stroke="{color}" stroke-width="1.2" '
              f'stroke-linejoin="round"/><rect x="{f(bx + 5 * d - (1.8 if d < 0 else 0))}" y="14" width="1.8" height="12" rx=".8" fill="{color}"/>')
    s += txt(w / 2 + 2.5, 16, title, 13, color, 400, "middle", 'letter-spacing="4.9"')
    s += txt(w / 2 + 2.2, 37, "It's Artist", 11.5, color, 400, "middle", 'letter-spacing="4.4" fill-opacity=".8"')
    y = 72
    s += f'<line x1="30" y1="{y}" x2="{f(w - 30)}" y2="{y}" stroke="{color}" stroke-opacity=".3" stroke-width="3" stroke-linecap="round"/>'
    return s + f'<line x1="30" y1="{y}" x2="{f(30 + (w - 60) * .42)}" y2="{y}" stroke="{color}" stroke-width="3" stroke-linecap="round"/>'


def lh_column(style="outline", hc=GE, mc=LAV, weather=False, extras=None, media=False, n=3):
    """Màn dọc như layout mẫu: đồng hồ sát trên (thanh trạng thái + 20 dp) → ngày cách 30 dp; nhạc và dòng pin neo dưới; khối thêm ở giữa."""
    cx = PW / 2
    s = lh_clock(cx, L_TOP, LR, style, hc, mc)
    date_top = L_TOP + LR + 30
    s += lh_date(cx, date_top, 13, weather=weather)
    if extras:
        s += extras(date_top + 13 + 34)
    if media:
        s += use("lply", 0, L_STATUS_CY - 7.5 - 28 - 74)
    return s + lh_status(cx, L_STATUS_CY, n=n)


def lh_extras(y):
    return (f'<g transform="translate(64 {f(y + 14)})">{events_list(3)}</g>'
            f'<g transform="translate(64 {f(y + 100)})">{memo_icon()}</g>')


def lh_hero(cx, cy, style="outline", weather=False, n=3):
    """Màn ngang: đồng hồ R = 251 dp, ngày 12 sp cách 20 dp, dòng pin; tâm cụm ở (cx, cy)."""
    r = LRL
    total = r + 20 + 8.6 + 26 + 15
    top = cy - total / 2
    s = lh_clock(cx, top, r, style)
    s += lh_date(cx, top + r + 20, 12, weather=weather)
    return s + lh_status(cx, top + r + 20 + 8.6 + 26 + 7.5, n=n)


def lh_styles_strip():
    r, gap = 136, 14
    w = lh_clock_w(r)
    x0 = (W - 6 * w - 5 * gap) / 2
    labels = ["1 · Viền", "2 · Nhạt dần xuống", "3 · Nhạt dần lên", "4 · Đặc", "5 · Giờ đặc", "6 · Phút đặc"]
    s = ""
    for i, (style, label) in enumerate(zip(L_STYLES, labels)):
        cx = x0 + i * (w + gap) + w / 2
        s += lh_clock(cx, 26, r, style) + txt(cx, 26 + r + 30, label, 14, DM, 400, "middle")
    return (f'<p style="font-size:13px;color:var(--text-secondary);margin:1.25rem 0 6px">Sáu kiểu đồng hồ (mặc định kiểu 1)</p>'
            f'<svg viewBox="0 0 {W} 210" width="100%" role="img" aria-label="Sáu kiểu đồng hồ" '
            f'style="display:block;font-family:\'Plus Jakarta Sans\',sans-serif" fill="{G}">'
            f'<rect width="{W}" height="210" rx="20" fill="#000"/>{s}</svg>')


tower_defs = (lh_gradients([GE, LAV]) + "".join(lh_digit_glyph(ch) for ch in "0189")
              + f'<g id="lply">{lh_player()}</g><g id="lply2">{lh_player(340)}</g><g id="lply3">{lh_player(262, title="That Cool…")}</g>')
tower = [
    lh_styles_strip(),
    '<div style="font-size:0">',
    frame_portrait("D1 · Dọc, kiểu viền (mặc định)", lh_column()),
    frame_portrait("D2 · Dọc, nhạt dần xuống, đang phát nhạc", lh_column("fade_down", media=True)),
    frame_portrait("D3 · Dọc, thêm khối (thời tiết cạnh ngày, sự kiện, ghi nhớ) + nhạc",
                   lh_column(weather=True, extras=lh_extras, media=True)),
    frame_portrait("D4 · Dọc, giờ đặc phút viền, đổi màu", lh_column("hour_fill", hc=MINT, mc=GE)),
    '</div>',
    frame("N1 · Ngang, tối giản (thời tiết cạnh ngày)", lh_hero(W / 2, CY, weather=True)),
    frame("N2 · Ngang, nhạc", lh_hero(236, CY) + vline(436) + use("lply2", 480, CY - 37)),
    frame("N3 · Ngang, lịch trình", lh_hero(236, CY) + vline(436, 96, 296) + f'<g fill="{G}" transform="translate(498 128)">{weather_header()}</g>'
          + f'<g transform="translate(498 178)">{events_list(3)}</g>' + f'<g transform="translate(498 270)">{memo_icon()}</g>'),
    frame("N4 · Ngang, nhạc + nhóm khác (ba cột)", f'<g fill="{G}" transform="translate(36 140)">{weather_header()}</g>'
          + f'<g transform="translate(36 186)">{events_list(3)}</g>' + vline(300, 96, 296) + lh_hero(W / 2, CY, "fade_down")
          + vline(573, 96, 296) + use("lply3", 590, CY - 37)),
]

# ================================================================== CHUỖI (Bullet List của Muviz Edge; số đo lấy từ layout aug23_screen_layout dọc / ngang,
# Aug23Clock và thông số font trong APK, không từ ảnh chụp). Đồng hồ là viên thuốc tô màu, chữ số khoét rỗng (thấy nền): view rộng 100 dp × Cỡ,
# cao một nửa; chữ Anton (font của app mẫu, bản Google Fonts) cỡ 0,58 chiều cao view, giãn 0,04 em; viên thuốc rộng = chữ + 2,8 × 0,11 rộng
# view, cao = chữ số + 2 × 0,11 rộng view, bo tròn hết. Danh sách: chấm 6 dp, vạch 1 dp nối xuống chấm sau, tiêu đề in hoa Anton 18 sp giãn
# 0,03 em, dòng phụ 13 sp mờ 0,7; mỗi dòng cao ít nhất 60 dp, cách nhau 2 dp.
BL_FONT = TTFont(ROOT / "app/src/main/res/font/anton_regular.ttf")
BL_GLYPHS = BL_FONT.getGlyphSet()
BL_CMAP = BL_FONT.getBestCmap()
BL_UPM = BL_FONT["head"].unitsPerEm


def bl_bounds(ch):
    pen = BoundsPen(BL_GLYPHS)
    BL_GLYPHS[BL_CMAP[ord(ch)]].draw(pen)
    return pen.bounds


BL_DIGIT = (bl_bounds("0")[3] - bl_bounds("0")[1]) / BL_UPM
BL_TEXT = 0.58 * 0.875 / BL_DIGIT
BL_CAP = 0.859 * 18
BL_TITLE, BL_SUB = 18, 13
BL_COLOR = DATE_GREY
BL_DATE, BL_DAY = "17 THÁNG 10, 2026", "THỨ BẢY"
BL_TOP = 0.13 * PH
BL_PLAYER_TOP = PH - 40 - 25 - 63
BL_USED = set()
BL_MASKS = [0]


def bl_glyph(ch):
    k = 100 / BL_UPM
    pen = SVGPathPen(BL_GLYPHS, ntos=lambda v: str(round(v)))  # đơn vị 0,01 em: làm tròn số nguyên vẫn nét ở cỡ ≤ 40 dp, file gọn một nửa
    BL_GLYPHS[BL_CMAP[ord(ch)]].draw(TransformPen(pen, (k, 0, 0, -k, 0, 0)))
    return f'<path id="b{ord(ch)}" d="{pen.getCommands()}"/>'


def bl_adv(ch):
    return BL_FONT["hmtx"][BL_CMAP[ord(ch)]][0] / BL_UPM


def bl_width(text, size, track=0.03):
    return sum(bl_adv(ch) for ch in text) * size + track * size * (len(text) - 1)


def bl_text(x, base, text, size, color, track=0.03, anchor="start", opacity=1.0):
    """Chữ Anton lấy đường viền thật từ font (mỗi ký tự một <path> trong defs, dùng lại bằng <use>)."""
    if anchor == "middle":
        x -= bl_width(text, size, track) / 2
    s, u = "", 0.0
    for ch in text:
        if ch != " ":
            BL_USED.add(ch)
            s += f'<use href="#b{ord(ch)}" x="{round(u)}"/>'
        u += (bl_adv(ch) + track) * 100
    op = "" if opacity == 1 else f' fill-opacity="{opacity}"'
    return f'<g fill="{color}"{op} transform="translate({f(x)} {f(base)}) scale({size / 100:.4f})">{s}</g>'


def bl_ink(text, size, track):
    x, lo, hi = 0.0, None, None
    for ch in text:
        b = bl_bounds(ch)
        if b:
            lo = x + b[0] / BL_UPM * size if lo is None else min(lo, x + b[0] / BL_UPM * size)
            hi = x + b[2] / BL_UPM * size if hi is None else max(hi, x + b[2] / BL_UPM * size)
        x += (bl_adv(ch) + track) * size
    return lo, hi


def bl_pill_w(s=1.0, text="10:08"):
    lo, hi = bl_ink(text, BL_TEXT * 50 * s, 0.04)
    return hi - lo + 2.8 * 11 * s


def bl_pill(x, top, s=1.0, text="10:08", color=BL_COLOR):
    """Viên thuốc như Aug23Clock: đặt sát trái view (rộng 100 dp × Cỡ, cao một nửa), giữa theo chiều cao view; chữ số là lỗ (mask)."""
    vh, pad = 50 * s, 11 * s
    size = BL_TEXT * vh
    lo, hi = bl_ink(text, size, 0.04)
    pw, ph = hi - lo + 2.8 * pad, BL_DIGIT * size + 2 * pad
    py = top + vh / 2 - ph / 2
    BL_MASKS[0] += 1
    mid = f"blm{BL_MASKS[0]}"
    digits = bl_text(x + 1.4 * pad - lo, top + vh / 2 + BL_DIGIT * size / 2, text, size, "#000", 0.04)
    return (f'<mask id="{mid}" maskUnits="userSpaceOnUse" x="{f(x - 2)}" y="{f(py - 2)}" width="{f(pw + 4)}" height="{f(ph + 4)}">'
            f'<rect x="{f(x - 2)}" y="{f(py - 2)}" width="{f(pw + 4)}" height="{f(ph + 4)}" fill="#fff"/>{digits}</mask>'
            f'<rect x="{f(x)}" y="{f(py)}" width="{f(pw)}" height="{f(ph)}" rx="{f(ph / 2)}" fill="{color}" mask="url(#{mid})"/>')


def bl_rows(x, top, rows):
    """Chấm 6 dp cách đỉnh dòng 4 dp; vạch 1 dp từ dưới chấm 5 dp kéo tới trên chấm sau 6 dp; dòng cuối không có vạch. Dòng cao = nội dung + 19 dp
    (dòng tiêu đề + dòng phụ ra đúng 60 dp như layout mẫu, dòng icon thông báo thấp hơn để không hở khi còn dòng sau). Mỗi dòng: (màu, cao nội
    dung, rộng, hàm vẽ tại (x chữ, đỉnh dòng))."""
    s, y = "", top
    for i, (color, h, _, draw) in enumerate(rows):
        last = i == len(rows) - 1
        rh = max(15, h) if last else max(41, h + 19)
        s += f'<circle cx="{f(x + 3)}" cy="{f(y + 7)}" r="3" fill="{color}"/>'
        if not last:
            s += f'<rect x="{f(x + 2.5)}" y="{f(y + 15)}" width="1" height="{f(rh + 2 - 2 - 15)}" fill="{color}"/>'
        s += draw(x + 16, y)
        y += rh + 2
    return s, y - 2 - top


def bl_sub(x, top, text, color):
    return txt(x, top + 13.5, text, BL_SUB, color, 400, extra='fill-opacity=".7"')


def bl_title_row(title, sub, color=BL_COLOR, weather=False):
    desc = 2.6 if "," in title else 0
    tw = bl_width(title, BL_TITLE)

    def draw(tx, top):
        s = bl_text(tx, top + BL_CAP, title, BL_TITLE, color)
        if weather:
            wx, cy = tx + tw + 12, top + BL_CAP / 2
            s += cloud(wx, cy + 6, 0.6, G, 1.4) + txt(wx + 28, cy + 5, "24°", 14)
        return s + bl_sub(tx, top + BL_CAP + desc + 8, sub, color)
    return color, BL_CAP + desc + 8 + 16.4, 16 + max(tw + (66 if weather else 0), len(sub) * 0.6 * BL_SUB), draw


def bl_date_row(weather=False, color=BL_COLOR):
    return bl_title_row(BL_DATE, BL_DAY, color, weather)


def bl_battery_row(state="KHÔNG SẠC", color=BL_COLOR):
    return bl_title_row("PIN 72%", state, color)


def bl_icons_row(color=BL_COLOR, n=3):
    shapes = icon_shapes(color)
    return color, 22, 16 + n * 28 - 10, lambda tx, top: "".join(shapes[i % 3](tx + i * 28, top, 18) for i in range(n))


def bl_latest_row(color=BL_COLOR):
    """Thông báo mới nhất như notify_lt của app mẫu: icon 18 dp + tiêu đề in hoa, dưới là nội dung tối đa hai dòng, mờ 0,7."""
    def draw(tx, top):
        s = (f'<circle cx="{f(tx + 9)}" cy="{f(top + 9)}" r="8.2" fill="none" stroke="{color}" stroke-width="1.5"/>'
             + txt(tx + 9, top + 13, "L", 11, color, 700, "middle") + bl_text(tx + 26, top + 9 + BL_CAP / 2, "LAN", BL_TITLE, color))
        return (s + bl_sub(tx, top + 26, "TỐI NAY ĐI ĂN LẨU KHÔNG? MÌNH ĐẶT", color)
                + bl_sub(tx, top + 26 + 16.4, "BÀN LÚC 7 GIỜ NHÉ", color))
    return color, 18 + 8 + 2 * 16.4, 270, draw


# Icon đặc Phosphor của app (res/drawable/ic_calendar_check_fill.xml, ic_note_pencil_fill.xml), khung 256.
BL_CAL = ("M208,32H184V24a8,8,0,0,0-16,0v8H88V24a8,8,0,0,0-16,0v8H48A16,16,0,0,0,32,48V208a16,16,0,0,0,16,16H208a16,16,0,0,0,16-16V48A16,16,"
          "0,0,0,208,32ZM169.66,133.66l-48,48a8,8,0,0,1-11.32,0l-24-24a8,8,0,0,1,11.32-11.32L116,164.69l42.34-42.35a8,8,0,0,1,11.32,11.32ZM48,"
          "80V48H72v8a8,8,0,0,0,16,0V48h80v8a8,8,0,0,0,16,0V48h24V80Z")
BL_MEMO = ("M224,128v80a16,16,0,0,1-16,16H48a16,16,0,0,1-16-16V48A16,16,0,0,1,48,32h80a8,8,0,0,1,0,16H48V208H208V128a8,8,0,0,1,16,0Zm5.66-58.34"
           "-96,96A8,8,0,0,1,128,168H96a8,8,0,0,1-8-8V128a8,8,0,0,1,2.34-5.66l96-96a8,8,0,0,1,11.32,0l32,32A8,8,0,0,1,229.66,69.66Zm-17-5.66"
           "L192,43.31,179.31,56,200,76.69Z")


def bl_icon_row(ref, title, subs, color=BL_COLOR):
    """Ghi nhớ, sự kiện do Chuỗi tự vẽ như dòng thông báo mới nhất: icon đặc 20 dp sáng như icon thông báo + tiêu đề in hoa đậm như ngày / pin,
    dưới là các dòng phụ mờ 0,7."""
    def draw(tx, top):
        s = (f'<use href="#{ref}" transform="translate({f(tx - 1)} {f(top + BL_CAP / 2 - 10)}) scale({20 / 256:.4f})" fill="{color}"/>'
             + bl_text(tx + 26, top + BL_CAP, title, BL_TITLE, color))
        for i, sub in enumerate(subs):
            s += bl_sub(tx, top + BL_CAP + 8 + i * 16.4, sub, color)
        return s
    w = max(26 + bl_width(title, BL_TITLE), max(len(sub) for sub in subs) * 0.6 * BL_SUB)
    return color, BL_CAP + 8 + len(subs) * 16.4, 16 + w, draw


def bl_memo_row():
    return bl_icon_row("blmemo", "GHI NHỚ", ["MUA SỮA TRÊN ĐƯỜNG VỀ"])


def bl_events_row():
    """Sự kiện gần nhất làm tiêu đề, các sự kiện sau mỗi cái một dòng phụ."""
    return bl_icon_row("blcal", "09:00  HỌP NHÓM THIẾT KẾ", ["12:30  ĂN TRƯA VỚI LAN", "19:00  TẬP GYM"])


def bl_player(w=313, color=BL_COLOR, title="THAT COOL SONG"):
    """Trình phát của layout mẫu: lùi 35 dp · tên in hoa 19 sp giãn 0,05 em / nghệ sĩ 13 sp mờ 0,7 (giữa, chạm để phát / dừng) · tới 35 dp; thanh
    tiến trình 1,5 dp cách 15 dp, nền cùng màu mờ 0,3."""
    cy = 23.25
    s = ""
    for bx, d in ((17.5, -1), (w - 17.5, 1)):
        s += (f'<path d="M{f(bx - 4.5 * d)},{f(cy - 6)} L{f(bx + 3.5 * d)},{f(cy)} L{f(bx - 4.5 * d)},{f(cy + 6)} Z" fill="{color}" '
              f'stroke="{color}" stroke-width="1.2" stroke-linejoin="round"/>'
              f'<rect x="{f(bx + 4.5 * d - (1.8 if d < 0 else 0))}" y="{f(cy - 6)}" width="1.8" height="12" rx=".8" fill="{color}"/>')
    s += bl_text(w / 2, 20, title, BL_TITLE, color, 0.05, "middle")
    s += txt(w / 2, 40, "IT'S ARTIST", BL_SUB, color, 400, "middle", 'letter-spacing=".26" fill-opacity=".7"')
    y = 62.25
    s += f'<line x1="0" y1="{y}" x2="{f(w)}" y2="{y}" stroke="{color}" stroke-opacity=".3" stroke-width="1.5"/>'
    return s + f'<line x1="0" y1="{y}" x2="{f(w * .42)}" y2="{y}" stroke="{color}" stroke-width="1.5"/>'


def bl_column(rows, s=1.0, text="10:08", clock=BL_COLOR, media=False):
    """Màn dọc như layout mẫu: khoảng trống 0,13 chiều cao → đồng hồ (lề trái 30 dp) → cách 30 dp → danh sách; nhạc neo đáy (đệm 40 + 25 dp)."""
    body, _ = bl_rows(30, BL_TOP + 50 * s + 30, rows)
    return bl_pill(30, BL_TOP, s, text, clock) + body + (use("blply", 40, BL_PLAYER_TOP) if media else "")


def bl_hero(cx, cy, rows, s=1.0, text="10:08"):
    """Màn ngang: đồng hồ + danh sách (khối của chủ đề) làm một cụm căn trái, cả cụm giữa ô (cx, cy)."""
    _, lh = bl_rows(0, 0, rows)
    w = max(bl_pill_w(s, text), max(r[2] for r in rows))
    h = 50 * s + 30 + lh
    x, top = cx - w / 2, cy - h / 2
    body, _ = bl_rows(x, top + 50 * s + 30, rows)
    return bl_pill(x, top, s, text) + body, w


def bl_strip():
    s = ""
    for x, sz, label in ((40, 0.7, "Cỡ 70%"), (170, 1.0, "100% (mặc định)"), (330, 1.3, "130%")):
        s += bl_pill(x, 75 - 25 * sz, sz) + txt(x, 132, label, 13, DM)
    s += ('<defs><linearGradient id="blwp" x1="0" y1="0" x2="1" y2="1"><stop offset="0" stop-color="#2E6B5A"/>'
          '<stop offset=".55" stop-color="#3B2C66"/><stop offset="1" stop-color="#7A3E5C"/></linearGradient></defs>'
          f'<rect x="540" y="14" width="300" height="100" rx="16" fill="url(#blwp)"/>')
    s += bl_pill(640, 39, 1.0, "21:47") + txt(540, 132, "Trên ảnh nền: chữ số khoét rỗng nên thấy ảnh", 13, DM)
    return (f'<p style="font-size:13px;color:var(--text-secondary);margin:1.25rem 0 6px">Đồng hồ viên thuốc (Cỡ 70–130%, mặc định 100% = cỡ 50 của '
            f'app mẫu)</p><svg viewBox="0 0 {W} 150" width="100%" role="img" aria-label="Đồng hồ viên thuốc" '
            f'style="display:block;font-family:\'Plus Jakarta Sans\',sans-serif" fill="{G}">'
            f'<rect width="{W}" height="150" rx="20" fill="#000"/>{s}</svg>')


def bl_info_chain(x, cy):
    """Cột ghi nhớ, sự kiện của màn ngang cũng là chuỗi như màn dọc (chấm + vạch nối, cùng kiểu dòng), giữa theo chiều cao."""
    rows = [bl_memo_row(), bl_events_row()]
    _, h = bl_rows(0, 0, rows)
    body, _ = bl_rows(x, cy - h / 2, rows)
    return body, max(r[2] for r in rows)


def bl_three(rows):
    """Ba cột từ trái sang: cụm Chuỗi | chuỗi khối dùng chung | nhạc (lấy phần còn lại); cả hàng cách mép 24 dp, giữa hai cột vạch ngăn."""
    _, hw = bl_hero(0, CY, rows)
    _, iw = bl_info_chain(0, CY)
    a = 24 + hw + 32
    b = a + 32 + iw + 32
    hero, _ = bl_hero(24 + hw / 2, CY, rows)
    info, _ = bl_info_chain(a + 32, CY)
    return (hero + vline(a, 96, 296) + info + vline(b, 96, 296)
            + f'<g transform="translate({f(b + 32)} {f(CY - 31.5)})">{bl_player(W - 24 - b - 32, title="THAT COOL…")}</g>')


def bl_two(rows, end):
    hero, w = bl_hero(236, CY, rows)
    return hero + vline(436, 96, 296) + end


chain = [
    bl_strip(),
    '<div style="font-size:0">',
    frame_portrait("D1 · Dọc, mặc định như app mẫu: ngày, pin, icon thông báo", bl_column([bl_date_row(), bl_battery_row(), bl_icons_row()])),
    frame_portrait("D2 · Dọc, thêm thời tiết cạnh ngày; ghi nhớ, sự kiện dưới thông báo (icon đặc, chữ đậm như ngày / pin) + nhạc",
                   bl_column([bl_date_row(True), bl_battery_row(), bl_icons_row(), bl_memo_row(), bl_events_row()], media=True)),
    frame_portrait("D3 · Dọc, bật nội dung thông báo (thông báo mới nhất ở dòng cuối), đang sạc, nhạc",
                   bl_column([bl_date_row(), bl_battery_row("ĐANG SẠC"), bl_latest_row()], text="21:47", media=True)),
    frame_portrait("D4 · Dọc, cỡ 130%, đồng hồ màu mint, tắt ngày: chuỗi tự nối lại",
                   bl_column([bl_battery_row(), bl_icons_row()], s=1.3, text="07:15", clock=MINT)),
    '</div>',
    frame("N1 · Ngang, tối giản (thời tiết cạnh ngày)", bl_hero(W / 2, CY, [bl_date_row(True), bl_battery_row(), bl_icons_row()])[0]),
    frame("N2 · Ngang, nhạc", bl_two([bl_date_row(True), bl_battery_row(), bl_icons_row()], use("blply2", 480, CY - 31.5))),
    frame("N3 · Ngang, ghi nhớ + lịch trình: cột phải cũng là chuỗi có chấm nối như màn dọc",
          bl_two([bl_date_row(True), bl_battery_row(), bl_icons_row()], bl_info_chain(480, CY)[0])),
    frame("N4 · Ngang, ba cột: Chuỗi → ghi nhớ, sự kiện (chuỗi có chấm) → nhạc",
          bl_three([bl_date_row(True), bl_battery_row(), bl_icons_row()])),
]
chain_defs = (f'<path id="blcal" d="{BL_CAL}"/><path id="blmemo" d="{BL_MEMO}"/>'
              f'<g id="blply">{bl_player()}</g><g id="blply2">{bl_player(340)}</g>'
              + "".join(bl_glyph(ch) for ch in sorted(BL_USED)))

# ================================================================== VÀNH (iOS Fill Clock của Muviz Edge; số đo lấy từ layout aug24_screen_layout dọc / ngang
# và IosFillClock trong APK, không từ ảnh chụp). Mặt số kim phủ cả màn: vạch là tia từ tâm cắt trong dải khung chữ nhật sát mép (khung ngoài lùi
# 0,06 nửa cạnh ngắn ngang / 0,03 nửa cạnh dài dọc; vạch phút tới khung lùi 0,2 / 0,1; vạch giờ dài tới khung lùi 0,5 / 0,22, bỏ vạch dài chỗ có
# số), nét 0,018 / 0,012 nửa cạnh ngắn (vạch phút mờ 70 / 255). Số 3 / 6 / 9 / 12 Zalando Sans Expanded SemiBold cỡ 0,14 cạnh ngắn, cách tâm
# 0,6 nửa cạnh ngắn (cặp số ở cạnh dài) / 0,68 nửa cạnh dài. Kim: đoạn mảnh 0,0148 tới 0,2, đoạn dày 0,0328 từ 0,15 tới 0,53 (giờ) / 0,83
# (phút), có bóng mờ; chấm tâm 0,0295 + lỗ đen + vòng màu nhấn. Mặt đồng hồ tối giản nên chỉ có ngày (Google Sans Bold 20 sp, + thời tiết) phía
# số 12 và pin · thông báo + nhạc phía số 6; ghi nhớ, sự kiện, nét vẽ, nội dung thông báo không hỗ trợ. Màn ngang mặt số luôn phủ màn như layout
# mẫu (không thu nhỏ, không chia cột), hai nhóm giữa chiều cao, nhóm trái từ 0,25 bề ngang, nhóm phải thẳng phải ở 0,75: không nhạc thì pin ·
# thông báo trái, ngày phải như app mẫu; có nhạc thì ngày + pin xếp dọc bên trái, trình phát bên phải.
class BzFont:
    """Đo chữ bằng font thật (fontTools) để căn vị trí, vẽ bằng <text> với cùng font tải từ Google Fonts (class CSS css_class): file gọn hơn
    đường viền từng ký tự nhiều lần."""

    def __init__(self, font, css_class):
        self.font, self.css_class = font, css_class
        self.glyphs, self.cmap, self.upm = font.getGlyphSet(), font.getBestCmap(), font["head"].unitsPerEm

    def bounds(self, ch):
        pen = BoundsPen(self.glyphs)
        self.glyphs[self.cmap[ord(ch)]].draw(pen)
        return pen.bounds

    def adv(self, ch):
        return self.font["hmtx"][self.cmap[ord(ch)]][0] / self.upm

    def width(self, text, size):
        return sum(self.adv(ch) for ch in text) * size

    def ink(self, text, size):
        """(trái, trên, phải, dưới) của nét chữ so với gốc chữ (y xuống)."""
        x, xs, ys = 0.0, [], []
        for ch in text:
            b = self.bounds(ch)
            if b:
                xs += [x + b[0] / self.upm * size, x + b[2] / self.upm * size]
                ys += [-b[3] / self.upm * size, -b[1] / self.upm * size]
            x += self.adv(ch) * size
        return min(xs), min(ys), max(xs), max(ys)

    def text(self, x, base, text, size, color, anchor="start", opacity=1.0):
        if anchor != "start":
            x -= self.width(text, size) / (2 if anchor == "middle" else 1)
        op = "" if opacity == 1 else f' fill-opacity="{opacity}"'
        return f'<text class="{self.css_class}" x="{f(x)}" y="{f(base)}" font-size="{f(size)}" fill="{color}"{op}>{text}</text>'

    def fit(self, text, size, w):
        while self.width(text, size) > w and len(text) > 1:
            text = text[:-2].rstrip() + "…"
        return text



BZ_NUM = BzFont(TTFont(ROOT / "tools/fonts/static/zalando_sans_expanded_semibold.ttf"), "bzn")
BZ_TEXT = BzFont(instancer.instantiateVariableFont(TTFont(ROOT / "tools/fonts/variable/google_sans_variable.ttf"),
                                                   {"wght": 700, "GRAD": 0, "opsz": 18}, inplace=False), "bzt")
BZ_FONTS = ('<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Google+Sans:wght@700'
            '&family=Zalando+Sans+Expanded:wght@600&display=swap"><style>.bzt{font-family:"Google Sans";font-weight:700}'
            '.bzn{font-family:"Zalando Sans Expanded";font-weight:600}</style>')
BZ_ASC = 0.966  # ascent Google Sans: đỉnh dòng chữ → đường gốc
BZ_MAIN = GE
BZ_ACCENT = "#80DEEA"  # màu nhấn đề xuất (app mẫu #E8FF82 trùng Lime của Viên nang)
BZ_LIME = "#EAFC82"
BZ_DAY, BZ_DATE = "THỨ BẢY", "17"
BZ_G = 18.0  # dòng trạng thái: icon pin / thông báo cao 18 dp như layout mẫu
BZ_ZONE = 184  # chữ ở vùng trên / dưới kim của màn dọc rộng tối đa 184 dp (dài hơn thì "…"): không chạm đầu vạch giờ dài
BZ_FACES = {}


def bz_ray(cx, cy, deg, r0, r1, width, color, opacity=1.0):
    a = math.radians(deg)
    dx, dy = math.sin(a), -math.cos(a)
    op = "" if opacity == 1 else f' stroke-opacity="{opacity:.3f}"'
    return (f'<line x1="{f(cx + dx * r0)}" y1="{f(cy + dy * r0)}" x2="{f(cx + dx * r1)}" y2="{f(cy + dy * r1)}" stroke="{color}" '
            f'stroke-width="{width:.2f}"{op}/>')


def bz_rays(cx, cy, degs, r0, r1, width, color, opacity=1.0):
    """Nhiều tia cùng nét gộp một <path> (file gọn: mỗi mặt số 70 tia)."""
    d = ""
    for deg in degs:
        a = math.radians(deg)
        dx, dy = math.sin(a), -math.cos(a)
        d += f"M{f(cx + dx * r0)},{f(cy + dy * r0)}L{f(cx + dx * r1)},{f(cy + dy * r1)}"
    op = "" if opacity == 1 else f' stroke-opacity="{opacity:.3f}"'
    return f'<path d="{d}" stroke="{color}" stroke-width="{width:.2f}"{op}/>'


def bz_number_box(w, h, num):
    """Tâm và cỡ số num trên mặt số w × h."""
    fmin, fmax, land = min(w, h) / 2, max(w, h) / 2, w > h
    deg = {"3": 90, "6": 180, "9": 270, "12": 0}[num]
    d = 0.6 * fmin if (num in ("6", "12")) == land else 0.68 * fmax
    a = math.radians(deg)
    return w / 2 + math.sin(a) * d, h / 2 - math.cos(a) * d, 0.14 * min(w, h)


def bz_face(w, h, style, main, accent):
    """Vạch + số của mặt số w × h tại gốc (không kim), mỗi tổ hợp một <g> trong defs để các khung dùng lại."""
    key = (w, h, style, main, accent)
    if key in BZ_FACES:
        return BZ_FACES[key][0]
    n = f"bzf{len(BZ_FACES)}"
    x0, y0 = 0, 0
    fmin, fmax, land = min(w, h) / 2, max(w, h) / 2, w > h
    cx, cy, reach = w / 2, h / 2, max(w, h)

    def rect(ix, iy):
        return f"M{f(x0 + ix)},{f(y0 + iy)}H{f(x0 + w - ix)}V{f(y0 + h - iy)}H{f(x0 + ix)}Z"

    outer = rect(0.06 * fmin, 0.03 * fmax)
    s = (f'<clipPath id="{n}a"><path clip-rule="evenodd" d="{outer}{rect(0.2 * fmin, 0.1 * fmax)}"/></clipPath>'
         f'<clipPath id="{n}b"><path clip-rule="evenodd" d="{outer}{rect(0.5 * fmin, 0.22 * fmax)}"/></clipPath>')
    ticks = bz_rays(cx, cy, range(0, 360, 30), 0.5 * fmin, reach, 0.018 * fmin, main)
    if style == 1:
        ticks += bz_rays(cx, cy, [i * 6 for i in range(60) if i % 5], 0.5 * fmin, reach, 0.012 * fmin, main, 70 / 255)
    skip = (6, 12) if land else (3, 9)
    longs = bz_rays(cx, cy, [i * 30 for i in range(1, 13) if i not in skip], 0.5 * fmin, reach, 0.018 * fmin, main)
    s += f'<g clip-path="url(#{n}a)">{ticks}</g><g clip-path="url(#{n}b)">{longs}</g>'
    for num in ("3", "6", "9", "12"):
        px, py, size = bz_number_box(w, h, num)
        lo, top, hi, bottom = BZ_NUM.ink(num, size)
        s += BZ_NUM.text(px - (lo + hi) / 2, py - (top + bottom) / 2, num, size, accent)
    BZ_FACES[key] = (n, f'<g id="{n}">{s}</g>')
    return n


def bz_dial(x0, y0, w, h, hh=10, mm=8, style=1, main=BZ_MAIN, accent=BZ_ACCENT):
    fmin, cx, cy = min(w, h) / 2, x0 + w / 2, y0 + h / 2
    s = use(bz_face(w, h, style, main, accent), x0, y0)
    hour, minute = (hh % 12 + mm / 60) * 30, mm * 6
    thin, thick = 0.0148 * fmin, 0.0328 * fmin
    hands = (bz_ray(cx, cy, minute, 0, 0.2 * fmin, thin, main) + bz_ray(cx, cy, minute, 0.15 * fmin, 0.83 * fmin, thick, main)
             + bz_ray(cx, cy, hour, 0, 0.2 * fmin, thin, main) + bz_ray(cx, cy, hour, 0.15 * fmin, 0.53 * fmin, thick, main))
    s += f'<g filter="url(#bzsh)" stroke-linecap="round">{hands}</g>'
    return (s + f'<circle cx="{f(cx)}" cy="{f(cy)}" r="{f(0.0295 * fmin)}" fill="{main}"/>'
            f'<circle cx="{f(cx)}" cy="{f(cy)}" r="{f(0.0131 * fmin)}" fill="#000"/>'
            f'<circle cx="{f(cx)}" cy="{f(cy)}" r="{f(0.0164 * fmin)}" fill="none" stroke="{accent}" stroke-width="{f(0.0131 * fmin)}"/>')


def bz_anchor(x, w, anchor):
    return x - (w / 2 if anchor == "middle" else w if anchor == "end" else 0)


def bz_date(x, top, anchor="middle", weather=False, main=BZ_MAIN):
    """Thứ + số ngày cách 7 dp như layout mẫu, cùng màu ngày (chỉnh cả hai ở tab Ngày); thời tiết cạnh ngày cùng cỡ, cùng độ đậm. Cao 25 dp."""
    size, base = 20, top + BZ_ASC * 20
    wd, wn = BZ_TEXT.width(BZ_DAY, size), BZ_TEXT.width(BZ_DATE, size)
    ww = 12 + 36 + BZ_TEXT.width("24°", size) if weather else 0
    x = bz_anchor(x, wd + 7 + wn + ww, anchor)
    s = BZ_TEXT.text(x, base, BZ_DAY, size, main) + BZ_TEXT.text(x + wd + 7, base, BZ_DATE, size, main)
    if weather:
        wx = x + wd + 7 + wn + 12
        s += cloud(wx - 2, base, 0.8, main, 2.4) + BZ_TEXT.text(wx + 36, base, "24°", size, main)
    return s


def bz_battery(x, top, g, color, charging=False):
    w = 0.522 * g
    sw, cap_h, cap_w = 0.14 * w, 0.1 * g, 0.44 * w
    bt, bh = top + cap_h, g - cap_h
    s = f'<rect x="{f(x + (w - cap_w) / 2)}" y="{f(top)}" width="{f(cap_w)}" height="{f(cap_h + sw / 2)}" rx="{f(sw / 2)}" fill="{color}"/>'
    if charging:
        bolt = [(.58, .12), (.2, .56), (.46, .56), (.38, .9), (.8, .42), (.54, .42)]
        return (s + f'<rect x="{f(x)}" y="{f(bt)}" width="{f(w)}" height="{f(bh)}" rx="{f(0.25 * w)}" fill="{color}"/>'
                + '<path d="M' + " L".join(f"{f(x + w * px)},{f(bt + bh * py)}" for px, py in bolt) + 'Z" fill="#000"/>')
    pad = sw * 1.6
    fh = (bh - 2 * pad) * 0.72
    return (s + f'<rect x="{f(x + sw / 2)}" y="{f(bt + sw / 2)}" width="{f(w - sw)}" height="{f(bh - sw)}" rx="{f(0.25 * w - sw / 2)}" '
            f'fill="none" stroke="{color}" stroke-width="{f(sw)}"/>'
            f'<rect x="{f(x + pad)}" y="{f(bt + bh - pad - fh)}" width="{f(w - 2 * pad)}" height="{f(fh)}" rx="{f(sw / 2)}" fill="{color}"/>')


def bz_status(x, cy, anchor="middle", color=BZ_MAIN, n=2, charging=False):
    """Dòng trạng thái dùng chung AodStatusLine (pin dọc · icon thông báo) cao 18 dp, chữ Google Sans Bold như layout mẫu."""
    g, fs = BZ_G, 0.915 * BZ_G
    tw = BZ_TEXT.width("72%", fs)
    w = 0.522 * g + 0.717 * g + tw + (1.13 * g + 0.228 * g + 1.065 * g + n * 0.978 * g + (n - 1) * 1.13 * g if n else 0)
    x = bz_anchor(x, w, anchor)
    s = bz_battery(x, cy - g / 2, g, color, charging) + BZ_TEXT.text(x + 1.239 * g, cy + 0.34 * fs, "72%", fs, color)
    if n:
        dx = x + 1.239 * g + tw + 1.13 * g
        s += f'<circle cx="{f(dx + 0.114 * g)}" cy="{f(cy)}" r="{f(0.114 * g)}" fill="{color}"/>'
        shapes = icon_shapes(color)
        ix = dx + 0.228 * g + 1.065 * g
        s += "".join(shapes[i % 3](ix + i * 2.108 * g, cy - 0.489 * g, 0.978 * g) for i in range(n))
    return s


def bz_player(x, top, w, anchor="middle", color=BZ_MAIN, paused=False, title="THAT COOL SONG"):
    """Trình phát gọn của chủ đề: lùi · tên in hoa 14 sp / nghệ sĩ 12 sp mờ 0,7 (chạm để phát / dừng, dừng thì mờ 0,6) · tới; thanh tiến trình
    1,5 dp dưới cùng, nền cùng màu mờ 0,3. Cao 41,5 dp."""
    x, op = bz_anchor(x, w, anchor), 0.6 if paused else 1.0
    cx = x + w / 2
    s = ""
    for bx, d in ((x + 8, -1), (x + w - 8, 1)):
        s += (f'<path d="M{f(bx - 4.5 * d)},{f(top + 9)} L{f(bx + 3.5 * d)},{f(top + 15)} L{f(bx - 4.5 * d)},{f(top + 21)} Z" fill="{color}" '
              f'stroke="{color}" stroke-width="1.2" stroke-linejoin="round"/>'
              f'<rect x="{f(bx + 4.5 * d - (1.8 if d < 0 else 0))}" y="{f(top + 9)}" width="1.8" height="12" rx=".8" fill="{color}"/>')
    s += BZ_TEXT.text(cx, top + 13.5, BZ_TEXT.fit(title, 14, w - 56), 14, color, "middle", op)
    s += BZ_TEXT.text(cx, top + 30, "IT'S ARTIST", 12, color, "middle", 0.7 * op)
    y = top + 40.75
    return (s + f'<line x1="{f(x)}" y1="{f(y)}" x2="{f(x + w)}" y2="{f(y)}" stroke="{color}" stroke-opacity=".3" stroke-width="1.5"/>'
            f'<line x1="{f(x)}" y1="{f(y)}" x2="{f(x + w * .42)}" y2="{f(y)}" stroke="{color}" stroke-width="1.5"/>')


def bz_info(date=True, weather=False):
    """Nhóm phía số 12 ở màn dọc, bên phải ở màn ngang: ngày (+ thời tiết); mỗi khối (cao, hàm vẽ)."""
    return [(25, lambda x, t, a: bz_date(x, t, a, weather))] if date else []


def bz_controls(media=False, n=2, charging=False, paused=False):
    """Nhóm phía số 6 ở màn dọc: dòng pin · thông báo, trình phát."""
    return [(18, lambda x, t, a: bz_status(x, t + 9, a, n=n, charging=charging))] + (bz_music(paused) if media else [])


def bz_music(paused=False):
    return [(41.5, lambda x, t, a: bz_player(x, t, BZ_ZONE, a, paused=paused))]


def bz_stack(x, cy, items, anchor, gap=10):
    y = cy - (sum(h for h, _ in items) + gap * (len(items) - 1)) / 2
    s = ""
    for h, draw in items:
        s += draw(x, y, anchor)
        y += h + gap
    return s


def bz_portrait(hh=10, mm=8, style=1, accent=BZ_ACCENT, date=True, weather=False, media=False, n=2, charging=False, paused=False):
    """Màn dọc: ngày giữa vùng từ số 12 tới đầu kim phút (0,83 nửa cạnh ngắn), nhóm pin + nhạc giữa vùng từ đầu kim tới số 6, nên kim
    không bao giờ đè lên chữ."""
    s = bz_dial(0, 0, PW, PH, hh, mm, style, BZ_MAIN, accent)
    _, y12, size = bz_number_box(PW, PH, "12")
    _, y6, _ = bz_number_box(PW, PH, "6")
    reach = 0.83 * PW / 2
    top0, top1 = y12 + BZ_NUM.ink("12", size)[3] + 16, PH / 2 - reach - 6  # dấu tiếng Việt của thứ cao hơn chữ hoa nên chừa thêm dưới số 12
    bottom0, bottom1 = PH / 2 + reach + 6, y6 + BZ_NUM.ink("6", size)[1] - 6
    info = bz_info(date, weather)
    if info:
        s += bz_stack(PW / 2, (top0 + top1) / 2, info, "middle", 8)
    return s + bz_stack(PW / 2, (bottom0 + bottom1) / 2, bz_controls(media, n, charging, paused), "middle")


def bz_landscape(left, right, hh=10, mm=8, style=1):
    """Màn ngang: mặt số phủ màn như layout mẫu; nhóm trái từ 0,25 bề ngang, nhóm phải thẳng phải ở 0,75, cả hai giữa chiều cao (chữ nằm trong
    tầm kim như app mẫu, kim có bóng mờ nên vẫn đọc được)."""
    return bz_dial(0, 0, W, H, hh, mm, style) + bz_stack(0.25 * W, CY, left, "start") + bz_stack(0.75 * W, CY, right, "end")


def bz_strip():
    s = ('<defs><linearGradient id="bzwp" x1="0" y1="0" x2="1" y2="1"><stop offset="0" stop-color="#2E6B5A"/>'
         '<stop offset=".55" stop-color="#3B2C66"/><stop offset="1" stop-color="#7A3E5C"/></linearGradient></defs>')
    k = 0.4
    for i, (label, kw, wall) in enumerate((("Kiểu 1 · vạch phút (mặc định)", {}, False),
                                            ("Kiểu 2 · chỉ vạch giờ", {"style": 2, "hh": 1, "mm": 50}, False),
                                            ("Số lime như app mẫu", {"accent": BZ_LIME, "hh": 4, "mm": 38}, False),
                                            ("Trên ảnh nền", {"hh": 7, "mm": 21}, True))):
        x = 36 + i * 212
        s += f'<g transform="translate({x} 14) scale({k})">'
        s += (f'<rect width="{PW}" height="{PH}" rx="34" fill="url(#bzwp)"/>' if wall else
              f'<rect width="{PW}" height="{PH}" rx="34" fill="#000" stroke="#333" stroke-width="2"/>')
        s += bz_portrait(**kw) + "</g>" + txt(x + PW * k / 2, 14 + PH * k + 22, label, 13, DM, 400, "middle")
    return (f'<p style="font-size:13px;color:var(--text-secondary);margin:1.25rem 0 6px">Hai kiểu vạch và màu số (cả hai màu chỉnh được)</p>'
            f'<svg viewBox="0 0 {W} 400" width="100%" role="img" aria-label="Hai kiểu vạch và màu số" '
            f'style="display:block;font-family:\'Plus Jakarta Sans\',sans-serif" fill="{G}">'
            f'<rect width="{W}" height="400" rx="20" fill="#111"/>{s}</svg>')


bezel = [
    BZ_FONTS,
    bz_strip(),
    '<div style="font-size:0">',
    frame_portrait("D1 · Dọc, mặc định (cũng là thẻ ở lưới chủ đề): thứ + ngày phía trên kim, pin · thông báo phía dưới", bz_portrait()),
    frame_portrait("D2 · Dọc, thêm thời tiết cạnh ngày và nhạc dưới dòng pin", bz_portrait(weather=True, media=True)),
    frame_portrait("D3 · Dọc, kiểu chỉ vạch giờ, đang sạc, nhạc đang dừng (mờ), 21:47",
                   bz_portrait(hh=21, mm=47, style=2, media=True, charging=True, paused=True, n=1)),
    frame_portrait("D4 · Dọc, số lime như app mẫu, tắt ngày, 4:38: kim không chạm vùng chữ",
                   bz_portrait(hh=4, mm=38, accent=BZ_LIME, date=False, n=3)),
    '</div>',
    frame("N1 · Ngang, mặc định như app mẫu: mặt số phủ màn, pin · thông báo bên trái, ngày + thời tiết bên phải",
          bz_landscape(bz_controls(), bz_info(weather=True))),
    frame("N2 · Ngang, nhạc: ngày + thời tiết, pin · thông báo xếp dọc bên trái, trình phát bên phải",
          bz_landscape(bz_info(weather=True) + bz_controls(), bz_music())),
    frame("N3 · Ngang, kiểu chỉ vạch giờ, nhạc, lúc 9:15: kim đè lên chữ như app mẫu, bóng mờ của kim giữ chữ đọc được",
          bz_landscape(bz_info() + bz_controls(), bz_music(), hh=9, mm=15, style=2)),
]
bezel_defs = ('<filter id="bzsh" filterUnits="userSpaceOnUse" x="-2000" y="-2000" width="5000" height="5000">'
              '<feDropShadow dx="0" dy="0" stdDeviation="1.8" flood-color="#000" flood-opacity=".3"/></filter>'
              + "".join(g for _, g in BZ_FACES.values()))

# ================================================================== LA BÀN (Ambitio Face của Muviz Edge; số đo lấy từ layout may23_screen_layout dọc / ngang
# và AmbitioClock trong APK, không từ ảnh chụp). Mặt số vuông cạnh = cạnh ngắn × cỡ (75% của app mẫu = 100%), u = nửa cạnh: các vòng số đồng tâm
# xoay theo giờ thật (mỗi khung hình như app mẫu) về một đường chỉ cố định nằm ngang từ tâm tới mép phải mặt số (đầu mũi tên 0,05u × ±0,03u ở
# tâm): vòng giờ 1–12 Krona One 0,165u ở 0,84u xen "hh:30" 0,066u, vòng phút 00–55 0,11u ở 0,55u xen vạch 0,475–0,625u (nét 1,3 dp), vòng giây
# 00–55 0,09u ở 0,3u, hai vòng tròn mảnh 0,68u / 0,42u (1 dp, alpha 40). Chữ đặt theo bán kính, đọc ra ngoài; số đang chỉ nằm ngang trên đường
# chỉ. Màu riêng: giờ, giờ rưỡi, phút (vạch giữa số phút cùng màu, mờ 0,6 như màu 2 / màu 1 của app mẫu), giây; đường chỉ theo màu giờ.
# Khối dưới mặt số cách đáy 60 dp như layout mẫu: ngày "dd MMMM yyyy" in hoa Google Sans Bold 14 sp giãn 0,25 em (+ thời tiết), cách 15 dp dòng
# pin · thông báo (thông báo mới nhất thêm một dòng icon + chữ in hoa 12,5 sp), trình phát (lùi · tên / nghệ sĩ in hoa giãn 0,25 em · tới, chạm
# tên để phát / dừng, không thanh tiến trình). Màn ngang: mặt số giữa màn, hai nhóm giữa hai vùng bên.
CP_KRONA = BzFont(TTFont(ROOT / "tools/fonts/static/krona_one_regular.ttf"), "cpk")
CP_BOLD = BzFont(BZ_TEXT.font, "cpb")
CP_REG = BzFont(instancer.instantiateVariableFont(TTFont(ROOT / "tools/fonts/variable/google_sans_variable.ttf"),
                                                  {"wght": 400, "GRAD": 0, "opsz": 18}, inplace=False), "cpr")
CP_FONTS = ('<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Google+Sans:wght@400;700&family=Krona+One'
            '&display=swap"><style>.cpb{font-family:"Google Sans";font-weight:700}.cpr{font-family:"Google Sans";font-weight:400}'
            '.cpk{font-family:"Krona One"}</style>')
CP_ACCENT = "#FFD54F"  # màu nhấn đề xuất: hổ phách, chưa chủ đề nào dùng
CP_HOUR, CP_HALF, CP_MINUTE, CP_SECOND, CP_SUB = CP_ACCENT, "#9A9A9A", "#E0E0E0", "#9A9A9A", "#9A9A9A"
CP_COLORS = (CP_HOUR, CP_HALF, CP_MINUTE, CP_SECOND)  # giờ, giờ rưỡi, phút, giây
CP_REF = ("#CFD8DC", "#90A4AE", "#CFD8DC", "#90A4AE")  # màu 1 / màu 2 của app mẫu
CP_MIX = ("#FFD54F", "#FF8B7B", "#80DEEA", "#C3A6FF")
CP_LIVE = []
CP_DATE = "18 THÁNG 10 2026"
CP_RINGS = {}


def cp_width(font, text, size, ls=0.0):
    return font.width(text, size) + ls * size * len(text)


def cp_text(font, x, base, text, size, color, anchor="start", ls=0.0, opacity=1.0):
    """Chữ giãn ls em như letterSpacing của Android (mỗi ký tự thêm ls × cỡ, chia đều hai bên)."""
    sp = ls * size
    x = bz_anchor(x, cp_width(font, text, size, ls), anchor) + sp / 2
    op = "" if opacity == 1 else f' fill-opacity="{opacity}"'
    lsa = f' letter-spacing="{f(sp)}"' if sp else ""
    return f'<text class="{font.css_class}" x="{f(x)}" y="{f(base)}" font-size="{f(size)}" fill="{color}"{lsa}{op}>{text}</text>'


def cp_label(text, size, color, px, py, rot):
    """Nhãn Krona One căn giữa nét chữ tại (px, py) như getTextBounds của app mẫu, xoay rot° quanh tâm nhãn."""
    lo, top, hi, bottom = CP_KRONA.ink(text, size)
    s = CP_KRONA.text(px - (lo + hi) / 2, py - (top + bottom) / 2, text, size, color)
    return s.replace("<text ", f'<text transform="rotate({f(rot)} {f(px)} {f(py)})" ', 1)


def cp_ring(kind, u):
    """Vòng giờ / phút / giây lúc 0 giờ 0 phút 0 giây, tâm tại gốc, mỗi tổ hợp một <g> trong defs; khung xoay cả vòng (xoay cứng: vị trí và hướng
    chữ cùng đổi). Màu lấy từ biến CSS --cpc (số) / --cpd (giờ rưỡi) đặt ở <use> nên mỗi cỡ chỉ một <g>. Góc theo app mẫu: x = sin θ · r,
    y = cos θ · r (θ = 90° là đường chỉ bên phải), chữ xoay 90 − θ."""
    key = (kind, round(u, 2))
    if key in CP_RINGS:
        return CP_RINGS[key][0]
    n = f"cp{kind}{len(CP_RINGS)}"
    s, ticks = "", ""
    for i in range(24):
        if kind == "h":
            th = 120 + 15 * i
            a = math.radians(th)
            px, py = math.sin(a) * 0.84 * u, math.cos(a) * 0.84 * u
            k = i // 2 + 1
            s += (cp_label(str(k), 0.165 * u, "var(--cpc)", px, py, 90 - th) if i % 2 == 0 else
                  cp_label(f"{k:02d}:30", 0.066 * u, "var(--cpd)", px, py, 90 - th))
            continue
        th = 90 + 15 * i
        a = math.radians(th)
        if i % 2 == 0:
            r, size = (0.55, 0.11) if kind == "m" else (0.3, 0.09)
            s += cp_label(f"{i * 5 // 2:02d}", size * u, "var(--cpc)", math.sin(a) * r * u, math.cos(a) * r * u, 90 - th)
        elif kind == "m":
            ticks += (f"M{f(math.sin(a) * 0.475 * u)},{f(math.cos(a) * 0.475 * u)}"
                      f"L{f(math.sin(a) * 0.625 * u)},{f(math.cos(a) * 0.625 * u)}")
    if ticks:
        s = f'<path d="{ticks}" stroke="var(--cpc)" stroke-opacity=".6" stroke-width="1.3" stroke-linecap="round"/>' + s
    CP_RINGS[key] = (n, f'<g id="{n}">{s}</g>')
    return n


def cp_dial(cx, cy, side, hh=10, mm=8, ss=42, colors=CP_COLORS, live=False, seconds=True):
    """Đường chỉ (màu giờ) vẽ trước nên chữ đè lên như app mẫu, rồi ba vòng số xoay liên tục theo giờ thật (giây có phần nghìn giây, phút có
    phần giây, giờ có phần phút), rồi hai vòng tròn mảnh màu phút. live: khung chạy theo đồng hồ của máy xem mock (script cuối trang).
    seconds=False: Kiểu 2 (Style 2 của app mẫu): không vòng giây, đường chỉ lùi ra 0,28u, mặt số đổi theo từng phút."""
    hour, half, minute, second = colors
    u = side / 2
    mv = mm + ss / 60
    hv = hh % 12 + mv / 60
    x0 = cx if seconds else cx + 0.28 * u
    s = (f'<line x1="{f(x0)}" y1="{f(cy)}" x2="{f(cx + u)}" y2="{f(cy)}" stroke="{hour}" stroke-width="1.3" stroke-linecap="round"/>'
         f'<path d="M{f(x0 + 0.05 * u)},{f(cy - 0.03 * u)}L{f(x0)},{f(cy)}L{f(x0 + 0.05 * u)},{f(cy + 0.03 * u)}" fill="none" '
         f'stroke="{hour}" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/>')
    ids = {}
    if live:
        lid = f"cplv{len(CP_LIVE)}"
        CP_LIVE.append((lid, cx, cy))
        ids = {k: f' id="{lid}{k}"' for k in "smh"}
    rings = (("s", 6 * ss, second, ""), ("m", 6 * mv, minute, ""), ("h", 30 * hv, hour, f";--cpd:{half}"))
    for kind, angle, color, sub in rings if seconds else rings[1:]:
        s += (f'<use{ids.get(kind, "")} href="#{cp_ring(kind, u)}" style="--cpc:{color}{sub}" '
              f'transform="translate({f(cx)} {f(cy)}) rotate({f(angle)})"/>')
    for r in (0.68, 0.42):
        s += (f'<circle cx="{f(cx)}" cy="{f(cy)}" r="{f(r * u)}" fill="none" stroke="{minute}" stroke-opacity="{40 / 255:.3f}" '
              f'stroke-width="1"/>')
    return s


def cp_live_script():
    """Khung live xoay ba vòng theo giờ của máy xem mock mỗi khung hình, đúng công thức góc của app mẫu."""
    frames = ",".join(f'["{lid}",{f(cx)},{f(cy)}]' for lid, cx, cy in CP_LIVE)
    return ('<script>(function(){var c=[' + frames + '];function t(){var d=new Date(),s=d.getSeconds()+d.getMilliseconds()/1e3,'
            'm=d.getMinutes()+s/60,h=d.getHours()%12+m/60;c.forEach(function(e){var p="translate("+e[1]+" "+e[2]+") rotate(";'
            '[["s",6*s],["m",6*m],["h",30*h]].forEach(function(r){var n=document.getElementById(e[0]+r[0]);'
            'if(n)n.setAttribute("transform",p+r[1].toFixed(2)+")");});});requestAnimationFrame(t);}t();})();</script>')


def cp_date(x, top, anchor="middle", weather=False, color=CP_MINUTE):
    """Ngày in hoa Google Sans Bold 14 sp giãn 0,25 em; thời tiết cách 12 dp cùng cỡ. Cao 17,5 dp."""
    size, base = 14, top + BZ_ASC * 14
    wd = cp_width(CP_BOLD, CP_DATE, size, 0.25)
    ww = 12 + 25 + CP_BOLD.width("24°", size) if weather else 0
    x = bz_anchor(x, wd + ww, anchor)
    s = cp_text(CP_BOLD, x, base, CP_DATE, size, color, ls=0.25)
    if weather:
        wx = x + wd + 12
        s += cloud(wx - 1, base, 0.56, color, 1.7) + CP_BOLD.text(wx + 25, base, "24°", size, color)
    return s


def cp_status(x, cy, anchor="middle", color=CP_SUB, n=2, charging=False):
    """Dòng pin · thông báo dùng chung AodStatusLine, icon pin 14 dp như layout mẫu, chữ Google Sans Regular."""
    g, fs = 14.0, 0.915 * 14
    tw = CP_REG.width("72%", fs)
    w = 0.522 * g + 0.717 * g + tw + (1.13 * g + 0.228 * g + 1.065 * g + n * 0.978 * g + (n - 1) * 1.13 * g if n else 0)
    x = bz_anchor(x, w, anchor)
    s = bz_battery(x, cy - g / 2, g, color, charging) + CP_REG.text(x + 1.239 * g, cy + 0.34 * fs, "72%", fs, color)
    if n:
        dx = x + 1.239 * g + tw + 1.13 * g
        s += f'<circle cx="{f(dx + 0.114 * g)}" cy="{f(cy)}" r="{f(0.114 * g)}" fill="{color}"/>'
        shapes = icon_shapes(color)
        ix = dx + 0.228 * g + 1.065 * g
        s += "".join(shapes[i % 3](ix + i * 2.108 * g, cy - 0.489 * g, 0.978 * g) for i in range(n))
    return s


def cp_latest(x, cy, w, anchor="middle", color=CP_SUB, text="MAI: TỐI NAY ĐI ĂN NHÉ"):
    """Thông báo mới nhất: icon 16 dp, cách 10 dp, chữ in hoa Google Sans Regular 12,5 sp giãn 0,1 em, dài thì "…"."""
    while cp_width(CP_REG, text, 12.5, 0.1) > w - 26 and len(text) > 1:
        text = text[:-2].rstrip() + "…"
    tw = 26 + cp_width(CP_REG, text, 12.5, 0.1)
    x = bz_anchor(x, tw, anchor)
    return icon_shapes(color)[0](x, cy - 8, 16) + cp_text(CP_REG, x + 26, cy + 0.34 * 12.5, text, 12.5, color, ls=0.1)


def cp_player(x, top, w, anchor="middle", color=CP_MINUTE, paused=False, title="THAT COOL SONG"):
    """Trình phát như layout mẫu: nút lùi / tới 40 dp (icon 12 dp), tên in hoa Bold 13 sp giãn 0,25 em, cách 8 dp nghệ sĩ Regular 11,5 sp
    mờ 0,7 (chạm để phát / dừng, dừng thì mờ 0,6). Cao 40 dp."""
    x, op = bz_anchor(x, w, anchor), 0.6 if paused else 1.0
    cx = x + w / 2
    s = ""
    for bx, d in ((x + 20, -1), (x + w - 20, 1)):
        s += (f'<path d="M{f(bx - 4 * d)},{f(top + 14)} L{f(bx + 3 * d)},{f(top + 20)} L{f(bx - 4 * d)},{f(top + 26)} Z" fill="{color}" '
              f'stroke="{color}" stroke-width="1" stroke-linejoin="round"/>'
              f'<rect x="{f(bx + 4 * d - (1.6 if d < 0 else 0))}" y="{f(top + 14)}" width="1.6" height="12" rx=".8" fill="{color}"/>')
    tw = w - 100
    t = title
    while cp_width(CP_BOLD, t, 13, 0.25) > tw and len(t) > 1:
        t = t[:-2].rstrip() + "…"
    s += cp_text(CP_BOLD, cx, top + 0.6 + BZ_ASC * 13, t, 13, color, "middle", 0.25, op)
    return s + cp_text(CP_REG, cx, top + 0.6 + 16.3 + 8 + BZ_ASC * 11.5, "IT'S ARTIST", 11.5, color, "middle", 0.25, 0.7 * op)


def cp_group(date=True, weather=False, status=True, n=2, charging=False, latest=False, w=300):
    """Khối thông tin (cao, hàm vẽ): ngày, cách 15 dp dòng pin, thông báo mới nhất."""
    items = []
    if date:
        items.append((17.5, 15, lambda x, t, a: cp_date(x, t, a, weather)))
    if status:
        items.append((14, 12, lambda x, t, a: cp_status(x, t + 7, a, n=n, charging=charging)))
    if latest:
        items.append((16, 12, lambda x, t, a: cp_latest(x, t + 8, w, a)))
    return items


def cp_music(w, paused=False):
    return [(40, 0, lambda x, t, a: cp_player(x, t, w, a, paused=paused))]


def cp_stack_h(items):
    return sum(h for h, _, _ in items) + sum(g for _, g, _ in items[:-1])


def cp_draw(x, top, items, anchor="middle"):
    s, y = "", top
    for i, (h, gap, draw) in enumerate(items):
        s += draw(x, y, anchor)
        y += h + (gap if i < len(items) - 1 else 0)
    return s


def cp_portrait(hh=10, mm=8, ss=42, size=1.0, colors=CP_COLORS, live=False, seconds=True, media=False, paused=False, **group):
    """Màn dọc: mặt số giữa màn; khối dưới thẳng đáy cách đáy 60 dp như layout mẫu, có nhạc thì trình phát nằm dưới cùng (khối lớn lên phía trên)."""
    s = cp_dial(PW / 2, PH / 2, PW * 0.75 * size, hh, mm, ss, colors, live, seconds)
    items = cp_group(w=PW - 60, **group)
    if media:
        if items:
            h, _, d = items[-1]
            items[-1] = (h, 24, d)
        items += cp_music(PW - 80, paused)
    return s + cp_draw(PW / 2, PH - 60 - cp_stack_h(items), items)


def cp_landscape(left, right, hh=10, mm=8, ss=42, size=1.0, live=False):
    """Màn ngang: mặt số giữa màn (cạnh = cạnh ngắn × cỡ như app mẫu), hai nhóm căn giữa hai vùng bên, giữa chiều cao."""
    side = H * 0.75 * size
    zone = (W - side) / 2
    s = cp_dial(W / 2, CY, side, hh, mm, ss, live=live)
    for x, items in ((zone / 2, left), (W - zone / 2, right)):
        if items:
            s += cp_draw(x, CY - cp_stack_h(items) / 2, items)
    return s


def cp_strip():
    k = 0.4
    s = ""
    for i, (label, kw) in enumerate((("Mặc định: giờ hổ phách", {}),
                                      ("Kiểu 2: không vòng giây", {"seconds": False, "hh": 1, "mm": 50, "ss": 0}),
                                      ("Mỗi vòng một màu", {"colors": CP_MIX, "hh": 7, "mm": 21, "ss": 5}),
                                      ("Cỡ 120%", {"size": 1.2, "hh": 4, "mm": 38, "ss": 50}))):
        x = 36 + i * 212
        s += f'<g transform="translate({x} 14) scale({k})">'
        s += f'<rect width="{PW}" height="{PH}" rx="34" fill="#000" stroke="#333" stroke-width="2"/>'
        s += cp_portrait(**kw) + "</g>" + txt(x + PW * k / 2, 14 + PH * k + 22, label, 13, DM, 400, "middle")
    return (f'<p style="font-size:13px;color:var(--text-secondary);margin:1.25rem 0 6px">Kiểu (có / không vòng giây như Style 1 / 2 của app mẫu), '
            f'màu từng vòng và cỡ mặt số</p>'
            f'<svg viewBox="0 0 {W} 400" width="100%" role="img" aria-label="Màu từng vòng và cỡ mặt số" '
            f'style="display:block;font-family:\'Plus Jakarta Sans\',sans-serif" fill="{G}">'
            f'<rect width="{W}" height="400" rx="20" fill="#111"/>{s}</svg>')


CP_ZONE_W = (W - H * 0.75) / 2 - 24
compass = [
    CP_FONTS,
    cp_strip(),
    '<div style="font-size:0">',
    frame_portrait("D1 · Dọc, mặc định (cũng là thẻ ở lưới chủ đề), chạy theo giờ thật: vòng giây quay một vòng mỗi phút, vòng phút và giờ "
                   "trôi liên tục", cp_portrait(live=True)),
    frame_portrait("D2 · Dọc, 10:08:42, thời tiết cạnh ngày, nhạc dưới dòng pin", cp_portrait(weather=True, media=True)),
    frame_portrait("D3 · Dọc, 21:47:15, đang sạc, thông báo mới nhất, nhạc đang dừng (mờ)",
                   cp_portrait(hh=21, mm=47, ss=15, charging=True, latest=True, n=1, media=True, paused=True)),
    frame_portrait("D4 · Dọc, Kiểu 2 (không vòng giây, đường chỉ lùi ra ngoài), 4:38, cỡ 120%, màu như app mẫu, tắt ngày",
                   cp_portrait(hh=4, mm=38, ss=0, size=1.2, colors=CP_REF, seconds=False, date=False, n=3)),
    '</div>',
    frame("N1 · Ngang, chạy theo giờ thật, không nhạc: ngày + thời tiết bên trái, pin · thông báo bên phải",
          cp_landscape(cp_group(weather=True, status=False), cp_group(date=False), live=True)),
    frame("N2 · Ngang, 10:08:42, nhạc: ngày + thời tiết, pin · thông báo xếp dọc bên trái, trình phát bên phải",
          cp_landscape(cp_group(weather=True), cp_music(CP_ZONE_W))),
    frame("N3 · Ngang, 9:15:30, thông báo mới nhất, nhạc đang dừng",
          cp_landscape(cp_group(latest=True, w=CP_ZONE_W), cp_music(CP_ZONE_W, paused=True), hh=9, mm=15, ss=30)),
    cp_live_script(),
]
compass_defs = "".join(g for _, g in CP_RINGS.values())

# ================================================================== BÁN NGUYỆT (Half Concentric của Muviz Edge; số đo lấy từ layout dec22_screen_layout dọc / ngang
# và HalfConcentricClock trong APK, không từ ảnh chụp). Mặt số vuông cạnh 0,9 cạnh ngắn, r = nửa cạnh, tâm lùi ra ngoài mép trái 0,95r (chỉ thấy
# nửa phải tới 1,05r), giữa chiều cao. Vòng ngoài là giây: 60 vạch tới r (vạch mỗi 5 giây từ 0,93r, còn lại từ 0,96r), số 00–55 Google Sans
# Regular 0,08r ở 0,86r; vòng trong là phút: vạch 0,68r / 0,72r → 0,76r, số 0,09r ở 0,58r. Chữ số đứng thẳng, hai vòng xoay theo giờ thật mỗi
# khung hình để giá trị hiện tại chỉ sang phải. Giờ "HH" 0,36r ở tâm + 0,18r, phút "mm" 0,155r trong viên viền ở tâm + 0,6r: vạch / số hai vòng
# khoét quanh viên (vuông 2,4 × cao chữ, bo 0,6 cao chữ), viền viên 3 px cao 2,4 × cao chữ bo tròn hết, kéo tới mép mặt số như lúc thức của
# app mẫu (user bỏ trạng thái nghỉ: viền thu về, số giây mờ). Phủ tối dạng tròn tâm cách tâm mặt số 1,75r về bên phải, bán
# kính 2r, tối từ 0,85: đỉnh, đáy, mép trái mặt số chìm vào nền. Ngày "dd MMM yyyy" 12 sp (tối hơn 30%) + thứ "EEEE" 13 sp in hoa Bold giãn
# 0,25 em, cách 8 dp, ở nửa phải giữa chiều cao (trên trục số đang chỉ).
HC_FONTS = ('<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Google+Sans:wght@400;700&display=swap">'
            '<style>.cpb{font-family:"Google Sans";font-weight:700}.cpr{font-family:"Google Sans";font-weight:400}</style>')
HC_ACCENT = "#E5484D"  # màu nhấn đề xuất: đỏ (Red), chưa chủ đề nào dùng
HC_COLORS = {"hour": GE, "minute": GE, "ring": HC_ACCENT, "dash": "#6E6E6E", "date": GE, "status": "#9A9A9A", "media": "#E0E0E0"}
HC_REF = {"hour": "#FFFFFF", "minute": "#FFFFFF", "ring": "#FFFFFF", "dash": "#757575", "date": "#FFFFFF", "status": "#909498",
          "media": "#CED4DA"}
HC_MIX = {**HC_COLORS, "hour": "#FFD54F", "minute": "#80DEEA", "ring": "#80DEEA", "dash": "#5A5A5A"}
HC_DATE, HC_DAY = "19 THG 10 2026", "THỨ HAI"
HC_PX = 1 / 2.75  # 1 px ở mật độ 2,75 (1080 px / 393 dp)
HC_LIVE = []
HC_N = [0]


def hc_dark(c, k=0.7):
    return "#" + "".join(f"{int(int(c[i:i + 2], 16) * k):02X}" for i in (1, 3, 5))


def hc_offset(text, size):
    """Độ lệch gốc chữ so với tâm nét chữ (getTextBounds + exactCenter của app mẫu)."""
    lo, top, hi, bottom = CP_REG.ink(text, size)
    return -(lo + hi) / 2, -(top + bottom) / 2


def hc_label(text, size, color, px, py, opacity=1.0, lid=""):
    ox, oy = hc_offset(text, size)
    s = CP_REG.text(px + ox, py + oy, text, size, color, opacity=opacity)
    return s.replace("<text ", f'<text id="{lid}" data-o="{f(ox)},{f(oy)}" ', 1) if lid else s


def hc_tick_path(kind, major, minor, outer):
    """60 vạch lúc giá trị 0 ở bán kính 100 (một lần trong defs, mỗi khung dùng lại bằng <use> co theo r)."""
    d = ""
    for i in range(60):
        a = math.radians(90 + 6 * i)
        r0 = (major if i % 5 == 0 else minor) * 100
        d += f"M{f(math.sin(a) * r0)},{f(math.cos(a) * r0)}L{f(math.sin(a) * outer * 100)},{f(math.cos(a) * outer * 100)}"
    return f'<path id="hct{kind}" d="{d}" stroke-linecap="round" fill="none"/>'


HC_DEFS = hc_tick_path("s", 0.93, 0.96, 1.0) + hc_tick_path("m", 0.68, 0.72, 0.76)


def hc_ticks(cx, cy, r, value, kind, color, lid=""):
    """Xoay cả nhóm 6° mỗi đơn vị (vạch nằm theo bán kính nên xoay cứng là đúng); nét 2 px không đổi theo r."""
    i = f' id="{lid}"' if lid else ""
    return (f'<use{i} href="#hct{kind}" stroke="{color}" stroke-width="{f(2 * HC_PX * 100 / r)}" '
            f'transform="translate({f(cx)} {f(cy)}) rotate({f(6 * value)}) scale({r / 100:.4f})"/>')


def hc_dial(cy, r, hh=10, mm=8, ss=42, colors=HC_COLORS, awake=True, live=False):
    """Nửa mặt số sát mép trái (toạ độ màn: tâm x = 0,05r). Thứ tự vẽ như app mẫu: hai vòng (khoét viên), giờ, phút, viền viên, phủ tối."""
    n = HC_N[0]
    HC_N[0] += 1
    lid = f"hcl{n}" if live else ""
    if live:
        HC_LIVE.append((lid, 0.05 * r, cy, r))
    cx = 0.05 * r
    sv, mv = ss, mm + ss / 60
    mtext = f"{mm:02d}"
    lo, top, hi, bottom = CP_REG.ink(mtext, 0.155 * r)
    th = bottom - top
    px, hc, mc = cx + 0.6 * r, 1.2 * th, 1.3 * th
    dash = colors["dash"]
    s = (f'<defs><mask id="hcm{n}" maskUnits="userSpaceOnUse"><rect x="-20" y="{f(cy - r - 20)}" width="{f(2 * r + 40)}" '
         f'height="{f(2 * r + 40)}" fill="#fff"/><rect x="{f(px - hc)}" y="{f(cy - hc)}" width="{f(2 * hc)}" height="{f(2 * hc)}" '
         f'rx="{f(0.5 * hc)}" fill="#000"/></mask>'
         f'<radialGradient id="hcv{n}" gradientUnits="userSpaceOnUse" cx="{f(cx + 1.75 * r)}" cy="{f(cy)}" r="{f(2 * r)}">'
         f'<stop offset=".85" stop-color="#000" stop-opacity="0"/><stop offset="1" stop-color="#000"/></radialGradient></defs>'
         f'<g mask="url(#hcm{n})">')
    s += hc_ticks(cx, cy, r, sv, "s", dash, lid and lid + "ts") + hc_ticks(cx, cy, r, mv, "m", dash, lid and lid + "tm")
    for kind, v, d, size, op in (("s", sv, 0.86, 0.08, 1.0 if awake else 0.0), ("m", mv, 0.58, 0.09, 1.0)):
        for i in range(0, 60, 5):
            a = math.radians(90 - 6 * v + 6 * i)
            s += hc_label(f"{i:02d}", size * r, dash, cx + math.sin(a) * d * r, cy + math.cos(a) * d * r, op, lid and f"{lid}{kind}{i}")
    s += "</g>"
    s += hc_label(f"{hh:02d}", 0.36 * r, colors["hour"], cx + 0.18 * r, cy, lid=lid and lid + "h")
    s += hc_label(mtext, 0.155 * r, colors["minute"], px, cy, lid=lid and lid + "n")
    right = (1.05 * r - 1.5 * HC_PX) if awake else px + mc
    s += (f'<rect x="{f(px - mc)}" y="{f(cy - hc)}" width="{f(right - (px - mc))}" height="{f(2 * hc)}" rx="{f(hc)}" fill="none" '
          f'stroke="{colors["ring"]}" stroke-width="{f(3 * HC_PX)}"/>')
    return s + f'<circle cx="{f(cx)}" cy="{f(cy)}" r="{f(r)}" fill="url(#hcv{n})"/>'


def hc_live_script():
    """Khung live: vạch xoay, số đứng thẳng chạy theo vòng, giờ / phút đổi theo giờ của máy xem mock, mỗi khung hình như app mẫu."""
    frames = ",".join(f'["{lid}",{f(cx)},{f(cy)},{f(r)}]' for lid, cx, cy, r in HC_LIVE)
    return ('<script>(function(){var c=[' + frames + '];function p(n){return(n<10?"0":"")+n}'
            'function t(){var d=new Date(),s=d.getSeconds()+d.getMilliseconds()/1e3,m=d.getMinutes()+s/60;'
            'c.forEach(function(e){var id=e[0],x=e[1],y=e[2],r=e[3];'
            '[["s",s,.86],["m",m,.58]].forEach(function(k){var g=document.getElementById(id+"t"+k[0]);'
            'if(g)g.setAttribute("transform","translate("+x+" "+y+") rotate("+(6*k[1]).toFixed(2)+") scale("+r/100+")");'
            'for(var i=0;i<60;i+=5){var l=document.getElementById(id+k[0]+i);if(!l)continue;var o=l.getAttribute("data-o").split(","),'
            'a=(90-6*k[1]+6*i)*Math.PI/180;l.setAttribute("x",(x+Math.sin(a)*k[2]*r+ +o[0]).toFixed(1));'
            'l.setAttribute("y",(y+Math.cos(a)*k[2]*r+ +o[1]).toFixed(1));}});'
            '[["h",p(d.getHours()),.18],["n",p(d.getMinutes()),.6]].forEach(function(k){var l=document.getElementById(id+k[0]);'
            'if(!l||l.textContent===k[1])return;l.textContent=k[1];var b=l.getBBox();'
            'l.setAttribute("x",(x+k[2]*r-b.width/2).toFixed(1));});});requestAnimationFrame(t);}t();})();</script>')


def hc_column(x, cy, colors=HC_COLORS, date=True, weather=False, n=2, charging=False, latest=False, w=180, sizes=(12, 13)):
    """Cột bên phải trên trục số đang chỉ, căn giữa tại x, cả cụm giữa cy: ngày (tối hơn 30%), cách 8 dp thứ, thời tiết một dòng riêng; cách
    12 dp dòng pin · thông báo, cách 12 dp thông báo mới nhất (rộng tối đa w). Dòng nào tắt thì không chiếm chỗ, cả khoảng cách."""
    ds, ys = sizes
    rows = []
    if date:
        rows += [(1.25 * ds, 0, lambda t: cp_text(CP_BOLD, x, t + BZ_ASC * ds, HC_DATE, ds, hc_dark(colors["date"]), "middle", 0.25)),
                 (1.25 * ys, 8, lambda t: cp_text(CP_BOLD, x, t + BZ_ASC * ys, HC_DAY, ys, colors["date"], "middle", 0.25))]
    if weather:
        def wx(t):
            ww = 25 + CP_BOLD.width("24°", ys)
            x0, base = x - ww / 2, t + BZ_ASC * ys
            return cloud(x0 - 1, base, 0.52, colors["date"], 1.6) + CP_BOLD.text(x0 + 25, base, "24°", ys, colors["date"])
        rows.append((1.25 * ys, 8, wx))
    rows.append((14, 12, lambda t: cp_status(x, t + 7, "middle", colors["status"], n, charging)))
    if latest:
        rows.append((16, 12, lambda t: cp_latest(x, t + 8, w, "middle", colors["status"])))
    h = sum(r[0] for r in rows) + sum(r[1] for r in rows[1:])
    s, y = "", cy - h / 2
    for i, (rh, gap, draw) in enumerate(rows):
        y += gap if i else 0
        s += draw(y)
        y += rh
    return s


def hc_portrait(hh=10, mm=8, ss=42, colors=HC_COLORS, awake=True, live=False, date=True, weather=False, n=2, charging=False,
                latest=False, media=False, paused=False):
    """Màn dọc: nửa mặt số giữa chiều cao (điểm nhấn); cột ngày, thứ (+ thời tiết), dòng pin · thông báo (+ thông báo mới nhất) ở nửa phải
    trên trục số đang chỉ (user chốt: dòng pin dưới ngày); trình phát giữa vùng dưới mặt số."""
    r, cy = 0.45 * PW, PH / 2
    zone = cy - r
    s = hc_dial(cy, r, hh, mm, ss, colors, awake, live)
    s += hc_column(0.75 * PW, cy, colors, date, weather, n, charging, latest, PW / 2 - 16)
    if media:
        s += cp_player(PW / 2, PH - zone / 2 - 20, PW - 80, "middle", colors["media"], paused)
    return s


def hc_landscape(hh=10, mm=8, ss=42, colors=HC_COLORS, awake=True, live=False, weather=False, n=2, charging=False, latest=False,
                 media=False, paused=False):
    """Màn ngang: nửa mặt số sát mép trái, r = 0,65 cạnh ngắn (app mẫu 0,45 chỉ chiếm 1/5 bề ngang, để trống giữa); đỉnh / đáy hai vòng ra
    ngoài màn, chìm trong phủ tối. Cột như màn dọc (ngày + thứ 14 / 15 sp như layout ngang mẫu) giữa vùng còn lại trên trục; có nhạc thì cột
    và trình phát (cách 28 dp) thành một khối giữa chiều cao (đặt trình phát ở đáy thì nửa trên trống, xếp hàng ngang thì trình phát sát mép)."""
    r = 0.65 * H
    xm = (1.05 * r + W) / 2
    s = hc_dial(CY, r, hh, mm, ss, colors, awake, live)
    if not media:
        return s + hc_column(xm, CY, colors, True, weather, n, charging, latest, 420, (14, 15))
    h = hc_column_h(weather, latest, (14, 15))
    top = CY - (h + 28 + 40) / 2
    s += hc_column(xm, top + h / 2, colors, True, weather, n, charging, latest, 420, (14, 15))
    return s + cp_player(xm, top + h + 28, 360, "middle", colors["media"], paused)


def hc_column_h(weather, latest, sizes):
    ds, ys = sizes
    return 1.25 * ds + 8 + 1.25 * ys + (8 + 1.25 * ys if weather else 0) + 12 + 14 + (28 if latest else 0)


def hc_strip():
    k = 0.4
    s = ""
    for i, (label, kw) in enumerate((("Mặc định: viền phút đỏ", {}),
                                      ("Buổi tối, 23:41", {"hh": 23, "mm": 41, "ss": 12}),
                                      ("Màu như app mẫu", {"colors": HC_REF, "hh": 7, "mm": 21, "ss": 5}),
                                      ("Đổi màu từng phần", {"colors": HC_MIX, "hh": 4, "mm": 38, "ss": 50}))):
        x = 36 + i * 212
        s += (f'<g transform="translate({x} 14) scale({k})"><clipPath id="hcc{i}"><rect width="{PW}" height="{PH}" rx="34"/></clipPath>'
              f'<rect width="{PW}" height="{PH}" rx="34" fill="#000" stroke="#333" stroke-width="2"/><g clip-path="url(#hcc{i})">')
        s += hc_portrait(**kw) + "</g></g>" + txt(x + PW * k / 2, 14 + PH * k + 22, label, 13, DM, 400, "middle")
    return (f'<p style="font-size:13px;color:var(--text-secondary);margin:1.25rem 0 6px">Màu (giờ, phút, viền phút, vạch, '
            f'ngày, dòng pin, nhạc)</p>'
            f'<svg viewBox="0 0 {W} 400" width="100%" role="img" aria-label="Màu" '
            f'style="display:block;font-family:\'Plus Jakarta Sans\',sans-serif" fill="{G}">'
            f'<rect width="{W}" height="400" rx="20" fill="#111"/>{s}</svg>')


half = [
    HC_FONTS,
    hc_strip(),
    '<div style="font-size:0">',
    frame_portrait("D1 · Dọc, mặc định (cũng là thẻ ở lưới chủ đề), chạy theo giờ thật: vòng giây quay một vòng mỗi phút, vòng phút trôi "
                   "liên tục", hc_portrait(live=True)),
    frame_portrait("D2 · Dọc, 10:08:42, thời tiết dưới thứ, nhạc", hc_portrait(weather=True, media=True)),
    frame_portrait("D3 · Dọc, 21:47:15, đang sạc, thông báo mới nhất, nhạc đang dừng (mờ)",
                   hc_portrait(hh=21, mm=47, ss=15, charging=True, latest=True, n=1, media=True, paused=True)),
    frame_portrait("D4 · Dọc, 4:38, màu như app mẫu, tắt ngày",
                   hc_portrait(hh=4, mm=38, ss=0, colors=HC_REF, date=False, n=3)),
    '</div>',
    frame("N1 · Ngang, chạy theo giờ thật, không nhạc: ngày + thời tiết, dòng pin · thông báo trên trục",
          hc_landscape(live=True, weather=True)),
    frame("N2 · Ngang, 10:08:42, nhạc", hc_landscape(media=True)),
    frame("N3 · Ngang, 21:47:15, đang sạc, thông báo mới nhất, nhạc đang dừng",
          hc_landscape(hh=21, mm=47, ss=15, charging=True, latest=True, n=1, media=True, paused=True)),
    hc_live_script(),
]


# ================================================================== DẢI MÀU (Gradient Line Clock của Muviz Edge; số đo lấy từ layout sep23_screen_layout (chỉ
# có bản dọc) và Sep23Clock trong APK, không từ ảnh chụp). Lề 40 dp hai bên, 60 dp trên / dưới; trình phát trên cùng cách vạch 30 dp, khối dưới
# (thông báo mới nhất, dòng pin hai màu) neo đáy. Vạch dọc giữa vùng còn lại, dày 0,008 cạnh ngắn, tô gradient vàng → hồng tím → xanh từ đầu
# tới cuối vạch. Giờ Google Sans Regular 0,11 cạnh ngắn × cỡ (110%) căn theo nét chữ trên vạch, tâm chạy từ th tới hết vạch trừ 2th theo phút
# trong ngày (hoặc ghim); ngày "EEEE, dd MMM" 0,043 mờ 78% cách tâm giờ 1,2th, thời tiết cạnh ngày; vạch khoét 1,1th trước tới 2th sau. Màn
# ngang (app mẫu dùng chung layout dọc nên vạch ngắn, giờ nhỏ): vạch ngang hết bề rộng trừ lề 40, cao hơn giữa 0,35th, giờ chạy trái → phải,
# khoét quanh giờ 0,6th mỗi bên, ngày dưới vạch; trình phát giữa phía trên, dòng pin giữa phía dưới.
GL_SUNSET = ("#FFCC70", "#C850C0", "#4158D0")  # bộ mặc định của app mẫu, màu nhấn của chủ đề
GL_FIRE = ("#D24C4C", "#D47C3A", "#E1B500")
GL_PASTEL = ("#A9C9FF", "#CEC4F7", "#FFBBEC")
GL_GREY = ("#6A6A61", "#898989", "#D6D6D6")
GL_TEXT = "#E0E0E0"
GL_DATE = "Thứ Ba, 20 thg 10"
GL_N = [0]


def gl_id():
    GL_N[0] += 1
    return f"gl{GL_N[0]}"


def gl_grad(gid, x1, y1, x2, y2, g):
    stops = "".join(f'<stop offset="{o}" stop-color="{c}"/>' for o, c in zip((0, .5, 1), g))
    return f'<linearGradient id="{gid}" gradientUnits="userSpaceOnUse" x1="{f(x1)}" y1="{f(y1)}" x2="{f(x2)}" y2="{f(y2)}">{stops}</linearGradient>'


def gl_row_w(ds, date=True, weather=False):
    w = CP_REG.width(GL_DATE, ds) if date else 0
    return w + ((ds * .5 if date else 0) + ds * 1.2 + 4 + CP_REG.width("24°", ds) if weather else 0)


def gl_row(cx, cy, ds, date=True, weather=False, color=GL_TEXT):
    """Dòng ngày mờ 78% (+ thời tiết cạnh ngày), căn giữa tại cx, giữa dòng ở cy."""
    base, x, s = cy + .35 * ds, cx - gl_row_w(ds, date, weather) / 2, ""
    if date:
        s += CP_REG.text(x, base, GL_DATE, ds, color, opacity=.78)
        x += CP_REG.width(GL_DATE, ds) + (ds * .5 if weather else 0)
    if weather:
        s += cloud(x - ds * .15, base, ds / 28, color, ds / 10) + CP_REG.text(x + ds * 1.2 + 4, base, "24°", ds, color, opacity=.78)
    return s


def gl_time(cx, cy, text, ts, color=GL_TEXT):
    """Giờ căn theo nét chữ (getTextBounds + exactCenter như app mẫu)."""
    lo, top, hi, bottom = CP_REG.ink(text, ts)
    return CP_REG.text(cx - (lo + hi) / 2, cy - (top + bottom) / 2, text, ts, color)


def gl_vclock(x0, y0, w, h, hh, mm, pos=None, size=1.1, g=GL_SUNSET, date=True, weather=False):
    """Vạch dọc như Sep23Clock: tâm giờ ở th + (dài − th − 2th) · phần đã qua của ngày (pos: vị trí ghim)."""
    mn, gid = min(w, h), gl_id()
    ts, ds, text = .11 * mn * size, .043 * mn * size, f"{hh:02d}:{mm:02d}"
    lo, top, hi, bottom = CP_REG.ink(text, ts)
    th, row, rh = bottom - top, date or weather, 1.25 * ds
    off = max(1.2 * th, .85 * th + rh / 2)
    fr = (hh * 60 + mm) / 1440 if pos is None else pos
    cy = y0 + th + max(h - th - max(2 * th, off + rh / 2 if row else 0), 0) * fr
    after = max(2 * th, off + rh / 2 + .45 * th) if row else 1.1 * th
    lx, sw = x0 + w / 2, .008 * mn
    s = (f'<defs>{gl_grad(gid, lx, y0, lx, y0 + h, g)}<mask id="{gid}m" maskUnits="userSpaceOnUse" x="0" y="0" width="{W}" height="{PH}">'
         f'<rect width="{W}" height="{PH}" fill="#fff"/><rect y="{f(cy - 1.1 * th)}" width="{W}" height="{f(1.1 * th + after)}" fill="#000"/>'
         f'</mask></defs><line x1="{f(lx)}" y1="{f(y0 + sw)}" x2="{f(lx)}" y2="{f(y0 + h - sw)}" stroke="url(#{gid})" stroke-width="{f(sw)}" '
         f'stroke-linecap="round" mask="url(#{gid}m)"/>')
    s += gl_time(lx, cy, text, ts)
    return s + (gl_row(lx, cy + off, ds, date, weather) if row else "")


def gl_status(cx, cy, n=2, charging=False, bat=GL_TEXT, noti=GL_TEXT):
    """Dòng pin hai màu như app mẫu: pin (mờ 0,9) theo màu pin, chấm và icon theo màu thông báo; icon pin cao 16 dp."""
    g, fs = 16.0, 0.915 * 16
    tw = CP_REG.width("72%", fs)
    w = 0.522 * g + 0.717 * g + tw + (1.13 * g + 0.228 * g + 1.065 * g + n * 0.978 * g + (n - 1) * 1.13 * g if n else 0)
    x = cx - w / 2
    s = f'<g opacity=".9">{bz_battery(x, cy - g / 2, g, bat, charging)}{CP_REG.text(x + 1.239 * g, cy + 0.34 * fs, "72%", fs, bat)}</g>'
    if n:
        dx = x + 1.239 * g + tw + 1.13 * g
        s += f'<circle cx="{f(dx + 0.114 * g)}" cy="{f(cy)}" r="{f(0.114 * g)}" fill="{noti}"/>'
        shapes = icon_shapes(noti)
        s += "".join(shapes[i % 3](dx + 0.228 * g + 1.065 * g + i * 2.108 * g, cy - 0.489 * g, 0.978 * g) for i in range(n))
    return s


def gl_latest(cx, cy, color=GL_TEXT, text="Mai: Tối nay đi ăn nhé"):
    """Thông báo mới nhất như notify_lt: icon 20 dp, cách 10 dp, chữ 16 sp."""
    x = cx - (30 + CP_REG.width(text, 16)) / 2
    return icon_shapes(color)[0](x, cy - 10, 20) + CP_REG.text(x + 30, cy + .34 * 16, text, 16, color)


def gl_player(cx, top, w, color=GL_TEXT, paused=False):
    """Trình phát như media_widget_lt: nút 35 dp (icon 9 dp) mờ 0,7, tên 16 sp, cách 4 dp nghệ sĩ 13 sp mờ 0,7; dừng thì chữ mờ 0,6."""
    x, op, cy = cx - w / 2, .6 if paused else 1.0, top + 19
    s = ""
    for bx, d in ((x + 17.5, -1), (x + w - 17.5, 1)):
        s += (f'<g opacity=".7"><path d="M{f(bx - 3.5 * d)},{f(cy - 4.5)} L{f(bx + 2.5 * d)},{f(cy)} L{f(bx - 3.5 * d)},{f(cy + 4.5)} Z" '
              f'fill="{color}"/><rect x="{f(bx + 2.5 * d - (1.4 if d < 0 else 0))}" y="{f(cy - 4.5)}" width="1.4" height="9" fill="{color}"/></g>')
    s += CP_REG.text(cx, top + 15, "That Cool Song", 16, color, "middle", op)
    return s + CP_REG.text(cx, top + 33, "It's Artist", 13, color, "middle", .7 * op)


def gl_portrait(hh=10, mm=8, pos=None, size=1.1, g=GL_SUNSET, date=True, weather=False, media=False, paused=False, latest=False, n=2,
                charging=False):
    """Màn dọc như layout mẫu: trình phát trên vạch, vạch chiếm phần còn lại, khối dưới neo đáy."""
    top, s = 60, ""
    if media:
        s += gl_player(PW / 2, 60, 313, paused=paused)
        top = 60 + 38 + 30
    sy = PH - 60 - 9
    details_top = sy - 9 - (30 if latest else 0)
    s += gl_vclock(40, top, 313, details_top - 15 - 15 - top, hh, mm, pos, size, g, date, weather)
    s += gl_status(PW / 2, sy, n, charging)
    return s + (gl_latest(PW / 2, sy - 29) if latest else "")


def gl_landscape(hh=10, mm=8, pos=None, size=1.1, g=GL_SUNSET, date=True, weather=False, media=False, paused=False, latest=False, n=2,
                 charging=False):
    """Màn ngang: vạch ngang hết bề rộng trừ lề 40, giờ + dòng ngày không ra khỏi vùng; trình phát trên, dòng pin dưới."""
    gid, ts, ds, text = gl_id(), .11 * H * size, .043 * H * size, f"{hh:02d}:{mm:02d}"
    lo, top, hi, bottom = CP_REG.ink(text, ts)
    th, tw, row = bottom - top, hi - lo, date or weather
    ly = H / 2 - .35 * th
    lw = max(tw, gl_row_w(ds, date, weather) if row else 0)
    fr = (hh * 60 + mm) / 1440 if pos is None else pos
    cx = 40 + lw / 2 + (W - 80 - lw) * fr
    c0, c1 = cx - tw / 2 - .6 * th, cx + tw / 2 + .6 * th
    sw = .008 * H
    s = (f'<defs>{gl_grad(gid, 40, ly, W - 40, ly, g)}<mask id="{gid}m" maskUnits="userSpaceOnUse" x="0" y="0" width="{W}" height="{H}">'
         f'<rect width="{W}" height="{H}" fill="#fff"/><rect x="{f(c0)}" width="{f(c1 - c0)}" height="{H}" fill="#000"/></mask></defs>'
         f'<line x1="{f(40 + sw)}" y1="{f(ly)}" x2="{f(W - 40 - sw)}" y2="{f(ly)}" stroke="url(#{gid})" stroke-width="{f(sw)}" '
         f'stroke-linecap="round" mask="url(#{gid}m)"/>')
    s += gl_time(cx, ly, text, ts)
    if row:
        s += gl_row(cx, ly + max(1.2 * th, .85 * th + 1.25 * ds / 2), ds, date, weather)
    if media:
        s += gl_player(W / 2, 30, 360, paused=paused)
    sy = H - 32 - 9
    s += gl_status(W / 2, sy, n, charging)
    return s + (gl_latest(W / 2, sy - 29) if latest else "")


def gl_reference_landscape():
    """App mẫu ở màn ngang: dùng chung layout dọc nên vạch dọc chỉ cao 188 dp, giờ 0,11 × 188 × 1,1 ≈ 23 dp."""
    return gl_vclock(40, 60, W - 80, 188, 10, 8) + gl_status(W / 2, H - 60 - 9)


def gl_strip():
    k, s = 0.4, ""
    for i, (label, kw) in enumerate((("Mặc định: bộ của app mẫu", {}),
                                      ("Bộ có sẵn: lửa", {"g": GL_FIRE, "hh": 19, "mm": 5}),
                                      ("Bộ có sẵn: pastel", {"g": GL_PASTEL, "hh": 6, "mm": 40}),
                                      ("Một màu, cỡ 150%", {"g": (MINT, MINT, MINT), "size": 1.5, "hh": 13, "mm": 30}))):
        x = 36 + i * 212
        s += (f'<g transform="translate({x} 14) scale({k})"><clipPath id="glc{i}"><rect width="{PW}" height="{PH}" rx="34"/></clipPath>'
              f'<rect width="{PW}" height="{PH}" rx="34" fill="#000" stroke="#333" stroke-width="2"/><g clip-path="url(#glc{i})">')
        s += gl_portrait(**kw) + "</g></g>" + txt(x + PW * k / 2, 14 + PH * k + 22, label, 13, DM, 400, "middle")
    return (f'<p style="font-size:13px;color:var(--text-secondary);margin:1.25rem 0 6px">Màu vạch: bộ gradient có sẵn hoặc tự chỉnh</p>'
            f'<svg viewBox="0 0 {W} 400" width="100%" role="img" aria-label="Màu vạch" '
            f'style="display:block;font-family:\'Plus Jakarta Sans\',sans-serif" fill="{G}">'
            f'<rect width="{W}" height="400" rx="20" fill="#111"/>{s}</svg>')


gradient_line = [
    HC_FONTS,
    gl_strip(),
    '<div style="font-size:0">',
    frame_portrait("D1 · Dọc, mặc định 10:08 (cũng là thẻ ở lưới chủ đề): giờ nằm trên vạch theo phút trong ngày, 00:00 ở đỉnh, 23:59 ở đáy",
                   gl_portrait()),
    frame_portrait("D2 · Dọc, 08:30, nhạc ở trên vạch, thời tiết cạnh ngày", gl_portrait(hh=8, mm=30, media=True, weather=True)),
    frame_portrait("D3 · Dọc, 21:47, đang sạc, thông báo mới nhất, nhạc đang dừng (mờ)",
                   gl_portrait(hh=21, mm=47, charging=True, latest=True, n=1, media=True, paused=True)),
    frame_portrait("D4 · Dọc, 23:10, ghim vị trí 50%, tắt ngày, bộ xám", gl_portrait(hh=23, mm=10, pos=.5, date=False, n=3, g=GL_GREY)),
    '</div>',
    frame("N1 · Ngang, 10:08: vạch nằm ngang hết bề rộng, giờ chạy trái → phải theo phút trong ngày", gl_landscape()),
    frame("N2 · Ngang, 08:30, nhạc ở trên, thời tiết cạnh ngày", gl_landscape(hh=8, mm=30, media=True, weather=True)),
    frame("N3 · Ngang, 21:47, đang sạc, thông báo mới nhất, nhạc đang dừng",
          gl_landscape(hh=21, mm=47, charging=True, latest=True, n=1, media=True, paused=True)),
    frame("N4 · Để so: app mẫu ở màn ngang dùng chung layout dọc, vạch dọc chỉ cao 188 dp, giờ 23 dp", gl_reference_landscape()),
]



# ================================================================== GỢN SÓNG (Android 12 của Muviz Edge; số đo lấy từ layout aug21_screen_layout (chỉ có bản dọc),
# Android12Clock2 và Android12ClockBg2 trong APK). Vùng thông báo trên cùng cao 200 dp (hàng icon, thông báo mới nhất), vùng dưới giữ 105 dp cho thẻ
# nhạc rồi dòng pin cách đáy 30 dp, mặt số giữa phần còn lại. half = nửa cạnh ngắn vùng mặt số; đĩa 12 gợn (±6%) bán kính 0,9 × cỡ (80%) × half,
# đặc hoặc viền 2 dp; ngày "E  dd" Medium 0,085 half uốn theo nửa trên đĩa ở 0,75 bán kính đĩa; kim phút / giờ dày 0,1 dài 0,5 / 0,3 (theo cỡ × half),
# chấm giây 0,05 ở 0,71. Thẻ nhạc bo tròn hết cao 65 dp (ảnh bìa 55 dp, tên 15 sp Medium, nghệ sĩ 14 sp tối hơn 40%), nút phát tròn 60 dp cách 15 dp.
# Màn ngang (app mẫu dùng chung layout dọc nên mặt số rất nhỏ): mặt số giữa màn; không nhạc thì thông báo trái, thời tiết + pin phải; có nhạc thì
# thông báo, thời tiết, pin trái, trình phát phải.
RP_MED = BzFont(instancer.instantiateVariableFont(TTFont(ROOT / "tools/fonts/variable/google_sans_variable.ttf"),
                                                  {"wght": 500, "GRAD": 0, "opsz": 18}, inplace=False), "rpm")
RP_FONTS = ('<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Google+Sans:wght@400;500&display=swap">'
            '<style>.cpr{font-family:"Google Sans";font-weight:400}.rpm{font-family:"Google Sans";font-weight:500}</style>')
RP_COLORS = {"face": "#121218", "hour": "#A6E3A1", "minute": "#CED4DA", "second": "#ADB5BD", "date": "#CED4DA", "noti": "#B9BEC4",
             "media": "#B9BEC4", "media_bg": "#121218", "battery": "#7C7F83", "weather": G}
RP_REF = {**RP_COLORS, "hour": "#FFFFFF"}
RP_PURPLE = {**RP_COLORS, "face": PU}
RP_NO_ART = "#3A3A3A"  # ô ảnh bìa khi app nhạc không gửi ảnh (Grey3A)
RP_N = [0]


def rp_id():
    RP_N[0] += 1
    return f"rp{RP_N[0]}"


def rp_dark(c, k=0.4):
    return "#" + "".join(f"{int(int(c[i:i + 2], 16) * (1 - k)):02X}" for i in (1, 3, 5))


def rp_point(cx, cy, deg, r):
    a = math.radians(deg)
    return cx + math.sin(a) * r, cy - math.cos(a) * r


def rp_face(cx, cy, r):
    """Viền 12 gợn như Android12ClockBg2: mỗi 15° một đường cong bậc hai, điểm điều khiển xen kẽ ngoài / trong 6%."""
    x, y = rp_point(cx, cy, 0, r)
    d, sign = f"M{f(x)},{f(y)}", 1
    for deg in range(0, 360, 15):
        qx, qy = rp_point(cx, cy, deg + 7.5, r * (1 + sign * .06))
        ex, ey = rp_point(cx, cy, deg + 15, r)
        d += f" Q{f(qx)},{f(qy)} {f(ex)},{f(ey)}"
        sign = -sign
    return d + "Z"


def rp_dial(cx, cy, half, hh=10, mm=8, ss=42, colors=RP_COLORS, size=.8, outline=False, date=True):
    k, s = size * half, ""
    rd = .9 * k
    s += (f'<path d="{rp_face(cx, cy, rd)}" fill="none" stroke="{colors["face"]}" stroke-width="2"/>' if outline
          else f'<path d="{rp_face(cx, cy, rd)}" fill="{colors["face"]}"/>')
    if date:
        pid, ar = rp_id(), .75 * rd
        s += (f'<path id="{pid}" d="M{f(cx - ar)},{f(cy)} A{f(ar)},{f(ar)} 0 0 1 {f(cx + ar)},{f(cy)}" fill="none"/>'
              f'<text class="rpm" font-size="{f(.085 * half)}" fill="{colors["date"]}"><textPath href="#{pid}" startOffset="50%" '
              f'text-anchor="middle" xml:space="preserve">Th 3  21</textPath></text>')
    mi, hr = mm + ss / 60, hh % 12 + (mm + ss / 60) / 60
    for deg, length, color in ((mi * 6, .5 * k, colors["minute"]), (hr * 30, .3 * k, colors["hour"])):
        x, y = rp_point(cx, cy, deg, length)
        s += f'<line x1="{f(cx)}" y1="{f(cy)}" x2="{f(x)}" y2="{f(y)}" stroke="{color}" stroke-width="{f(.1 * k)}" stroke-linecap="round"/>'
    x, y = rp_point(cx, cy, ss * 6, .71 * k)
    return s + f'<circle cx="{f(x)}" cy="{f(y)}" r="{f(.05 * k)}" fill="{colors["second"]}"/>'


def rp_icons(cx, cy, n, color):
    g, gap = 18, 14
    w = n * g + (n - 1) * gap
    shapes = icon_shapes(color)
    return "".join(shapes[i % 3](cx - w / 2 + i * (g + gap), cy - g / 2, g) for i in range(n))


def rp_latest(cx, cy, color, w=330, title="Mai", text="Tối nay đi ăn nhé, nhớ mang ô"):
    tw = min(max(CP_REG.width(title, 16), CP_REG.width(text, 14)), w - 40)
    while CP_REG.width(text, 14) > tw and len(text) > 2:
        text = text[:-2].rstrip() + "…"
    x = cx - (40 + tw) / 2
    return (f'<circle cx="{f(x + 14)}" cy="{f(cy)}" r="14" fill="{color}"/>' + icon_shapes("#000")[0](x + 5, cy - 9, 18)
            + CP_REG.text(x + 40, cy - 3, title, 16, color) + CP_REG.text(x + 40, cy + 15, text, 14, rp_dark(color)))


def rp_notifications(cx, cy, colors, n=3, latest=False, w=330):
    if not latest:
        return rp_icons(cx, cy, n, colors["noti"]) if n else ""
    return (rp_icons(cx, cy - 30, n, colors["noti"]) if n else "") + rp_latest(cx, cy + 18 if n else cy, colors["noti"], w)


def rp_weather(cx, cy, colors):
    w = 26 + CP_REG.width("24°", 14)
    return cloud(cx - w / 2 - 1, cy + 6, .62, colors["weather"], 1.6) + CP_REG.text(cx - w / 2 + 26, cy + 5, "24°", 14, colors["weather"])


def rp_battery(cx, cy, colors, charging=False):
    g, fs = 16.0, 0.915 * 16
    t = "72%"
    w = 0.522 * g + 0.717 * g + CP_REG.width(t, fs)
    x = cx - w / 2
    return (f'<g opacity="{1 if charging else .8}">{bz_battery(x, cy - g / 2, g, colors["battery"], charging)}'
            f'{CP_REG.text(x + 1.239 * g, cy + 0.34 * fs, t, fs, colors["battery"])}</g>')


def rp_player(x, top, w, colors, outline=False, paused=False, art=True):
    """Thẻ nhạc bo tròn hết + nút phát tròn riêng như media_widget_lt; viền 2 dp ở kiểu viền."""
    pill, cy, bg, c = w - 75, top + 32.5, colors["media_bg"], colors["media"]
    s = (f'<rect x="{f(x + 1)}" y="{f(top + 1)}" width="{f(pill - 2)}" height="63" rx="31.5" fill="none" stroke="{bg}" stroke-width="2"/>'
         if outline else f'<rect x="{f(x)}" y="{f(top)}" width="{f(pill)}" height="65" rx="32.5" fill="{bg}"/>')
    s += f'<circle cx="{f(x + 32.5)}" cy="{f(cy)}" r="27.5" fill="{"#C9A3E8" if art else RP_NO_ART}"/>'
    tx = x + 72
    tw = pill - 84
    t1 = CP_REG.fit("That Cool Song", 15, tw)
    s += RP_MED.text(tx + tw / 2, cy - 3, t1, 15, c, "middle") + CP_REG.text(tx + tw / 2, cy + 16, "It's Artist", 14, rp_dark(c), "middle")
    bx = x + pill + 45
    s += (f'<circle cx="{f(bx)}" cy="{f(cy)}" r="29" fill="none" stroke="{bg}" stroke-width="2"/>' if outline
          else f'<circle cx="{f(bx)}" cy="{f(cy)}" r="30" fill="{bg}"/>')
    if paused:
        return s + f'<path d="M{f(bx - 5)},{f(cy - 8)} L{f(bx + 8)},{f(cy)} L{f(bx - 5)},{f(cy + 8)} Z" fill="{c}"/>'
    return (s + f'<rect x="{f(bx - 6)}" y="{f(cy - 8)}" width="4" height="16" rx="1" fill="{c}"/>'
            f'<rect x="{f(bx + 2)}" y="{f(cy - 8)}" width="4" height="16" rx="1" fill="{c}"/>')



def rp_portrait(hh=10, mm=8, ss=42, colors=RP_COLORS, size=.8, outline=False, date=True, weather=False, media=False, paused=False,
                latest=False, n=3, charging=False, art=True):
    top, bottom = 200, PH - 152
    cy = (top + bottom) / 2
    s = rp_notifications(PW / 2, 100, colors, n, latest)
    s += rp_dial(PW / 2, cy, PW / 2, hh, mm, ss, colors, size, outline, date)
    reach = PW / 2 * size * .9 * 1.06
    if weather:
        s += rp_weather(PW / 2, cy + reach + (bottom - cy - reach) / 2, colors)
    if media:
        s += rp_player(40, bottom + 10, PW - 80, colors, outline, paused, art)
    return s + rp_battery(PW / 2, PH - 30 - 9, colors, charging)


def rp_landscape(hh=10, mm=8, ss=42, colors=RP_COLORS, size=.8, outline=False, weather=False, media=False, paused=False, latest=False, n=3,
                 charging=False):
    s = rp_dial(W / 2, CY, H / 2, hh, mm, ss, colors, size, outline)
    lx, rx = 157, 716
    if not media:
        s += rp_notifications(lx, CY, colors, n, latest, 250)
        if weather:
            return s + rp_weather(rx, CY - 16, colors) + rp_battery(rx, CY + 18, colors, charging)
        return s + rp_battery(rx, CY, colors, charging)
    parts = []
    if n or latest:
        parts.append((84 if latest and n else 30 if latest else 18, lambda y: rp_notifications(lx, y, colors, n, latest, 250)))
    if weather:
        parts.append((20, lambda y: rp_weather(lx, y, colors)))
    parts.append((16, lambda y: rp_battery(lx, y, colors, charging)))
    h = sum(p[0] for p in parts) + 22 * (len(parts) - 1)
    y = CY - h / 2
    for ph, draw in parts:
        s += draw(y + ph / 2)
        y += ph + 22
    return s + rp_player(rx - 133, CY - 32.5, 266, colors, outline, paused)


def rp_reference_landscape():
    """App mẫu ở màn ngang: dùng chung layout dọc nên vùng mặt số chỉ cao 41 dp, đĩa bán kính khoảng 15 dp."""
    top, bottom = 200, H - 152
    return (rp_icons(W / 2, 100, 3, RP_REF["noti"]) + rp_dial(W / 2, (top + bottom) / 2, (bottom - top) / 2, colors=RP_REF)
            + rp_battery(W / 2, H - 39, RP_REF))


def rp_strip():
    k, s = 0.4, ""
    for i, (label, kw) in enumerate((("Mặc định: đĩa đặc, kim giờ xanh lá", {}),
                                      ("Màu như app mẫu (kim giờ trắng)", {"colors": RP_REF, "hh": 7, "mm": 21, "ss": 5}),
                                      ("Kiểu viền, mặt số tím", {"outline": True, "colors": RP_PURPLE, "hh": 4, "mm": 38, "ss": 50}),
                                      ("Cỡ 110%, tắt ngày", {"size": 1.1, "date": False, "hh": 21, "mm": 47, "ss": 15}))):
        x = 36 + i * 212
        s += (f'<g transform="translate({x} 14) scale({k})"><clipPath id="rpc{i}"><rect width="{PW}" height="{PH}" rx="34"/></clipPath>'
              f'<rect width="{PW}" height="{PH}" rx="34" fill="#000" stroke="#333" stroke-width="2"/><g clip-path="url(#rpc{i})">')
        s += rp_portrait(**kw) + "</g></g>" + txt(x + PW * k / 2, 14 + PH * k + 22, label, 13, DM, 400, "middle")
    return (f'<p style="font-size:13px;color:var(--text-secondary);margin:1.25rem 0 6px">Kiểu mặt số và màu (màu nào cũng có thể là gradient)</p>'
            f'<svg viewBox="0 0 {W} 400" width="100%" role="img" aria-label="Kiểu và màu" '
            f'style="display:block;font-family:\'Plus Jakarta Sans\',sans-serif" fill="{G}">'
            f'<rect width="{W}" height="400" rx="20" fill="#111"/>{s}</svg>')


ripple = [
    RP_FONTS,
    rp_strip(),
    '<div style="font-size:0">',
    frame_portrait("D1 · Dọc, mặc định 10:08:42 (cũng là thẻ ở lưới chủ đề): icon thông báo trên, pin dưới; trên máy chấm giây chạy vòng",
                   rp_portrait()),
    frame_portrait("D2 · Dọc, 08:30, thời tiết dưới mặt số, đang phát nhạc (vuốt ngang thẻ để chuyển bài)",
                   rp_portrait(8, 30, 12, weather=True, media=True)),
    frame_portrait("D3 · Dọc, 21:47, đang sạc, thông báo mới nhất, nhạc đang dừng; app nhạc không có ảnh bìa",
                   rp_portrait(21, 47, 33, charging=True, latest=True, n=1, media=True, paused=True, art=False)),
    frame_portrait("D4 · Dọc, 23:10, kiểu viền, cỡ 110%, tắt ngày, mặt số tím",
                   rp_portrait(23, 10, 51, RP_PURPLE, 1.1, outline=True, date=False, n=2)),
    '</div>',
    frame("N1 · Ngang, không nhạc: thông báo trái, thời tiết + pin phải; mặt số giữa màn", rp_landscape(weather=True)),
    frame("N2 · Ngang, 08:30, đang phát nhạc: thông báo, thời tiết, pin trái; trình phát phải",
          rp_landscape(8, 30, 12, weather=True, media=True, latest=True, n=2)),
    frame("N3 · Ngang, 21:47, kiểu viền, đang sạc, nhạc đang dừng",
          rp_landscape(21, 47, 33, RP_PURPLE, outline=True, charging=True, media=True, paused=True)),
    frame("N4 · Để so: app mẫu ở màn ngang dùng chung layout dọc, mặt số chỉ còn bán kính khoảng 15 dp", rp_reference_landscape()),
]


# TODO(coum-face): mục mock của chủ đề (hàm vẽ + danh sách khung)
# ================================================================== NHỊP GIÂY (Coum Face Clock của Muviz Edge; số đo lấy từ CoumTick và layout oct23_screen_layout (chỉ
# có bản dọc) trong APK, không từ ảnh chụp). u = nửa cạnh ngắn, L = nửa cạnh dài. 60 vạch từ 0,8u tới 0,9u nét 0,007u xoay theo chiều kim 6°/giây
# theo giờ thật; gradient của vòng trải từ tâm − 0,7u tới tâm + 0,7u, đứng yên theo màn (vạch đổi màu khi trôi qua); hai vùng tròn bán kính u ở
# tâm ± 0,7L làm vòng chìm dần trên / dưới (mất hẳn trong 0,65u); chấm giây ở tâm + 0,84u bên phải, bán kính 0,12u, viền 0,007u + số giây 0,115u
# Google Sans Regular theo màu giờ, khoét vòng. Giờ Google Sans Regular 0,3664u × cỡ (72 sp × cỡ trên màn 393 dp) giãn 0,02 em, ngày in hoa
# 0,117u × cỡ giãn 0,12 em, cụm giờ + ngày giữa vòng; thời tiết treo dưới ngày. Dòng pin cách đáy 60 dp (icon 15 dp, chữ 14 sp giãn 0,05), trình
# phát ngay trên (lùi 40 dp · tên 13 sp Bold / nghệ sĩ 11,5 sp in hoa giãn 0,25 tối hơn 30% · tới 40 dp, lề 40 dp); hàng icon thông báo (17 dp
# cách 10 dp) + thông báo mới nhất (icon 17 dp + chữ 13 sp in hoa giãn 0,1) đối xứng ở trên. Màn ngang: vòng giữa màn cỡ như màn dọc, thông báo
# trái, pin (+ trình phát) phải.
TK_TROPIC = ("#F0AB32", "#07A7B9", "#DD4B1C")  # GradientSwatchValue.TROPIC hướng dọc: màu nhấn đề xuất, chưa chủ đề nào dùng
TK_SUNSET = ("#FFCC70", "#C850C0", "#4158D0")  # mặc định của app mẫu (trùng Dải màu)
TK_LILAC_SEA = ("#D8B5FF", "#63A4B6", "#1EAE98")
TK_COLORS = {"ring": TK_TROPIC, "dir": "v", "clock": "#E0E0E0", "date": "#9A9A9A", "noti": "#E0E0E0", "battery": "#E0E0E0",
             "media": "#E0E0E0", "weather": G}
TK_REF = {**TK_COLORS, "ring": TK_SUNSET, "clock": "#B39DDB", "date": rp_dark("#B39DDB", .3), "noti": "#B39DDB", "battery": "#B39DDB",
          "media": "#B39DDB"}
TK_SEA = {**TK_COLORS, "ring": TK_LILAC_SEA, "dir": "h", "clock": "#D8B5FF", "date": rp_dark("#D8B5FF", .3)}
TK_MONO = {**TK_COLORS, "ring": ("#9A9A9A",)}
TK_DATE = "TH 2, 26 THG 10"
TK_ASC, TK_LINE = 0.966, 1.252  # Google Sans: đỉnh dòng → đường gốc, cao dòng (em)
TK_LIVE = []
TK_N = [0]


def tk_ticks_def():
    """60 vạch quanh gốc toạ độ ở u = 100 (dùng lại bằng <use> + scale), nét trắng để làm mặt nạ."""
    d = ""
    for i in range(60):
        a = math.radians(6 * i)
        sx, sy = math.sin(a), -math.cos(a)
        d += f"M{f(sx * 80)},{f(sy * 80)}L{f(sx * 90)},{f(sy * 90)}"
    return f'<path id="tkt" d="{d}" fill="none" stroke="#fff" stroke-width=".7"/>'


def tk_id():
    TK_N[0] += 1
    return f"tk{TK_N[0]}"


def tk_dial(cx, cy, u, big, hh=10, mm=8, ss=42, colors=TK_COLORS, size=70, date=True, weather=False, live=False, tu=None):
    """Vòng 60 vạch (tô qua mặt nạ để gradient đứng yên khi vạch xoay), hai vùng chìm ở tâm ± 0,7 big, chấm giây khoét vòng, giờ + ngày giữa
    vòng (cỡ chữ theo tu, mặc định u)."""
    gid = tk_id()
    ring, s = colors["ring"], ""
    if len(ring) > 1:
        v = colors["dir"] == "v"
        grad = gl_grad(gid + "g", cx if v else cx - .7 * u, cy - .7 * u if v else cy, cx if v else cx + .7 * u, cy + .7 * u if v else cy, ring)
        paint = f"url(#{gid}g)"
    else:
        grad, paint = "", ring[0]
    bx, br = cx + .84 * u, .12 * u
    rid = f' id="{gid}r"' if live else ""
    s += (f'<defs>{grad}<mask id="{gid}t" maskUnits="userSpaceOnUse" x="{f(cx - u)}" y="{f(cy - u)}" width="{f(2 * u)}" height="{f(2 * u)}">'
          f'<use href="#tkt"{rid} transform="translate({f(cx)} {f(cy)}) rotate({f(ss * 6)}) scale({f(u / 100)})"/></mask>'
          f'<radialGradient id="{gid}d"><stop offset=".65" stop-color="#000"/><stop offset="1" stop-color="#000" stop-opacity="0"/></radialGradient>'
          f'<mask id="{gid}f" maskUnits="userSpaceOnUse" x="{f(cx - u)}" y="{f(cy - u)}" width="{f(2 * u)}" height="{f(2 * u)}">'
          f'<rect x="{f(cx - u)}" y="{f(cy - u)}" width="{f(2 * u)}" height="{f(2 * u)}" fill="#fff"/>'
          f'<circle cx="{f(cx)}" cy="{f(cy - .7 * big)}" r="{f(u)}" fill="url(#{gid}d)"/>'
          f'<circle cx="{f(cx)}" cy="{f(cy + .7 * big)}" r="{f(u)}" fill="url(#{gid}d)"/>'
          f'<circle cx="{f(bx)}" cy="{f(cy)}" r="{f(br)}" fill="#000"/></mask></defs>'
          f'<g mask="url(#{gid}f)"><rect x="{f(cx - u)}" y="{f(cy - u)}" width="{f(2 * u)}" height="{f(2 * u)}" fill="{paint}" '
          f'mask="url(#{gid}t)"/></g>')
    c = colors["clock"]
    s += f'<circle cx="{f(bx)}" cy="{f(cy)}" r="{f(br)}" fill="none" stroke="{c}" stroke-width="{f(.007 * u)}"/>'
    sec, fs = f"{ss:02d}", .115 * u
    lo, top, hi, bottom = CP_REG.ink(sec, fs)
    num = CP_REG.text(bx - (lo + hi) / 2, cy - (top + bottom) / 2, sec, fs, c)
    tu = tu or u
    ts, ds = .3664 * tu * size / 100, .117 * tu * size / 100
    h = TK_LINE * ts + (TK_LINE * ds if date else 0)
    gt = cy - h / 2
    clock = cp_text(CP_REG, cx, gt + TK_ASC * ts, f"{hh:02d}:{mm:02d}", ts, c, "middle", .02)
    if live:
        num = num.replace("<text ", f'<text id="{gid}s" ', 1)
        clock = clock.replace("<text ", f'<text id="{gid}h" ', 1)
        TK_LIVE.append((gid, cx, cy, u))
    s += num + clock
    if date:
        s += cp_text(CP_REG, cx, gt + TK_LINE * ts + TK_ASC * ds, TK_DATE, ds, colors["date"], "middle", .12)
    if weather:
        s += rp_weather(cx, cy + h / 2 + 18, colors)
    return s


def tk_live_script():
    """Khung live: vạch xoay 6°/giây mượt từng khung hình, số giây và giờ theo đồng hồ của máy xem mock."""
    frames = ",".join(f'["{lid}",{f(cx)},{f(cy)},{f(u)}]' for lid, cx, cy, u in TK_LIVE)
    return ('<script>(function(){var c=[' + frames + '];function p(n){return(n<10?"0":"")+n}'
            'function t(){var d=new Date(),s=d.getSeconds()+d.getMilliseconds()/1e3;'
            'c.forEach(function(e){var id=e[0],x=e[1],y=e[2],u=e[3],r=document.getElementById(id+"r");'
            'if(r)r.setAttribute("transform","translate("+x+" "+y+") rotate("+(6*s).toFixed(2)+") scale("+u/100+")");'
            '[["s",p(d.getSeconds()),x+.84*u],["h",p(d.getHours())+":"+p(d.getMinutes()),x]].forEach(function(k){'
            'var l=document.getElementById(id+k[0]);if(!l||l.textContent===k[1])return;l.textContent=k[1];var b=l.getBBox();'
            'l.setAttribute("x",(k[2]-b.width/2).toFixed(1));});});requestAnimationFrame(t);}t();})();</script>')


def tk_icons(cx, cy, n, color):
    g, gap = 17, 10
    w = n * g + (n - 1) * gap
    shapes = icon_shapes(color)
    return "".join(shapes[i % 3](cx - w / 2 + i * (g + gap), cy - g / 2, g) for i in range(n))


def tk_latest(cx, cy, w, color, text="MAI: TỐI NAY ĐI ĂN NHÉ"):
    """Thông báo mới nhất như notify_lt: icon 17 dp, cách 10 dp, chữ 13 sp in hoa giãn 0,1 (dài thì chạy chữ; mock cắt "…")."""
    fs, ls = 13, .1
    while cp_width(CP_REG, text, fs, ls) > w - 27 and len(text) > 1:
        text = text[:-2].rstrip() + "…"
    x = cx - (27 + cp_width(CP_REG, text, fs, ls)) / 2
    return icon_shapes(color)[0](x, cy - 8.5, 17) + cp_text(CP_REG, x + 27, cy + .34 * fs, text, fs, color, ls=ls)


def tk_notifications(cx, cy, colors, n=3, latest=False, w=313, center=False):
    """Hàng icon rồi thông báo mới nhất cách 12 dp; cy là hàng đầu (center: cy là giữa cả nhóm)."""
    rows = ([lambda y: tk_icons(cx, y, n, colors["noti"])] if n else []) + ([lambda y: tk_latest(cx, y, w, colors["noti"])] if latest else [])
    y = cy - (len(rows) - 1) * 14.5 if center else cy
    s = ""
    for draw in rows:
        s += draw(y)
        y += 29
    return s


def tk_battery(cx, cy, colors, charging=False):
    """Dòng pin như battery_lt: icon 15 dp, cách 5 dp chữ 14 sp giãn 0,05."""
    g, fs, ls, color = 15.0, 14.0, .05, colors["battery"]
    x = cx - (20 + cp_width(CP_REG, "72%", fs, ls)) / 2
    return bz_battery(x + (15 - .522 * g) / 2, cy - g / 2, g, color, charging) + cp_text(CP_REG, x + 20, cy + .34 * fs, "72%", fs, color, ls=ls)


def tk_player(cx, cy, w, colors, paused=False, title="THAT COOL SONG"):
    """media_widget_lt: nút lùi / tới 40 dp (icon 12 dp), tên Bold 13 sp in hoa giãn 0,25, cách 8 dp nghệ sĩ 11,5 sp in hoa giãn 0,25 tối hơn
    30%; chạm tên để phát / dừng, dừng thì chữ mờ 0,6 (nâng cấp như La bàn)."""
    color, op, x, s = colors["media"], .6 if paused else 1.0, cx - w / 2, ""
    for bx, d in ((x + 20, -1), (x + w - 20, 1)):
        s += (f'<path d="M{f(bx - 4 * d)},{f(cy - 6)} L{f(bx + 3 * d)},{f(cy)} L{f(bx - 4 * d)},{f(cy + 6)} Z" fill="{color}" '
              f'stroke="{color}" stroke-width="1" stroke-linejoin="round"/>'
              f'<rect x="{f(bx + 4 * d - (1.6 if d < 0 else 0))}" y="{f(cy - 6)}" width="1.6" height="12" rx=".8" fill="{color}"/>')
    t = title
    while cp_width(CP_BOLD, t, 13, .25) > w - 100 and len(t) > 1:
        t = t[:-2].rstrip() + "…"
    top = cy - (TK_LINE * 13 + 8 + TK_LINE * 11.5) / 2
    s += cp_text(CP_BOLD, cx, top + TK_ASC * 13, t, 13, color, "middle", .25, op)
    return s + cp_text(CP_REG, cx, top + TK_LINE * 13 + 8 + TK_ASC * 11.5, "IT'S ARTIST", 11.5, rp_dark(color, .3), "middle", .25, op)


def tk_portrait(hh=10, mm=8, ss=42, colors=TK_COLORS, size=70, date=True, weather=False, media=False, paused=False, latest=False, n=3,
                charging=False, live=False):
    s = tk_notifications(PW / 2, 78.5, colors, n, latest)
    s += tk_dial(PW / 2, PH / 2, PW / 2, PH / 2, hh, mm, ss, colors, size, date, weather, live)
    if media:
        s += tk_player(PW / 2, PH - 78.5 - 46.5, PW - 80, colors, paused)
    return s + tk_battery(PW / 2, PH - 78.5, colors, charging)


def tk_landscape(hh=10, mm=8, ss=42, colors=TK_COLORS, size=70, date=True, weather=False, media=False, paused=False, latest=False, n=3,
                 charging=False, live=False):
    """Vòng giữa màn cỡ như màn dọc; hai vùng bên rộng 209 dp đối xứng qua tâm (bên phải bắt đầu sau chấm giây): thông báo trái, pin phải,
    có nhạc thì trình phát ngay trên dòng pin (cả nhóm giữa chiều cao)."""
    s = tk_dial(W / 2, CY, H / 2, W / 2, hh, mm, ss, colors, size, date, weather, live)
    lx, rx, zone = 128.5, 744.5, 209
    s += tk_notifications(lx, CY, colors, n, latest, zone, center=True)
    if media:
        return s + tk_player(rx, CY - 23.25, zone, colors, paused) + tk_battery(rx, CY + 23.25, colors, charging)
    return s + tk_battery(rx, CY, colors, charging)


def tk_reference_landscape():
    """App mẫu ở màn ngang: dùng chung layout dọc, vòng và chấm giây tính theo nửa bề ngang (chấm giây to 52 dp), hai vùng chìm ở tâm ± 0,7 nửa
    chiều cao nên chỉ còn hai cung lớn sát mép; giờ, ngày, dòng pin giữ cỡ sp như màn dọc."""
    return tk_dial(W / 2, CY, W / 2, H / 2, colors=TK_REF, tu=PW / 2) + tk_battery(W / 2, H - 78.5, TK_REF)


def tk_strip():
    k, s = 0.4, ""
    for i, (label, kw) in enumerate((("Mặc định: Tropic dọc", {}),
                                      ("Như app mẫu", {"colors": TK_REF, "hh": 9, "mm": 50, "ss": 17}),
                                      ("Gradient ngang", {"colors": TK_SEA, "hh": 4, "mm": 38, "ss": 5}),
                                      ("Một màu, cỡ 90, tắt ngày", {"colors": TK_MONO, "size": 90, "date": False, "hh": 21, "mm": 47,
                                                                          "ss": 29}))):
        x = 36 + i * 212
        s += (f'<g transform="translate({x} 14) scale({k})"><clipPath id="tkc{i}"><rect width="{PW}" height="{PH}" rx="34"/></clipPath>'
              f'<rect width="{PW}" height="{PH}" rx="34" fill="#000" stroke="#333" stroke-width="2"/><g clip-path="url(#tkc{i})">')
        s += tk_portrait(**kw) + "</g></g>" + txt(x + PW * k / 2, 14 + PH * k + 22, label, 13, DM, 400, "middle")
    return (f'<p style="font-size:13px;color:var(--text-secondary);margin:1.25rem 0 6px">Màu vòng và cỡ (màu nào cũng có thể là gradient, '
            f'gradient của vòng theo 4 hướng + đảo chiều)</p>'
            f'<svg viewBox="0 0 {W} 400" width="100%" role="img" aria-label="Màu và cỡ" '
            f'style="display:block;font-family:\'Plus Jakarta Sans\',sans-serif" fill="{G}">'
            f'<rect width="{W}" height="400" rx="20" fill="#111"/>{s}</svg>')


tick = [
    CP_FONTS,
    tk_strip(),
    '<div style="font-size:0">',
    frame_portrait("D1 · Dọc, mặc định (cũng là thẻ ở lưới chủ đề): vòng trôi theo chiều kim 6°/giây và số giây chạy theo giờ thật",
                   tk_portrait(live=True)),
    frame_portrait("D2 · Dọc, 08:30, thông báo mới nhất trên, thời tiết dưới ngày, đang phát nhạc",
                   tk_portrait(8, 30, 12, weather=True, media=True, latest=True, n=2)),
    frame_portrait("D3 · Dọc, 21:47, đang sạc, nhạc đang dừng (chữ mờ), một thông báo",
                   tk_portrait(21, 47, 33, charging=True, media=True, paused=True, n=1)),
    frame_portrait("D4 · Dọc, 23:10, màu như app mẫu, cỡ 90%, không thông báo",
                   tk_portrait(23, 10, 51, TK_REF, 90, n=0)),
    '</div>',
    frame("N1 · Ngang, không nhạc (live): thông báo trái, pin phải; vòng giữa màn cỡ như màn dọc, thời tiết dưới ngày",
          tk_landscape(weather=True, live=True)),
    frame("N2 · Ngang, 08:30, đang phát nhạc: thông báo + thông báo mới nhất trái; trình phát + pin phải",
          tk_landscape(8, 30, 12, media=True, latest=True, n=2)),
    frame("N3 · Ngang, 21:47, màu như app mẫu, đang sạc, nhạc đang dừng", tk_landscape(21, 47, 33, TK_REF, media=True, paused=True, charging=True,
                                                                                  n=1)),
    frame("N4 · Để so: app mẫu ở màn ngang dùng chung layout dọc, vòng tính theo bề ngang nên chỉ còn hai cung lớn sát mép",
          tk_reference_landscape()),
]
tick.append(tk_live_script())
# TODO(big-overlap): mục mock của chủ đề (hàm vẽ + danh sách khung)


for name, summary, defs, frames in [
    ("w_classic", "Bảy bố cục ngang của chủ đề Cổ điển theo các khối đang bật", classic_defs, classic),
    ("w_jersey", "Bố cục ngang của chủ đề Số áo: ngày, bốn chữ số một hàng, dòng pin", jersey_defs, jersey),
    ("w_pebble", "Bốn bố cục ngang của chủ đề Sỏi", pebble_defs, pebble),
    ("w_capsule", "Chủ đề Viên nang: bốn khung dọc và bốn bố cục ngang", capsule_defs, capsule),
    ("w_expressive", "Chủ đề Thẻ (M3 Expressive): bốn khung dọc và bốn bố cục ngang", expressive_defs, expressive),
    ("w_tower", "Chủ đề Tháp (Long HyperOS): sáu kiểu đồng hồ, bốn khung dọc và bốn bố cục ngang", tower_defs, tower),
    ("w_chain", "Chủ đề Chuỗi (Bullet List): đồng hồ viên thuốc, bốn khung dọc và bốn bố cục ngang", chain_defs, chain),
    ("w_bezel", "Chủ đề Vành (iOS Fill Clock): hai kiểu vạch, bốn khung dọc và ba bố cục ngang", bezel_defs, bezel),
    ("w_compass", "Chủ đề La bàn (Ambitio Face): màu và cỡ mặt số, bốn khung dọc và ba bố cục ngang", compass_defs, compass),
    ("w_half", "Chủ đề Bán nguyệt (Half Concentric): màu, bốn khung dọc và ba bố cục ngang", HC_DEFS, half),
    ("w_gradient_line", "Chủ đề Dải màu (Gradient Line Clock): màu vạch, bốn khung dọc và bốn khung ngang", "", gradient_line),
    ("w_ripple", "Chủ đề Gợn sóng (Android 12): kiểu và màu, bốn khung dọc và bốn khung ngang", "", ripple),
    # TODO(coum-face): dòng ("w_…", …) của chủ đề
    ("w_tick", "Chủ đề Nhịp giây (Coum Face Clock): màu vòng và cỡ, bốn khung dọc và bốn khung ngang", tk_ticks_def(), tick),
    # TODO(big-overlap): dòng ("w_…", …) của chủ đề
]:
    code = widget(summary, defs, frames)
    OUT.mkdir(parents=True, exist_ok=True)
    (OUT / f"{name}.html").write_text(code, encoding="utf-8")
    print(name, len(code))
