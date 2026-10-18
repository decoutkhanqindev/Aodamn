# Chủ đề AOD của Muviz Edge — danh mục và đối chiếu

Phân tích tĩnh app mẫu `com.sparkine.muvizedge` 2.1.5.0 (versionCode 142, cài từ Play trên Redmi Note 13 Pro 5G) bằng REA
(`rea pull-adb-package`, `inspect-android-package`, `search-android-classes`, `inspect-android-class`, `inspect-android-method`)
và engine JADX 0.7.1 đi kèm REA. Chỉ phần AOD, chưa xét Edge Lights. Ngày 2026-10-17.

Ranh giới: tài liệu này chỉ ghi tên, tuỳ chọn, giá trị mặc định và cấu trúc để tự vẽ lại theo kiến trúc của mình. Không chép
code dịch ngược, không lấy asset (font, ảnh, Lottie) từ APK. APK và mã dịch ngược chỉ nằm ở scratchpad, không đưa vào repo.

## Cách app mẫu mô tả một chủ đề

- Mỗi chủ đề là một class `fragment.aodscreen.<Tháng><Năm>Screen` (tháng ra mắt), số thứ tự 1–44 trong bảng tra
  trung tâm. `DefaultScreen` (số 0) chỉ là chỗ giữ chỗ, không có tuỳ chọn.
- Mỗi chủ đề khai báo 3 thứ: tên, giá trị mặc định theo mã thuộc tính, danh sách thuộc tính được chỉnh (giữ thứ tự).
- Loại control → kiểu tuỳ chọn của mình:

  | App mẫu | Ý nghĩa | Bên mình |
  |---|---|---|
  | 1 | thanh trượt min–max (20 nấc) | `AodRangeOption` |
  | 2 | chọn một (chip "Style 1…N") | `AodChoiceOption` |
  | 3 | chọn nhiều (bitmask) | chưa có (tách thành `AodToggleOption`) |
  | 4 | bảng màu (đơn sắc hoặc gradient) | `AodColorOption` |
  | 5 | mốc nhóm "Advanced": các thuộc tính sau nó là tab phụ | tab chia theo phần (CLAUDE.md) |
  | 6 | chọn ký hiệu / emoji | chưa có |
  | 7 | ô nhập chữ | chưa có |
  | 8 | chọn múi giờ | chưa có |

- Chủ đề Premium (gói mua `aod_freedom_pack`): 20, 21, 25, 30, 40, 43 (⭐ trong bảng).
- Thứ tự trong lưới chủ đề không cố định trong code: `AodFragment` đọc bảng SQLite `aod_screen_data_tbl` theo `ROWID DESC`
  (mục thêm sau hiện trước), nên đánh số theo mã chủ đề.
- App mẫu không có bản dịch tiếng Việt cho nhãn tuỳ chọn; nhãn dưới đây là chuỗi tiếng Anh của app.

## Đối chiếu với chủ đề đã có (tránh làm trùng)

| Chủ đề của mình | Chủ đề app mẫu | Bằng chứng |
|---|---|---|
| Cổ điển (`CLASSIC`) | không clone — chủ đề riêng (4 mặt số / kim có từ trước khi có chủ đề) | `ClassicThemeSpec` |
| Sỏi (`PEBBLE`) | **44 · Dec24 "M3 Shapes Clock"** | tab "Hour Style, Minute Style, Clock Style, Color 1" (`reference/theme_1/03.jpg`); layout hai ô hình chứa giờ / phút + ngày + nhạc |
| Số áo (`JERSEY`) | **37 · May24 "Fill HyperOS"** | tab "Clock Size, Hour Color, Minute Color, Date Color" (`reference/theme_2/02.jpg`); mặc định giờ `#BDB7FF` = `Lavender` của mình |
| Viên nang (`CAPSULE`) | **32 · Dec23 "Flex Round"** | tab "Clock Style, Color 1, Color 2, Clock Size" + "Style 1 / Style 2" (ảnh thứ hai trong `reference/theme_3/`); mặc định xám `#CFD8DC` / `#E4E4E4` |
| Thẻ (`CARD`) | **38 · Jun24 "M3 Expressive"** | làm từ danh mục này (2026-10-17), số đo lấy từ layout / code / font trong APK |
| Tháp (`TOWER`) | **34 · Feb24 "Long HyperOS"** | làm từ danh mục này (2026-10-17), số đo lấy từ layout `feb24_screen_layout` / code / font trong APK |
| Chuỗi (`CHAIN`) | **28 · Aug23 "Bullet List"** | làm từ danh mục này (2026-10-17), số đo lấy từ layout `aug23_screen_layout` / `Aug23Clock` / font trong APK |
| Vành (`BEZEL`) | **40 · Aug24 "iOS Fill Clock"** | làm từ danh mục này (2026-10-18), số đo lấy từ layout `aug24_screen_layout` / `IosFillClock` / font trong APK |

