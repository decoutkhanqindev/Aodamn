# Lỗi đã gặp khi làm chủ đề

Mỗi dòng là một lỗi đã xảy ra thật; kiểm từng dòng trước khi báo xong. Luật đã có ở CLAUDE.md / README không ghi lại ở đây.

## Vẽ

- **Glyph tự chồng khi vẽ nét** (Chồng số, Google Sans Flex số 4): chữ số cắt từ font variable có thể là một đường viền tự cắt;
  tô đặc thì đúng, vẽ nét / khoét viền thì lộ cạnh trong. App: gộp mỗi glyph một lần bằng `Path.op(glyph, Path(), UNION)` trước
  khi vẽ nét. Mock: `fontTools.ttLib.removeOverlaps` (cần `pip install --user skia-pathops`, chạy script mock không `-I`).
  Kiểm cả 0–9 dạng viền, không chỉ số đang hiện.
- **Vùng mờ / khoét che mất ảnh nền** (Nhịp giây): vùng mờ, lỗ cho chấm giây vẽ bằng `BlendMode.DstOut` / `Clear` trên lớp
  riêng (`graphicsLayer` offscreen), không phủ màu đen lên trên.
- **Chuyển động theo giây**: vòng / chấm giây tự chạy đồng hồ mỗi khung hình (`rememberLiveClockMillis`, đọc `longValue` ở
  pha vẽ, mẫu `AodSectorDial`), phần theo phút recompose bằng `derivedStateOf`. Không cấp phát brush / path mỗi khung: dựng trong
  `drawWithCache`.
- **Một nguồn giờ cho cả mặt số** (Quỹ đạo): mọi thứ thuộc mặt số chạy theo giờ thật (kim, hành tinh, vòng số và cả chữ như đồng hồ số
  giữa đĩa) đọc cùng giờ thật đó khi `state.isClockLive`; lấy `state.nowMillis` là giờ mẫu 08:30 ở màn chủ đề / Tùy chỉnh, lệch với phần
  đang chạy. Các khối ngoài mặt số (ngày, pin…) vẫn theo giờ mẫu. Kiểm ở màn chủ đề: mọi chữ số trên mặt số khớp kim / hành tinh.
- **Hằng số trong file Compose**: không `val` / `const val` cấp file (check-rules chặn); số đo vào `<Chủ đề>Metrics`.
- **Gradient trải theo khung của chính phần được tô** (hành tinh Quỹ đạo, chấm dòng Chuỗi, chấm tâm Bốn số): brush trải theo khung
  lớn hơn (cả đĩa, cả mặt số, cả hàng; `toBrush()` không truyền khung là trải theo cả vùng vẽ của composable, `toLineShader(from, to)` theo
  hai điểm truyền vào) thì phần nhỏ chỉ lấy một dải, trông như một màu. Mỗi phần (chấm, vạch, chữ, kim) dựng brush theo khung riêng của
  nó; phần chạy thì dựng một lần quanh gốc toạ độ (`Rect(center = Offset.Zero, radius = r)`) rồi `translate` tới vị trí mỗi khung hình.
  Cố ý trải chung nhiều phần (khung + vạch + số của Khung) thì nêu trong mock. Kiểm: chọn một gradient tương phản (Hoàng hôn) cho từng
  màu của chủ đề, nhìn từng phần có đủ dải màu không.
- **Khung nét chữ** (Đơn cách): `TextLayoutResult.getPathForRange` trả khung vùng chọn cả dòng (cao bằng dòng), không phải nét chữ;
  ô chữ ôm sát nét như `NoPadTextView` đo bằng `Paint.getTextBounds` với typeface từ `resolveTypeface`, cộng `firstBaseline` của layout.
- **`min` / `max` với `Dp`**: dùng `androidx.compose.ui.unit.min/max`, không phải `kotlin.math`.

## Bố cục

