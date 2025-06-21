package orq.fiap.services;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import jakarta.transaction.Transactional;
import org.apache.tika.Tika;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import orq.fiap.dto.VideoData;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.entity.HistoricoProcessamento;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ApplicationScoped
public class ProcessamentoService {

    @Inject
    S3Client s3Client;

    @Inject
    HistoricoProcessamentoService historicoProcessamentoService;

    @ConfigProperty(name = "bucket.name")
    String bucketName;

    @Channel("processador-requests")
    Emitter<String> emitter;

    /**
     * Salva o vídeo no bucket S3 e envia solicitação de processamento
     */
    public void iniciarProcessamento(VideoData videoData) throws IOException {
        if (videoData.getVideo() == null) {
            throw new BadRequestException("Video file is required and must exist");
        }

        File uploadedFile = videoData.getVideo().uploadedFile().toFile();

        Tika tika = new Tika();
        String mimeType = tika.detect(uploadedFile);

        if (!mimeType.startsWith("video/")) {
            throw new BadRequestException("Invalid video file type: " + mimeType);
        }

        String uuid = uploadedFile.getName() + "-" + UUID.randomUUID();
        VideoDataUUID videoDataUUID = new VideoDataUUID(videoData, mimeType, uuid);

        PutObjectResponse putResponse = s3Client.putObject(buildPutRequest(videoDataUUID),
                RequestBody.fromFile(uploadedFile));

        if (putResponse == null) {
            throw new WebApplicationException("Failed to upload file to S3");
        }

        ObjectMapper mapper = new ObjectMapper();
        String encoded = mapper.writeValueAsString(videoDataUUID);

        historicoProcessamentoService.criar(videoDataUUID);

        emitter.send(encoded);
    }

    private PutObjectRequest buildPutRequest(VideoDataUUID videoData) {
        return PutObjectRequest.builder()
            .bucket(bucketName)
            .key(videoData.getUuid())
            .contentType(videoData.getMimeType())
            .build();
    }
}
