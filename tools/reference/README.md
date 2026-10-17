# Lập danh mục chủ đề từ app mẫu

Làm nhanh khi có một app mẫu cài trên máy test: biết nó có những chủ đề AOD nào, mỗi chủ đề có tuỳ chọn và giá trị mặc định
gì, chủ đề nào mình đã clone. Làm lần đầu với Muviz Edge (2026-10-17), kết quả ở `plans/muviz-edge-themes.md`.

Ranh giới: chỉ đọc cấu trúc, tên, tuỳ chọn, giá trị mặc định để tự làm lại theo kiến trúc của app. Không chép code dịch
ngược, không lấy asset (font, ảnh, Lottie, hình nền) từ APK; cần font thì lấy bản gốc có giấy phép mở và hỏi trước khi
thêm. APK và mã dịch ngược để ngoài repo (mặc định `~/.local/share/rea/work/<package>/`, script chặn thư mục trong repo).

## Cài một lần

- Node ≥ 22.19 (máy dùng nvm: `nvm use 24.12.0`). REA đăng ký MCP bằng `C:\nvm4w\nodejs\node.exe`, lùi nvm về bản cũ
  hơn là REA không chạy.
- REA: `npm install -g rea-agents@latest --ignore-scripts`. Bỏ script vì gói tuỳ chọn `frida` treo lúc tải bản dựng sẵn
  (chỉ cần cho phân tích động); cập nhật cũng dùng lệnh này thay cho `rea update`.
- JADX cho REA: `jadx-headless-mcp-0.7.1-all.jar` (release v0.7.1 của `1013503897/jadx-headless-mcp` trên GitHub) vào
  `~/.local/share/rea/android/`, SHA-256 phải là `6e5eacf500b64292bfb73c49797c1958f6ee44646e43e868039ae7feb573ff75`
  (giá trị REA ghim ở `src/android/JadxRelease.ts`).
- Đăng ký MCP cho Claude Code (tuỳ chọn, CLI `rea …` dùng được không cần): đặt `$env:REA_JADX_MCP_JAR` tới jar trên, chạy
  `rea setup --client claude_code --dry-run --json`, xem kế hoạch rồi chạy lại với `--yes` thay `--dry-run`. Setup ghi
  `mcpServers.rea` (kèm `JAVA_HOME`, `REA_JADX_MCP_JAR`) vào `~/.claude.json` và skill `reverse-engineer-anything`;
  tool MCP chỉ hiện ở phiên mới.
- JDK 17+ (JBR của Android Studio qua `JAVA_HOME`), `adb` và `aapt2` của Android SDK, Python 3.

## Các bước

1. Kéo, dịch ngược, lập chỉ mục (khoảng 30 giây):
   `python -I -X utf8 tools/reference/pull_decompile.py <package>` (`--serial` nếu cắm nhiều máy, `--out` để làm lại
   từ đầu). Đọc `index.md` trước: package của app, class nghi là chủ đề, **file tra cứu** (file import nhiều class chủ
   đề, thường là bảng mã → class và mã → nhãn; với Muviz là `ib/a.java`), font, layout, tiền tố chuỗi (nhãn tuỳ chọn
   thường là nhóm tiền tố lớn nhất, Muviz là `prop_`).
2. Tìm mô hình chủ đề: mở một class chủ đề và file tra cứu, tìm hàm trả tên, hàm trả giá trị mặc định, hàm trả danh sách
   tuỳ chọn; tìm màn chỉnh (`*Edit*Activity`) rẽ nhánh theo loại control để biết loại nào là thanh trượt, chọn một, màu.
   Cần bằng chứng cho một method: `rea inspect-android-method <apk> <class> <method>` (ghi Evidence ID vào tài liệu).
3. Viết bộ trích: chép `extract_muviz.py` thành `extract_<app>.py`, sửa phần đọc theo mô hình mới; chạy ra
   `catalog.json` và `catalog.md` (bảng Markdown) trong thư mục làm việc.
4. Đối chiếu với chủ đề đã có (`AodThemeValue`) để không làm trùng: khớp bằng ảnh tab tuỳ chọn của app mẫu trong
   `reference/theme_N/` và giá trị mặc định (màu), không đoán theo tên (lần Muviz đoán theo tên sai 2 / 3). Ghi kết quả
   vào `CLONED` của bộ trích.
5. Ghi `plans/<app>-themes.md`: mô hình, đối chiếu, bảng, đề xuất làm tiếp, câu hỏi còn mở, bằng chứng (SHA-256 APK,
   Evidence ID). Chốt với user chủ đề làm tiếp.
6. Làm chủ đề theo README › Thêm chủ đề mới. **Không xin user chụp ảnh**: số đo lấy từ dịch ngược. Layout của chủ đề:
   `aapt2 dump xmltree --file res/layout/<tên>.xml <apk>` (có thêm `res/layout-land/` thì xem bản ngang), id font / màu
   trong layout tra ở `res-base.txt`; code của chủ đề cho cỡ nhân theo tuỳ chọn, kiểu, 24 giờ; view chữ riêng (vd
   `NoPadTextView` của Muviz: khung = khung nét chữ, viền = 1% cỡ chữ) đọc ở `src/sources`. Font: đo chữ số của font trong
   APK (cao / em, rộng / cao, độ dày nét, bước) bằng PIL, so với font có sẵn, chỉ đo, không chép file font. Rồi mock, user
   duyệt mới code.

## Lỗi hay gặp

- Python đọc file dịch ngược luôn chạy `-I`; thêm `-X utf8` vì console Windows là cp1252 (lỗi khi in chữ Việt) và `-I`
  bỏ qua `PYTHONIOENCODING`.
- `rea pull-adb-package` không ghi đè thư mục ra đã có; script dùng lại APK đã kéo, muốn kéo bản mới thì đổi `--out`.
- Kết quả CLI của REA mặc định là văn bản dạng YAML, không phải JSON.
- Split `split_config.<abi>.apk` chỉ có thư viện native, không có bảng resource: script bỏ qua.
- Tên field / method do R8 đặt (`f1783a`, `W()`) đổi theo từng bản build của app mẫu: app cập nhật thì kiểm lại bộ trích.