- **Khoảng cách giữ chỗ khi khối kề bên ẩn** (Khung, Đơn cách, Quỹ đạo; rà lại cả Vành, Viên nang, Thẻ, Tháp, Chuỗi, La bàn, Phai,
  Bán nguyệt, Rẻ quạt, Nhịp giây, Lời): `AnimatedVisibility` ẩn hẳn thì không dựng node nào, nên `padding` trên modifier của nó mất theo; lỗi
  nằm ở `padding` trên node vẫn dựng: khung bọc luôn có (`Row` / `Column` chứa các `AnimatedVisibility` như dòng ngày + thời tiết, dòng
  pin + đèn pin, danh sách của Chuỗi) hoặc khối đang hiện mang khoảng hướng về khối kề bên đã ẩn (trình phát cách dòng pin khi tắt pin).
  Khi đó cụm căn giữa (vùng màn ngang, phần tư của Khung) lệch nửa khoảng, cụm neo đáy bị đẩy lên cả khoảng. Khoảng giữa hai khối chỉ
  đặt khi cả hai đều hiện: viết `has<ChủĐề>Status()` / `is<ChủĐề>BatteryShown()`… theo đúng điều kiện hiện của khối (`AodPanelBlock`:
  sự kiện `events.isNotEmpty()`, ghi nhớ `extras.memo.isNotBlank()`; `AodStatusLine`: pin bật, đèn pin, icon; thông báo mới nhất;
  `AodWeather`: `weather != null`, rỗng thì vẫn dựng một ô 0dp) và dùng lại cho cả quyết định ẩn cột / thu vùng. Lề mép màn (khoảng
  tới đáy, tới đỉnh) không thuộc luật này. Kiểm: tắt lần lượt ngày, pin, thông báo, nhạc, lịch, ghi nhớ ở cả dọc lẫn ngang, cụm còn
  lại vẫn căn đúng.

## Dùng lại

- **Chép code của chủ đề cũ**: mỗi chủ đề chép trình phát (3 kiểu, 10 chủ đề), thông báo mới nhất (6), khung bố cục (Chồng số,
  4 góc), hàm làm tối brush (5), resolve typeface cho `Paint` (12), vòng lặp giờ thật (5) từ chủ đề trước rồi sửa; sau đó phải
  tách lại thành các khối ở [reuse.md](reuse.md). Chọn khối theo reuse.md trước khi viết từng phần.

## Tuỳ chọn và màn chỉnh

- **Một tab nhiều phần** (Nhịp giây: tab Đồng hồ có chip Đồng hồ / Vòng): mỗi điều khiển trong tab phải chỉ tác động đúng phần
  đang chọn. Thử từng chip với từng thanh / lựa chọn trong tab; thanh Cỡ chỉnh cỡ đồng hồ cả khi chọn chip Vòng là lỗi user bắt.
  User muốn giữ chung tab, tách điều khiển theo phần, không tách tab mới.
- **Điều khiển theo kiểu** (Gợn sóng): thẻ nhạc đổi đặc / viền theo kiểu mặt số như app mẫu; trước khi "sửa" một hành vi user
  báo, kiểm xem đó có phải hành vi của app mẫu không và giải thích trước.
- **Màu mặc định gần đen** (nền thẻ nhạc `#121218`): chạm vòng màu giữ độ sáng nên ra màu gần đen; mặc định tối thì kiểm cách
  chọn màu sáng vẫn dễ.
- **Mọi màu cho gradient** và phần vẽ tô được gradient thật (chữ, icon, vạch, Paint): bật cờ mà quên tô thì gradient chỉ lấy
  màu đầu.
- **`AodThemeSpecTest`**: tự vẽ thông báo mới nhất thì thêm vào tập `drawsLatest`; chạy theo giờ thật ở xem trước thì thêm vào
  `liveThemes`; thêm test riêng chặn chủ đề có lại khối đã bỏ và giữ đủ tuỳ chọn của app mẫu (khoảng cỡ, mặc định, thứ tự).

## Font

- Thêm font: chỉ sinh họ mới (`python -I tools/fonts/make_static_fonts.py <họ>`), không sinh lại họ cũ (đổi byte file đã có).
- Cần độ đậm 1000 hoặc hai họ cắt từ cùng một file variable (vd góc vuông / bo tròn ghim trục `ROND`): script đã hỗ trợ (tên
  `extrablack`, phần tử thứ ba của mục `FONTS` là họ của file nguồn).
- Kiểm file sinh ra không có `fvar`, có đủ chữ số và tiếng Việt.

## Git và worktree

- Không dùng `git stash`: các worktree dùng chung một stash. Cần cất file thì chép ra scratchpad rồi `git restore`.
- `git merge` / `git rebase` bị chặn vì cần chính user cho phép (CLAUDE.md): hỏi user, không tìm đường vòng.
- Không stage `local.properties`, `.idea/*`, `app/build.gradle.kts` hay file không phải của mình.
- Script sửa file giữ đúng kiểu xuống dòng (CRLF / LF) của file: đọc bytes, nhận diện `\r\n`, ghi lại cùng kiểu bằng `write_bytes`
  (`Path.write_text` trên Windows tự đổi `\n` thành `\r\n`, file LF như CHANGELOG thành CRLF).
- In chữ Việt từ Python trên console Windows: `python -I -X utf8` (hoặc `sys.stdout.reconfigure(encoding="utf-8")`).
