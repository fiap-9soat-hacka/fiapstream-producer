package orq.fiap.services;

import java.time.LocalDateTime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import orq.fiap.dao.VideoDao;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.model.VideosEntity;

@ApplicationScoped
public class EstadoService {

    @Inject
    VideoDao videoDao;

    @Transactional(rollbackOn = Exception.class)
    public void setVideo() {
        // VideosEntity video = new VideosEntity();
        // video.setIdVideoS3("teste123123");
        // video.setCodigoCliente(1);
        // video.setNomeArquivo("teste.mp4");
        // video.setEstadoProcessamento(EstadoProcessamento.PENDENTE);
        // video.setWebhookUrl("http://example.com/webhook");
        // video.setTsAlteracao(LocalDateTime.now());

        // videoDao.persist(video);

        // videoDao.updateVideoState(1, EstadoProcessamento.PROCESSANDO);
    }
}
