#!/usr/bin/env python3
"""Tách file font tĩnh từng độ đậm từ font variable (Google Fonts, OFL) ở tools/fonts/variable vào app/src/main/res/font; họ font Google Fonts
chỉ phát hành bản tĩnh (Anton) thì chép file ở tools/fonts/static.

Chạy ở gốc repo:  python -I tools/fonts/make_static_fonts.py [họ font ...]   (cần fonttools: pip install fonttools, chỉ dùng cho script này)
Thêm font mới thì chỉ sinh họ đó (vd `... make_static_fonts.py anton`): sinh lại họ cũ có thể đổi byte file đã có.

Tại sao không để app dùng thẳng font variable: Compose áp trục wght bằng Paint.setFontVariationSettings rồi lấy lại typeface của Paint,
ROM Xiaomi (MIUI / HyperOS) can thiệp Paint nên font của app bị thay bằng font hệ thống (máy ảo vẫn đúng). File tĩnh chỉ cần Font(resId,
weight) nên không phụ thuộc ROM. Độ đậm ngoài dải trục của font không sinh (Dancing Script nhỏ nhất 400, Comfortaa 300, Plus Jakarta 200):
Compose tự lấy độ đậm gần nhất. Tên file: <họ font>_<độ đậm>.ttf; khai báo ở presentation/theme/Typography.kt, đổi danh sách ở đây thì
đổi ở đó.
"""
import sys
from pathlib import Path

try:
    from fontTools.ttLib import TTFont
    from fontTools.varLib import instancer
except ImportError:
    sys.exit("Thiếu fonttools: pip install fonttools")

ROOT = Path(__file__).resolve().parents[2]
SOURCE = ROOT / "tools/fonts/variable"
STATIC_SOURCE = ROOT / "tools/fonts/static"
OUT = ROOT / "app/src/main/res/font"

NAMES = {
    100: "thin", 200: "extralight", 300: "light", 400: "regular", 500: "medium", 600: "semibold", 700: "bold", 800: "extrabold", 900: "black",
}

# họ font -> (độ đậm cần dùng, trục đặt cố định ngoài wght)
FONTS = {
    "plus_jakarta_sans": ([200, 300, 400, 500, 600, 700, 800], {}),
    "fraunces": ([100, 300, 400, 600, 700], {"SOFT": 100}),
    "jetbrains_mono": ([100, 300, 400, 600, 700], {}),
    "dancing_script": ([400, 600, 700], {}),
    "comfortaa": ([300, 400, 600, 700], {}),
    "big_shoulders_display": ([100, 300, 400, 600, 700, 900], {}),
    "josefin_sans": ([100, 300, 400, 600, 700], {}),
}

# họ font chỉ có bản tĩnh -> độ đậm có sẵn (file <họ font>_<độ đậm>.ttf ở tools/fonts/static)
STATIC_FONTS = {
    "anton": [400],
}


def main(families):
    OUT.mkdir(parents=True, exist_ok=True)
    for family, (weights, pinned) in FONTS.items():
        if families and family not in families:
            continue
        source = SOURCE / f"{family}_variable.ttf"
        for weight in weights:
            font = TTFont(source)
            axes = {axis.axisTag: axis for axis in font["fvar"].axes}
            location = {tag: axis.defaultValue for tag, axis in axes.items()}
            location.update(pinned)
            location["wght"] = min(max(weight, axes["wght"].minValue), axes["wght"].maxValue)
            static = instancer.instantiateVariableFont(font, location, inplace=False)
            target = OUT / f"{family}_{NAMES[weight]}.ttf"
            static.save(target)
            print(f"{target.name}  {target.stat().st_size // 1024} KB")
    for family, weights in STATIC_FONTS.items():
        if families and family not in families:
            continue
        for weight in weights:
            target = OUT / f"{family}_{NAMES[weight]}.ttf"
            target.write_bytes((STATIC_SOURCE / target.name).read_bytes())
            print(f"{target.name}  {target.stat().st_size // 1024} KB")


if __name__ == "__main__":
    main(set(sys.argv[1:]))
