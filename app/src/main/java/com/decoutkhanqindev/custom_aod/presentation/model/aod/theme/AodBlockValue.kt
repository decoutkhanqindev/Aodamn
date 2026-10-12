package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import androidx.compose.runtime.Immutable

// Khối trên AOD mà một chủ đề có thể tự vẽ thay bản mặc định (AodThemeSpec.ownBlocks). Xem trước chủ đề (lưới chủ đề, màn chủ đề) luôn
// hiện các khối này để thấy kiểu riêng của chủ đề; thêm khối tự vẽ mới = thêm mục ở đây, nhánh vẽ theo chủ đề ở AodDetails / khối đó, và
// dữ liệu mẫu cho nó trong toThemePreviewDraft.
@Immutable
enum class AodBlockValue {
    DATE,
    MEDIA,
}
