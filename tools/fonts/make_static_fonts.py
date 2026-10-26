#!/usr/bin/env python3
"""Tách file font tĩnh từng độ đậm từ font variable (Google Fonts, OFL) ở tools/fonts/variable vào app/src/main/res/font; họ font Google Fonts
chỉ phát hành bản tĩnh (Anton) hoặc có tên dành riêng trong OFL (Zalando, Krona One: không được sửa file nên lấy bản tĩnh gốc của nhà làm font) thì chép file
ở tools/fonts/static.

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
    1000: "extrablack",
}

# họ font -> (độ đậm cần dùng, trục đặt cố định ngoài wght[, họ font của file nguồn khi hai họ cắt từ cùng một font variable])
FONTS = {
    "plus_jakarta_sans": ([200, 300, 400, 500, 600, 700, 800], {}),
    "fraunces": ([100, 300, 400, 600, 700], {"SOFT": 100}),
    "jetbrains_mono": ([100, 300, 400, 600, 700], {}),
    "dancing_script": ([400, 600, 700], {}),
    "comfortaa": ([300, 400, 600, 700], {}),
    "big_shoulders_display": ([100, 300, 400, 600, 700, 900], {}),
    "josefin_sans": ([100, 300, 400, 600, 700], {}),
    # Bản Google Fonts nặng 5 MB vì nhiều hệ chữ: tools/fonts/variable giữ bản đã cắt còn Latin + tiếng Việt (fontTools subset, giữ mọi trục).
    "google_sans": ([400, 500, 700], {"GRAD": 0, "opsz": 18}),
    # TODO(coum-face): họ font mới của chủ đề
    # TODO(big-overlap): họ font mới của chủ đề
    # Chữ số của Chồng số: font mẫu đậm hơn cả wght 1000 nên lấy 1000, wdth 108 cho số 0 rộng như mẫu; góc vuông / bo tròn (ROND 0 / 100) là hai họ
    # cắt từ cùng một file nguồn (bản Google Fonts đã cắt còn Latin + tiếng Việt, ghim opsz 18, slnt 0).
    "google_sans_flex": ([1000], {"wdth": 108, "ROND": 0}),
    "google_sans_flex_rounded": ([1000], {"wdth": 108, "ROND": 100}, "google_sans_flex"),
}

# họ font chỉ có bản tĩnh -> độ đậm có sẵn (file <họ font>_<độ đậm>.ttf ở tools/fonts/static)
STATIC_FONTS = {
    "anton": [400],
    "zalando_sans_expanded": [600],
    "krona_one": [400],
}


def main(families):
    OUT.mkdir(parents=True, exist_ok=True)
    for family, (weights, pinned, *source_family) in FONTS.items():
        if families and family not in families:
            continue
        source = SOURCE / f"{source_family[0] if source_family else family}_variable.ttf"
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
