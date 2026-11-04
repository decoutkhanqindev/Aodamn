# Chọn bố cục dọc + ngang

Đọc trước khi vẽ mock. Mỗi hướng chỉ có **một** bố cục cố định cho cả chủ đề; các khung D1–D4 / N1–N4 chỉ đổi dữ liệu, màu, tuỳ chọn,
hiện / ẩn và trạng thái chuyển động.

## 1. Xếp chủ đề vào một loại (ghi loại và lý do vào mock)

| Loại | Dấu hiệu | Đồng hồ | Block còn lại |
|---|---|---|---|
| A. Đồng hồ là điểm nhấn duy nhất | chữ số xếp chồng hoặc hàng số, mặt số, kim, vòng, không có hình lớn riêng ngoài đồng hồ (Dịu, Phai, Khung, Quỹ đạo, La bàn, Vành, Bốn số, Chồng số, Rẻ quạt, Nhịp giây, Kẹp giấy) | **giữa màn** ở cả dọc lẫn ngang | dọc: nhóm trên + nhóm dưới đồng hồ; ngang: hai bên đồng hồ (vd ngày + thông báo một bên, nhạc + pin bên kia) |
| B. Hình lớn riêng + đồng hồ | trăng, mắt, nhật thực, vầng sáng: hình là điểm nhấn, đồng hồ đi theo hình (Pha trăng, Chớp mắt, Nhật thực) | dọc: khối hình + đồng hồ ở giữa như app mẫu; ngang: khối này giữa hoặc dồn về một phía, tuỳ phía nào cho cụm block cân | block vào **một cụm** ở phía đối trọng, cùng đường căn, cỡ chữ ngày / thời tiết chỉnh theo cụm (không dùng cỡ dọc) |
| C. Cột chữ lệch mép theo app mẫu | cột chữ canh trái hoặc đồng hồ dọc sát mép (Lời, Gáy sách, Đơn cách ở màn ngang) | theo cột của app mẫu | block vào cột đối trọng |

Chưa rõ loại thì đọc layout dọc / ngang trong apk: app mẫu đặt đồng hồ ở đâu, có hình lớn riêng không. Không chọn theo cảm giác.

## 2. Quy tắc (mỗi dòng là một lời sửa của user, kèm phạm vi)

- Loại A mà dồn đồng hồ về một phía kèm mọi block ở phía kia là sai. Dịu: "tại sao bạn không đặt clock ở giữa còn các block nằm 2 bên
  hoặc trên dưới". Muốn lệch thì chủ đề phải thuộc loại B / C và ghi lý do lấy từ app mẫu.
- Loại B / C mà ép đồng hồ ra giữa làm các block hai bên lệch hoàn toàn, hoặc ngày quá to, là sai. Chớp mắt, Pha trăng: "không phải
  nhất thiết lúc nào cũng đặt clock ở center". Gom block thành một cụm cân với khối chính.
- Hai lời sửa trên cùng đúng vì khác loại. Không gộp thành "luôn giữa" hay "không mặc định giữa".
- Không rải ngày, pin, thông báo, nhạc ra bốn cạnh chỉ để lấp chỗ: mỗi hướng tối đa 2–3 cụm đọc được.
- Không đổi phía, hàng, thứ tự của block giữa các khung cùng hướng. Mock không có tay cầm, nút hay chú thích gợi ý kéo / đổi vị trí
  block: user "kh yêu cầu feat chỉnh vị trí components".
- Khung dùng chung ([reuse.md](reuse.md) › Khung bố cục): bố cục khớp thì dùng `AodHeroInfoPortrait` / `AodHeroInfoLandscape`
  (hero giữa, info trái, trình phát + pin phải), `AodPortraitSpread` (dọc), `AodLandscapeLayout` + `AodLandscapePlan` (ngang). Không
  dùng thì ghi một dòng lý do trong mock; Dịu lần đầu bỏ `AodHeroInfoLayout` mà không nói.
- Lời sửa của user cho một chủ đề chỉ áp cho loại của chủ đề đó. Ghi vào [pitfalls.md](pitfalls.md) kèm tên chủ đề và loại, không
  viết thành luật "luôn" / "không bao giờ" cho mọi chủ đề.

## 3. Ghi vào mock trước khi render

Bảng anchor ở [mock.md](mock.md) có thêm cột **Loại** (A / B / C) và một câu lý do vị trí đồng hồ, dẫn từ chỗ đọc app mẫu.
