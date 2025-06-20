package orq.fiap.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import orq.fiap.entity.HistoricoProcessamento;

import java.util.List;

@ApplicationScoped
public class HistoricoProcessamentoRepository implements PanacheRepositoryBase<HistoricoProcessamento, String> {
    public HistoricoProcessamento findLatestById(String uuid) {
        return find("uuid", uuid, Sort.by("timestamp", Sort.Direction.Descending)).firstResult();
    }

    public List<HistoricoProcessamento> findAllById(String uuid) {
        return find("uuid", uuid, Sort.by("timestamp", Sort.Direction.Descending)).list();
    }

}
