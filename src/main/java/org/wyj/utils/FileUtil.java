package org.wyj.utils;

import java.io.File;
import java.io.FileWriter;

public class FileUtil {
    public static void writeToFile(String content, String path, String filename) throws Exception {
        File file = new File(path, filename);
        if (!file.getParentFile().exists()) {
            boolean isMkSuccess = file.mkdirs();
            if (!isMkSuccess) {
                throw new Exception("创建文件夹失败：" + file.getName());
            }
        }
        if (file.exists()) {
            boolean deleteFile = file.delete();
            if (!deleteFile) {
                throw new Exception("删除文件失败：" + file.getName());
            }
        }
        boolean newFile = file.createNewFile();
        if (!newFile) {
            throw new Exception("创建文件失败：" + file.getName());
        }
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }
}
