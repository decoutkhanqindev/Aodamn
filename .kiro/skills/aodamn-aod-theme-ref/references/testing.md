# Test tự động cho chủ đề AOD

Nguyên tắc: tối ưu bằng lọc và gộp, không bỏ bằng chứng.

`ThemeSpec → case tự sinh → test nhanh → test máy → báo cáo`

Bộ test dùng chung nằm ở `app/src/{debug,androidTest}/java/com/decoutkhanqindev/aodamn/testing/`. Mỗi theme chỉ thêm một
`ThemeExpectation`; không chép harness, quyền, flow, screenshot hay test block.

## Nhóm chạy

| Nhóm | Cờ script | Khi chạy | Nội dung |
|---|---|---|---|
| Fast | không cờ | một lần khi code đã ổn; chạy lại khi spec, config hoặc logic đổi | compile, contract, option, pairwise, lưu cấu hình, rule, đăng ký |
| Theme | `--device` | trước báo xong; script tự chọn nhóm có file đổi | đúng theme ID: behavior, screens, options, editor, motion |
| Editor | `--editor` | chỉ khi preview hoặc control editor đổi sau lần Theme | đúng theme ID: panel editor |
| Record editor | `--record-editor` | sau khi ảnh panel mới đã được xem | chỉ ghi ảnh panel editor |
| Record theme | `--record` | một lần sau khi ảnh theme mới đã được xem | chỉ ghi theme ID đang làm |
| Shared | `--blocks` | chỉ khi code block chung đổi | chỉ ảnh mọi layout của block chung |
| Record shared | `--record-blocks` | chỉ khi block chung được duyệt đổi | chỉ ghi ảnh block chung |

Lint, toàn bộ unit và toàn bộ test máy thuộc cổng phát hành, không thuộc vòng làm một theme. `connectedDebugAndroidTest` không được gọi trần khi làm một theme.

## Chọn nhóm và dấu pass

Script băm nội dung đầu vào của từng nhóm. Dấu pass nằm tại `build/theme-tests/cache/<gói>/<nhóm>.sha256`.
Chỉ ghi dấu sau khi cả nhóm pass. `clean` xóa dấu, nên lần kế tiếp chạy lại để tạo bằng chứng mới.

| Nhóm | Đầu vào chính | Test |
|---|---|---|
| `behavior` | spec, ViewModel, DataStore, flow | mở theme, apply, reset, reopen |
| `screens` | bố cục, phần vẽ, block dùng chung | tám ảnh toàn màn |
| `options` | spec, phần vẽ, case tự sinh | option board |
| `editor` | preview, control, font | panel editor |
| `motion` | phần vẽ, mốc chuyển động | ba khung chuyển động |
| `blocks` | block và style dùng chung | ảnh block dùng chung |

`--device` gửi mọi nhóm đổi vào một lần chạy máy. Runner chỉ nạp class thuộc các nhóm đó.
Nếu một nhóm lỗi, script vẫn ghi dấu cho các nhóm đã pass. Lần sau chỉ nhóm lỗi chạy lại.
`--editor` và `--blocks` giữ phạm vi hẹp. `--force` bỏ qua dấu pass khi cần kiểm lại.

## Một ThemeExpectation

`ThemeExpectation` nằm trong debug source để JVM và androidTest đọc cùng một dữ liệu. Nó giữ theme, option key, component được phép,
`ownBlocks`, nội dung thông báo, live preview và mốc chuyển động.

Case tự sinh từ `ThemeSpec`:

| Option | Case JVM / ảnh board |
|---|---|
| Choice | mọi lựa chọn |
| Range | thấp, mặc định, cao |
| Toggle | tắt, bật |
| Color | một màu, gradient |
| Pairwise | mọi cặp option cùng đổi |
| Component | trống, mặc định, dữ liệu dài |
| Layout | dọc, ngang |
| Motion | đầu, giữa, cuối đúng mili giây |

`AodThemeExpectationTest` chạy phần lớn case trên JVM. Nó kiểm từng option đổi được config, mọi cặp cùng tồn tại, reset về mặc định và
chuỗi lưu đọc lại không mất dữ liệu.

## Bằng chứng ảnh

Mỗi theme giữ đủ tám ảnh toàn màn:

