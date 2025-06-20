package orq.fiap.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import orq.fiap.dao.VideoDao;
import orq.fiap.dto.VideoData;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.integration.s3.CommonResource;
import orq.fiap.model.ClientesEntity;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ApplicationScoped
public class S3Service {

    @Inject
    S3Client s3Client;

    @Inject
    CommonResource commonResource;

    @Inject
    VideoDao videoDao;

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
                RequestBody.fromFile(videoData.video));

        if (putResponse == null) {
            throw new WebApplicationException("Failed to upload file to S3");
        }

        // TODO arrumar isso aqui
        videoDao.armazenarVideo(videoDataUUID, 1, "teste");

        ObjectMapper objectMapper = new ObjectMapper();
        String videoDataUUIDJson = objectMapper.writeValueAsString(videoDataUUID);
        emitter.send(videoDataUUIDJson);
    }
}
