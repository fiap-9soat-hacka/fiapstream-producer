package orq.fiap.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import orq.fiap.entity.Usuario;

@QuarkusTest
public class UsuarioRepositoryTest {

    @Inject
    UsuarioRepository repository;

    @Test
    @Transactional
    void testaSucessoAdicionar() {
        Long newUserId = repository.add("teste3", "teste3", "teste3@t.com", "user");

        Usuario usuario = repository.findById(newUserId);
        assertEquals("teste3", usuario.getUsername());
        assertEquals("teste3@t.com", usuario.getEmail());
        assertEquals("user", usuario.getRole());
    }
}
