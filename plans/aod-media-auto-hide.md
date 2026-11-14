# Plan: cài đặt "Tự ẩn trình phát" cho mọi chủ đề

User chốt 2026-11-13: "hãy thực hiện như đề xuất nhé chấp nhận đổi nhiều những mang lại hiệu ứng tốt cho app hơn (như mấy theme mới)".

## Mục tiêu

Giống app mẫu (Muviz Edge: cài đặt chung `AOD_CONTROLS_TIMEOUT`, thanh 10–60 giây, mặc định 30, nấc "Never"): trình phát trên AOD
thật hiện khi mở AOD (đang có nhạc) hoặc khi đổi bài, tự ẩn sau thời gian user chọn, chạm màn để hiện / ẩn lại. Áp cho **mọi chủ đề có
trình phát** (cả chủ đề cũ đang hiện suốt), thay hẹn giờ 30 giây cố định riêng của 6 chủ đề mới bằng một chỗ điều khiển chung.

## Hiện trạng

- Chủ đề cũ (Bezel, Chồng số, Kẹp giấy, La bàn, Phai, Khung, Dịu, Dải màu, Bán nguyệt, Đơn cách, Quỹ đạo, Bốn số, Gợn sóng, Rẻ quạt, Gáy
  sách, Nhịp giây, Lời, và các chủ đề dùng khung chung như Cổ điển, Sỏi, Thẻ, Tháp, Chuỗi, Số áo, Viên nang): trình phát hiện suốt khi
  `AodState.media != null`; bố cục ngang đổi theo khối đang có (`AodLandscapePlan`, `Crossfade(hasMedia)`).
- 6 chủ đề mới (Áp phích, Điểm ảnh, Kinh tuyến, Nhật thực, Pha trăng, Chớp mắt): tự giữ state máy trạng thái riêng, hẹn 30 giây cố định,
  chạm màn bật / tắt bằng `pointerInput` riêng, thông báo mới 10 giây.
- Thẻ lật không có trình phát. Nhạc vào AOD qua `AodViewModel.observeMedia()` (`MediaStateManager.playback`).
- Cài đặt "Hết giờ AOD" đã có mẫu thanh trượt 0 = Không bao giờ (`SettingsSliderItem`, `value_never`).

## Thiết kế

1. **Lưu**: `DataStoreManager` thêm key `aod_media_hold_seconds` (Int, mặc định 30; 0 = không bao giờ ẩn), theo mẫu key có sẵn.
   `AodOptionsUiModel` thêm `mediaHoldSeconds` + hằng `MEDIA_HOLD_MAX_SECONDS = 60`, `MEDIA_HOLD_STEP_SECONDS = 10`.
2. **Cài đặt**: `SettingsOptionsSection` thêm một `SettingsSliderItem` "Tự ẩn trình phát" ngay dưới "Hết giờ AOD": 0 → "Không bao giờ",
   10–60 → "30 giây"; Intent `SettingsIntent.Options.ChangeMediaHold(seconds)`; icon Phosphor duotone `music-notes` (thêm vào `icons.txt`).
3. **Điều khiển chung ở `AodViewModel`** (AOD thật, không đụng xem trước):
   - `AodState.isMediaShown: Boolean = true` (mặc định `true` = giữ hành vi cũ ở xem trước / test). VM giữ `media` thật và cờ hiện.
   - Hiện + hẹn lại khi: mở AOD lúc đang có nhạc, đổi bài (tiêu đề / nghệ sĩ khác), bấm nút trên trình phát (`PlayPauseMedia`,
     `SkipTo…`), chạm màn lúc đang ẩn. Ẩn khi hết giờ hoặc chạm màn lúc đang hiện. `mediaHoldSeconds = 0` thì không bao giờ ẩn.
   - Một `Job?` hẹn giờ, `cancel()` trước khi hẹn mới (theo CLAUDE.md). Không I/O, chỉ `delay`.
4. **Chạm màn**: `AodContent` thêm `onTap` vào `detectTapGestures` có sẵn → `AodIntent.ToggleMediaControls`. Nút / dòng có `onClick` riêng
   (nút trình phát, dòng pin của chủ đề mới) vẫn ăn chạm trước nên không bị bật nhầm.
5. **Chủ đề đọc cờ chung**: `AodState.shownMedia` = `media.takeIf { isMediaShown }`.
   - Chủ đề cũ: đổi các chỗ đọc `state.media` để vẽ / chọn bố cục sang `state.shownMedia` (bố cục ngang tự chuyển bằng `Crossfade` đã có
     khi trình phát ẩn / hiện). Đây là chỗ đổi hành vi của chủ đề cũ mà user đã đồng ý.
   - 6 chủ đề mới: bỏ máy trạng thái nhạc + `pointerInput` riêng, đọc `state.shownMedia`; giữ thông báo mới 10 giây và quy tắc "thông báo mới
     ẩn trình phát" của từng chủ đề; giữ quy tắc xem trước vừa sửa (`isPreviewShown` / `isPreviewFocused`).
6. **Xem trước**: không đổi (VM không chạy ở xem trước; `isMediaShown` mặc định `true`), thẻ đứng yên và ảnh chuẩn test không đổi.

## Kiểm

- Unit test `AodViewModel` (Intent → State): mở AOD có nhạc → hiện; hết giờ → ẩn; đổi bài → hiện lại; bấm nút → hẹn lại; chạm → bật / tắt;
  `mediaHoldSeconds = 0` → không ẩn. Dùng `StandardTestDispatcher` + `advanceTimeBy`, fake ở biên Manager như test hiện có (nếu VM khó
  dựng thì tách logic hẹn giờ thành class thuần `AodMediaHold` để test).
- Unit test `SettingsViewModel` lưu / đọc giá trị; `AodThemeSpecTest`, `AodStatePreviewTest` vẫn pass.
- `compileDebugKotlin`, `check-rules`, review `jetpack-compose-performance` + `anr-audit-and-fix`.
- Máy thật (user): bước mới ở README › Kiểm tra trên máy thật cho 30 giây / Không bao giờ, chạm màn, đổi bài, xoay ngang ở một chủ đề cũ
  (vd Rẻ quạt) và một chủ đề mới.

## Docs

README (Tùy chọn trong app: hàng `aod_media_hold_seconds`; Cách dùng: đoạn trình phát theo chủ đề sửa "hiện suốt khi có nhạc" thành "tự
ẩn theo cài đặt"; bước test mới), CHANGELOG, CLAUDE.md không cần đổi.

## Rủi ro

- Chủ đề cũ có bố cục ngang ba cột theo nhạc (Chuỗi, Sỏi…) sẽ chuyển cột mỗi lần trình phát ẩn / hiện (có `Crossfade`, nhưng là thay đổi
  thấy rõ): đã nêu cho user, user chấp nhận "đổi nhiều".
- Chạm một lần giờ phải chờ hết thời gian nhận chạm 2 lần (khoảng 300 ms) mới bật / tắt trình phát, vì đang có cử chỉ chạm 2 lần.
