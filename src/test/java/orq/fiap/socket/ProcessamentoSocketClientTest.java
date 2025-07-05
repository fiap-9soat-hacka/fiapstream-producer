package orq.fiap.socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;

@QuarkusTest
public class ProcessamentoSocketClientTest {

    @Inject
    ProcessamentoSocketClient processamentoSocket;

    @TestHTTPResource("/processamento/1")
    URI uri;

    @Test
    void testMessage() throws InterruptedException, DeploymentException, IOException, URISyntaxException {
        String testUuid = "CONNECT";
        Session session = ContainerProvider
                .getWebSocketContainer()
                .connectToServer(ProcessamentoSocketClient.class,
                        uri);
        processamentoSocket.message(testUuid, session);

        String retrievedMessage = ProcessamentoSocketClient.MESSAGES.poll(1, TimeUnit.SECONDS);
        assertEquals(testUuid, retrievedMessage, "The message should be added to the queue correctly.");
    }
}
