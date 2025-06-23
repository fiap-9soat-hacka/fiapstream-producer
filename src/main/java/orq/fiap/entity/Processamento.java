package orq.fiap.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

    @ManyToMany
    @JoinTable(name = "UsuarioProcessamento", joinColumns = @JoinColumn(name = "usuarioId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "processamentoId", referencedColumnName = "id"))
    Set<Usuario> usuarios = new HashSet<>();
}
