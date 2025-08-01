package orq.fiap.services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.UUID;

import jakarta.ws.rs.NotFoundException;
import org.apache.tika.Tika;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import orq.fiap.dto.VideoData;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.entity.Processamento;
import orq.fiap.enums.EstadoProcessamento;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ApplicationScoped
public class ProcessamentoService {

    @Inject
    S3Client s3Client;

    @Inject
    EstadoProcessamentoService estadoProcessamentoService;

    @ConfigProperty(name = "bucket.name")
    String bucketName;

    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.access-key-id")
    String acessKey;

    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.secret-access-key")
    String secretAcessKey;

    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.session-token")
    String sessionToken;

    @Channel("processador-requests")
    Emitter<String> emitter;

    @Inject
    AuthService authService;

    /**
     * Salva o vídeo no bucket S3 e envia solicitação de processamento
     * 
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public String iniciarProcessamento(VideoData videoData)
            throws IOException, IllegalAccessException, InvocationTargetException {
        if (videoData.getVideo() == null) {
            throw new BadRequestException("Video file is required and must exist");
        }

        File uploadedFile = videoData.getVideo().uploadedFile().toFile();

        Tika tika = new Tika();
        String mimeType = tika.detect(uploadedFile);

        if (!mimeType.startsWith("video/")) {
            throw new BadRequestException("Invalid video file type: " + mimeType);
        }

        String uuid = videoData.getVideo().fileName() + "-" + UUID.randomUUID();
        VideoDataUUID videoDataUUID = new VideoDataUUID(videoData.getVideo().fileName(), uuid, mimeType,
                videoData.getWebhookUrl());

        PutObjectResponse putResponse = s3Client.putObject(buildPutRequest(videoDataUUID),
                RequestBody.fromFile(uploadedFile));

        if (putResponse == null) {
            throw new WebApplicationException("Failed to upload file to S3");
        }

        ObjectMapper mapper = new ObjectMapper();
        String encoded = mapper.writeValueAsString(videoDataUUID);

        estadoProcessamentoService.criar(videoDataUUID);

        emitter.send(encoded);

        return uuid;
    }

    public String getPresignedUrl(String uuid) {
        Processamento processamento = authService.validarPermissaoAcesso(uuid);

        if (processamento.getPresignedUrl() == null) {
            throw new NotFoundException("A URL de acesso ao arquivo ainda não foi preenchida.");
        }

        return processamento.getPresignedUrl();
    }

    public String createPresignedGetUrl(String uuid) {
        System.setProperty("aws.accessKeyId", acessKey);
        System.setProperty("aws.secretAccessKey", secretAcessKey);
        System.setProperty("aws.sessionToken", sessionToken);

        try (S3Presigner presigner = S3Presigner.create()) {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uuid + ".zip")
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            Log.info("Presigned URL: " + presignedRequest.url().toString());

            return presignedRequest.url().toExternalForm();
        }
    }

    private PutObjectRequest buildPutRequest(VideoDataUUID videoData) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(videoData.getUuid())
                .contentType(videoData.getMimeType())
                .build();
    }
}
