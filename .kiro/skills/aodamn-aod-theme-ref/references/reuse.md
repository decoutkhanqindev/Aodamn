# Dùng lại khối dùng chung

Luật gốc ở .kiro/steering/aodamn.md (xem `presentation/components/` trước khi viết mới; code dùng chung chỉ thêm, có mặc định giữ hành vi cũ,
hỏi trước). File này chỉ nói khối nào đã tách cho chủ đề và cách chọn. Danh sách dưới có thể cũ: trước khi chọn,
`ls components/aod/block/ components/aod/theme/ model/aod/theme/` và đọc chữ ký hàm, style mới nhất.

## Chọn

Làm sau bước 1 (đã có bảng tuỳ chọn của app mẫu), trước mock, cho từng phần: trình phát, thông báo mới nhất, bố cục, chữ phụ
tối đi, font của `Paint`, giờ thật theo khung hình.

1. So phần đó của app mẫu với khối ở bảng dưới, bằng code vẽ của khối (không đoán theo tên).
2. Khớp: dùng khối, chủ đề chỉ thêm style trong `<Chủ đề>Metrics` (`val CompactPlayer = AodCompactPlayerStyle(font =
   <Chủ đề>ThemeSpec.Font, …)`, số đo từ app mẫu).
3. Khác ít (một khoảng cách, viết hoa, độ mờ, số dòng…): mở rộng khối bằng thuộc tính style mới có mặc định đúng hành vi cũ, để
   chủ đề đang dùng không đổi hiển thị. Hỏi user trước, nêu ở mục "Code dùng chung" của mock.
4. Khác hẳn (bố cục đặc trưng như Rẻ quạt, Gợn sóng; trình phát kiểu khác): viết riêng trong `components/aod/theme/<chủ đề>/`,
   nêu lý do trong mock. Trình phát tự dựng vẫn dùng `AodPlayerButton` / `AodFlatProgress`.

Không chép Media / Layout / thông báo mới nhất của chủ đề cũ sang rồi sửa, không viết lại hàm làm tối brush, resolve typeface
hay vòng lặp giờ thật: khớp thì gọi khối, khác thì viết riêng.

## Khối có sẵn

Style là data class ở `model/aod/theme/Aod*Style.kt`; khối ở `components/aod/block/` (B) hoặc `components/aod/theme/` (T).

### Brush, font, giờ

| Khối | File | Làm gì | Đang dùng |
|---|---|---|---|
| `AodThemeConfig.darkBrushOf(option, fraction)` | `model/aod/theme/AodThemeConfig.kt` | brush tối đi theo tỉ lệ về đen, giữ gradient (nghệ sĩ, dòng thông báo); Metrics chỉ giữ `TEXT_DARKEN` | Gợn sóng, Vạch, trong trình phát / thông báo / khung dùng chung |
| `resolveTypeface(fontFamily, fontWeight, fallback)` | T `AodTypeface.kt` | `Typeface` cho `Paint` từ `FontFamily` của chủ đề; chủ đề vẫn tự `remember(typeface) { … }` dựng Paint | mọi chủ đề vẽ chữ bằng `Paint` |
| `rememberLiveClockMillis(isLive)` | T `AodLiveClock.kt` | `MutableLongState` giờ thật cập nhật mỗi khung hình khi `isLive`, đọc ở pha vẽ | Bán nguyệt, 4 góc, Gợn sóng, Vạch, Rẻ quạt (La bàn giữ vòng lặp riêng vì nhảy theo phút khi tắt kim giây) |

### Trình phát (Media của chủ đề chỉ gọi một hàm)

| Khối | File | Vẽ gì | Đang dùng |
|---|---|---|---|
| `AodTextPlayer` + `AodTextPlayerStyle` | B `AodTextPlayer.kt` | trước · tên bài / nghệ sĩ trượt lên, bấm chữ để phát / dừng · sau | La bàn, Bán nguyệt, Vạch, Dải màu |
| `AodWavePlayer` + `AodWavePlayerStyle` | B `AodWavePlayer.kt` | như trên, tên bài ở giữa cắt …, thanh sóng phẳng bên dưới | Vành, Chuỗi, Tháp |
| `AodCompactPlayer` + `AodCompactPlayerStyle` | B `AodCompactPlayer.kt` | tên bài / nghệ sĩ trái (nghệ sĩ tối đi `artistDarken`), nút phát / dừng, nút sau, thanh tiến trình | Chồng số, 4 góc, Rẻ quạt |
| `AodPlayerButton`, `AodFlatProgress` | B `AodPlayerParts.kt` | nút icon tròn tô màu / gradient, xám khi tắt; thanh tiến trình phẳng | trong ba trình phát trên; trình phát mới tự dựng |

### Thông báo mới nhất

| Khối | File | Vẽ gì | Đang dùng |
|---|---|---|---|
| `AodLatestLine` + `AodLatestLineStyle` | B `AodLatestLine.kt` | icon + một dòng chạy chữ một màu (viết hoa / giãn chữ tuỳ style); chuỗi "tiêu đề + nội dung" lấy bằng `NotificationContentUiModel.lineText(@StringRes formatRes)` | La bàn, Bán nguyệt, Dải màu, Vạch |
| `AodBadgeLatest` + `AodBadgeLatestStyle` | B `AodBadgeLatest.kt` | icon trong vòng tròn tô màu, tiêu đề, nội dung tối hơn nhiều dòng | Gợn sóng, Rẻ quạt |

### Khung bố cục

| Khối | File | Vẽ gì | Đang dùng |
|---|---|---|---|
| `AodHeroInfoPortrait` / `AodHeroInfoLandscape` + `AodHeroInfoStyle` | T `AodHeroInfoLayout.kt` | dọc: info trái trên (ngày + thời tiết, icon thông báo, thông báo mới nhất), phần trung tâm `hero(modifier)` giữa, trình phát + dòng pin (mờ khi không sạc) ở đáy; ngang: trung tâm giữa, info trái, trình phát + pin phải, chủ đề truyền `heroHalfWidth(maxWidth, maxHeight)` | Chồng số, 4 góc |

Khối khác cho phần vẽ riêng: `AodWaveTrack` (thanh tiến trình sóng truyền vào `AodMediaProgress(track)`),
`AodGradientShader.kt` (gradient cho `Paint`), `AodShapePath.kt`.

## Mẫu

- Media: `sector/AodSectorMedia.kt` (`AodCompactPlayer`), `tick/AodTickMedia.kt` (`AodTextPlayer`, `artistBrush =
  config.darkBrushOf(…, <Chủ đề>Metrics.TEXT_DARKEN)`), `bezel/AodBezelMedia.kt` (`AodWavePlayer`).
- Thông báo mới nhất: `tick/AodTickLayout.kt` (`lineText` + `AodLatestLine`), `sector/AodSectorLayout.kt` (`AodBadgeLatest`).
- Font và giờ: `tick/AodTickDial.kt` (`resolveTypeface` rồi `remember(typeface)` dựng Paint), `sector/AodSectorDial.kt`
  (`rememberLiveClockMillis`, đọc `longValue` ở pha vẽ).
- Layout trên khung chung: `bigoverlap/AodBigOverlapLayout.kt` (dọc chỉ truyền `hero`, ngang thêm `heroHalfWidth` tính từ số
  đo đồng hồ trong Metrics).
- Layout riêng: `sector/AodSectorLayout.kt` (trình phát vẫn đặt bằng `AodPanelBlock(block = AodPanelBlockValue.MEDIA, …)` để đi
  qua `AodMediaControls`).
