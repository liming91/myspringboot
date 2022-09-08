package com.ming.util;

import cn.hutool.core.io.FileUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;

/**
 * @description: miniio工具类
 * @author: 233
 * @create: 2021-08-06 09:16
 **/
@Slf4j
@Component
public class MinIoUtil {

    private static String url;

    private static String accessKey;

    private static String secretKey;

    private static String bucketName;

    @Value("${minio.url}")
    public void setUrl(String url) {
        MinIoUtil.url = url;
    }

    @Value("${minio.accessKey}")
    public void setAccessKey(String accessKey) {
        MinIoUtil.accessKey = accessKey;
    }

    @Value("${minio.secretKey}")
    public void setSecretKey(String secretKey) {
        MinIoUtil.secretKey = secretKey;
    }

    @Value("${minio.bucketName}")
    public void setBucketName(String bucketName) {
        MinIoUtil.bucketName = bucketName;
    }

    public static String upload(String folderName, File file) {
        try (FileInputStream is = new FileInputStream(file)) {
//            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            MinioClient minioClient = new MinioClient.Builder()
                    .endpoint(url)
                    .credentials(accessKey, secretKey)
                    .build();
            String mainName = FileUtil.mainName(file.getName());
            //后缀名
            String extName = FileUtil.extName(file.getName());
            // 获取文件名称
            String fileName = folderName + "/" + mainName + "." + extName;
            // 放入minio
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(is, is.available(), -1)
                    .contentType("application/octet-stream")
                    .build()
            );
//            minioClient.putObject(bucketName, fileName, is, "application/octet-stream");
            String pathUrl = "/" + bucketName + "/" + fileName;
            return pathUrl;
        } catch (Exception e) {
            log.info("minio上传异常: {}", e.getMessage());
            return null;
        }
    }
}
