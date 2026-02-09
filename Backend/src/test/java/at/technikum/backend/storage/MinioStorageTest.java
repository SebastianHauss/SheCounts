package at.technikum.backend.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import at.technikum.backend.storage.properties.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@ExtendWith(MockitoExtension.class)
@DisplayName("MinioStorage Unit Tests")
public class MinioStorageTest {

    @Mock
    MinioClient minioClient;
    @Mock
    MinioProperties minioProperties;

    @InjectMocks
    MinioStorage storage;

    // ====================================================================
    // POSITIVE FÄLLE
    // ====================================================================

    @Test
    void upload_shouldStoreFile_andReturnUuid() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "hello".getBytes());

        when(minioProperties.getBucket()).thenReturn("my-bucket");

        // when
        String id = storage.upload(file);

        // then
        assertNotNull(id);

        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void load_shouldReturnInputStream() throws Exception {
        when(minioProperties.getBucket()).thenReturn("my-bucket");

        GetObjectResponse response = mock(GetObjectResponse.class);

        when(minioClient.getObject(any(GetObjectArgs.class)))
                .thenReturn(response);

        InputStream result = storage.load("123");

        assertNotNull(result);
        assertEquals(response, result);
    }

    @Test
    void initBucket_shouldDoNothing_ifBucketExists() throws Exception {
        when(minioProperties.getBucket()).thenReturn("my-bucket");
        when(minioClient.bucketExists(any(BucketExistsArgs.class)))
                .thenReturn(true);

        storage.initBucket();

        verify(minioClient, never()).makeBucket(any(MakeBucketArgs.class));
    }

    @Test
    void initBucket_shouldCreateBucket_ifNotExists() throws Exception {
        when(minioProperties.getBucket()).thenReturn("my-bucket");
        when(minioClient.bucketExists(any(BucketExistsArgs.class)))
                .thenReturn(false);

        storage.initBucket();

        verify(minioClient).makeBucket(any(MakeBucketArgs.class));
    }

    // ====================================================================
    // NEGATIVE FÄLLE
    // ====================================================================

    @Test
    void upload_shouldWrapException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "hello".getBytes());

        when(minioProperties.getBucket()).thenReturn("my-bucket");

        doThrow(new RuntimeException("boom"))
                .when(minioClient)
                .putObject(any(PutObjectArgs.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> storage.upload(file));

        assertTrue(ex.getMessage().contains("Upload file failed"));
    }

    @Test
    void load_shouldWrapException() throws Exception {
        when(minioProperties.getBucket()).thenReturn("my-bucket");

        when(minioClient.getObject(any(GetObjectArgs.class)))
                .thenThrow(new RuntimeException("boom"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> storage.load("123"));

        assertTrue(ex.getMessage().contains("Load file failed"));
    }

    @Test
    void initBucket_shouldWrapException() throws Exception {
        when(minioProperties.getBucket()).thenReturn("my-bucket");
        when(minioClient.bucketExists(any(BucketExistsArgs.class)))
                .thenThrow(new RuntimeException("boom"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> storage.initBucket());

        assertTrue(ex.getMessage().contains("Failed to initialize"));
    }

}
