package at.technikum.backend.controller;

import at.technikum.backend.entity.FileEntity;
import at.technikum.backend.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final StorageService storageService;

    public FileUploadController(StorageService storageService){
        this.storageService = storageService;
        this.storageService.init();
    }


    @GetMapping
    public ResponseEntity<?> listAllFiles(){
        var files = storageService.loadAll()
            .map(Path::toString)
            .toList();

        return ResponseEntity.ok(files);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }

    @PostMapping("/upload")
    public ResponseEntity<FileEntity> handleFileUpload(@RequestParam("file")MultipartFile file){
        FileEntity fileEntity = storageService.store(file);
        return ResponseEntity.ok(fileEntity);
    }

}
