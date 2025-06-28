package orq.fiap.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import orq.fiap.dto.MessageResponseData;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.entity.Processamento;
import orq.fiap.entity.Usuario;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.repository.HistoricoProcessamentoRepository;
import orq.fiap.repository.ProcessamentoRepository;
import orq.fiap.services.AuthService;
import orq.fiap.services.EmailService;
import orq.fiap.services.EstadoProcessamentoService;
import orq.fiap.services.UsuarioService;
import orq.fiap.services.WebhookService;
import orq.fiap.utils.StringUtils;

@QuarkusTest
public class EstadoProcessamentoServiceTest {

    @Inject
    EstadoProcessamentoService estadoProcessamentoService;

    @InjectMock
    HistoricoProcessamentoRepository historicoProcessamentoRepository;

    @InjectMock
    ProcessamentoRepository processamentoRepository;

    @InjectMock
    WebhookService webhookService;

    @InjectMock
    EmailService emailService;

    @InjectMock
    UsuarioService usuarioService;

    @InjectMock
    AuthService authService;

    @Test
    void testaSucessoCriarProcessamento() {
        Usuario usuarioAutenticado = new Usuario();
        usuarioAutenticado.setId(1L);

        when(usuarioService.getAuthUser()).thenReturn(usuarioAutenticado);

        VideoDataUUID video = new VideoDataUUID("teste", "teste", "video", "http://teste.com");

        assertDoesNotThrow(() -> estadoProcessamentoService.criar(video));
    }

    @Test
    void testaSucessoAtualizarEstado() throws JsonProcessingException {
        Processamento proc = new Processamento();
        proc.setFilename("teste");
        Usuario usu = new Usuario();
        usu.setEmail("teste@teste.com");
        proc.setUsuario(usu);
        when(processamentoRepository.findById(anyString())).thenReturn(proc);

        MessageResponseData request = new MessageResponseData();
        request.setKey("teste");
        request.setFilename("teste");
        request.setEstado(EstadoProcessamento.ERRO);
        ObjectMapper mapper = new ObjectMapper();
        String req = mapper.writeValueAsString(request);

        assertDoesNotThrow(() -> estadoProcessamentoService.atualizarEstado(req));
    }

}
