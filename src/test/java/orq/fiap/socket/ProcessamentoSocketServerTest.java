package orq.fiap.socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Session;
import orq.fiap.dto.VideoDataUUID;

@QuarkusTest
public class ProcessamentoSocketServerTest {

    ProcessamentoSocketServer processamentoSocket;

    @BeforeEach
    public void setup() {
        processamentoSocket = new ProcessamentoSocketServer();
        processamentoSocket.sessions = new ConcurrentHashMap<>();
    }

    @Test
    public void testOnOpen() {
        Session mockSession = mock(Session.class);
        String uuid = "test-uuid";

        processamentoSocket.onOpen(mockSession, uuid);

        assertTrue(processamentoSocket.sessions.containsKey(uuid), "Session should be added to the map.");
    }

    @Test
    public void testOnClose() {
        Session mockSession = mock(Session.class);
        String uuid = "test-uuid";

        processamentoSocket.sessions.put(uuid, mockSession);
        processamentoSocket.onClose(mockSession, uuid);

        assertTrue(!processamentoSocket.sessions.containsKey(uuid), "Session should be removed from the map.");
    }

    @Test
    public void testOnError() {
        Session mockSession = mock(Session.class);
        String uuid = "test-uuid";
        Throwable mockThrowable = new Throwable("Test error");

        processamentoSocket.sessions.put(uuid, mockSession);
        processamentoSocket.onError(mockSession, uuid, mockThrowable);

        assertTrue(!processamentoSocket.sessions.containsKey(uuid),
                "Session should be removed from the map after error.");
    }

    @Test
    public void testOnMessage() throws JsonProcessingException {
        VideoDataUUID mockVideoData = new VideoDataUUID();
        mockVideoData.setUuid("test-uuid");
        ObjectMapper mapper = new ObjectMapper();

        processamentoSocket.sessions.put("test-uuid", mock(Session.class));
        processamentoSocket.onMessage("123", mapper.writeValueAsString(mockVideoData));

        String expectedMessage = mapper.writeValueAsString(mockVideoData);
        // Verify broadcast logic (mocking session interactions)
        processamentoSocket.sessions.values().forEach(session -> {
            verify(session.getAsyncRemote(), times(1)).sendObject(eq(expectedMessage), any());
        });
    }
}
