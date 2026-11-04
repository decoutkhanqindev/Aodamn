# Mock dọc + ngang

## Dựng

1. Thêm một mục vào `tools/mockups/aod_landscape_mockups.py`, theo mẫu mục của chủ đề vừa làm (`ripple`, `tick`,
   `big_overlap`): hàm vẽ có tiền tố riêng (vd `tk_`), danh sách `frames` gồm khung dọc (`frame_portrait`, 393 × 873) và khung
   ngang (873 × 393), rồi một dòng `("w_<chủ đề>", "<tóm tắt>", <defs>, <frames>)` trong vòng lặp cuối file. Chữ số lấy đường viền
   thật từ font bằng fontTools.
2. Khung cần có (ít nhất 4 dọc + 4 ngang):
   - D1 mặc định: đúng cái thẻ ở lưới chủ đề sẽ hiện (mặc định nổi nhất), chỉ đồng hồ + khối mặc định;
   - các khung có nhạc, thông báo mới nhất, đang sạc, thời tiết, kiểu khác của chủ đề (viền / đặc…), tắt ngày;
   - một khung "app mẫu" vẽ lại bố cục gốc để user so;
   - ngang: cùng các trạng thái, bố cục tự đánh giá cho cân (CLAUDE.md, README › Bố cục ngang).
   - vị trí đồng hồ và block: xếp chủ đề vào loại A / B / C theo [layout.md](layout.md) (loại A đồng hồ giữa màn; loại B / C theo app mẫu), ghi loại và lý do. Không có luật chung "luôn giữa" hay "không mặc định giữa".
   - chú thích mỗi khung: điểm nhấn, vị trí đồng hồ, block cân thị giác. Khung cùng hướng phải giữ cùng anchor và hierarchy; các case chỉ đổi dữ liệu, option nguồn, trạng thái hoặc block bật. Chỉ được có layout khác khi app mẫu có kiểu bố cục là một option riêng, và phải nêu rõ.
   - ngang phải chia thành cụm đọc được: hero + clock, details, rồi action/media khi có. Gom block cùng mục vào một cột hoặc vùng chung, cùng đường căn; không dàn ngày, pin, thông báo và nhạc thành các mảnh rời ở bốn cạnh chỉ để lấp chỗ.

## Mẫu anchor bắt buộc

Trước khi render, ghi một bảng cho từng orientation. Bảng có `hero`, `clock`, `date/weather`, `status`, `latest`, `media` và nêu anchor, vùng hoặc cột. Block không hợp chủ đề ghi `bỏ` cùng lý do.

| Loại | Dọc | Ngang |
|---|---|---|
| A | đồng hồ giữa; nhóm trên + nhóm dưới, các slot cố định từ trên xuống | đồng hồ giữa; hai nhóm hai bên, nhạc ở nhóm có pin |
| B | khối hình + đồng hồ giữa như app mẫu; nhóm dưới | khối chính một phía cố định; một cụm đối trọng, nhạc trong cụm |
| C | theo cột của app mẫu | theo cột của app mẫu; block vào cột đối trọng |

Bảng của chủ đề ghi loại, rồi ghi `hero`, `clock`, `date/weather`, `status`, `latest`, `media` ở đâu cho từng hướng.

Mọi D1–D4 phải dùng cùng bảng dọc. Mọi N1–N4 phải dùng cùng bảng ngang. Chỉ dữ liệu, màu, option, visibility và trạng thái chuyển động được đổi. Không đổi anchor, hàng, cột, thứ tự đọc hoặc phía của block. Khi hero chuyển động, tâm hình học của hero và clock vẫn giữ nguyên.

Trước khi gửi mock, so bảng này với hai chủ đề cùng loại ở [layout.md](layout.md). Nếu một block đổi vị trí giữa các frame cùng orientation, sửa toàn bộ mock trước khi render lại.

3. Chuyển động theo giây thật thì thêm script nhỏ cho khung mock chạy (mẫu `tk_live_script()` của Nhịp giây).

## Render và tự kiểm (trước khi cho user xem)