## Danh mục đủ 44 chủ đề

Cột "Advanced" là tab phụ (đa số là màu của từng phần). "Vẽ bằng" là view tự vẽ của app mẫu (bỏ thanh tiến trình nhạc và
text view chung) hoặc font dùng trong code; "chữ (font layout)" nghĩa là chủ đề dựng bằng chữ, font khai báo trong layout.

| # | Class | Tên | Tuỳ chọn chính | Advanced | Vẽ bằng | Cần thêm | Bên mình |
|---|---|---|---|---|---|---|---|
| 1 | May21 | Gradient | Clock Weight, Clock Size, Theme Color | Hour, Minute, Date, Notification, Controls, Battery Status | chữ: lato | – | chưa có |
| 2 | Jun21 | Default | Symbol, Clock Weight, Symbol Size, Clock Size, Theme Color | Hour, Minute, Date, Notification, Controls, Battery Status | chữ: rubik | chọn ký hiệu | chưa có |
| 3 | Jul21 | Nike | Clock Size, Hour Color, Minute Color, Second Color, Clock Background Color, Theme Color | Date, Notification, Controls, Battery Status | AnalogueClock1, PieClock | – | chưa có |
| 4 | Aug21 | Android 12 | Clock Style, Clock Size, Hour Color, Minute Color, Second Color, Date Color, Theme Color | Clock Background, Controls, Controls Background, Notification, Battery Status | Android12Clock2, Android12ClockBg2 | – | chưa có |
| 5 | Sep21 | Text Clock | Clock Weight, Clock Size, Theme Color | Hour, Minute, Date, Notification, Controls, Battery Status | chữ: lato | – | chưa có |
| 6 | Oct21 | Visualizer | Clock Size, Viz Height, Viz Spread, Viz Speed, Theme Color | Viz 1, Viz 2, Viz 3, Hour, Minute, Date, Notification, Controls, Battery Status | chữ (font layout) | quyền RECORD_AUDIO | chưa có |
| 7 | Nov21 | Retro | Clock Size, Theme Color | Clock, Date, Notification, Controls | chữ (font layout) | – | chưa có |
| 8 | Dec21 | Blinky | Character Size, Clock Size, Theme Color | Character, Clock | BlinkyCharacter | – | chưa có |
| 9 | Jan22 | Flip Clock | Clock Style, Clock Size, Character Size, Clock Color, Character Color | – | FlipClock | – | chưa có |
| 10 | Feb22 | Box Clock | Clock Size, Theme Color | Clock, Date, Notification, Controls | chữ (font layout) | – | chưa có |
| 11 | Mar22 | Eclipse | Clock Color, Shadow Color, Rotation, Peek, Character Color, Controls Color | – | Eclipse | – | chưa có |
| 12 | Apr22 | Vertical Clock | Clock Size, Theme Color | Clock, Date, Notification, Controls | VerticalTextView | – | chưa có |
| 13 | May22 | Paper Clip | Clock Size, Second Color, Theme Color | Hour, Minute, Controls | PaperClipClock | – | chưa có |
| 14 | Jun22 | Solar System | Clock Size, Digital Clock, Hour Color, Minute Color, Second Color, Clock Color, Controls Color | – | SolarSystemClock | – | chưa có |
| 15 | Jul22 | Samsung Like | Clock Weight, Clock Size, Hour Color, Minute Color, Notification Color, Controls Color | – | chữ: nunito | – | chưa có |
| 16 | Aug22 | Quotes | Text, Text Size, Text Style, Text Color, Text Align, Notification Color, Controls Color | – | chữ: 12 font (arvo, bangers, caveat, pacifico…) | ô nhập chữ | chưa có |
| 17 | Sep22 | Nothing(1) | Clock Size, Theme Color | Hour, Minute, Second, Date, Notification, Controls, Battery Status | chữ: ndot47, ndot55 | font độc quyền | chưa có |
| 18 | Oct22 | Pixel Default | Clock Weight, Clock Size, Theme Color | Hour, Minute, Date, Notification, Controls, Battery Status | chữ: google_sans_clock | font độc quyền | chưa có |
| 19 | Nov22 | Concentric | Clock Size, Hour Color, Minute Color, Ring Color, Dash Color, Controls Color | – | ConcentricClock | – | chưa có |
| 20 | Dec22 | Half Concentric ⭐ | Hour Color, Minute Color, Ring Color, Dash Color, Date Color, Controls Color | – | HalfConcentricClock | – | chưa có |
| 21 | Jan23 | Moon Phase ⭐ | Clock Color, Date Color, Clock Size, Character Color, Character Size, Peek, Rotation, Glow Color, Particle Color, Controls Color | – | MoonSpace, StarField | – | chưa có |
| 22 | Feb23 | Vertical Text | Text, Spacing, Text Size, Text Style, Text Color, Notification Color, Controls Color | – | chữ: 12 font (abril, bebas, monoton, rye…) | ô nhập chữ | chưa có |
| 23 | Mar23 | Big Overlap Clock | Clock Style, Clock Size, Color 1, Color 2, Theme Color | Color 3, Color 4, Date, Notification, Controls, Battery Status | BigPixel2Clock | – | chưa có |
| 24 | Apr23 | Android 14 Analog Clock | Text Style, Clock Style, Clock Size, Color 1, Color 2, Theme Color | Color 3, Date, Notification, Controls, Battery Status | PilotBold2Tick, PixelAnalogClock | – | chưa có |
| 25 | May23 | Ambitio Face ⭐ | Clock Style, Clock Size, Color 1, Color 2, Controls Color | – | AmbitioClock | – | chưa có |
| 26 | Jun23 | iOS Lockscreen | Clock Style, Clock Size, Clock Color, Theme Color | Date, Notification, Controls, Battery Status | chữ: 5 font iOS | font độc quyền | chưa có |
| 27 | Jul23 | Nothing(2) | Clock Size, Theme Color | Clock, Date, Notification, Controls, Battery Status | chữ: ndot47 | font độc quyền | chưa có |
| 28 | Aug23 | Bullet List | Clock Size, Theme Color | Clock, Date, Battery Status, Notification, Controls | Aug23Clock | – | **Chuỗi** (`CHAIN`) |
| 29 | Sep23 | Gradient Line Clock | Clock Size, Dash Color, Theme Color | Position, Clock, Battery Status, Notification, Controls | Sep23Clock | – | chưa có |
| 30 | Oct23 | Coum Face Clock ⭐ | Clock Size, Ring Color, Clock Color, Controls Color | – | CoumTick | – | chưa có |
| 31 | Nov23 | Space Mono | Clock Size, Hour Color, Minute Color, Theme Color | Date, Notification, Controls | chữ (font layout) | – | chưa có |
| 32 | Dec23 | Flex Round | Clock Style, Color 1, Color 2, Clock Size | – | chữ (font layout) | – | **Viên nang** (`CAPSULE`) |
| 33 | Jan24 | Dual Timezone | Time Zone, Clock Style, Color 1, Color 2 | – | SimpleAnalogClock | chọn múi giờ | chưa có |
| 34 | Feb24 | Long HyperOS | Clock Style, Clock Size, Hour Color, Minute Color, Date Color, Controls Color | – | chữ (font layout) | – | **Tháp** (`TOWER`) |
| 35 | Mar24 | Text HyperOS | Clock Size, Theme Color | Hour, Minute, Day, Date, Controls | chữ (font layout) | – | chưa có |
| 36 | Apr24 | Big Retro | Clock Style, Clock Size, Clock Color, Theme Color | Date, Controls | chữ (font layout) | – | chưa có |
| 37 | May24 | Fill HyperOS | Clock Size, Hour Color, Minute Color, Date Color, Controls Color | – | chữ (font layout) | – | **Số áo** (`JERSEY`) |
| 38 | Jun24 | M3 Expressive | Clock Style, Background, Color 1, Color 2, Clock Size | – | chữ (font layout) | – | **Thẻ** (`CARD`) |
| 39 | Jul24 | Nothing Wide | Clock Style, Color 1, Color 2, Date Color, Controls Color | – | NothingWideClock | – | chưa có |
| 40 | Aug24 | iOS Fill Clock ⭐ | Clock Style, Color 1, Color 2 | – | IosFillClock | – | **Vành** (`BEZEL`) |
| 41 | Sep24 | Pixel Width Clock | Variation, Clock Weight, Clock Size, Hour Color, Minute Color, Theme Color | Date, Notification, Controls, Battery Status | chữ: gsans_flex_width | font độc quyền | chưa có |
| 42 | Oct24 | Pixel Growth Rotation | Clock Style, Style, Variation, Clock Size, Hour Color, Minute Color, Theme Color | Date, Notification, Controls, Battery Status | chữ: growth, rotation | – | chưa có |
| 43 | Nov24 | MIUI Doodle ⭐ | Color 1, Color 2 | Date, Controls, Notification, Clock Size | chữ (font layout) | – | chưa có |
| 44 | Dec24 | M3 Shapes Clock | Hour Style, Minute Style, Clock Style, Color 1, Color 2, Clock Size | – | chữ (font layout) | – | **Sỏi** (`PEBBLE`) |

