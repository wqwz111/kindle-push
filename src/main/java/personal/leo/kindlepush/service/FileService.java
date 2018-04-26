package personal.leo.kindlepush.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.multipart.MultipartFile;
import personal.leo.kindlepush.error.StorageException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

@Service
public class FileService {

    private final Path rootDir;

    @Autowired
    public FileService(@Value("${root-dir:/upload}") String rootDirProp) {
        this.rootDir = Paths.get(rootDirProp);
    }

    public boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    @Async
    public ListenableFuture<Path> save (String userId, MultipartFile file) throws StorageException {
        Path path;
        String fileName = this.getFileName(file);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to save empty file: " + fileName);
            }
            if (fileName.contains("..")) {
                throw new StorageException("Failed to save file outside current directory: " + fileName);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path dir = this.rootDir.resolve(userId);
                Files.createDirectories(dir);
                path = dir.resolve(fileName);
                Files.copy(inputStream, path,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            return AsyncResult.forExecutionException(e);
//            throw new StorageException("Failed to store file " + fileName, e);
        }
        return AsyncResult.forValue(path);
    }

    public void saveAll(String userId, ArrayList<MultipartFile> fileList) throws StorageException {
        for (MultipartFile item : fileList) {
            this.save(userId, item);
        }
    }

    public String getFileName(MultipartFile file) {
        return StringUtils.cleanPath(file.getOriginalFilename());
    }

    public Path getFilePath(String userId, String fileName) {
        return rootDir.resolve(userId).resolve(fileName);
    }
}