- Dọc: REFERENCE, MINIMUM, DEFAULT, MAXIMUM.
- Ngang: REFERENCE, MINIMUM, DEFAULT, MAXIMUM.

`AodThemeOptionScreenshotTest` bổ sung một ảnh lưới, tự vẽ mọi Choice, ba mức Range, hai Toggle và solid / gradient. Nó không thay tám ảnh.

`AodThemeEditorScreenshotTest` chụp panel editor thật. Nó giữ khung preview và chữ không bị cắt trên kích thước máy chuẩn.
Chỉ chạy lại lớp này bằng `--editor` khi preview hoặc control editor đổi.

`AodThemeMotionTest` dùng Dropshots cho đúng khung 0 / giữa / cuối đã duyệt. Không chỉ đếm số pixel đổi. Ảnh lỗi phải có ảnh mới, ảnh chuẩn
và ảnh diff.

`AodSharedBlockScreenshotTest` giữ ảnh mọi layout ngày, pin, thời tiết, lịch, ghi nhớ, thông báo, nhạc, nét vẽ. Theme mới không chạy lớp này.
Chỉ `--blocks` hoặc `--record-blocks` mới chạy block chung.

## Flow và dữ liệu

`ThemeTestActivity` chỉ có trong APK debug. Mỗi hướng mở Activity một lần, đổi bốn state rồi chụp liên tiếp. Một theme mở hai Activity thay
vì tám. Profile MAXIMUM dùng gradient khác nhau cho nền và chữ, nên không ghi ảnh chữ chìm vào nền.

`AodThemeFlowTest` dựng thẳng `AodTabContent`. `AodThemeEditorBehaviorTest` đổi option, reset, apply, tạo ViewModel mới và kiểm config đã lưu.
`ThemeTestActivity` dựng thẳng `AodContent`. Không lớp nào chạy Splash, onboarding, mạng, quảng cáo, lịch thật hay nhạc thật.

Không hardcode `canRequestAds=false` trong release. Test host không tạo `AdsManager`, nên quảng cáo chỉ bị tắt trong môi trường test.

`ThemeDeviceRule` chuẩn bị đúng một lần trong mỗi process: `isFirstOpen=false`, AOD tắt, quyền runtime / overlay / notification listener đủ,
giờ 24h, font scale 1.0 và ba animation scale 1.0.

## Máy ảo cố định

Chỉ dùng AVD `Pixel_7_Pro`, serial mặc định `emulator-5554`, API 37, 1440 × 3120, density 560. Script dừng nếu có thêm máy nối hoặc sai AVD.

```powershell
android emulator start Pixel_7_Pro
adb devices -l
```

Có thể đổi serial bằng `AOD_TEST_SERIAL` và AVD bằng `AOD_TEST_AVD`. Mặc định vẫn là `emulator-5554` và `Pixel_7_Pro`.
Quyền Xiaomi riêng của ROM vẫn kiểm tay trên máy Xiaomi.

## Lệnh

```powershell
bash .kiro/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói> "<Tên Việt>"
bash .kiro/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói> "<Tên Việt>" --device
bash .kiro/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói> "<Tên Việt>" --editor
bash .kiro/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói> "<Tên Việt>" --record-editor
bash .kiro/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói> "<Tên Việt>" --record
bash .kiro/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói> "<Tên Việt>" --blocks
bash .kiro/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói> "<Tên Việt>" --device --force
```

Fast và các nhóm máy giữ dấu pass theo nội dung file. Nếu không có đầu vào đổi, script báo `SKIP` và không mở máy.
Một lần `--device` có thể chạy nhiều nhóm đổi, nhưng chỉ build và cài APK một lần.

Record chỉ chạy sau khi case rẻ pass và ảnh thực tế đã được xem. Không record để biến ảnh lỗi thành ảnh chuẩn. Record theme truyền đúng
`themeId`, nên không ghi lại theme cũ. Cờ record luôn bị xóa khi script kết thúc.

Mỗi lần chạy có `run_id` và thư mục riêng dưới `build/theme-tests/`, gồm log, báo cáo Android và diff Dropshots. Không đọc log của lần cũ để
kết luận lần mới.