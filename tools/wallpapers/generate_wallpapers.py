#!/usr/bin/env python3
"""Sinh ảnh nền AMOLED có sẵn (res/drawable/img_wallpaper_<tên>.xml, VectorDrawable 1080×2400) bằng code, không vẽ tay.

Chạy ở gốc repo:  python -I tools/wallpapers/generate_wallpapers.py
Xem thử ngoài Android: thêm --preview-svg <thư mục> để ghi cùng hình ra SVG, mở bằng trình duyệt.

Mỗi ảnh là một hàm trả (danh sách phần tử, vị trí giữ lại khi cắt cho bố cục ngang); ngẫu nhiên dùng seed cố định nên chạy lại ra
đúng file cũ. Chỉ dùng tập con vẽ được 1-1 giữa SVG và VectorDrawable: path, màu đặc kèm alpha, gradient tuyến tính / toả tròn,
nhóm dịch / xoay / co giãn; không blur, mask, nét đứt. File sinh ra có dòng "Sinh bằng", đừng sửa tay: sửa hàm rồi chạy lại.
Đổi tên / thêm ảnh thì sửa WALLPAPERS ở cuối file và WallpaperValue (mã, nhãn, vị trí giữ lại phải khớp dòng in ra).
"""
import argparse
import math
import random
from pathlib import Path

W, H = 1080, 2400
MINT, BLUE, PURPLE, ORANGE, PINK, RED = '7FD1AE', '8AB4F8', 'C3A6FF', 'FFB27A', 'FF9EC4', 'E5484D'
WHITE, WARM, CREAM, ICE, GREY = 'FFFFFF', 'FFF0DC', 'FFF6E6', 'D9E6FF', 'EDEDED'
# Vùng đồng hồ + ngày + pin: giữ gần như trống.
CLOCK = (230, 820, 850, 1560)
MARKER = 'Sinh bằng tools/wallpapers/generate_wallpapers.py'
SIDE = {'TopCenter': 'trên', 'BottomCenter': 'dưới', 'Center': 'giữa'}


def n(v):
    s = f'{v:.1f}'
    if s.endswith('.0'):
        s = s[:-2]
    return '0' if s == '-0' else s


def a(v):
    return f'{v:.2f}'.rstrip('0').rstrip('.') if v < 1 else '1'


class Lin:
    def __init__(self, x1, y1, x2, y2, stops):
        self.x1, self.y1, self.x2, self.y2, self.stops = x1, y1, x2, y2, stops


class Rad:
    def __init__(self, cx, cy, r, stops):
        self.cx, self.cy, self.r, self.stops = cx, cy, r, stops


def P(d, fill=None, fa=1.0, stroke=None, sa=1.0, sw=None, cap='round', join='round'):
    return dict(k='path', d=d, fill=fill, fa=fa, stroke=stroke, sa=sa, sw=sw, cap=cap, join=join)


def G(children, tx=0.0, ty=0.0, rot=0.0, px=0.0, py=0.0, sx=1.0, sy=1.0):
    return dict(k='group', children=children, tx=tx, ty=ty, rot=rot, px=px, py=py, sx=sx, sy=sy)


def circle(cx, cy, r):
    return f'M{n(cx - r)},{n(cy)}a{n(r)},{n(r)} 0 1,0 {n(2 * r)},0a{n(r)},{n(r)} 0 1,0 {n(-2 * r)},0z'


def ellipse_arc(cx, cy, rx, ry, top):
    return f'M{n(cx - rx)},{n(cy)}A{n(rx)},{n(ry)} 0 0,{1 if top else 0} {n(cx + rx)},{n(cy)}'


def ellipse(cx, cy, rx, ry):
    return ellipse_arc(cx, cy, rx, ry, True) + f'A{n(rx)},{n(ry)} 0 0,1 {n(cx - rx)},{n(cy)}z'


def smooth(pts, closed):
    m = len(pts)
    get = (lambda i: pts[i % m]) if closed else (lambda i: pts[max(0, min(m - 1, i))])
    d = f'M{n(pts[0][0])},{n(pts[0][1])}'
    for i in range(m if closed else m - 1):
        p0, p1, p2, p3 = get(i - 1), get(i), get(i + 1), get(i + 2)
        c1 = (p1[0] + (p2[0] - p0[0]) / 6, p1[1] + (p2[1] - p0[1]) / 6)
        c2 = (p2[0] - (p3[0] - p1[0]) / 6, p2[1] - (p3[1] - p1[1]) / 6)
        d += f'C{n(c1[0])},{n(c1[1])} {n(c2[0])},{n(c2[1])} {n(p2[0])},{n(p2[1])}'
    return d + ('z' if closed else '')


def chunked(parts, limit):
    """Gộp các đoạn path thành các chuỗi không dài quá `limit` ký tự (mỗi đoạn là một hình đóng riêng)."""
    chunks, cur = [], ''
    for part in parts:
        if cur and len(cur) + len(part) > limit:
            chunks.append(cur)
            cur = ''
        cur += part
    return chunks + ([cur] if cur else [])


