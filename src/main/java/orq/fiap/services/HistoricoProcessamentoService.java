package orq.fiap.services;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.repository.HistoricoProcessamentoRepository;

import java.util.List;

@RequestScoped
public class HistoricoProcessamentoService {

    @Inject
    HistoricoProcessamentoRepository historicoProcessamentoRepository;

    /**
     * Recupera o ultimo estado (atual) relacionado a um processamento.
     * @param uuid
     * @return
     */
    public HistoricoProcessamento getEstadoAtual(String uuid){
        HistoricoProcessamento processamento = this.historicoProcessamentoRepository.findLatestById(uuid);
        return processamento;
    }

    public List<HistoricoProcessamento> getHistorico(String uuid){
        return this.historicoProcessamentoRepository.findAllById(uuid);
    }

    public void criar(VideoDataUUID videoDataUUID){
        HistoricoProcessamento historicoProcessamento = new HistoricoProcessamento();
        historicoProcessamento.setWebhookUrl(videoDataUUID.getWebhookUrl());
        historicoProcessamento.setUuid(videoDataUUID.getUuid());
        historicoProcessamento.setEstado(EstadoProcessamento.PENDENTE);

        historicoProcessamentoRepository.persist(historicoProcessamento);
    }

    public void atualizarEstado(String uuid, EstadoProcessamento estado){
        HistoricoProcessamento historicoProcessamento = getEstadoAtual(uuid);

        historicoProcessamento.setEstado(estado);

        historicoProcessamentoRepository.persist(historicoProcessamento);
    }
}