```bash
S="<scratchpad>/mock"
python tools/mockups/aod_landscape_mockups.py "$S" w_<chủ đề>          # không -I nếu cần gói --user (skia-pathops)
python -I -c "import pathlib,sys; d=pathlib.Path(sys.argv[1]); s=(d/'w_<chủ đề>.html').read_text(encoding='utf-8'); (d/'page.html').write_text('<!doctype html><meta charset=utf-8><style>body{background:#1e1e1e;margin:0;padding:16px;width:900px;--text-secondary:#aaa}.sr-only{display:none}</style>'+s, encoding='utf-8')" "$S"
EDGE="/c/Program Files (x86)/Microsoft/Edge/Application/msedge.exe"
"$EDGE" --headless=new --disable-gpu --hide-scrollbars --window-size=932,5200 --virtual-time-budget=4000 --screenshot="$S/shot.png" "file:///$S/page.html"
```

Đọc `shot.png` bằng Read, nhìn kỹ: chữ số có nét lạ (glyph tự chồng, xem pitfalls), khối lệch, chữ tràn, màn ngang trống một bên,
block thành các mảnh rời, đồng hồ lệch dù chủ đề loại A hoặc ép giữa dù loại B / C làm block lệch ([layout.md](layout.md)).
Sửa tới khi ổn rồi mới hiện.

## Font trong mock

Chữ AOD trong mock dùng font của chủ đề, không dùng font mặc định của HTML: chữ số lớn dựng bằng `<path>` từ glyph (fontTools, mẫu
`BzFont`), chữ nhỏ dùng `<text>` với `<link>` Google Fonts / jsdelivr (widget cho nạp). Mock gọn như các chủ đề cũ, nhờ `<use>` và
`<text>`: không dựng mọi chữ nhỏ bằng path. Tự kiểm: nét chữ trong ảnh khớp font thật (chữ số, dấu tiếng Việt).

## Hiện cho user

- `show_widget` một lần với nội dung `w_<chủ đề>.html`, mọi khung dọc + ngang trong cùng một widget, có chuyển động thì chạy được ngay
  trong widget (không gửi file HTML, không gửi ảnh PNG, không tách nhiều lần; user xem trong chat).
- Kèm text, theo thứ tự:
  1. Tên tiếng Việt + tiếng Anh đề xuất (en ngắn, vd "Tick", "Overlap").
  2. Màu nhấn mặc định: một màu chưa chủ đề nào dùng. Xem màu đã dùng ở `theme/Colors.kt` và `default =` của
     mọi `*ThemeSpec.kt`; nêu màu đã chọn và vì sao (màu thẻ ở lưới của app mẫu nếu chưa bị dùng).
  3. Bảng tuỳ chọn giữ / đổi / bỏ / nâng cấp (từ read-reference.md).
  4. Khối dùng chung: từng khối (ngày, pin, thông báo + thông báo mới nhất, thời tiết, sự kiện, ghi nhớ, nét vẽ, nhạc) là có hay
     bỏ, mỗi khối bỏ một dòng lý do; khối có thì đặt ở đâu ở cả dọc lẫn ngang.
  5. Font định dùng (tải Google Fonts nào, sinh độ đậm nào).
  6. Code dùng chung ([reuse.md](reuse.md)): khối dùng lại (trình phát, thông báo mới nhất, khung bố cục); khối cần mở rộng, mỗi thuộc tính mới một
     dòng lý do và mặc định giữ hành vi cũ; phần viết riêng, mỗi phần một dòng lý do vì sao khối có sẵn không khớp.
- Kết thúc lượt bằng câu hỏi duyệt (wrap_up), không code.

## Khi user góp ý

- Sửa đúng ý, rồi render và hiện lại **mọi khung**, không chỉ khung vừa sửa.
- Góp ý ở dọc mà ảnh hưởng ngang (vd dời pin lên dưới ngày) thì tự sửa cả ngang.
- Lời duyệt của user (vd "Duyệt mock …, đồng ý cả 4 mục đổi / bỏ. Bắt đầu code.") là mốc: ghi nguyên văn vào CHANGELOG của chủ
  đề. Mục user không nhắc tới trong lời duyệt mà mock có nêu là đổi / bỏ thì hỏi lại, không tự coi là đồng ý.
