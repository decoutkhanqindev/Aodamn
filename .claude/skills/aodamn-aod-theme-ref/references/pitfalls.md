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
- **Cỡ chữ app mẫu đặt bằng `setTextSize(px)`** (Thẻ lật): tham số hiểu là sp nên cỡ thật nhân thêm density máy. Không chép hệ số từ máy test (560dpi); canh theo cỡ lớn nhất của app mẫu
  phải vừa khít ở density khoảng 3 và kiểm mọi kiểu ở mức lớn nhất không bị cắt. Font mẫu lạ thì xem ảnh thẻ trước khi chọn thay thế (Metropolis 1920 là hai đường mỗi thân số, không phải chữ thường).
- **Chủ đề vẽ cầu kì** (Nhật thực, user: "theme này cầu kì nên cần tối ưu về perf"): theo skill `jetpack-compose-performance`, ghi thẳng vào brief: một node `drawWithCache`; `Paint`, shader, `Path` tạo ở khối cache; giá trị đổi theo phút qua
  `rememberUpdatedState`, đọc ở `onDrawBehind`; list màu qua `remember(config)` để lambda giữ nguyên; chuyển động ngắn đọc `Animatable` ở pha vẽ (giãn chữ bằng `Paint.letterSpacing`, không qua `TextStyle`); không `BoxWithConstraints`.
- **`SolidColor` vào `aodTint`** (Nhật thực, Chớp mắt): brush khác null là mở một lớp offscreen cho từng icon. Màu đơn truyền brush null, chỉ truyền khi tuỳ chọn có gradient thật.
- **`pointerInput(Unit)` giữ lambda đầu** (Nhật thực): giá trị thường trong lambda chạm bị cũ mãi (có nhạc sau khi mở AOD mà chạm không ra trình phát). Đọc qua `rememberUpdatedState` hoặc biến state.
- **Lần tới listener trước khi kết luận chuyển động** (Chớp mắt): `postDelayed` / `setStartDelay` / `addListener` trỏ sang class khác (`jb/b`, `ab/k`). Bản đầu đọc riêng số 60–359 giây nên tưởng là nhịp chớp, thật ra là thời gian trước khi ngủ;
  nhịp chớp nằm trong listener, cả chuỗi ngủ 12 giây bị bỏ sót.
- **Kích thước đặt thẳng vào `Canvas` là px thô** (Chớp mắt, cỡ mắt 100): đo tỉ lệ trên ảnh thẻ để biết máy cơ sở (9,2% bề ngang = 100 / 1080), rồi quy về cạnh ngắn; đừng coi số đó là dp (bản đầu to gấp 2,75 lần).
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
- **Ngang: khối rải nhiều chỗ thì bật / tắt là lệch** (Kinh tuyến, loại A): mock đặt ngày cột trái, dòng pin cột phải, rồi nhạc dưới hai thẻ;
  user: "khi quay ngang bật tắt blocks bị lệch", rồi "thay vì start thì đưa các blocks center". Cách đã duyệt cho chủ đề này: hai thẻ cố định
  một cột, mọi khối gom thành một cụm ở cột kia, căn giữa cả ngang lẫn dọc, nhạc hiện thì cụm trượt mượt (`expandVertically`). Mock phải có
  khung tắt ngày (chỉ còn dòng pin) và khung đủ khối để thấy cụm vẫn cân.

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
- **Lựa chọn kiểu của chủ đề không cần ảnh từng cái** (Thẻ lật, user: "clock style thì kh cần preview từng cái đâu nhé"): chip tên chữ, khung xem trước
  đã cho thấy kiểu đang chọn; đừng thêm nhánh vào `aodChoicePreview` cho kiểu của riêng một chủ đề.
- **Màu mặc định gần đen** (nền thẻ nhạc `#121218`): chạm vòng màu giữ độ sáng nên ra màu gần đen; mặc định tối thì kiểm cách
  chọn màu sáng vẫn dễ.
- **Khối tự vẽ phải có đúng một tab** (`AodThemeSpecTest`, Chớp mắt): chủ đề gộp ngày, pin, nhạc vào một dòng vẫn cần ba tab, nên phải có màu riêng từng khối. Nêu trong mock ngay lúc chọn `ownBlocks`, đừng để lộ ra sau khi duyệt.
- **Màu chung Theme Color của app mẫu** (Chớp mắt, user: "đều chỉnh được components colors nên mục này không cần, như các theme đã có"): chủ đề đã có màu từng phần thì bỏ tab màu chung, mỗi màu độc lập, không viết
  chuỗi màu dự phòng (ô màu ở tab chưa chỉnh còn hiện sai màu mặc định). Ghi là bỏ trong bảng của mock.
- **Mọi màu cho gradient** và phần vẽ tô được gradient thật (chữ, icon, vạch, Paint): bật cờ mà quên tô thì gradient chỉ lấy
  màu đầu.
- **`AodThemeSpecTest`**: tự vẽ thông báo mới nhất thì thêm vào tập `drawsLatest`; chạy theo giờ thật ở xem trước thì thêm vào
  `liveThemes`; thêm test riêng chặn chủ đề có lại khối đã bỏ và giữ đủ tuỳ chọn của app mẫu (khoảng cỡ, mặc định, thứ tự).

## Mock và trình bày

- **Mock phải xem được trong chat**: một `show_widget` có mọi khung dọc + ngang, có chuyển động thì chạy được ngay trong đó ([mock.md](mock.md)).
  Chỉ gửi file HTML thì user phải hỏi "tôi xem mock ở đâu" (Thẻ lật, Dịu); gửi ảnh PNG tách rời cũng sai, user đòi lại cách vẽ thẳng (Dịu).
- **Nêu rõ chỗ khác các chủ đề đang có** (Chớp mắt, Nhật thực: nhạc thay chỗ và tự ẩn sau 30 giây như app mẫu, các chủ đề khác hiện suốt): bám app mẫu mà khác thói quen của app thì mock phải ghi là khác; chỉ ghi con số thì user
  duyệt mà không biết, lúc thử trên máy tưởng mất tính năng.
- **Font của mock**: chữ AOD dùng đúng font của chủ đề (số lớn `<path>` từ glyph, chữ nhỏ `<text>` + `<link>` font, [mock.md](mock.md)), tự kiểm
  không rơi về font mặc định (Dịu: mock dùng font mặc định của HTML nên khác app).
- **Vị trí đồng hồ theo loại chủ đề** ([layout.md](layout.md)): loại A (đồng hồ là điểm nhấn) dồn đồng hồ về một phía là sai (Dịu);
  loại B / C ép đồng hồ ra giữa làm block lệch là sai (Chớp mắt, Pha trăng). Hai lời sửa cùng đúng vì khác loại.
- **Không gợi ý chỉnh vị trí block**: mock không có tay cầm, nút hay chú thích "kéo / đổi chỗ"; khung cùng hướng giữ nguyên anchor
  (Chớp mắt và Pha trăng bị làm lại nhiều lần vì lỗi này).
- **Khung dùng chung đã có**: reuse.md chỉ khung nào thì dùng khung đó (`AodHeroInfoLayout`) hoặc ghi lý do bỏ trong mock; bỏ im
  lặng là lỗi (Dịu).

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
