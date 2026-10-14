"""Sinh hình minh hoạ của sheet / hộp thoại: điện thoại viền sáng như icon app, khung 120 x 96.

Chạy từ gốc repo: python tools/brand/generate_illustrations.py
Sinh `img_spot_phone.xml` (viền mint → xanh → tím, đồng hồ mint) và `img_spot_phone_offline.xml` (xám, dùng khi mất mạng).
Huy hiệu (đặt lại, quyền, mất mạng...) vẽ trong Compose (`AppIllustration`) chồng lên góc dưới phải điện thoại, nên một hình dùng
cho mọi ngữ cảnh. Hình học lấy từ brand_geometry.py (cùng icon app), dời vào giữa khung 120 x 96; đừng sửa tay file XML.
"""
from brand_geometry import (
    BORDER_STROKES, CAMERA_COLOR, CAMERA_RADIUS, CAMERA_X, CAMERA_Y, CLOCK_COLOR, DOT_COLOR, DOT_RADIUS, DOTS_X, DOTS_Y,
    EDGE_COLORS, PHONE_H, PHONE_RADIUS, PHONE_W, PHONE_X, PHONE_Y, RES, SCREEN_COLOR, digit_origins, segment_boxes,
)

WIDTH, HEIGHT = 120, 96
# Dời điện thoại của khung icon 108 vào giữa khung 120 x 96 (tâm điện thoại 54, 54 → 60, 44).
SHIFT_X, SHIFT_Y = 6, -10
AURA_RADIUS, AURA_SCALE_Y = 50, 0.84
OFFLINE_EDGE, OFFLINE_CLOCK = ("6E6E6E", "3A3A3A"), "6E6E6E"
PATH_DATA_LIMIT = 800  # lint VectorPath
MARKER = "<!-- Sinh bằng tools/brand/generate_illustrations.py, đừng sửa tay. -->"


def fmt(v):
    s = f"{v:.2f}".rstrip("0").rstrip(".")
    return s if s else "0"


def rounded_rect(x, y, w, h, r):
    return (f"M{fmt(x + r)},{fmt(y)}H{fmt(x + w - r)}A{fmt(r)},{fmt(r)} 0,0 1,{fmt(x + w)},{fmt(y + r)}"
            f"V{fmt(y + h - r)}A{fmt(r)},{fmt(r)} 0,0 1,{fmt(x + w - r)},{fmt(y + h)}"
            f"H{fmt(x + r)}A{fmt(r)},{fmt(r)} 0,0 1,{fmt(x)},{fmt(y + h - r)}"
            f"V{fmt(y + r)}A{fmt(r)},{fmt(r)} 0,0 1,{fmt(x + r)},{fmt(y)}Z")


def pill(x, y, w, h):
    r = min(w, h) / 2
    if w >= h:
        return (f"M{fmt(x + r)},{fmt(y)}H{fmt(x + w - r)}A{fmt(r)},{fmt(r)} 0,0 1,{fmt(x + w - r)},{fmt(y + h)}"
                f"H{fmt(x + r)}A{fmt(r)},{fmt(r)} 0,0 1,{fmt(x + r)},{fmt(y)}Z")
    return (f"M{fmt(x)},{fmt(y + r)}A{fmt(r)},{fmt(r)} 0,0 1,{fmt(x + w)},{fmt(y + r)}V{fmt(y + h - r)}"
            f"A{fmt(r)},{fmt(r)} 0,0 1,{fmt(x)},{fmt(y + h - r)}Z")


def circle(cx, cy, r):
    return (f"M{fmt(cx - r)},{fmt(cy)}A{fmt(r)},{fmt(r)} 0,1 0,{fmt(cx + r)},{fmt(cy)}"
            f"A{fmt(r)},{fmt(r)} 0,1 0,{fmt(cx - r)},{fmt(cy)}Z")


phone_path = rounded_rect(PHONE_X, PHONE_Y, PHONE_W, PHONE_H, PHONE_RADIUS)
camera_path = circle(CAMERA_X, CAMERA_Y, CAMERA_RADIUS)
digit_paths = ["".join(pill(*box) for box in segment_boxes(x0, y0, char)) for char, x0, y0 in digit_origins()]
dots_path = "".join(circle(cx, DOTS_Y, DOT_RADIUS) for cx in DOTS_X)
aura_path = circle(PHONE_X + PHONE_W / 2, (PHONE_Y + PHONE_H / 2) / AURA_SCALE_Y, AURA_RADIUS)
assert all(len(p) < PATH_DATA_LIMIT for p in digit_paths + [dots_path, phone_path, aura_path])


def linear(attr, colors):
    items = "".join(
        f'          <item android:offset="{fmt(i / (len(colors) - 1))}" android:color="#FF{color}" />\n'
        for i, color in enumerate(colors)
    )
    return f"""      <aapt:attr name="{attr}">
        <gradient
            android:type="linear"
            android:startX="{fmt(PHONE_X)}"
            android:startY="{fmt(PHONE_Y)}"
            android:endX="{fmt(PHONE_X + PHONE_W)}"
            android:endY="{fmt(PHONE_Y + PHONE_H)}">
{items}        </gradient>
      </aapt:attr>
"""


def aura(colors):
    first, last = colors[0], colors[-1]
    return f"""    <group android:scaleY="{fmt(AURA_SCALE_Y)}">
      <path android:pathData="{aura_path}">
        <aapt:attr name="android:fillColor">
          <gradient
              android:type="radial"
              android:centerX="{fmt(PHONE_X + PHONE_W / 2)}"
              android:centerY="{fmt((PHONE_Y + PHONE_H / 2) / AURA_SCALE_Y)}"
              android:gradientRadius="{fmt(AURA_RADIUS)}">
            <item android:offset="0" android:color="#38{first}" />
            <item android:offset="0.6" android:color="#14{last}" />
            <item android:offset="1" android:color="#00{last}" />
          </gradient>
        </aapt:attr>
      </path>
    </group>
"""


def illustration(edge_colors, clock_color):
    strokes = "".join(
        f"""    <path
        android:pathData="{phone_path}"
        android:strokeWidth="{fmt(width)}"
        android:strokeAlpha="{fmt(alpha)}">
{linear("android:strokeColor", edge_colors)}    </path>
""" for width, alpha in sorted(BORDER_STROKES, key=lambda s: -s[0]))
    digits = "".join(f'    <path android:fillColor="#FF{clock_color}" android:pathData="{p}" />\n' for p in digit_paths)
    return f"""<?xml version="1.0" encoding="utf-8"?>
{MARKER}
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:aapt="http://schemas.android.com/aapt"
    android:width="120dp"
    android:height="96dp"
    android:viewportWidth="{WIDTH}"
    android:viewportHeight="{HEIGHT}">
  <group android:translateX="{SHIFT_X}" android:translateY="{SHIFT_Y}">
{aura(edge_colors)}{strokes}    <path android:fillColor="#FF{SCREEN_COLOR}" android:pathData="{phone_path}" />
    <path android:fillColor="#FF{CAMERA_COLOR}" android:pathData="{camera_path}" />
{digits}    <path android:fillColor="#FF{DOT_COLOR}" android:pathData="{dots_path}" />
  </group>
</vector>
"""


for name, edge, clock in (("img_spot_phone", EDGE_COLORS, CLOCK_COLOR), ("img_spot_phone_offline", OFFLINE_EDGE, OFFLINE_CLOCK)):
    (RES / "drawable" / f"{name}.xml").write_text(illustration(edge, clock), encoding="utf-8")
    print(name)