def in_box(x, y, box, pad=0):
    return box[0] - pad < x < box[2] + pad and box[1] - pad < y < box[3] + pad


def bg():
    return P(f'M0,0h{W}v{H}h-{W}z', fill='000000')


def glow(cx, cy, r, color, alpha):
    return P(circle(cx, cy, r), fill=Rad(cx, cy, r, [(0, color, alpha), (0.45, color, alpha * 0.4), (1, color, 0)]))


def stars(rng, count, box, rmin, rmax, color=WHITE, alphas=(0.3, 0.55, 0.85), avoid=None):
    buckets = [''] * len(alphas)
    placed = 0
    while placed < count:
        x, y = rng.uniform(box[0], box[2]), rng.uniform(box[1], box[3])
        if avoid and in_box(x, y, avoid):
            continue
        r = rmin + (rmax - rmin) * rng.random() ** 2
        buckets[min(len(alphas) - 1, int(rng.random() * len(alphas)))] += circle(x, y, r)
        placed += 1
    return [P(d, fill=color, fa=al) for d, al in zip(buckets, alphas) if d]


# ---------------------------------------------------------------- 1. Mưa sao băng
def meteor(rng):
    out = [bg(), glow(880, 260, 720, PURPLE, 0.1), glow(160, 700, 520, BLUE, 0.06)]
    out += stars(rng, 80, (0, 0, W, 1050), 1.4, 3.4)
    out += stars(rng, 30, (0, 1050, W, H), 1.2, 2.2, alphas=(0.18, 0.3), avoid=CLOCK)
    ang = math.radians(34)
    ux, uy = math.cos(ang), -math.sin(ang)
    heads = [(740, 360, 560, WHITE, 6), (540, 190, 380, MINT, 4), (950, 600, 330, BLUE, 4), (400, 520, 280, WHITE, 3),
             (690, 790, 210, MINT, 3), (250, 280, 190, BLUE, 3), (990, 150, 260, PURPLE, 3), (150, 760, 150, WHITE, 2.5)]
    for hx, hy, length, color, width in heads:
        tx, ty = hx + ux * length, hy + uy * length
        out.append(P(f'M{n(tx)},{n(ty)}L{n(hx)},{n(hy)}',
                     stroke=Lin(tx, ty, hx, hy, [(0, color, 0), (0.7, color, 0.35), (1, color, 0.95)]), sw=width))
        out.append(glow(hx, hy, width * 6, color, 0.45))
        out.append(P(circle(hx, hy, width * 0.75), fill=WHITE, fa=0.95))
    return out, 'TopCenter'


# ---------------------------------------------------------------- 2. Hành tinh vành đai
def planet(rng):
    cx, cy, r = 250, 2140, 560
    out = [bg(), glow(cx + 120, cy - 300, 900, PURPLE, 0.08)]
    out += stars(rng, 90, (0, 0, W, 1500), 1.3, 3.2, avoid=CLOCK)
    out += stars(rng, 20, (500, 1500, W, H), 1.2, 2.4, alphas=(0.3, 0.5))
    rings = [(980, 44, MINT, 0.08), (900, 26, BLUE, 0.2), (860, 6, PURPLE, 0.35), (820, 12, MINT, 0.32), (770, 4, ICE, 0.45)]

    def ring_group(top):
        return G([P(ellipse_arc(cx, cy, rx, rx * 0.21, top), stroke=c, sa=al, sw=w, cap='butt') for rx, w, c, al in rings],
                 rot=-16, px=cx, py=cy)

    out.append(ring_group(True))
    out.append(P(circle(cx, cy, r), fill=Rad(cx + 230, cy - 270, 820, [(0, '6B58A8', 1), (0.35, '2E2452', 1), (0.7, '0C0918', 1), (1, '000000', 1)])))
    out.append(P(circle(cx, cy, r - 3), stroke=Lin(cx + 400, cy - 400, cx - 100, cy + 100, [(0, MINT, 0.7), (0.5, MINT, 0.15), (1, MINT, 0)]), sw=6))
    out.append(ring_group(False))
    out.append(glow(890, 1700, 70, BLUE, 0.3))
    out.append(P(circle(890, 1700, 24), fill=Rad(880, 1690, 30, [(0, ICE, 0.9), (1, BLUE, 0.5)])))
    return out, 'BottomCenter'


