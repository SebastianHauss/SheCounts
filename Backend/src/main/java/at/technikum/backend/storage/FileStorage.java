package at.technikum.backend.storage;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * FileStorage interface
 */
public interface FileStorage {

    /**
     * 
     * Upload file to storage
     * @param file multipart file
     * @return id of file
     */
    String upload(MultipartFile file);

    /**
     * Load file from storage
     * @param id id of file
     * @return input stream of file
     */
    InputStream load(String id);
}
