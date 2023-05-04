package ru.ivvasch.niolibrary.configuration;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    private final MinioConfiguration minioConfiguration;
    public BeanConfig(MinioConfiguration minioConfiguration) {
        this.minioConfiguration = minioConfiguration;
    }

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(minioConfiguration.getMinioServer().host(), minioConfiguration.getMinioServer().port(), false)
                .credentials(minioConfiguration.getMinioUser().login(), minioConfiguration.getMinioUser().password()).build();
    }
}
