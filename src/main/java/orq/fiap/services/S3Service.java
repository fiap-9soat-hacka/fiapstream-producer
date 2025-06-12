package orq.fiap.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import orq.fiap.dto.FormData;
import orq.fiap.rest.out.CommonResource;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ApplicationScoped
public class S3Service {

    @Inject
    S3Client s3Client;

    @Inject
    CommonResource commonResource;

    public void uploadFile(FormData formData) {

        // if (formData.filename == null || formData.filename.isEmpty()) {
        // return Response.status(Status.BAD_REQUEST).build();
        // }

        // if (formData.mimetype == null || formData.mimetype.isEmpty()) {
        // return Response.status(Status.BAD_REQUEST).build();
        // }

        PutObjectResponse putResponse = s3Client.putObject(commonResource.buildPutRequest(formData),
                RequestBody.fromFile(formData.video));

        if (putResponse == null) {
            throw new RuntimeException("Failed to upload file to S3");
        }
    }
}