Font: không lấy file từ APK. Font của app mẫu có trên Google Fonts (Anton, Rubik, Lato, Bebas...) thì tải bản Google Fonts (OFL)
về dùng (README › Thêm chủ đề mới bước 6), kiểm có đủ tiếng Việt trước; font có tên dành riêng trong OFL (Zalando) thì lấy bản tĩnh
gốc của nhà làm font, không tự tách / cắt. Google Sans (và Google Sans Flex, Product Sans cùng thiết kế) nay có trên Google Fonts.
"Font độc quyền" (Apple iOS, Nothing NDot) không có trên Google Fonts, và font thiếu tiếng Việt (Rubik), thì chọn font mở gần
nhất trong app (`anton`, `big_shoulders_display`, `comfortaa`, `dancing_script`, `fraunces`, `google_sans`, `jetbrains_mono`,
`josefin_sans`, `plus_jakarta_sans`, `zalando_sans_expanded`).

## Ô trong ảnh lưới đã có → chủ đề

Ghép theo tên, hình và tuỳ chọn; "chắc" là đã khớp bằng ảnh trình chỉnh, còn lại là đoán.

- `reference/theme_1/01.jpg` (đầu lưới): "11 18" hai ô hình → 44 (chắc) · "09 05" viền cao + viên thuốc thứ → 37 (gần
  chắc) · "08:36 AUGUST" ⭐ kiểu nhãn dán → 43 · "12 45" → 42 · "83 00" số tròn chồng nhau → 41 hoặc 23 · "05 36 WEDNESDAY
  30/4" → 34 · "09 35 PM" số giờ trong ô nền → 38 · kim, số 12/3/6/9 xanh vàng ⭐ → 40 (mặc định `#E8FF82`) · mặt bầu dục
  vạch toả → 39.
