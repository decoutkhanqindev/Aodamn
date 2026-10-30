# Đăng ký chủ đề mới ở mọi chỗ

Chủ đề mới chỉ thêm vào các chỗ dưới đây (không sửa dòng của chủ đề cũ, trừ khi một câu liệt kê phải chèn thêm tên). Tìm chỗ
bằng tên chủ đề vừa làm trước đó (vd `grep -n "Chồng số\|BIG_OVERLAP\|bigoverlap" <file>`): chỗ nào có tên đó thì chủ đề mới
cũng phải có. `scripts/check_theme.sh` đếm tên ở các chỗ này.

## Code

| File | Thêm |
|---|---|
| `model/aod/theme/AodThemeValue.kt` | import spec + mục enum cuối (id chữ thường, không đổi về sau; `;` ở mục cuối) |
| `components/aod/theme/AodThemeLayout.kt` | import, nhánh `AodPortrait`, nhánh `AodLandscape` (bố cục khớp khung chung thì `Aod<Chủ đề>Layout` chỉ gọi `AodHeroInfoPortrait` / `AodHeroInfoLandscape`, [reuse.md](reuse.md)) |
| `components/aod/block/AodMediaControls.kt` | import + nhánh trình phát (chủ đề tự vẽ nhạc); `Aod<Chủ đề>Media` gọi trình phát dùng chung với style trong `<Chủ đề>Metrics` khi khớp ([reuse.md](reuse.md)) |
| `<Chủ đề>Metrics` | style cho khối dùng chung (`val TextPlayer` / `WavePlayer` / `CompactPlayer`, `LatestLine` / `BadgeLatest`, `HeroInfo`), hằng `TEXT_DARKEN` cho `darkBrushOf` |
| `res/values/strings.xml` (mẫu dòng thông báo) | chuỗi định dạng "tiêu đề + nội dung" cho `lineText(R.string.aod_<chủ đề>_notification)` khi dùng `AodLatestLine` |
| `model/aod/theme/Aod*Style.kt`, khối ở `block/` / `theme/` | chỉ khi mở rộng khối dùng chung: thuộc tính mới có mặc định đúng hành vi cũ (hỏi user trước) |
| `app/src/test/.../AodThemeSpecTest.kt` | import, `drawsLatest` / `liveThemes` nếu đúng, test riêng |
| `theme/Colors.kt`, `theme/Typography.kt` | màu nhấn / họ font mới |
| `res/values/strings.xml`, `res/values-vi/strings.xml` | `theme_<id>`, nhãn màu / tuỳ chọn mới; mẫu định dạng `translatable="false"` chỉ ở `values/` |
| `tools/icons/icons.txt` | icon mới (dòng `light:` / `fill:` riêng), chạy `import_phosphor.py` |
| `tools/fonts/make_static_fonts.py` | mục `FONTS` + comment lý do |
| `tools/mockups/aod_landscape_mockups.py` | mục mock + dòng `("w_<chủ đề>", …)`; cần gói mới thì ghi ở docstring đầu file |

Thứ tự import theo cách IDE xếp (chữ cái trong khối `com.decoutkhanqindev…`), không chèn lệch.

## Docs

`README.md` (đếm bằng tên tiếng Việt của chủ đề vừa làm trước):
- › Cách dùng: danh sách chủ đề tự vẽ khối (câu "…Gợn sóng, Nhịp giây và Chồng số; thêm ghi nhớ…"), câu khối bị bỏ theo chủ đề
  ("…cũng vậy (…)"), đoạn trình phát theo chủ đề, đoạn bố cục ngang theo chủ đề.
- › Tùy chọn trong app: ô danh sách chủ đề của `aod_theme_id`, ô cấu hình chủ đề của `aod_theme_configs`.
- › Cấu trúc mã nguồn: dòng `presentation/model/aod/theme/` và `presentation/components/aod/theme/` (tên thư mục + mô tả phần vẽ),
  dòng `res/font/` (số file, dung lượng) nếu thêm font.
- › Thêm chủ đề mới: câu mở đầu (tên + `<Chủ đề>Metrics`), danh sách `hasNotificationContent = true`, danh sách đặt màn dọc
  (bước 1), ví dụ font ở bước 6 nếu có font mới / cách làm mới.
- › Kiểm tra trên máy thật: bước `25<chữ kế tiếp>` (sau bước chủ đề cuối, trước `25f`), bước 27 màn ngang thêm một câu.

`CHANGELOG.md`: mục mới trên cùng `## [Chưa phát hành] – <yyyy-mm-dd> – Chủ đề <Tên>`, theo mẫu mục chủ đề trước (kết quả kiểm,
Thêm, Thay đổi: "Chỉ thêm, không đổi chủ đề đang có: …", font tải về).

`plans/muviz-edge-themes.md`:
- › Đối chiếu với chủ đề đã có: dòng `| <Tên> (`<ENUM>`) | **<số> · <Mon><YY> "<tên app mẫu>"** | làm từ danh mục này (<ngày>), số đo lấy từ … |`.
- › Danh mục đủ 44 chủ đề: cột cuối của dòng chủ đề thành `**<Tên>** (`<ENUM>`)`.
- › Đề xuất làm tiếp: gạch đầu dòng của chủ đề thành `~~**<số> · <tên>**~~ (…, đã làm thành <Tên> <ngày>): <tóm tắt số đo>`.
