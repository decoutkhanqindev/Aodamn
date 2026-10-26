#!/usr/bin/env bash
# Kiểm một chủ đề AOD mới trước khi báo xong. Chạy ở gốc repo (hoặc worktree):
#   bash .claude/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói chủ đề> "<Tên tiếng Việt>" [--skip-gradle]
# vd: bash .claude/skills/aodamn-aod-theme-ref/scripts/check_theme.sh tick "Nhịp giây"
set -u
PKG="${1:?thiếu gói chủ đề, vd tick}"
NAME="${2:?thiếu tên tiếng Việt, vd \"Nhịp giây\"}"
SKIP_GRADLE="${3:-}"
ROOT="$(git rev-parse --show-toplevel)" || exit 1
cd "$ROOT"
J=app/src/main/java/com/decoutkhanqindev/aodamn/presentation
FAIL=0
ok() { echo "[OK]   $1"; }
bad() { echo "[FAIL] $1"; FAIL=1; }

[ -d "$J/components/aod/theme/$PKG" ] && [ -d "$J/model/aod/theme/$PKG" ] \
  && ok "có $J/{components,model}/aod/theme/$PKG" || bad "thiếu thư mục components/ hoặc model/aod/theme/$PKG"

[ -f local.properties ] && ok "có local.properties" || bad "thiếu local.properties (chép từ checkout chính)"

if [ "$SKIP_GRADLE" != "--skip-gradle" ]; then
  mkdir -p build
  if ./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:lintDebug --console=plain -q > build/check_theme_gradle.log 2>&1; then
    ok "compileDebugKotlin + testDebugUnitTest + lintDebug"
  else
    bad "gradle lỗi, xem build/check_theme_gradle.log"; tail -20 build/check_theme_gradle.log
  fi
fi

RULES="$(bash tools/check-rules.sh 2>&1 | tail -1)"
case "$RULES" in *"0 vi phạm"*) ok "check-rules: $RULES" ;; *) bad "check-rules: $RULES" ;; esac

BASE="$(git merge-base HEAD main 2>/dev/null || git rev-parse HEAD)"
OLD=()
for d in "$J"/components/aod/theme/*/ "$J"/model/aod/theme/*/; do
  name="$(basename "$d")"
  [ "$name" = "$PKG" ] || [ "$name" = "component" ] || OLD+=("$d")
done
CHANGED="$(git diff --name-only "$BASE" -- "${OLD[@]}"; git ls-files --others --exclude-standard -- "${OLD[@]}")"
[ -z "$CHANGED" ] && ok "thư mục chủ đề cũ không đổi so với $BASE" || { bad "chủ đề cũ bị đổi:"; echo "$CHANGED"; }

python -I -X utf8 - "$PKG" "$NAME" <<'EOF' || FAIL=1
import re, sys
from pathlib import Path
pkg, name = sys.argv[1], sys.argv[2]
J = Path("app/src/main/java/com/decoutkhanqindev/aodamn/presentation")
enum = J / "model/aod/theme/AodThemeValue.kt"
entries = re.findall(r"^\s+([A-Z_]+)\(id = \"(\w+)\", nameRes = R\.string\.(\w+), spec = (\w+)\)", enum.read_text(encoding="utf-8"), re.M)
spec_import = re.search(rf"import \S+\.model\.aod\.theme\.{pkg}\.(\w+)", enum.read_text(encoding="utf-8"))
new = next((e for e in entries if spec_import and e[3] == spec_import.group(1)), None)
if not new:
    print(f"[FAIL] AodThemeValue chưa có mục dùng spec của gói {pkg}"); sys.exit(1)
vi = Path("app/src/main/res/values-vi/strings.xml").read_text(encoding="utf-8")
def vi_name(key):
    m = re.search(rf'<string name="{key}">([^<]+)</string>', vi)
    return m.group(1) if m else None
prev = entries[entries.index(new) - 1]
prev_name = vi_name(prev[2])
fail = False
if vi_name(new[2]) != name:
    print(f"[FAIL] values-vi {new[2]} = {vi_name(new[2])!r}, khác {name!r}"); fail = True
checks = [
    ("README.md", name, None), ("CHANGELOG.md", name, None), ("plans/muviz-edge-themes.md", name, None),
    (str(J / "components/aod/theme/AodThemeLayout.kt"), f".{pkg}.", f".{prev[1].replace('_', '')}."),
    ("app/src/test/java/com/decoutkhanqindev/aodamn/presentation/model/aod/theme/AodThemeSpecTest.kt", new[0], None),
    ("tools/mockups/aod_landscape_mockups.py", f"w_{new[1]}", None),
]
for path, needle, ref in checks:
    n = Path(path).read_text(encoding="utf-8").count(needle)
    want = Path(path).read_text(encoding="utf-8").count(ref) if ref else 1
    tag = "OK" if n >= want and n > 0 else "FAIL"
    fail |= tag == "FAIL"
    hint = f" (chủ đề trước {prev_name}: {want})" if ref else ""
    print(f"[{tag}]{' ' * (5 - len(tag))} {path}: {needle!r} xuất hiện {n} lần{hint}")
# Đoạn README nhắc chủ đề trước mà chưa nhắc chủ đề mới: thường là câu liệt kê tên chủ đề còn thiếu (kiểm tay, không tính FAIL).
blocks, cur = [], []
for line in Path("README.md").read_text(encoding="utf-8").splitlines():
    if not line.strip() or re.match(r"\s*(- |\||#|\d+[a-z]?\. )", line):
        if cur: blocks.append(" ".join(cur))
        cur = [line.strip()] if line.strip() else []
    else:
        cur.append(line.strip())
if cur: blocks.append(" ".join(cur))
for b in blocks:
    if prev_name in b and name not in b and not re.match(rf"\d+[a-z]?\. Chủ đề {re.escape(prev_name)}", b):
        i = b.index(prev_name)
        print(f"[CHECK] README chưa có {name!r} cạnh {prev_name!r}: …{b[max(0, i - 60):i + 60]}…")
sys.exit(1 if fail else 0)
EOF

echo
[ "$FAIL" = 0 ] && echo "Tất cả OK" || { echo "Còn mục FAIL"; exit 1; }
