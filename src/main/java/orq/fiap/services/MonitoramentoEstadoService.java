package orq.fiap.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import orq.fiap.dto.VideoDataUUID;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class MonitoramentoEstadoService {
    // TODO: Adicionar @Incoming da fila de RESPOSTAS aqui
    // TODO: Notificar usuario (webhook? email?) em caso de erro
}
