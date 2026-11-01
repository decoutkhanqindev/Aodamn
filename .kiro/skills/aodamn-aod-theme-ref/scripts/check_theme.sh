#!/usr/bin/env bash
# Kiểm một chủ đề AOD mới trước khi báo xong. Chạy ở gốc repo (hoặc worktree):
#   bash .kiro/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói> "<Tên Việt>" [--theme-id=<id>] [--device|--editor|--record|--record-editor|--blocks|--record-blocks] [--force]
set -u
set -o pipefail
PKG="${1:?thiếu gói chủ đề, vd tick}"
NAME="${2:?thiếu tên tiếng Việt, vd \"Nhịp giây\"}"
THEME_ID="$PKG"
MODE=fast
FORCE=false
for arg in "${@:3}"; do
  case "$arg" in
    --theme-id=*) THEME_ID="${arg#--theme-id=}" ;;
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
J=app/src/main/java/com/decoutkhanqindev/aodamn/presentation
TEST_ROOT=app/src/androidTest/java/com/decoutkhanqindev/aodamn/testing
DEBUG_TEST_ROOT=app/src/debug/java/com/decoutkhanqindev/aodamn/testing
SERIAL="${AOD_TEST_SERIAL:-}"
AVD_NAME="${AOD_TEST_AVD:-}"
LOCK_DIR=""
TEST_PACKAGE="com.decoutkhanqindev.aodamn.test"
RUN_ID="$(date -u +%Y%m%dT%H%M%SZ)-$THEME_ID-$MODE"
REPORT="build/theme-tests/$RUN_ID"
CACHE="build/theme-tests/cache/$THEME_ID"
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
run_git() {
  if command -v cmd.exe >/dev/null 2>&1; then
    cmd.exe /d /c git "$@" | tr -d '\r'
  else
    git "$@"
  fi
}
ROOT="$(run_git rev-parse --show-toplevel)" || exit 1
cd "$ROOT"
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
release_device_lock() {
  [ -n "$LOCK_DIR" ] && rmdir "$LOCK_DIR" >/dev/null 2>&1 || true
}
reserve_device() {
  mapfile -t devices < <(run_adb devices | tr -d '\r' | awk 'NR > 1 && $2 == "device" && $1 ~ /^emulator-/ { print $1 }')
  local candidate sdk lock
  if [ -n "$SERIAL" ]; then
    printf '%s\n' "${devices[@]}" | grep -Fxq "$SERIAL" || {
      bad "không có máy $SERIAL; đang có: ${devices[*]:-không có}"
      return 1
    }
    sdk="$(run_adb -s "$SERIAL" shell getprop ro.build.version.sdk | tr -d '\r')"
    [ "$sdk" = 37 ] || {
      bad "$SERIAL dùng API $sdk, cần API 37"
      return 1
    }
    lock="${TMPDIR:-/tmp}/aod-theme-test-$SERIAL.lock"
    mkdir "$lock" 2>/dev/null || {
      bad "$SERIAL đang chạy test khác"
      return 1
    }
    LOCK_DIR="$lock"
    return 0
  fi
  for candidate in "${devices[@]}"; do
    sdk="$(run_adb -s "$candidate" shell getprop ro.build.version.sdk | tr -d '\r')"
    [ "$sdk" = 37 ] || continue
    lock="${TMPDIR:-/tmp}/aod-theme-test-$candidate.lock"
    mkdir "$lock" 2>/dev/null || continue
    SERIAL="$candidate"
    LOCK_DIR="$lock"
    return 0
  done
  bad "không có máy ảo API 37 rảnh; đang có: ${devices[*]:-không có}"
  return 1
}
prepare_device() {
  reserve_device || return
  local avd sdk size density
  avd="$(run_adb -s "$SERIAL" emu avd name 2>/dev/null | tr -d '\r' | head -1)"
  [ -n "$avd" ] || {
    bad "$SERIAL không phải máy ảo Android"
    release_device_lock
    return
  }
  if [ -n "$AVD_NAME" ] && [ "$avd" != "$AVD_NAME" ]; then
    bad "$SERIAL là $avd, cần $AVD_NAME"
    release_device_lock
    return
  fi
  AVD_NAME="$avd"
  run_adb -s "$SERIAL" shell wm size 1440x3120 >/dev/null
  run_adb -s "$SERIAL" shell wm density 560 >/dev/null
  run_adb -s "$SERIAL" shell settings put system time_12_24 24
  run_adb -s "$SERIAL" shell settings put system font_scale 1.0
  run_adb -s "$SERIAL" shell settings put global window_animation_scale 1.0
  run_adb -s "$SERIAL" shell settings put global transition_animation_scale 1.0
  run_adb -s "$SERIAL" shell settings put global animator_duration_scale 1.0
  sdk="$(run_adb -s "$SERIAL" shell getprop ro.build.version.sdk | tr -d '\r')"
  size="$(run_adb -s "$SERIAL" shell wm size | tr -d '\r')"
  density="$(run_adb -s "$SERIAL" shell wm density | tr -d '\r')"
  [ "$sdk" = 37 ] || bad "API $sdk, cần API 37"
  [[ "$size" == *"1440x3120"* ]] || bad "màn sai: $size"
  [[ "$density" == *"560"* ]] || bad "density sai: $density"
  cleanup_record_flag
  trap 'cleanup_record_flag; release_device_lock' EXIT
  export ANDROID_SERIAL="$SERIAL"
  ok "$AVD_NAME · $SERIAL · API 37 · 1440x3120 · 560dpi · 24h · font 1.0"
}
prepare_instrumentation() {
  run_gradle :app:assembleDebug :app:assembleDebugAndroidTest --console=plain || return
  run_adb -s "$SERIAL" install -r "app/build/outputs/apk/debug/app-debug.apk" || return
  run_adb -s "$SERIAL" install -r "app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
}
run_instrumentation() {
  local output status
  output="$(run_adb -s "$SERIAL" shell am instrument -w "$@")"
  status=$?
  printf '%s\n' "$output"
  [ "$status" -eq 0 ] || return "$status"
  case "$output" in
    *"FAILURES!!!"*|*"INSTRUMENTATION_CODE: -1"*) return 1 ;;
  esac
}
run_theme_instrumentation() {
  local groups="$1"
  run_instrumentation \
    -e class "$THEME_CLASSES" \
    -e themeId "$THEME_ID" \
    -e testGroups "$groups" \
    -e runId "$RUN_ID" \
    "$TEST_PACKAGE/androidx.test.runner.AndroidJUnitRunner"
}
run_block_instrumentation() {
  run_instrumentation \
    -e class "$BLOCK_CLASS" \
    -e runId "$RUN_ID" \
    "$TEST_PACKAGE/androidx.test.runner.AndroidJUnitRunner"
}
clear_device_screenshots() {
  run_adb -s "$SERIAL" shell rm -rf "/sdcard/Download/screenshots/$TEST_PACKAGE"
  run_adb -s "$SERIAL" shell mkdir -p "/sdcard/Download/screenshots/$TEST_PACKAGE"
}
begin_recording() {
  clear_device_screenshots
  run_adb -s "$SERIAL" shell touch "/sdcard/Download/screenshots/$TEST_PACKAGE/.isRecordingScreenshots"
}
copy_device_reports() {
  run_adb -s "$SERIAL" pull "/sdcard/Download/screenshots/$TEST_PACKAGE" "$REPORT/dropshots" >/dev/null 2>&1 || true
}
copy_recorded_references() {
  local source="$REPORT/dropshots/reference"
  [ -d "$source" ] || {
    bad "không có ảnh record từ $SERIAL"
    return 1
  }
  cp -R "$source/." app/src/androidTest/screenshots/
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
  run_step "build APK test" instrument-build prepare_instrumentation || return 1
  clear_device_screenshots
  local status=0
  run_step "test máy theo nhóm: $joined" device-theme run_theme_instrumentation "$joined" || status=$?
  if [ "$status" -eq 0 ]; then
    for group in "${selected[@]}"; do
      mark_group "$group" "${hashes[$group]}"
      ok "ghi dấu pass: $group"
    done
  fi
  copy_device_reports
  return "$status"
}
mark_theme_groups() {
  local group
  for group in behavior screens options editor motion; do
    mark_group "$group" "$(group_hash "$group")"
  done
}

