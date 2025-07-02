package orq.fiap.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import orq.fiap.entity.Processamento;
import orq.fiap.enums.EstadoProcessamento;

@QuarkusTest
public class ProcessamentoRepositoryTest {

    @Inject
    ProcessamentoRepository repository;

    @Test
    void testaSucessoFindById() {
        Processamento resp = repository.findById("testevideo - Copia (2).mp4-96c69a19-8b00-43fd-8f80-0c020fdb481f");

        assertEquals(EstadoProcessamento.PENDENTE, resp.getEstado());
        assertEquals(1L, resp.getUserId());
    }
}
