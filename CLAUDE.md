# CLAUDE.md

Hướng dẫn cho Claude Code (và dev) khi làm việc trong project này. Code mới phải theo đúng rule dưới
đây, không trôi về pattern Android/Compose chung chung. Code hiện có mâu thuẫn với rule → theo rule
và báo lại.

## Project

**Custom AOD**: Always On Display giả lập. Mỗi lần màn hình tắt, app mở một Activity nền đen có đồng
hồ che màn hình khoá rồi tự bật màn hình lại. Repo:
[decoutkhanqindev/Custom-AOD](https://github.com/decoutkhanqindev/Custom-AOD).

- `com.decoutkhanqindev.custom_aod` · single module `:app` · minSdk 30 · targetSdk 37 · phiên bản
  thư viện: `gradle/libs.versions.toml`.
- Compose · Clean Architecture + MVI · Navigation 3 · Koin · Coroutines/Flow. Theme luôn tối (nền
  gradient chéo mint → đen → tím, accent mint; màn AOD và khung xem trước AOD nền đen).
- Chỗ còn phải làm có `// TODO` (Android Studio › View › Tool Windows › TODO). Build, cấp quyền,
  test trên máy: [README.md](README.md).

## Commands

- Verify nhanh: `./gradlew :app:compileDebugKotlin`
- Kiểm rule: `bash tools/check-rules.sh` (phải ra 0 vi phạm)
- Build, test, lint: `assembleDebug` · `testDebugUnitTest` · `lint` (release cần signing trong
  `local.properties`, xem README).
- Build treo → `./gradlew --stop`. Báo thiếu resource dù file còn (sau khi đổi tên / di chuyển
  `res/`) → `./gradlew --stop` rồi `./gradlew clean assembleDebug --no-configuration-cache`.

## Hard rules ⚠️

- **Git — KHÔNG tự chạy khi chưa được phép:** `git commit` · `git push` / `--force` ·
  `git reset --hard` · `git rebase` · `git merge` · `git branch -D`.
- Commit message không thêm dòng `Co-Authored-By` (hay trailer đồng tác giả nào khác).
- Thêm dependency, permission Manifest, hoặc xoá code/resource đang dùng → hỏi trước.
- Xong việc phải: `./gradlew :app:compileDebugKotlin` pass + chạy `bash tools/check-rules.sh` (các
  lệnh grep kiểm tra) ra 0 kết quả.

## Kiến trúc

Clean Architecture + MVI trong 1 module `:app`. Ranh giới layer chỉ là package nên rule import là
bắt buộc (compiler không chặn hộ):

- `domain/` (model, repository interface, usecase) thuần Kotlin: KHÔNG `android.*`, `androidx.*`,
  Compose, Koin, `data`, `presentation`, `ads`.
- `data/` (repository Impl, `local/`, `network/`, `device/`, `mapper/`): KHÔNG `presentation`,
  Compose, `ads`. Retrofit + kotlinx-serialization chỉ trong `data/network/api`.
- `presentation/` (`aod/`, `screens/<màn>/`, `components/`, `model/`, `navigation/`, `theme/`,
  `base/`, `effects/`): dùng `domain`, Manager ở `data/` và `ads/`; KHÔNG `data` Impl / Repository /
  DataSource.
- `ads/`: KHÔNG `domain`, ViewModel, screen (chỉ `ads/composables` được import
  `presentation/components` + `theme`). `utils/`: KHÔNG import package nào của app. `di/` (Koin):
  import mọi layer.
- Dữ liệu nghiệp vụ: ViewModel → UseCase → Repository (interface ở `domain`, Impl ở `data`, bind ở
  `di`). Hạ tầng app-shell (ngôn ngữ, cờ lần đầu mở, mạng, thiết bị, ads) đi qua **Manager**, inject
  thẳng nơi dùng, không bọc UseCase / Repository. Qua biên layer phải map: DTO → domain (mapper ở
  `data`) → UiModel (`toUiModel()` ở `presentation/model/<tính năng>/`).
- Runtime AOD (`presentation/aod/`, `data/device/`) mã hoá hành vi phụ thuộc ROM: đọc comment "tại
  sao" ngay tại code và README › Các quyết định thiết kế trước khi sửa.
- Chủ đề AOD (`model/aod/theme/`): một chủ đề = mục `AodThemeValue` (id lưu, không đổi) + `AodThemeSpec`
  (tab và thuộc tính `AodThemeOption`: lựa chọn / màu / khoảng số / bật tắt, giá trị Int trong
  `AodThemeConfig`; khối tự vẽ `ownBlocks`; khối dùng chung có hiện `components`) ở `model/aod/theme/<chủ đề>/` +
  phần vẽ ở `components/aod/theme/<chủ đề>/` nối vào `AodPortrait` / `AodLandscape` (bố cục dọc, ngang
  riêng từng chủ đề, `theme/AodThemeLayout.kt`; ngang dựng trên bố cục chung theo khối đang hiện `AodLandscapePlan` +
  `AodLandscapeLayout`, mock bằng `tools/mockups/` và chốt với user trước khi code) và `AodMediaControls`. Các bước
  thêm chủ đề và những chỗ dễ vướng: README › Thêm chủ đề mới. Control dựng theo loại thuộc tính (`AodThemeOptionList`), không viết màn
  riêng cho chủ đề; thêm thuộc tính không đổi cách lưu. Khối dùng chung (ngày, pin, thời tiết, sự
  kiện, ghi nhớ, thông báo, nhạc, nét vẽ) có kiểu hiển thị (`Aod*LayoutValue`), font / cỡ / màu riêng
  từng khối (`AodComponentSpec`, `AodAppearanceUiModel.components`, chỉnh ở Tùy chỉnh) và không viền, không
  nền; lựa chọn có ảnh xem trước vẽ bằng chính composable của AOD (`aodChoicePreview`). Khung xem trước
  ngoài AOD thật không chạy theo giờ: giá trị mẫu cố định từ `CustomizePreviewSamples` (giờ 08:30), trừ chủ đề có chuyển động theo giờ thật bật
  `AodThemeSpec.isLiveInPreview` (La bàn: mặt số chạy theo giờ thật ở màn chủ đề, Tùy chỉnh; thẻ ở tab AOD vẫn đứng yên, `isStill`; các khối khác vẫn mẫu); khối chủ đề tự vẽ
  khai báo trong `ownBlocks` (`AodBlockValue`: ngày, nhạc, dòng trạng thái, ghi nhớ, sự kiện), kiểu do chủ đề quyết định, và luôn có trong
  xem trước chủ đề (`toThemePreviewDraft`). Tùy chỉnh không chỉnh chủ đề: tab Đồng hồ lúc onboarding
  chỉ có Cổ điển (`ClassicThemeSpec.ClockTab`), sau đó là dòng dẫn sang màn chủ đề. Tab của chủ đề chia theo
  phần như khối ở Tùy chỉnh (kiểu rồi màu của chính phần đó, không gom màu vào tab riêng), tab nào cũng có
  icon đặt lại riêng ở thanh tiêu đề panel (`AppEditorPanel(action)` + `AppEditorResetButton`), cả ở Tùy chỉnh.
  Chủ đề mới: số đo lấy từ dịch ngược app mẫu (không xin user ảnh chụp); bám sát app mẫu: đọc code vẽ + layout trước khi mô tả / đề
  xuất, giữ đủ mọi tuỳ chọn, chi tiết và chuyển động (vd kiểu có / không vòng giây, vòng giây xoay theo giờ thật, không bỏ vì AOD đếm theo
  phút); không tự bỏ / gộp tuỳ chọn hay tính năng của app mẫu: mock liệt kê từng tuỳ chọn của app mẫu là giữ / đổi / bỏ, đổi hay bỏ phải
  nêu lý do và hỏi user (ghi ở comment / CHANGELOG không tính); chỗ làm xịn hơn được thì cứ làm và nêu trong mock; mock dọc + ngang để
  user duyệt rồi mới code; thêm
  các khối dùng chung thật sự hợp bố cục (điểm khác đối thủ), không nhồi khối / tuỳ chọn chỉ vì app hỗ trợ, khối không hợp thì bỏ khỏi `components` và nêu
  lý do trong mock; khối đặt trong bố cục đặc trưng thì trông như dòng của chủ đề (không được thì chủ đề tự vẽ, vd ghi nhớ / sự kiện của
  Chuỗi), màn ngang giữ dạng đặc trưng như màn dọc và tự đánh giá riêng từng chủ đề cho cân, đầy màn (không rập khuôn thu nhỏ đồng hồ
  / chia 2–3 cột), khối không đặt cân được ở cả dọc lẫn ngang thì bỏ hẳn khỏi chủ đề, sửa mock xong vẽ lại cả bản; không sửa / refactor code của chủ đề đang có (cả phần dùng
  chung được), chỉ thêm nhánh `when` của chủ đề mới; code dùng chung chỉ sửa khi chủ đề mới cần, hỏi trước, chỉ thêm (mục enum / thuộc
  tính mới có mặc định cho đúng hành vi cũ), chủ đề đang chạy không được đổi gì (`git diff` thư mục chủ đề cũ trống, `AodThemeSpecTest`).

## Presentation: MVI

- Đặt tên theo code có sẵn (`XxxScreen` / `XxxContent` / `XxxViewModel` +
  `state/{XxxState, XxxIntent, XxxEffect}`, `XxxUiModel` + `toUiModel()`, enum giá trị UI
  `XxxValue`). Component dùng chung (`presentation/components/`) là `AppXxx`, file cùng tên; riêng
  AOD (`components/aod/`) là `Aod…`, chia thư mục: gốc ghép màn AOD, `block/` khối dùng chung, `theme/` chọn
  theo chủ đề + `theme/<chủ đề>/` phần vẽ riêng, `editor/` control chỉnh của Tùy chỉnh / màn chủ đề; riêng một màn (`screens/<màn>/components/`) là tên thường theo
  việc nó làm.
- `XxxScreen` lấy ViewModel (`koinViewModel()`), collect `state` + `effect`, điều hướng,
  `koinInject()` manager, load / show ad full-screen. `XxxContent` là UI thuần nhận `state` +
  `onIntent`, không biết ViewModel / Koin / `NavBackStack` / Activity (`@Preview` là `private`, bọc
  `Theme`). Copy `screens/language/` làm khung.
- `XxxViewModel : BaseViewModel<S, I, E>` (đã implement `Tag`: dùng `Timber.tag(tag)`, không khai
  báo lại). `onIntent` là cửa duy nhất UI gọi vào, `when` exhaustive không `else`; chỉ đổi state qua
  `updateState { copy(…) }`; tác vụ có thể bị gọi chồng thì giữ `Job?` và `cancel()` trước khi
  launch. Constructor nhận UseCase (+ Manager), KHÔNG nhận `Context` Activity, `NavBackStack`,
  Repository, DataSource, `AdsManager`. Map domain → UiModel trong ViewModel; text cho user là
  `@StringRes`.
- State là `@Immutable data class` với default cho mọi field, list là `ImmutableList`; dialog /
  bottom sheet đang mở là field (`showXxx`) + cặp Intent `ShowXxx` / `DismissXxx`. Effect chỉ cho
  việc 1 lần (điều hướng, toast, Intent hệ thống, service, ad) và **mất** nếu phát lúc không có
  collector, nên thứ user bắt buộc phải thấy thì đưa vào State.
- Screen collect effect:
  `LaunchedWithLifecycleEffect { viewModel.effect.collectCatching(block = …, catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) }) }`
  với `val tag = LocalTag.current`. Không `repeatOnLifecycle` thô; việc gắn với RESUMED dùng
  `LifecycleResumeEffect`.
- Trạng thái đang xử lý (`isApplying`, `isSaving…`) phải có loading ở UI: user không được làm gì
  khác thì dùng lớp phủ chặn chạm + Back (mẫu `CustomizeApplyingOverlay`), chỉ ảnh hưởng một dòng
  thì đổi chữ / vòng xoay tại chỗ.
- Component của màn nằm trong Content hoặc `screens/<màn>/components/` (chỉ màn đó dùng; màn khác
  không import từ `screens/<màn khác>/` trừ `state/`). Dùng ≥ 2 màn → `presentation/components/`.
- Tên Intent: động từ đứng đầu, có đối tượng, theo hành động của user. `Toggle<X>(isEnabled)` (mang
  giá trị mới) · `Change<X>(value)` · `Select<X>(item)` · `Confirm<X>` · `NavigateTo<Màn>` /
  `NavigateBack` · `Open<Đích>` (trang cài đặt kết thúc bằng `Settings`) · `Show<Dialog>` /
  `Dismiss<Dialog>` · `PerformGesture(gesture)` · kết quả từ hệ thống `<Thứ>Result(value)` /
  `<Thứ>Shown`.
- Tên Effect: `NavigateTo<Màn>` · `NavigateBack` · `Open<Đích>` · `Request<Quyền>Permission` ·
  `Start<Service>` / `Stop<Service>` · `ShowMessage(@StringRes)` · `Close<Màn>`. Intent chỉ để phát
  một Effect thì trùng tên Effect; hàm xử lý trong ViewModel cùng tên (`ConfirmLanguage` →
  `confirmLanguage()`). Không dùng danh từ trơn (`Preview`), nhãn nút (`Done`, `Back`), thì quá khứ
  (`BackgroundPicked` → `BackgroundPickerResult`), thiếu đối tượng (`Close` → `CloseAod`). Boolean
  là `isXxx`; số có đơn vị trong tên (`percent`, `minuteOfDay`).
- Màn có ≥ 3 cụm thao tác độc lập hoặc ≥ 15 Intent (`screens/settings/`, `screens/customize/`) thì chia
  cụm, không gom phẳng: `sealed interface <Cụm> : XxxIntent` / `: XxxEffect` lồng trong cha (việc
  chung cả màn ở cấp cha, gọi bằng đường đầy đủ `CustomizeIntent.Appearance.ToggleDate(it)`);
  `onIntent` chỉ `when` theo cụm rồi giao `on<Cụm>Intent`; State = field chung + `Xxx<Cụm>State` +
  UiModel chia cụm, composable của cụm chỉ nhận sub-state của cụm. Màn nhỏ giữ phẳng.
- Màn chia cụm thu effect theo cụm, mỗi cụm một
  `LaunchedWithLifecycleEffect { viewModel.effect.filterIsInstance<XxxEffect.<Cụm>>().collectCatching(…) }`
  với `when` đủ nhánh của cụm (cụm một effect thì lọc thẳng theo effect đó). Thêm cụm hoặc lá effect
  mới phải thêm block ở Screen, compiler không nhắc.

## Compose và theme

- `presentation/theme/` chỉ có `Colors.kt` · `Typography.kt` · `Shapes.kt` · `Theme.kt`, toàn `val`
  cấp file; UI **import từng tên rồi đọc thẳng** (`Mint`, `BodyLarge`, `RoundedCornerShape16dp`).
  Không `MaterialTheme.*`, hex, `Color.White/Black`, `RoundedCornerShape(…)` ngoài `theme/`, không
  `.copy(alpha = …)` trên màu theme (dùng biến thể `MintAlpha12`). Màu / chữ / bo góc mới → thêm
  `val` vào đúng file. `Theme { }` chỉ là cầu nối cho component Material 3. Không dựng tầng token
  hay object trung gian (đã thử và bỏ). Khoảng cách, `sp`, thời lượng, alpha viết số thẳng.
- Font trong `res/font` là file TTF tĩnh từng độ đậm, sinh từ font variable (font chỉ có bản tĩnh thì chép từ `tools/fonts/static/`) bằng
  `tools/fonts/make_static_fonts.py <họ font>` và khai báo `Font(R.font.x_bold, FontWeight.Bold)` ở `Typography.kt`. Thiếu font (vd font
  của app mẫu) thì tự tải bản Google Fonts (OFL, giấy phép vào `licenses/`), không hỏi; chỉ khi Google Fonts không có hay font thiếu
  tiếng Việt mới dùng font có sẵn gần nhất;
  không dùng font variable + `FontVariation` (Compose áp trục `wght` qua `Paint`, máy Xiaomi thay bằng font
  hệ thống).
- Nền màn app là `BackgroundGradient` (`Colors.kt`), vẽ một lần ở `AppNavDisplay`; màn, tab và
  `Scaffold` để trong suốt (`containerColor = Color.Transparent` + `contentColor = GreyED`), không vẽ
  lại. Màn AOD và Customize (khung xem trước AOD) tự vẽ nền đen.
- Thanh trên của màn dùng `AppTopBar` (căn giữa, nền trong suốt, chữ và icon `GreyED`); không gọi
  `TopAppBar` / `CenterAlignedTopAppBar` thẳng, không tô màu riêng cho icon trên thanh. Mint để cho
  trạng thái đang chọn / giá trị.
- Icon là VectorDrawable Phosphor (`res/drawable/ic_*.xml`, sinh từ `tools/icons/icons.txt` bằng
  `tools/icons/import_phosphor.py`, không sửa tay) qua `painterResource`; không dùng `Icons.*` /
  `ImageVector` của Material. Độ đậm theo chỗ: regular cho thanh / nút, fill cho tab đang chọn,
  duotone cho ô `AppIconTile`, light trên màn AOD (chủ đề chữ số đậm như Sỏi dùng fill).
- Ảnh nền AOD mã 7–18 (`img_wallpaper_*.xml` có dòng "Sinh bằng") do `tools/wallpapers/generate_wallpapers.py`
  sinh, không sửa tay. Thêm ảnh = hàm vẽ + dòng `WALLPAPERS` + mục `WallpaperValue` (alignment khớp dòng
  script in ra) + chuỗi vi / en; vẽ chừa vùng đồng hồ, không logo / nhân vật / công trình có bản quyền.
- Slider và thanh tiến trình chỉ qua `AppSlider`: nhận `progress: () -> Int` và đọc trễ (snapshotFlow,
  pha layout / vẽ), không đọc state trong composition; `onProgressChange = null` là chỉ hiển thị (không
  qua Slider của Material, `progress` gọi ở pha vẽ); thanh vẽ riêng (thanh nhạc gợn sóng) truyền qua
  `track`.
- Overlay không dùng bản Material: sheet / hộp xác nhận là `AppSheet` / `AppConfirmSheet` (`components/sheet/`, đầu sheet
  `AppSheetHeroHeader` có hình minh hoạ hoặc `AppSheetToolHeader`), nút là `AppPrimaryButton` (việc app muốn user làm, trên
  cùng) / `AppDangerButton` (mất dữ liệu) / `AppTextButton` (Hủy, Để sau), chọn một là `AppOptionRow`, giờ là `AppTimeWheel`,
  thông báo ngắn là `AppToastHost` (Screen giữ `SnackbarHostState`, effect `ShowMessage(isError)`); hình minh hoạ sinh bằng
  `tools/brand/generate_illustrations.py`. `tools/check-rules.sh` chặn AlertDialog / ModalBottomSheet / Button… gọi thẳng.
- Màn AOD (`screens/aod/`) **không bọc `Theme`**, cả `@Preview`: bọc thì `Text` nhận
  `LocalTextStyle` làm đồng hồ đè lên dòng ngày. Cỡ chữ ở đó là `TextUnit` (`14.sp * scale`), chữ
  xám (`Grey8A` / `Grey6E` / `Grey5A`) để ít sáng, ít burn-in.
- Text qua `stringResource` / `LocalResources.current`, không hardcode, không `context.getString`
  (Context của Activity bỏ qua locale override).
- Click dùng `Modifier.onClick` (debounce 300ms), nhấn giữ dùng `Modifier.onLongClick`, không `clickable`; hàng công tắc / radio dùng
  `toggleable` / `selectable` cho TalkBack. Lottie chỉ qua `AppLottie`. Component nhận
  `modifier: Modifier = Modifier` đầu tiên và áp vào node gốc.
- `LaunchedEffect` chỉ khi body gọi hàm suspend; việc đồng bộ chạy lại theo key dùng
  `SideEffect(key…)`. State đổi phải có animation, không snap. Không truyền ViewModel /
  `NavBackStack` / `MutableState` xuống Content.
- Composable và giá trị theme chỉ `private` hoặc public, không `internal`. File Compose không khai
  báo `val` / `const val` / class phụ cấp file (hằng của logic → `companion object` của ViewModel /
  UiModel; pattern định dạng → `strings.xml` `translatable="false"`; Intent / hằng hệ thống →
  `utils/ContextExt.kt`); ngoại lệ duy nhất là `LocalXxx` dùng chung (`LocalTag`) ở
  `presentation/components/`.
- Trước khi viết composable / util mới, xem `presentation/components/` và `utils/`; dùng ≥ 2 nơi →
  để ở đó, 1 nơi → `private`. Component dùng chung không tự `koinInject`.

## Navigation 3

- Chỉ Navigation 3. Destination là `@Serializable : NavKey` (`navigation/AppDestinations.kt`), args
  chỉ primitive / `@Serializable`. Màn mới = thêm `screen<XxxDestination> { dest -> XxxScreen(…) }`
  trong `AppNavDisplay` (hàm bọc `entry`, tự cấp `LocalTag` = tên class của destination;
  `AodActivity` tự cấp `"AodScreen"`). `NavDisplay` phải đủ 2 decorator (saveable state holder +
  ViewModel store).
- Main = `MainNavDisplay` (theo mẫu `AppNavDisplay`): `Scaffold` chứa thanh trên chung của tab
  (`MainTopBar`: tiêu đề theo tab, IAP / Share xử lý ngay đó), `NavDisplay` của các tab và thanh tab (`AppEditorTab` như thanh tab của màn sửa);
  không ViewModel. Tab là Screen đủ bộ (`AodTabScreen` / `SettingsScreen` có ViewModel và effect
  riêng; `EdgeLightScreen` còn là màn giữ chỗ), không tự vẽ thanh trên hay đệm thanh trạng thái;
  destination ở `navigation/MainDestinations.kt`, thứ tự, icon và tiêu đề ở `MainTabValue`. Tab AOD là gốc back stack tab; `screens/aod/` là màn AOD thật trên màn hình khoá.
- Chỉ Screen đụng `backStack`, ViewModel bắn Effect. Thay cả stack:
  `navigateTo(dest, preserveState = false)`; `navigateTo(dest)` xoá mọi màn giữa root và dest nên mở
  màn chồng lên dùng `backStack.add`; quay lại luôn `navigateBack()` (không `removeLast…`: back
  stack rỗng làm NavDisplay crash).
- Mất mạng được quan sát từ `NetworkManager`: `AppNavDisplay` hiện `AppNoInternetDialog` phủ mọi
  màn, màn không cần trạng thái offline riêng.

## Coroutines, Manager và DataStore

- Dùng helper ở `utils/CoroutineExt.kt` (`-Catching` = rethrow `CancellationException`, bắt phần còn
  lại): `suspendRunCatching` (UseCase 1 lần → `Result`) ·
  `withContextCatching(context, block, catch)` (Repository / Manager: đổi dispatcher + log + giá trị
  dự phòng) · `collectCatching` / `collectLatestCatching(block, catch)` (**mọi** chỗ collect flow
  của manager / UseCase và `effect`; Latest khi cần huỷ khối cũ lúc có giá trị mới) ·
  `recoverCatching` (trước `shareIn` / `stateIn`). Không dùng bản thô (`runCatching` trong suspend,
  `withContext`, `.collect { }`), không tự `catch (CancellationException)`; hàm đồng bộ của hệ thống
  ném exception có tên (`startForegroundService`) thì `try/catch` đúng loại đó.
- Dispatcher chọn ở Repository / Manager, không ở ViewModel / UseCase. UseCase 1 lần trả
  `Result<T>`; observe trả `Flow<T>`, không bọc `Result`.
- Không có exception riêng của domain: lỗi nguồn dữ liệu (mất quyền, mất mạng, API lỗi) xử lý ngay ở
  Impl trong `catch` của `withContextCatching` (`Timber.w` + giá trị dự phòng có nghĩa), không trả
  `null` để báo lỗi.
- Manager = 1 class `XxxManager` (không interface + `Impl`), Koin `single`, tự tạo scope
  `SupervisorJob() + Dispatchers.IO/Default`, expose `StateFlow` nóng, lỗi xử lý trong manager
  (không trả `Result`), chỉ primitive / ISO code qua biên. Signal hạ tầng mới → manager mới, không
  UseCase / Repository / `object` trong `utils/`. Manager tín hiệu thiết bị (`data/device/`) dùng
  `WhileSubscribed(replayExpirationMillis = 0)`. Dùng ở Screen / host bằng `koinInject()`, ViewModel
  / Repository qua constructor, Activity / Service bằng `by inject()`; `KoinComponent` chỉ ở
  `AdsManager`, `AdUnit`, `BootReceiver`.
- `DataStoreManager`: mỗi key 1 `StateFlow` primitive (không gộp data class, không enum / mapper),
  thêm key theo mẫu có sẵn. Giá trị khởi đầu `null` = chưa đọc xong: đọc 1 lần bằng `.value` (`null`
  → `DEFAULT_*`) hoặc `filterNotNull().first()`. `save*` chạy trên scope của manager nên không bị
  huỷ khi rời màn. Phải là 1 instance.

## Ngôn ngữ, onboarding, resources

- Ngôn ngữ lưu ISO code ở `DataStoreManager.selectedLangCode`; `AppLanguageProvider` bọc
  `setContent` của `MainActivity` và `AodActivity`, provide `LocalConfiguration` / `LocalResources`
  nên đổi ngôn ngữ không recreate Activity. Không đọc kích thước màn hình từ `LocalConfiguration`
  (đã bị override): dùng `LocalWindowInfo` / `BoxWithConstraints`. Giữ
  `bundle { language { enableSplit = false } }`.
- Thêm bản dịch = `values-<qualifier>/strings.xml` + `LanguageValue.TRANSLATED`. Qualifier khác
  code: `id → in`, `he → iw`, `zh-hk → zh-rHK`, `es-la → es-rLA`, `pt-br → pt-rBR`, `fil → b+fil`.
- Onboarding lần đầu: Splash → Language → Customize → Permission → Main; các lần sau Splash → Main.
  `saveIsFirstOpen(false)` gọi ngay ở nút Tiếp tục của Language, nên thoát giữa chừng thì lần sau
  vào thẳng Main (thiếu quyền thì tab AOD hiện `AodTabPermissionSheet`). **Bỏ qua ở Customize vào
  thẳng Main**, không qua Permission (ép cấp quyền sau khi user đã bỏ qua thì dễ bỏ app); Áp dụng thì
  đủ quyền bắt buộc vào Main, thiếu thì sang Permission. **Bỏ qua ở Permission** (chỉ hiện khi còn thiếu quyền) vào
  `MainDestination(isPermissionSkipped = true)`: sheet quyền của tab AOD bắt đầu ở trạng thái đã đóng nên không tự hiện
  trong phiên đó (còn dòng cảnh báo, bật AOD thì vẫn hiện), quyền chỉ bị đòi lúc Áp dụng. Customize mở từ tab AOD
  (`CustomizeDestination(isFirstOpen = false)`): không bước / Bỏ qua, Áp dụng xong quay về Main. Áp dụng ở đây và ở
  màn chủ đề mà còn thiếu quyền bắt buộc thì chưa ghi gì, hiện sheet quyền ngay tại màn; cấp đủ (quay lại màn) thì tự áp dụng.
- Trang cấp quyền ở app Cài đặt mở bằng `context.openXxxSettings(isGranted)`
  (`utils/ContextExt.kt`): `returnAppWhen` kéo app về khi quyền được cấp (poll 200 ms, tối đa 60
  giây; quyền đã có lúc mở hoặc user tự quay lại thì không kéo). Trang mở bằng `startActivityForResult`
  để đóng bằng `finishActivity` (Xiaomi chặn `startActivity` từ nền khi chưa có quyền cửa sổ bật lên),
  `startActivity` chỉ là đường dự phòng; mở trang cấp quyền mới thì đi qua `openSettingsPage`. `isGranted` lấy từ
  `PermissionManager.isGranted(permission)`.
- Icon app và Lottie `lottie_device_edge_light` sinh bằng script ở `tools/brand/`, không sửa tay
  file sinh ra (đọc `tools/brand/README.md`). XML layout chỉ cho native ad.

## Ads

Placement: `interSplash` (Splash) và banner đáy màn `bannerLanguage` (chỉ onboarding), `bannerOnboardingCustomize`,
`bannerPermission`, `bannerMain` (3 tab chung), `bannerThemeEditor` (xem + sửa), `bannerCustomize`; id đang là test id Google.
Banner: Screen truyền slot `bannerAd` xuống Content; chân onboarding đặt qua `AppOnboardingFooter(ad = …)`, màn có thanh dưới
qua `AppBottomAdSlot` đặt dưới thanh (slot tự chừa thanh điều hướng nên thanh bên trên không chừa nữa; ẩn tạm thì
`isHidden`, không bỏ khỏi composition kẻo banner tải lại).

- `AdsManager` (Koin `single`) giữ consent UMP, init MobileAds, `isAdShowing` và các placement
  `by lazy` (không tạo `AdUnit` trong composable). Id mỗi placement là `buildConfigField` ở cả
  `release {}` (id thật) lẫn `debug {}` (test id Google). `AdUnit` load / show trên main thread, log
  qua `log("Action")` (không `Timber` trực tiếp). Composable ad: 1 `DisposableEffect(adUnit())` load
  / release; ad full-screen load / show ở Screen, banner / native truyền slot xuống Content.
- **Không bao giờ load / hiện ads hay consent trên `AodActivity`** (nó nằm trên màn hình khoá). Thêm
  app-open ad thì phải bỏ qua `AodActivity`.

## Thư viện, log, test

- Version ở `gradle/libs.versions.toml` (alias kebab-case, dùng `libs.xxx`), không inline; lib
  Compose không ghi version (theo BOM). Lib mới phải có lý do và hỏi trước, ưu tiên lib chính chủ,
  không thêm lib trùng chức năng. MockK / Turbine chưa có.
- R8: không thêm keep rule rộng cho lib đã có consumer rules; đổi rule thì test release. Secret / id
  theo môi trường → `local.properties` (gitignore) → `signingConfigs` / `buildConfigField`.
- Class cần log implement `Tag` → `Timber.tag(tag).e(throwable.stackTraceToString())`; composable
  đọc `LocalTag.current`. Không `Log.*` / `println`.
- Không comment mô tả "làm gì"; UI không comment. Chỉ giữ 1 dòng **tại sao** cho invariant không
  hiển nhiên và `// TODO:`.
- Test: fake tại biên interface Repository, không mock UseCase / class final. Ưu tiên UseCase,
  ViewModel (Intent → State / Effect), mapper.

## Skills và agent

- CLAUDE.md là chuẩn: skill / agent trong `.claude/` khác thì theo CLAUDE.md. Rule mới của user ghi
  vào CLAUDE.md, kèm một check trong `tools/check-rules.sh` nếu grep được; không ghi vào skill.
  Giữ CLAUDE.md ngắn (khoảng 200 dòng), không tách `.claude/rules/`: chỉ ghi thứ code và README
  không nói.
- **Mặc định tự làm, không tạo agent** (agent phải đọc lại toàn bộ ngữ cảnh, tốn token). Skill có
  bước giao cho `compose-implementer` (`design-tokens`, `compose-optimizer`) thì tự làm theo file
  agent đó. Chỉ dùng agent khi user bảo; agent đang chạy cùng việc thì SendMessage tiếp.
- `design-tokens` chỉ ghi giá trị màu / chữ / bo góc vào `Colors` / `Typography` / `Shapes` (không
  dùng kiến trúc token 3 tầng của skill). Kết quả trung gian của skill (brief, PRD, design spec,
  danh sách màn) nằm trong `plans/`.
