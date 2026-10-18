package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable

// Khối trên AOD mà một chủ đề có thể tự vẽ thay bản mặc định (AodThemeSpec.ownBlocks). Xem trước chủ đề (lưới chủ đề, màn chủ đề) luôn
// hiện các khối này để thấy kiểu riêng của chủ đề; thêm khối tự vẽ mới = thêm mục ở đây, nhánh vẽ theo chủ đề ở AodDetails / khối đó, và
// dữ liệu mẫu cho nó trong toThemePreviewDraft.
@Immutable
enum class AodBlockValue {
    DATE,
    MEDIA,

    // Dòng trạng thái: pin, đèn pin và icon thông báo chung một dòng (thay khối Pin và Thông báo dùng chung)
    STATUS,

    // Ghi nhớ, sự kiện do chủ đề vẽ theo kiểu chữ của nó (Chuỗi); chủ đề không khai báo thì vẫn là khối dùng chung như cũ.
    MEMO,
    EVENTS,
}
