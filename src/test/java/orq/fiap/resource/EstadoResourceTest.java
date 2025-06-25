package orq.fiap.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.awaitility.Awaitility.await;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.inject.Inject;
import orq.fiap.RabbitMqQuarkusTestResource;
import orq.fiap.dto.MessageResponseData;
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
