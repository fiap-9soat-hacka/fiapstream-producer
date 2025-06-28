package orq.fiap.repository;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.entity.Processamento;

@ApplicationScoped
public class ProcessamentoRepository implements PanacheRepositoryBase<Processamento, Integer> {

    public Processamento findById(String uuid) {
        return find("""
                uuid = ?1
                    """, uuid).firstResult();
    }
}
