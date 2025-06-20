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
    public void updateVideoState(Integer id, EstadoProcessamento estado) {
        VideosEntity video = findById(id);
        if (video != null) {
            video.setEstadoProcessamento(estado);
            persist(video);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void armazenarVideo(VideoDataUUID video, Integer codigoCliente, String webhookUrl) {
        VideosEntity videoEntity = new VideosEntity();
        videoEntity.setIdVideoS3(video.uuid);
        videoEntity.setCodigoCliente(codigoCliente);
        videoEntity.setNomeArquivo(video.filename);
        videoEntity.setEstadoProcessamento(EstadoProcessamento.PENDENTE);
        videoEntity.setWebhookUrl(webhookUrl);
        videoEntity.setTsAlteracao(LocalDateTime.now());

        persist(videoEntity);
    }
}
