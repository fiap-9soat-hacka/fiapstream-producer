package orq.fiap.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.enums.EstadoProcessamento;

@QuarkusTest
public class HistoricoProcessamentoRepositoryTest {

    @Inject
    HistoricoProcessamentoRepository repository;

    @Test
    void testaSucessoFindBylatestById() {
        HistoricoProcessamento resp = repository
                .findLatestById("testevideo - Copia (2).mp4-96c69a19-8b00-43fd-8f80-0c020fdb481f");

        assertEquals("testevideo - Copia (2).mp4", resp.getFilename());
        assertEquals(EstadoProcessamento.CONCLUIDO, resp.getEstado());
    }

    @Test
    void testaSucessoFindAllById() {
        List<HistoricoProcessamento> lista = repository
                .findAllById("testevideo - Copia (2).mp4-96c69a19-8b00-43fd-8f80-0c020fdb481f");

        assertEquals(3, lista.size());
    }
}
