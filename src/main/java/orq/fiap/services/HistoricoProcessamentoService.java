package orq.fiap.services;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import orq.fiap.dto.ResponseData;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.entity.Processamento;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.repository.HistoricoProcessamentoRepository;
import orq.fiap.repository.ProcessamentoRepository;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.aayushatharva.brotli4j.common.annotations.Local;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;

@RequestScoped
public class HistoricoProcessamentoService {

    @Inject
    HistoricoProcessamentoRepository historicoProcessamentoRepository;

    @Inject
    ProcessamentoRepository processamentoRepository;

    /**
     * Recupera o ultimo estado (atual) relacionado a um processamento.
     * 
     * @param uuid
     * @return
     */
    public HistoricoProcessamento getEstadoAtual(String uuid) {
        HistoricoProcessamento processamento = this.historicoProcessamentoRepository.findLatestById(uuid);
        return processamento;
    }

    public List<HistoricoProcessamento> getHistorico(String uuid) {
        return this.historicoProcessamentoRepository.findAllById(uuid);
    }

    @Transactional(rollbackOn = Exception.class)
    public void criar(VideoDataUUID videoDataUUID) throws IllegalAccessException, InvocationTargetException {
        Processamento processamento = new Processamento();
        processamento.setWebhookUrl(videoDataUUID.getWebhookUrl());
        processamento.setUuid(videoDataUUID.getUuid());
        processamento.setEstado(EstadoProcessamento.PENDENTE);

        HistoricoProcessamento historicoProcessamento = new HistoricoProcessamento();
        BeanUtils.copyProperties(historicoProcessamento, processamento);
        historicoProcessamento.setTimestamp(LocalDateTime.now());

        processamentoRepository.persist(processamento);
        historicoProcessamentoRepository.persist(historicoProcessamento);
    }

    public void atualizarEstado(String request)
            throws JsonProcessingException, InvocationTargetException, ReflectiveOperationException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData responseData = objectMapper.readValue(request, ResponseData.class);

        if (responseData.getEstado() == EstadoProcessamento.ERRO) {

        }

        persistirNaBase(responseData);
    }

    @Transactional(rollbackOn = Exception.class)
    public void persistirNaBase(ResponseData responseData)
            throws ReflectiveOperationException, InvocationTargetException {
        Processamento processamento = processamentoRepository
                .findById(responseData.getKey());

        HistoricoProcessamento novoHistoricoProcessamento = new HistoricoProcessamento();
        BeanUtils.copyProperties(novoHistoricoProcessamento, processamento);
        novoHistoricoProcessamento.setId(null);
        novoHistoricoProcessamento.setEstado(responseData.getEstado());
        novoHistoricoProcessamento.setTimestamp(LocalDateTime.now());

        historicoProcessamentoRepository.persist(novoHistoricoProcessamento);
    }
}
