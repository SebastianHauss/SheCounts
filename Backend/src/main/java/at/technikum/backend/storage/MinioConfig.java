package at.technikum.backend.storage;

import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.technikum.backend.storage.properties.MinioProperties;

@Configuration
@AllArgsConstructor
public class MinioConfig {
    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(
                        minioProperties.getUrl(),
                        minioProperties.getPort(),
                        minioProperties.getUrl().contains("https"))
                .credentials(
                        minioProperties.getUser(),
                        minioProperties.getPassword())
                .build();
    }
}