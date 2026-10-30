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
  | La bàn (`COMPASS`) | **25 · May23 "Ambitio Face"** | làm từ danh mục này (2026-10-18), số đo lấy từ layout `may23_screen_layout` / `AmbitioClock` / font trong APK |
| Bán nguyệt (`HALF_MOON`) | **20 · Dec22 "Half Concentric"** | làm từ danh mục này (2026-10-20), số đo lấy từ layout `dec22_screen_layout` dọc / ngang / `HalfConcentricClock` |
| Dải màu (`GRADIENT_LINE`) | **29 · Sep23 "Gradient Line Clock"** | làm từ danh mục này (2026-10-20), số đo lấy từ layout `sep23_screen_layout` (chỉ có bản dọc) / `Sep23Clock` |
| Gợn sóng (`RIPPLE`) | **4 · Aug21 "Android 12"** | làm từ danh mục này (2026-10-21), số đo lấy từ layout `aug21_screen_layout` (chỉ có bản dọc) / `Android12Clock2` / `Android12ClockBg2` |
| Nhịp giây (`TICK`) | **30 · Oct23 "Coum Face Clock"** | làm từ danh mục này (2026-10-26), số đo lấy từ layout `oct23_screen_layout` (chỉ có bản dọc) / `CoumTick` |
| Chồng số (`BIG_OVERLAP`) | **23 · Mar23 "Big Overlap Clock"** | làm từ danh mục này (2026-10-26), số đo lấy từ layout `mar23_screen_layout` dọc / ngang / `BigPixel2Clock` |
| Bốn số (`QUARTERS`) | **24 · Apr23 "Android 14 Analog Clock"** | làm từ danh mục này (2026-10-26), số đo lấy từ layout `apr23_screen_layout` dọc / ngang / `PixelAnalogClock` / `PilotBold2Tick` |
| Rẻ quạt (`SECTOR`) | **3 · Jul21 "Nike"** | làm từ danh mục này (2026-10-27), số đo lấy từ layout `jul21_screen_layout` / `AnalogueClock1` / `PieClock` |
| Gáy sách (`SPINE`) | **12 · Apr22 "Vertical Clock"** | làm từ danh mục này (2026-10-27), số đo lấy từ layout `apr22_screen_layout` dọc / ngang / `VerticalTextView` |
| Kẹp giấy (`CLIP`) | **13 · May22 "Paper Clip"** | làm từ danh mục này (2026-10-28), số đo lấy từ layout `may22_screen_layout` dọc / ngang / `PaperClipClock` |
| Phai (`FADE`) | **1 · May21 "Gradient"** | làm từ danh mục này (2026-10-28), số đo lấy từ layout `may21_screen_layout` (chỉ có bản dọc) / `May21Screen` / font Lato 1.104 trong APK |
| Quỹ đạo (`ORBIT`) | **14 · Jun22 "Solar System"** | làm từ danh mục này (2026-10-30), số đo lấy từ layout `jun22_screen_layout` dọc / ngang / `SolarSystemClock` / `Jun22Screen` |
| Đơn cách (`MONO`) | **31 · Nov23 "Space Mono"** | làm từ danh mục này (2026-10-29), số đo lấy từ layout `nov23_screen_layout` dọc / ngang / `Nov23Screen` / `NoPadTextView` / font Space Mono trong APK |
| Khung (`FRAME`) | **10 · Feb22 "Box Clock"** | làm từ danh mục này (2026-10-29), số đo lấy từ layout `feb22_screen_layout` dọc / ngang / `Feb22Screen` / font Josefin Sans trong APK |
| Lời (`WORDS`) | **5 · Sep21 "Text Clock"** | làm từ danh mục này (2026-10-29), số đo lấy từ layout `sep21_screen_layout` dọc / ngang / `Sep21Screen` / font Lato 1.104 trong APK |

## Danh mục đủ 44 chủ đề

Cột "Advanced" là tab phụ (đa số là màu của từng phần). "Vẽ bằng" là view tự vẽ của app mẫu (bỏ thanh tiến trình nhạc và
text view chung) hoặc font dùng trong code; "chữ (font layout)" nghĩa là chủ đề dựng bằng chữ, font khai báo trong layout.

