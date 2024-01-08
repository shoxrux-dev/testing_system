package com.example.testing_system.util;

import com.example.testing_system.constant.FilePathConstant;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@UtilityClass
public class FileUtil {

    @SneakyThrows
    public String uploadImage(MultipartFile file, String type) {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String[] split = originalFilename.split("\\.");
        String fileExtension = split[split.length - 1];
        String fileName = UUID.randomUUID() + "." + fileExtension;
        Path uploadPath = Paths.get(FilePathConstant.FILE_UPLOAD_PATH+type);
        Path filePath = uploadPath.resolve(fileName);

        Files.createDirectories(uploadPath);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath);
        }

        return fileName;
    }

    public boolean deleteImage(String image, String type) {
        String imagePath = FilePathConstant.FILE_UPLOAD_PATH + type + "/" + image;
        Path filePath = Paths.get(imagePath);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

}
