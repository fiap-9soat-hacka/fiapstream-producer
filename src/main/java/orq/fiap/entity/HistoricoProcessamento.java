package orq.fiap.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import orq.fiap.enums.EstadoProcessamento;

import java.time.LocalDateTime;

@Entity()
@Data
public class HistoricoProcessamento {
    // UUID da solicitação
    @Id
    private String uuid;
    @Column
    @Enumerated(EnumType.STRING)
    private EstadoProcessamento estado;
    @Column(length = 500)
    private String descricaoEstado;
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dataHora;
    // Relações
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "userId")
    private Usuario usuario;

    @Column(name = "userId", nullable = false, insertable = false, updatable = false)
    private Long userId;
}
