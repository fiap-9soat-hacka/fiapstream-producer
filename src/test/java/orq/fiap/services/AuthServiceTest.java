package orq.fiap.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Test;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.ForbiddenException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import orq.fiap.entity.Processamento;
import orq.fiap.entity.Usuario;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.repository.ProcessamentoRepository;

@QuarkusTest
public class AuthServiceTest {

    @Inject
    AuthService authService;

    @InjectMock
    UsuarioService usuarioService;

    @InjectMock
    ProcessamentoRepository processamentoRepository;

    @InjectMock
    JsonWebToken jwt;

    @Test
    void testaSucessoSignIn() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@teste.com.br");
        usuario.setUsername("teste");
        usuario.setPassword(BcryptUtil.bcryptHash("senha"));

        when(usuarioService.findByUsername(any())).thenReturn(usuario);

        assertDoesNotThrow(() -> authService.signIn("a", "senha"));
    }

    @Test
    void testaForbiddenValidarUsuario() {
        Processamento processamento = new Processamento();
        processamento.setUuid("teste");
        processamento.setEstado(EstadoProcessamento.CONCLUIDO);
        processamento.setFilename("teste");
        processamento.setId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        processamento.setUsuario(usuario);

        when(processamentoRepository.findById(anyString())).thenReturn(processamento);
        when(jwt.getClaim("userId")).thenReturn("2");

        assertThrows(ForbiddenException.class, () -> authService.validarUsuario("teste"));
    }

}
