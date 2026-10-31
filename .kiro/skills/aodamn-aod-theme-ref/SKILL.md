---
name: aodamn-aod-theme-ref
description: Làm một chủ đề AOD mới cho Aodamn theo app mẫu đã dịch ngược (Muviz Edge, danh mục plans/muviz-edge-themes.md), từ đọc code mẫu → mock dọc + ngang chờ user duyệt → code → kiểm → commit lên main. Dùng khi được bảo "làm theme / chủ đề <tên hoặc số> của Muviz", "clone theme từ app mẫu", hoặc "dùng skill aodamn-aod-theme-ref".
---

# Làm chủ đề AOD theo app mẫu

Luật code, luật chủ đề và các bước kỹ thuật đã có sẵn, đọc trước khi làm và **không lặp lại ở đây**:
- `.kiro/steering/aodamn.md` › Kiến trúc › Chủ đề AOD, Hard rules (git, xin phép).
- `README.md` › Thêm chủ đề mới (bước 1–7) và › Bố cục ngang.
- `tools/reference/README.md` (đọc app mẫu đã dịch ngược).

Skill này chỉ thêm **quy trình**: thứ tự bước, điểm dừng chờ user, lệnh cụ thể, và những lỗi đã mắc khi làm các chủ đề trước.
Mỗi lần dùng làm đúng một chủ đề.

## Các bước

Chép checklist này vào task list (`update_plan`) và đi đúng thứ tự. Hai bước có ⛔ là điểm dừng: kết thúc lượt, chờ user trả lời.

```
- [ ] 0. Chọn chủ đề, chuẩn bị
- [ ] 1. Đọc app mẫu, lập bảng tuỳ chọn
- [ ] 2. Mock dọc + ngang ⛔ chờ user duyệt
- [ ] 3. Code chủ đề
- [ ] 4. Đăng ký chủ đề ở mọi chỗ (code + docs)
- [ ] 5. Kiểm (script) + báo user cách test trên máy
- [ ] 6. Commit + đưa lên main ⛔ chỉ khi user bảo
```

### 0. Chọn chủ đề, chuẩn bị
- Chưa có chủ đề: đọc `plans/muviz-edge-themes.md` › Đề xuất làm tiếp, đề xuất 2–3 ứng viên, mỗi ứng viên mô tả từ code vẽ thật
  (`view/<X>.java` onDraw + layout; chỗ chưa đọc ghi là đoán, chủ đề dựa vào ảnh thì nêu là không làm lại được), chờ user chọn.
  Cột cuối của bảng danh mục đã có tên mình nghĩa là đã làm.
- Đang ở worktree: chép `local.properties` từ checkout chính vào gốc worktree (gitignore, không commit), thiếu thì gradle không
  build. `git log -1` để biết nhánh cắt từ commit nào của main.

### 1. Đọc app mẫu
Theo [references/read-reference.md](references/read-reference.md): đọc code vẽ + layout dọc / ngang + hàm tuỳ chọn của chủ
đề, đo font, rồi lập **bảng mọi tuỳ chọn, chi tiết, chuyển động** của app mẫu. Không mô tả hay đề xuất gì trước khi đọc code.
Xong bảng thì so trình phát, thông báo mới nhất, bố cục, font của `Paint`, giờ thật với khối dùng chung theo
[references/reuse.md](references/reuse.md) để ghi vào mock.

### 2. Mock ⛔
Theo [references/mock.md](references/mock.md): thêm mục chủ đề vào `tools/mockups/aod_landscape_mockups.py`, render, tự
chụp kiểm, rồi vẽ inline bằng `show_widget` kèm bảng giữ / đổi / bỏ. Kết thúc lượt bằng câu hỏi duyệt. Chưa có câu "duyệt" của
user thì không viết dòng code chủ đề nào. Sửa mock theo góp ý thì vẽ lại **cả bản**.

### 3. Code
Theo README › Thêm chủ đề mới bước 3–6. Trước khi vẽ, thêm đúng một `ThemeExpectation` vào bộ test dùng chung theo
[references/testing.md](references/testing.md); không chép harness hay test block sang file riêng của chủ đề. Media, Layout, thông báo mới nhất, font, giờ thật: dùng khối đã chọn ở
[references/reuse.md](references/reuse.md), chủ đề chỉ thêm style trong Metrics; không chép hàm làm tối brush, resolve
typeface hay vòng lặp giờ thật. Phần riêng mới lấy mẫu từ chủ đề vừa làm (`sector/`, `bigoverlap/`, `tick/`). Trước khi viết, đọc
[references/pitfalls.md](references/pitfalls.md): danh sách lỗi đã gặp, mỗi lỗi một dòng kiểm.

Code dùng chung (ngoài nhánh `when` của chủ đề) chỉ thêm, có mặc định giữ hành vi cũ, và **hỏi user trước** khi sửa.

### 4. Đăng ký ở mọi chỗ
Theo [references/registration.md](references/registration.md): danh sách đủ mọi chỗ trong code và docs phải có tên chủ đề
mới (README có hơn 10 chỗ liệt kê tên chủ đề, dễ sót).

### 5. Kiểm
Theo [references/testing.md](references/testing.md). Kiểm local trước:
```bash
bash .kiro/skills/aodamn-aod-theme-ref/scripts/check_theme.sh <gói chủ đề> "<Tên tiếng Việt>"
```
Lần đầu có AVD cố định: chạy Fast một lần khi code ổn, chạy `--device` để lấy ảnh thực tế, xem ảnh rồi ghi ảnh chuẩn bằng `--record`.
Script băm đầu vào theo nhóm `behavior`, `screens`, `options`, `editor`, `motion`, `blocks`; chỉ nhóm chưa pass hoặc có file đổi mới chạy.
Dùng `--force` khi cần chạy lại dù đầu vào không đổi. Dấu pass nằm trong `build/theme-tests/cache/<gói>/` và mất khi `clean`.
`--editor` chỉ xét panel editor. Block chung chỉ xét bằng `--blocks`. Fast không compile androidTest; lệnh test máy tự compile phần đó.
Lint và toàn bộ test nằm ở cổng phát hành, không nằm trong vòng làm một theme.
Mọi mục phải OK; lỗi rẻ thì dừng trước máy, lỗi ảnh thì đọc actual / reference / diff. Lệnh Gradle lâu thì chạy nền.

Báo user: kết quả local + device, AVD dùng để ghi ảnh, đường dẫn diff nếu fail, các file dùng chung đã sửa và phần quyền Xiaomi còn phải
kiểm tay. Test theme phải đi qua test host không tạo `AdsManager`; không sửa `canRequestAds` của app thật. Không bắt user lặp lại luồng onboarding để xem theme.

### 6. Commit + đưa lên main ⛔
Chỉ khi user bảo ("commit", "commit vào main"). Làm theo [references/land.md](references/land.md).
Sau khi chủ đề đã lên main, user báo lỗi thì sửa thẳng trên checkout chính (user đã chốt), không sửa ở worktree.

## Khi user sửa hướng

User chỉ ra hướng làm sai (vd "vẫn chung tab, chỉ tách phần chỉnh cỡ"): hoàn tác đúng phần đã làm sai trước khi làm lại,
không chồng bản sửa mới lên bản sai. Chưa rõ ý thì hỏi lại trước khi code.
