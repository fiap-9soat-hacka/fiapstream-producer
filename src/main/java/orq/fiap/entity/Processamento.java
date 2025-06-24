package orq.fiap.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.utils.EstadoProcessamentoConverter;

@Entity
@Data
public class Processamento {
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
    @Column(nullable = false)
    private String filename;
    @ManyToOne
    @JoinColumn(nullable = false, name = "userId", updatable = false, insertable = false)
    private Usuario usuario;
    @Column(nullable = false, name = "userId")
    private Long userId;
}