printf 'run_id=%s\nmode=%s\ntheme_id=%s\npackage=%s\n' "$RUN_ID" "$MODE" "$THEME_ID" "$PKG" > "$REPORT/run.txt"

[ -n "$PYTHON" ] || bad "thiếu python3 / python"
[ -d "$J/components/aod/theme/$PKG" ] && [ -d "$J/model/aod/theme/$PKG" ] \
  && ok "có $J/{components,model}/aod/theme/$PKG" || bad "thiếu thư mục components/ hoặc model/aod/theme/$PKG"
[ -f local.properties ] && ok "có local.properties" || bad "thiếu local.properties"

if [ "$MODE" = fast ] && [ "$FAIL" -eq 0 ]; then
  LOCAL_HASH="$(group_hash local)"
  if is_group_changed local "$LOCAL_HASH"; then
    if run_step "compileDebugKotlin + JVM option, pairwise và lưu cấu hình của $PKG" local-theme \
      run_gradle "-Daod.theme.test.id=$THEME_ID" :app:compileDebugKotlin :app:testDebugUnitTest \
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
  BASE="$(run_git merge-base HEAD main 2>/dev/null || run_git rev-parse HEAD)"
  OLD=()
  for d in "$J"/components/aod/theme/*/ "$J"/model/aod/theme/*/; do
    theme_dir="$(basename "$d")"
    [ "$theme_dir" = "$PKG" ] || [ "$theme_dir" = component ] || OLD+=("$d")
  done
  CHANGED="$(run_git diff --name-only "$BASE" -- "${OLD[@]}"; run_git ls-files --others --exclude-standard -- "${OLD[@]}")"
  [ -z "$CHANGED" ] && ok "thư mục chủ đề cũ không đổi" || { bad "chủ đề cũ bị đổi"; printf '%s\n' "$CHANGED" | tee -a "$REPORT/summary.txt"; }

  "$PYTHON" -I -X utf8 - "$PKG" "$THEME_ID" "$NAME" <<'PY' | tee "$REPORT/registration.log" || FAIL=1
