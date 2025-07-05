package orq.fiap.socket;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

@ApplicationScoped
@ClientEndpoint
public class ProcessamentoSocketClient {

    Session session;

    static final LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();

    @OnOpen
    public void open(Session session) {
        MESSAGES.add("CONNECT");
        session.getAsyncRemote().sendText("_ready_");
    }

    @OnMessage
    public void message(String uuid, Session session) {
        MESSAGES.add(uuid);
        session.getAsyncRemote().sendText(uuid);
    }

    @OnClose
    public void close(Session session) throws IOException {
        session.close();
    }

}
