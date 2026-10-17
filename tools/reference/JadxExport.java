import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;

import java.io.File;

// Xuất toàn bộ mã nguồn của một APK (không kèm resource) bằng engine JADX nằm trong jar jadx-headless-mcp của REA, vì CLI
// của REA chỉ dịch từng method và mỗi lệnh lại nạp lại APK. Thường gọi qua pull_decompile.py; chạy tay:
//   java -Xmx6g -cp <jadx-headless-mcp-0.7.1-all.jar> tools/reference/JadxExport.java <apk> <thư mục ra>
public class JadxExport {
    public static void main(String[] a) throws Exception {
        if (a.length != 2) {
            System.err.println("Cách dùng: JadxExport.java <apk> <thư mục ra>");
            System.exit(2);
        }
        JadxArgs args = new JadxArgs();
        args.setInputFile(new File(a[0]));
        args.setOutDir(new File(a[1]));
        args.setSkipResources(true);
        args.setThreadsCount(Math.max(2, Runtime.getRuntime().availableProcessors() - 2));
        try (JadxDecompiler jadx = new JadxDecompiler(args)) {
            jadx.load();
            System.out.println("classes: " + jadx.getClasses().size());
            jadx.save();
        }
    }
}
