package com.ming.util;

import java.io.*;

/**
 * 保存文件工具类
 * Created By Ranger on 2022/6/27.
 */
public class FileUtil {

    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

    /**
     * 保存文件对象
     * @param input
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     */
    public String writeFile(InputStream input, String path, String fileName) throws IOException {
        fileName = System.currentTimeMillis()+fileName.substring(fileName.indexOf("."));
        String filePath = verifyFolderExist(null, SYS_TEM_DIR, fileName);
        if (filePath == null) return filePath;
        writeFile(input, new File(filePath));
        return SYS_TEM_DIR + File.separator + fileName;
    }
    /**
     * 校验文件夹path文件夹是否存在
     * @param file
     * @param path
     * @param fileName
     * @return
     */
    public String verifyFolderExist(File file, String path, String fileName) {
        String folder_path = path;
        String filePath = folder_path + File.separator + fileName;
        if (file != null && file.getAbsolutePath().equals(filePath)) {
            return null;
        }
        File folder = new File(folder_path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return filePath;
    }
    /**
     * 写入文件
     * @param is
     * @param target
     * @throws IOException
     */
    public void writeFile(InputStream is, File target) throws IOException {
        OutputStream os = new FileOutputStream(target);
        byte[] bytes = new byte[1024 * 1024];
        int len = 0;
        while ((len = is.read(bytes, 0, bytes.length)) > 0) {
            os.write(bytes, 0, len);
        }
        is.close();
        os.close();
    }
}
