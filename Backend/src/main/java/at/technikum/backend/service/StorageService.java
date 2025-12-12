package at.technikum.backend.service;

import at.technikum.backend.entity.FileEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    //Legt den Upload-Ordner an, falls er noch nicht existiert
    void init();

    //Speichert die hochgeladene Datei im Upload-Ordner
    FileEntity store(MultipartFile file);

    //Gibt eine Liste aller gespeicherten Dateien zurück
    Stream<Path> loadAll();

    //Gibt den Pfad zu einer bestimmten Datei zurück
    Path load(String filename);

    //Wandelt den Pfad in ein Resource Objekt um, das Spring direkt
    //an den HTTP-Response senden kann
    //Damit Controller Datei über GET /files/{filename} ausliefern kann
    Resource loadAsResource(String filename);

    //Löscht alle Datein im Upload Ordner (für Entwicklung oder Admin Funktion)
    void deleteAll();
}
