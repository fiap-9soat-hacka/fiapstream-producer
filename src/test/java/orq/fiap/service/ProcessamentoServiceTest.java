package orq.fiap.service;

import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import jakarta.inject.Inject;
import orq.fiap.RabbitMqQuarkusTestResource;
import orq.fiap.services.ProcessamentoService;

@QuarkusTest
@QuarkusTestResource(RabbitMqQuarkusTestResource.class)
public class ProcessamentoServiceTest {

    @Inject
    ProcessamentoService processamentoService;

    @Inject
    @Connector("smallrye-in-memory")
    InMemoryConnector connector;

    @Test
    void testaSucessoIniciarProcessamento() {

        InMemorySink<String> out = connector.sink("processador-requests");
        out.received().size();
    }
}
