package orq.fiap.entity;

import jakarta.persistence.*;
import lombok.Data;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.utils.EstadoProcessamentoConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "HistoricoProcessamento")
@Data
public class HistoricoProcessamento {
    // UUID da solicitação
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String uuid;
    @Column(nullable = false)
    @Convert(converter = EstadoProcessamentoConverter.class)
    private EstadoProcessamento estado;
    @Column(length = 500, nullable = true)
    private String descricaoEstado;
    @Column(nullable = true)
    private String webhookUrl;
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String filename;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(nullable = false, name = "userId")
    // private Usuario usuario;
    // @Column(nullable = false, insertable = false, updatable = false)
    // @Column(nullable = false)
    // private Long userId;
}
