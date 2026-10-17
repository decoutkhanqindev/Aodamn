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

for name, summary, defs, frames in [
    ("w_classic", "Bảy bố cục ngang của chủ đề Cổ điển theo các khối đang bật", classic_defs, classic),
    ("w_jersey", "Bố cục ngang của chủ đề Số áo: ngày, bốn chữ số một hàng, dòng pin", jersey_defs, jersey),
    ("w_pebble", "Bốn bố cục ngang của chủ đề Sỏi", pebble_defs, pebble),
    ("w_capsule", "Chủ đề Viên nang: bốn khung dọc và bốn bố cục ngang", capsule_defs, capsule),
    ("w_expressive", "Chủ đề Thẻ (M3 Expressive): bốn khung dọc và bốn bố cục ngang", expressive_defs, expressive),
]:
    code = widget(summary, defs, frames)
    OUT.mkdir(parents=True, exist_ok=True)
    (OUT / f"{name}.html").write_text(code, encoding="utf-8")
    print(name, len(code))