| # | Class | Tên | Tuỳ chọn chính | Advanced | Vẽ bằng | Cần thêm | Bên mình |
|---|---|---|---|---|---|---|---|
| 1 | May21 | Gradient | Clock Weight, Clock Size, Theme Color | Hour, Minute, Date, Notification, Controls, Battery Status | chữ: lato | – | **Phai** (`FADE`) |
| 2 | Jun21 | Default | Symbol, Clock Weight, Symbol Size, Clock Size, Theme Color | Hour, Minute, Date, Notification, Controls, Battery Status | chữ: rubik | chọn ký hiệu | bỏ (trùng Cổ điển, user chốt) |
| 3 | Jul21 | Nike | Clock Size, Hour Color, Minute Color, Second Color, Clock Background Color, Theme Color | Date, Notification, Controls, Battery Status | AnalogueClock1, PieClock | – | **Rẻ quạt** (`SECTOR`) |
| 4 | Aug21 | Android 12 | Clock Style, Clock Size, Hour Color, Minute Color, Second Color, Date Color, Theme Color | Clock Background, Controls, Controls Background, Notification, Battery Status | Android12Clock2, Android12ClockBg2 | – | **Gợn sóng** (`RIPPLE`) |
| 5 | Sep21 | Text Clock | Clock Weight, Clock Size, Theme Color | Hour, Minute, Date, Notification, Controls, Battery Status | chữ: lato | – | **Lời** (`WORDS`) |
| 6 | Oct21 | Visualizer | Clock Size, Viz Height, Viz Spread, Viz Speed, Theme Color | Viz 1, Viz 2, Viz 3, Hour, Minute, Date, Notification, Controls, Battery Status | chữ (font layout) | quyền RECORD_AUDIO | chưa có |
| 7 | Nov21 | Retro | Clock Size, Theme Color | Clock, Date, Notification, Controls | chữ (font layout) | – | chưa có |
| 8 | Dec21 | Blinky | Character Size, Clock Size, Theme Color | Character, Clock | BlinkyCharacter | – | chưa có |
| 9 | Jan22 | Flip Clock | Clock Style, Clock Size, Character Size, Clock Color, Character Color | – | FlipClock | – | chưa có |
| 10 | Feb22 | Box Clock | Clock Size, Theme Color | Clock, Date, Notification, Controls | chữ (font layout) | – | **Khung** (`FRAME`) |
| 11 | Mar22 | Eclipse | Clock Color, Shadow Color, Rotation, Peek, Character Color, Controls Color | – | Eclipse | – | chưa có |
| 12 | Apr22 | Vertical Clock | Clock Size, Theme Color | Clock, Date, Notification, Controls | VerticalTextView | – | **Gáy sách** (`SPINE`) |
| 13 | May22 | Paper Clip | Clock Size, Second Color, Theme Color | Hour, Minute, Controls | PaperClipClock | – | **Kẹp giấy** (`CLIP`) |
| 14 | Jun22 | Solar System | Clock Size, Digital Clock, Hour Color, Minute Color, Second Color, Clock Color, Controls Color | – | SolarSystemClock | – | **Quỹ đạo** (`ORBIT`) |
| 15 | Jul22 | Samsung Like | Clock Weight, Clock Size, Hour Color, Minute Color, Notification Color, Controls Color | – | chữ: nunito | – | chưa có |
| 16 | Aug22 | Quotes | Text, Text Size, Text Style, Text Color, Text Align, Notification Color, Controls Color | – | chữ: 12 font (arvo, bangers, caveat, pacifico…) | ô nhập chữ | chưa có |
| 17 | Sep22 | Nothing(1) | Clock Size, Theme Color | Hour, Minute, Second, Date, Notification, Controls, Battery Status | chữ: ndot47, ndot55 | font độc quyền | chưa có |
| 18 | Oct22 | Pixel Default | Clock Weight, Clock Size, Theme Color | Hour, Minute, Date, Notification, Controls, Battery Status | chữ: google_sans_clock | font độc quyền | chưa có |
| 19 | Nov22 | Concentric | Clock Size, Hour Color, Minute Color, Ring Color, Dash Color, Controls Color | – | ConcentricClock | – | chưa có |
| 20 | Dec22 | Half Concentric ⭐ | Hour Color, Minute Color, Ring Color, Dash Color, Date Color, Controls Color | – | HalfConcentricClock | – | **Bán nguyệt** (`HALF_MOON`) |
| 21 | Jan23 | Moon Phase ⭐ | Clock Color, Date Color, Clock Size, Character Color, Character Size, Peek, Rotation, Glow Color, Particle Color, Controls Color | – | MoonSpace, StarField | – | chưa có |
| 22 | Feb23 | Vertical Text | Text, Spacing, Text Size, Text Style, Text Color, Notification Color, Controls Color | – | chữ: 12 font (abril, bebas, monoton, rye…) | ô nhập chữ | chưa có |
| 23 | Mar23 | Big Overlap Clock | Clock Style, Clock Size, Color 1, Color 2, Theme Color | Color 3, Color 4, Date, Notification, Controls, Battery Status | BigPixel2Clock | – | **Chồng số** (`BIG_OVERLAP`) |
| 24 | Apr23 | Android 14 Analog Clock | Text Style, Clock Style, Clock Size, Color 1, Color 2, Theme Color | Color 3, Date, Notification, Controls, Battery Status | PilotBold2Tick, PixelAnalogClock | – | **Bốn số** (`QUARTERS`) |
| 25 | May23 | Ambitio Face ⭐ | Clock Style, Clock Size, Color 1, Color 2, Controls Color | – | AmbitioClock | – | **La bàn** (`COMPASS`) |
| 26 | Jun23 | iOS Lockscreen | Clock Style, Clock Size, Clock Color, Theme Color | Date, Notification, Controls, Battery Status | chữ: 5 font iOS | font độc quyền | chưa có |
| 27 | Jul23 | Nothing(2) | Clock Size, Theme Color | Clock, Date, Notification, Controls, Battery Status | chữ: ndot47 | font độc quyền | chưa có |
| 28 | Aug23 | Bullet List | Clock Size, Theme Color | Clock, Date, Battery Status, Notification, Controls | Aug23Clock | – | **Chuỗi** (`CHAIN`) |
| 29 | Sep23 | Gradient Line Clock | Clock Size, Dash Color, Theme Color | Position, Clock, Battery Status, Notification, Controls | Sep23Clock | – | **Dải màu** (`GRADIENT_LINE`) |
| 30 | Oct23 | Coum Face Clock ⭐ | Clock Size, Ring Color, Clock Color, Controls Color | – | CoumTick | – | **Nhịp giây** (`TICK`) |
| 31 | Nov23 | Space Mono | Clock Size, Hour Color, Minute Color, Theme Color | Date, Notification, Controls | chữ (font layout) | – | **Đơn cách** (`MONO`) |
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
`josefin_sans`, `krona_one`, `plus_jakarta_sans`, `zalando_sans_expanded`).

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

