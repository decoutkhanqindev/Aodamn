# Đọc app mẫu và lập bảng tuỳ chọn

Thư mục làm việc (ngoài repo): `W=~/.local/share/rea/work/com.sparkine.muvizedge`. Python đọc file dịch ngược luôn chạy
`python -I -X utf8`. Không chép code dịch ngược hay asset của app mẫu vào repo.

## File cần đọc

| Thứ | Ở đâu |
|---|---|
| Màn chủ đề: tên, mặc định, danh sách tuỳ chọn | `$W/src/sources/com/sparkine/muvizedge/fragment/aodscreen/<Mon><YY>Screen.java` (hàm `W()` / `V()` / `Z()`) |
| Bảng mã → nhãn tuỳ chọn | `$W/src/sources/com/sparkine/muvizedge/ib/a.java`, `catalog.json` / `catalog.md` trong `$W` |
| Code vẽ đồng hồ | `$W/src/sources/com/sparkine/muvizedge/view/<X>.java` (đọc `onDraw`, chỗ nhân cỡ, kiểu, 24 giờ, chuyển động) |
| Layout dọc / ngang | `aapt2 dump xmltree` (lệnh dưới), có `res/layout-land/` thì đọc cả bản ngang |
| Id font / màu / dimen trong layout | `$W/res-base.txt` |
| Bảng màu gradient có sẵn | `ab/a0.java` (đã thành `GradientSwatchValue`) |

```bash
APK="$HOME/.local/share/rea/work/com.sparkine.muvizedge/apk/com.sparkine.muvizedge/base.apk"
AAPT="$LOCALAPPDATA/Android/Sdk/build-tools/36.0.0/aapt2.exe"
"$AAPT" dump xmltree --file res/layout/<mon><yy>_screen_layout.xml "$APK"
"$AAPT" dump xmltree --file res/layout-land/<mon><yy>_screen_layout.xml "$APK"   # lỗi = không có bản ngang
```

Lệnh `rea` hoặc tool MCP `mcp__rea__*` dùng khi cần bằng chứng cho một method (Evidence ID ghi vào plans).

## Số đo

- Quy mọi số đo về một đơn vị gốc (vd `u` = nửa cạnh ngắn màn, hoặc chiều cao hàng số) và ghi tỉ lệ, không ghi dp cố định:
  sau thành hằng trong `object <Chủ đề>Metrics` kèm comment nguồn ("oct23_screen_layout", "CoumTick.onDraw").
- Font: tìm font của app mẫu trong APK, đo chữ số bằng PIL (cao / em, rộng / cao, độ dày nét). Chỉ đo, không lấy file. Lấy
  bản Google Fonts cùng họ theo CLAUDE.md › Font; font mẫu đậm hơn bản đậm nhất của Google Fonts thì lấy độ đậm cao nhất
  (Chồng số: wght 1000) và ghi là đổi trong bảng.
- Không xin user chụp ảnh app mẫu.

## Bảng tuỳ chọn (đưa vào mock)

Lập trước khi vẽ mock, mỗi dòng một thứ của app mẫu, đủ cả bốn nhóm:

1. **Tuỳ chọn** trong `Z()` / `V()` và cột Options / Advanced của danh mục (Clock Style, Clock Size, Color 1…).
2. **Chi tiết hiển thị**: từng phần vẽ (vạch, chấm giây, vùng mờ, khe đen giữa số…), định dạng ngày, in hoa.
3. **Chuyển động**: thứ chạy theo giây thật (vòng giây, chấm giây), animation khi đổi phút / đổi kiểu, chạy mỗi khung hình.
4. **Trạng thái của app**: Muviz có cài đặt chung "Low power after" (mặc định 15 giây) đổi chủ đề sang dạng tiết kiệm (thu
   gọn, ẩn số giây). Ghi chủ đề có dạng tiết kiệm không và đề xuất giữ (tuỳ chọn, mặc định tắt) hay bỏ. Bán nguyệt trước đây
   gắn dạng nghỉ vào lúc AOD dịu trong phòng tối và user tưởng mất vòng giây; Gợn sóng, Nhịp giây, Chồng số user duyệt bỏ.

Mỗi dòng ghi: **giữ** / **đổi** (đổi thành gì, vì sao) / **bỏ** (vì sao). Đổi hay bỏ phải có user OK trong lời duyệt mock.
Chỗ làm xịn hơn app mẫu (màu riêng từng phần, chữ mờ khi dừng nhạc, gradient theo hướng bất kỳ, vùng mờ chỉ khoét để ảnh nền
vẫn thấy) ghi thành dòng **nâng cấp**.

Ghi lại phần đã kiểm vào `plans/muviz-edge-themes.md` › Đề xuất làm tiếp (gạch đầu dòng của chủ đề).
