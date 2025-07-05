package orq.fiap.socket;

import java.util.concurrent.LinkedBlockingDeque;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

@ApplicationScoped
@ClientEndpoint
public class ProcessamentoSocketClient {

    public static final LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();

    @OnOpen
    public void open(Session session) {
        MESSAGES.add("CONNECT");
        session.getAsyncRemote().sendText("finished");
    }

    @OnMessage
    public void message(String uuid) {
        MESSAGES.add(uuid);
    }

}
