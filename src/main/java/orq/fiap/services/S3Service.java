package orq.fiap.services;

import java.io.IOException;
import java.util.UUID;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import orq.fiap.dto.VideoData;
import orq.fiap.dto.VideoDataUUID;
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

    @Channel("processador-requests")
    Emitter<String> emitter;

    public void uploadFile(VideoData videoData) throws IOException, MimeTypeException {

        if (videoData.filename == null || videoData.filename.isEmpty()) {
            throw new BadRequestException("Filename is required");
        }

        if (videoData.video == null || !videoData.video.exists()) {
            throw new BadRequestException("Video file is required and must exist");
        }

        Tika tika = new Tika();
        String mimeType = tika.detect(videoData.video);

        if (!mimeType.startsWith("video/")) {
            throw new BadRequestException("Invalid video file type: " + mimeType);
        }

        String uuid = videoData.filename + "-" + UUID.randomUUID().toString();
        VideoDataUUID videoDataUUID = new VideoDataUUID(videoData, mimeType, uuid);

        PutObjectResponse putResponse = s3Client.putObject(commonResource.buildPutRequest(videoDataUUID),
                RequestBody.fromFile(videoDataUUID.video));

        if (putResponse == null) {
            throw new WebApplicationException("Failed to upload file to S3");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String videoDataUUIDJson = objectMapper.writeValueAsString(videoDataUUID);
        emitter.send(videoDataUUIDJson);
    }
}
