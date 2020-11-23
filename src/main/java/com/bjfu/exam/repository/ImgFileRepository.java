package com.bjfu.exam.repository;

import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.exception.OSSException;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.DeleteError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class ImgFileRepository {

    @Autowired
    MinioClient minioClient;

    @Value("${minIO.imgBucket}")
    private String imgBucket;

    public void uploadFile(String fileName, InputStream inputStream) {
        try {
            minioClient.putObject(imgBucket, fileName, inputStream, "application/octet-stream");
        } catch(Exception e) {
            log.error("oss upload file failed!" + e.getMessage());
            throw new OSSException(ResultEnum.OSS_UPLOAD_FILE_FAILED);
        }
    }

    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(imgBucket, fileName);
        } catch (Exception e) {
            log.error("oss delete file failed!");
        }
    }

    public void deleteFiles(List<String> fileNames) {
        Iterable<Result<DeleteError>> results = minioClient.removeObject(imgBucket, fileNames);
        results.forEach((errorResult) -> {
            try {
                DeleteError deleteError = errorResult.get();
            } catch (Exception e) {
                log.error("oss delete file failed!");
            }
        });
    }


}