# ---------------------------------------------------------------- 3. Quỹ đạo
def orbits(rng):
    cx, cy = 540, 2060
    out = [bg()]
    out += stars(rng, 70, (0, 0, W, 1450), 1.2, 3, avoid=CLOCK)
    out.append(glow(cx, cy, 460, ORANGE, 0.28))
    orbit_list = [(210, MINT, 0.32, 0.6, 9, MINT), (330, BLUE, 0.28, 2.4, 13, BLUE), (460, PURPLE, 0.25, 4.1, 8, PINK),
                  (610, MINT, 0.2, 1.3, 20, PURPLE), (780, BLUE, 0.16, 5.3, 12, ORANGE), (980, PURPLE, 0.12, 3.3, 7, GREY)]
    lines, bodies = [], []
    for rx, c, al, t, pr, pc in orbit_list:
        ry = rx * 0.3
        lines.append(P(ellipse(cx, cy, rx, ry), stroke=c, sa=al, sw=2.2))
        x, y = cx + rx * math.cos(t), cy + ry * math.sin(t)
        bodies.append(glow(x, y, pr * 3.2, pc, 0.35))
        bodies.append(P(circle(x, y, pr), fill=Rad(x - pr * 0.35, y - pr * 0.35, pr * 1.4, [(0, WHITE, 0.95), (0.4, pc, 0.95), (1, pc, 0.55)])))
    out.append(G(lines + bodies, rot=-9, px=cx, py=cy))
    out.append(P(circle(cx, cy, 72), fill=Rad(cx - 18, cy - 20, 90, [(0, CREAM, 1), (0.5, WARM, 0.95), (1, ORANGE, 0.85)])))
    return out, 'BottomCenter'


# ---------------------------------------------------------------- 4. Núi trăng
def ridge(rng, y0, amp, peak_x, peak_h, peak_w, rough=0.55):
    pts = [(-20, y0 + rng.uniform(-amp, amp) * 0.4), (W + 20, y0 + rng.uniform(-amp, amp) * 0.4)]
    disp = amp
    for _ in range(6):
        nxt = [pts[0]]
        for p, q in zip(pts, pts[1:]):
            nxt += [((p[0] + q[0]) / 2, (p[1] + q[1]) / 2 + rng.uniform(-disp, disp)), q]
        pts, disp = nxt, disp * rough
    return [(x, y - peak_h * math.exp(-((x - peak_x) / peak_w) ** 2)) for x, y in pts]


def poly(pts):
    return 'M' + 'L'.join(f'{n(x)},{n(y)}' for x, y in pts)


def mountains(rng):
    out = [bg(), P(f'M0,1500h{W}v900h-{W}z', fill=Lin(0, 1500, 0, 2150, [(0, BLUE, 0), (1, BLUE, 0.1)]))]
    out += stars(rng, 75, (0, 0, W, 1400), 1.2, 3, avoid=CLOCK)
    mx, my, mr = 800, 460, 92
    out.append(glow(mx, my, 430, GREY, 0.1))
    out.append(P(circle(mx, my, mr), fill=CREAM, fa=0.92))
    out.append(P(circle(mx + 42, my - 26, mr * 0.93), fill='000000'))
    layers = [(1880, 120, 300, 260, 200, '101A2A', BLUE, 0.35), (2040, 110, 760, 200, 170, '0A111C', MINT, 0.22),
              (2200, 80, 180, 140, 160, '05080D', None, 0)]
    for i, (y0, amp, px, ph, pw, fill, rim, rim_a) in enumerate(layers):
        pts = ridge(rng, y0, amp, px, ph, pw)
        out.append(P(poly(pts) + f'L{W + 20},{H}L-20,{H}z', fill=fill))
        if rim:
            out.append(P(poly(pts), stroke=rim, sa=rim_a, sw=3))
        if i < 2:
            out.append(P(f'M0,{y0 - 40}h{W}v260h-{W}z', fill=Lin(0, y0 - 40, 0, y0 + 220, [(0, GREY, 0), (0.6, GREY, 0.045), (1, GREY, 0)])))
    return out, 'BottomCenter'


# ---------------------------------------------------------------- 5. Đom đóm
def fireflies(rng):
    out = [bg(), glow(540, 2550, 1000, MINT, 0.12)]
    out += stars(rng, 35, (0, 0, W, 900), 1.1, 2.2, alphas=(0.2, 0.35))
    shades = [[], []]
    x = -30
    while x < W + 30:
        h = rng.uniform(90, 300) * (1.25 if rng.random() < 0.15 else 1)
        bend = rng.uniform(-60, 60)
        w = rng.uniform(3, 6)
        top = (x + bend, H - h)
        shades[rng.random() < 0.5].append(f'M{n(x - w)},{H}Q{n(x + bend * 0.2)},{n(H - h * 0.6)} {n(top[0])},{n(top[1])}'
                                          f'Q{n(x + bend * 0.25 + w)},{n(H - h * 0.55)} {n(x + w)},{H}z')
        x += rng.uniform(5, 12)
    # Màu đặc không alpha nên chia path ra nhiều mảnh không đổi hình; lint cảnh báo path dài hơn ~3000 ký tự.
    for blades, color in zip(shades, ('0B2018', '061510')):
        out += [P(chunk, fill=color) for chunk in chunked(blades, 2400)]
    halo, inner, core = '', '', ''
    placed = 0
    while placed < 30:
        fx, fy = rng.uniform(30, W - 30), rng.uniform(1450, 2330)
        if fy < 1650 and 200 < fx < 880:
            continue
        r = rng.uniform(3.5, 6.5) * (1.3 if fy > 2000 else 1)
        halo += circle(fx, fy, r * 6)
        inner += circle(fx, fy, r * 2.6)
        core += circle(fx, fy, r)
        placed += 1
    out += [P(halo, fill='D8F27A', fa=0.07), P(inner, fill='D8F27A', fa=0.2), P(core, fill='F2FBC2', fa=0.95)]
    return out, 'BottomCenter'


