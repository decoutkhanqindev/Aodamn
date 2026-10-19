# Timber — xoá log v/d/i ở release
-assumenosideeffects class timber.log.Timber {
    public static void v(...);
    public static void d(...);
    public static void i(...);
}

# WorkManager 2.7.0 (kéo theo từ Google Mobile Ads) dùng Room tạo WorkDatabase_Impl bằng reflection qua constructor rỗng; rule đi kèm của nó
# không giữ constructor khi R8 full mode nên release crash lúc khởi động ("Failed to create an instance of androidx.work.impl.WorkDatabase").
-keep class * extends androidx.room.RoomDatabase {
    <init>();
}
