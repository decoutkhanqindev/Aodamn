#!/usr/bin/env bash
# Kiểm một chủ đề AOD mới trước khi báo xong. Chạy ở gốc repo (hoặc worktree):
#   bash .kiro/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói> "<Tên Việt>" [--device|--editor|--record|--record-editor|--blocks|--record-blocks] [--force]
set -u
set -o pipefail
PKG="${1:?thiếu gói chủ đề, vd tick}"
NAME="${2:?thiếu tên tiếng Việt, vd \"Nhịp giây\"}"
MODE=fast
FORCE=false
for arg in "${@:3}"; do
  case "$arg" in
    --device) MODE=theme ;;
    --editor) MODE=editor ;;
    --record) MODE=record-theme ;;
    --record-editor) MODE=record-editor ;;
    --blocks) MODE=blocks ;;
    --record-blocks) MODE=record-blocks ;;
    --force) FORCE=true ;;
    *) echo "Cờ lạ: $arg"; exit 2 ;;
  esac
done
ROOT="$(git rev-parse --show-toplevel)" || exit 1
cd "$ROOT"
J=app/src/main/java/com/decoutkhanqindev/aodamn/presentation
TEST_ROOT=app/src/androidTest/java/com/decoutkhanqindev/aodamn/testing
DEBUG_TEST_ROOT=app/src/debug/java/com/decoutkhanqindev/aodamn/testing
SERIAL="${AOD_TEST_SERIAL:-emulator-5554}"
AVD_NAME="${AOD_TEST_AVD:-Pixel_7_Pro}"
TEST_PACKAGE="com.decoutkhanqindev.aodamn.test"
RUN_ID="$(date -u +%Y%m%dT%H%M%SZ)-$PKG-$MODE"
REPORT="build/theme-tests/$RUN_ID"
CACHE="build/theme-tests/cache/$PKG"
THEME_CLASSES="com.decoutkhanqindev.aodamn.testing.AodThemeTestSuite"
EDITOR_CLASS="com.decoutkhanqindev.aodamn.testing.AodThemeEditorScreenshotTest"
BLOCK_CLASS="com.decoutkhanqindev.aodamn.testing.AodSharedBlockScreenshotTest"
PYTHON="$(command -v python3 || command -v python || true)"
FAIL=0
mkdir -p "$REPORT" "$CACHE"
ok() { echo "[OK]   $1" | tee -a "$REPORT/summary.txt"; }
skip() { echo "[SKIP] $1" | tee -a "$REPORT/summary.txt"; }
bad() { echo "[FAIL] $1" | tee -a "$REPORT/summary.txt"; FAIL=1; }
run_gradle() {
  if command -v cmd.exe >/dev/null 2>&1; then
    cmd.exe /d /c '.\gradlew.bat' "$@"
  else
    ./gradlew "$@"
  fi
}
run_adb() {
  if command -v adb >/dev/null 2>&1; then
    adb "$@"
  elif command -v adb.exe >/dev/null 2>&1; then
    adb.exe "$@"
  else
    cmd.exe /d /c adb "$@"
  fi
}
run_step() {
  local label="$1"
  local log="$2"
  shift 2
  if "$@" > "$REPORT/$log.log" 2>&1; then
    ok "$label"
    return 0
  fi
  bad "$label; xem $REPORT/$log.log"
  tail -20 "$REPORT/$log.log"
  return 1
}
cleanup_record_flag() {
  run_adb -s "$SERIAL" shell rm -f "/sdcard/Download/screenshots/$TEST_PACKAGE/.isRecordingScreenshots" >/dev/null 2>&1 || true
}
prepare_device() {
  mapfile -t devices < <(run_adb devices | tr -d '\r' | awk 'NR > 1 && $2 == "device" { print $1 }')
  if [ "${#devices[@]}" -ne 1 ] || [ "${devices[0]:-}" != "$SERIAL" ]; then
    bad "cần đúng một máy $SERIAL; đang có: ${devices[*]:-không có}"
    return
  fi
  local avd
  avd="$(run_adb -s "$SERIAL" emu avd name 2>/dev/null | tr -d '\r' | head -1)"
  if [ "$avd" != "$AVD_NAME" ]; then
    bad "$SERIAL là $avd, cần $AVD_NAME"
    return
  fi
  run_adb -s "$SERIAL" shell wm size 1440x3120 >/dev/null
  run_adb -s "$SERIAL" shell wm density 560 >/dev/null
  run_adb -s "$SERIAL" shell settings put system time_12_24 24
  run_adb -s "$SERIAL" shell settings put system font_scale 1.0
  run_adb -s "$SERIAL" shell settings put global window_animation_scale 1.0
  run_adb -s "$SERIAL" shell settings put global transition_animation_scale 1.0
  run_adb -s "$SERIAL" shell settings put global animator_duration_scale 1.0
  local sdk size density
  sdk="$(run_adb -s "$SERIAL" shell getprop ro.build.version.sdk | tr -d '\r')"
  size="$(run_adb -s "$SERIAL" shell wm size | tr -d '\r')"
  density="$(run_adb -s "$SERIAL" shell wm density | tr -d '\r')"
  [ "$sdk" = 37 ] || bad "API $sdk, cần API 37"
  [[ "$size" == *"1440x3120"* ]] || bad "màn sai: $size"
  [[ "$density" == *"560"* ]] || bad "density sai: $density"
  cleanup_record_flag
  trap cleanup_record_flag EXIT
  export ANDROID_SERIAL="$SERIAL"
  ok "$AVD_NAME · $SERIAL · API 37 · 1440x3120 · 560dpi · 24h · font 1.0"
}
copy_device_reports() {
  [ -d app/build/reports/androidTests/connected/debug ] && cp -R app/build/reports/androidTests/connected/debug "$REPORT/android-report"
  [ -d app/build/test-results/dropshots/DebugAndroidTest ] && cp -R app/build/test-results/dropshots/DebugAndroidTest "$REPORT/dropshots"
}
hash_paths() {
  "$PYTHON" -I -X utf8 - "$@" <<'PY'
import hashlib, sys
from pathlib import Path
files = set()
for raw in sys.argv[1:]:
    path = Path(raw)
    if path.is_file():
        files.add(path)
    elif path.is_dir():
        files.update(item for item in path.rglob("*") if item.is_file())
digest = hashlib.sha256()
for path in sorted(files, key=lambda item: item.as_posix()):
    digest.update(path.as_posix().encode())
    digest.update(b"\0")
    digest.update(path.read_bytes())
    digest.update(b"\0")
print(digest.hexdigest())
PY
}
group_hash() {
  local group="$1"
  case "$group" in
    local)
      hash_paths \
        "$J/model/aod/theme/$PKG" \
        "$J/components/aod/theme/$PKG" \
        "$J/model/aod/theme"/*.kt \
        "$DEBUG_TEST_ROOT/ThemeExpectation.kt" \
        app/src/test/java/com/decoutkhanqindev/aodamn/presentation/model/aod/theme \
        app/build.gradle.kts gradle/libs.versions.toml
      ;;
    behavior)
      hash_paths \
        "$J/model/aod/theme/$PKG" \
        "$J/model/aod/theme"/*.kt \
        "$J/screens/themeeditor" \
        "$J/screens/aodtab" \
        app/src/main/java/com/decoutkhanqindev/aodamn/data/local/datastore \
        app/src/main/java/com/decoutkhanqindev/aodamn/data/device/permission \
        "$DEBUG_TEST_ROOT/ThemeExpectation.kt" \
        "$TEST_ROOT/AodThemeFlowTest.kt" \
        "$TEST_ROOT/AodThemeEditorBehaviorTest.kt" \
        "$TEST_ROOT/ThemeDeviceRule.kt" \
        "$TEST_ROOT/ThemeTestKit.kt" \
        "$TEST_ROOT/AodThemeTestSuite.kt"
      ;;
    screens)
      hash_paths \
        "$J/model/aod/theme/$PKG" \
        "$J/components/aod/theme/$PKG" \
        "$J/components/aod"/*.kt \
        "$J/components/aod/theme"/*.kt \
        "$J/components/aod/block" \
        "$J/model/aod/theme/component" \
        "$J/screens/aod" \
        "$DEBUG_TEST_ROOT" \
        "$TEST_ROOT/AodThemeScreenshotTest.kt" \
        "$TEST_ROOT/ThemeTestActivity.kt" \
        "$TEST_ROOT/ThemeTestKit.kt" \
        "$TEST_ROOT/AodThemeTestSuite.kt" \
        app/src/main/res/font app/src/main/res/drawable app/src/main/res/values app/src/main/res/values-vi
      ;;
    options)
      hash_paths \
        "$J/model/aod/theme/$PKG" \
        "$J/components/aod/theme/$PKG" \
        "$J/components/aod"/*.kt \
        "$J/components/aod/theme"/*.kt \
        "$J/components/aod/block" \
        "$J/model/aod/theme/component" \
        "$J/screens/aod" \
        "$DEBUG_TEST_ROOT" \
        "$TEST_ROOT/AodThemeOptionScreenshotTest.kt" \
        "$TEST_ROOT/ThemeTestContent.kt" \
        "$TEST_ROOT/ThemeTestKit.kt" \
        "$TEST_ROOT/AodThemeTestSuite.kt" \
        app/src/main/res/font app/src/main/res/drawable app/src/main/res/values app/src/main/res/values-vi
      ;;
    editor)
      hash_paths \
        "$J/model/aod/theme/$PKG" \
        "$J/components/aod/theme/$PKG" \
        "$J/components/aod/editor" \
        "$J/components/aod/theme"/*.kt \
        "$J/model/aod/theme/component" \
        "$DEBUG_TEST_ROOT/ThemeExpectation.kt" \
        "$TEST_ROOT/AodThemeEditorScreenshotTest.kt" \
        "$TEST_ROOT/ThemeDeviceRule.kt" \
        "$TEST_ROOT/AodThemeTestSuite.kt" \
        app/src/main/res/font app/src/main/res/drawable app/src/main/res/values app/src/main/res/values-vi
      ;;
    motion)
      hash_paths \
        "$J/model/aod/theme/$PKG" \
        "$J/components/aod/theme/$PKG" \
        "$J/components/aod"/*.kt \
        "$J/components/aod/theme"/*.kt \
        "$J/screens/aod" \
        "$DEBUG_TEST_ROOT" \
        "$TEST_ROOT/AodThemeMotionTest.kt" \
        "$TEST_ROOT/ThemeTestActivity.kt" \
        "$TEST_ROOT/ThemeTestKit.kt" \
        "$TEST_ROOT/AodThemeTestSuite.kt" \
        app/src/main/res/font app/src/main/res/drawable app/src/main/res/values app/src/main/res/values-vi
      ;;
    blocks)
      hash_paths \
        "$J/components/aod/block" \
        "$J/model/aod/theme/component" \
        "$TEST_ROOT/AodSharedBlockScreenshotTest.kt" \
        "$TEST_ROOT/ThemeTestContent.kt" \
        "$TEST_ROOT/ThemeDeviceRule.kt" \
        app/src/main/res/font app/src/main/res/drawable app/src/main/res/values app/src/main/res/values-vi
      ;;
  esac
}
is_group_changed() {
  local group="$1"
  local hash="$2"
  [ "$FORCE" = true ] || [ ! -f "$CACHE/$group.sha256" ] || [ "$(tr -d '\r\n' < "$CACHE/$group.sha256")" != "$hash" ]
}
mark_group() {
  local group="$1"
  local hash="$2"
  printf '%s\n' "$hash" > "$CACHE/$group.sha256"
}
passed_theme_groups() {
  local xml
  xml="$(find app/build/outputs/androidTest-results/connected/debug -maxdepth 1 -type f -name 'TEST-*.xml' -print -quit 2>/dev/null)"
  [ -n "$xml" ] || return
  "$PYTHON" -I -X utf8 - "$xml" "$@" <<'PY'
import sys
from xml.etree import ElementTree
xml, *selected = sys.argv[1:]
expected = {
    "behavior": {
        "com.decoutkhanqindev.aodamn.testing.AodThemeFlowTest",
        "com.decoutkhanqindev.aodamn.testing.AodThemeEditorBehaviorTest",
    },
    "screens": {"com.decoutkhanqindev.aodamn.testing.AodThemeScreenshotTest"},
    "options": {"com.decoutkhanqindev.aodamn.testing.AodThemeOptionScreenshotTest"},
    "editor": {"com.decoutkhanqindev.aodamn.testing.AodThemeEditorScreenshotTest"},
    "motion": {"com.decoutkhanqindev.aodamn.testing.AodThemeMotionTest"},
}
results = {
    suite.attrib["name"]: int(suite.attrib.get("failures", 0)) + int(suite.attrib.get("errors", 0))
    for suite in ElementTree.parse(xml).getroot().findall("testsuite")
}
for group in selected:
    classes = expected[group]
    if classes.issubset(results) and all(results[name] == 0 for name in classes):
        print(group)
PY
}
run_changed_theme_groups() {
  local requested=("$@")
  local selected=()
  local group hash
  declare -A hashes
  for group in "${requested[@]}"; do
    hash="$(group_hash "$group")"
    hashes[$group]="$hash"
    if is_group_changed "$group" "$hash"; then
      selected+=("$group")
    else
      skip "$group không đổi từ lần pass gần nhất"
    fi
  done
  [ "${#selected[@]}" -gt 0 ] || return 0
  local joined
  joined="$(IFS=+; echo "${selected[*]}")"
  prepare_device
  [ "$FAIL" -eq 0 ] || return 1
  local status=0
  run_step "test máy theo nhóm: $joined" device-theme \
    run_gradle :app:connectedDebugAndroidTest \
    "-Pandroid.testInstrumentationRunnerArguments.class=$THEME_CLASSES" \
    "-Pandroid.testInstrumentationRunnerArguments.themeId=$PKG" \
    "-Pandroid.testInstrumentationRunnerArguments.testGroups=$joined" \
    "-Pandroid.testInstrumentationRunnerArguments.runId=$RUN_ID" --console=plain || status=$?
  local passed
  while IFS= read -r passed; do
    [ -n "$passed" ] || continue
    mark_group "$passed" "${hashes[$passed]}"
    ok "ghi dấu pass: $passed"
  done < <(passed_theme_groups "${selected[@]}")
  copy_device_reports
  return "$status"
}
mark_theme_groups() {
  local group
  for group in behavior screens options editor motion; do
    mark_group "$group" "$(group_hash "$group")"
  done
}

printf 'run_id=%s\nmode=%s\ntheme=%s\n' "$RUN_ID" "$MODE" "$PKG" > "$REPORT/run.txt"

[ -n "$PYTHON" ] || bad "thiếu python3 / python"
[ -d "$J/components/aod/theme/$PKG" ] && [ -d "$J/model/aod/theme/$PKG" ] \
  && ok "có $J/{components,model}/aod/theme/$PKG" || bad "thiếu thư mục components/ hoặc model/aod/theme/$PKG"
[ -f local.properties ] && ok "có local.properties" || bad "thiếu local.properties"

if [ "$MODE" = fast ] && [ "$FAIL" -eq 0 ]; then
  LOCAL_HASH="$(group_hash local)"
  if is_group_changed local "$LOCAL_HASH"; then
    if run_step "compileDebugKotlin + JVM option, pairwise và lưu cấu hình của $PKG" local-theme \
      run_gradle "-Daod.theme.test.id=$PKG" :app:compileDebugKotlin :app:testDebugUnitTest \
      --tests "*AodThemeExpectationTest" --tests "*AodThemeContractTest" --console=plain -q; then
      mark_group local "$LOCAL_HASH"
    fi
  else
    skip "local không đổi từ lần pass gần nhất"
  fi
fi

RULES="$(bash tools/check-rules.sh 2>&1 | tail -1)"
case "$RULES" in *"0 vi phạm"*) ok "check-rules: $RULES" ;; *) bad "check-rules: $RULES" ;; esac

if [ "$MODE" = fast ]; then
  BASE="$(git merge-base HEAD main 2>/dev/null || git rev-parse HEAD)"
  OLD=()
  for d in "$J"/components/aod/theme/*/ "$J"/model/aod/theme/*/; do
    theme_dir="$(basename "$d")"
    [ "$theme_dir" = "$PKG" ] || [ "$theme_dir" = component ] || OLD+=("$d")
  done
  CHANGED="$(git diff --name-only "$BASE" -- "${OLD[@]}"; git ls-files --others --exclude-standard -- "${OLD[@]}")"
  [ -z "$CHANGED" ] && ok "thư mục chủ đề cũ không đổi" || { bad "chủ đề cũ bị đổi"; printf '%s\n' "$CHANGED" | tee -a "$REPORT/summary.txt"; }

  "$PYTHON" -I -X utf8 - "$PKG" "$NAME" <<'PY' | tee "$REPORT/registration.log" || FAIL=1
import re, sys
from pathlib import Path
pkg, name = sys.argv[1], sys.argv[2]
J = Path("app/src/main/java/com/decoutkhanqindev/aodamn/presentation")
enum = J / "model/aod/theme/AodThemeValue.kt"
entries = re.findall(r"^\s+([A-Z_]+)\(id = \"(\w+)\", nameRes = R\.string\.(\w+), spec = (\w+)\)", enum.read_text(encoding="utf-8"), re.M)
spec_import = re.search(rf"import \S+\.model\.aod\.theme\.{pkg}\.(\w+)", enum.read_text(encoding="utf-8"))
new = next((entry for entry in entries if spec_import and entry[3] == spec_import.group(1)), None)
if not new:
    print(f"[FAIL] AodThemeValue chưa có mục dùng spec của gói {pkg}")
    sys.exit(1)
vi = Path("app/src/main/res/values-vi/strings.xml").read_text(encoding="utf-8")
def vi_name(key):
    match = re.search(rf'<string name="{key}">([^<]+)</string>', vi)
    return match.group(1) if match else None
prev = entries[entries.index(new) - 1]
prev_name = vi_name(prev[2])
fail = vi_name(new[2]) != name
checks = [
    ("README.md", name, None),
    ("CHANGELOG.md", name, None),
    ("plans/muviz-edge-themes.md", name, None),
    (str(J / "components/aod/theme/AodThemeLayout.kt"), f".{pkg}.", f".{prev[1].replace('_', '')}."),
    ("app/src/test/java/com/decoutkhanqindev/aodamn/presentation/model/aod/theme/AodThemeSpecTest.kt", new[0], None),
    ("app/src/debug/java/com/decoutkhanqindev/aodamn/testing/ThemeExpectation.kt", f"AodThemeValue.{new[0]}", None),
    ("tools/mockups/aod_landscape_mockups.py", f"w_{new[1]}", None),
]
for path, needle, ref in checks:
    count = Path(path).read_text(encoding="utf-8").count(needle)
    wanted = Path(path).read_text(encoding="utf-8").count(ref) if ref else 1
    tag = "OK" if count >= wanted and count > 0 else "FAIL"
    fail |= tag == "FAIL"
    hint = f" (chủ đề trước {prev_name}: {wanted})" if ref else ""
    print(f"[{tag}]{' ' * (5 - len(tag))} {path}: {needle!r} xuất hiện {count} lần{hint}")
sys.exit(1 if fail else 0)
PY
fi

if [ "$FAIL" -ne 0 ]; then
  echo "Dừng trước test máy. Báo cáo: $REPORT" | tee -a "$REPORT/summary.txt"
  exit 1
fi

case "$MODE" in
  fast) ;;
  theme)
    run_changed_theme_groups behavior screens options editor motion
    ;;
  editor)
    run_changed_theme_groups editor
    ;;
  record-editor)
    prepare_device
    [ "$FAIL" -eq 0 ] && run_step "record ảnh panel editor của $PKG" record-editor \
      run_gradle :app:recordDebugAndroidTestScreenshots \
      "-Pandroid.testInstrumentationRunnerArguments.class=$EDITOR_CLASS" \
      "-Pandroid.testInstrumentationRunnerArguments.themeId=$PKG" \
      "-Pandroid.testInstrumentationRunnerArguments.runId=$RUN_ID" --console=plain
    cleanup_record_flag
    [ "$FAIL" -eq 0 ] && mark_group editor "$(group_hash editor)"
    copy_device_reports
    ;;
  record-theme)
    prepare_device
    [ "$FAIL" -eq 0 ] && run_step "record riêng ảnh $PKG" record-theme \
      run_gradle :app:recordDebugAndroidTestScreenshots \
      "-Pandroid.testInstrumentationRunnerArguments.class=$THEME_CLASSES" \
      "-Pandroid.testInstrumentationRunnerArguments.themeId=$PKG" \
      "-Pandroid.testInstrumentationRunnerArguments.runId=$RUN_ID" --console=plain
    cleanup_record_flag
    [ "$FAIL" -eq 0 ] && mark_theme_groups
    copy_device_reports
    ;;
  blocks)
    BLOCK_HASH="$(group_hash blocks)"
    if is_group_changed blocks "$BLOCK_HASH"; then
      prepare_device
      [ "$FAIL" -eq 0 ] && run_step "ảnh block dùng chung" device-blocks \
        run_gradle :app:connectedDebugAndroidTest \
        "-Pandroid.testInstrumentationRunnerArguments.class=$BLOCK_CLASS" \
        "-Pandroid.testInstrumentationRunnerArguments.runId=$RUN_ID" --console=plain
      [ "$FAIL" -eq 0 ] && mark_group blocks "$BLOCK_HASH"
      copy_device_reports
    else
      skip "blocks không đổi từ lần pass gần nhất"
    fi
    ;;
  record-blocks)
    prepare_device
    [ "$FAIL" -eq 0 ] && run_step "record riêng block dùng chung" record-blocks \
      run_gradle :app:recordDebugAndroidTestScreenshots \
      "-Pandroid.testInstrumentationRunnerArguments.class=$BLOCK_CLASS" \
      "-Pandroid.testInstrumentationRunnerArguments.runId=$RUN_ID" --console=plain
    cleanup_record_flag
    [ "$FAIL" -eq 0 ] && mark_group blocks "$(group_hash blocks)"
    copy_device_reports
    ;;
esac

cleanup_record_flag
printf '\nBáo cáo: %s\n' "$REPORT"
[ "$FAIL" = 0 ] && echo "Tất cả OK" || { echo "Còn mục FAIL"; exit 1; }
