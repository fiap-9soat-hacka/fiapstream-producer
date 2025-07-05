package orq.fiap.socket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint("/processamento/{userId}")
public class ProcessamentoSocketServer {

    Map<String, Session> sessions = new ConcurrentHashMap<>();
    static final String VIDEO = "Video ";

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        broadcast(VIDEO + userId + " processing opened");
        sessions.put(userId, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        sessions.remove(userId);
        broadcast(VIDEO + userId + " processing closed");
    }

    @OnError
    public void onError(Session session, @PathParam("userId") String userId, Throwable throwable) {
        sessions.remove(userId);
        broadcast(VIDEO + userId + " processing error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) {
        broadcast(userId);
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    Log.info("Unable to send message to socket: " + result.getException());
                }
            });
        });
    }
}
