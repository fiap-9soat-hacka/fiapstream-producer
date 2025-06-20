package orq.fiap.dao;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.model.ClientesEntity;
import orq.fiap.model.VideosEntity;

@ApplicationScoped
public class VideoDao implements PanacheRepositoryBase<VideosEntity, Integer> {

    @Transactional(rollbackOn = Exception.class)
    public void updateVideoState(Integer videoUuid, EstadoProcessamento estado) {
        VideosEntity video = find("""
                WHERE idVideoS3 = :videoUuid
                """).singleResult();
        if (video != null) {
            video.setEstadoProcessamento(estado);
            persist(video);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void armazenarVideo(VideoDataUUID video, Integer codigoCliente) {
        VideosEntity videoEntity = new VideosEntity();
        videoEntity.setIdVideoS3(video.uuid);
        videoEntity.setCodigoCliente(codigoCliente);
        videoEntity.setNomeArquivo(video.filename);
        videoEntity.setEstadoProcessamento(EstadoProcessamento.PENDENTE);
        videoEntity.setWebhookUrl(video.webhookUrl);
        videoEntity.setTsAlteracao(LocalDateTime.now());

        persist(videoEntity);
    }
}
