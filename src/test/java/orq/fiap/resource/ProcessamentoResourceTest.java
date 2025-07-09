package orq.fiap.resource;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import orq.fiap.entity.Processamento;
import orq.fiap.repository.ProcessamentoRepository;
import orq.fiap.services.ProcessamentoService;

@QuarkusTest
class ProcessamentoResourceTest {

    @InjectMock
    ProcessamentoService processamentoService;

    @Test
    @TestSecurity(authorizationEnabled = false)
    void testaSucessoSolicitarProcessamento() throws Exception {
        given()
                .multiPart("video", "test.mp4", "video/mp4")
                .when()
                .post("/processamento")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    void testaSucessoGetProcessamento() {
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        String url = "http://teste.com";
        when(processamentoService.getPresignedUrl(uuid)).thenReturn(url);

        given()
                .when()
                .get("/processamento/url-video/" + uuid)
                .then()
                .statusCode(200)
                .body(containsString(url));
    }
}
