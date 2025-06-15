package orq.fiap.rest.out;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import orq.fiap.dto.VideoDataUUID;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
public class CommonResource {

    @ConfigProperty(name = "bucket.name")
    String bucketName;

    public PutObjectRequest buildPutRequest(VideoDataUUID videoData) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(videoData.uuid)
                .contentType(videoData.mimeType)
                .build();
    }

}