- Ảnh thứ nhất trong `reference/theme_3/`: "09:08" chữ điểm → 36 · mặt quay "02 (40)" ⭐ → 30 · "Twelve Twenty Seven" (giờ bằng chữ) → 5 hoặc 35, chưa rõ ·
  "05:00" chữ chấm → 27 · vòng nhật thực → 11 · hai đồng hồ LOCAL / MONTERREY → 33 · viên nang "08:26" → 32 (chắc) · "7:38"
  chữ có chân + thẻ nhạc → 26 · "01.36PM" mặt trăng ⭐ → 21 · quỹ đạo "10:26" → 14 · "01:45" + danh sách gạch đầu dòng → 28 ·
  số 12/3/6/9 lớn quanh kim → 24.

## Đề xuất làm tiếp

Ưu tiên chủ đề khác hẳn 4 chủ đề đã có, vừa với 4 kiểu tuỳ chọn hiện có, không cần quyền mới hay font độc quyền:

1. ~~**38 · M3 Expressive**~~: đã làm thành chủ đề Thẻ (2026-10-17).
2. ~~**34 · Long HyperOS**~~: đã làm thành chủ đề Tháp (2026-10-17).
3. ~~**28 · Bullet List**~~: đã làm thành chủ đề Chuỗi (2026-10-17).
4. ~~**40 · iOS Fill Clock**~~ ⭐: đã làm thành chủ đề Vành (2026-10-18).

