package orq.fiap.repository;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import orq.fiap.entity.Processamento;

@ApplicationScoped
public class ProcessamentoRepository implements PanacheRepositoryBase<Processamento, Integer> {

    public Processamento findById(String uuid) {
        return find("""
                uuid = ?1
                    """, uuid).firstResult();
    }

    public Optional<Processamento> findByIdOptional(String uuid) {
        return find("""
                uuid = ?1
                    """, uuid).firstResultOptional();
    }
}