# ---------------------------------------------------------------- 6. Hoa anh đào
PETAL = 'M0,0C-17,-9 -21,-35 -5,-47L0,-41L5,-47C21,-35 17,-9 0,0z'


def flower(x, y, s, rot, alpha):
    petals = [G([P(PETAL, fill=PINK, fa=alpha)], rot=k * 72) for k in range(5)]
    return G(petals + [P(circle(0, 0, 6), fill=WARM, fa=0.9), P(circle(0, 0, 13), fill='FF6FA8', fa=0.35)],
             tx=x, ty=y, rot=rot, sx=s, sy=s)


def sakura(rng):
    out = [bg(), glow(420, 260, 700, PINK, 0.08)]
    out += stars(rng, 30, (0, 700, W, 2300), 1.1, 2.2, alphas=(0.18, 0.3), avoid=CLOCK)
    bark = '2A161C'
    branches = [('M-40,110C200,170 400,110 640,250C760,320 860,300 1120,380', 28), ('M300,150C350,250 380,330 430,450', 15),
                ('M560,210C600,120 680,80 760,60', 12), ('M720,300C760,400 820,470 900,520', 12), ('M150,140C160,230 120,300 90,360', 10),
                ('M430,450C470,500 520,520 580,520', 6), ('M900,520C940,560 960,600 960,660', 5)]
    out += [P(d, stroke=bark, sw=w) for d, w in branches]
    spots = [(250, 150, 1.2), (330, 110, 0.9), (420, 160, 1.1), (520, 175, 0.8), (610, 235, 1.2), (690, 280, 0.9), (800, 310, 1.1),
             (900, 330, 0.85), (1010, 355, 1.2), (365, 300, 1.0), (420, 430, 1.15), (520, 515, 0.8), (600, 140, 0.9), (720, 75, 1.0),
             (770, 400, 0.9), (880, 500, 1.1), (955, 640, 0.8), (130, 250, 1.0), (95, 360, 0.85), (200, 120, 0.7), (470, 110, 0.75)]
    for x, y, s in spots:
        out.append(flower(x, y, s, rng.uniform(0, 72), rng.uniform(0.7, 0.9)))
    falling = []
    placed = 0
    while placed < 34:
        x, y = rng.uniform(0, W), rng.uniform(450, 2350)
        if in_box(x, y, CLOCK, 40):
            continue
        s = rng.uniform(0.45, 0.85) * (1 - (y - 450) / 4200)
        falling.append(G([P(PETAL, fill=PINK, fa=rng.uniform(0.3, 0.6))], tx=x, ty=y, rot=rng.uniform(0, 360), sx=s, sy=s * rng.uniform(0.6, 1)))
        placed += 1
    return out + falling, 'TopCenter'


# ---------------------------------------------------------------- 7. Sứa phát sáng
def jellyfish(rng):
    out = [bg(), glow(540, 200, 900, BLUE, 0.07)]
    snow = stars(rng, 70, (0, 0, W, H), 1, 2.6, color=ICE, alphas=(0.15, 0.28, 0.42), avoid=CLOCK)
    out += snow
    for cx, cy, w, h, c1, c2, length in [(300, 400, 280, 180, MINT, BLUE, 520), (800, 260, 190, 125, PURPLE, BLUE, 380),
                                         (900, 690, 110, 72, MINT, PURPLE, 260), (130, 900, 80, 52, BLUE, MINT, 200)]:
        dome = (f'M{n(cx - w / 2)},{n(cy)}C{n(cx - w / 2)},{n(cy - h * 1.32)} {n(cx + w / 2)},{n(cy - h * 1.32)} {n(cx + w / 2)},{n(cy)}'
                f'Q{n(cx + w * 0.375)},{n(cy + h * 0.12)} {n(cx + w / 4)},{n(cy + h * 0.02)}Q{n(cx + w / 8)},{n(cy + h * 0.14)} {n(cx)},{n(cy + h * 0.04)}'
                f'Q{n(cx - w / 8)},{n(cy + h * 0.14)} {n(cx - w / 4)},{n(cy + h * 0.02)}Q{n(cx - w * 0.375)},{n(cy + h * 0.12)} {n(cx - w / 2)},{n(cy)}z')
        out.append(glow(cx, cy - h * 0.4, w * 1.3, c1, 0.18))
        tent = []
        for k in range(7):
            sx_ = cx - w * 0.36 + w * 0.72 * k / 6
            amp = rng.uniform(10, 22)
            phase = rng.uniform(0, math.pi * 2)
            ln = length * rng.uniform(0.7, 1.05)
            pts = [(sx_ + amp * math.sin(phase + j * 0.9) * (j / 6), cy + h * 0.06 + ln * j / 6) for j in range(7)]
            tent.append((pts, ln))
        for pts, ln in tent:
            out.append(P(smooth(pts, False), stroke=Lin(0, cy, 0, cy + ln, [(0, c1, 0.55), (1, c2, 0)]), sw=2.4))
        for dx in (-0.08, 0.08):
            pts = [(cx + w * dx + 14 * math.sin(j * 1.1 + dx * 9) * j / 5, cy + h * 0.05 + length * 0.55 * j / 5) for j in range(6)]
            out.append(P(smooth(pts, False), stroke=Lin(0, cy, 0, cy + length * 0.55, [(0, c1, 0.3), (1, c1, 0)]), sw=9))
        out.append(P(dome, fill=Rad(cx, cy - h * 0.7, w * 0.8, [(0, c1, 0.55), (0.6, c2, 0.18), (1, c2, 0.05)])))
        out.append(P(dome, stroke=c1, sa=0.55, sw=2.5))
        out.append(P(ellipse(cx, cy - h * 0.55, w * 0.18, h * 0.16), fill=WHITE, fa=0.18))
    return out, 'TopCenter'


