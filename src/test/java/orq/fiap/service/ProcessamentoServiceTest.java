package orq.fiap.service;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.jboss.resteasy.reactive.client.api.ClientMultipartForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Path;

import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import orq.fiap.RabbitMqQuarkusTestResource;
import orq.fiap.dto.VideoData;
import orq.fiap.services.AuthService;
import orq.fiap.services.EstadoProcessamentoService;
import orq.fiap.services.ProcessamentoService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@QuarkusTest
@QuarkusTestResource(RabbitMqQuarkusTestResource.class)
public class ProcessamentoServiceTest {

    @Inject
    ProcessamentoService processamentoService;

    @Inject
    @Connector("smallrye-in-memory")
    InMemoryConnector connector;

    @InjectMock
    S3Client s3Client;

    @InjectMock
    EstadoProcessamentoService estadoProcessamentoService;

    @InjectMock
    AuthService authService;

    private FileUpload fileUp;
    private File file;
    private VideoData videoData = new VideoData();

    @BeforeEach
    void setUp() {
        file = new File("src/test/resources/testevideo.mp4");
        fileUp = new FileUpload() {

            @Override
            public String name() {
                return "teste";
            }

            @Override
            public Path filePath() {
                return file.toPath();
            }

            @Override
            public String fileName() {
                return file.getName();
            }

            @Override
            public long size() {
                return 1L;
            }

            @Override
            public String contentType() {
                return "video/mp4";
            }

            @Override
            public String charSet() {
                return "ok";
            }

        };
        videoData.setVideo(fileUp);
        videoData.setWebhookUrl("http://teste.com");
    }

    @Test
    void testaSucessoIniciarProcessamento() {
        InMemorySink<String> out = connector.sink("processador-requests");
        PutObjectResponse putResponse = PutObjectResponse.builder().size(1L).versionId("1").build();
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(putResponse);

        assertDoesNotThrow(() -> processamentoService.iniciarProcessamento(videoData));

        await().atMost(Duration.ofSeconds(2));
        out.received().size();
    }

    @Test
    void testaFalhaVideoIniciarProcessamento() {
        videoData.setVideo(null);
        PutObjectResponse putResponse = PutObjectResponse.builder().size(1L).versionId("1").build();
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(putResponse);

        assertThrows(BadRequestException.class, () -> processamentoService.iniciarProcessamento(videoData));

    }

    @Test
    void testaFalhaS3IniciarProcessamento() {

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);
        assertThrows(WebApplicationException.class, () -> processamentoService.iniciarProcessamento(videoData));

    }
}
