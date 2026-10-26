# Commit và đưa lên main

Chỉ làm khi user bảo. Lệnh git bị chặn vì cần user cho phép thì hỏi user, không đổi sang lệnh khác để vòng qua.

## Commit

1. `git status --short`: liệt kê đúng file của chủ đề (thư mục chủ đề, các chỗ ở registration.md, font / license / mock).
2. `git add <từng file>` (không `-A`, không `.`); không stage `local.properties`, `.idea/*`, `app/build.gradle.kts` hay file
   không phải của chủ đề.
3. Message: một đoạn chữ thường tiếng Anh như các commit chủ đề trước (`git log --grep "add the" -3`): chủ đề nào, số đo lấy từ
   đâu, các phần vẽ, tuỳ chọn, mặc định, font. Không dòng `Co-Authored-By`.

Làm thẳng trên checkout chính thì commit xong là xong. Không push.

## Đang ở worktree: đưa nhánh lên main

Checkout chính: `M="C:/Android Development/aodamn/aodamn"`.

1. `git -C "$M" status --short`: thay đổi chưa commit ở đó không phải của chủ đề, để nguyên. Nếu nó đụng file mình vừa commit
   thì dừng, báo user.
2. Main chưa có commit mới (nhánh cắt từ đúng `main` hiện tại): `git -C "$M" merge --ff-only <nhánh>`.
3. Main đã có commit mới: trong worktree `git merge main` (không rebase), giải conflict nếu có, chạy lại
   `scripts/check_theme.sh`, rồi `git -C "$M" merge --ff-only <nhánh>`.
4. `git -C "$M" log --oneline -3` để xác nhận. Không push.

## Báo user

Hash commit, main đang ở đâu, chưa push, chưa test trên máy (nếu chưa).
