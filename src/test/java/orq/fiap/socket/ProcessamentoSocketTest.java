package orq.fiap.socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class ProcessamentoSocketTest {

    @Inject
    ProcessamentoSocketClient processamentoSocket;

    @Test
    public void testMessage() throws InterruptedException {
        String testUuid = "test-uuid";
        processamentoSocket.message(testUuid);

        String retrievedMessage = ProcessamentoSocketClient.MESSAGES.poll(1, TimeUnit.SECONDS);
        assertEquals(testUuid, retrievedMessage, "The message should be added to the queue correctly.");
    }
}
