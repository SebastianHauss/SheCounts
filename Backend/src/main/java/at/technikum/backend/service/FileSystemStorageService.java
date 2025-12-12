package at.technikum.backend.service;

import at.technikum.backend.entity.FileEntity;
import at.technikum.backend.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements StorageService{

    private final Path rootLocation = Paths.get("/app/uploads");
    private final FileRepository fileRespository;

    @Override
    public void init() {
        try{
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public FileEntity store(MultipartFile file) {
        try {
            if (file.isEmpty()){
                throw new RuntimeException("Cannot store empty file");
            }
            Files.copy(
                file.getInputStream(),
                rootLocation.resolve(file.getOriginalFilename()),
                StandardCopyOption.REPLACE_EXISTING
            );

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(file.getOriginalFilename());
            fileEntity.setPath(rootLocation.resolve(file.getOriginalFilename()).toString());
            fileRespository.save(fileEntity);

            return fileEntity;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(rootLocation, 1)
                    .filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize);//relativize liefert relative Pfade, z.B. nur bild.png, nicht /app/uploads/bild.png
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()){
                return resource;
            }else {
                throw new RuntimeException("Could not read file: " + filename);
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            Files.walk(rootLocation)
                    .filter(path -> !path.equals(rootLocation))
                    .map(Path::toFile)
                    .forEach(file -> file.delete());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete files", e);
        }
    }
}