# ---------------------------------------------------------------- 8. Thành phố đêm
def city(rng):
    out = [bg(), P(f'M0,1500h{W}v900h-{W}z', fill=Lin(0, 1500, 0, 2150, [(0, PURPLE, 0), (1, PURPLE, 0.14)]))]
    out += stars(rng, 45, (0, 0, W, 1300), 1.1, 2.4, alphas=(0.2, 0.4), avoid=CLOCK)

    def skyline(hmin, hmax, wmin, wmax, color, lit, warm_a):
        body, warm, cool, tops = '', '', '', []
        x = -10
        while x < W:
            bw, bh = rng.uniform(wmin, wmax), rng.uniform(hmin, hmax)
            top = H - bh
            body += f'M{n(x)},{H}V{n(top)}H{n(x + bw)}V{H}z'
            tops.append((x, bw, top))
            for wy in range(int(top + 22), H - 30, 30):
                for wx in range(int(x + 10), int(x + bw - 16), 22):
                    roll = rng.random()
                    if roll < lit:
                        cell = f'M{wx},{wy}h10v14h-10z'
                        if roll < lit * 0.2:
                            cool += cell
                        else:
                            warm += cell
            x += bw + rng.uniform(0, 10)
        res = [P(body, fill=color)]
        if warm:
            res.append(P(warm, fill='FFD9A0', fa=warm_a))
        if cool:
            res.append(P(cool, fill=BLUE, fa=warm_a * 0.8))
        return res, tops

    back, tops = skyline(280, 560, 60, 120, '0E1124', 0.05, 0.5)
    out += back
    tallest = sorted(tops, key=lambda t: t[2])[:3]
    for x, bw, top in tallest:
        mid = x + bw / 2
        out.append(P(f'M{n(mid)},{n(top)}V{n(top - 70)}', stroke='0E1124', sw=4))
        out.append(glow(mid, top - 72, 22, RED, 0.5))
        out.append(P(circle(mid, top - 72, 4.5), fill=RED, fa=0.9))
    front, _ = skyline(150, 360, 80, 160, '05060C', 0.09, 0.85)
    out += front
    return out, 'BottomCenter'


# ---------------------------------------------------------------- 9. Pháo hoa
def fireworks(rng):
    out = [bg(), glow(540, 2500, 900, PURPLE, 0.07)]
    out += stars(rng, 40, (0, 0, W, 1400), 1.1, 2.4, alphas=(0.2, 0.38), avoid=CLOCK)

    def burst(cx, cy, radius, rays, color, tip, gravity):
        out.append(glow(cx, cy, radius * 1.15, color, 0.16))
        inner, outer, tips = '', '', ''
        offset = rng.uniform(0, math.pi * 2)
        for k in range(rays):
            ang = offset + 2 * math.pi * k / rays + rng.uniform(-0.06, 0.06)
            length = radius * rng.uniform(0.82, 1.05)
            vx, vy = math.cos(ang) * length, math.sin(ang) * length

            def pt(s):
                return cx + vx * s, cy + vy * s + gravity * s * s

            def seg(s0, s1):
                x0, y0 = pt(s0)
                x1, y1 = pt(s1)
                h = (s1 - s0) / 2
                qx, qy = x0 + vx * h, y0 + (vy + 2 * gravity * s0) * h
                return f'M{n(x0)},{n(y0)}Q{n(qx)},{n(qy)} {n(x1)},{n(y1)}'

            inner += seg(0.18, 0.62)
            outer += seg(0.62, 0.97)
            tips += circle(*pt(1.0), 3.2)
        out.append(P(inner, stroke=color, sa=0.22, sw=2.4))
        out.append(P(outer, stroke=color, sa=0.75, sw=3))
        out.append(P(tips, fill=tip, fa=0.95))
        out.append(P(circle(cx, cy, 5), fill=tip, fa=0.6))

    burst(330, 430, 300, 36, PINK, WARM, 70)
    burst(790, 290, 230, 30, MINT, WHITE, 55)
    burst(890, 690, 140, 24, ORANGE, CREAM, 35)
    burst(140, 830, 105, 20, BLUE, ICE, 25)
    for x, y0, y1, color in [(520, 2360, 1760, ORANGE), (930, 2390, 1940, PINK), (170, 2380, 2080, MINT)]:
        out.append(P(f'M{x},{y0}Q{x + 12},{n((y0 + y1) / 2)} {x - 4},{y1}', stroke=Lin(0, y0, 0, y1, [(0, color, 0), (1, color, 0.8)]), sw=3))
        out.append(glow(x - 4, y1, 26, color, 0.5))
        out.append(P(circle(x - 4, y1, 3.5), fill=WARM, fa=0.95))
    return out, 'TopCenter'