Ứng viên tiếp theo, đã đọc code vẽ + layout (2026-10-18; trước đó mô tả theo ảnh lưới, sai mấy chủ đề). AOD của mình chỉ cập nhật
mỗi phút (`AodViewModel`), nên phần chạy theo giây của app mẫu phải đổi hoặc bỏ, nêu trong mock:

- ~~**25 · Ambitio Face**~~ ⭐ (`AmbitioClock`, đã làm thành La bàn): ba vòng số đồng tâm cùng xoay về một vạch chỉ cố định chạy từ trái sang mép phải (đầu
  mũi tên bên trái): vòng ngoài giờ 1–12 lớn xen "hh:30" nhỏ, vòng giữa phút 00–55 xen vạch, vòng trong giây mờ; hai vòng tròn
  mảnh alpha 40. Font Krona One (Google Fonts, chỉ vẽ số). Đang làm thành chủ đề La bàn (2026-10-18): user muốn giữ vòng giây, cả ba vòng
  xoay theo giờ thật mỗi khung hình như app mẫu (mặt số tự chạy đồng hồ khung hình, không đổi nhịp phút của AOD).
- ~~**20 · Half Concentric**~~ ⭐ (`HalfConcentricClock`, đã làm thành Bán nguyệt 2026-10-20): mặt số vuông 0,9 cạnh ngắn, tâm lùi ra ngoài
  mép trái 0,95r (chỉ thấy nửa phải), vòng ngoài là **giây** 00–55, vòng trong là phút, cùng xoay theo giờ thật mỗi khung hình để giá trị
  hiện tại chỉ sang phải, số đứng thẳng; giờ "HH" lớn (0,36r), phút "mm" trong viên viền ở 0,6r, vạch khoét quanh viên; phủ tối tròn làm
  đỉnh, đáy, mép trái chìm vào nền; chế độ tiết kiệm (`LOW_POWER_AFTER`) thu viên về và làm mờ số giây. Font Product Sans (= Google Sans).
  Giữ vòng giây chạy theo giờ thật như La bàn; bỏ trạng thái nghỉ (mặc định sau 15 giây ở app mẫu) để vòng giây luôn hiện (user chọn).
