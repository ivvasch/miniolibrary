package ru.ivvasch.niolibrary.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfiguration {

    private MinioServer minioServer;
    private MinioUser minioUser;

        public record MinioServer(String host, int port, String bucket) {
    }

        public record MinioUser(String login, String password) {
    }

}
