package com.bjfu.exam.config;

import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.exception.OSSExceptionExam;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinIOConfig {

    @Value("${minIO.url}")
    private String minIOUrl;
    @Value("${minIO.port}")
    private Integer minIOPort;
    @Value("${minIO.accessKey}")
    private String accessKey;
    @Value("${minIO.secretKey}")
    private String secretKey;
    @Value("${minIO.imgBucket}")
    private String imgBucket;

    @Bean
    MinioClient minioClient() {
        MinioClient minioClient;
        try {
            minioClient = new MinioClient(minIOUrl, minIOPort, accessKey, secretKey, false);
            boolean isExist = minioClient.bucketExists(imgBucket);
            if(!isExist) {
                minioClient.makeBucket(imgBucket);
            }
        } catch (Exception e) {
            log.error("oss client init failed!" + e.getMessage());
            throw new OSSExceptionExam(ResultEnum.OSS_CLIENT_INIT_FAILED);
        }
        return minioClient;
    }
}
