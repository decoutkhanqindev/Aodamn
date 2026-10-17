#!/usr/bin/env python3
"""Bộ trích danh mục chủ đề AOD của Muviz Edge (com.sparkine.muvizedge 2.1.5.0) từ kết quả của pull_decompile.py.

Chạy ở gốc repo:  python -I -X utf8 tools/reference/extract_muviz.py [THƯ_MỤC_LÀM_VIỆC]
(mặc định ~/.local/share/rea/work/com.sparkine.muvizedge). Ra catalog.json + catalog.md (bảng Markdown) trong thư mục đó;
bảng dán vào plans/muviz-edge-themes.md.

Mô hình của app này (đổi theo bản, R8 đổi tên mỗi lần build, kiểm lại khi app cập nhật):
- Mỗi chủ đề là fragment/aodscreen/<Tháng><Năm>Screen.java: W() trả tên, V() trả mặc định (cb.i: h = số, i = màu db.c,
  j = chữ), Z() trả danh sách thuộc tính theo thứ tự (cb.h: e(mã, cb.g)). cb.g(loại) / cb.g(min, max) / cb.g(int[] lựa chọn,
  loại); helper b2.h / b2.i / b2.s / b2.u của com.google.android.gms.internal.measurement.b2 (R8 gộp hàm vào đó).
- ib/a.java: c(mã) → class chủ đề, f14544b = mã thuộc tính / lựa chọn → R.string.prop_*, f14545c = chủ đề Premium.
App khác: chép file này thành extract_<app>.py rồi sửa phần đọc theo mô hình của app đó (README cùng thư mục).
"""
import json
import re
import sys
from pathlib import Path

WORK = Path(sys.argv[1]) if len(sys.argv) > 1 else Path.home() / ".local/share/rea/work/com.sparkine.muvizedge"
SRC = WORK / "src" / "sources"
SCREENS = SRC / "com" / "sparkine" / "muvizedge" / "fragment" / "aodscreen"

TYPE_NAMES = {1: "range", 2: "choice", 3: "multi", 4: "color", 5: "advanced-group", 6: "symbol", 7: "text", 8: "timezone"}
NEW_CONTROL = {"multi": "chọn nhiều", "symbol": "chọn ký hiệu", "text": "ô nhập chữ", "timezone": "chọn múi giờ"}
PROPRIETARY_FONTS = ("ios_", "ndot", "google_sans", "gsans_flex")
COMMON_VIEWS = ("TimeProgressBar", "TimeProgressBarRounded", "NoPadTextView")

# Chủ đề đã clone: mã chủ đề Muviz → chủ đề của mình. Khớp bằng ảnh tab tuỳ chọn trong reference/theme_N/, không đoán theo tên.
CLONED = {44: "**Sỏi** (`PEBBLE`)", 37: "**Số áo** (`JERSEY`)", 32: "**Viên nang** (`CAPSULE`)", 38: "**Thẻ** (`CARD`)"}


def read(p):
    return p.read_text(encoding="utf-8", errors="replace")


def string_values(dump):
    values, name = {}, None
    for line in dump.splitlines():
        m = re.match(r"\s+resource 0x[0-9a-f]+ string/(\S+)$", line)
        if m:
            name = m.group(1)
            continue
        m = re.match(r'\s+\(\) "(.*)"$', line)
        if m and name:
            values[name] = m.group(1)
            name = None
    return values


def prop_map(ib_src):
    body = ib_src[ib_src.index("f14544b = map;"):ib_src.index("public static cb.i a(")]
    names = re.findall(r"R\.string\.(\w+)", body)
    chain = list(reversed(names[:-1])) + [names[-1]]
    tail = body[body.index("prop_hour_color, map, "):]
    first = re.match(r"prop_hour_color, map, (-?\d+), (-?\d+)\)", tail)
    keys = [int(first.group(1)), int(first.group(2))] + [int(x) for x in re.findall(r", (-?\d+)\)", tail[first.end():])]
    keys = keys[:len(chain)]
    if len(keys) != len(chain):
        sys.exit("Bảng mã → nhãn trong ib/a.java đã đổi dạng (%d mã, %d nhãn): sửa prop_map" % (len(keys), len(chain)))
    return dict(zip(keys, chain))


def theme_ids(ib_src):
    ids = {m.group(2): int(m.group(1)) for m in
           re.finditer(r"case (?:\w+\.\w+ /\* )?(\d+)(?: \*/)?:\s*return new (\w+)\(iVar\);", ib_src)}
    ids["DefaultScreen"] = 0
    premium = [int(x) for x in re.search(r"f14545c = new int\[\]\{([^}]*)\}", ib_src).group(1).split(",")]
    return ids, premium


def method_body(src, signature):
    start = src.find(signature)
    if start < 0:
        return ""
    open_i = src.index("{", start)
    depth = 0
    for i in range(open_i, len(src)):
        if src[i] == "{":
            depth += 1
        elif src[i] == "}":
            depth -= 1
            if depth == 0:
                return src[open_i + 1:i]
    return ""


