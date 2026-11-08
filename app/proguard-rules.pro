# WorkManager 2.7.0 (kéo theo từ Google Mobile Ads) dùng Room tạo WorkDatabase_Impl bằng reflection qua constructor rỗng; rule đi kèm của nó
# không giữ constructor khi R8 full mode nên release crash lúc khởi động ("Failed to create an instance of androidx.work.impl.WorkDatabase").
-keep class * extends androidx.room.RoomDatabase {
    <init>();
}
