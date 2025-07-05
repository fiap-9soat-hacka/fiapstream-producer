package orq.fiap.services;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.ws.rs.InternalServerErrorException;
import orq.fiap.dto.MessageResponseData;
import orq.fiap.dto.ResponseData;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.entity.Processamento;
import orq.fiap.entity.Usuario;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.repository.HistoricoProcessamentoRepository;
import orq.fiap.repository.ProcessamentoRepository;
import orq.fiap.socket.ProcessamentoSocket;
import orq.fiap.utils.StringUtils;

@RequestScoped
public class EstadoProcessamentoService {

    @Inject
    HistoricoProcessamentoRepository historicoProcessamentoRepository;

    @Inject
    ProcessamentoRepository processamentoRepository;

    @Inject
    WebhookService webhookService;

    @Inject
    EmailService emailService;

    @Inject
    UsuarioService usuarioService;

    @Inject
    StringUtils stringUtils;

    @Inject
    AuthService authService;

    @Inject
    ProcessamentoSocket processamentoSocket;

    @ConfigProperty(name = "websocket.uri")
    String websocketUri;

    public Processamento getEstadoAtual(String uuid) {
        return authService.validarUsuario(uuid);
    }

    public List<HistoricoProcessamento> getHistorico(String uuid) {
        authService.validarUsuario(uuid);

        return this.historicoProcessamentoRepository.findAllById(uuid);
    }

    @Transactional(rollbackOn = Exception.class)
    public void criar(VideoDataUUID videoDataUUID) throws IllegalAccessException, InvocationTargetException {
        Usuario usuarioAutenticado = usuarioService.getAuthUser();

        Processamento processamento = new Processamento();
        processamento.setFilename(videoDataUUID.getFilename());
        processamento.setWebhookUrl(videoDataUUID.getWebhookUrl());
        processamento.setUuid(videoDataUUID.getUuid());
        processamento.setEstado(EstadoProcessamento.PENDENTE);
        processamento.setUserId(usuarioAutenticado.getId());

        HistoricoProcessamento historicoProcessamento = new HistoricoProcessamento();
        BeanUtils.copyProperties(historicoProcessamento, processamento);
        historicoProcessamento.setTimestamp(LocalDateTime.now());

        processamentoRepository.persist(processamento);
        historicoProcessamentoRepository.persist(historicoProcessamento);

        ResponseData responseData = new ResponseData();
        responseData.setFilename(processamento.getFilename());
        responseData.setEstado(EstadoProcessamento.PENDENTE);

        webhookService.sendData(processamento.getWebhookUrl(), responseData);
    }

    public void atualizarEstado(String request)
            throws ReflectiveOperationException {
        MessageResponseData responseData = stringUtils.convert(request, MessageResponseData.class);

        if (responseData.getEstado() == EstadoProcessamento.ERRO) {
            Processamento processamento = processamentoRepository.findById(responseData.getKey());
            String email = processamento.getUsuario().getEmail();
            emailService.sendEmail(email, responseData.getFilename());
        } else if (responseData.getEstado() == EstadoProcessamento.CONCLUIDO) {
            try {
                URI uri = new URI(websocketUri);
                Session session = ContainerProvider.getWebSocketContainer().connectToServer(ProcessamentoSocket.class,
                        uri);
                processamentoSocket.open(session);
                processamentoSocket.message(responseData.getKey());
            } catch (Exception e) {
                // throw new InternalServerErrorException(e.getMessage());
                Log.info("Websocket mock não enviado");
            }
        }

        persistirNaBaseEEnviarWebhook(responseData);
    }

    @Transactional(rollbackOn = Exception.class)
    public void persistirNaBaseEEnviarWebhook(MessageResponseData responseData)
            throws ReflectiveOperationException {
        Processamento processamento = processamentoRepository
                .findById(responseData.getKey());
        processamento.setEstado(responseData.getEstado());

        webhookService.sendData(processamento.getWebhookUrl(), responseData);

        HistoricoProcessamento novoHistoricoProcessamento = new HistoricoProcessamento();
        BeanUtils.copyProperties(novoHistoricoProcessamento, processamento);
        novoHistoricoProcessamento.setId(null);
        novoHistoricoProcessamento.setEstado(responseData.getEstado());
        novoHistoricoProcessamento.setTimestamp(LocalDateTime.now());

        historicoProcessamentoRepository.persist(novoHistoricoProcessamento);
    }
}
