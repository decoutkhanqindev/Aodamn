# Plan: "Tự ẩn thông báo" cho mọi chủ đề

User chốt 2026-11-14: làm thêm hướng 1 sau "Tự ẩn trình phát" (`plans/aod-media-auto-hide.md`): thông báo mới nhất trên AOD thật tự ẩn ở
mọi chủ đề, chạm màn để hiện lại cùng trình phát.

## Mục tiêu

- Thông báo mới nhất (tiêu đề + nội dung, khối `NOTIFICATION`) hiện khi mở AOD lúc đang có thông báo hoặc khi có thông báo mới, tự ẩn sau
  thời gian ở Cài đặt › "Tự ẩn thông báo".
- Hàng icon thông báo, viền sáng khi có thông báo, ngày, pin… không đổi (vẫn hiện suốt).
- Chạm màn một lần: đang hiện trình phát hoặc thông báo thì ẩn cả hai; đang ẩn hết thì hiện lại cả hai (khối nào đang có).
- Xem trước, thẻ thu nhỏ, ảnh chuẩn test giữ nguyên (mặc định của state = hiện, như phần trình phát).

## Hiện trạng

- Chủ đề cũ (Rẻ quạt, Chuỗi, Kẹp giấy, La bàn, Phai, Khung, Dịu, Dải màu, Bán nguyệt, Đơn cách, Quỹ đạo, Gợn sóng, Gáy sách, Nhịp giây,
  Lời và các chủ đề khung chung Cổ điển, Sỏi, Thẻ, Tháp, Số áo, Viên nang qua `shownPanelBlocks`): thông báo mới nhất hiện suốt khi có.
- Áp phích, Điểm ảnh, Kinh tuyến, Nhật thực, Pha trăng: tự giữ hẹn 10 giây cố định (`LATEST_HOLD_MILLIS`), chạm hàng / dòng pin để bật / tắt,
  thông báo mới thì ẩn trình phát. Chớp mắt: dòng phụ hiện thông báo 10 giây (`NOTIFICATION_HOLD_MILLIS`).

## Thiết kế

1. **Một thanh chung** (user chốt 2026-11-14: "Dùng chung một thanh"): đổi "Tự ẩn trình phát" thành "Tự ẩn trình phát và thông báo",
   key đổi tên `aod_auto_hide_seconds` (chưa commit nên chưa có dữ liệu cũ), 0 = Không bao giờ, 10–60 giây nấc 10, mặc định 30 giây. Trình phát và
   thông báo mỗi khối một hẹn giờ riêng, cùng thời lượng. Các chủ đề mới đổi thông báo từ 10 giây cố định sang thời lượng này.
2. **Hẹn giờ chung**: tổng quát `AodMediaHold` thành `AodHold<T>(scope, holdSeconds, isNew: (old: T?, new: T) -> Boolean, onShownChange)`;
   trình phát dùng "khác tên bài / nghệ sĩ", thông báo dùng "khác key / tiêu đề / nội dung". `AodMediaHoldTest` thành `AodHoldTest` + ca cho
   thông báo.
3. **State**: `AodState.isLatestShown: Boolean = true`, `mediaHoldSeconds` đổi thành `autoHideSeconds`, getter `shownLatest = notifications.latest.takeIf { isLatestShown }`.
   Intent: `ToggleLatestNotification(isShown)` (chủ đề mới chạm dòng pin) và `ToggleAutoHideBlocks(isShown)` cho chạm màn (thay
   `ToggleMediaControls` ở `AodContent`; `ToggleMediaControls` giữ cho chủ đề mới ẩn trình phát khi có thông báo mới).
4. **Chủ đề cũ**: chỗ đọc `notifications.latest` để vẽ / chọn bố cục đổi sang `shownLatest` (khoảng 18 file + `shownPanelBlocks`). Nội dung
   trong `AnimatedVisibility` giữ thông báo cuối cùng lúc đang mờ đi để không giật (đọc thật `notifications.latest`, chỉ điều kiện hiện mới
   dùng `shownLatest`). Hàng icon không đổi.
5. **Chủ đề mới** (5 chủ đề): bỏ `isLatestShown` / `latestHoldStamp` / `LaunchedEffect(LATEST_HOLD_MILLIS)` tự giữ, đọc `state.isLatestShown`
   (AOD thật) + giữ nguyên điều kiện xem trước (`isPreviewFocused(NOTIFICATIONS)`); chạm dòng pin gửi `ToggleLatestNotification`; giữ quy tắc
   "thông báo mới ẩn trình phát". Bỏ `LATEST_HOLD_MILLIS`.
6. **Chớp mắt**: giữ kiểu dòng xoay vòng, chỉ thời gian giữ dòng thông báo lấy theo `autoHideSeconds` (0 = giữ tới khi chạm đổi dòng),
   như đã làm với dòng nhạc.

## Kiểm

- Unit test `AodHoldTest`: thông báo đến → hiện, hết giờ → ẩn; cùng thông báo cập nhật lại → không đếm lại; thông báo khác → hiện lại;
  0 giây → không ẩn; hết thông báo → ẩn. Ca trình phát cũ giữ nguyên. `BlinkyMetricsTest`, `AodThemeSpecTest`, `AodStatePreviewTest` pass.
- `compileDebugKotlin`, `testDebugUnitTest`, `check-rules`, review `jetpack-compose-performance` + `anr-audit-and-fix`.
- Máy thật (user): README bước mới: thông báo mới hiện 30 giây rồi ẩn, chạm màn hiện lại cùng trình phát, chỉnh 10 giây / Không bao giờ, một
  chủ đề cũ (Rẻ quạt, cả xoay ngang) và một chủ đề mới (Áp phích: chạm hàng pin vẫn bật / tắt thông báo).

## Docs

README (Tùy chọn trong app: hàng `aod_auto_hide_seconds` thay `aod_media_hold_seconds`; Cách dùng › Thông báo: tự ẩn; mô tả các chủ đề mới bỏ "10 giây" cố định; bước
test mới), CHANGELOG. Commit chung một commit với phần trình phát hoặc tách hai commit, theo user.

## Rủi ro

- Chủ đề cũ có bố cục ngang theo khối đang hiện (Chuỗi, Sỏi, Thẻ…) sẽ đổi bố cục thêm một lần khi thông báo ẩn (cùng kiểu với trình phát).
- Ai muốn thông báo hiện suốt như trước thì chọn "Không bao giờ".