def split_top(s, sep):
    out, depth, cur, in_str = [], 0, "", False
    for ch in s:
        if ch == '"':
            in_str = not in_str
        if not in_str:
            if ch in "([{":
                depth += 1
            elif ch in ")]}":
                depth -= 1
            elif ch == sep and depth == 0:
                out.append(" ".join(cur.split()))
                cur = ""
                continue
        cur += ch
    if cur.strip():
        out.append(" ".join(cur.split()))
    return out


def inner(expr, prefix):
    return expr[len(prefix):-1]


def int_list(expr):
    return [int(x) for x in split_top(expr[expr.index("{") + 1:expr.rindex("}")], ",")]


def color_hex(expr):
    m = re.search(r'"(#[0-9A-Fa-f]{6,8})"', expr)
    if m:
        return m.group(1).upper()
    v = int(expr.strip().rstrip("L")) & 0xFFFFFFFF
    return "#%06X" % (v & 0xFFFFFF) if (v >> 24) == 0xFF else "#%08X" % v


def color_value(expr, cvars):
    if expr in cvars:
        return cvars[expr]
    args = split_top(inner(expr, "new c("), ",")
    if args and args[0].startswith("new int[]"):
        return {"gradient": [color_hex(x) for x in split_top(args[0][args[0].index("{") + 1:args[0].rindex("}")], ",")]}
    return {"color": color_hex(args[0])} if args else {"expr": expr}


def g_spec(expr, gvars):
    if expr in gvars:
        return gvars[expr]
    args = split_top(inner(expr, "new g("), ",")
    if not args:
        return {"type": None}
    if args[0].startswith("new int[]"):
        return {"type": int(args[1]), "choices": int_list(args[0])}
    if len(args) == 2:
        return {"type": 1, "min": int(args[0]), "max": int(args[1])}
    return {"type": int(args[0])}


def statements(body):
    lazy = re.match(r"\s*if \(this\.(\w+) == null\) \{(.*)\}\s*return this\.\1;?\s*$", body, re.S)
    if lazy:
        body = lazy.group(2).replace("this.%s" % lazy.group(1), "hVar")
    return [s for s in split_top(body, ";") if s and not s.startswith("return") and not re.match(r"^(\w+) = \1$", s)]


def parse_options(body):
    order, specs, gvars, unparsed = [], {}, {}, []

    def put(key, spec):
        if key not in specs:
            order.append(key)
        specs[key] = dict(spec)

    def eval_g(expr):
        if expr.startswith("b2.h("):
            _, k, inner_expr, lo, hi = split_top(inner(expr, "b2.h("), ",")
            put(int(k), eval_g(inner_expr))
            return {"type": 1, "min": int(lo), "max": int(hi)}
        return g_spec(expr, gvars)

    for st in statements(body):
        if st.startswith("h hVar = new h("):
            continue
        m = re.match(r"(?:g )?(\w+) = new g\((.*)\)$", st)
        if m:
            gvars[m.group(1)] = g_spec("new g(%s)" % m.group(2), gvars)
            continue
        m = re.match(r"(\w+)\.f17\d\d([a-f]) = (.*)$", st)
        if m and m.group(1) in gvars:
            field, value = m.group(2), m.group(3)
            if field == "a":
                gvars[m.group(1)]["type"] = int(value)
            elif field == "f":
                gvars[m.group(1)]["symbols"] = re.findall(r'"([^"]*)"', value)
            elif field == "e":
                gvars[m.group(1)]["choices"] = int_list(value)
            continue
        m = re.match(r"[\w.]+\.e\((-?\d+), (.*)\)$", st)
        if m:
            put(int(m.group(1)), eval_g(m.group(2)))
            continue
        m = re.match(r"b2\.u\((\d+), [\w.]+, (-?\d+)\)$", st)
        if m:
            put(int(m.group(2)), {"type": int(m.group(1))})
            continue
        m = re.match(r"b2\.s\((-?\d+), (-?\d+), [\w.]+, (-?\d+)\)$", st)
        if m:
            put(int(m.group(3)), {"type": 1, "min": int(m.group(1)), "max": int(m.group(2))})
            continue
        unparsed.append(st)
    return order, specs, unparsed