# ---------------------------------------------------------------- 10. Đường đồng mức
def contours(rng):
    out = [bg()]
    for (cx, cy, count, step, c_in, c_out, rmax) in [(1010, 250, 13, 58, MINT, BLUE, 760), (60, 2190, 12, 58, PURPLE, BLUE, 700)]:
        phases = [rng.uniform(0, 6.28) for _ in range(3)]
        for i in range(count):
            r = 46 + i * step
            if r > rmax:
                break
            t = i / max(1, count - 1)
            pts = []
            for j in range(56):
                th = j / 56 * 2 * math.pi
                rr = r * (1 + 0.09 * math.sin(3 * th + phases[0] + i * 0.18) + 0.05 * math.sin(5 * th + phases[1] - i * 0.1)
                          + 0.03 * math.sin(7 * th + phases[2]))
                pts.append((cx + rr * math.cos(th), cy + rr * math.sin(th)))
            color = c_in if t < 0.5 else c_out
            index_line = i % 4 == 0
            out.append(P(smooth(pts, True), stroke=color, sa=(0.5 - 0.32 * t) * (1.25 if index_line else 1), sw=3.4 if index_line else 1.8))
        out.append(P(circle(cx, cy, 7), fill=c_in, fa=0.8))
        out.append(glow(cx, cy, 120, c_in, 0.2))
    return out, 'TopCenter'


# ---------------------------------------------------------------- 11. Sóng âm
def soundwave(rng):
    base = 2060
    out = [bg(), glow(540, base, 640, BLUE, 0.11)]
    bars, refl = '', ''
    count = 41
    for i in range(count):
        x = 60 + (W - 120) * i / (count - 1)
        env = math.exp(-((x - 540) / 320) ** 2)
        h = 28 + 300 * env * (0.45 + 0.55 * abs(math.sin(i * 0.73 + 0.4))) * rng.uniform(0.75, 1)
        bars += f'M{n(x)},{n(base - h)}V{base}'
        refl += f'M{n(x)},{base + 22}V{n(base + 22 + h * 0.4)}'
    grad = Lin(60, 0, W - 60, 0, [(0, MINT, 0.95), (0.5, BLUE, 0.95), (1, PURPLE, 0.95)])
    out.append(P(bars, stroke=grad, sw=13))
    out.append(P(refl, stroke=Lin(60, 0, W - 60, 0, [(0, MINT, 0.22), (0.5, BLUE, 0.22), (1, PURPLE, 0.22)]), sw=13))
    out.append(P(f'M40,{base + 11}H{W - 40}', stroke=GREY, sa=0.12, sw=2))
    return out, 'BottomCenter'


# ---------------------------------------------------------------- 12. Đèn trời
def lanterns(rng):
    out = [bg(), P(f'M0,1600h{W}v800h-{W}z', fill=Lin(0, 1600, 0, 2200, [(0, ORANGE, 0), (1, ORANGE, 0.09)]))]
    out += stars(rng, 40, (0, 0, W, 1200), 1.1, 2.4, alphas=(0.2, 0.38), avoid=CLOCK)
    spots = [(170, 2050, 86), (430, 2140, 70), (720, 2010, 92), (930, 2170, 64), (560, 1860, 54), (300, 1820, 46), (880, 1840, 50),
             (110, 1680, 38), (980, 1650, 34), (640, 1700, 30), (420, 1640, 26), (200, 1500, 22), (900, 1450, 20), (60, 1240, 16),
             (1020, 1180, 15), (150, 760, 14), (960, 660, 13), (820, 420, 11), (260, 360, 10), (560, 240, 9), (700, 560, 9), (380, 600, 8)]
    halo, inner, body, light, flame = '', '', '', '', ''
    for x, y, s in spots:
        halo += circle(x, y + s * 0.3, s * 2.4)
        inner += circle(x, y + s * 0.4, s * 1.2)
        body += (f'M{n(x - s * 0.5)},{n(y - s * 0.6)}Q{n(x)},{n(y - s * 0.78)} {n(x + s * 0.5)},{n(y - s * 0.6)}'
                 f'L{n(x + s * 0.38)},{n(y + s * 0.62)}Q{n(x)},{n(y + s * 0.7)} {n(x - s * 0.38)},{n(y + s * 0.62)}z')
        light += (f'M{n(x - s * 0.44)},{n(y)}L{n(x + s * 0.44)},{n(y)}L{n(x + s * 0.38)},{n(y + s * 0.62)}'
                  f'Q{n(x)},{n(y + s * 0.7)} {n(x - s * 0.38)},{n(y + s * 0.62)}z')
        flame += circle(x, y + s * 0.56, max(1.8, s * 0.08))
    out += [P(halo, fill=ORANGE, fa=0.07), P(inner, fill=ORANGE, fa=0.14), P(body, fill='E8894A', fa=0.75),
            P(light, fill='FFD49A', fa=0.55), P(flame, fill=CREAM, fa=0.95)]
    water = ''
    for x, y, s in spots[:4]:
        for k in range(4):
            yy = 2290 + k * 22
            ww = s * (0.9 - k * 0.15)
            water += f'M{n(x - ww / 2)},{yy}h{n(ww)}v5h{n(-ww)}z'
    out.append(P(water, fill=ORANGE, fa=0.22))
    out.append(P(f'M0,2270H{W}', stroke=ORANGE, sa=0.15, sw=2))
    return out, 'BottomCenter'