Để sau: 33 Dual Timezone (cần control chọn múi giờ), 16 / 22 (cần ô nhập chữ), chủ đề giờ bằng chữ (ô "Twelve Twenty
Seven", phải đổi số thành chữ cả vi + en), 6 Visualizer (cần quyền `RECORD_AUDIO`, phải hỏi trước), 8 / 9 / 11 / 14 / 21 (hiệu ứng, hạt, lật số: công sức
lớn), 17 / 26 / 27 (cần chọn font mở thay font độc quyền). 18 / 41 dùng Google Sans: font này nay có trên Google Fonts (OFL, Vành
dùng bản Bold), Google Sans Flex cũng có, nên không còn vướng font.

Không cần chụp ảnh chủ đề từ app mẫu: số đo lấy từ layout XML, code của chủ đề và thông số font trong APK
(`tools/reference/README.md` bước 6); ảnh lưới ở trên chỉ để đối chiếu.

## Câu hỏi đã trả lời / còn mở

| Câu hỏi | Trạng thái |
|---|---|
| Có những chủ đề AOD nào (mã, class, tên) | Đã trả lời: 44 chủ đề + `DefaultScreen` giữ chỗ |
| Tuỳ chọn và mặc định từng chủ đề | Đã trả lời; Visualizer đọc tay (thân hàm khởi tạo lười), Dual Timezone mặc định theo múi giờ máy |
| Chủ đề Premium | Đã trả lời (suy ra từ kiểm tra gói mua đi cùng danh sách) |
| Thứ tự lưới | Một phần: lấy từ cơ sở dữ liệu lúc chạy, không đọc được bằng phân tích tĩnh |
| Trùng với chủ đề đã có | Đã trả lời: 3 chủ đề clone khớp chắc; Cổ điển là chủ đề riêng |
| Hình dáng chi tiết các chủ đề chưa làm | Còn mở: chỉ có ảnh lưới thu nhỏ cho khoảng 20 chủ đề; hiệu ứng động chưa quan sát |

## Bằng chứng

- APK: `base.apk` SHA-256 `9474fb9b2249f771be1477931d777d8af360b4d05ec71126d3e0e3c9c8816f0c`, REA
  `inspect-android-package` Evidence `ev_19949d5596ecda6e416ffbaae40a48dcf541875a6d0f1e6cbc30d275c515fe7b`.
- Danh sách class của app (110, coverage complete): Evidence
  `ev_e9bd24c01142709fe296b537906a4c9677c8871cbb6822fa6507aec79dd7cd85`.
- Mẫu khai báo một chủ đề (`Aug24Screen` tên / mặc định / tuỳ chọn): Evidence
  `ev_2125e17038c4872e371554a180d7482872f14ffc7d821208b930252e3d05b16f`,
  `ev_c12a6dc1e8198ad5f86be3f989bf129e1237b3e53f660af405c8d6e7ef62d27c`,
  `ev_372d0009f99fa87d8e64c27a3b0ed104e1bb0fda88787aab9f5d3a40b5e00c45`.
- Cả 44 chủ đề: xuất toàn bộ mã nguồn bằng cùng engine JADX 0.7.1 (jar SHA-256 `6e5eacf5…ff75`) rồi đọc bằng script; bước
  này không có Evidence ID của REA. Nhãn tuỳ chọn: `aapt2 dump resources` (build-tools 36.0.0); layout `dec24`, `feb24`,
  `aug24`: `aapt2 dump xmltree`.
- Phân tích tĩnh, không chạy app mẫu; hành vi lúc chạy (hiệu ứng, thứ tự lưới) chưa quan sát.
- Làm lại / cập nhật khi app mẫu ra bản mới: `python -I -X utf8 tools/reference/pull_decompile.py com.sparkine.muvizedge
  --out <thư mục mới>` rồi `python -I -X utf8 tools/reference/extract_muviz.py <thư mục đó>` (xem `tools/reference/README.md`).
