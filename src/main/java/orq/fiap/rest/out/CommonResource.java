package orq.fiap.rest.out;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import orq.fiap.dto.FormData;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
public class CommonResource {

    @ConfigProperty(name = "bucket.name")
    String bucketName;

    public PutObjectRequest buildPutRequest(FormData formData) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(formData.filename)
                .contentType(formData.mimetype)
                .build();
    }

    public GetObjectRequest buildGetRequest(String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }

}
