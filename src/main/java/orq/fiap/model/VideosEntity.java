package orq.fiap.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.utils.EstadoConverter;

@Entity
@Data
@Table(name = "Videos")
public class VideosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_video")
    private Integer codigoVideo;

    @Column(name = "id_video_s3")
    private String idVideoS3;

    @Column(name = "codigo_cliente")
    private Integer codigoCliente;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "estado_processamento")
    @Convert(converter = EstadoConverter.class)
    private EstadoProcessamento estadoProcessamento;

    @Column(name = "webhook_url")
    private String webhookUrl;

    @Column(name = "ts_alteracao")
    private LocalDateTime tsAlteracao;
}