# (khoá file, tên hiển thị, hàm vẽ). Thứ tự quyết định seed (1000 + vị trí): đổi thứ tự là đổi hình.
WALLPAPERS = [
    ('meteor', 'Mưa sao băng', meteor),
    ('planet', 'Hành tinh vành đai', planet),
    ('orbits', 'Quỹ đạo', orbits),
    ('mountains', 'Núi trăng', mountains),
    ('fireflies', 'Đom đóm', fireflies),
    ('sakura', 'Hoa anh đào', sakura),
    ('jellyfish', 'Sứa phát sáng', jellyfish),
    ('city', 'Thành phố đêm', city),
    ('fireworks', 'Pháo hoa', fireworks),
    ('contours', 'Đường đồng mức', contours),
    ('soundwave', 'Sóng âm', soundwave),
    ('lanterns', 'Đèn trời', lanterns),
]


def offset(o):
    return f'{o:.2f}'.rstrip('0').rstrip('.') if o < 1 else '1'


def argb(color, alpha):
    return f'#{round(alpha * 255):02X}{color}'


def to_vector(elems, name, align):
    lines = [
        f'<?xml version="1.0" encoding="utf-8"?><!-- Ảnh nền AMOLED "{name}" (WallpaperValue): nền đen, chi tiết mờ, chừa vùng giữa cho '
        f'đồng hồ, giữ phần {SIDE[align]} khi cắt cho bố cục ngang. Viewport {W}×{H} = màn hình 9:20. {MARKER}, không sửa tay. -->',
        '<vector xmlns:android="http://schemas.android.com/apk/res/android"',
        '    xmlns:aapt="http://schemas.android.com/aapt"',
        f'    android:width="{W // 12}dp"',
        f'    android:height="{H // 12}dp"',
        f'    android:viewportWidth="{W}"',
        f'    android:viewportHeight="{H}">',
    ]

    def gradient(paint, pad):
        if isinstance(paint, Lin):
            head = [f'android:endX="{n(paint.x2)}"', f'android:endY="{n(paint.y2)}"', f'android:startX="{n(paint.x1)}"',
                    f'android:startY="{n(paint.y1)}"', 'android:type="linear"']
        else:
            head = [f'android:centerX="{n(paint.cx)}"', f'android:centerY="{n(paint.cy)}"',
                    f'android:gradientRadius="{n(paint.r)}"', 'android:type="radial"']
        out = [f'{pad}<gradient']
        out += [f'{pad}    {h}' for h in head[:-1]]
        out.append(f'{pad}    {head[-1]}>')
        for o, color, alpha in paint.stops:
            out += [f'{pad}    <item', f'{pad}        android:color="{argb(color, alpha)}"', f'{pad}        android:offset="{offset(o)}" />']
        out.append(f'{pad}</gradient>')
        return out

    def emit(e, depth):
        pad = '    ' * depth
        if e['k'] == 'group':
            attrs = []
            if e['px']:
                attrs.append(f'android:pivotX="{n(e["px"])}"')
            if e['py']:
                attrs.append(f'android:pivotY="{n(e["py"])}"')
            if e['rot']:
                attrs.append(f'android:rotation="{n(e["rot"])}"')
            if e['sx'] != 1.0:
                attrs.append(f'android:scaleX="{e["sx"]:.3f}"')
            if e['sy'] != 1.0:
                attrs.append(f'android:scaleY="{e["sy"]:.3f}"')
            if e['tx']:
                attrs.append(f'android:translateX="{n(e["tx"])}"')
            if e['ty']:
                attrs.append(f'android:translateY="{n(e["ty"])}"')
            if attrs:
                lines.append(f'{pad}<group')
                lines.extend(f'{pad}    {a_}' for a_ in attrs[:-1])
                lines.append(f'{pad}    {attrs[-1]}>')
            else:
                lines.append(f'{pad}<group>')
            for child in e['children']:
                emit(child, depth + 1)
            lines.append(f'{pad}</group>')
            return
        attrs = []
        gradients = []
        if e['fill'] is not None:
            if isinstance(e['fill'], str):
                attrs.append(f'android:fillColor="{argb(e["fill"], e["fa"])}"')
            else:
                gradients.append(('fillColor', e['fill']))
        attrs.append(f'android:pathData="{e["d"]}"')
        if e['stroke'] is not None:
            if isinstance(e['stroke'], str):
                attrs.append(f'android:strokeColor="{argb(e["stroke"], e["sa"])}"')
            else:
                gradients.append(('strokeColor', e['stroke']))
            attrs += [f'android:strokeLineCap="{e["cap"]}"', f'android:strokeLineJoin="{e["join"]}"', f'android:strokeWidth="{n(e["sw"])}"']
        lines.append(f'{pad}<path')
        lines.extend(f'{pad}    {a_}' for a_ in attrs[:-1])
        if not gradients:
            lines.append(f'{pad}    {attrs[-1]} />')
            return
        lines.append(f'{pad}    {attrs[-1]}>')
        for attr, paint in gradients:
            lines.append(f'{pad}    <aapt:attr name="android:{attr}">')
            lines.extend(gradient(paint, pad + '        '))
            lines.append(f'{pad}    </aapt:attr>')
        lines.append(f'{pad}</path>')

    for e in elems:
        emit(e, 1)
    lines.append('</vector>')
    return '\n'.join(lines) + '\n'


