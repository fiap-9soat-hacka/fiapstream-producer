package orq.fiap.resource;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.List;

import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.inject.Inject;
import orq.fiap.RabbitMqQuarkusTestResource;
import orq.fiap.dto.MessageResponseData;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.entity.Processamento;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.services.EstadoProcessamentoService;

@QuarkusTest
@QuarkusTestResource(RabbitMqQuarkusTestResource.class)
class EstadoResourceTest {

    @InjectMock
    EstadoProcessamentoService estadoProcessamentoService;

    @Inject
    @Connector("smallrye-in-memory")
    InMemoryConnector connector;

    @Test
    @TestSecurity(authorizationEnabled = false)
    void testaSucessoGetEstadoAtual() {
        Processamento processamento = new Processamento();
        processamento.setId(1L);
        processamento.setUuid("123e4567-e89b-12d3-a456-426614174000");

        when(estadoProcessamentoService.getEstadoAtual(any())).thenReturn(processamento);

        given()
                .when()
                .get("/estado/1")
                .then()
                .statusCode(200)
                .body(containsString("123e4567-e89b-12d3-a456-426614174000"));
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    void testaSucessoGetHistorico() {
        HistoricoProcessamento historico = new HistoricoProcessamento();
        historico.setId(1L);
        historico.setUuid("123e4567-e89b-12d3-a456-426614174000");
        historico.setEstado(EstadoProcessamento.CONCLUIDO);
        List<HistoricoProcessamento> historicos = List.of(historico);

        when(estadoProcessamentoService.getHistorico(any())).thenReturn(historicos);

        given()
                .when()
                .get("/estado/1/historico")
                .then()
                .statusCode(200)
                .body(containsString("123e4567-e89b-12d3-a456-426614174000"))
                .body(containsString("CONCLUIDO"));
    }

    @Test
    void testaSucessoProcessadorResposta()
            throws JsonProcessingException, InvocationTargetException, ReflectiveOperationException {
        InMemorySource<String> ordersIn = connector.source("processador-responses");

        MessageResponseData message = new MessageResponseData();
        message.setKey("123e4567-e89b-12d3-a456-426614174000");
        message.setEstado(EstadoProcessamento.CONCLUIDO);
        message.setFilename("teste");

        ObjectMapper objectMapper = new ObjectMapper();
        ordersIn.send(objectMapper.writeValueAsString(message));

        await().atMost(Duration.ofSeconds(2));
    }
}
