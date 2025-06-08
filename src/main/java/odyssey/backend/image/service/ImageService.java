package odyssey.backend.image.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.image.domain.Image;
import odyssey.backend.image.domain.ImageRepository;
import odyssey.backend.roadmap.domain.Roadmap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${upload.thumbnail-dir}")
    private String thumbnailDir;

    @Value("${upload.thumbnail-url-prefix}")
    private String thumbnailUrlPrefix;

    private final ImageRepository imageRepository;

    public Image save(MultipartFile file, Roadmap roadmap) {
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }

        try {
            Path uploadPath = Paths.get(thumbnailDir).toAbsolutePath();
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = uploadPath.resolve(uniqueFilename);
            file.transferTo(targetPath.toFile());

            return imageRepository.save(
                    Image.builder()
                            .url(thumbnailUrlPrefix + uniqueFilename)
                            .roadmap(roadmap)
                            .build()
            );

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }
}
