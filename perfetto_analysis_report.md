# Phân tích pin khi AOD đang hiện

## Mục tiêu

Tìm phần CPU/GPU còn chạy khi `AodActivity` đang hiện trên màn hình khóa, rồi giảm tải mà vẫn giữ đủ theme, chuyển động, cảm biến, thông báo và điều khiển nhạc.

## Môi trường

- Thiết bị: AVD `medium_phone`, `emulator-5558`
- Android: API 36, build `BE2A.250530.026.D1`
- App: `com.decoutkhanqindev.aodamn`
- Theme tĩnh: Cổ điển
- Theme động A/B: Bán nguyệt
- Độ sáng cửa sổ AOD: 1%
- Trace: Perfetto system trace, CPU scheduling, frame timeline, GPU frame events

## Trace

- Cổ điển: `C:\Android Development\aodamn\aodamn\build\aod-active.perfetto-trace`
- Bán nguyệt trước sửa: `C:\Android Development\aodamn\aodamn\build\aod-halfmoon.perfetto-trace`
- Bán nguyệt sau sửa: `C:\Android Development\aodamn\aodamn\build\aod-halfmoon-optimized.perfetto-trace`

Hai trace Bán nguyệt dài khoảng 30 giây và không có lỗi data-loss. Trace Cổ điển có một sự kiện `sorter_push_event_out_of_order`, nên số liệu Cổ điển chỉ dùng làm mốc phụ.

## Kết quả

| Chỉ số trong 30 giây | Trước | Sau | Giảm |
|---|---:|---:|---:|
| Khung hình | 1.775 | 297 | 83,3% |
| Tốc độ khung | 59,2 fps | 9,9 fps | 83,3% |
| Tổng CPU app | 13,834 giây | 1,493 giây | 89,2% |
| Một lõi CPU | 46,14% | 4,96% | 89,2% |
| CPU main thread | 3.850 ms | 651 ms | 83,1% |
| CPU RenderThread | 9.117 ms | 636 ms | 93,0% |

Theme Bán nguyệt trước sửa gọi `withFrameMillis` liên tục. Nó tạo khoảng 60 lần cập nhật mỗi giây. Mỗi lần làm Compose vẽ lại toàn màn hình 1080 × 2400.

Sau sửa, chuyển động vẫn chạy 10 lần mỗi giây. Kim giây, vòng số và hành tinh vẫn chuyển động liên tục. Không tùy chọn hay khối AOD nào bị bỏ.

Theme Cổ điển tĩnh chỉ tạo một GPU frame trong 89,75 giây. App dùng 0,439 giây CPU, tương đương 0,49% một lõi. Vì vậy foreground service và timer theo phút không phải nguồn hao pin chính trong trường hợp tĩnh.

## Phạm vi bản sửa

`AodLiveClock` nay cập nhật theo nhịp 100 ms. Thay đổi này áp dụng cho Bán nguyệt, Gợn sóng, Nhịp giây, Bốn số, Rẻ quạt, Quỹ đạo và các phần dùng helper chung.

La bàn có ticker riêng. Kiểu có vòng giây cũng được giới hạn ở 100 ms. Kiểu không có vòng giây vẫn chỉ cập nhật mỗi phút.

Hoạt ảnh ngắn vẫn giữ nguyên tốc độ: viền sáng 4 giây, chuyển theme, đổi thông báo, đổi bài và marquee một lượt.

## Các nguồn pin còn lại

### Màn hình

`FLAG_KEEP_SCREEN_ON` giữ panel sáng trong suốt lúc AOD hiện. Đây là chi phí lớn nhất còn lại và là cơ chế chính của app. Xóa cờ này sẽ làm mất AOD.

Theme có nhiều điểm ảnh sáng, gradient, ảnh nền hoặc ảnh người dùng sẽ dùng pin OLED nhiều hơn theme ít chữ. Độ sáng 1% và nền đen đã là mặc định tốt.

### MainActivity và quảng cáo

Khi AOD hiện, `MainActivity` vẫn nằm trong task nền. Lần đo thấy hai Activity, bốn đến sáu WebView và khoảng 159 MB PSS. Banner đã gọi `AdView.pause()` khi Activity dừng. CPU của các thread quảng cáo trong trace Bán nguyệt thấp hơn 1 ms mỗi thread, nên chưa có bằng chứng đủ mạnh để đổi vòng đời quảng cáo.

Hủy banner khi app vào nền có thể giảm RAM. Nó cũng buộc tải quảng cáo lại sau mở khóa, tăng mạng và đổi hành vi quảng cáo. Bản sửa này không làm vậy.

### Cảm biến

Tiệm cận chỉ được đăng ký khi tùy chọn trong túi bật. Cảm biến sáng chỉ được đăng ký khi tự giảm sáng bật. Cả hai được hủy khi `AodViewModel` đóng.

AVD không có cảm biến sáng và cảm biến tiệm cận thật. Trace này không đo được mức pin của hai cảm biến. Cần trace máy thật để chốt.

### Thời tiết, lịch, pin, nhạc và thông báo

- Thời tiết chỉ gọi mạng khi dữ liệu cũ từ 30 phút.
- Lịch đọc lại mỗi 15 phút nếu đã bật.
- Pin dùng broadcast thay vì polling.
- Nhạc và thông báo dùng callback.
- Thanh tiến trình nhạc cập nhật mỗi giây.

Các phần này không tạo khung hình liên tục trong trace hiện tại.

## Truy vấn đã chạy

```sql
SELECT
  COUNT(*) AS sched_slices,
  SUM(s.dur) / 1e9 AS cpu_seconds,
  100.0 * SUM(s.dur) / (trace_end() - trace_start()) AS one_core_percent
FROM sched_slice AS s
JOIN thread AS t ON t.utid = s.utid
JOIN process AS p ON p.upid = t.upid
WHERE p.name = 'com.decoutkhanqindev.aodamn';
```

```sql
SELECT
  COALESCE(t.name, printf('tid-%d', t.tid)) AS thread_name,
  t.utid,
  COUNT(*) AS runs,
  ROUND(SUM(s.dur) / 1e6, 3) AS cpu_ms,
  ROUND(AVG(s.dur) / 1e3, 3) AS avg_run_us,
  ROUND(MAX(s.dur) / 1e6, 3) AS max_run_ms
FROM sched_slice AS s
JOIN thread AS t ON t.utid = s.utid
JOIN process AS p ON p.upid = t.upid
WHERE p.name = 'com.decoutkhanqindev.aodamn'
GROUP BY t.utid
ORDER BY SUM(s.dur) DESC
LIMIT 15;
```

```sql
INCLUDE PERFETTO MODULE slices.with_context;
SELECT
  COUNT(*) AS app_frames,
  ROUND(SUM(IIF(s.dur = -1, trace_end() - s.ts, s.dur)) / 1e6, 3) AS total_frame_ms
FROM thread_or_process_slice AS s
WHERE s.process_name = 'com.decoutkhanqindev.aodamn'
  AND s.name GLOB 'Choreographer#doFrame*';
```

## Chưa thể kết luận từ AVD

- Mức pin theo giờ trên OLED thật
- Công suất panel theo số điểm ảnh sáng
- Mức pin của cảm biến sáng và tiệm cận
- Khả năng hạ tần số panel LTPO
- Hành vi HyperOS khi khóa qua đêm

Bước tiếp theo đúng là chạy cùng trace trên máy OLED thật. Không nên giảm thêm xuống dưới 10 fps trước khi xem chuyển động trên máy thật.