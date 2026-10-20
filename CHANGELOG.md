# Changelog

Các thay đổi đáng chú ý của Aodamn (trước đây là Custom AOD). Định dạng dựa
theo [Keep a Changelog](https://keepachangelog.com/vi/1.1.0/).

Project chưa phát hành (`versionName` vẫn là `v1.0.0`), nên các thay đổi được gom theo
ngày. Lịch sử của demo FakeAOD, nơi lõi AOD được làm ra trước, giữ ở cuối file.
Mã nguồn demo FakeAOD không nằm trong repo này.

## [Chưa phát hành] – 2026-10-20 – Chủ đề Dải màu

Build được, unit test pass (thêm test chặn Dải màu có lại khối đã bỏ hay thiếu tuỳ chọn của app mẫu, test vị trí giờ trên vạch theo app mẫu,
test thuộc tính có chế độ tự động; test nội dung thông báo cho phép Dải màu như Bán nguyệt), `tools/check-rules.sh` ra 0 vi phạm (38 rule),
thư mục các chủ đề cũ không đổi dòng nào. Chưa test trên máy.

### Thêm

- Chủ đề Dải màu (Gradient Line Clock của Muviz Edge, số đo lấy từ `Sep23Clock` và layout `sep23_screen_layout` dịch ngược; mock duyệt trước
  khi code): vạch mảnh dọc giữa màn (dày 0,008 cạnh ngắn, đầu tròn) tô gradient của app mẫu vàng → hồng tím → xanh từ đầu tới cuối vạch. Giờ
  "HH:mm" / "hh:mm" Google Sans Regular cỡ 0,11 cạnh ngắn × cỡ nằm trên vạch, căn theo nét chữ như app mẫu, trượt từ đỉnh (00:00) tới đáy
  (23:59) theo phút trong ngày; ngày "EEEE, dd MMM" mờ 78% dưới giờ; vạch khoét quanh giờ và ngày. Thời tiết (khối dùng chung) nằm cạnh ngày,
  đi cùng giờ; kiểu thời tiết cao thì dòng ngày lùi xuống, vạch khoét dài theo. Trình phát như app mẫu ở đầu màn (lùi · tên bài / nghệ sĩ mờ
  · tới, nút mờ 0,7, chạm tên để phát / dừng, dừng thì chữ mờ, đổi bài thì tên trượt lên), hiện / ẩn thì vạch co giãn mượt; đáy là thông báo
  mới nhất (icon + "Tiêu đề: nội dung") trên dòng pin · thông báo. Màn ngang (app mẫu dùng chung layout dọc nên vạch ngắn, giờ nhỏ): vạch nằm
  ngang hết bề rộng, giờ to hơn trượt từ trái sang phải, trình phát giữa phía trên, dòng pin giữa phía dưới.
- Tuỳ chọn đủ như app mẫu: cỡ 80–150% (mặc định 110%), vị trí ("Chạy theo giờ" mặc định bật, tắt thì ghim 0–100% chiều dài vạch, 20 nấc),
  màu giờ, màu vạch (gradient, dùng bộ chọn Một màu / Gradient), màu pin, màu thông báo, màu nhạc; màu chủ đạo của app mẫu tách thành màu
  từng phần mặc định #E0E0E0, thêm màu ngày riêng (app mẫu dùng màu giờ mờ 78%) cho tab Ngày, giờ và ngày tô được gradient. Dòng pin hai màu
  như app mẫu (pin theo màu pin, mờ 0,9; chấm và icon theo màu thông báo), một dòng như các chủ đề khác (user duyệt).
- Dải màu chỉ có ngày, thời tiết, pin, thông báo (kèm thông báo mới nhất, `hasNotificationContent = true`), nhạc: ghi nhớ, sự kiện, nét vẽ
  không có chỗ vì giờ trượt hết chiều dài vạch, hai bên vạch hẹp.
- Thuộc tính khoảng số có chế độ tự động (`AodRangeOption.autoLabelRes`, giá trị `AUTO`): màn sửa có công tắc tự động, tắt thì hiện thanh
  trượt ghim, bắt đầu ở giữa dải. Icon tab Vạch `line-vertical` (Phosphor, bản thường + fill).

### Thay đổi

- Chỉ thêm, không đổi chủ đề đang có: mục `GRADIENT_LINE` ở `AodThemeValue`, nhánh `when` ở `AodPortrait` / `AodLandscape` /
  `AodMediaControls`, chuỗi vi / en (`theme_gradient_line`, `theme_tab_line`, `theme_color_line`, `opt_position_*`, mẫu giờ / ngày / thông báo),
  `autoLabelRes` mặc định `null` nên các thanh trượt cũ không đổi (`decode` vẫn kẹp vào dải), một dòng ở `filledIconOf` cho icon tab mới.

## [Chưa phát hành] – 2026-10-20 – Chủ đề Bán nguyệt

Build được, lint 0 lỗi, 16 cảnh báo đều ở file không đụng tới (4 cảnh báo mới so với 12 cũ là bản mới hơn của thư viện baseline profile),
unit test pass (thêm test chặn Bán nguyệt có lại khối đã bỏ hay thiếu màu của app mẫu; test nội dung thông báo và chạy
theo giờ thật ở khung xem trước cho phép Bán nguyệt như La bàn), `tools/check-rules.sh` ra 0 vi phạm (38 rule), thư mục các chủ đề cũ không đổi
dòng nào. Đã test trên máy.

### Thêm

- Chủ đề Bán nguyệt (Half Concentric của Muviz Edge, số đo lấy từ `HalfConcentricClock` và layout `dec22_screen_layout` dọc / ngang dịch ngược;
  mock duyệt trước khi code): mặt số tròn bán kính 0,45 cạnh ngắn, tâm lùi ra ngoài mép trái 0,95r nên chỉ thấy nửa phải, giữa chiều cao. Vòng
  ngoài là giây (60 vạch, số 00–55 Google Sans Regular), vòng trong là phút; cả hai xoay theo giờ thật mỗi khung hình như app mẫu (vòng giây
  quay một vòng mỗi phút, vòng phút trôi liên tục) để giá trị hiện tại chỉ sang phải, số luôn đứng thẳng. Giờ "HH" to, phút "mm" trong viên
  viền; vạch và số hai vòng khoét quanh viên; đĩa đen 50% dưới mặt số (thấy khi có ảnh nền) và phủ tối tròn làm đỉnh, đáy, mép trái chìm vào
  nền như app mẫu. Viền viên luôn kéo tới mép mặt số ôm số giây đang chỉ như lúc thức của app mẫu; bỏ trạng thái nghỉ (app mẫu sau 15 giây
  mặc định, cài đặt "Low power after", thu viên về và ẩn số giây) để vòng giây luôn hiện (user chọn sau khi test trên máy). Hai vòng vạch vẽ một lần vào `GraphicsLayer` rồi chỉ xoay, số đặt lại trong pha vẽ nên không recompose. Khung xem trước ở màn chủ đề,
  Tùy chỉnh chạy theo giờ thật (`isLiveInPreview`), thẻ ở tab AOD đứng yên.
- Cột bên phải trên trục số đang chỉ (user chốt dòng pin dưới ngày): ngày "dd MMM yyyy" tối hơn 30% rồi thứ "EEEE", in hoa Google Sans Bold
  giãn chữ (12 / 13sp màn dọc, 14 / 15sp màn ngang như layout mẫu), thời tiết một dòng riêng, dòng pin · thông báo (`AodStatusLine`), thông
  báo mới nhất (icon + "TIÊU ĐỀ: NỘI DUNG" in hoa, chạy chữ một lần). Trình phát như app mẫu (lùi · tên bài / nghệ sĩ in hoa giãn chữ · tới,
  chạm tên bài để phát / dừng, dừng thì chữ mờ; đổi bài thì tên trượt lên) giữa vùng dưới mặt số. Màn ngang: nửa mặt số to hơn (r = 0,65 cạnh
  ngắn, app mẫu 0,45 chỉ chiếm 1/5 bề ngang), đỉnh / đáy vòng chìm vào phủ tối; cột giữa phần còn lại, có nhạc thì cột và trình phát thành một
  khối giữa chiều cao.
- Mặc định bắt mắt: viền viên phút `Red` (#E5484D, màu nhấn chưa chủ đề nào dùng), giờ / phút / ngày GreyED, vạch và số Grey6E (#757575 của
  app mẫu), dòng pin Grey9A, nhạc GreyE0. Đủ 6 màu của app mẫu: Đồng hồ (giờ, phút, viền phút, vạch & số), Ngày, Pin & thông báo, Nhạc (màu
  điều khiển tách theo phần, user duyệt).
- Bán nguyệt chỉ có ngày, thời tiết, pin, thông báo (kèm thông báo mới nhất, `hasNotificationContent = true`), nhạc: ghi nhớ, sự kiện, nét vẽ
  không có chỗ đặt cân quanh nửa mặt số ở cả dọc lẫn ngang.
- `tools/mockups/aod_landscape_mockups.py` có nhóm khung của Bán nguyệt (màu, bốn khung dọc, ba khung ngang; khung D1 và N1
  xoay theo giờ thật của máy xem mock).

### Thay đổi

- Chỉ thêm, không đổi chủ đề đang có: mục `HALF_MOON` ở `AodThemeValue`, nhánh `when` ở `AodPortrait` / `AodLandscape` / `AodMediaControls`,
  chuỗi vi / en (`theme_half_moon`, `theme_color_minute_ring`, `theme_color_dial`, mẫu ngày / thứ / thông báo); không thêm màu, font hay cờ dùng
  chung (dùng `Red`, `FontFamilyGoogleSansText`, `AodState.isClockLive` có sẵn).

## [Chưa phát hành] – 2026-10-20 – Bộ chọn màu Một màu / Gradient, mã hex

### Thêm

- `AodColorPicker` thay chỗ `ClockColorPicker` trong `AodThemeOptionList` (màu chủ đề và màu khối ở Tùy chỉnh). Tab Một màu giữ 16 ô +
  vòng màu + thanh độ sáng, thêm ô mã hex 6 ký tự. Thuộc tính bật `isGradientAllowed` (hoặc có `defaultGradient`) có thêm tab Gradient:
  12 bộ có sẵn lấy từ bảng màu app mẫu Muviz Edge (`GradientSwatchValue`, bộ trùng đã bỏ, hướng quy về Đầu → Cuối), 3 chấm Đầu / Giữa /
  Cuối, 4 hướng (`AodGradientDirection`) + Đảo chiều = 8 hướng, bảng màu và mã hex của chấm đang chọn. Chip phần tô đang là gradient hiện
  3 chấm chồng nhau (`AodGradientDots`).
- Cách lưu: khoá màu cũ giữ màu Đầu (code chỉ đọc `config[option]` không phải sửa), gradient thêm `<khoá>_gradient_2`,
  `<khoá>_gradient_3`, `<khoá>_gradient_direction`; không có khoá phụ là một màu nên màu đã lưu giữ nguyên. Đọc bằng
  `config.gradientOf(option)` (null = một màu), tô bằng `AodGradient.toBrush()` (trải theo khung của chính phần được tô).
- Bật gradient cho 7 khối dùng chung ở Tùy chỉnh: ngày, pin, thời tiết, sự kiện, ghi nhớ, thông báo, nhạc (nét vẽ giữ một màu vì tô bằng
  ảnh đã vẽ). `AodComponentStyle.gradient` mang gradient của khối; `Modifier.aodTint(style)` vẽ phần tô bằng `style.color` ra lớp riêng
  rồi phủ gradient SrcIn, nên mỗi chữ / icon / chấm tô theo khung của chính nó, giữ độ trong; khối một màu không đổi gì. Nút nhạc tắt
  vẫn xám, thanh nhạc tô gradient phần đã phát; pin đang sạc / yếu giữ màu báo của phần đầy, chỉ chữ phần trăm tô gradient. Chủ đề
  Dải màu và các chủ đề cũ bật sau.
- Chủ đề cũ, mỗi chủ đề một lượt, chỉ đổi chỗ tô màu (bố cục, số đo giữ nguyên). Hàm dùng chung `AodThemeConfig.brushOf(option)`
  (gradient hoặc `SolidColor`) cho chữ, `colorsOf(option)` cho hình tự dựng gradient.
  - Cổ điển: giờ và phút bật gradient (cả tab Đồng hồ lúc onboarding). Mặt số: span brush, giờ và phút cùng gradient thì liền một dải;
    xếp chồng: mỗi dòng một gradient; kim: kim, mốc giờ chính tô dọc theo chính nó (gốc màu Đầu → đầu kim màu Cuối, bỏ qua hướng vì kim
    xoay), chấm giữa màu Đầu của giờ.
  - Sỏi: 6 màu (khối giờ / phút, số giờ / phút, ngày, nhạc) bật gradient. Khối (đặc lẫn viền) và nút phát tô theo khung của khối, nút
    phát xoay thì gradient xoay theo; chữ số, ngày tô bằng brush; thanh sóng tô phần đã nghe (`AodWaveTrack` thêm `brush` mặc định
    `SolidColor(color)`, chủ đề khác không đổi). Icon trên nút phát chọn đen / trắng theo màu Đầu.
  - Số áo: giờ, phút, ngày, dòng trạng thái bật gradient. Nét viền mỗi chữ số tô theo ô của số đó; chữ ngày và viền viên thuốc thứ tô
    bằng brush. `AodStatusLine` thêm `brush` (mặc định null = như cũ, chủ đề khác chưa truyền): pin, chữ, chấm ngăn, icon app tô gradient,
    pin yếu vẫn đỏ. `Modifier.aodTint(brush)` cho chỗ nhận Brush thẳng.
  - Viên nang: 5 màu (chữ số, viên nang, ngày, nhạc, dòng trạng thái) bật gradient. Viên nang đặc (giữ độ mờ) lẫn viền tô theo khung
    viên; mỗi chữ số tô theo khung số đó, hai chấm của dấu hai chấm theo khung viên; ngày, tên bài, nút phát tròn, phần đã nghe của thanh
    tiến trình tô bằng brush; nghệ sĩ, nút lùi / tới vẫn xám.
  - Thẻ: 6 màu (giờ, phút + ô AM/PM, thẻ, ngày, nhạc, dòng trạng thái) bật gradient. Mỗi chữ số (đặc lẫn viền) tô theo ô của số đó, ô
    AM/PM và chữ trong ô theo khung ô, nền / viền thẻ theo khung thẻ (giữ độ mờ); ngày, tên bài, nghệ sĩ, nút phát, nút lùi / tới (lúc
    bấm được) và thanh sóng tô gradient; icon nút phát chọn đen / trắng theo màu Đầu.
- Test `AodColorOptionTest`: màu cũ đọc như cũ, gradient lưu / đọc lại, về Một màu xoá khoá phụ, mặc định gradient, đặt lại, đổi hướng.

### Đổi

- Callback của `AodThemeOptionList` và intent Preview / Change option ở Tùy chỉnh, màn chủ đề mang map khoá → giá trị (null = xoá)
  thay cho một cặp khoá / giá trị, để một lần đổi gradient ghi nhiều khoá trong một bước hoàn tác (`AodThemeConfig.withRaws`).
  `isDefault` / `isSame` / đặt lại tab tính cả khoá phụ.

## [Chưa phát hành] – 2026-10-19 – Màu có sẵn trong bảng màu

### Thêm

- Bảng màu (Tùy chỉnh và màn chủ đề, mọi khối / phần có màu) có thêm hàng "Màu có sẵn" phía trên vòng màu: 16 ô, 2 hàng × 8, xếp theo
  sắc độ: trắng, xám, YouTube, SoundCloud, Amazon, Snapchat, Duolingo, Spotify, mint Aodamn, TikTok, Telegram, Zalo, Discord, Twitch,
  Instagram, Tinder (`ColorSwatchValue`, màu ở `Colors.kt`). Chọn ô ghi thẳng màu ARGB như vòng màu nên không đổi cách lưu; ô đang chọn
  có vòng mint (chuyển mờ dần), tên ô cho TalkBack.

## [Chưa phát hành] – 2026-10-19 – Baseline profile

Đã tạo profile khởi động trên máy thật; bản release build, cài và mở được (hết crash lúc khởi động).

### Sửa

- Release crash ngay lúc mở ("Failed to create an instance of androidx.work.impl.WorkDatabase"): R8 xoá constructor rỗng của
  `WorkDatabase_Impl` mà Room gọi bằng reflection (WorkManager 2.7.0 đi kèm Google Mobile Ads, rule của nó không giữ constructor ở R8
  full mode). Thêm rule hẹp `-keep class * extends androidx.room.RoomDatabase { <init>(); }` ở `proguard-rules.pro`.

### Thêm

- Module `:baselineprofile` (`com.android.test`, plugin `androidx.baselineprofile`, chạy trên máy thật đang cắm): `BaselineProfileGenerator`
  ghi đoạn khởi động, `StartupBenchmarks` đo khởi động lạnh không / có profile; `:app` có `profileinstaller` và nhận profile qua
  `baselineProfile(project(":baselineprofile"))`. Tạo bằng `./gradlew :app:generateReleaseBaselineProfile`.
- Sửa lại bộ khung mẫu: thư mục `baselineprofile` khớp `include(":baselineprofile")` (trước là `baselineProfile`, chỉ chạy được trên máy
  không phân biệt hoa thường), namespace / package `com.decoutkhanqindev.aodamn.baselineprofile` thay `com.example`, cả hai lớp đọc app id
  từ `targetAppId` thay vì viết cứng, `benchmark-macro-junit4` cùng version `1.5.0-beta01` với plugin.

## [Chưa phát hành] – 2026-10-19 – Banner Splash, onboarding chung một banner

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Thêm

- Banner ở đáy Splash (`bannerSplash`, `BANNER_SPLASH_ALL_ID`), dưới thanh tải; chỉ tải khi đã có kết quả consent.
- `OnboardingNavDisplay` (`OnboardingDestination`): Ngôn ngữ → Tùy chỉnh → Quyền lúc mở lần đầu nằm trong back stack riêng, một banner
  chung `bannerOnboarding` (`BANNER_ONBOARDING_ALL_ID`) ở đáy ngoài `NavDisplay` nên chuyển màn không tải lại banner. Màn bên trong không tự
  chừa thanh điều hướng (inset đã tiêu thụ, slot banner chừa); Tùy chỉnh báo nhấn giữ xem AOD (`onPeekingChange`) để banner trượt xuống,
  chỗ banner thành nền đen như khung xem trước. Vào Main / màn chủ đề đi bằng back stack gốc (`rootBackStack` của `CustomizeScreen`,
  `PermissionScreen`).

### Bỏ

- Banner riêng của từng màn onboarding: `bannerLanguage`, `bannerOnboardingCustomize`, `bannerPermission` và id `BANNER_LANGUAGE_ALL_ID`,
  `BANNER_ONBOARDING_CUSTOMIZE_ALL_ID`, `BANNER_PERMISSION_ALL_ID`; slot `bannerAd` của `LanguageContent`, `PermissionContent`.
  `PermissionDestination` chỉ còn trong `OnboardingNavDisplay`.

## [Chưa phát hành] – 2026-10-19 – Màn dọc giãn trên - dưới

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy. Màn ngang không đổi.

### Đổi

- Cổ điển, Sỏi, Viên nang, Thẻ (màn dọc, mock đã duyệt): không còn dồn cả cột vào giữa màn. Đồng hồ + ngày bắt đầu ở 12% chiều cao, ngay
  dưới là thời tiết (một dòng riêng giữa bề ngang, Cổ điển trước đây đứng cạnh ngày), sự kiện, ghi nhớ, nét vẽ; thông báo mới nhất, nhạc
  và dòng pin thẳng đáy cách đáy 10%; giữa để trống. Hai cụm chạm nhau thì cụm đáy nằm ngay dưới cụm trên. Dùng chung `AodPortraitSpread`
  + `AodSpreadMetrics`, khối chia bằng `AodThemeSpec.topPanelBlocks()` / `bottomPanelBlocks()`; `AodDetails` tách thành `AodTopDetails`
  / `AodBottomDetails`. Thẻ vẫn thu nhỏ khi nhiều khối, giờ theo phần chiều cao giữa hai lề (`CardSpread`).
- La bàn (màn dọc): mặt số vẫn giữa màn; ngày (+ thời tiết) lên giữa vùng trên mặt số, dòng pin · thông báo, thông báo mới nhất và trình phát
  giữa vùng dưới mặt số (trước đây tất cả xếp dọc cách đáy 60dp, bỏ `CompassMetrics.BottomMargin`).
- Vành (màn dọc): dòng pin · thông báo lên ngay dưới ngày (vùng giữa số 12 và đầu kim phút), vùng dưới kim chỉ còn trình phát.
- Tháp (màn dọc): thời tiết luôn một dòng riêng dưới ngày, không đứng cạnh ngày cả khi không có sự kiện.
- CLAUDE.md, README › Thêm chủ đề mới: ghi quy tắc màn dọc giãn trên - dưới cho chủ đề sau.

## [Chưa phát hành] – 2026-10-18 – Đổi tên thành Aodamn

### Đổi

- Đổi tên app từ Custom AOD thành Aodamn: tên app, package, `namespace` và `applicationId` thành `com.decoutkhanqindev.aodamn`, repo GitHub
  thành `decoutkhanqindev/Aodamn`, thư mục project thành `aodamn`. Đổi `applicationId` nên máy đang cài bản cũ sẽ có thêm một app mới,
  dữ liệu không chuyển sang (file DataStore cũng đổi thành `aodamn_prefs`).

## [Chưa phát hành] – 2026-10-18 – Toast khi áp dụng

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Đổi

- Tùy chỉnh mở từ tab AOD: Áp dụng xong ở lại màn và hiện toast "Đã áp dụng tùy chỉnh" thay vì quay về tab AOD; bản vừa ghi thành
  bản gốc mới (nút Áp dụng mờ lại, lịch sử hoàn tác xoá như lúc Lưu ở màn chủ đề). Ảnh nền hay nét vẽ ghi lỗi thì chỉ hiện toast lỗi,
  giữ bản gốc cũ để Áp dụng lại được. Onboarding vẫn sang Main / màn quyền như cũ.
- Tab AOD › Đặt lại về chủ đề mặc định: ghi xong hiện toast "Đã đặt lại chủ đề mặc định".

## [Chưa phát hành] – 2026-10-18 – Chủ đề La bàn

Build được, lint không thêm cảnh báo (0 lỗi, 12 cảnh báo như trước), unit test pass (thêm test chặn La bàn có lại khối đã bỏ, thiếu kiểu của app mẫu, và chủ đề
khác chạy theo giờ thật ở khung xem trước; test nội dung thông báo cho phép La bàn tự vẽ thông báo mới nhất như Chuỗi), `tools/check-rules.sh` ra 0 vi phạm (38 rule), thư mục các chủ đề cũ không
đổi dòng nào. Đã test trên máy.

### Thêm

- Chủ đề La bàn (Ambitio Face của Muviz Edge, số đo lấy từ `AmbitioClock` và layout `may23_screen_layout` dịch ngược; mock duyệt trước khi code): mặt số vuông 75% cạnh ngắn × cỡ, các vòng số đồng tâm xoay về một đường chỉ cố định sang phải: vòng giờ 1–12 Krona One xen nhãn giờ rưỡi "02:30", vòng phút 00–55 xen vạch, vòng giây 00–55 trong cùng, hai vòng tròn mảnh ôm vòng phút; chữ đặt theo bán kính, số đang chỉ nằm ngang trên đường chỉ. Hai kiểu như Clock Style của app mẫu: có vòng giây (mặc định; đường chỉ từ tâm, cả ba vòng chạy theo giờ thật mỗi khung hình: vòng giây quay một vòng mỗi phút, vòng phút và giờ trôi liên tục) và không vòng giây (đường chỉ lùi ra 0,28u, cập nhật đầu mỗi phút, vòng phút và giờ xoay mượt 400ms tới chỗ mới thay vì nhảy như app mẫu); đổi kiểu thì vòng giây mờ dần và đường chỉ lùi trong 300ms như app mẫu. Mỗi vòng vẽ một lần vào `GraphicsLayer`, mỗi khung hình chỉ xoay layer nên không recompose, không vẽ lại chữ. Khung xem trước ở màn chủ đề và Tùy chỉnh cũng chạy theo giờ thật như màn sửa của app mẫu (`isLiveInPreview`); thẻ thu nhỏ ở tab AOD (thẻ thiết kế, lưới chủ đề) đứng yên ở giờ mẫu (`AodState.isStill`) để màn danh sách không vẽ lại mỗi khung hình.
- Dưới mặt số, cách đáy 60dp như layout mẫu: ngày "dd MMMM yyyy" in hoa giãn chữ (Google Sans Bold 14sp, + thời tiết), dòng pin · thông báo (`AodStatusLine`, Google Sans Regular), thông báo mới nhất (icon + "TIÊU ĐỀ: NỘI DUNG" in hoa, chạy chữ một lần khi dài, như `notify_lt` của app mẫu) và trình phát như app mẫu (lùi · tên bài / nghệ sĩ in hoa giãn chữ · tới, không nút phát: chạm tên bài để phát / dừng, dừng thì chữ mờ; không thanh tiến trình; tên dài chạy chữ, đổi bài thì tên trượt lên). Màn ngang: mặt số giữa màn, không nhạc thì ngày trái, pin · thông báo phải; có nhạc thì ngày + pin trái, trình phát phải.
- Mặc định bắt mắt: số giờ và đường chỉ `Amber` (#FFD54F, màu mới chưa chủ đề nào dùng), phút GreyE0, giờ rưỡi / giây / dòng pin Grey9A như màu 2 của app mẫu. Tuỳ chọn đủ như app mẫu: Đồng hồ (kiểu có / không vòng giây, cỡ 70–120%, màu giờ, giờ rưỡi, phút, giây riêng từng vòng thay cho màu 1 / màu 2; vạch giữa số phút theo màu phút, mờ 0,6), Ngày, Pin & thông báo, Nhạc (thay cho màu điều khiển).
- La bàn chỉ có ngày, thời tiết, pin, thông báo (kèm thông báo mới nhất, `hasNotificationContent = true`), nhạc: ghi nhớ, sự kiện, nét vẽ không có chỗ đặt cân quanh mặt số tròn ở cả dọc lẫn ngang.
- Font Krona One (số, đúng font của app mẫu, bản tĩnh Google Fonts; tên dành riêng "Krona" nên không tự sửa file; không có tiếng Việt nhưng chỉ vẽ chữ số) và Google Sans Regular (sinh từ bản Google Sans đã cắt). File TTF tĩnh không trục variable như mọi font khác (lỗi Xiaomi 2026-10-12). Giấy phép `licenses/KronaOne-OFL.txt`.
- `tools/mockups/aod_landscape_mockups.py` có nhóm khung của La bàn (màu từng vòng và cỡ, bốn khung dọc, ba khung ngang; khung D1 và N1 xoay theo giờ thật của máy xem mock).
- CLAUDE.md, README: chủ đề mới bám sát app mẫu (đọc code vẽ + layout trước khi mô tả / đề xuất, giữ đủ mọi tuỳ chọn, chi tiết và chuyển động như kiểu có / không vòng giây, vòng giây xoay theo giờ thật); không tự bỏ / gộp tuỳ chọn của app mẫu: mock liệt kê từng tuỳ chọn là giữ / đổi / bỏ, đổi hay bỏ phải hỏi user; chỗ làm xịn hơn được thì cứ làm và nêu trong mock; khung xem trước dùng giờ mẫu trừ chủ đề bật `isLiveInPreview`.

### Thay đổi

- Chỉ thêm, không đổi chủ đề đang có: mục `COMPASS` ở `AodThemeValue`, nhánh `when` ở `AodPortrait` / `AodLandscape` / `AodMediaControls`, cờ `AodState.isPreview` (mặc định `false`, chỉ `toPreviewAodState` đặt `true`) và `AodThemeSpec.isLiveInPreview` (mặc định `false`, chỉ La bàn bật; mặt số đọc `AodState.isClockLive`), khung xem trước các chủ đề khác vẫn dùng giờ mẫu, màu `Amber`, `FontFamilyGoogleSansText` (Regular + Bold, họ riêng để dòng pin của Vành giữ Bold) và `FontFamilyKronaOne`; `make_static_fonts.py` thêm Google Sans Regular và Krona One (file `google_sans_bold.ttf` giữ nguyên byte).

## [Chưa phát hành] – 2026-10-18 – Thẻ chủ đề luôn là kiểu mặc định

### Sửa

- Thẻ ở lưới chủ đề (tab AOD) quay lại luôn vẽ kiểu mặc định (kiểu đẹp nhất của chủ đề) thay vì cấu hình đã lưu: sửa chủ đề không làm đổi thẻ. Màn chủ đề (xem trước và sửa) vẫn mở cấu hình đã lưu, chưa sửa thì là mặc định. Bỏ `AodTabState.themeConfigs`.

## [Chưa phát hành] – 2026-10-18 – Chủ đề Vành

Build được, lint không thêm cảnh báo (0 lỗi, 12 cảnh báo như trước), unit test pass (thêm test chặn Vành có lại khối đã bỏ), `tools/check-rules.sh` ra 0 vi phạm (38 rule), thư mục các chủ đề cũ không đổi dòng nào. Đã test trên máy.

### Thêm

- Chủ đề Vành (iOS Fill Clock của Muviz Edge, số đo lấy từ `IosFillClock` và layout `aug24_screen_layout` dịch ngược; mock duyệt trước khi code): mặt số kim phủ cả màn, vạch là tia từ tâm cắt trong dải khung chữ nhật sát mép (khung ngoài lùi 0,06 nửa cạnh ngắn / 0,03 nửa cạnh dài; vạch phút tới khung lùi 0,2 / 0,1, mờ 70 / 255; vạch giờ dài tới khung lùi 0,5 / 0,22, bỏ chỗ có số), bốn số 12 / 3 / 6 / 9 cỡ 0,14 cạnh ngắn căn giữa theo nét chữ, kim giờ / phút (đoạn mảnh + đoạn dày, bóng mờ đen 30%) quay 400ms theo chiều ngắn nhất khi sang phút mới, chấm tâm có vòng màu nhấn. Không có kim giây vì AOD cập nhật mỗi phút (app mẫu cũng ẩn ở chế độ tiết kiệm pin). Thứ + số ngày Google Sans Bold 20sp (tiếng Việt viết đủ "THỨ BẢY", tiếng Anh "SAT"), thời tiết cạnh ngày, nằm giữa số 12 và đầu kim phút; pin · thông báo (`AodStatusLine`) và trình phát gọn riêng (lùi · tên bài in hoa / nghệ sĩ mờ · tới, chạm tên bài để phát / dừng, rộng 184dp) giữa đầu kim và số 6, nên kim không đè lên chữ. Màn ngang mặt số vẫn phủ màn: không nhạc thì pin trái, ngày phải như app mẫu; có nhạc thì ngày + pin xếp dọc bên trái, trình phát bên phải.
- Mặc định bắt mắt: đủ 60 vạch, số 12 / 3 / 6 / 9 màu `Cyan` (#80DEEA, màu mới: lime của app mẫu đã là màu Viên nang), kim / vạch / chữ GreyED. Tuỳ chọn gọn theo app mẫu: Đồng hồ (kiểu vạch: vạch phút / vạch giờ, màu chữ số, màu kim và vạch), Ngày (một màu cho cả thứ và số ngày), Pin & thông báo, Nhạc.
- Vành chỉ có ngày, thời tiết, pin, thông báo, nhạc (`components`): ghi nhớ, sự kiện, nét vẽ, nội dung thông báo bỏ hẳn vì mặt số phủ màn chỉ chừa vùng nhỏ quanh kim.
- Font Zalando Sans Expanded SemiBold (số, đúng font của app mẫu; bản tĩnh gốc của nhà làm font vì giấy phép có tên dành riêng "Zalando") và Google Sans Bold (chữ; app mẫu dùng Product Sans, không có trên Google Fonts, Google Sans cùng thiết kế, cắt còn Latin + tiếng Việt: 118 KB thay 2,4 MB). Cả hai là file TTF tĩnh không có trục variable, khai báo đúng một độ đậm, nên không dính lỗi máy Xiaomi thay font (2026-10-12); bút vẽ số chỉ đặt `typeface`. Giấy phép `licenses/ZalandoSans-OFL.txt`, `licenses/GoogleSans-OFL.txt`.
- `tools/mockups/aod_landscape_mockups.py` có nhóm khung của Vành (hai kiểu vạch, bốn khung dọc, ba khung ngang; chữ đo bằng font thật, vẽ bằng font Google Fonts).

### Thay đổi

- Chỉ thêm, không đổi chủ đề đang có: mục `BEZEL` ở `AodThemeValue`, nhánh `when` ở `AodPortrait` / `AodLandscape` / `AodMediaControls`, màu `Cyan` và `BlackAlpha30` (bóng kim), `FontFamilyGoogleSans` / `FontFamilyZalandoSansExpanded`; `make_static_fonts.py` thêm hai họ font (chỉ sinh hai họ đó, file font cũ không đổi byte).
- CLAUDE.md / README › Thêm chủ đề mới: màn ngang tự đánh giá riêng từng chủ đề cho cân, đầy màn (không rập khuôn thu nhỏ đồng hồ / chia cột), khối không đặt cân được thì bỏ hẳn; không nhồi khối / tuỳ chọn chỉ vì app hỗ trợ; font phải là file tĩnh đúng độ đậm (lỗi Xiaomi).

## [Chưa phát hành] – 2026-10-18 – Khung giờ và ngưỡng pin mặc định tắt

### Thay đổi

- "Chỉ hiện trong khung giờ" mặc định tắt (trước bật 07:00–23:00) và "Bỏ qua đồng hồ khi pin dưới" mặc định 0% (trước 15%): user vừa cài app ngoài khung giờ hay lúc pin yếu vẫn thấy AOD ngay. Ai đã lưu giá trị thì giữ nguyên.

## [Chưa phát hành] – 2026-10-18 – Kiểu mặc định bắt mắt cho mọi chủ đề, lưới chủ đề theo cấu hình đã lưu

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm. Chưa test trên máy.

### Thay đổi

- Mặc định của cả 7 chủ đề đổi sang kiểu bắt mắt nhất, mỗi chủ đề một màu nhấn để lưới không trùng: Cổ điển số xếp chồng, giờ mint, phút trắng; Sỏi giờ khối bánh quy đặc tím, phút khối hoa viền hồng, ngày lavender, nhạc hồng, nút phát bánh quy; Số áo giờ và ngày cam; Viên nang kiểu viền, chữ số / viên / nhạc xanh chanh (`Lime`, đo trên thẻ chủ đề trong lưới của app mẫu), ngày và dòng pin trắng; Thẻ giờ và thẻ xanh dương; Tháp kiểu nhạt dần xuống; Chuỗi viên thuốc và trình phát san hô (`Coral`). Chỉ đổi giá trị mặc định, phần vẽ giữ nguyên; thẻ chủ đề, màn chủ đề, Đặt lại và AOD sau khi áp dụng cùng một kiểu. Bỏ `Frost`, `GreyD0`, `BlueGreyB0` (không còn dùng).
- Lưới chủ đề ở tab AOD vẽ mỗi chủ đề theo cấu hình đã lưu (chưa chỉnh thì là mặc định), đúng như khi mở màn chủ đề; đọc lại mỗi lần tab AOD resume (`AodTabState.themeConfigs`).

## [Chưa phát hành] – 2026-10-17 – Chủ đề Chuỗi

Build được, lint không thêm cảnh báo, unit test pass (thêm test chặn chủ đề cũ đổi khối tự vẽ / nội dung thông báo), `tools/check-rules.sh` ra 0 vi phạm (38 rule), thư mục các chủ đề cũ không đổi dòng nào. Đã test trên máy.

### Thêm

- Chủ đề Chuỗi (Bullet List của Muviz Edge, số đo lấy từ layout `aug23_screen_layout`, `Aug23Clock` dịch ngược và thông số font trong APK; mock duyệt trước khi code): giờ khoét rỗng trong viên thuốc tô màu (thấy nền / ảnh nền qua chữ số; view rộng 100dp × Cỡ, chữ số 0,51 chiều cao view, viên thuốc rộng theo chữ, bo tròn hết), sát lề trái ở 0,13 chiều cao màn hình; dưới là chuỗi dòng có chấm 6dp nối nhau bằng vạch 1dp (dòng cuối không có vạch, dòng ẩn thì chuỗi tự nối lại có animation): ngày dài theo ngôn ngữ / thứ (thời tiết cạnh ngày), "PIN 72%" / trạng thái sạc, thông báo (icon, hay thông báo mới nhất: icon + tiêu đề + nội dung khi bật nội dung thông báo), ghi nhớ, sự kiện. Chữ số và tiêu đề in hoa Anton 18sp như app mẫu; dòng phụ Plus Jakarta Sans 13sp mờ 0,7 (Rubik của app mẫu không có tiếng Việt). Giờ đổi: viên thuốc co giãn có animation, ký tự đổi mờ chéo, ký tự giữ nguyên chỗ không nháy (`ChainGlyphs`). Trình phát riêng neo đáy: lùi · tên bài in hoa / nghệ sĩ · tới, chạm tên bài để phát / dừng, thanh tiến trình 1,5dp. Cỡ 70–130% (= 35–65 của app mẫu), màu đồng hồ, ngày, pin, thông báo, ghi nhớ, sự kiện, nhạc (mặc định #E0E0E0 như app mẫu).
- Ghi nhớ, sự kiện của Chuỗi do chủ đề tự vẽ (icon đặc sáng như icon thông báo, tiêu đề in hoa đậm như ngày / pin): `AodBlockValue.MEMO` / `EVENTS`, tab riêng ở màn chủ đề, Tùy chỉnh › Ghi chú / Sự kiện dẫn sang đó; xem trước riêng chủ đề có ghi nhớ và sự kiện mẫu. Không có nét vẽ (hình lớn không hợp dạng dòng). Màn ngang giữ dạng chuỗi: cụm đồng hồ + chuỗi ngày, pin, thông báo | chuỗi ghi nhớ, sự kiện | trình phát (`AodChainLandscape`).
- Font Anton (Google Fonts, OFL, có tiếng Việt; `res/font/anton_regular.ttf`, giấy phép `licenses/Anton-OFL.txt`). Anton chỉ có bản tĩnh nên `tools/fonts/make_static_fonts.py` chép từ `tools/fonts/static/`; script nhận tên họ font để chỉ sinh họ mới, các file font cũ không đổi byte.
- `tools/mockups/aod_landscape_mockups.py` có nhóm khung của Chuỗi (dải đồng hồ viên thuốc, bốn khung dọc, bốn khung ngang, chữ Anton).

### Thay đổi

- Chỉ thêm, không đổi hành vi chủ đề đang có: `AodBlockValue` thêm `MEMO`, `EVENTS` (chỉ Chuỗi khai báo; `AodComponentValue.EVENTS` / `MEMO` trỏ tới chúng nên chỉ chủ đề tự vẽ mới dẫn sang màn chủ đề); `AodThemeSpec.hasNotificationContent` mặc định `STATUS !in ownBlocks` như điều kiện cũ (dùng ở `AodNotificationOptionsUiModel.shownIn` và công tắc nội dung thông báo ở Tùy chỉnh), Chuỗi đặt `true`; `toThemePreviewDraft` bật sự kiện mẫu / ghi nhớ mẫu chỉ khi chủ đề tự vẽ chúng (`CustomizeDraftUiModel.isThemePreview`). Thêm nhánh Chuỗi vào `AodPortrait`, `AodLandscape`, `AodMediaControls`.

## [Chưa phát hành] – 2026-10-17 – Chủ đề Tháp

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Đã test trên máy.

### Thêm

- Chủ đề Tháp (Long HyperOS của Muviz Edge, số đo lấy từ layout, code dịch ngược và thông số font trong APK; mock duyệt trước khi code): bốn chữ số cao 0,96 cạnh ngắn màn hình, rất hẹp (Big Shoulders Display Black nén ngang 0,312, `TowerGlyphs`), một hàng sát đỉnh màn hình; ngày in hoa giãn chữ 0,48 em dưới số; trình phát riêng không có nút phát (chạm tên bài để phát / dừng như app mẫu, đang dừng thì tên bài mờ đi), thanh tiến trình mảnh; pin + icon thông báo một dòng (`AodStatusLine`) neo đáy phần 0,8 của khoảng dưới ngày như layout mẫu. Sáu kiểu số như app mẫu: viền (mặc định), viền nhạt dần xuống / lên, đặc, giờ đặc phút viền, giờ viền phút đặc; cỡ 80–110%, màu giờ, phút, ngày, nhạc, dòng pin (app mẫu gộp nhạc và pin thành một màu điều khiển).
- Khối dùng chung hợp với bố cục: thời tiết (cạnh ngày khi không có sự kiện), sự kiện, ghi nhớ nằm trên trình phát, nhiều khối thì hàng số nhỏ lại; không có nét vẽ (khối lớn tranh chỗ với hàng số cao) và thông báo mới nhất (dòng trạng thái chỉ có icon). Màn ngang dựng trên bố cục chung (`AodLandscapeLayout`).
- `tools/mockups/aod_landscape_mockups.py` có nhóm khung của Tháp (sáu kiểu, bốn khung dọc, bốn khung ngang).

### Thay đổi

- Chỉ thêm nhánh Tháp vào các chỗ chọn theo chủ đề (`AodPortrait`, `AodLandscape`, `AodMediaControls`); code của các chủ đề đang có không đổi.

## [Chưa phát hành] – 2026-10-17 – Nút xoay khung thành nút nổi, thanh tab màn sửa như Main

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Thay đổi

- Nút xoay khung của Tùy chỉnh và màn chủ đề không còn chiếm thanh tab / thanh trên: thành nút tròn nổi 48dp ở cạnh phải, cách panel / thanh tab (màn chủ đề lúc xem: banner) 24dp (`AppEditorFab`, nền `Neutral12` viền mảnh, đang xem ngang thì icon mint), mở panel thì đi lên theo. `AppPreviewOrientationButton` vẽ bằng `AppEditorFab`. Bước hướng dẫn Xoay chỉ vào nút nổi.
- Nút hướng dẫn của Tùy chỉnh lên thanh trên, trước Bỏ qua / Áp dụng. Nút hoàn tác, làm lại, đặt lại và hướng dẫn 40dp (trước 48dp) để thanh trên lúc onboarding vẫn vừa màn 393dp.
- Thanh tab của Tùy chỉnh và màn chủ đề cao 56dp như thanh tab Main (trước 48dp), tab đang chọn dùng icon đặc như Main: thêm bản fill của 13 icon tab vào `tools/icons/icons.txt`, `AppEditorTab` tự đổi sang bản fill (`selectedIconRes`).

## [Chưa phát hành] – 2026-10-17 – Thanh trên, thanh tab thấp hơn để thấy AOD xem trước nhiều hơn

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Đã test trên máy.

### Thay đổi

- Tùy chỉnh và màn chủ đề bỏ thanh công cụ nổi giữa màn hình (che phần trên AOD xem trước): hoàn tác, làm lại, đặt lại lên thanh trên cạnh nút quay lại (`AppEditorHistoryButtons`), Áp dụng / Lưu / Tùy chỉnh / Sửa thành nút gọn 40dp ở cuối thanh trên (`AppPrimaryButton` / `AppTextButton` có `isCompact`). Tùy chỉnh không còn tiêu đề; lúc onboarding thanh trên có Bỏ qua trước Áp dụng và bỏ ô bước (không đủ chỗ ở máy 360dp). Màn chủ đề lúc xem giữ tên chủ đề, "Đang dùng" thành dòng nhỏ màu mint dưới tên thay cho nhãn. `AppTopBar` có `isTitleCentered = false` cho thanh trên kiểu này. Phần AOD thấy được (màn 873dp, có banner) tăng từ khoảng 560dp lên 650dp.
- Thanh tab của Tùy chỉnh và màn chủ đề cao 48dp (trước 72dp): `AppEditorTab` chỉ có icon 20dp, tab đang chọn nền mint nhạt và hiện tên (giãn ra có animation), TalkBack luôn đọc tên. Nút xoay khung và nút hướng dẫn của Tùy chỉnh chuyển xuống cuối thanh tab, đứng yên khi cuộn tab; bước hướng dẫn Xoay chỉ vào đó, bước cuối chỉ vào thanh trên.
- Thanh tab Main thay `NavigationBar` 80dp của Material bằng thanh 56dp cùng kiểu (`AppEditorTab`: chỉ icon, tab đang chọn icon đặc và có tên).

## [Chưa phát hành] – 2026-10-17 – Thẻ "Giao diện của bạn" đủ thông tin hơn, thanh công cụ thoáng hơn, toast giữa màn hình

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Đã test trên máy.

### Thay đổi

- Thẻ "Giao diện của bạn" ở tab AOD bớt trống: ảnh AOD thu nhỏ cao 112dp (trước 64dp), tên chủ đề ghi "Chủ đề …", thêm hàng icon các khối đang bật theo thứ tự tab ở Tùy chỉnh (icon của tab, ô 28dp, không đủ chỗ thì ô cuối "+N") và dòng tóm tắt số khối + nền ("7 khối · Ảnh nền Sao", "Chỉ đồng hồ · Nền đen"; tên nền viết hoa chữ đầu như tên ảnh nền có sẵn); nút bút chì vẫn giữa thẻ theo chiều dọc. Khối tính theo cài đặt (khối cần quyền chỉ tính khi có quyền, như bản nháp của Tùy chỉnh), `CustomizeDraftUiModel.enabledBlockTabs()`. TalkBack đọc cả thẻ một lần, bỏ qua hàng icon.
- Thanh công cụ nổi của Tùy chỉnh và màn chủ đề (hoàn tác, làm lại, đặt lại · Áp dụng / Lưu / Tùy chỉnh / Sửa) có đệm 8dp mọi phía (trước chỉ 4–6dp hai bên, không đệm trên dưới) nên nút không còn sát mép viên.
- Toast của Tùy chỉnh và màn chủ đề hiện giữa màn hình thay cho dưới thanh trên.
- Tiêu đề mục chủ đề ở tab AOD: "Chủ đề & kiểu dáng" / "Themes & styles" (trước "Chủ đề" / "Themes").

## [Chưa phát hành] – 2026-10-17 – Chủ đề Thẻ, lập danh mục chủ đề từ app mẫu

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Đã test trên máy.

### Thêm

- Chủ đề Thẻ (`AodThemeValue.CARD`, id `card`; M3 Expressive của Muviz Edge): số giờ rất cao và hẹp bên trái, phút nhỏ bằng nửa trên ô AM/PM (24 giờ: "HRS", luôn tiếng Anh như app mẫu) kéo tới chân số giờ, cả cụm trong thẻ bo góc; ngày in hoa đậm dưới thẻ, trình phát riêng (nút phát tròn đặc 48dp, thanh gợn sóng lúc đang phát), pin + icon thông báo một dòng dưới cùng; đủ các khối dùng chung (giữa ngày và nhạc, nhiều khối thì thẻ nhỏ lại cho vừa màn hình), màn ngang dựng trên `AodLandscapeLayout`. Tuỳ chọn: kiểu số (đặc / viền), nền thẻ (nền mờ / viền / không), cỡ 80–120%, màu giờ, màu phút + AM/PM, màu thẻ riêng với màu giờ (tô nền mờ hoặc nét viền theo kiểu nền; mặc định trùng màu giờ như app mẫu), màu ngày, màu nhạc, màu dòng pin + thông báo.
- Số đo của Thẻ (`CardMetrics`) lấy từ layout XML, code dịch ngược và thông số chữ số của font trong APK app mẫu, không từ ảnh chụp; chữ số và AM/PM là Big Shoulders Display Black (có sẵn) nén ngang 0,55 cho khớp tỉ lệ font mẫu (`CardGlyphs`), chữ số trong ô rộng cố định, đổi số thì mờ chéo; đồng hồ luôn trái sang phải kể cả ngôn ngữ RTL.
- `tools/reference/`: kéo APK app mẫu đang cài trên máy bằng REA, dịch ngược (engine JADX của REA), dump resource bằng `aapt2`, lập `index.md` chỉ ra class chủ đề và file tra cứu; bộ trích danh mục `extract_muviz.py` làm mẫu cho app khác. Danh mục 44 chủ đề AOD của Muviz Edge, đối chiếu chủ đề đã clone: `plans/muviz-edge-themes.md`.
- `tools/mockups/aod_landscape_mockups.py`: nhóm khung Thẻ (4 dọc, 4 ngang).

### Thay đổi

- Thanh tiến trình gợn sóng của Sỏi tách thành `AodWaveTrack` (`components/aod/theme/`) dùng chung với Thẻ; thông số của Sỏi giữ nguyên.
- README › Thêm chủ đề mới: app mẫu dịch ngược được thì lấy số đo từ layout / code / font trong APK, không xin chụp ảnh nữa.

## [Chưa phát hành] – 2026-10-17 – Ô Cài đặt nhanh: icon app, xám khi thiếu quyền

Build được, lint không thêm cảnh báo, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Thay đổi

- Icon ô Cài đặt nhanh (`AodTileService`) đổi sang `ic_tile_aod`: hình đơn sắc của icon app cắt sát quanh điện thoại cho khung 24dp, sinh cùng icon app bằng `tools/brand/generate_launcher_icon.py`.
- Còn thiếu quyền bắt buộc thì ô ở trạng thái không dùng được (xám, không chạm được) với dòng phụ "Cần cấp quyền" theo ngôn ngữ chọn trong app; quyền đọc lại mỗi lần mở bảng Cài đặt nhanh.

## [Chưa phát hành] – 2026-10-14 – Banner ở đáy các màn

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Thêm

- 6 placement banner trong `AdsManager` (`by lazy`, id `BuildConfig.BANNER_<PLACEMENT>_ALL_ID` khai báo như `INTER_SPLASH_ALL_ID` ở cả release lẫn debug, đều là test id banner của Google): ngôn ngữ (chỉ onboarding), Tùy chỉnh lúc onboarding, quyền, 3 tab chính (một banner chung, dưới thanh tab), màn chủ đề (cả lúc xem lẫn lúc sửa), Tùy chỉnh mở từ tab AOD.
- `AppBottomAdSlot`: chỗ banner dưới thanh dưới, tự chừa thanh điều hướng của hệ thống, `isHidden` trượt xuống khi nhấn giữ xem toàn màn hình mà không huỷ banner. `AppOnboardingFooter` có slot `ad` dưới nút chính. Content của Ngôn ngữ, Quyền, Tùy chỉnh, màn chủ đề nhận slot `bannerAd` từ Screen.

### Thay đổi

- Thanh tab của Tùy chỉnh, màn chủ đề và thanh tab chính không tự chừa thanh điều hướng nữa (banner bên dưới chừa).

## [Chưa phát hành] – 2026-10-14 – Tab Pin & thông báo ở màn chủ đề Số áo, Viên nang; Tùy chỉnh mở thẳng tab của khối

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Sửa

- Số áo và Viên nang tự vẽ dòng pin + icon thông báo, Tùy chỉnh › Pin / Thông báo dẫn sang màn chủ đề, nhưng màn chủ đề không có tab cho phần này (màu đi chung màu phút / màu ngày). Thêm tab "Pin & thông báo" với màu riêng (`status_color`, mặc định giữ đúng màu cũ: `GreyED` cho Số áo, `GreyE0` cho Viên nang). README › Thêm chủ đề mới ghi thêm: mỗi khối trong `ownBlocks` phải có tab ở màn chủ đề.
- Tùy chỉnh › tab của khối chủ đề tự vẽ (Ngày, Pin, Thông báo, Nhạc) › "Kiểu do chủ đề quyết định" mở thẳng chế độ sửa của màn chủ đề, panel mở sẵn tab chỉnh khối đó, cho cả Sỏi (Ngày, Nhạc), Số áo (Ngày, Pin & thông báo) và Viên nang (cả bốn); Cổ điển không tự vẽ khối nào nên không đổi. `AodThemeTab.block` ghi tab chỉnh khối nào, `ThemeEditorDestination.focusBlock` mang khối cần mở; màn chủ đề đang nằm dưới Tùy chỉnh thì được thay bằng màn mới mở sẵn tab. Test mới `AodThemeSpecTest`: mỗi khối tự vẽ có đúng một tab.

## [Chưa phát hành] – 2026-10-14 – Chủ đề Viên nang

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Thêm

- Chủ đề Viên nang (`capsule`, clone từ ảnh mẫu `reference/theme_3`, mock dọc + ngang đã duyệt): giờ:phút Plus Jakarta Sans ExtraBold trong viên nang (đặc: màu viên pha nhạt 12%, viền: nét 0,043 R), mỗi chữ số một ô rộng cố định, hai chấm tròn; ngày in hoa đậm; trình phát riêng (nút phát tròn đặc, thanh tiến trình dày tách đôi); pin + icon thông báo một dòng dưới cùng. Màn dọc: viên nang → ngày → khối thêm → nhạc → dòng pin, cả cột giữa màn hình; màn ngang dùng bố cục chung, hero là viên nang + ngày (thời tiết cạnh ngày) + dòng pin. Tuỳ chọn: kiểu viên, cỡ 60–150%, màu chữ số, màu viên, màu ngày, màu nhạc. Tỉ lệ đo trên ảnh mẫu ở `CapsuleMetrics` (R = chiều cao chữ số).
- Font `plus_jakarta_sans_extrabold.ttf` (sinh riêng bằng `tools/fonts/make_static_fonts.py`, thêm độ đậm 800). Màu `Frost`, `GreyD0`, `GreyE0`. Chuỗi `theme_capsule`, `theme_color_digits`, `theme_color_capsule`.
- `tools/mockups/aod_landscape_mockups.py`: nhóm khung Viên nang, có thêm khung dọc 393 × 873 (`frame_portrait`).

### Thay đổi

- Dòng pin + icon thông báo của Số áo tách thành khối dùng chung `block/AodStatusLine` (cỡ theo chiều cao icon pin, font và `gapScale` do chủ đề truyền vào); Số áo không đổi hình.

## [Chưa phát hành] – 2026-10-14 – Ảnh nền không xoay theo màn ngang, sheet không còn nút X

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Thay đổi

- Ảnh nền AOD (có sẵn hay ảnh từ máy) đứng yên theo chiều dọc của máy như hình nền điện thoại: AOD nằm ngang (đồng hồ đêm, tự xoay) hay khung xem trước xoay ngang ở Tùy chỉnh / màn chủ đề thì chỉ đồng hồ và các khối đổi bố cục, ảnh không xoay theo. `AodBackdrop` vẽ ảnh trong khung cỡ màn dọc rồi xoay ngược góc màn hình. Góc đọc lại mỗi khi cửa sổ đổi hướng và khi màn hình báo đổi (`DisplayListener`, thêm một lần sau 500 ms cho lật ngang 180°), chỉ lấy góc thật của màn hình khi nó khớp hướng cửa sổ đang vẽ, chưa khớp thì theo cấu hình của cửa sổ: đọc `view.display.rotation` ngay lúc màn hình báo đổi thì cửa sổ chưa nhận hướng mới, góc trễ một nhịp (xoay ngang nền vẫn xoay theo, xoay về dọc nền vẫn ngang). Khung xem trước truyền -90°. Bản thu nhỏ và khung xem trước dọc không nghe xoay màn hình.
- Đầu sheet nhập / chọn (`AppSheetToolHeader`) bỏ nút X; sheet đóng bằng nút chữ "Hủy" dưới cùng như các sheet quyết định. Thêm "Hủy" cho sheet chọn thao tác, chọn nguồn điện và bảng vẽ (Ghi nhớ, Khung giờ đã có). Bỏ chuỗi `action_close`.

## [Chưa phát hành] – 2026-10-14 – Bỏ viền sáng của bottom sheet

Build được, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Sửa

- `AppSheet` không còn viền sáng: viền gắn vào `modifier` của `ModalBottomSheet`, mà modifier này nằm ngoài offset của sheet, nên viền vẽ ở mép trên màn hình (bo hai góc như viền app) thay vì mép sheet. Bỏ hẳn `glowTopEdge`, sheet chỉ còn thanh kéo dải màu. Các chỗ khác có viền (toast, hộp mất mạng) vẽ trên chính thẻ của chúng nên không bị.

## [Chưa phát hành] – 2026-10-14 – Bỏ qua ở màn quyền onboarding

Build được, lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Thêm

- Màn quyền onboarding có nút Bỏ qua ở thanh trên (như bước Tùy chỉnh, chỉ hiện khi còn thiếu quyền). Bỏ qua thì vào Main với `MainDestination(isPermissionSkipped = true)`: tab AOD không tự hiện sheet quyền trong phiên đó (sheet bắt đầu ở trạng thái đã đóng, còn dòng cảnh báo dưới công tắc), quyền chỉ bị đòi lúc Áp dụng ở Tùy chỉnh / màn chủ đề hay lúc bật AOD. Vào Main theo đường khác (mở app, Bỏ qua ở Tùy chỉnh) thì sheet hiện như cũ.

### Thay đổi

- `MainDestination` thành data class (`isPermissionSkipped`), `AodTabViewModel` nhận cờ này qua Koin. Chuỗi `customize_skip` đổi thành `action_skip` (dùng chung cho Tùy chỉnh và màn quyền).

## [Chưa phát hành] – 2026-10-14 – Overlay riêng của app thay cho Material, onboarding dẫn từng bước cấp quyền

Build được (`assembleDebug`), lint không thêm cảnh báo, unit test pass, `tools/check-rules.sh` ra 0 vi phạm (38 rule). Chưa test trên máy.

### Thêm

- Bộ overlay riêng (`components/sheet/`, `components/`): `AppSheet` (bottom sheet nền Neutral12, mép trên và thanh kéo là viền sáng mint → xanh → tím như icon app, giữ nội dung trong lúc trượt xuống, tắt kéo cho sheet có thao tác kéo), `AppSheetHeroHeader` / `AppSheetToolHeader`, `AppConfirmSheet`, nút `AppPrimaryButton` / `AppDangerButton` / `AppTextButton` (việc chính mint trên cùng, mất dữ liệu đỏ nhạt, Hủy bằng chữ), `AppOptionRow` + `AppOptionIcon`, `AppTimeWheel` (bánh xe giờ : phút, SA / CH, cuộn vòng, tự dừng đúng số), `AppToastHost` + `ToastUiModel` (toast trong app, lỗi viền đỏ), `AppIllustration`.
- Hình minh hoạ riêng của app: `tools/brand/generate_illustrations.py` sinh `img_spot_phone` / `img_spot_phone_offline` (điện thoại viền sáng của icon app, khung 120 x 96), huy hiệu theo ngữ cảnh vẽ chồng trong Compose.
- Màn chủ đề, Back khi còn thay đổi: thêm "Lưu rồi thoát" (cùng Bỏ thay đổi, Tiếp tục sửa). Ghi nhớ có 3 gợi ý nhanh. Bảng vẽ có "Hoàn tác nét".
- Icon Phosphor mới cho lựa chọn thao tác (`AodActionValue.iconRes`), nguồn điện (`ChargingRuleValue.iconRes`), bảng vẽ, huy hiệu; `ScheduleTimeValue.descriptionRes`.
- Check mới trong `tools/check-rules.sh`: không gọi thẳng AlertDialog / ModalBottomSheet / Button / TextButton / RadioButton / TimePicker / showToast.

### Thay đổi

- Mọi hộp thoại / sheet đổi sang bộ mới: đặt lại chủ đề (tab AOD), đặt lại Tùy chỉnh, đặt lại / bỏ thay đổi ở màn chủ đề, chọn thao tác và nguồn điện, chọn giờ (bánh xe thay đồng hồ Material), ghi nhớ và hình vẽ (`AodExtrasSheets`), sheet quyền (`AppPermissionSheet` dùng `AppPermissionSteps`: tiến độ "đã cấp" xếp dọc trên thanh chia đoạn, hàng có số thứ tự, quyền kế tiếp viền mint, nút "Cấp quyền tiếp theo"), hộp mất mạng (hình điện thoại xám), lớp phủ đang áp dụng (animation viền sáng thay vòng xoay).
- Toast hệ thống ở Tùy chỉnh và màn chủ đề thay bằng toast trong app (`ShowMessage(isError)`), nằm dưới thanh trên để không che panel.
- Onboarding: màn quyền có hình minh hoạ và các bước như sheet quyền; nút dưới không còn mờ khi thiếu quyền mà thành "Cấp quyền tiếp theo" (mở đúng quyền còn thiếu, kèm "Còn N quyền…"), đủ thì "Bắt đầu". Màn ngôn ngữ dùng `AppOptionRow` thay RadioButton; `AppOnboardingFooter` dùng nút mới (`hint` thay `disabledHint`). Nút Áp dụng / Sửa / Bỏ qua / Mở / Cho phép ở các thanh đổi sang nút mới.
- Bỏ `AppPermissionCard`, `AppRadioRow`, `AodExtrasDialogs`, `ic_arrow_right`, chuỗi `permission_missing_hint`, `action_ok` (không còn dùng).

## [Chưa phát hành] – 2026-10-13 – Chủ đề Số áo, bố cục ngang theo khối đang hiện, Sỏi bỏ chọn font, nhấn giữ để xem toàn màn hình, chia thư mục, AOD đứng yên giữa màn hình

Build được (`assembleDebug`), lint không thêm cảnh báo, `tools/check-rules.sh` ra 0 vi phạm (37 rule). Chưa test trên máy.

### Thêm

- Chủ đề Số áo (Jersey, clone từ ảnh app mẫu): giờ và phút hai hàng chữ số rất cao chỉ có nét viền, ngày "tháng / ngày" xếp dọc sát mép phải với thứ in hoa xoay dọc trong viên thuốc viền, dòng "pin • icon thông báo" dưới đồng hồ. Tuỳ chọn: cỡ 50–100% (100% to như app mẫu), màu giờ, màu phút (cả dòng pin), màu ngày; chia tab Đồng hồ / Ngày. Chữ số là Big Shoulders Display Black (thêm file tĩnh `big_shoulders_display_black.ttf` sinh từ font variable có sẵn), lấy đường viền chữ bằng `Paint.getTextPath` rồi kéo cao path trước khi vẽ nét nên nét dày đều; kích thước theo chỗ trống, màn ngang ngày đứng cạnh số. Màu mới `Lavender`.
- Chủ đề khai báo khối dùng chung nó có (`AodThemeSpec.components`, mặc định tất cả): khối bố cục của chủ đề không có chỗ thì Tùy chỉnh ẩn tab, AOD không vẽ và AOD thật không đọc lịch, tải thời tiết, theo dõi nhạc (`shownIn`); cài đặt của các khối đó vẫn giữ cho chủ đề khác. Khối tự vẽ mới `AodBlockValue.STATUS` (dòng pin + icon thông báo trong bố cục của chủ đề): tab Pin / Thông báo dẫn sang màn chủ đề, tab Thông báo ẩn công tắc nội dung.
- Hướng dẫn ở Tùy chỉnh thêm hai bước sau bước đầu: nhấn giữ khung xem trước để xem toàn màn hình, và nút xoay ngang (vòng sáng quanh nút).
- Nút xoay ngang ở thanh trên Tùy chỉnh và màn chủ đề (`AppPreviewOrientationButton`, icon `device-rotate`; `isLandscapePreview` trong state hai màn): khung xem trước đổi AOD sang bố cục ngang ở cỡ đầy đủ (rộng = chiều cao màn hình) rồi xoay 90° cho vừa khít màn hình dọc, màn hình app và các thanh vẫn dọc; user tự xoay máy sang ngang để xem đúng chiều (`AodPreviewFrame`). Bản đầu thu nhỏ AOD vào một khung ngang giữa màn dọc nên chi tiết quá nhỏ và không giống lúc khóa máy.
- Nhấn giữ khung xem trước ở Tùy chỉnh và màn chủ đề: rung nhẹ, ẩn thanh trên, thanh công cụ, panel và thanh tab để xem AOD toàn màn hình, thả tay thì hiện lại. Extension mới `Modifier.onLongClick` (nhấn giữ, thả tay, chạm nhanh) ở `AppModifiers`.
- README › Thêm chủ đề mới: các bước clone chủ đề từ ảnh mẫu và những chỗ dễ vướng.
- Bố cục ngang theo các khối đang hiện, dùng chung cho mọi chủ đề: `AodLandscapePlan` (thuần Kotlin, chọn từ `AodState.shownPanelBlocks()`) + `AodLandscapeLayout` (`components/aod/AodLandscapeLayout.kt`). Không có khối nào: đồng hồ giữa màn hình (thời tiết riêng lẻ đứng cạnh ngày); chỉ có nhạc: đồng hồ | trình phát; thời tiết + sự kiện, ghi nhớ, thông báo mới nhất: đồng hồ | cột thông tin (thời tiết lên đầu cột); nét vẽ: đứng cạnh cột thông tin; nhạc cùng nhóm khác: ba cột (thông tin hoặc nét vẽ | đồng hồ đúng giữa màn hình | nét vẽ + nhạc, `AodThreeColumns`). Đồng hồ + ngày + icon thông báo và pin một dòng là `AodHeroDetails`. Bật / tắt khối hay nhạc phát / dừng thì đổi bố cục bằng mờ chéo (`Crossfade` theo plan). Mỗi khối vẽ qua `AodPanelBlock` (cũng dùng cho `AodDetails` màn dọc) nên giữ kiểu / font / cỡ / màu chọn ở Tùy chỉnh.
- Cổ điển dùng nguyên bố cục chung; Sỏi giữ kiểu kẹp giữa khi chỉ có nhạc (khối giờ trái, khối phút phải, giữa là ngày, trình phát Sỏi, pin), không có khối nào thì hai khối to giữa màn hình, có khối khác thì hai cột với nhạc xếp dưới (không ba cột vì hai khối hình đã rộng). `AodPebbleClock` nhận cỡ khối (`tileSize`), `PebbleTimeTile` / `PebbleDate` tách ra dùng lại; chữ số trong khối theo tỉ lệ khối (dp) thay vì sp cố định nên cỡ chữ hệ thống lớn không làm tràn khối.
- Số áo màn ngang viết lại: một cột giữa màn hình, ngày nằm ngang như màn dọc nhưng chữ đứng thẳng ("13 / 10" + viên thuốc thứ, cỡ như màn dọc) → bốn chữ số một hàng, giờ và phút cách 0,3 R → pin + icon thông báo → các khối thêm xếp cạnh nhau (`FlowRow`). Tắt ngày thì số to ra.
- Số áo có đủ khối dùng chung (thời tiết, sự kiện, ghi nhớ, nét vẽ, nhạc), kiểu / font / cỡ / màu chỉnh ở Tùy chỉnh như Cổ điển: màn dọc xếp dưới dòng trạng thái, màn ngang xếp cạnh nhau dưới dòng trạng thái; hàng số nhỏ lại cho cả cột vừa màn hình (đo khối thêm trước bằng `SubcomposeLayout`), ngày và dòng pin giữ cỡ. Thông báo mới nhất vẫn không có (dòng trạng thái chỉ có icon).
- `tools/mockups/aod_landscape_mockups.py`: mock bố cục ngang (khung 873 × 393 dp, font thật) để chốt với user trước khi code; README › Thêm chủ đề mới › Bố cục ngang ghi cách chọn bố cục và cách nối chủ đề mới.
- Unit test đầu tiên của project: `AodLandscapePlanTest` (chọn bố cục ngang theo khối).

### Thay đổi

- Sỏi bỏ tuỳ chọn Font: chữ số, ngày và trình phát luôn dùng font gốc của chủ đề (`PebbleThemeSpec.Font`); khoá "font" đã lưu bị bỏ qua.
- `components/aod` chia thư mục: gốc ghép màn AOD (`AodContent`, `AodDetails`, `AodBackdrop`, `AodThumbnail`, `AodPreview`), `block/` khối dùng chung, `theme/` chọn theo chủ đề (`AodClockFace`, `AodThemeStatus`, `AodShapePath`) + `theme/classic/`, `theme/pebble/`, `theme/jersey/`, `editor/` control chỉnh. Đồng hồ Cổ điển tách khỏi `AodClockFace` sang `theme/classic/AodClassicClock.kt`.
- Khung xem trước ở Tùy chỉnh không còn nhận thao tác của AOD (`isInteractive = false`), chạm và nhấn giữ đi qua một lớp chung.
- Chia thêm thư mục: `model/aod/theme/` (gốc là hệ chủ đề; `classic/`, `pebble/` gồm cả `AodShapeValue` và các lựa chọn của Sỏi, `jersey/` là spec từng chủ đề; `component/` là kiểu khối dùng chung), `components/editor/` (`AppEditorPanel`, `AppEditorTab`, `AppApplyingOverlay`), `model/aod/settings/` (`interaction/`: thao tác, hành động; `rules/`: quy tắc sạc, khung giờ). Chỉ đổi package và import, không đổi code.
- AOD thật không còn dịch cả cụm sang vị trí ngẫu nhiên mỗi phút (bỏ `shiftXDp` / `shiftYDp`): cụm đứng yên giữa màn hình đúng như khung xem trước của chủ đề. Đổi lại đồng hồ đứng yên nên để lâu trên OLED dễ lưu ảnh hơn (README › Giới hạn đã biết).

### Sửa

- Áp dụng ở Tùy chỉnh (mở từ tab AOD) và màn chủ đề khi còn thiếu quyền bắt buộc vẫn ghi luôn, AOD không hiện được mà không ai nhắc (sheet quyền ở tab AOD đã đóng một lần thì không tự hiện lại; comment "lúc áp dụng cũng gọi showPermissionSheet()" chưa từng được nối). Giờ Áp dụng kiểm `requiredAodPermissions()` trước: thiếu thì chưa ghi gì, hiện sheet "Cấp quyền để áp dụng" ngay tại màn (`AppPermissionSheet`, mở trang cấp từng quyền như tab AOD); quay lại màn mà đã đủ thì sheet đóng và tự áp dụng; Để sau thì không áp dụng. Onboarding giữ như cũ (thiếu quyền thì sang màn quyền).

- Số áo: chữ phần trăm pin (và "+N") ở dòng trạng thái bị cắt đáy: hàng cao cố định bằng icon pin (0,046 R, khoảng 13dp) còn chữ có chiều cao dòng tự nhiên 1,26 lần cỡ chữ (khoảng 15dp), bị ép vào khung thấp hơn mình. Hàng giờ cao tối thiểu bằng icon pin (`heightIn`) và chữ chốt chiều cao dòng bằng cỡ chữ (`StatusText`). Chỉ ảnh hưởng Số áo, cả khung xem trước lẫn AOD thật vì dùng chung một composable; Cổ điển và Sỏi dùng `AodStatus` ở `block/` không có hàng cao cố định chứa chữ.

- Số áo lệch tỉ lệ so với app mẫu (đo trên ảnh mẫu 858 × 1908 bằng PIL, so với ảnh chụp trên máy): chữ ngày lớn hơn mẫu 37%, viên thuốc thứ lớn hơn 18%, dòng pin + icon lớn hơn 15–40%, số 1 hẹp hơn 15% (font có cờ vát, mẫu cờ vuông), khoảng cách hai hàng rộng hơn 40%. Viết lại theo số đo của mẫu: mọi kích thước theo R (chiều cao hàng số) trong `JerseyMetrics`, số 1 tự vẽ cờ vuông (`JerseyGlyphs`), cỡ chữ ngày 4,81% R, dòng trạng thái theo R (`AodJerseyStatus(unit)`).
- Số áo bị đẩy trái khi hiện ngày (bản căn giữa cả cụm "số + ngày") và hàng số co giãn theo nét chữ (bản ô theo nét): quay lại đúng bố cục ảnh mẫu, mỗi số một ô rộng cố định (0,4006 R) và hai hàng nằm giữa màn hình; ngày treo bên phải trong `Layout` nhưng không tính vào vị trí số, bật / tắt ngày số đứng yên.
- Bố cục dọc theo chủ đề (`AodPortrait`) thay cho `AodClockFace`: Số áo tự vẽ cả cụm (số, ngày, dòng trạng thái) trong một `Layout` nên không còn phụ thuộc kích thước do `Column` chia lại; `AodThemeStatus` và `AodDetails` bỏ nhánh trạng thái của chủ đề.

- Số áo: chữ số nằm giữa ô rộng cố định nên số 1 hẹp làm hàng "12" lệch phải, "21" lệch trái. Giờ mỗi số một ô rộng đúng nét (co giãn dần khi số đổi), khoảng cách giữa hai số cố định, hai hàng cùng nằm giữa một trục; trục đặt sao cho hàng giờ + ngày là một cụm nằm giữa màn hình (`Layout` riêng), nên phút đổi chỉ làm số phút trượt lại cho cân. Bỏ cách dịch cả cụm theo phần trống của số chục (`sideSpace`).
- Số áo trên máy thật trông lệch phải (lề trái rộng hơn lề phải nhiều): ngày bị ghim sát mép phải còn số căn giữa riêng, và số 1 hẹp nằm giữa ô rộng cố định để trống bên trái. Giờ số và ngày là một cụm căn giữa (ngày ngay bên phải hàng giờ), cụm dịch sang trái nửa phần trống bên trái của số hàng chục (`JerseyGlyphs.sideSpace`) để phần nhìn thấy nằm giữa; chỉ đổi khi số hàng chục đổi, trượt 500 ms cùng lúc số mờ chéo.
- Cụm đồng hồ + thông tin bị lệch lên ở mọi chủ đề (rõ nhất ở Số áo): cuối khối thông tin có `Spacer(28dp)` và dòng "Chạm 2 lần để thoát" vẫn chiếm chỗ khi đã mờ. Dòng gợi ý chuyển ra ghim đáy màn hình (`AodContent`), bỏ khoảng trống đó, nên cả AOD thật lẫn khung xem trước đều căn giữa.

## [Chưa phát hành] – 2026-10-12 – Màn chủ đề chia tab theo phần như Tùy chỉnh

Build được (`assembleDebug`), lint không thêm cảnh báo, `tools/check-rules.sh` ra 0 vi phạm (37 rule). Chưa test trên máy.

### Thay đổi

- Tab của màn chủ đề chia theo phần như khối ở Tùy chỉnh, mỗi tab có kiểu rồi màu của chính phần đó, bỏ tab Màu gom chung. Sỏi: Giờ (hình, kiểu, màu khối, màu số), Phút (tương tự), Chữ số (font, cỡ, xếp chồng), Ngày (kiểu, màu), Nhạc (nút phát, thanh tiến trình, màu), Hiệu ứng. Cổ điển: một tab Đồng hồ (mặt, font, cỡ, màu giờ / phút), cũng là tab Đồng hồ lúc onboarding (`ClassicThemeSpec.OnboardingTab` thành `ClockTab`). Khoá cấu hình không đổi nên cấu hình đã lưu giữ nguyên.
- Đặt lại riêng từng tab ở màn chủ đề (`ThemeEditorIntent.Option.ResetTab`, một bước hoàn tác), giống khối ở Tùy chỉnh. Ở cả hai màn, nút này giờ là icon (mũi tên xoay, không chữ) bên phải tên tab ở thanh tiêu đề panel, chỉ hiện khi tab đã khác mặc định (`AppEditorPanel(action)` + `AppEditorResetButton`), thay cho dòng "Đặt lại mặc định" cuối danh sách (bỏ `StyleResetRow`).
- Bỏ chuỗi `theme_tab_font`, `theme_tab_color` và icon `palette` không còn dùng.
- Tab AOD: cả tab là một `LazyVerticalGrid` 3 cột thay cho `Column` + `verticalScroll` (lưới lazy không lồng được trong cột cuộn). Phần đầu (công tắc, cảnh báo quyền, thẻ "Giao diện của bạn") và tiêu đề "Chủ đề" chiếm cả hàng, mỗi chủ đề một ô có `key` / `contentType` (`LazyGridScope.aodTabThemesSection` thay cho composable `AodTabThemesSection` chia hàng bằng `chunked(2)`). Ảnh chủ đề rộng đúng bằng ô và cao theo tỉ lệ màn hình thay cho cao cố định 240dp.
- Lưới chủ đề không còn ô Cổ điển (chủ đề mặc định). Tiêu đề "Chủ đề" có icon đặt lại (chỉ hiện khi đang dùng chủ đề khác), bấm thì hỏi bằng hộp thoại rồi đổi chủ đề đang dùng về Cổ điển; kiểu đã lưu của từng chủ đề giữ nguyên (`AodTabIntent.ResetTheme`). Sửa Cổ điển khi đang dùng: Tùy chỉnh › Đồng hồ › Chủ đề.

## [Chưa phát hành] – 2026-10-12 – Tự quay về app sau khi cấp quyền trên Xiaomi

Build được (`assembleDebug`), lint không thêm cảnh báo, `tools/check-rules.sh` ra 0 vi phạm (37 rule). Chưa test trên máy.

### Sửa

- Xiaomi không tự quay về app sau khi bật "Hiển thị trên ứng dụng khác", "Hiển thị trên màn hình khóa"... (chỉ "Hiển thị cửa sổ bật lên khi chạy nền" quay về được): HyperOS chặn `startActivity` từ nền tới khi có quyền cửa sổ bật lên, và bản cũ chỉ thử một lần. Trang cấp quyền giờ mở bằng `startActivityForResult` (`startSettingsPage`), cấp xong thì `returnAppWhen` đóng trang bằng `finishActivity` (không cần quyền mở từ nền); app vẫn chưa lên trước thì `startActivity` với `CLEAR_TOP | SINGLE_TOP` (bỏ trang cài đặt nằm trên, không tạo lại `MainActivity`) và thử lại mỗi 500 ms tới khi app lên trước hoặc hết 60 giây.
- Bỏ `REORDER_TO_FRONT`: trang cài đặt cùng task bị đẩy xuống dưới chứ không đóng, Back ở app lại về trang cài đặt.
- User tự quay về app trước khi cấp quyền thì thôi chờ, không kéo app lên sau đó; vòng chờ chạy trên scope của process thay vì của Activity.
- Tùy chỉnh: từ chối quyền lịch / vị trí (bật Sự kiện hôm nay / Thời tiết) trước đây chỉ hiện một dòng nhắc, từ chối hai lần là không cấp lại được từ app. Giờ mở Thông tin ứng dụng như Cài đặt › Quyền (`CustomizeEffect.Permission.OpenAppSettings`), cấp xong app tự về và bật luôn mục đang chờ; bỏ chuỗi `calendar_permission_denied`, `location_permission_denied`.

## [Chưa phát hành] – 2026-10-12 – Kiểu hiển thị từng khối, ô xem trước, khung xem trước đứng yên, màn đang áp dụng ở màn chủ đề

Build được (`assembleDebug`), lint không thêm cảnh báo, `tools/check-rules.sh` ra 0 vi phạm (37 rule). Chưa test trên máy.

### Thêm

- Kiểu hiển thị cho từng khối dùng chung (như mặt đồng hồ, ý tưởng lấy từ skill ui-ux-pro-max hướng OLED: nền đen, không viền, chữ dịu): Ngày 5 kiểu (Giãn chữ, Một câu, Tờ lịch, Hai cột, Số), Pin 5 (Biểu tượng, Vòng, Thanh, Chấm, Chữ), Thời tiết 4 (Một dòng, Nhiệt độ, Xếp chồng, Chi tiết), Sự kiện 4 (Danh sách, Dòng thời gian, Sắp tới, Một dòng), Ghi nhớ 4 (Biểu tượng, Trích dẫn, Chữ trơn, Nhãn), Thông báo 4 (Hàng icon, Xếp lớp, Chuông, Chấm), Nhạc 3 (Hàng ngang, Căn giữa, Gọn). Enum `Aod*LayoutValue`, thuộc tính `<khối>_layout` đứng đầu tab của khối; `AodDate` tách thành file riêng.
- Ô xem trước ở danh sách lựa chọn (`aodChoicePreview`, `PreviewChoice`): kiểu từng khối vẽ bằng chính composable của AOD với dữ liệu mẫu ở font / màu / cỡ mặc định (đổi font, màu chỉ thấy ở khung xem trước AOD, ô không vẽ lại); font hiện "Aa 12" và tên bằng chính font đó; mặt đồng hồ Cổ điển là đồng hồ thu nhỏ 08:30 màu mặc định (`AodClassicFacePreview`).
- Màn chủ đề có lớp phủ "Đang áp dụng…" lúc Lưu / Áp dụng (chặn chạm và Back), chờ DataStore ghi xong: `edit` của `DataStoreManager` trả `Job`, `saveAodThemeId` / `saveAodThemeConfig` trả `Job` để chờ (ghi vẫn chạy trên scope của manager). Lớp phủ dùng chung: `CustomizeApplyingOverlay` thành `AppApplyingOverlay`.
- Chạm vào khung xem trước khi panel đang mở thì đóng panel (Tùy chỉnh và màn chủ đề lúc Sửa).
- Icon `bell` bản light cho kiểu thông báo "Chuông".

### Thay đổi

- Lưới chủ đề ở tab AOD: chủ đề đang dùng có nhãn "Đang dùng" (`AppBadge`) trên ảnh thay cho dấu tích.
- Khung xem trước ngoài AOD thật (Tùy chỉnh, màn chủ đề, thẻ "Giao diện của bạn", lưới chủ đề, ô xem trước) không chạy theo giờ nữa: bỏ vòng cập nhật mỗi phút và `nowMillis` khỏi state của 3 màn, giờ mẫu cố định 08:30 hôm nay như icon app (`CustomizePreviewSamples.nowMillis`), các khối khác vẫn là dữ liệu mẫu cố định.
- Nút ở màn chủ đề đang dùng: "Tùy chỉnh thêm" thành "Tùy chỉnh" (dùng lại `customize_title`, bỏ `theme_customize_more`).

## [Chưa phát hành] – 2026-10-12 – Kiểu riêng từng khối ở Tùy chỉnh, bỏ viền, lối từ Tùy chỉnh sang màn chủ đề, font trên Xiaomi

Build được (`assembleDebug`), lint không thêm cảnh báo, `tools/check-rules.sh` ra 0 vi phạm (37 rule). Chưa test trên máy.

### Thêm

- Kiểu riêng từng khối dùng chung (`AodComponentSpec`, `AodComponentValue`: ngày, pin, thời tiết, sự kiện, ghi nhớ, thông báo, nhạc, nét vẽ): font (trừ nét vẽ), cỡ 60–150%, màu; chỉnh ở từng tab của Tùy chỉnh bằng đúng control của màn chủ đề (`AodThemeOptionList`), có xem trước theo tay, hoàn tác, đặt lại. Lưu ở khoá DataStore mới `aod_component_styles` (chuỗi `khoá=giá trị,...`, cùng cách mã hoá với cấu hình chủ đề, `toAodConfigValues` / `toAodConfigString`), nạp vào `AodAppearanceUiModel.components`; `AodThemeSpec` tách `AodOptionSpec` (danh sách tab + thuộc tính) để dùng chung `isSame` / `isDefault`.
- Khối mà chủ đề đang dùng tự vẽ (`ownBlocks`: ngày, nhạc của Sỏi) thì tab đó ở Tùy chỉnh chỉ có công tắc kèm dòng "Kiểu do chủ đề quyết định" dẫn sang màn chủ đề.
- Lối từ Tùy chỉnh sang màn chủ đề: tab Đồng hồ (luôn có) ngoài onboarding là dòng "Chủ đề: <tên>" mở màn chủ đề đang dùng; màn chủ đề đã nằm dưới trong back stack thì quay lại nó thay vì mở chồng, và "Tùy chỉnh thêm" ở màn chủ đề làm tương tự với Tùy chỉnh. Tùy chỉnh nạp lại chủ đề mỗi lần resume (bản nháp, lịch sử hoàn tác và bản gốc cùng lấy chủ đề mới) để Áp dụng không ghi đè chủ đề vừa đổi.
- Cổ điển: màu giờ và màu phút riêng (số: phần trước dấu ":" và từ dấu ":" trở đi; xếp chồng: hai dòng; kim: kim giờ + mốc giờ + chấm giữa, kim phút). Màu giờ giữ khoá `color` nên cấu hình đã lưu không mất; `minute_color` mới, cấu hình cũ chưa có thì phút về màu mặc định (bản cũ chuyển từ `aod_clock_color` thì cả hai cùng màu).

### Thay đổi

- Bỏ mọi viền của các khối trên AOD (thẻ, viên, vòng icon, vòng quanh nút phát, vạch sự kiện): khối không viền, không nền; thanh tiến trình nhạc, chấm của ngày và pin theo màu của khối; pin sạc dùng Mint. `AodCard` / `AodPill` / `AodIconRing` thay bằng `AodBlock` (khối nhiều dòng, cùng bề ngang); bỏ `AccentGradient` và `RoundedCornerShape20dp` vì không còn chỗ dùng.
- Ngày, thời tiết, ghi nhớ, nét vẽ không còn lấy font / màu từ chủ đề (bỏ `AodThemeSpec.fontOf` / `accentOf`, `AodAppearanceUiModel.font` / `color`): mỗi khối có kiểu riêng, mặc định font mặc định và màu xám dịu.
- Tab Đồng hồ ở Tùy chỉnh không còn `isOnboardingOnly`: onboarding vẫn chỉ có Cổ điển (mặt rồi font, cỡ, màu giờ / phút theo cùng thứ tự với các khối khác), sau đó là dòng dẫn sang màn chủ đề.
- Mỗi khối ở Tùy chỉnh (và khối Đồng hồ lúc onboarding) có dòng "Đặt lại mặc định", chỉ hiện khi khối đã khác mặc định, đặt lại riêng khối đó (`AodThemeConfig.without`, `AodThemeTab.isDefault`); nút Đặt lại trên thanh công cụ vẫn đặt lại cả bản nháp.
- Khung xem trước của Tùy chỉnh luôn hiện khối của tab đang mở bằng dữ liệu mẫu (`shownComponent` trong `toPreviewAodState`), kể cả khi khối đang tắt, thiếu quyền hay chưa có nội dung (ghi nhớ mẫu, hình vẽ mẫu): trước đó đổi font / cỡ / màu của thời tiết, sự kiện, ghi nhớ, thông báo... không thấy gì vì khối không hiện.
- Font không áp dụng trên máy Xiaomi (máy ảo vẫn đúng): font variable + `Font(resId, variationSettings)` của Compose đặt độ đậm qua `Paint.setFontVariationSettings` rồi lấy lại typeface của `Paint`, mà MIUI / HyperOS can thiệp `Paint` nên font của app bị thay bằng font hệ thống. Đổi sang file TTF tĩnh từng độ đậm (như Fredoka trong app tham khảo): `tools/fonts/make_static_fonts.py` (fonttools) tách 33 file từ 7 font variable (chuyển sang `tools/fonts/variable/`, không còn trong `res/font`), `Typography.kt` khai báo `Font(R.font.x_bold, FontWeight.Bold)`, bỏ `FontVariation` / `variationSettings`. APK nặng thêm khoảng 2–3 MB; dải độ đậm giữ như cũ (mảnh → đậm, độ đậm ngoài dải của font lấy gần nhất). Thêm rule 37 ở `tools/check-rules.sh`: không dùng `FontVariation` / `variationSettings`.
- Nhãn cỡ của khối Clock và chủ đề Sỏi là "Cỡ: x%" như các khối khác (bỏ chuỗi "Cỡ đồng hồ").
- Bánh xe màu (`ClockColorPicker`) không còn giữ cú vuốt dọc của panel: lớp phủ nhận cử chỉ thay bánh xe, chạm đúng nút chọn rồi kéo thì chọn màu, chạm nhẹ chỗ khác thì chọn màu ở đó, vuốt thì panel cuộn. Thanh độ sáng chỉ nhận vuốt ngang nên giữ nguyên.

## [Chưa phát hành] – 2026-10-11 – Lưới chủ đề dùng kiểu mặc định, chạm xem trước để sửa

Build được (`assembleDebug`), `tools/check-rules.sh` ra 0 vi phạm (36 rule). Chưa test trên máy.

### Thay đổi

- Lưới chủ đề ở tab AOD chỉ xem trước kiểu mặc định của từng chủ đề, không theo cấu hình user đã chỉnh (`AodTabState.themes` và việc nạp cấu hình ở `AodTabViewModel` bỏ). Cấu hình đã lưu chỉ hiện khi vào màn chủ đề.
- Màn chủ đề: chạm vào khung xem trước (lúc chưa sửa) thì mở phần chỉnh, như nút Sửa.

## [Chưa phát hành] – 2026-10-11 – Lưới chủ đề chỉ xem trước riêng chủ đề

Build được (`assembleDebug`), lint không thêm cảnh báo, `tools/check-rules.sh` ra 0 vi phạm (36 rule). Chưa test trên máy.

### Thay đổi

- Lưới chủ đề ở tab AOD chỉ vẽ riêng chủ đề đó (đồng hồ, ngày và khối chủ đề tự vẽ, `AodThemeUiModel.toThemePreviewDraft()`), không còn hiện ảnh nền, thông báo, sự kiện, thời tiết, ghi nhớ, pin, nhạc... đã bật của user: các thẻ nhìn gọn, so sánh chủ đề dễ hơn. AOD đầy đủ chỉ còn ở thẻ "Giao diện của bạn" (Tùy chỉnh); khối chủ đề tự vẽ như trình phát nhạc của Sỏi vẫn có trong xem trước.
- Màn chủ đề cũng chỉ xem trước riêng chủ đề (đồng hồ và ngày), không còn nạp phần chung của user: `ThemeEditorViewModel` bỏ `loadCustomizeDraft`, `ThemeEditorState.base` và `RefreshBase`. Lúc Sửa không còn ép hiện ngày và trình phát mẫu ở mọi tab.
- Mỗi chủ đề khai báo khối nó tự vẽ trong `AodThemeSpec.ownBlocks` (`AodBlockValue`: ngày, nhạc; thay cho `isDateBuiltIn`); xem trước chủ đề (lưới và màn chủ đề) luôn hiện các khối đó, nên thẻ Sỏi có sẵn trình phát nhạc riêng còn Cổ điển (dùng trình phát chung) thì không. Chủ đề sau có khối riêng mới chỉ cần khai báo ở `ownBlocks` và thêm dữ liệu mẫu trong `toThemePreviewDraft`.

## [Chưa phát hành] – 2026-10-11 – Kiểu mặc định mới cho các khối AOD, màu riêng từng phần của Sỏi

Build được (`assembleDebug`), lint không thêm cảnh báo, `tools/check-rules.sh` ra 0 vi phạm (36 rule), đã test trên máy.

### Thay đổi

- Kiểu mặc định của các khối dùng chung trên AOD lấy nét gradient mint → xanh → tím của app làm điểm nhận diện (`AccentGradient`, không tô nền để AOD ít sáng):
  - Ngày: thứ in hoa theo màu chủ đề, chấm gradient, ngày tháng xám.
  - Thông báo: mỗi app một vòng tròn viền mảnh, app mới nhất viền gradient; thông báo mới nhất trong thẻ viền gradient (`AodCard`).
  - Pin và đèn pin gom thành một viên viền mảnh (`AodStatus`): pin tự vẽ theo mức, sạc thì tô gradient kèm tia sét, ≤ 15% thì đỏ.
  - Nhạc (Cổ điển): thẻ viền gradient, nút phát trong vòng gradient, thanh tiến trình gradient như thanh trượt của app.
  - Chữ của thông báo, sự kiện, pin, nhạc dùng font của app (Plus Jakarta Sans); ngày và ghi nhớ vẫn theo font chủ đề.
- Sỏi: màu riêng cho khối giờ, số giờ, khối phút, số phút, ngày và nhạc (thay cho màu khối / màu số chung và lựa chọn "Số trên khối đặc"). Màn sửa: tab có nhiều màu thì gom thành chip chọn phần (chấm màu hiện tại) + một bảng màu. Icon trên nút phát tự đen / trắng theo độ sáng màu nhạc.
- Lưới chủ đề ở tab AOD: bỏ khung thẻ, tên căn giữa; chủ đề đang dùng viền mint và có dấu tích phủ góc ảnh thay nhãn "Đang dùng".
- Sự kiện và ghi nhớ thành thẻ viền gradient có vòng icon (như thẻ thông báo), thời tiết thành viên viền mảnh cạnh ngày (như viên pin). Khung dùng chung: `AodCard`, `AodPill`, `AodIconRing`. Icon mới bản light: note-pencil (calendar-check light giữ lại cho thẻ sự kiện).
- Tùy chỉnh lúc onboarding có lại tab Đồng hồ (`CustomizeTabValue.isOnboardingOnly`) để user mới thấy hết app làm được gì: chỉ chủ đề Cổ điển như bản trước khi có chủ đề (mặt, font, màu, cỡ; `ClassicThemeSpec.OnboardingTab`), không chọn chủ đề; dùng đúng control của màn chủ đề (`AodThemeOptionList`, chuyển từ `screens/themeeditor/` sang `components/aod/`), Áp dụng thì lưu cấu hình. Mở từ tab AOD thì không có tab này và Đặt lại giữ chủ đề đang dùng. Hướng dẫn nhóm Giao diện đổi chữ theo.
- `AppSlider` chỉ hiển thị (`onProgressChange = null`: splash, thanh nhạc) không đi qua Slider của Material nữa (nó đọc `value` trong composition): `progress` gọi ở pha vẽ của track, thumb ở pha layout, nên tiến trình đổi chỉ vẽ lại. `AodMediaProgress` dùng chung cho thanh nhạc của mọi chủ đề.

### Xoá

- Lựa chọn "Số trên khối đặc" (`AodDigitToneValue`) và chuỗi của nó, chuỗi "Màu khối" / "Màu số" chung của Sỏi.

## [Chưa phát hành] – 2026-10-11 – Hệ chủ đề AOD, chủ đề Cổ điển và Sỏi (kèm trình phát nhạc riêng)

Build được (`assembleDebug`), lint không thêm cảnh báo, `tools/check-rules.sh` ra 0 vi phạm (36 rule), đã test trên máy.

### Thêm

- Hệ chủ đề (`presentation/model/aod/theme/`): mỗi chủ đề khai báo tab và thuộc tính sửa được (`AodThemeOption`: lựa chọn, màu, khoảng số, bật tắt), giá trị lưu thành Int theo khoá trong `AodThemeConfig`; phần vẽ chọn trong `AodClockFace`. Chủ đề đang dùng lưu ở `aod_theme_id`, cấu hình mọi chủ đề ở một chuỗi `aod_theme_configs`.
- Chủ đề Cổ điển: 4 mặt đồng hồ có từ trước (số, số xếp chồng, kim, kim tối giản) với font, màu, cỡ. Chưa lưu lần nào thì lấy mặt / font / màu / cỡ đã chọn ở bản trước, nên máy đang dùng không mất cài đặt.
- Chủ đề Sỏi: giờ và phút mỗi số một khối hình mềm, 8 hình (vuông bo, viên sỏi, lục giác, bốn cánh, tròn, bánh quy, hoa, sao), mỗi khối đặc hoặc viền; font; số trên khối đặc tối hoặc theo màu số; cỡ; xếp giờ trên phút; màu khối, màu số (color picker có sẵn); dòng ngày in hoa đậm hoặc thường; animation khi đổi phút: trượt, phóng hoặc nảy hình (hình co và đổi dần sang tròn rồi về). Hình là hàm bán kính quanh tâm nên đổi hình chỉ là nội suy; giá trị animation đọc ở pha vẽ.
- Trình phát nhạc riêng của Sỏi (`AodPebbleMedia`, chọn trong `AodMediaControls` theo chủ đề như `AodClockFace`): tên bài và nghệ sĩ theo font chủ đề, nút phát nằm trong khối hình màu khối (chọn 1 trong 8 hình, xoay khi phát / dừng, icon tối hoặc theo màu số như chữ số), thanh tiến trình gợn sóng lúc phát, duỗi thẳng lúc dừng, hoặc kiểu thẳng / ẩn (tab Nhạc). Vị trí bài lấy từ media session (vị trí, độ dài, tốc độ phát; `MediaProgress`) và nội suy mỗi giây trong `AppSlider` nên đồng hồ không recompose theo tiến trình; app nhạc không báo độ dài (radio, livestream) thì không có thanh.
- Màn chủ đề (`screens/themeeditor/`, `ThemeEditorDestination`) dùng chung cho mọi chủ đề: xem trước toàn màn với phần chung đang dùng (Sửa / Áp dụng; Áp dụng xong ở lại màn, thanh trên ghi "Đang dùng" và nút đổi thành "Tùy chỉnh thêm" mở Tùy chỉnh, quay về thì khung xem trước nạp lại phần chung), sửa theo tab của chủ đề (hoàn tác, làm lại, đặt lại, Lưu), Back khi còn thay đổi chưa lưu thì hỏi bỏ thay đổi.
- Tab AOD: lưới chủ đề thay phần "Chủ đề sắp có"; mỗi thẻ là AOD thu nhỏ của chủ đề đó, chủ đề đang dùng có nhãn "Đang dùng". Thẻ "Giao diện của bạn" ghi tên chủ đề đang dùng.
- Component dùng chung: `AodThumbnail` (AOD thu nhỏ, dùng ở thẻ "Giao diện của bạn" và thẻ chủ đề), `AppEditorPanel` và `AppEditorTab` (tấm chỉnh và tab, dùng ở Tùy chỉnh và màn chủ đề). Icon mới: timer, text-aa, palette, magic-wand, pencil-simple; play, pause, skip-back, skip-forward bản fill cho trình phát Sỏi. Màu `Grey3A` cho phần chưa phát của thanh nhạc.
- `AppSlider` nhận `track` để vẽ thanh riêng (thanh nhạc gợn sóng), mặc định vẫn là thanh gradient cũ.

### Thay đổi

- Tùy chỉnh bỏ tab Đồng hồ (mặt, font, màu, cỡ chuyển sang màn chủ đề); hướng dẫn nhóm Giao diện đổi chữ theo.
- Ngày, thời tiết, ghi chú, nét vẽ lấy font và màu theo chủ đề đang dùng; chủ đề tự vẽ dòng ngày (Sỏi) thì khối chung không vẽ lại.
- Màn chủ đề lúc Sửa luôn hiện dòng ngày và trình phát mẫu (kể cả khi đã tắt ở Tùy chỉnh) để tab Ngày / Nhạc thấy được thay đổi; bản xem trước có thanh tiến trình mẫu đứng yên.
- Font variable khai báo đủ độ đậm Thin → Bold: chữ số Sỏi in đậm được, mặt Số của Cổ điển với font Mặc định về đúng nét mảnh (trước đó bị vẽ ở độ đậm Normal).

### Xoá

- `saveAodClockFace` / `Font` / `Color` / `SizePercent` của DataStore (key cũ chỉ còn đọc để chuyển sang chủ đề Cổ điển), các intent và panel đồng hồ của Tùy chỉnh, chuỗi "Chủ đề sắp có" và "Mặc định" của thẻ giao diện.

## [Chưa phát hành] – 2026-10-11 – 12 ảnh nền AMOLED mới

Build được (`assembleDebug`), `tools/check-rules.sh` ra 0 vi phạm (36 rule), đã test trên máy.

### Thêm

- 12 ảnh nền AMOLED có sẵn (`WallpaperValue` mã 7–18, tổng cộng 18 ảnh): Mưa sao băng, Hành tinh vành đai, Quỹ đạo, Núi trăng, Đom đóm, Hoa anh đào, Sứa phát sáng, Thành phố đêm, Pháo hoa, Đường đồng mức, Sóng âm, Đèn trời. Thiết kế duyệt trên canvas rồi sinh bằng code; nền đen, màu theo bảng màu app, chừa vùng giữa cho đồng hồ, tránh logo / nhân vật / công trình có bản quyền. Mỗi ảnh có chuỗi tiếng Việt và tiếng Anh.
- `tools/wallpapers/generate_wallpapers.py`: sinh 12 file `res/drawable/img_wallpaper_*.xml` (VectorDrawable 1080×2400) từ các hàm vẽ, seed cố định nên chạy lại ra đúng file cũ; `--preview-svg <thư mục>` ghi thêm SVG để xem trong trình duyệt. Chạy: `python -I -X utf8 tools/wallpapers/generate_wallpapers.py`. Tổng thêm khoảng 280 KB XML (6 ảnh vẽ tay cũ giữ nguyên).

### Thay đổi

- Hàng chọn ảnh nền của Tùy chỉnh dùng `LazyRow` có key theo mã (chỉ dựng các ảnh đang thấy, không phân tích hết 18 vector một lượt) và tự cuộn tới ảnh đang chọn khi mở.

## [Chưa phát hành] – 2026-10-11 – Icon Phosphor, font Google Fonts, slider và color picker mới

Build được (`assembleDebug`), `tools/check-rules.sh` ra 0 vi phạm (36 rule), đã test trên máy.

### Thêm

- Icon Phosphor 2.1.1 (MIT) thay toàn bộ Material Icons: 74 VectorDrawable `res/drawable/ic_*.xml` sinh bằng `tools/icons/import_phosphor.py` từ `tools/icons/icons.txt` (ghim phiên bản, icon có hướng tự lật RTL). Độ đậm theo chỗ: regular cho thanh / nút, fill cho tab đang chọn, duotone cho ô icon ở Cài đặt và thẻ quyền, light trên màn AOD.
- Font Google Fonts (OFL, bản variable, có tiếng Việt): Plus Jakarta Sans cho toàn app thay Inter; font đồng hồ đóng gói sẵn thay họ font hệ thống: Mặc định (Plus Jakarta Sans), Có chân (Fraunces), Đơn cách (JetBrains Mono), Viết tay (Dancing Script) giữ mã 0–3, thêm Bo tròn (Comfortaa), Cao gầy (Big Shoulders Display), Hình học (Josefin Sans). App nặng thêm khoảng 0,5 MB.
- `licenses/`: giấy phép OFL của từng font và MIT của Phosphor.
- `AppSlider`: track gradient mint → xanh → tím, thumb tròn phóng to và có quầng khi nhấn, không chấm step. Nhận `progress: () -> Int`; giá trị chỉ đọc trong `snapshotFlow`, vị trí thumb ở pha layout, track và hiệu ứng nhấn ở pha vẽ, nên kéo không recompose. Thumb theo ngón tay bằng `SliderState` cục bộ, chỉ báo ra khi sang bước khác. `onProgressChange = null` là chế độ chỉ hiển thị (không nhận chạm, không mờ, bỏ vùng chạm 48dp); `isThumbVisible = false` ẩn thumb, track trải hết chiều ngang.
- Màu đồng hồ chọn tự do bằng `skydoves/colorpicker-compose` 1.3.0 (wheel HSV + thanh độ sáng) thay 8 ô màu cố định. Kéo thì xem trước trực tiếp, thả tay mới ghi một bước hoàn tác. Màu lưu dạng ARGB; mã 0–7 cũ vẫn đọc ra đúng màu nên máy đang dùng không mất lựa chọn.

### Thay đổi

- Splash: thanh tiến trình dùng `AppSlider` chỉ hiển thị thay `LinearProgressIndicator` chạy mãi, chạy từ 0 tới 99% trong 5 giây (`tween`, thang 0..1000 cho mượt) rồi giữ ở 99% tới khi chuyển màn, không bao giờ 100% vì splash còn chờ quảng cáo; không thumb; phần trăm hiện cùng hàng "Đang tải…", chỉ recompose khi sang số mới.
- Thanh trên của tab chuyển vào `topBar` của `Scaffold` trong `MainNavDisplay` (`MainTopBar` private, `TopAppBar` có inset thanh trạng thái, tiêu đề mờ dần theo tab). IAP và Share xử lý ngay ở `MainNavDisplay`; các tab không còn intent / effect `NavigateToIap` / `ShareApp` và không tự đệm thanh trạng thái. Bỏ `AppTabTopBar`.
- Tab Viền sáng quay về màn giữ chỗ không ViewModel (bỏ `EdgeLightViewModel` + `state/`, chỉ dùng cho IAP / Share).
- Nền `BackgroundGradient` vẽ một lần ở `AppNavDisplay`; màn, tab và `Scaffold` để trong suốt (trước đây mỗi màn tự vẽ). CLAUDE.md và check đổi theo.
- Quyền Xiaomi: mở trang Cài đặt cho quyền nào thì chỉ kéo app về khi đúng quyền đó được bật (effect `OpenMiuiPermissionSettings(permission)`), bỏ `areMiuiPermissionsGranted()` gộp hai quyền.
- `tools/check-rules.sh` thêm rule chặn `Slider` của Material và `Icons.*` / `ImageVector`.

### Xoá

- Dependency `material-icons-extended`, font `inter_variable.ttf`, `ClockColorValue` và chuỗi tên 8 màu.

## [Chưa phát hành] – 2026-10-11 – IAP / Share trên thanh tiêu đề tab, bỏ chế độ Xem thử

Build được (`assembleDebug`), `tools/check-rules.sh` ra 0 vi phạm, đã test trên máy.

### Thêm

- `AppTabTopBar` (`presentation/components/`): tiêu đề tab kèm icon IAP (mở `IapScreen`) và Share (bảng chia sẻ của hệ thống, kèm link Play Store), dùng ở cả 3 tab. Helper `shareText`, `playStoreUrl` ở `utils/ContextExt.kt`.
- `IapScreen` (`screens/iap/`, `IapDestination`): màn giữ chỗ cho Premium, chưa có ViewModel và Play Billing.
- `AppTopBar` (`presentation/components/`): thanh trên căn giữa, nền trong suốt, nút back tuỳ chọn, chữ và icon `GreyED`. Thay cho `CenterAlignedTopAppBar` viết tay ở `IapContent`, `LanguageContent`, `CustomizeTopBar`; `AppOnboardingTopBar` dựng trên nó (bỏ tham số `containerColor`). `tools/check-rules.sh` chặn gọi `TopAppBar` thẳng.
- `EdgeLightViewModel` + `state/` cho tab Viền sáng (state còn trống) để IAP / Share đi qua ViewModel như 2 tab kia.

### Thay đổi

- Thẻ "Giao diện của bạn": nút bút chì dùng `AppIconTile` như icon ở tab Cài đặt.
- Icon trên mọi thanh trên cùng màu `GreyED` (IAP bỏ màu cam, nút Hướng dẫn ở Customize bỏ `Grey9A`); mint chỉ cho trạng thái đang chọn / giá trị. CLAUDE.md ghi quy tắc này.
- Các tab không nhận `contentPadding` nữa: `MainNavDisplay` chỉ chừa phần dưới cho thanh tab, tab tự đệm thanh trạng thái (`statusBarsPadding()`) nên nền vẫn tràn dưới status bar. Tiêu đề tab chuyển sang `HeadlineSmall` để đủ chỗ cho 2 icon.
- README: các bước test dùng nút "Xem thử" đổi sang xem trước ở Tùy chỉnh rồi khoá máy.

### Xoá

- `AodActivity.preview()` và chế độ xem thử của AOD (`isPreview` ở `AodActivity`, `AodScreen`, `AodViewModel`, DI). AOD thật chạy như trước: luôn gắn vào `AodSession`, luôn hẹn giờ tối, theo cảm biến tiệm cận và kiểm quy tắc.

## [Chưa phát hành] – 2026-10-10 – Màn Main mới: 3 tab AOD / Viền sáng / Cài đặt

Build được (`compileDebugKotlin`, `assembleDebug`), `tools/check-rules.sh` ra 0 vi phạm, đã test trên máy.

### Thêm

- `MainNavDisplay` (theo mẫu `AppNavDisplay`) thay `MainScreen`: chỉ chứa `NavDisplay` của 3 tab và thanh tab dưới (M3 `NavigationBar`, tab đang chọn icon mint trên nền mint nhạt), không ViewModel. Tab AOD là gốc back stack tab: Back ở tab khác về tab AOD rồi mới thoát app, chuyển tab mờ dần. Destination tab ở `navigation/MainDestinations.kt`, thứ tự / icon / nhãn ở `MainTabValue`.
- Tab AOD (`screens/aodtab/`): công tắc "Hiện đồng hồ khi màn hình tắt" dạng thanh bo tròn; thẻ "Giao diện của bạn" vẽ AOD đang áp dụng thu nhỏ bằng chính `AodContent` (đồng hồ nhảy theo phút, mờ đi khi tắt AOD, chạm để mở Customize); lưới chủ đề giữ chỗ "Sắp có"; sheet và dòng cảnh báo khi thiếu quyền bắt buộc.
- Tab Viền sáng (`screens/edgelight/`): màn giữ chỗ dùng Lottie viền sáng của icon, nhắc viền sáng khi có thông báo đã có ở Tùy chỉnh › Hiệu ứng.
- Tab Cài đặt (`screens/settings/`): nhóm Hiển thị, Khi nào hiện, Thao tác và cảm biến, Quyền, Ứng dụng dạng thẻ có ô icon; giá trị màu mint nằm dưới tên; nguồn điện chọn trong hộp thoại; cuối trang có icon, tên và phiên bản app.
- Customize mở được từ tab AOD (`CustomizeDestination(isFirstOpen = false)`): thanh trên có nút quay lại và tiêu đề "Tùy chỉnh", không bước / Bỏ qua; Áp dụng xong quay về Main.
- Nền gradient chéo mint → đen → tím (`BackgroundGradient`) cho các tab, màn Ngôn ngữ và màn Quyền; mỗi màn tự vẽ nền. Màn AOD, Customize và Splash vẫn đen.
- `AppIconTile`, `AppBadge` (`presentation/components/`); `rememberAodPreview` (`components/aod/AodPreview.kt`) dùng chung cho Customize và thẻ giao diện; `loadCustomizeDraft` đọc cấu hình đang lưu thành bản nháp; `AodContent(isInteractive = false)` cho bản thu nhỏ (không lấy focus, không nhận chạm / phím).

### Thay đổi

- `MainViewModel` tách thành `AodTabViewModel` và `SettingsViewModel`, mỗi tab tự xử lý effect. Bỏ khỏi Main 3 nhóm Giao diện đồng hồ, Thông tin thêm, Thông báo (đã có ở Customize) và nút Xem thử; "Luôn xoay ngang" chuyển sang Cài đặt › Hiển thị.
- `CustomizePreviewSamples` chuyển từ `screens/customize/` sang `model/customize/`. `AodGestureValue` có thêm icon. `AppOnboardingTopBar` mặc định nền trong suốt. Hàm `screen<>` của `AppNavDisplay` thành public để `MainNavDisplay` dùng chung.
- CLAUDE.md thêm quy tắc `MainNavDisplay` / tab và nền gradient; `tools/check-rules.sh` thêm lệnh chặn vẽ nền ở `navigation/`. README cập nhật bảng file.

### Xoá

- `screens/main/` (Screen, Content, ViewModel, state, các section) và 14 chuỗi chỉ các section bị bỏ dùng (`section_appearance`, `section_extras`, `section_notifications`, `opt_timeout_value`, …).

## [Chưa phát hành] – 2026-10-10 – Gọn lại CLAUDE.md

Build được (`compileDebugKotlin`), `tools/check-rules.sh` ra 0 vi phạm. Chỉ đổi tài liệu và script, không đổi code app.

### Thay đổi

- CLAUDE.md từ 1617 dòng (222 KB) còn 237 dòng (18 KB). Chỉ giữ thứ code không tự nói: Hard rules, quy tắc import giữa các layer, quy ước MVI / theme / coroutines / Manager / Navigation / onboarding, ads. Bỏ cây thư mục, bảng method của Manager, danh sách key DataStore, mã mẫu, bảng "Dùng cái có sẵn", lịch sử thử rồi bỏ theme, mô tả giao diện từng màn và bảng phiên bản thư viện (đã có trong `gradle/libs.versions.toml`). Bản cũ còn trong lịch sử git (commit `fe6efd1`).
- Bỏ hẳn mục "AOD core" (39 bất biến và sơ đồ luồng) và không tạo `.claude/rules/`: lý do của từng điểm đã có trong comment ngay tại code (`AodService`, `AodActivity`, `AodSession`, `ScreenStateManager`, manifest) và README › Các quyết định thiết kế. CLAUDE.md chỉ giữ quy tắc không load ads trên `AodActivity` và một dòng trỏ tới hai nơi đó. Bản đầy đủ còn trong lịch sử git.
- 32 lệnh grep kiểm tra chuyển từ CLAUDE.md sang `tools/check-rules.sh`: chạy `bash tools/check-rules.sh`, in dòng vi phạm kèm cách sửa, thoát mã 1 nếu có vi phạm. CLAUDE.md, agent `compose-implementer` và các skill gọi script thay vì "grep mục 18". Thêm `.gitattributes` giữ `*.sh` ở LF, vì `core.autocrlf=true` trên Windows sẽ đổi sang CRLF làm script lỗi.
- Gỡ tham chiếu tới Android-Base, Lịch Việt, DexReader và kotlin-accelerator-ai: README (mục "Dựng từ Android-Base" đổi thành "Còn phải làm", bỏ hàng Onboarding vì đã có đủ 3 bước), COMPARISON, agent và skill.
- Sửa các số mục CLAUDE.md đã lỗi thời trong agent và skill (nay trỏ theo tên mục). Sửa vài chỗ cũ đi kèm: agent còn ghi dp / thời lượng phải lấy từ token, `ui-ux-pro-max` còn nhắc `presentation/theme/tokens/` và font mặc định của Material 3.

## [Chưa phát hành] – 2026-10-10 – `LocalTag` cho composable

Build được (`compileDebugKotlin`), các lệnh grep của CLAUDE.md ra 0 dòng.

### Thêm

- `LocalTag` và `AppTagProvider` (`components/AppTagProvider.kt`): tương đương interface `Tag` nhưng cho composable, đọc bằng `val tag = LocalTag.current` rồi `Timber.tag(tag)`.
- `AppNavDisplay` có hàm bọc `screen<XxxDestination> { … }` thay cho `entry<>`: tự cấp `LocalTag` = tên class của destination (`MainDestination`, `CustomizeDestination`…), nên màn mới không phải tự đặt tên tag. `AodActivity` cấp `"AodScreen"` vì `AodScreen` nằm ngoài NavDisplay.

### Thay đổi

- `MainScreen`, `CustomizeScreen`, `PermissionScreen`, `LanguageScreen`, `AodScreen` log lỗi effect bằng `Timber.tag(tag)` từ `LocalTag` thay cho 12 chuỗi tên Screen viết cứng.
- CLAUDE.md: thêm `LocalTag` vào bảng "Dùng cái có sẵn" và quy tắc log; quy tắc "file Compose không khai báo `val` cấp file" có ngoại lệ cho `LocalXxx` ở `presentation/components/`.
- Các mục CHANGELOG cũ còn ghi "chưa chạy trên máy" đổi thành đã test.

## [Chưa phát hành] – 2026-10-10 – Loading khi Áp dụng ở màn Customize

Build được (`compileDebugKotlin`), các lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thêm

- Bấm Áp dụng thì hiện lớp phủ loading toàn màn `CustomizeApplyingOverlay` (nền tối, vòng xoay, "
  Đang áp dụng…") trong suốt lúc `isApplying`: ghi cài đặt, lưu ảnh nền và hình vẽ, tải thời tiết
  đều là hàm suspend có thể mất vài giây. Lớp phủ chặn chạm; phím Back bị nuốt trong lúc đó (trước
  đây Back có thể thoát màn giữa chừng và huỷ việc ghi).

### Thay đổi

- `CustomizeViewModel.confirmCustomization` tắt `isApplying` sau khi gửi effect điều hướng, không
  phải trước, để lớp phủ không tắt trước lúc chuyển màn. Sang màn Permission thì Customize còn dưới
  stack nên vẫn tắt như cũ.
- CLAUDE.md mục 10.4 thêm quy tắc: trạng thái đang xử lý phải có loading ở UI.

## [Chưa phát hành] – 2026-10-10 – Bỏ qua màn Customize vào thẳng Main

Build được (`compileDebugKotlin`), các lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thay đổi

- Bấm Bỏ qua ở màn Customize (onboarding) giờ vào thẳng Main, không còn qua màn Permission khi thiếu
  quyền. Thiếu quyền bắt buộc thì Main hiện `MainPermissionSheet` như các lần mở sau. Bấm Áp dụng
  giữ nguyên: đủ quyền vào Main, thiếu thì sang màn Permission.
- Không cần đổi cờ lần đầu mở app: `saveIsFirstOpen(false)` đã gọi ở nút Tiếp tục của màn Language,
  nên bỏ qua màn Permission không làm onboarding chạy lại.
- CLAUDE.md ghi luồng mới; sửa chỗ còn ghi `saveIsFirstOpen(false)` "luôn ở bước cuối" và bảng
  onboarding thiếu bước Customize.

## [Chưa phát hành] – 2026-10-10 – Tự kéo app về sau khi cấp quyền ở app Cài đặt

Build được (`compileDebugKotlin`), các lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thêm

- Mọi trang cấp quyền mở từ app nay tự kéo app về khi quyền được cấp, dùng `returnAppWhen` có sẵn (
  như `openWifiSettings` với mạng): `openOverlaySettings`, `openNotificationSettings`,
  `openNotificationListenerSettings` (qua `AodNotificationListener.openAccessSettings`),
  `openMiuiPermissionSettings` (kéo về khi cả hai quyền của Xiaomi đã bật), `openAppSettings` (lịch,
  vị trí sau khi bị từ chối hai lần). Các hàm này nhận thêm `isGranted: () -> Boolean`.
- Quyền đã có sẵn lúc mở trang thì không kéo về: user vào để xem hoặc tắt, nếu kéo sau 1 giây sẽ
  giật họ ra khỏi trang.
- `PermissionManager.isGranted(permission)` và `areMiuiPermissionsGranted()` (trong
  `PermissionUiModel.kt`) làm điều kiện; quyền không đọc được trạng thái (op của Xiaomi bị đánh số
  lại) coi như không chặn, cùng cách với `hasRequiredPermissions`. `MainScreen`, `PermissionScreen`,
  `CustomizeScreen` `koinInject()` `PermissionManager` để truyền vào.

### Thay đổi

- `MainEffect.Permission.OpenAppSettings` từ `data object` thành
  `data class OpenAppSettings(permission)` để Screen biết đang chờ quyền nào.

## [Chưa phát hành] – 2026-10-10 – `Tag` nằm ở `BaseViewModel`

Không đổi hành vi. Build được (`compileDebugKotlin`), các lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thay đổi

- `BaseViewModel` implement `Tag`; `AodViewModel`, `CustomizeViewModel`, `LanguageViewModel`,
  `MainViewModel`, `PermissionViewModel` bỏ `Tag` và import của nó, vẫn dùng `Timber.tag(tag)` như
  cũ (`tag` là tên lớp con lúc chạy). Thêm lệnh grep vào checklist để ViewModel con không khai báo
  lại `Tag`.
- CLAUDE.md: mẫu ViewModel, bảng "Dùng cái có sẵn" và mục log ghi rõ ViewModel không cần implement
  `Tag`. Sửa nốt chỗ còn ghi `action` thay vì `block` cho `collectCatching`.

## [Chưa phát hành] – 2026-10-10 – Component dùng chung cấp app có tiền tố `App`

Không đổi hành vi. Build được (`compileDebugKotlin`), các lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thay đổi

- Component trong `presentation/components/` (dùng chung giữa các màn, không gắn với tính năng riêng
  của app, có thể dùng lại ở project khác) đặt tên `AppXxx`: `AppLottie`, `AppSectionHeader` /
  `AppSectionLabel` / `AppSwitchRow` / `AppRadioRow` / `AppValueRow` / `AppSliderRow`,
  `AppOnboardingTopBar` / `AppOnboardingHeader` / `AppOnboardingFooter`, `AppPermissionCard`,
  `AppPermissionSheet`, `AppNoInternetDialog`. File đổi theo (`AppLottie.kt`, `AppModifiers.kt`,
  `settings/AppSettingsRows.kt`, `onboarding/AppOnboarding.kt`, `permission/AppPermissionCard.kt`,
  `permission/AppPermissionSheet.kt`, `dialog/AppNoInternetDialog.kt`); cùng nhóm với
  `AppLanguageProvider` và `AppTimeFormat` có sẵn.
- Component riêng một màn (`screens/<màn>/components/`) giữ tên thường; phần dành riêng cho AOD (
  `components/aod/`) giữ tên `Aod…` hoặc tên theo nội dung. Quy tắc ghi ở CLAUDE.md mục 6; thêm lệnh
  kiểm tra tên component và tên file trong `presentation/components/` vào checklist.
- CLAUDE.md, README, agent `compose-implementer` và skill `compose-optimizer` cập nhật theo; sửa
  đường dẫn lệch trong bảng "Dùng cái có sẵn" và tên `AodTimeFormat` → `AppTimeFormat`.

## [Chưa phát hành] – 2026-10-10 – Screen collect effect theo cụm, qua `collectCatching`

Không đổi hành vi, trừ việc lỗi ném ra khi xử lý một effect giờ được log thay vì làm crash app.
Build được (`compileDebugKotlin`), 29 lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thay đổi

- `MainScreen` (5 khối) và `CustomizeScreen` (4 khối) thay một khối
  `LaunchedWithLifecycleEffect { effect.collect { when … } }` bằng mỗi cụm effect một khối
  `viewModel.effect.filterIsInstance<XxxEffect.<Cụm>>().collectCatching(…)`; `when` đủ nhánh của cụm
  nên không cần `else`. Cụm chỉ có một effect (`Appearance` / `Decor` mở bộ chọn ảnh) và
  `ShowMessage` lọc thẳng theo effect, không có `when`. `effect` là `SharedFlow` nên mọi khối đều
  nhận đủ effect.
- Mọi Screen (kể cả `AodScreen`, `LanguageScreen`, `PermissionScreen`, effect phẳng) collect effect
  bằng `collectCatching(block, catch)` có sẵn, `catch` log bằng `Timber.tag("<Screen>")`. Khối gặp
  lỗi dừng cho tới lần vào lại STARTED kế tiếp.
- CLAUDE.md: còn 1 chỗ collect thô (`snapshotFlow … collectLatest` trong `Modifier.onClick`); lệnh
  grep cấm `.collect {` bỏ ngoại lệ `.effect.collect {`. Mục 10.2.2 ghi cách collect effect theo
  cụm; agent `compose-implementer` cập nhật theo.
- Sửa docs lệch với code: tham số của `collectCatching`, `collectLatestCatching`,
  `withContextCatching` đã đổi từ `action` thành `block` ở commit `bd09bbd` nhưng CLAUDE.md và agent
  vẫn ghi `action =`.

## [Chưa phát hành] – 2026-10-10 – Theme bỏ object bọc ngoài

Không đổi hành vi. Build được (`compileDebugKotlin`), 29 lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thay đổi

- `Colors.kt`, `Typography.kt`, `Shapes.kt` không còn bọc trong `object`: mỗi giá trị là một `val`
  cấp file trong package `presentation.theme`. UI import từng tên rồi dùng thẳng (`Mint`,
  `BodyLarge`, `RoundedCornerShape16dp`), không còn `Colors.Mint` / `Typography.BodyLarge` /
  `Shapes.RoundedCornerShape16dp`.
- Cầu nối Material 3 đổi tên cho khỏi trùng nhau: `Typography.Material` → `MaterialTypography`,
  `Shapes.Material` → `MaterialShapes`, khai báo bằng tên đầy đủ
  `androidx.compose.material3.Typography` / `Shapes` nên không còn import alias.
- Lệnh grep cấm `.copy(` trên màu theme đổi sang danh sách tên màu theme (`Mint.copy(`,
  `Black.copy(`…) vì không còn tiền tố `Colors.`.
- CLAUDE.md, README, agent `compose-implementer` và skill `design-tokens` cập nhật theo.

## [Chưa phát hành] – 2026-10-10 – Object theme public

Không đổi hành vi. `Colors`, `Typography`, `Shapes` trong `presentation/theme/` từ `internal object`
thành `object` public, cùng quy tắc với composable: `private` hoặc public, không `internal`. Lệnh
grep `^internal ` trong checklist của CLAUDE.md ra 0 dòng. Đã test trên máy.

## [Chưa phát hành] – 2026-10-10 – Bỏ tiền tố `Aods` khỏi tên code

Không đổi hành vi. Build được (`compileDebugKotlin`), các lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thay đổi

- Component dùng chung đặt tên thường theo việc nó làm: `Lottie`, `OnboardingTopBar` /
  `OnboardingHeader` / `OnboardingFooter`, `PermissionCard`, `PermissionSheet`, `SectionHeader`,
  `SectionLabel`, `SwitchRow`, `RadioRow`, `ValueRow`, `SliderRow`, `NoInternetDialog`. File đổi
  theo (`components/settings/SettingsRows.kt`, `components/permission/PermissionSheet.kt`…).
- Theme bỏ tiền tố: `AodsColors` → `Colors`, `AodsTypography` → `Typography`, `AodsShapes` →
  `Shapes`, `AodsTheme` → `Theme`. Hai object trùng tên type của Material 3 (`Typography`, `Shapes`)
  nên file theme import type đó dưới alias `MaterialTypography` / `MaterialShapes`.
- Màn AOD giữ tên `Aod` (`AodScreen`, `AodContent`, `AodViewModel`…) vì đó là tên của tính năng,
  không phải tiền tố design system.
- CLAUDE.md, README và các file trong `.claude/` cập nhật theo; bỏ quy tắc `AodsXxx` ở bảng
  component. Thêm lệnh grep `Aods[A-Z]` vào checklist để tên cũ không quay lại. Lịch sử trong mục
  này và các mục trước giữ nguyên.

## [Chưa phát hành] – 2026-10-10 – Sắp xếp thư mục `presentation` theo tính năng

Không đổi hành vi. Build được (`compileDebugKotlin`), 28 lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thay đổi

- `presentation/model/` chia thư mục theo tính năng: `aod/settings`, `aod/appearance`, `aod/info`,
  `customize/`, `language/`, `permission/`, `onboarding/`; `AnimationContentKey` dùng chung để ở
  gốc.
- `presentation/components/` chỉ giữ thành phần dùng chung ≥ 2 màn, chia nhóm: `onboarding/`,
  `permission/`, `settings/`, `aod/` (khung hiển thị AOD `AodContent` và các phần của nó, dialog ghi
  nhớ / hình vẽ, chọn hình nền / màu đồng hồ — màn Customize dùng chung), `dialog/`.
- Thành phần chỉ một màn dùng nằm trong `screens/<màn>/components/`: `main/components/` (các
  `MainXxxSection`, `MainPermissionSheet`), `customize/components/` (toolbar, thanh tab, khung
  panel, guide, panel). Content vẫn giữ các phần đủ ngắn.
- Màn `customize` không import từ `screens/main/` nữa; `screens/aod/` không còn `AodContent`.
- Composable đổi từ `internal` sang `fun` public (hoặc `private`); không còn `internal fun` trong
  `app/`. Thêm vào checklist grep của CLAUDE.md.
- CLAUDE.md, README và agent `compose-implementer` cập nhật đường dẫn và quy tắc thành phần của màn.

## [Chưa phát hành] – 2026-10-10 – Màn chính chia Intent / Effect / State theo cụm, panel Customize như bottom sheet

Không đổi hành vi màn chính. Build được (`compileDebugKotlin`), 27 lệnh grep của CLAUDE.md ra 0
dòng, đã test trên máy.

### Thay đổi

- `MainIntent` (50 Intent phẳng) chia theo đúng các section của màn chính: `Options`, `Appearance`,
  `Extras`, `Notifications`, `Interaction`, `Rules`, `Permission`; `ToggleAod`,
  `NavigateToLanguage`, `OpenPreview` ở cấp cha. `MainEffect` chia `Service`, `Appearance`,
  `Permission`, `Navigation`, `ShowMessage` ở cấp cha. `MainViewModel.onIntent` giao cho
  `on<Cụm>Intent`.
- `MainState` từ 24 field còn 10: state con `MainAppearanceState`, `MainExtrasState`,
  `MainInteractionState`, `MainRulesState` gói UiModel đã lưu của cụm (`settings`) cùng UI tạm (
  dialog, đang lưu ảnh, cảm biến có / không, quyền lịch / vị trí); `MainPermissionState` giữ danh
  sách quyền và trạng thái đóng sheet. Mỗi `MainXxxSection` chỉ nhận state con của nó.
  `isEditingMemo` đổi tên thành `isMemoEditorVisible` cho giống màn Customize. CLAUDE.md mục 10.2.2
  và agent `compose-implementer` ghi thêm biến thể này.
- Panel phía trên thanh tab của màn Customize trượt lên / xuống như bottom sheet M3 (cùng thông số
  chuyển động với `ModalBottomSheet`), đổi tab thì chỉ nội dung mờ chuyển; bỏ nút thu xuống, tiêu đề
  căn giữa (đóng bằng cách bấm lại tab hoặc back). Xoá string `customize_close_panel`.

## [Chưa phát hành] – 2026-10-09 – Màn thử tùy chỉnh AOD trong onboarding

Thêm bước "Thử tùy chỉnh AOD" vào onboarding lần đầu: Language → Customize → Permission. Không thêm
quyền Manifest, không thêm thư viện. Build được (`compileDebugKotlin`), lint 0 lỗi (không có cảnh
báo mới), 27 lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thêm

- Màn Customize (`screens/customize/`, bước 2/3): khung xem trước toàn màn hình là chính
  `AodContent` dựng từ bản nháp, dữ liệu mẫu cho thông báo, sự kiện, thời tiết, nhạc, pin. Top bar
  trong suốt (back, chỉ báo bước, `?` xem lại hướng dẫn, nút chữ Bỏ qua); ngay dưới, căn giữa, là
  thanh công cụ hoàn tác / làm lại / đặt lại + nút Áp dụng nền mint (chỉ bấm được khi đã có thay
  đổi); thanh tab vuốt ngang 11 tab chia 4 cụm: Giao diện (đồng hồ, ngày, pin) · Trang trí (nền, vẽ,
  ghi chú) · Thông tin (thông báo, sự kiện, thời tiết, nhạc) · Hiệu ứng (viền sáng, có Xem thử). Bấm
  tab mở panel (trượt lên / xuống như bottom sheet M3, tiêu đề căn giữa), bấm lại tab hoặc back thì
  đóng.
- Mọi thay đổi là bản nháp (hoàn tác / làm lại tối đa 50 bước, đặt lại có hỏi xác nhận); Áp dụng chỉ
  ghi phần đã đổi rồi vào Main (đủ quyền bắt buộc) hoặc sang màn quyền. Ảnh của user giải mã giữ
  trong bộ nhớ, chỉ ghi file khi Áp dụng (`AodImageManager.decodeBackground` /
  `saveBackground(bitmap)`).
- Bật mục cần quyền (truy cập thông báo, lịch, vị trí) khi chưa có quyền thì hiện sheet xin đúng
  quyền đó, cấp xong mục mới bật.
- Hướng dẫn 6 bước tự hiện ở lần đầu vào màn: xem trước, 4 cụm tab (làm sáng dải tab của cụm, tự
  cuộn tới), thanh công cụ; bong bóng nằm trên hoặc dưới mục tiêu tuỳ chỗ trống, mũi nhọn chỉ vào
  mục tiêu; Bỏ qua hoặc xem hết thì không tự hiện lại (`isCustomizeGuideShown`).
- Tuỳ chọn mới cho AOD: hiện ngày, hiện phần trăm pin (`isAodDateEnabled`, `isAodBatteryEnabled`,
  mặc định bật), có ở màn chỉnh và mục "Thông tin thêm" ở màn chính.
- Component dùng chung `AodsPermissionSheet` (sheet xin quyền, ẩn có animation);
  `MainPermissionSheet` dùng lại nó. `AodsOnboardingTopBar` nhận thêm `containerColor` và `actions`.

### Thay đổi

- Viền LED chạy trong Lottie ở splash / icon động to hơn khoảng 1/3 (`LED_LAYERS`: nét 2.4 / 5.4 /
  9.0 thay cho 1.8 / 4.0 / 7.0, đơn vị khung 108); viền tĩnh của icon app giữ nguyên.
- Màn ngôn ngữ lần đầu luôn sang màn Customize (bỏ nhánh vào thẳng Main / màn quyền,
  `LanguageViewModel` không cần `PermissionManager`); màn quyền thành bước 3, back về Customize.
- Intent / Effect / State của màn Customize chia theo cụm (`CustomizeIntent.Appearance`, `.Decor`,
  `.Info`, `.Effects`, `.History`, `.Permission`, `.Guide`, `.Panel`; state con `history`, `decor`,
  `effects`; bản nháp chia `appearance` / `decor` / `info` / `effects`). CLAUDE.md thêm mục 10.2.2
  cho pattern này và ghi chú hiệu năng cho màn nặng (mục 14); agent `compose-implementer`, skill
  `compose-optimizer` / `unit-testing` cập nhật theo.

## [Chưa phát hành] – 2026-10-09 – UI đọc thẳng AodsColors / AodsTypography / AodsShapes

Commit `e743565` cho UI đọc theme qua `MaterialTheme.colorScheme / .typography / .shapes`; bản này
đổi lại cho UI đọc thẳng các object, tránh đường vòng. Build được (`compileDebugKotlin`), lint 0
lỗi (không có cảnh báo mới), 27 lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thay đổi

- UI (22 file) đọc thẳng `AodsColors.Mint`, `AodsTypography.BodyLarge`,
  `AodsShapes.RoundedCornerShape16dp`; màu có alpha dùng lại biến thể đặt tên sẵn (`MintAlpha12`,
  `RedAlpha12`, `WhiteAlpha30`, `BlackAlpha50`). Đọc được cả trong lambda vẽ / `onClick` nên bỏ các
  biến gán tạm.
- `AodsTypography` / `AodsShapes` có lại các `TextStyle` / `RoundedCornerShape<N>dp`, cộng
  `Material` cho cầu nối. `AodsTheme { }` chỉ bọc `MaterialTheme` cho component Material 3; bỏ
  `inverseSurface` / `scrim` thêm riêng cho UI.
- Bỏ `AodsAodTheme`: màn AOD lại không bọc theme (`AodActivity`, preview giữ nguyên như trước
  `e743565`). Chữ gợi ý trong bảng vẽ trở lại Grey6E như ban đầu.
- Bottom sheet quyền ở màn chính dùng cùng giao diện màn quyền: tiêu đề / mô tả của onboarding (
  `AodsOnboardingHeader`), các thẻ quyền (bấm thẻ là mở cài đặt của quyền đó) và một nút Để sau rộng
  hết, cao 56dp.
- `LaunchedEffect` không gọi hàm suspend đổi sang `SideEffect` có key (Compose runtime 1.12):
  `requestFocus()` ở `AodContent`, `onDarkChange` / `onDimChange` ở `AodScreen`, nạp quảng cáo /
  điều hướng ở `SplashScreen`. Giữ `LaunchedEffect` ở `Modifiers.onClick` (`collectLatest`,
  `animateTo`) và `MainPermissionSheet` (`sheetState.hide()`). CLAUDE.md mục 14, agent
  `compose-implementer`, skill `compose-optimizer` ghi quy tắc này.
- CLAUDE.md (mục 14, 18), agent `compose-implementer`, skill `design-tokens` / `compose-optimizer`,
  README cập nhật theo. Lệnh grep: cấm `MaterialTheme.*` ngoài `theme/`, cấm `.copy(` trên
  `AodsColors` ngoài `theme/`, cấm `AodsTheme.xxx`; bỏ lệnh cấm gọi thẳng các object ngoài `theme/`.

## [Chưa phát hành] – 2026-10-07 – Quyền bắt buộc, chỉnh UI onboarding, theme theo pattern Lich-Viet-Loc-Phat, script icon

Siết lại bộ quyền bắt buộc theo thử nghiệm trên máy Xiaomi, chỉnh giao diện splash / màn ngôn ngữ /
màn quyền, theme đổi sang pattern phẳng của Lich-Viet-Loc-Phat (bỏ tầng token), đưa script sinh
icon / Lottie vào repo. Không thêm quyền Manifest, không thêm thư viện. Bản này build được (
`compileDebugKotlin`), lint 0 lỗi (không có cảnh báo mới), 27 lệnh grep của CLAUDE.md ra 0 dòng,
đã test trên máy.

### Thêm

- `tools/brand/`: script Python sinh icon app (`generate_launcher_icon.py`) và Lottie của icon (
  `generate_edge_light_lottie.py`) từ cùng một bộ hình học (`brand_geometry.py`); đổi màu viền, tốc
  độ, độ dài vệt sáng… chỉ cần sửa thông số rồi chạy lại. File sinh ra giống hệt bản đang dùng (icon
  chỉ thêm dòng ghi chú "sinh bằng script").
- `PermissionValue` có đủ mọi quyền app dùng: thêm lịch và vị trí (không bắt buộc). Danh sách quyền
  ở màn chính hiện cả hai; bấm thì xin quyền (chỉ cấp, không tự bật "Sự kiện hôm nay" / "Thời
  tiết"), bị từ chối thì mở Thông tin ứng dụng.
- Màn quyền có nút back trên top bar để quay về màn ngôn ngữ.
- `AodsOnboardingTopBar`: top bar của onboarding, chỉ báo bước nằm giữa top bar (không cuộn theo nội
  dung nữa), kèm nút back khi cần.
- `NavBackStack.navigateBack()` (`utils/NavExt.kt`): quay lại nhưng không bao giờ pop màn cuối
  cùng (back stack rỗng thì NavDisplay crash), bấm back liên tiếp cũng an toàn. Dùng cho `onBack`
  của `AppNavDisplay` và nút back của Language / Permission; CLAUDE.md cấm gọi
  `backStack.removeLastOrNull()` thẳng (thêm lệnh grep thứ 25).

### Thay đổi

- Quyền bắt buộc: thêm Thông báo (thông báo của service giữ đồng hồ chạy nền) và, trên Xiaomi, "Cửa
  sổ bật lên khi chạy nền" (HyperOS chặn mở AOD từ nền khi thiếu). Màn quyền xin thông báo bằng hộp
  thoại hệ thống, bị từ chối thì mở trang cài đặt thông báo.
- Bỏ việc tự hỏi quyền thông báo khi vào màn chính (và cờ `is_notifications_asked`).
- Onboarding: bấm Tiếp tục ở Language lần đầu là xong onboarding; thoát ở màn quyền thì lần sau vào
  thẳng màn chính, thiếu quyền thì màn chính nhắc bằng bottom sheet.
- Bottom sheet quyền ở màn chính hiện mỗi khi thiếu quyền bắt buộc (không còn phụ thuộc công tắc
  AOD) và hiện lại khi bật AOD hay áp giao diện cho đồng hồ (mặt, font, màu, cỡ, xoay ngang, ảnh
  nền), kể cả khi đã đóng trước đó.
- Splash: Lottie và chữ chung một cột ở giữa màn hình, chữ mở dần ra làm cả cụm dồn lên giữa. Lottie
  thu nhỏ từ 288dp còn 200dp, bỏ placeholder.
- Tiêu đề màn ngôn ngữ và màn quyền căn giữa; chỉ báo bước chuyển từ nội dung lên top bar.
- Thẻ quyền gọn hơn (padding 12dp, icon 40dp, mô tả chữ nhỏ), nội dung căn giữa theo chiều dọc. Bên
  phải trong thẻ là nút tròn filled màu primary, cùng cỡ với icon quyền: mũi tên khi chưa cấp, dấu
  tích khi đã cấp. Quyền không kiểm tra được vẫn có dòng nhắc trong thẻ, bấm thẻ để mở Cài đặt (bỏ
  nút "Mở cài đặt" riêng).
- Thẻ quyền bấm được cả thẻ khi quyền chưa cấp (trước chỉ bấm được nút); nút tròn bên phải giờ chỉ
  để báo trạng thái.
- Bottom sheet quyền: tiêu đề và mô tả căn giữa; nút "Để sau" rộng hết chiều ngang, chữ căn giữa,
  cao bằng nút chính của onboarding (chữ vốn cùng cỡ `labelLarge` với nút đó).
- Theme theo pattern của Lich-Viet-Loc-Phat cho đơn giản: `presentation/theme/` phẳng, mỗi file một
  `internal object` cùng tên — `AodsColors` (màu, biến thể alpha `MintAlpha12`, `RedAlpha12`,
  `WhiteAlpha30`, `BlackAlpha50`), `AodsTypography` (font Inter, các `TextStyle`, `Material` cho cầu
  nối), `AodsShapes` (`RoundedCornerShape8dp` / `12dp` / `16dp`). UI đọc qua
  `AodsTheme.colors / .typography / .shapes` (object trỏ thẳng tới các object trên, không đổi tên,
  không `CompositionLocal`), không gọi thẳng các object ngoài package `theme`; hàm `AodsTheme { }`
  chỉ bọc `MaterialTheme`. Khoảng cách, cỡ icon, viền, kích thước, `sp`, thời lượng, alpha viết số
  thẳng (`16.dp`, `14.sp`, `tween(durationMillis = 600)`), hình tròn dùng `CircleShape`. Bỏ thư mục
  `tokens/` cùng mọi tầng token: semantic (`AodsColorTokens`, `AodsSpacingTokens`…), token riêng của
  component / màn (`AodsOnboardingTokens`, `AodsClockTokens`…), `LocalAods…`, và các object
  `AodsPrimitive*`. Giao diện giữ nguyên. CLAUDE.md, agent `compose-implementer`, skill
  `design-tokens` / `compose-optimizer`, README cập nhật theo; lệnh grep: thư mục `theme/` chỉ có
  `AodsTheme` / `AodsColors` / `AodsTypography` / `AodsShapes` / `AodsBrush`, không gọi thẳng
  `AodsColors` / `AodsTypography` / `AodsShapes` ngoài `theme/`, không `AodsPrimitive` / tầng token,
  không `.copy(` trên màu theme trong UI; bỏ 2 lệnh cấm số `dp` / `sp` và `durationMillis` viết
  thẳng.

### Ghi chú

- "Tắt màn hình không thấy AOD" lúc thử là do đang ngoài khung giờ hiện (mặc định 07:00–23:00, mục
  Quy tắc hiện), không phải lỗi code.

## [Chưa phát hành] – 2026-10-06 – Onboarding mới, font Inter, icon app

Làm lại giao diện Splash và Language theo hướng skill ui-ux-pro-max gợi ý (style Dark Mode OLED, giữ
màu mint), thêm màn Permission sau Language ở lần đầu mở app. Không thêm quyền, không thêm thư viện.
Bản này build được (`compileDebugKotlin`), lint 0 lỗi (không có cảnh báo mới), 24 lệnh grep của
CLAUDE.md ra 0 dòng, đã test trên máy.

### Thêm

- Màn Permission (`screens/permission/`, bước 2/2 của onboarding): chỉ các quyền bắt buộc để AOD mặc
  định chạy — "Hiển thị trên ứng dụng khác", thêm "Hiển thị trên màn hình khóa" trên máy Xiaomi. Nút
  Bắt đầu chỉ bật khi đủ quyền; quyền mà máy không cho kiểm tra (op của Xiaomi) thì không chặn, chỉ
  nhắc mở Cài đặt. Quay lại từ Cài đặt thì tự đọc lại quyền.
- Các lần mở app sau: AOD đang bật mà thiếu quyền bắt buộc thì màn chính hiện bottom sheet để cấp
  quyền. Đóng sheet thì còn dòng cảnh báo ở đầu màn chính để mở lại; bật lại công tắc AOD cũng mở
  lại sheet; đủ quyền thì cả hai tự ẩn.
- Component dùng chung `AodsOnboardingHeader` / `AodsOnboardingFooter` (chỉ báo bước, một nút chính,
  dòng nhắc khi nút bị khoá) và `AodsPermissionCard`; token `AodsOnboardingTokens`; thứ tự bước
  `OnboardingStepValue`.
- Font Inter (bản variable của Google Fonts, giấy phép OFL, khoảng 856 KB) cho toàn bộ giao diện
  app, kể cả component Material 3. Màn AOD giữ font đồng hồ user chọn.
- Icon app mới: điện thoại tắt màn hình có viền sáng gradient mint → xanh → tím, đồng hồ 7 đoạn xếp
  chồng và ba chấm thông báo; thêm lớp đơn sắc cho icon theo chủ đề (Android 13+). Small icon của
  thông báo vẫn là `ic_aod`.
- Animation Lottie `res/raw/lottie_device_edge_light.json` (37 KB, lặp 2,4 giây): ánh sáng kiểu LED
  lướt theo viền máy — nằm sát trên viền, độ rộng đều, cùng dải màu, độ sáng mịn với đuôi dài và mép
  trước tắt dần (44 lớp mỏng chồng nhau, đầu nét phẳng) nên trông như chính viền đang sáng lên; aura
  chuyển màu thở phía sau; cùng khung 108 với icon nên khung đầu trùng icon tĩnh. `AodsLottie` thêm
  tham số `placeholder` để vẽ thứ khác trong lúc file đang nạp.

### Thay đổi

- Splash: icon động (Lottie viền sáng) 288dp đúng tâm cửa sổ, khung đầu trùng splash hệ thống; tên
  app và câu giới thiệu hiện dần bên dưới; giữ thanh tải và dòng "có thể có quảng cáo".
- Language: mỗi ngôn ngữ là một thẻ chọn lớn (cờ, viền, radio, đổi màu có animation); lần đầu mở app
  có chỉ báo "Bước 1/2" và nút Tiếp tục.
- Cờ "lần đầu mở app" chỉ xoá khi đã đủ quyền bắt buộc (ở Language nếu máy đã có sẵn quyền, không
  thì ở màn Permission), nên thoát giữa chừng thì lần sau đi lại onboarding.
- `PermissionValue` thêm tên ngắn và icon; danh sách quyền dùng chung
  `PermissionManager.aodPermissions()` / `requiredAodPermissions()`.
- CLAUDE.md: luồng onboarding (mục 5), font Inter và splash (mục 14), component mới (mục 15), icon
  app, quy tắc tự làm thay vì tạo agent (mục 21).

## [Chưa phát hành] – 2026-10-06 – Skill ui-ux-pro-max

Chuẩn bị cho đợt làm lại UI (style, theme). Không đổi code app.

### Thêm

- `.claude/skills/ui-ux-pro-max/` từ nextlevelbuilder/ui-ux-pro-max-skill (MIT, bản 2.13.0): tra
  style, bảng màu, cặp font, luật UX / accessibility và hướng dẫn riêng cho Jetpack Compose bằng
  script Python (chỉ thư viện chuẩn). Đường dẫn script sửa cho bản cài trong project, bỏ test của
  skill, thêm mục "Project notes (Custom-AOD)". `CLAUDE.md` mục 21.

## [Chưa phát hành] – 2026-10-05 – Bộ skill Claude, design system Aods

Đưa bộ skill của kotlin-accelerator-ai vào project và refactor UI theo chuẩn token của skill. Giá
trị hình ảnh giữ nguyên nên giao diện không đổi. Không thêm quyền và thư viện. Bản này build được (
`assembleDebug`), lint 0 lỗi (không có cảnh báo mới), 24 lệnh grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thêm

- `.claude/`: agent `compose-implementer` và 8 skill: `idea-pipeline` → `prd-pipeline` →
  `design-spec` → `design-tokens` (từ ý tưởng đến token rồi dựng màn), `compose-optimizer`,
  `kotlin-review`, `unit-testing`, `gradle-module`. Bỏ DRE (dre-kt), Supabase và prefix `kta-` của
  bản gốc. Đã sửa theo project: MVI + Navigation 3, 1 module `:app`, theme luôn tối, CLAUDE.md là
  chuẩn, mỗi skill có mục "Project notes (Custom-AOD)". `CLAUDE.md` mục 21.
- Design system `Aods` trong `presentation/theme/`: token Primitive (`AodsPrimitiveColors`,
  `Spacing`, `IconSize`, `Shape`, `Border`, `Typography`, `Motion`, `Opacity`, `Elevation`) →
  Semantic (`AodsColorTokens`, `AodsSpacingTokens`, `AodsShapeTokens`, `AodsTypographyTokens`,
  `AodsMotionTokens`, `AodsOpacityTokens`, `AodsElevationTokens`) → component/domain (
  `AodsSettingsRowTokens`, `AodsPickerTokens`, `AodsDrawingPadTokens`, `AodsClockTokens`,
  `AodsSplashTokens`, `AodsAdTokens`). `AodsTheme { }` cấp token và nối sang Material 3; đọc bằng
  `AodsTheme.colors/.spacing/…`.
- `DataStoreManager.observeAodOptions/NotificationOptions/Appearance/Interaction/Rules/Extras()`
  trong các file UiModel, cạnh `currentAodXxx()`.
- 3 lệnh grep mới ở `CLAUDE.md` mục 18: số `dp`/`sp`, `durationMillis` viết thẳng,
  `MaterialTheme.*`/`CircleShape` ngoài `presentation/theme/`.

### Thay đổi

- Component dùng chung mang prefix `Aods`: `SettingsRows.kt` → `AodsSettingsRows.kt` (
  `AodsSectionHeader`, `AodsSectionLabel`, `AodsSwitchRow`, `AodsRadioRow`, `AodsValueRow`,
  `AodsSliderRow`), `AppLottie` → `AodsLottie`, `NoInternetDialog` → `AodsNoInternetDialog`,
  `AppTheme` → `AodsTheme`.
- Mọi màn (Main, Language, Splash, AOD), `Modifiers.kt`, composable quảng cáo và `ClockColorValue`
  đọc token thay cho `MaterialTheme.*`, màu trong `Color.kt` và số viết thẳng.
- `AodContent.kt` (934 dòng) tách thành `AodContent`, `AodClockFace`, `AodDetails`, `AodExtras`,
  `AodNotifications`, `AodMediaControls`, `AodBackdrop`, `AodTimeFormat`; `MainAppearanceSection`
  tách picker sang `MainAppearancePickers.kt`; `MainExtrasSection` tách hộp thoại sang
  `MainExtrasDialogs.kt`; `MainViewModel` bỏ 5 hàm dựng luồng cài đặt (dùng `observeAodXxx()`).
- `.gitignore`: `.claude/settings.local.json`, `__pycache__/`.

### Xóa

- `presentation/theme/Color.kt`, `Theme.kt`, `Type.kt` (thay bằng token).

## [Chưa phát hành] – 2026-10-05 – Nội dung thông báo, ảnh nền AMOLED có sẵn

Hai khoảng trống còn lại so với Always On AMOLED. Không thêm quyền và thư viện. Bản này build được (
`assembleDebug`), lint 0 lỗi (không có cảnh báo mới), grep của CLAUDE.md ra 0 dòng, đã test trên máy (theo các bước mới: README › Kiểm tra trên máy thật, bước 40–41).

### Thêm

- "Hiện nội dung thông báo mới nhất" (`is_aod_notification_content_enabled`, mặc định tắt): tiêu đề
  và nội dung của thông báo mới nhất dưới các icon. `NotificationStateManager` che nội dung đúng như
  màn hình khóa của hệ thống: đọc "hiện thông báo trên màn hình khóa", "hiện nội dung nhạy cảm",
  khóa bảo mật, chính sách quản lý thiết bị, `visibility` của thông báo và tùy chỉnh theo kênh; bị
  che thì dùng `publicVersion`, không có thì AOD ghi "Nội dung đã ẩn". `ActiveNotification` có thêm
  `content` (`NotificationContent`).
- 6 ảnh nền AMOLED có sẵn (`WallpaperValue`, key `aod_wallpaper`): Sao đêm, Cực quang, Nhật thực,
  Sóng, Tinh vân, Chân trời. Vector `res/drawable/img_wallpaper_*.xml`, AOD vẽ thẳng (không lưu
  file), mờ 50%, bố cục ngang giữ phần có chi tiết. Màn cài đặt có hàng ảnh xem trước để chọn.
- `CLAUDE.md`: bất biến 38 (nội dung thông báo), 39 (ảnh nền có sẵn), resource ảnh nền (mục 16),
  cách thêm ảnh nền (mục 20.6).

### Thay đổi

- Mục ảnh nền của màn cài đặt: nhãn "Ảnh nền", hàng ảnh có sẵn, dòng "Ảnh từ máy", "Bỏ ảnh nền" bỏ
  cả hai. Mỗi lúc chỉ một ảnh nền: chọn ảnh có sẵn thì xóa ảnh từ máy, và ngược lại.
- `AodViewModel` theo dõi thông báo khi bật icon hoặc nội dung; `toAodNotificationsUiModel(options)`
  nhận tùy chọn thông báo.

## [Chưa phát hành] – 2026-10-04 – Giai đoạn 5: thông tin thêm trên đồng hồ

Thời tiết, sự kiện hôm nay, ghi nhớ, hình vẽ nhanh. Thêm 2 quyền hỏi lúc chạy, chỉ hỏi khi bật tùy
chọn tương ứng (`READ_CALENDAR`, `ACCESS_COARSE_LOCATION`), và thư viện Retrofit 3.0.0 + converter
kotlinx-serialization, kotlinx-serialization-json 1.11.0 cho API thời tiết Open-Meteo. Lần đầu dùng
tầng domain (UseCase, Repository). Bỏ qua widget và Tasker. Bản này build được (`assembleDebug`, R8
của bản release), lint 0 lỗi, grep của CLAUDE.md ra 0 dòng, đã test trên máy (theo các bước mới: README › Kiểm tra trên máy thật, bước 35–39).

### Thêm

- Thời tiết (`is_aod_weather_enabled`, `is_aod_weather_fahrenheit`): icon trời và nhiệt độ cạnh
  ngày, °C hoặc °F. `DeviceLocationManager` (`data/device/location/`) lấy vị trí gần đúng một lần;
  `OpenMeteoApiService` (`data/network/api/`) nhận toạ độ đã làm tròn 2 chữ số thập phân;
  `WeatherRepositoryImpl` lưu bản mới vào DataStore (`weather_temperature_celsius`, `weather_code`,
  `is_weather_day`, `weather_updated_at_millis`) để đồng hồ hiện ngay mà không chờ mạng.
  `RefreshWeatherUseCase` chỉ gọi mạng khi bản đã lưu cũ từ 30 phút; AOD đang hiện thử lại mỗi 10
  phút; bản cũ từ 3 giờ không hiện. Mã thời tiết WMO gom thành 8 nhóm (`WeatherCondition`), icon
  ngày và đêm.
- Sự kiện hôm nay (`is_aod_calendar_enabled`): tối đa 2 sự kiện còn lại trong ngày từ
  `CalendarContract.Instances` (`CalendarRepositoryImpl`, `GetUpcomingEventsUseCase`), sự kiện cả
  ngày so theo ngày ghi trên lịch. AOD đọc lúc mở và mỗi 15 phút, sự kiện đã kết thúc rời đồng hồ ở
  tick mỗi phút.
- Ghi nhớ (`aod_memo`, tối đa 120 ký tự, hiện tối đa 3 dòng) và hình vẽ nhanh (file
  `aod_drawing.png`, PNG nền trong suốt, tô theo màu đồng hồ).
- Màn cài đặt: mục "Thông tin thêm trên đồng hồ" (`MainExtrasSection`), hộp thoại nhập ghi nhớ có bộ
  đếm ký tự, khung vẽ (Lưu, Xóa hết, Hủy). Bật lịch hoặc thời tiết thì hỏi quyền, chỉ lưu "bật" khi
  được cấp; bật thời tiết thì tải ngay. `SettingsValueRow` có thêm dòng mô tả (hiện nội dung ghi
  nhớ).
- Tầng domain: `Weather`, `WeatherCondition`, `CalendarEvent`; `WeatherRepository`,
  `CalendarRepository`; `ObserveWeatherUseCase`, `RefreshWeatherUseCase`,
  `GetUpcomingEventsUseCase`. Koin: `networkModule` (`Json`, `Retrofit`, `OpenMeteoApiService`),
  `repositoryModule`, `useCaseModule`.
- `CLAUDE.md`: tầng domain và data (mục 1, 9), manager mới (mục 2), thư viện (mục 7.1), DI (mục 12),
  quy tắc RepositoryImpl dùng `withContextCatching` và không có exception riêng của domain (mục 9,
  13, 13.1, 18 kèm lệnh grep chặn `withContext(` thô), bất biến 33–37, cách thêm thông tin cho đồng
  hồ (mục 20.6).

### Thay đổi

- `BackgroundImageManager` (`data/local/background/`) → `AodImageManager` (`data/local/image/`), giữ
  cả ảnh nền lẫn hình vẽ: `hasImage`, `saveImage`, `loadImage`, `removeImage` → `hasBackground`,
  `saveBackground`, `loadBackground`, `removeBackground`; thêm `hasDrawing`, `saveDrawing`,
  `loadDrawing`, `removeDrawing`.
- `PermissionManager`: thêm `hasCalendarPermission()`, `hasCoarseLocationPermission()`.
- `AodViewModel` nhận thêm 3 UseCase; `MainViewModel` nhận thêm `RefreshWeatherUseCase`.
- Splash: icon app (glyph đồng hồ màu mint) ở chính giữa màn hình, cỡ 144dp như icon của splash hệ
  thống từ Android 12, tên app nằm ngay dưới; thêm preview.

### Xóa

- `Placeholder.kt` của `domain/model`, `domain/repository`, `domain/usecase`, `data/repository`.

## [Chưa phát hành] – 2026-10-04 – Giai đoạn 4: thao tác và cảm biến

Gán hành động cho thao tác và phím, đèn pin, tự giảm sáng theo cảm biến ánh sáng, nhấc máy để hiện
lại đồng hồ. Không thêm quyền và thư viện: đèn pin dùng `setTorchMode`, không cần quyền `CAMERA`.
Bản này build được, lint 0 lỗi, grep của CLAUDE.md ra 0 dòng, đã test trên máy (theo các bước mới: README › Kiểm tra trên máy thật, bước 29–34).

### Thêm

- Hành động cho 6 thao tác (`AodGestureValue`): chạm 2 lần, vuốt lên, vuốt xuống, phím tăng/giảm âm
  lượng, phím quay lại. 7 hành động (`AodActionValue`): không làm gì, về màn hình khóa, tối màn
  hình, bật/tắt đèn pin, phát/tạm dừng, bài trước, bài tiếp theo. Mặc định giữ cách chạy trước đây (
  chạm 2 lần và quay lại về màn hình khóa). Keys `aod_double_tap_action`, `aod_swipe_up_action`,
  `aod_swipe_down_action`, `aod_volume_up_action`, `aod_volume_down_action`, `aod_back_action`.
- `FlashlightManager` (`data/device/flashlight/`): bật/tắt đèn pin, theo dõi trạng thái đèn từ mọi
  nguồn; AOD hiện icon khi đèn đang bật.
- `AmbientLightManager` (`data/device/light/`) và tùy chọn "Tự giảm sáng khi phòng tối" (
  `is_aod_auto_dim_enabled`): dưới 5 lux giảm về 1%, trên 20 lux trở lại, phải giữ 2 giây mới đổi.
  `AodActivity.renderDim()`.
- `PickupGestureManager` (`data/device/pickup/`) và tùy chọn "Nhấc máy để hiện lại đồng hồ" (
  `is_aod_raise_to_wake_enabled`): cảm biến `android.sensor.pick_up_gesture`. `AodService` chỉ chờ
  nhấc máy sau khi đồng hồ tối theo chủ ý và màn hình tắt, không chờ khi máy trong túi.
- Màn cài đặt: mục "Thao tác và cảm biến" (`MainInteractionSection`), hộp thoại chọn hành động cho
  từng thao tác. `SettingsSwitchRow` có thêm dòng mô tả và trạng thái tắt (máy không có cảm biến).
- `collectLatestCatching` trong `utils/CoroutineExt.kt`: như `collectCatching` nhưng huỷ khối đang
  chạy khi có giá trị mới. `AodViewModel.observeProximity` và `observeAmbientLight` dùng hàm này
  thay cho `collectLatest` thô.
- `CLAUDE.md`: 3 manager mới (mục 2), `collectLatestCatching` (mục 13, 13.1, 15, 18 kèm lệnh grep
  chặn `.collectLatest {` thô), bất biến 29–32, cách thêm thao tác hoặc hành động (mục 20.6).

### Thay đổi

- `AodIntent.DoubleTap` → `PerformGesture(gesture)`: ViewModel tra hành động đã gán. Dòng "Chạm 2
  lần để thoát" chỉ hiện khi chạm 2 lần vẫn là về màn hình khóa (`AodState.isExitHintVisible`).
- `AodContent` giữ focus để nhận phím âm lượng; `AodScreen` xử lý phím quay lại bằng `BackHandler`.
- `AodService.launch()` → `launchAod()`.
- `AodViewModel` nhận thêm `FlashlightManager`, `AmbientLightManager`; `MainViewModel` nhận thêm 3
  manager mới để biết máy có đèn pin và cảm biến không.

## [Chưa phát hành] – 2026-10-04 – Giai đoạn 3: giao diện đồng hồ

Mặt đồng hồ, font, màu, cỡ, ảnh nền, xoay ngang làm đồng hồ đêm. Không thêm quyền và thư viện: ảnh
nền chọn bằng Photo Picker (không cần quyền đọc ảnh), font là họ font của hệ thống. Bản này build
được, lint 0 lỗi, grep của CLAUDE.md ra 0 dòng, đã test trên máy (theo các bước mới:
README › Kiểm tra trên máy thật, bước 24–28).

### Thêm

- 4 mặt đồng hồ (`ClockFaceValue`): Số (như trước), Số xếp chồng (giờ trên, phút dưới), Kim (có vạch
  giờ), Kim tối giản. Mặt kim vẽ bằng `Canvas`, nhích mỗi phút.
- Font (`ClockFontValue`): Mặc định, Có chân, Đơn cách, Viết tay — họ font chung của hệ thống, áp
  cho giờ và ngày. Dòng chọn font trong cài đặt hiện bằng chính font đó.
- Màu (`ClockColorValue`): 8 màu dịu trên nền đen (token mới `Blue`, `Purple`, `Orange`, `Pink`
  trong `Color.kt`), áp cho số giờ hoặc kim; mặc định xám như trước.
- Cỡ đồng hồ 60–150%, mỗi nấc 10%.
- Ảnh nền: `BackgroundImageManager` (`data/local/background/`) lưu bản sao JPEG đã thu về cỡ màn
  hình (Photo Picker chỉ cho đọc tạm). AOD giải mã ở luồng nền rồi hiện dần, mờ 50%, ẩn khi tối.
- "Luôn xoay ngang (đồng hồ đêm)": `AodActivity` khóa ngang theo cảm biến trước khi cửa sổ hiện. Bố
  cục ngang (đồng hồ trái, thông tin phải) chọn theo kích thước thật, nên cũng đúng khi hệ thống tự
  xoay.
- Màn cài đặt: mục "Giao diện đồng hồ" (`MainAppearanceSection`), bảng chọn màu dạng ô tròn. Keys
  `aod_clock_face`, `aod_clock_font`, `aod_clock_color`, `aod_clock_size_percent`,
  `is_aod_landscape`.
- `SettingsLabel` (nhãn nhóm dùng chung) và tham số `labelFontFamily` của `SettingsRadioRow`.
- `CLAUDE.md`: manager mới (mục 2), thành phần (mục 20.2), bất biến 25–28, cách thêm mặt đồng hồ
  hoặc màu (mục 20.6).

### Thay đổi

- `AodContent` tách giờ (`AodClockFace`) khỏi ngày và phần thông tin (`AodDetails`). Ở bố cục ngang,
  hai biên độ dịch chống burn-in đổi chỗ cho nhau.
- `AodViewModel` đọc giao diện trong `initialState` và tải ảnh nền lúc mở; nhận thêm
  `BackgroundImageManager`. `MainViewModel` nhận thêm `BackgroundImageManager`, gom 4 nhóm cài đặt
  vào một lần cập nhật state.
- Nhãn "Nguồn điện" của mục quy tắc dùng `SettingsLabel`.
- Đồng nhất tên Intent/Effect theo quy tắc mới (CLAUDE.md mục 10.2.1, kèm lệnh grep chặn tên ở thì
  quá khứ):
    - `MainIntent`: `Preview` → `OpenPreview`, `OpenLanguage` → `NavigateToLanguage`,
      `OpenPermission` → `OpenPermissionSettings`, `PickBackground` → `OpenBackgroundPicker`,
      `BackgroundPicked` → `BackgroundPickerResult`, `NotificationPermissionRequested` →
      `NotificationPermissionDialogShown`.
    - `MainEffect`: `OpenMiuiPermissionEditor` → `OpenMiuiPermissionSettings`,
      `LaunchBackgroundPicker` → `OpenBackgroundPicker`. Hàm `Context.openMiuiPermissionEditor()` →
      `openMiuiPermissionSettings()`.
    - `LanguageIntent`: `Done` → `ConfirmLanguage`, `Back` → `NavigateBack`;
      `LanguageState.isDoneEnabled` → `isConfirmEnabled`.
    - `AodIntent`: `MediaPlayPause` → `PlayPauseMedia`, `MediaSkipPrevious` → `SkipToPreviousTrack`,
      `MediaSkipNext` → `SkipToNextTrack`; `AodEffect.Close` → `CloseAod`.
    - Hàm private trong ViewModel đổi theo: `openPermissionSettings()`,
      `onBackgroundPickerResult()`, `onNotificationPermissionDialogShown()`, `confirmLanguage()`,
      `closeAod()`.

## [Chưa phát hành] – 2026-10-04 – Giai đoạn 2: thông báo trên đồng hồ

Ba tính năng dùng chung quyền "Truy cập thông báo" (`NotificationListenerService`): icon thông báo,
viền sáng khi có thông báo mới, điều khiển nhạc. Không thêm thư viện và không thêm
`uses-permission`; listener chỉ được khai báo là service do hệ thống bind. Bản này build được (
`assembleDebug`), lint 0 lỗi, grep của CLAUDE.md ra 0 dòng, đã test trên máy (theo các bước mới: README › Kiểm tra trên máy thật, bước 19–23).

### Thêm

- `AodNotificationListener` (`presentation/aod/`): `NotificationListenerService`, hệ thống chỉ bind
  khi user đã cấp quyền. Callback chỉ đọc danh sách thông báo đang có và `RankingMap` rồi chuyển cho
  manager.
- `NotificationStateManager` (`data/device/notification/`):
    - Lọc thông báo như màn hình chờ của hệ thống: bỏ thông báo của chính app, thường trực, tóm tắt
      nhóm, thông báo nhạc, im lặng, bị Không làm phiền ẩn khỏi màn hình chờ, ẩn trên màn hình khóa,
      của app bị tạm ngưng.
    - Nạp sẵn icon trên một luồng nền, cache theo (package, resource), để khung đầu tiên của đồng hồ
      đã có icon.
    - `alerts`: báo thông báo mới, hoặc cập nhật không đặt "chỉ báo một lần", khi không bị Không làm
      phiền chặn.
    - `mediaSessionToken`: phiên nhạc của thông báo nhạc mới nhất.
- `MediaStateManager` (`data/device/media/`): `MediaController` từ token đó (không cần quyền riêng),
  cho tên bài, nghệ sĩ, đang phát, và Phát/Tạm dừng, Bài trước, Bài tiếp theo.
- Màn đồng hồ:
    - Hàng icon dưới ngày, mỗi app một icon, tối đa 5, còn lại "+N". Chỉ icon, không nội dung.
    - Viền sáng ở 4 cạnh, nhấp nháy khoảng 4 giây theo màu của app (không đặt hoặc quá tối thì dùng
      màu mint), chỉ khi đồng hồ không tối.
    - Tên bài, nghệ sĩ và 3 nút nhạc; nút bị bỏ qua khi cảm biến tiệm cận đang bị che, như chạm 2
      lần.
- Màn cài đặt:
    - Mục "Thông báo trên đồng hồ" với 3 công tắc, mặc định bật (keys
      `is_aod_notification_icons_enabled`, `is_aod_edge_glow_enabled`,
      `is_aod_media_controls_enabled`), kèm dòng nhắc khi chưa có quyền.
    - Dòng quyền "Truy cập thông báo" (không bắt buộc), mở thẳng trang bật quyền của app (
      `openNotificationListenerSettings`, ROM không có trang đó thì mở danh sách).
- `CLAUDE.md`: 2 manager mới và ngoại lệ của chúng (mục 2), luồng và thành phần (mục 20.1, 20.2),
  bất biến 20–24.

### Thay đổi

- `AodViewModel` nhận thêm `NotificationStateManager` và `MediaStateManager`. Chạm 2 lần và nút nhạc
  cùng bị bỏ qua khi cảm biến tiệm cận đang bị che.
- Màn Main có thêm `MainNotificationsSection`; `MainState` có `notificationOptions` và
  `isNotificationAccessGranted`.

## [Chưa phát hành] – 2026-10-04 – Giai đoạn 1: quy tắc hiện, độ sáng, ô Cài đặt nhanh, ngôn ngữ

Năm tính năng đầu trong danh sách còn thiếu so với Always On
AMOLED ([COMPARISON.md](COMPARISON.md)). Không thêm quyền và thư viện nào. Bản này build được (
`assembleDebug`), lint 0 lỗi, grep của CLAUDE.md ra 0 dòng, đã test trên máy (theo các bước mới: README › Kiểm tra trên máy thật, bước 1 và 14–18).

### Thêm

- Quy tắc nguồn điện (key `aod_charging_rule`): Luôn hiện / Chỉ khi đang cắm sạc / Chỉ khi dùng
  pin. "Đang cắm" đọc từ `BatteryManager.EXTRA_PLUGGED` (`BatteryStateManager.isPlugged`,
  `readIsPlugged()`), nên pin đầy hoặc máy dừng sạc ở 80% vẫn tính là đang cắm.
- Khung giờ (keys `is_aod_schedule_enabled`, `aod_schedule_start_minute`,
  `aod_schedule_end_minute`): mặc định bật, 07:00–23:00, chỉnh bằng `TimePicker` của Material 3. Bắt
  đầu sau kết thúc là khung qua nửa đêm; bắt đầu trùng kết thúc là cả ngày; tắt khung giờ là hiện cả
  ngày.
- Mức độ sáng (keys `is_aod_custom_brightness`, `aod_brightness_percent`): công tắc cùng thanh trượt
  1–100%, mặc định bật ở 1% (bằng mức "giảm độ sáng" cũ). Tắt thì đồng hồ theo độ sáng của hệ thống.
- `AodRulesUiModel` (kèm `AodScheduleUiModel`, `ChargingRuleValue`, `ScheduleTimeValue`,
  `DataStoreManager.currentAodRules()`): một hàm `allows()` cho nguồn điện, khung giờ và ngưỡng pin,
  dùng chung cho `AodService.shouldEnter()` và `AodViewModel.checkRules()`. Đồng hồ đang hiện mà sai
  quy tắc (rút hoặc cắm sạc, hết khung giờ, pin yếu) thì chuyển sang đen, kiểm lại khi pin, nguồn
  cắm đổi và mỗi phút.
- Ô Cài đặt nhanh `AodTileService` (`TOGGLEABLE_TILE`): bật/tắt AOD, trạng thái theo
  `is_aod_enabled`. Hệ thống chặn khởi động foreground service từ ô (Android 15 trở lên) thì ô mở
  app bằng `startActivityAndCollapse` (đang khóa thì `unlockAndRun` trước) để `MainActivity` khởi
  động service.
- Màn chọn ngôn ngữ `presentation/screens/language/` (MVI, `LanguageDestination(isFirstOpen)`):
  English và Tiếng Việt (`LanguageValue.TRANSLATED`), tên viết bằng chính ngôn ngữ đó (
  `LanguageUiModel`), ngôn ngữ của máy lên đầu.
    - Lần đầu mở app: Splash → Language (chọn sẵn ngôn ngữ của máy, không có nút back) → Main. Bấm "
      Xong" lưu ngôn ngữ và `is_first_open = false`; thay TODO của base trong `SplashScreen`.
    - Từ màn Main: mục "Ứng dụng › Ngôn ngữ". "Xong" chỉ bật khi chọn khác ngôn ngữ đang dùng.
- `presentation/components/SettingsRows.kt`: `SettingsSectionHeader`, `SettingsSwitchRow`,
  `SettingsRadioRow`, `SettingsValueRow`, `SettingsSlider`, dùng chung cho màn Main và Language.
- `CLAUDE.md`:
    - Mục 13.1 "Chọn helper": collect flow của manager/UseCase phải dùng `collectCatching`, các chỗ
      được giữ collect thô, khi nào dùng `suspendRunCatching`, `withContextCatching`,
      `recoverCatching`.
    - Mục 18 thêm lệnh grep `\.collect {` thô.
    - Mục 20 thêm bất biến 17–19 (quy tắc hiện, `EXTRA_PLUGGED`, khung giờ) và ô Cài đặt nhanh.

### Thay đổi

- Màn Main tách thành `MainContent` cùng `MainPermissionsSection`, `MainOptionsSection`,
  `MainRulesSection` (mục mới "Khi nào hiện": nguồn điện, khung giờ, ngưỡng pin) và
  `MainAppSection` (ngôn ngữ). Ngưỡng pin chuyển từ "Tùy chọn" sang "Khi nào hiện" (
  `AodOptionsUiModel.minBattery` → `AodRulesUiModel.minBattery`).
- `MainScreen` nhận `backStack` (mở màn Language); `MainViewModel` nhận thêm `LanguageManager`.
- `AodActivity`: độ sáng là `aod_brightness_percent / 100` khi bật độ sáng riêng, ngược lại
  `BRIGHTNESS_OVERRIDE_NONE`. Vẫn đặt trong `onCreate`, trước khi cửa sổ hiện.
- `AodService.start()` trả `Boolean`: `false` khi hệ thống chặn khởi động foreground service.
- Collector của `AodService` (sự kiện màn hình, ngôn ngữ), `AodViewModel` (pin, nguồn cắm, âm thanh)
  và `AodTileService` dùng `collectCatching` thay vì `.collect { }` thô. Lỗi chỉ dừng collector đó
  và được log, không làm crash app. Collector tiệm cận giữ `collectLatest`, vì cần huỷ lần chờ 3
  giây khi giá trị đổi.

### Xóa

- Key `is_aod_dim_brightness` (`saveIsAodDimBrightness`) và chuỗi `opt_dim`: thay bằng độ sáng riêng
  1–100%. Giá trị đã lưu của key cũ không được chuyển sang, vì app chưa phát hành.

## [Chưa phát hành] – 2026-10-03 – Chuyển sang Android-Base

Lõi AOD của FakeAOD được đưa vào
base [Android-Base](https://github.com/decoutkhanqindev/Android-Base) (repo Custom-AOD) và refactor
theo rule của base: tùy chọn vào `DataStoreManager`, tín hiệu thiết bị thành manager, màn đồng hồ và
màn cài đặt theo MVI. Cơ chế lõi giữ như FakeAOD; các chỗ hành vi khác ghi ở mục "Khác FakeAOD". Bản
này build được, lint 0 lỗi, grep của CLAUDE.md ra 0 dòng, đã test trên máy.

### Thêm

- Toàn bộ hạ tầng của base:
    - Koin, Navigation 3 (Splash → Main), MVI (`BaseViewModel`), DataStore, Timber.
    - AdMob + consent UMP, với quảng cáo xen kẽ ở Splash (đang là id test của Google).
    - `NoInternetDialog`, và hạ tầng đổi ngôn ngữ trong app (`LanguageManager`, `LanguageValue`).
      Màn chọn ngôn ngữ chưa có.
- Manager tín hiệu thiết bị trong `data/device/`:
    - `ScreenStateManager`: sự kiện `SCREEN_OFF` / `USER_PRESENT` dạng `SharedFlow`,
      `isInteractive`, `isDeviceSecure`, wake lock bật lại màn hình khóa.
    - `BatteryStateManager`: phần trăm pin và trạng thái sạc, kèm hàm đọc đồng bộ từ broadcast
      sticky.
    - `AudioStateManager`: `isBusy` (chuông, cuộc gọi, báo thức) và `isBusyNow()`.
    - `ProximityManager`: `isNear`.
    - `PermissionManager`: quyền overlay, thông báo, và hai quyền riêng của Xiaomi.
- Màn AOD theo MVI trong `presentation/screens/aod/`: `AodViewModel`, `AodContent`, `AodScreen`,
  `state/`.
- Màn cài đặt theo MVI trong `presentation/screens/main/`, kèm `AodOptionsUiModel`,
  `BatteryUiModel`, `PermissionValue`, `PermissionStatusValue`, `PermissionUiModel`,
  `WakeResultValue`.
- `AppLanguageProvider`, dùng chung cho `MainActivity` và `AodActivity`. Component tự lấy
  `DataStoreManager` và `LanguageManager` qua `koinInject`, Activity chỉ cần
  `AppLanguageProvider { … }`.
- Trạng thái quyền hiện bằng Material icon có `contentDescription` (Đã cấp / Chưa cấp / Không rõ),
  thay cho ký tự ✓ ✕ ? –.
- Bản dịch tiếng Việt cho các chuỗi của base.
- `CLAUDE.md` mục 20 "AOD core": luồng, thành phần, các điểm không được đổi và lý do, ngoại lệ so
  với MVI chuẩn, lưu ý về ads.

### Thay đổi

- Định danh:
    - Package `com.example.fakeaod` → `com.decoutkhanqindev.custom_aod`.
    - Tên app "Fake AOD" → "Custom AOD".
    - Wake lock `fakeaod:lockscreen` → `customaod:lockscreen`.
    - Extra xem thử đổi theo package mới.
- Lưu tùy chọn: SharedPreferences `aod` (`AodPrefs`) → DataStore `custom_aod_prefs` (
  `DataStoreManager`), mỗi tùy chọn một key. Dữ liệu của FakeAOD không được chuyển sang, vì đây là
  app khác package.
- `AodService`:
    - Receiver chuyển vào `ScreenStateManager`.
    - `Handler` → coroutine. Kiểm tra sau khi mở đổi tên `launchCheck` → `checkLaunch()`, vẫn sau 2
      giây.
    - `Log` → Timber.
- `AodActivity`:
    - Logic chuyển vào `AodViewModel`: pin, tick mỗi phút (giờ hiện tại và dịch vị trí), ẩn dòng gợi
      ý sau 3 giây, hết giờ, tiệm cận 3 giây, âm thanh, chạm 2 lần.
    - `AodContent` chỉ vẽ theo state, không còn tự đếm giờ (`produceState`) hay hẹn giờ ẩn dòng gợi
      ý.
    - Activity chỉ còn cờ cửa sổ, độ sáng, ẩn thanh hệ thống, và chuyển sang đen theo
      `state.isDark`.
- `AodSession`: `object` → Koin `single`. `isCovered` đọc từ `ProximityManager`.
- Các file khác:
    - `ProximityGate` → `ProximityManager` cộng `AodViewModel`.
    - `MiuiPerm` → `PermissionManager` (đọc quyền) cộng `utils/ContextExt.kt` (
      `openMiuiPermissionEditor`).
    - `SystemPages` → `utils/ContextExt.kt` (`registerSystemReceiver`, `openSettingsPage`,
      `openOverlaySettings`, `openNotificationSettings`, `packageUri`).
- File Compose không còn hằng số top-level:
    - Pattern giờ, ngày → `strings.xml` (`translatable="false"`).
    - Bước và giới hạn thanh trượt → companion của `AodOptionsUiModel`.
    - Trạng thái quyền → `PermissionStatusValue`.
    - Intent của trang "Quyền khác" (Xiaomi) → `utils/ContextExt.kt`.
    - Thời lượng animation viết inline (`tween(durationMillis = …)`).
- Hằng số `_MS` → `_MILLIS`, giữ nguyên giá trị.
- Theme:
    - `FakeAodTheme` → `AppTheme` của base, cùng bảng màu. Màu thành token trong `Color.kt`.
    - Trạng thái "không rõ" của quyền dùng `onSurfaceVariant` (`#9A9A9A`, trước là `#8A8A8A`).
- Vị trí đồng hồ dùng `Modifier.offset` dạng lambda (lint `UseOfNonLambdaOffsetOverload` của
  FakeAOD).
- Đồng hồ mờ dần khi chuyển sang đen và hiện dần khi sáng lại (`AnimatedVisibility`), thay vì tắt
  bật tức thì. Lý do là rule "state đổi phải có animation" của base.
- Những phần của base được chỉnh:
    - `minSdk` 26 → 30. `NoInternetDialog` bỏ nhánh dưới API 29.
    - `Theme.App` dùng nền đen. `MainActivity` gọi `enableEdgeToEdge` với icon thanh hệ thống màu
      sáng, và khởi động `AodService` nếu đang bật.
    - Thay `backup_rules.xml` mẫu bằng `allowBackup="false"` cộng `data_extraction_rules.xml` của
      FakeAOD.
    - Launcher icon thay bằng icon đồng hồ của FakeAOD.
- `CLAUDE.md`: lệnh grep số 5 loại trừ `presentation/effects/`. `LaunchedWithLifecycleEffect.kt` bắt
  buộc dùng `repeatOnLifecycle`, nên trước đó grep này báo lỗi ngay trên base gốc.
- Git: remote `origin` đổi từ Android-Base sang Custom-AOD.

### Khác FakeAOD

- Giao diện, đồng hồ (ngày, gợi ý, pin) và thông báo đi theo ngôn ngữ chọn trong app, mặc định
  English. FakeAOD theo ngôn ngữ của máy.
- Mở app phải qua Splash (consent, quảng cáo) trước màn cài đặt, và cần mạng.
- Service đọc pin từ `BATTERY_CHANGED`, cùng nguồn với màn AOD: phần trăm từ level/scale, đang sạc
  khi trạng thái là CHARGING hoặc FULL. Trước đó service đọc `BatteryManager.CAPACITY` và
  `isCharging`.
- Dưới Android 12, `AudioStateManager` hỏi lại mode âm thanh mỗi phút. FakeAOD hỏi trong tick mỗi
  phút của AOD, và bỏ qua khi pin yếu hoặc khi xem thử.
- AOD đóng khi trạng thái "bận" (chuông, cuộc gọi, báo thức) chuyển sang true sau lúc mở. FakeAOD
  đóng ở mỗi lần callback báo có âm thanh khẩn trong danh sách.
- Thanh trượt vẫn lưu ở từng nấc như FakeAOD, nhưng vị trí hiển thị phải chờ DataStore ghi xong.
- Màn cài đặt khóa dọc, theo base.
- Log của app chỉ có ở bản debug, vì Timber chỉ được gắn ở debug.

### Xóa

- `AodPrefs`, `MiuiPerm`, `SystemPages`, `ProximityGate`, `ui/Theme.kt`: thay bằng các thành phần ở
  trên.
- `values/colors.xml`: dùng `@android:color/black`.
- `res/xml/backup_rules.xml` của base.

---

## FakeAOD – 2026-10-03

Lịch sử của demo FakeAOD, chép nguyên từ CHANGELOG của demo. Tên file và đường dẫn trong mục này là
của FakeAOD.

AOD chuyển từ overlay của dịch vụ trợ năng sang Activity hiện trên màn hình khóa, giống cách Always
On AMOLED làm. Số quyền người dùng phải bật giảm còn 2. Đã chạy thử trên Redmi Note 13 Pro 5G (
HyperOS 1.0, Android 14) và máy ảo Android 17.

### Thêm

- `AodService`: foreground service loại `specialUse`.
    - Giữ receiver `SCREEN_OFF` và `USER_PRESENT`, mở AOD mỗi khi màn hình tắt.
    - Sau mỗi lần mở 2 giây, ghi lại kết quả (`launchCheck`).
- `AodActivity`: màn hình AOD bằng Compose.
    - Che màn hình khóa: `showWhenLocked` trong manifest, cộng cờ cửa sổ
      `FLAG_SHOW_WHEN_LOCKED | FLAG_TURN_SCREEN_ON | FLAG_KEEP_SCREEN_ON`.
    - Ẩn thanh hệ thống, đọc pin, dịch vị trí mỗi phút để chống burn-in.
- `AodSession`: trạng thái dùng chung giữa service và Activity.
- `BootReceiver`: chạy lại service sau `BOOT_COMPLETED` và `MY_PACKAGE_REPLACED`.
- Bấm nút nguồn khi đồng hồ đang hiện thì về màn hình khóa. App bật lại màn hình bằng wake lock
  `ACQUIRE_CAUSES_WAKEUP`.
- Không mở AOD nếu màn hình đã sáng lại trước khi `SCREEN_OFF` tới, tức là khi bấm nguồn hai lần
  nhanh.
- Không mở AOD khi đang có chuông, cuộc gọi hoặc báo thức. Có kiểm tra cả
  `activePlaybackConfigurations`.
- Chế độ đen thay cho tắt màn hình khi hết giờ, khi trong túi, hoặc khi pin yếu. Lấy máy khỏi túi
  trước khi màn hình tắt thì đồng hồ sáng lại.
- Không bật lại màn hình khóa khi cảm biến tiệm cận đang bị che.
- Lần đầu mở app trên Android 13+, hỏi quyền thông báo một lần.
- Service đăng lại thông báo thường trực mỗi khi nhận lệnh start, để thông báo hiện ngay sau khi
  người dùng cho phép.
- Splash màu đen cho AOD trên Android 12+ (`res/values-v31/themes.xml`), thay vì icon app.
- Danh sách quyền mới trên màn hình chính:
    - "Hiển thị trên ứng dụng khác" (bắt buộc).
    - Xiaomi "Hiển thị trên màn hình khóa" (bắt buộc).
    - Xiaomi "Mở cửa sổ mới khi chạy nền" (tùy chọn).
    - "Thông báo" (tùy chọn).
- `CHANGELOG.md` và `COMPARISON.md` (so sánh kỹ thuật và tính năng với Always On AMOLED).
- `res/xml/data_extraction_rules.xml` và `android:fullBackupContent="false"`. Từ Android 12,
  `allowBackup="false"` không còn chặn việc chuyển dữ liệu sang máy mới.

### Thay đổi

- AOD không còn là cửa sổ `TYPE_ACCESSIBILITY_OVERLAY` mà là Activity che màn hình khóa. Vì vậy:
    - Nhận diện khuôn mặt không còn tự chạy khi đồng hồ đang hiện.
    - Màn hình khóa không còn lộ ra.
    - Màn hình cuộc gọi và báo thức tự nằm trên đồng hồ.
- Màn hình được bật bằng cờ cửa sổ `FLAG_TURN_SCREEN_ON`, thay cho cờ trên overlay cộng wake lock.
- Độ sáng chỉnh qua `screenBrightness` của cửa sổ, không ghi vào cài đặt hệ thống nữa.
- Nút nguồn khi đồng hồ đang hiện:
    - Bản đầu: tắt hẳn màn hình.
    - Bản trung gian: mở lại AOD.
    - Bản hiện tại: về màn hình khóa.
- "Tắt hẳn màn hình sau X phút" đổi thành "Tối màn hình sau X phút". "Tắt khi máy ở trong túi" đổi
  thành "Tối màn hình khi máy ở trong túi".
- Đồng hồ ở định dạng 12 giờ hiện "5:15" thay vì "05:15".
- `ProximityGate` báo cả lúc bị che và lúc được lấy ra.
- `SystemPages.kt`: thêm `registerSystemReceiver`, bỏ `isAodServiceEnabled`.
- `minSdk` 28 → 30 (Android 11), `targetSdk` 33 → 37 (Android 17), `compileSdk` 36 → 37. Các thay
  đổi hành vi từ Android 14 tới 17 đã được rà, ghi trong mục "targetSdk 37" của README.
    - `AodActivity.hideSystemBars()` bỏ nhánh `systemUiVisibility` cho Android dưới 11.
    - `setDecorFitsSystemWindows` chỉ còn được gọi dưới API 35.
    - `res/mipmap-anydpi-v26` đổi tên thành `res/mipmap-anydpi`, vì `-v26` thừa khi `minSdk` là 30.
- Lý do cũ để giữ `targetSdk = 33` (quyền `TURN_SCREEN_ON`) không còn đúng: AOSP chưa áp dụng yêu
  cầu này cho `targetSdk` nào, kể cả 37.
- Sửa theo lint sau khi nâng SDK:
    - `BootReceiver` kiểm tra action trước khi xử lý.
    - `ModeWatcher` dùng `@RequiresApi` thay cho `@TargetApi`.
    - `registerSystemReceiver` ghi rõ vì sao Android dưới 13 không cần cờ export.
    - Lint còn 0 lỗi và 12 cảnh báo: có bản mới của thư viện, gợi ý dùng hàm KTX, và một gợi ý tối
      ưu `Modifier.offset`.
- README viết lại theo kiến trúc mới. Thêm các phần: trạng thái kiểm thử, quyền trong manifest, tùy
  chọn trong app, cách đọc log.

### Xóa

- Dịch vụ trợ năng: `AodAccessibilityService.kt` và `res/xml/aod_a11y.xml`.
- `AodOverlay.kt`, `ScreenWaker.kt`, `WakeActivity.kt` (đánh thức dự phòng bằng thông báo toàn màn
  hình), `Brightness.kt` (ghi độ sáng hệ thống).
- Quyền `WRITE_SETTINGS`, `TURN_SCREEN_ON`, `USE_FULL_SCREEN_INTENT`,
  `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`.
- Các dòng quyền "Dịch vụ trợ năng", "Sửa đổi cài đặt hệ thống", "Thông báo toàn màn hình", "Không
  hạn chế pin" và Xiaomi "Tự khởi chạy"; hàm `MiuiPerm.openAutostart`.
- Broadcast `ACTION_PREVIEW`. Trên Android 9–12, receiver này bị export, nên app khác có thể bật
  đồng hồ. Nút "Xem thử" giờ mở thẳng Activity.
- Dependency `androidx.savedstate`, vì chỉ overlay đã xóa dùng tới.
- Khối `lint { disable += listOf("ExpiredTargetSdkVersion", "OldTargetApi") }` trong
  `app/build.gradle.kts`.

### Sửa lỗi

- Mở khóa bằng khuôn mặt chạy mỗi lần đồng hồ hiện, và có thể mở khóa luôn. Nguyên nhân: overlay
  không che được màn hình khóa.
- Màn hình khóa lóe lên 0,2–0,45 giây trước khi đồng hồ vẽ xong.
- Bấm nguồn tắt rồi bật lại ngay vẫn bị đồng hồ đè lên màn hình khóa.
- Đồng hồ có thể đè lên báo thức đang reo nếu màn hình tắt đúng lúc đó.

### Diễn biến trong phiên

Thứ tự các bước đã làm, và lý do mỗi bước bị thay thế:

1. **Bản ban đầu:** overlay của dịch vụ trợ năng, wake lock, và thông báo toàn màn hình làm đường dự
   phòng.
    - Overlay vẽ đục nhưng màn hình khóa vẫn hoạt động bên dưới.
    - Nhận diện khuôn mặt chạy ở mỗi lần hiện, vòng vân tay của Xiaomi nằm trên đồng hồ, và màn hình
      khóa lóe lên khoảng 0,3 giây.
2. **Activity mở bằng thông báo toàn màn hình:** chỉ cần quyền thông báo và "Hiển thị trên màn hình
   khóa".
    - SystemUI bật màn hình trước rồi mới mở Activity, nên màn hình khóa vẫn lộ khoảng 0,2 giây.
    - Nhận diện khuôn mặt chạy 0,15–0,3 giây rồi mới bị hủy.
3. **Activity mở bằng "Hiển thị trên ứng dụng khác",** như Always On AMOLED.
    - Service mở đồng hồ lúc màn hình còn tắt, và nút nguồn về màn hình khóa.
    - Log cho thấy `android:turnScreenOn` trong manifest vẫn bật màn hình quá sớm: màn hình khóa lộ
      0,3–0,45 giây, và nhận diện khuôn mặt chạy ở mọi lần mở.
4. **Đổi sang cờ cửa sổ** giống Always On AMOLED.
    - Nhận diện khuôn mặt không còn chạy ở 5/6 lần mở được ghi lại.
    - Quyền "Mở cửa sổ mới khi chạy nền" của Xiaomi chuyển thành tùy chọn, vì không cần trên HyperOS
      1.0.
5. **Dọn quyền:** bỏ "Không hạn chế pin" và "Tự khởi chạy" sau khi test trên máy. Hệ quả đã ghi
   trong README: trên Xiaomi, app không tự chạy lại sau khi bị đóng.
6. **Thêm bước hỏi quyền thông báo** ở lần mở app đầu tiên.
7. **Nâng SDK:** `minSdk` lên 30, `targetSdk` và `compileSdk` lên 37. Rà các thay đổi hành vi từ
   Android 14 tới 17: không có thay đổi nào chặn cơ chế lõi.
8. **Chuyển sang Android-Base:** xem mục đầu file.
