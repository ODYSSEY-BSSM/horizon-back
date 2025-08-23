package odyssey.backend.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.S3Properties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final S3Properties s3Properties;
    private final AmazonS3 s3;

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            s3.putObject(s3Properties.getBucket(), fileName, file.getInputStream(), metadata);
            return fileName;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String fileName) {
        s3.deleteObject(s3Properties.getBucket(), extractFileName(fileName));
    }

    private String extractFileName(String url) {
        return url.substring(url.indexOf(".amazonaws.com/") + 14);
    }

}