def to_svg(elems):
    defs, counter = [], [0]

    def paint(p):
        if isinstance(p, str):
            return '#' + p
        counter[0] += 1
        gid = f'g{counter[0]}'
        stops = ''.join(f'<stop offset="{offset(o)}" stop-color="#{c}" stop-opacity="{a(al)}"/>' for o, c, al in p.stops)
        if isinstance(p, Lin):
            defs.append(f'<linearGradient id="{gid}" gradientUnits="userSpaceOnUse" x1="{n(p.x1)}" y1="{n(p.y1)}" x2="{n(p.x2)}" y2="{n(p.y2)}">{stops}</linearGradient>')
        else:
            defs.append(f'<radialGradient id="{gid}" gradientUnits="userSpaceOnUse" cx="{n(p.cx)}" cy="{n(p.cy)}" r="{n(p.r)}">{stops}</radialGradient>')
        return f'url(#{gid})'

    def emit(e):
        if e['k'] == 'group':
            t = (f'translate({n(e["tx"] + e["px"])},{n(e["ty"] + e["py"])}) rotate({n(e["rot"])}) '
                 f'scale({e["sx"]:.3f},{e["sy"]:.3f}) translate({n(-e["px"])},{n(-e["py"])})')
            return f'<g transform="{t}">' + ''.join(emit(c) for c in e['children']) + '</g>'
        attrs = [f'd="{e["d"]}"']
        if e['fill'] is None:
            attrs.append('fill="none"')
        else:
            attrs.append(f'fill="{paint(e["fill"])}"')
            if isinstance(e['fill'], str) and e['fa'] < 1:
                attrs.append(f'fill-opacity="{a(e["fa"])}"')
        if e['stroke'] is not None:
            attrs += [f'stroke="{paint(e["stroke"])}"', f'stroke-width="{n(e["sw"])}"', f'stroke-linecap="{e["cap"]}"', f'stroke-linejoin="{e["join"]}"']
            if isinstance(e['stroke'], str) and e['sa'] < 1:
                attrs.append(f'stroke-opacity="{a(e["sa"])}"')
        return '<path ' + ' '.join(attrs) + '/>'

    content = ''.join(emit(e) for e in elems)
    return (f'<svg xmlns="http://www.w3.org/2000/svg" width="{W}" height="{H}" viewBox="0 0 {W} {H}">'
            f'<defs>{"".join(defs)}</defs>{content}</svg>\n')


def main():
    root = Path(__file__).resolve().parents[2]
    parser = argparse.ArgumentParser()
    parser.add_argument('--out', default=str(root / 'app/src/main/res/drawable'))
    parser.add_argument('--preview-svg')
    args = parser.parse_args()
    out = Path(args.out)
    preview = Path(args.preview_svg) if args.preview_svg else None
    if preview:
        preview.mkdir(parents=True, exist_ok=True)

    wanted = {f'img_wallpaper_{key}.xml' for key, _, _ in WALLPAPERS}
    for i, (key, name, draw) in enumerate(WALLPAPERS):
        elems, align = draw(random.Random(1000 + i))
        (out / f'img_wallpaper_{key}.xml').write_text(to_vector(elems, name, align), encoding='utf-8', newline='\n')
        if preview:
            (preview / f'wp_{key}.svg').write_text(to_svg(elems), encoding='utf-8', newline='\n')
        print(f'{key:<10} Alignment.{align}')

    for stale in out.glob('img_wallpaper_*.xml'):
        if stale.name not in wanted and MARKER in stale.read_text(encoding='utf-8'):
            stale.unlink()
            print('xoá', stale.name)


if __name__ == '__main__':
    main()