def parse_defaults(body):
    defaults, cvars, unparsed = {}, {}, []
    for st in statements(body):
        m = re.match(r"i (\w+) = b2\.i\((-?\d+), (-?\d+), (-?\d+), (-?\d+)\)$", st)
        if m:
            defaults[int(m.group(2))] = int(m.group(3))
            defaults[int(m.group(4))] = int(m.group(5))
            continue
        if re.match(r"i \w+ = new i\(\)$", st):
            continue
        m = re.match(r"(?:c )?(\w+) = (new c\(.*\))$", st)
        if m:
            cvars[m.group(1)] = color_value(m.group(2), cvars)
            continue
        m = re.match(r"\w+\.h\((-?\d+), (.*)\)$", st)
        if m:
            value = m.group(2)
            defaults[int(m.group(1))] = color_hex(value) if value.startswith("Color.") else int(value.rstrip("L"))
            continue
        m = re.match(r"\w+\.i\((-?\d+), (.*)\)$", st)
        if m:
            defaults[int(m.group(1))] = color_value(m.group(2), cvars)
            continue
        m = re.match(r"\w+\.j\((.*), (-?\d+)\)$", st)
        if m:
            text = re.fullmatch(r'"(.*)"', m.group(1))
            defaults[int(m.group(2))] = {"text": text.group(1) if text else m.group(1)}
            continue
        unparsed.append(st)
    return defaults, unparsed


def drawn_with(src):
    views = sorted(set(re.findall(r"com\.sparkine\.muvizedge\.view\.(?:[a-z]\w*\.)*([A-Z]\w*)", src)) - set(COMMON_VIEWS))
    fonts = sorted(set(re.findall(r"R\.font\.(\w+)", src)))
    return views, fonts


def main():
    ib_src = read(SRC / "ib" / "a.java")
    props = prop_map(ib_src)
    labels = string_values(read(WORK / "res-base.txt"))
    ids, premium = theme_ids(ib_src)

    def label(key):
        return labels.get(props.get(key, ""), props.get(key, str(key)))

    themes = []
    for f in sorted(SCREENS.glob("*Screen.java")):
        src = read(f)
        cls = f.stem
        name = re.search(r'public (?:final )?String W\(\) \{\s*return "([^"]*)";', src)
        order, specs, unparsed_z = parse_options(method_body(src, "public final h Z()"))
        defaults, unparsed_v = parse_defaults(method_body(src, "public final i V()"))
        views, fonts = drawn_with(src)
        options, advanced = [], False
        for key in order:
            spec = specs[key]
            kind = TYPE_NAMES.get(spec.get("type"), spec.get("type"))
            advanced = advanced or key == 10
            entry = {"key": key, "prop": props.get(key), "label": label(key), "type": kind, "advanced": advanced and key != 10}
            for k in ("min", "max", "symbols"):
                if k in spec:
                    entry[k] = spec[k]
            if "choices" in spec:
                entry["choices"] = [label(c) for c in spec["choices"]]
            if key in defaults:
                d = defaults[key]
                entry["default"] = label(d) if isinstance(d, int) and kind in ("choice", "multi") else d
            options.append(entry)
        themes.append({"id": ids.get(cls), "class": cls, "name": name.group(1) if name else None,
                       "premium": ids.get(cls) in premium, "views": views, "fonts": fonts, "options": options,
                       "unparsed": unparsed_z + unparsed_v})
    themes.sort(key=lambda t: (t["id"] is None, t["id"]))
    (WORK / "catalog.json").write_text(json.dumps(themes, ensure_ascii=False, indent=1), encoding="utf-8")

    rows = ["| # | Class | Tên | Tuỳ chọn chính | Advanced | Vẽ bằng | Cần thêm | Bên mình |", "|---|---|---|---|---|---|---|---|"]
    for t in themes:
        if t["id"] == 0:
            continue
        main_opts = [o["label"] for o in t["options"] if not o["advanced"] and o["type"] != "advanced-group"]
        adv = [o["label"].replace(" Color", "") for o in t["options"] if o["advanced"]]
        needs = sorted({NEW_CONTROL[o["type"]] for o in t["options"] if o["type"] in NEW_CONTROL})
        if any((o["prop"] or "").startswith("prop_viz") for o in t["options"]):
            needs.append("quyền RECORD_AUDIO")
        if any(f.startswith(PROPRIETARY_FONTS) for f in t["fonts"]):
            needs.append("font độc quyền")
        drawn = ", ".join(t["views"]) or ("chữ: " + ", ".join(t["fonts"]) if t["fonts"] else "chữ (font layout)")
        rows.append("| %d | %s | %s%s | %s | %s | %s | %s | %s |" % (
            t["id"], t["class"].replace("Screen", ""), t["name"], " ⭐" if t["premium"] else "", ", ".join(main_opts) or "–",
            ", ".join(adv) or "–", drawn, "; ".join(needs) or "–", CLONED.get(t["id"], "chưa có")))
    (WORK / "catalog.md").write_text("\n".join(rows) + "\n", encoding="utf-8")
    print("\n".join(rows))
    left = [(t["class"], t["unparsed"]) for t in themes if t["unparsed"]]
    for cls, items in left:
        print("Chưa đọc được ở %s: %s" % (cls, items), file=sys.stderr)
    print("Ra: %s, %s" % (WORK / "catalog.json", WORK / "catalog.md"), file=sys.stderr)


main()
