package com.bjfu.exam.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
        MinioClient minioClient = null;
        try {
            minioClient = new MinioClient(minIOUrl, minIOPort, accessKey, secretKey, false);
            boolean isExist = minioClient.bucketExists(imgBucket);
            if(!isExist) {
                minioClient.makeBucket(imgBucket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minioClient;
    }
}