- ~~**29 · Gradient Line Clock**~~ (`Sep23Clock`, đã làm thành Dải màu 2026-10-20): vạch dọc giữa màn tô gradient (mặc định #4158D0 → #C850C0
  → #FFCC70 từ dưới lên), giờ "HH:mm" Product Sans 0,11 cạnh ngắn × cỡ nằm trên vạch, trượt theo phút trong ngày (Position −1) hoặc ghim
  0–100%, ngày "EEEE, dd MMM" mờ 200 / 255 dưới giờ, vạch khoét quanh giờ + ngày; trình phát trên vạch, thông báo + chữ pin dưới. Một layout
  cho cả màn ngang (vạch ngắn, giờ nhỏ) nên bên mình cho vạch nằm ngang. Bảng màu AOD của app mẫu có 211 màu đơn + 13 bộ gradient 3 màu kèm
  hướng (`ab/a0`, một bộ trùng) và màn tự tạo gradient (`AddColorActivity`: 3 ô màu, vòng màu, mã hex, 6 hướng): bên mình thành bộ chọn
  Một màu / Gradient dùng chung (12 bộ có sẵn, 4 hướng + đảo chiều, mã hex).
- ~~**4 · Android 12**~~ (`Android12Clock2` + `Android12ClockBg2`, đã làm thành Gợn sóng 2026-10-21): đĩa 12 gợn (đặc / viền, nền `#121218`),
  ngày "E  dd" Rubik Medium uốn theo nửa trên đĩa, kim giờ / phút dày đầu tròn, chấm giây chạy vòng; hàng icon / thông báo mới (icon trong
  vòng tròn màu) ở trên, thẻ nhạc bo tròn có ảnh bìa (vuốt để chuyển bài) + nút phát tròn và chữ pin ở dưới; trạng thái tiết kiệm đổi đĩa sang
  viền, ẩn chấm giây. Một layout cho cả màn ngang (mặt số còn rất nhỏ) nên bên mình tự dựng màn ngang.
- ~~**30 · Coum Face**~~ ⭐ (`CoumTick` + `oct23_screen_layout`, đã làm thành Nhịp giây 2026-10-26): giờ "HH:mm" Product Sans 72sp × cỡ
  (55–90, mặc định 70) + ngày "EEE, dd MMM" in hoa 23sp × cỡ ở giữa, vòng 60 vạch 0,8–0,9 nửa bề ngang trôi theo chiều kim 6°/giây (gradient
  dọc đứng yên theo màn, mặc định #4158D0 → #C850C0 → #FFCC70 từ dưới lên), hai vùng tròn đen mờ dần làm vòng chìm trên / dưới, số giây trong
  chấm tròn ở mép phải (nền đen, viền + số theo màu đồng hồ); dòng pin · icon thông báo cách đáy 60dp, thông báo mới và trình phát (lùi · tên
  bài / nghệ sĩ · tới) thay chỗ dòng đó theo hẹn giờ; trạng thái tiết kiệm dừng vòng, ẩn chấm giây. Một layout cho cả màn ngang (vòng tính theo
  bề ngang chỉ còn hai cung lớn sát mép) nên bên mình tự dựng màn ngang; bỏ trạng thái tiết kiệm, hiện cùng lúc thông báo / trình phát / pin
  (user chọn).
- **39 · Nothing Wide** (`NothingWideClock`): khung chữ nhật bo góc sát mép, 60 vạch là tia cắt trong dải (như Vành), kim có
  bóng, kim giây đỏ: gần như cùng cấu trúc với Vành, không làm tiếp ngay.
- ~~**23 · Big Overlap**~~ (`BigPixel2Clock`, đã làm thành Chồng số 2026-10-26): bốn chữ số Google Sans Flex cỡ 0,5 cạnh ngắn × cỡ lưới
  2 × 2 (giờ trên, phút dưới), cột trái căn trái / cột phải căn phải trong ô rộng bằng số 0, lấn qua tâm 0,17 bề rộng / 0,04 chiều cao số 0
  (viền 0,2 / 0,06); số sau khoét số trước (clip DIFFERENCE theo hình số), kiểu đặc nét đen 0,07 cỡ chữ quanh số, kiểu viền nét màu mảnh hơn
  2,5 lần; 4 kiểu đặc / viền × trục `ROND` 0 / 100, đổi đặc ↔ viền chạy 500ms; trạng thái tiết kiệm tự sang viền (bỏ, user duyệt). Màu
  Color 1–4 chéo (Color 3 theo 2, Color 4 theo 1). Font mẫu là bản cắt riêng đậm hơn cả wght 1000 của Google Fonts: dùng wght 1000, wdth 108;
  số 4 của bản Google Fonts là đường viền tự cắt nên gộp phần chồng trước khi vẽ nét. Layout dọc: ngày 20sp + icon thông báo căn trái trên,
  khối số giữa (0,08 / 0,8 / 0,12), trình phát (phát / tới, thanh 1,5dp) + pin 14sp dưới; layout ngang mẫu: khối số nhỏ ở cột trái.
- ~~**24 · Android 14 Analog Clock**~~ (`PixelAnalogClock` + `PilotBold2Tick`, đã làm thành Bốn số 2026-10-26): r = cỡ (50–90, mặc định 70) ×
  cạnh ngắn / 2; bốn số 12 / 3 / 6 / 9 Google Sans Flex (cùng file font với Big Overlap) cỡ 0,72r, giữa khung nét chữ tại 0,52r (12, 6) /
  0,6r (3, 9); Text Style 1–4 = đặc ROND 100 / đặc ROND 0 / viền ROND 100 / viền ROND 0 (nét 2dp; đọc từ bytecode vì JADX gộp sai nhánh);
  kim giờ viên 0,15r từ −0,085r tới 0,615r (Clock Style 2: nền #1A000000 + nét 0,0175r), kim phút 0,035r tới 0,86r, kim giây 0,013r tới 0,91r +
  chấm 0,0234r màu Color 1 tối 25%; bóng #80000000 bán kính 0,1r; kim chạy theo mili giây. Màu: Color 1 kim giờ #CFD8DC, Color 2 số (mặc
  định theo Color 1), Color 3 kim phút #FEF7EB. Layout dọc giống Big Overlap; ngang mẫu: mặt số × 1,2 ở cột trái. Tiết kiệm: ẩn kim giây,
  số và kim giờ sang viền (bỏ, user duyệt).
- ~~**3 · Nike**~~ (`AnalogueClock1` + `PieClock`, `jul21_screen_layout`, đọc 2026-10-26, đã làm thành Rẻ quạt 2026-10-27): kim phủ cả màn, r = cỡ (60–120, mặc định 100) ×
  nửa bề ngang; kim giờ / phút hai đoạn (cổ mảnh 7px tới 0,2r, thân dày từ 0,15r tới 0,45r / 0,75r), kim giây 4px từ −0,1r tới 0,75r màu
  #FF0A54, tâm chấm 18px màu kim giờ + lỗ đen 10px + vòng 5px màu giây, bóng #444444 (độ dày kim giờ đọc từ bytecode); màn vẽ lại mỗi giây nên
  kim nhảy từng giây. Nền là **hình quạt lớn hơn** giữa kim giờ và kim phút, bán kính bằng chiều cao màn nên phủ tới mép, màu Clock Background
  #121218, cả view mờ 0,8; dải đen 50% → 20% → 0 sau khối chữ trên / dưới; chủ đề tắt dịch điểm ảnh chống lưu ảnh. Trên: ngày "EEEE  dd" Lato 18sp,
  vạch ngăn + icon thông báo, thẻ thông báo mới (icon trong ô tròn màu, tiêu đề 16sp / nội dung 15sp); dưới (cách 40dp): trình phát (tên
  16sp / nghệ sĩ 15sp, phát + tới 22dp, thanh tiến trình) và dòng pin. Màu: giờ, phút trắng, giây #FF0A54, nền quạt #121218, Theme trắng
  (ngày, thông báo, nhạc tối 10%, pin tối 40%). Tiết kiệm: ẩn kim giây, quạt sang đen (bỏ, user duyệt). Không có layout ngang (bên mình tự dựng); tên mẫu là thương hiệu nên
  đặt tên khác.
- ~~**13 · Paper Clip**~~ (`PaperClipClock`, `may22_screen_layout` + bản ngang, đọc 2026-10-26, đọc lại đủ 2026-10-28, đã làm thành Kẹp giấy
  2026-10-28):
  r = cỡ (60–100, mặc định 80) / 110 × nửa cạnh ngắn; kim giờ / phút là viên **chỉ viền** (nét 0,021r, cao 0,14r, bo tròn hết, lùi sau tâm
  0,07r) tới 0,55r / 0,85r như kẹp giấy; vẽ chấm tâm 0,021r (màu Second #4AEDC6) trước, rồi kim phút, rồi kim giờ có bóng #444444 bán kính
  0,07r không lệch; không vạch, số, kim giây; `setTime` chỉ gọi khi đổi phút nên kim nhảy mỗi phút (kim giờ trôi theo phút). Cụm dưới cách
  đáy 60dp, cùng họ layout với `may23` (La bàn): ngày "dd MMMM yyyy" in hoa Poppins SemiBold 15sp giãn 0,25 em, cách 10dp dòng pin 15,5dp +
  "72%" Medium 14sp · chấm 11sp (cách 12dp) · icon thông báo 17dp (cách 10dp); thông báo mới (icon 17dp + một dòng in hoa 13sp giãn 0,1 em)
  thay hàng icon một lúc; trình phát (lùi / tới 40dp cách mép 40dp, tên in hoa SemiBold 13sp / nghệ sĩ Regular 11,5sp, giãn 0,25 em, không
  thanh tiến trình) thay cả cụm theo hẹn giờ. Màu: Theme trắng; Hour, Minute theo Theme; Controls tô ngày, tên bài, nút, còn nghệ sĩ, pin,
  thông báo tối 30%. Ngang: lề trên / dưới 30dp, đồng hồ vuông cạnh = chiều cao còn lại ở phần trên, cụm chữ dưới (chữ nhỏ hơn 1sp). Không có
  dạng tiết kiệm. Poppins (Google Fonts 4.004) thiếu dấu tiếng Việt (ạ, ầ, ơ, ư…), gần nhất trong app là Plus Jakarta Sans. Bên mình: kim
  lướt sang phút mới, kim phút + chấm tâm ngọc mặc định, mỗi phần một màu, hiện thông báo mới + trình phát cùng lúc, màn ngang kim giữa
  màn chữ hai bên, không ghi nhớ / sự kiện / nét vẽ (user duyệt).
- Đọc lướt 2026-10-28 (không đề xuất): **10 · Box Clock** "HH" / vạch 1dp / "mm" Josefin Sans 65sp (cỡ 50–100) trong khung chữ nhật viền 1dp
  góc vuông, màn chia 0,25 / 0,5 / 0,25; **31 · Space Mono** "HH" trên "mm" căn giữa 160sp × cỡ (60–100, mặc định 80), thêm một kiểu xếp
  chồng; **35 · Text HyperOS** "HH mm" / "EEEE" / "d/M" chữ rất to Neumatic Black, **36 · Big Retro** "HH:mm" MT Pixel Display, **42 · Pixel
  Growth Rotation** font Growth / Rotation của Pixel: cả ba font không có trên Google Fonts, đặc trưng nằm ở font.
- Đọc 2026-10-28 (bước 0 sau Kẹp giấy), đề xuất: ~~**1 · Gradient**~~ (`May21Screen`, `may21_screen_layout`, chỉ bản dọc; đã làm thành Phai 2026-10-28): "HH" trên "mm"
  Lato, mỗi số là ô chữ tự co giãn (tối đa 250sp) chiếm cỡ / 2 phần giữa màn (cỡ 30–100, mặc định 60), phút lấn lên 15dp; Clock Weight
  Thin (mặc định) / Light / Regular / Bold; khi Hour, Minute, Theme để mặc định thì mỗi số tô `LinearGradient` trong suốt → trắng chéo từ
  góc trên trái chữ số đầu tới đầu chữ số sau + 0,25 em (chữ số hàng chục phai vào nền), chọn màu bất kỳ là mất hiệu ứng; đồng hồ hiện mờ
  dần khi dựng lại gradient. Trên (lề 70 / 30dp): ngày "EEEE  dd" Lato 18sp · vạch dọc 0,5dp · icon thông báo 18dp, thẻ thông báo mới
  (icon trong ô tròn màu 18dp, tiêu đề 16sp / nội dung 15sp tối 40%) thay dòng đó; dưới (lề 40dp): trình phát (tên 16sp / nghệ sĩ 15sp
  tối 40%, phát · tới, thanh tiến trình) + chữ pin tối 40% (mờ 0,8 khi không sạc). Lato của Google Fonts thiếu tiếng Việt, app đã có Lato
  2.015 (latofonts) bản Regular. Đọc đủ 2026-10-28, đã làm thành Phai (`FADE`): khối trên / dưới bị ẩn (INVISIBLE) vẫn
  giữ chỗ nên đồng hồ không đổi cỡ (khối trên 179,8dp, khối dưới 188,5dp trên màn 873dp), chữ pin "72%" / "72%  ·  Charging" / "Charged",
  khối trên / dưới cùng số đo với `jul21` (Rẻ quạt); Lato Thin / Light / Bold 2.015 lấy ở google/fonts `ofl/lato` (bản Thin cùng hình chữ số
  với Hairline 1.104 của app mẫu). Bên mình: mặc định Đậm + gradient Trời pastel dọc, phai là công tắc chạy với mọi màu, chữ số đổi mờ chéo,
  hiện thông báo mới + trình phát cùng lúc, màn ngang số giữa màn chữ hai bên, không ghi nhớ / sự kiện / nét vẽ (user duyệt). **15 · Samsung Like** (`Jul22Screen`, `jul22_screen_layout` dọc + ngang): "HH" trên "mm" Nunito 90sp ×
  cỡ (70–130), phút lấn lên 20dp, Clock Weight ExtraLight / Regular (mặc định) / Bold; dưới số (lề 20 / 40dp) một dòng pin 15dp + "%" ·
  ngày "EEE dd" in hoa · icon 17dp (Nunito Medium 13sp giãn 0,05, chấm 10sp cách 12dp), thông báo mới (icon 17dp + một dòng in hoa 12,5sp)
  thay dòng đó; trình phát neo đáy (lề 40dp): tên in hoa Nunito Bold 13sp / nghệ sĩ 11,5sp giãn 0,2 em, thanh 1,5dp, lùi 38 · phát 45 · tới
  38dp cách 40dp; ngang: 5% · cột số + dòng tin 45% · cột trình phát 45% neo đáy cách 60dp. Màu Hour, Minute, Notification, Controls (nghệ
  sĩ tối 30%) đều trắng. Nunito có trên Google Fonts, đủ tiếng Việt; tên mẫu là thương hiệu. **2 · Default** (`Jun21Screen`, dọc + ngang):
  "HH ❖ mm" một hàng Rubik 30 + 0,7 × cỡ sp (mặc định cỡ 30), ký hiệu 15–50% cỡ đồng hồ cách 10dp, chọn 1 trong 27 ký hiệu (đa số emoji
  màu, không tô được), 4 độ đậm, ngày "EEEE, dd MMMM" 16sp dưới, khối 0,35 trên màn; Rubik thiếu tiếng Việt.
  Không đề xuất: **19 · Concentric** (`ConcentricClock`) cùng cấu trúc Bán nguyệt (vòng giây 00–55 ngoài, vòng phút trong, cùng xoay để giá
  trị hiện tại chỉ sang phải, "HH" 0,46r giữa, "mm" trong viên viền 0,51r kéo tới mép phải), chỉ khác là cả đĩa ở giữa màn (nền đen 30%);
  **18 · Pixel Default** "HH" / "mm" xếp chồng Google Sans Clock (không có trên Google Fonts); **41 · Pixel Width** lưới 2 × 2 như Chồng số,
  đặc trưng là thanh trượt wdth 25–150 / wght 100–800 qua `setFontVariation` (cấm, bản tĩnh thì cần hàng trăm file); **7 · Retro** "HH:mm"
  căn trái font Pixeboy (không có trên Google Fonts); **5 · Text Clock** là giờ bằng chữ (`b.k0`, để sau).
- Đọc 2026-10-29 (bước 0 sau Phai), đề xuất: ~~**5 · Text Clock**~~ (`Sep21Screen`, `sep21_screen_layout` dọc + ngang; đã làm thành Lời
  2026-10-29): giờ (luôn 12 giờ, `calendar.get(10)`) trên phút đọc thành chữ, căn trái lề 40dp; mỗi dòng là `AppCompatTextView` tự co giãn
  một dòng cao Clock Size dp (30–60, mặc định 40), Lato, Clock Weight Thin / Light (mặc định) / Regular / Bold; phút 00 thì chuỗi rỗng, ẩn
  dòng phút; tiếng Anh dùng bảng riêng viết hoa mỗi từ ("Twenty Seven"), ngôn ngữ khác dùng ICU4J `RuleBasedNumberFormat` spellout với
  `CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE` ("Hai mươi bảy") theo ngôn ngữ máy. Ngày "EEEE, dd LLLL" ô 20dp cách 12dp; cụm thông báo đệm
  trên 40dp: hàng icon 18dp cách 10dp (tối đa 5 icon rồi chấm), thẻ thông báo mới (ô tròn 28dp, tiêu đề 16sp, nội dung 15sp tối 40% cách
  7dp, tối đa 2 dòng) giữa hai vạch 1px #3E3E3E cách 15dp, thay hàng icon một lúc; trình phát lề 40dp cách 30dp trên chữ pin 14sp cách đáy
  30dp (cùng số đo trình phát của `may21`); khối chữ chiếm 0,75 phần còn lại dưới 0,25 trống nên dịch lên khi trình phát hiện. Ngang: 5% ·
  cột chữ 50% neo trên (30 + 20dp) · cột trình phát 45% neo đáy · 5%, pin giữa đáy cách 20dp. Màu: Theme trắng, Hour / Minute theo Theme,
  Date / Notification / Controls tối 10%, Battery tối 40%. Android không mở `RuleBasedNumberFormat` trong SDK, bên mình dùng
  `android.icu.text.MessageFormat` `{0,spellout}` (chạy RBNF bên trong) rồi tự viết hoa. Bên mình: mặc định Đậm, phút gradient Tím biển,
  theo ngôn ngữ app, hiện icon + thông báo mới + trình phát cùng lúc, khối chữ không dịch, màn ngang hai cột căn giữa, không ghi nhớ / sự
  kiện / nét vẽ (user duyệt). ~~**10 · Box Clock**~~ (`Feb22Screen`, dọc + ngang; đã làm thành Khung 2026-10-29, số đo khung chữ Josefin
  trong APK yMax 1,096 / yMin −0,233 em, cùng hình chữ số với bản trong app; bên mình: gradient Lan vàng chéo cả khung, khung co giãn mượt,
  số đổi mờ dần, hiện dòng pin + thông báo mới cùng lúc, trình phát nút cố định hai đầu, dừng thì mờ, thêm sự kiện + ghi nhớ, màn ngang khung
  giữa màn, bỏ nét vẽ; user duyệt): "HH" / vạch 1dp cách 5dp / "mm" Josefin Sans Regular
  65sp (cỡ 50–100) trong khung chữ nhật viền 1dp góc vuông nền trong suốt (`bg_white_stroke`), đệm 20 / 15dp; màn dọc 0,25 / 0,5 / 0,25:
  trên ngày "EEEE dd" 24sp cách 18dp dòng pin 15,5dp + "%" 16sp · chấm 12sp · icon 18dp (thông báo mới: icon 18dp + một dòng 17sp thay
  hàng icon), dưới trình phát lùi 35dp · tên 18sp / nghệ sĩ 15sp Light · tới 35dp cách mép 30dp, không thanh tiến trình; màu Clock (khung,
  vạch, số), Date, Notification, Controls; ngang 10% · cột 40% (ngày, dòng trạng thái, khung số) · cột trình phát. **2 · Default** như ghi ở
  trên. Không đề xuất lần này: **15 · Samsung Like** ("HH" trên "mm" như Phai vừa làm).
- Đọc 2026-10-29 (bước 0 sau Khung): ~~**31 · Space Mono**~~ (`Nov23Screen`, dọc + ngang; đã làm thành Đơn cách 2026-10-29): "HH" trên "mm"
  Space Mono 160sp × cỡ (60–100, mặc định 80; ngang × 0,9), mỗi số là `NoPadTextView` (khung = nét chữ của chuỗi, nét sát đỉnh khung) cách
  28dp, ngày "EEEE, dd MMM" in hoa 20sp × cỡ cách 28dp, khối căn giữa 0,7 phần trên (lề 40dp); 0,3 phần dưới neo đáy cách 35dp: dòng pin
  16dp + "%" 16sp · chấm · icon 17dp (thông báo mới: icon 18dp + một dòng 16sp in hoa thay dòng đó), cách 25dp trình phát (thanh 1,5dp, 15dp,
  tên 16sp / nghệ sĩ 14sp in hoa tối 30% trái, lùi 43 · phát 48 cách 10 · tới 43dp, lấn lề phải 13dp); ngang cột số 45% căn giữa, cột phải
  55% neo đáy (ẩn khi trống). Giờ / phút cho gradient (`xc.b.C0`). Font Space Mono của Google Fonts cùng hình chữ số, đủ tiếng Việt. Bên
  mình: giờ gradient Mùa thu, số đổi mờ dần, thời tiết dưới ngày, sự kiện + ghi nhớ đầu khối dưới, hiện dòng pin + thông báo mới cùng lúc,
  cột phải ngang căn giữa, bỏ nét vẽ; `AodCompactPlayer` thêm nút lùi, thanh tiến trình ở trên, chữ in hoa (user duyệt). **2 · Default**
  bỏ: user chốt là trùng chủ đề Cổ điển đã có.
- Đọc 2026-10-29 (bước 0 sau Đơn cách): ~~**14 · Solar System**~~ (`SolarSystemClock`, `Jun22Screen`, dọc + ngang; đã làm thành Quỹ đạo
  2026-10-30): r = cỡ (65–90, mặc định 80) / 110 × nửa bề ngang của view (dọc cả màn, ngang ô vuông cạnh ngắn), đĩa đen 30%, ba quỹ đạo nét
  1dp × cỡ / 110 ở r − 0,44r / r − 0,22r / r (màu Clock alpha 150 / 100 / 50), hành tinh giờ 0,22r / 3 trên quỹ đạo trong, phút 0,22r / 4,5
  giữa, giây 0,22r / 7,5 ngoài, góc theo giờ thật tới mili giây nhưng màn vẽ lại mỗi giây (`ib.w` căn đầu giây); chế độ tiết kiệm ẩn hành
  tinh giây; Digital Clock Hiện / Ẩn: "HH:mm" Josefin Regular 28sp × cỡ / 110 giãn 0,2 em giữa màn; khối chữ dưới cách đáy 60dp cùng họ với
  `may22` (Kẹp giấy): ngày "dd MMMM yyyy" in hoa Poppins SemiBold 15sp giãn 0,25 em, dòng pin · icon 17dp, thông báo mới một dòng in hoa
  13sp, trình phát lùi / tới 40dp cách mép 40dp, tên 13sp / nghệ sĩ 11,5sp in hoa; ngang: lề 30dp, đĩa ô vuông cạnh ngắn của màn (tràn lên
  khối chữ). Màu: giờ #CA5353, phút #76AEA1, giây #9E9E9E, Clock trắng, Controls #CED4DA (ngày, nhạc; pin, thông báo, nghệ sĩ tối 30%).
  Bên mình: hành tinh chạy mượt mỗi khung hình (`isLiveInPreview`), Controls tách ngày / pin & thông báo / nhạc, hiện tất cả cùng lúc, sự kiện
  + ghi nhớ giữa vùng trống trên đĩa, màn ngang đĩa giữa màn, Plus Jakarta Sans thay Poppins, bỏ chế độ tiết kiệm và nét vẽ (user duyệt).
  Không đề xuất lần này: **9 · Flip Clock** (`FlipClock`: hai thẻ lật 220dp, 9 font, lật 400ms; ba font không có trên Google Fonts).
- ~~**12 · Vertical Clock**~~ (`VerticalTextView`, `apr22_screen_layout` + bản ngang, đọc 2026-10-26, đọc lại đủ 2026-10-27, đã làm thành Gáy
  sách 2026-10-27): "HH:mm" Josefin Sans Regular cỡ 25–60sp (mặc định 40) xoay −90° (đọc từ dưới lên, nét chữ căn giữa dòng), ngày "EEE, dd MMMM" in
  hoa nửa cỡ trên giờ cùng trục cách 30dp; cột cách mép trái 18dp, đáy giờ cách đáy 45dp (ngang 30dp). Dọc: phần trên cột chia 0,25 trống /
  0,75 cho cột thông tin (lề 40 / 20dp): pin 15,5dp + "72%" 15sp · chấm 14sp · icon thông báo 17dp cách 10dp; thông báo mới (icon 17dp +
  một dòng 17sp) thay hàng đó một lúc; đệm 40dp; trình phát (tên 18sp / nghệ sĩ 16sp Light, chữ 120–200dp, phát 42dp cách 30dp, tới 37dp
  cách 5dp, không nút lùi, không thanh tiến trình) hiện theo hẹn giờ. Ngang: 5% · cột giờ neo đáy · 10% · cột thông tin neo đáy lề 30dp ·
  5%; cỡ lớn thì cột giờ cao hơn màn ngang. Màu: Theme trắng, Clock / Date / Notification (cả pin) / Controls theo Theme. Không có dạng
  tiết kiệm.
- **43 · MIUI Doodle** ⭐ (`Nov24Screen`): giờ, phút, thứ, tháng, ngày là **ảnh vẽ tay** (24 + 60 + 7 + 12 + 31 hình), không phải
  font; không được lấy ảnh của app mẫu, phải tự vẽ cả bộ, nên để sau.

Để sau: 33 Dual Timezone (cần control chọn múi giờ), 16 / 22 (cần ô nhập chữ), ~~chủ đề giờ bằng chữ~~ (đã làm thành Lời bằng ICU spellout có sẵn), 6 Visualizer (cần quyền `RECORD_AUDIO`, phải hỏi trước), 8 / 9 / 11 / 14 / 21 (hiệu ứng, hạt, lật số: công sức
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