import re, sys
from pathlib import Path
pkg, theme_id, name = sys.argv[1], sys.argv[2], sys.argv[3]
J = Path("app/src/main/java/com/decoutkhanqindev/aodamn/presentation")
enum = J / "model/aod/theme/AodThemeValue.kt"
entries = re.findall(r"^\s+([A-Z_]+)\(id = \"(\w+)\", nameRes = R\.string\.(\w+), spec = (\w+)\)", enum.read_text(encoding="utf-8"), re.M)
spec_import = re.search(rf"import \S+\.model\.aod\.theme\.{pkg}\.(\w+)", enum.read_text(encoding="utf-8"))
new = next((entry for entry in entries if spec_import and entry[3] == spec_import.group(1)), None)
if not new:
    print(f"[FAIL] AodThemeValue chưa có mục dùng spec của gói {pkg}")
    sys.exit(1)
if new[1] != theme_id:
    print(f"[FAIL] theme id {theme_id!r} không khớp id {new[1]!r} của gói {pkg}")
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
    [ "$FAIL" -eq 0 ] && run_step "build APK test" instrument-build prepare_instrumentation
    [ "$FAIL" -eq 0 ] && begin_recording
    [ "$FAIL" -eq 0 ] && run_step "record ảnh panel editor của $PKG" record-editor run_theme_instrumentation editor
    cleanup_record_flag
    copy_device_reports
    [ "$FAIL" -eq 0 ] && copy_recorded_references && mark_group editor "$(group_hash editor)"
    ;;
  record-theme)
    prepare_device
    [ "$FAIL" -eq 0 ] && run_step "build APK test" instrument-build prepare_instrumentation
    [ "$FAIL" -eq 0 ] && begin_recording
    [ "$FAIL" -eq 0 ] && run_step "record riêng ảnh $PKG" record-theme run_theme_instrumentation screens+options+editor+motion
    cleanup_record_flag
    copy_device_reports
    [ "$FAIL" -eq 0 ] && copy_recorded_references && mark_theme_groups
    ;;
  blocks)
    BLOCK_HASH="$(group_hash blocks)"
    if is_group_changed blocks "$BLOCK_HASH"; then
      prepare_device
      [ "$FAIL" -eq 0 ] && run_step "build APK test" instrument-build prepare_instrumentation
      [ "$FAIL" -eq 0 ] && clear_device_screenshots
      [ "$FAIL" -eq 0 ] && run_step "ảnh block dùng chung" device-blocks run_block_instrumentation
      [ "$FAIL" -eq 0 ] && mark_group blocks "$BLOCK_HASH"
      copy_device_reports
    else
      skip "blocks không đổi từ lần pass gần nhất"
    fi
    ;;
  record-blocks)
    prepare_device
    [ "$FAIL" -eq 0 ] && run_step "build APK test" instrument-build prepare_instrumentation
    [ "$FAIL" -eq 0 ] && begin_recording
    [ "$FAIL" -eq 0 ] && run_step "record riêng block dùng chung" record-blocks run_block_instrumentation
    cleanup_record_flag
    copy_device_reports
    [ "$FAIL" -eq 0 ] && copy_recorded_references && mark_group blocks "$(group_hash blocks)"
    ;;
esac

cleanup_record_flag
printf '\nBáo cáo: %s\n' "$REPORT"
[ "$FAIL" = 0 ] && echo "Tất cả OK" || { echo "Còn mục FAIL"; exit 1; }
