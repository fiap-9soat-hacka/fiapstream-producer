package orq.fiap.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import orq.fiap.dto.MessageResponseData;
import orq.fiap.dto.ResponseData;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.entity.Processamento;
import orq.fiap.entity.Usuario;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.repository.HistoricoProcessamentoRepository;
import orq.fiap.repository.ProcessamentoRepository;
import orq.fiap.services.*;
import orq.fiap.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

@QuarkusTest
class EstadoProcessamentoServiceTest {

    @Inject
    EstadoProcessamentoService service;

    @InjectMock
    HistoricoProcessamentoRepository historicoRepo;

    @InjectMock
    ProcessamentoRepository processamentoRepo;

    @InjectMock
    WebhookService webhookService;

    @InjectMock
    EmailService emailService;

    @InjectMock
    UsuarioService usuarioService;

    @InjectMock
    AuthService authService;

    @InjectMock
    StringUtils stringUtils;

    Usuario mockUser;
    Processamento mockProcessamento;

    @BeforeEach
    void setup() {
        mockUser = new Usuario();
        mockUser.setId(1L);
        mockUser.setEmail("user@example.com");

        mockProcessamento = new Processamento();
        mockProcessamento.setUuid("uuid-123");
        mockProcessamento.setWebhookUrl("http://webhook.url");
        mockProcessamento.setFilename("video.mp4");
        mockProcessamento.setUsuario(mockUser);
    }

    @Test
    void testGetEstadoAtual() {
        when(authService.validarUsuario("uuid-123")).thenReturn(mockProcessamento);

        Processamento result = service.getEstadoAtual("uuid-123");

        assertEquals(mockProcessamento, result);
        verify(authService).validarUsuario("uuid-123");
    }

    @Test
    void testGetHistorico() {
        when(authService.validarUsuario("uuid-123")).thenReturn(mockProcessamento);
        when(historicoRepo.findAllById("uuid-123")).thenReturn(List.of(new HistoricoProcessamento()));

        List<HistoricoProcessamento> result = service.getHistorico("uuid-123");

        assertEquals(1, result.size());
        verify(authService).validarUsuario("uuid-123");
        verify(historicoRepo).findAllById("uuid-123");
    }

    @Test
    void testCriar() throws Exception {
        VideoDataUUID videoData = new VideoDataUUID();
        videoData.setUuid("uuid-123");
        videoData.setFilename("video.mp4");
        videoData.setWebhookUrl("http://webhook.url");

        when(usuarioService.getAuthUser()).thenReturn(mockUser);

        service.criar(videoData);

        verify(processamentoRepo).persist(any(Processamento.class));
        verify(historicoRepo).persist(any(HistoricoProcessamento.class));
        verify(webhookService).sendData(eq("http://webhook.url"), any(ResponseData.class));
    }

    @Test
    void testAtualizarEstadoWithErro() throws Exception {
        MessageResponseData responseData = new MessageResponseData();
        responseData.setEstado(EstadoProcessamento.ERRO);
        responseData.setKey("uuid-123");
        responseData.setFilename("video.mp4");

        when(stringUtils.convert(anyString(), eq(MessageResponseData.class))).thenReturn(responseData);
        when(processamentoRepo.findById("uuid-123")).thenReturn(mockProcessamento);

        service.atualizarEstado("some-json");

        verify(emailService).sendEmail("user@example.com", "video.mp4");
        verify(webhookService).sendData(eq("http://webhook.url"), eq(responseData));
        verify(historicoRepo).persist(any(HistoricoProcessamento.class));
    }

    @Test
    void testPersistirNaBaseEEnviarWebhook() throws Exception {
        MessageResponseData responseData = new MessageResponseData();
        responseData.setEstado(EstadoProcessamento.CONCLUIDO);
        responseData.setKey("uuid-123");

        when(processamentoRepo.findById("uuid-123")).thenReturn(mockProcessamento);

        service.persistirNaBaseEEnviarWebhook(responseData);

        verify(webhookService).sendData(eq("http://webhook.url"), eq(responseData));
        verify(historicoRepo).persist(any(HistoricoProcessamento.class));
    }
}
