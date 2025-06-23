package orq.fiap.services;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import orq.fiap.dto.MessageResponseData;
import orq.fiap.dto.ResponseData;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.entity.Processamento;
import orq.fiap.enums.EstadoProcessamento;
import orq.fiap.repository.HistoricoProcessamentoRepository;
import orq.fiap.repository.ProcessamentoRepository;

@RequestScoped
public class EstadoProcessamentoService {

    @Inject
    HistoricoProcessamentoRepository historicoProcessamentoRepository;

    @Inject
    ProcessamentoRepository processamentoRepository;

    @Inject
    WebhookService webhookService;

    @Inject
    EmailService emailService;

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
        processamento.setFilename(videoDataUUID.getFilename());
        processamento.setWebhookUrl(videoDataUUID.getWebhookUrl());
        processamento.setUuid(videoDataUUID.getUuid());
        processamento.setEstado(EstadoProcessamento.PENDENTE);

        HistoricoProcessamento historicoProcessamento = new HistoricoProcessamento();
        BeanUtils.copyProperties(historicoProcessamento, processamento);
        historicoProcessamento.setTimestamp(LocalDateTime.now());

        processamentoRepository.persist(processamento);
        historicoProcessamentoRepository.persist(historicoProcessamento);

        ResponseData responseData = new ResponseData();
        responseData.setFilename(processamento.getFilename());
        responseData.setEstado(EstadoProcessamento.PENDENTE);

        webhookService.sendData(processamento.getWebhookUrl(), responseData);
    }

    public void atualizarEstado(String request)
            throws JsonProcessingException, InvocationTargetException, ReflectiveOperationException {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponseData responseData = objectMapper.readValue(request, MessageResponseData.class);

        if (responseData.getEstado() == EstadoProcessamento.ERRO) {
            Processamento processamento = processamentoRepository.findById(responseData.getKey());
            String email = processamento.getUsuarios().iterator().next().getEmail();
            // String email = "teste@teste.com"
            emailService.sendEmail(email, responseData.getFilename());
        }

        persistirNaBaseEEnviarWebhook(responseData);
    }

    @Transactional(rollbackOn = Exception.class)
    public void persistirNaBaseEEnviarWebhook(MessageResponseData responseData)
            throws ReflectiveOperationException, InvocationTargetException {
        Processamento processamento = processamentoRepository
                .findById(responseData.getKey());

        webhookService.sendData(processamento.getWebhookUrl(), responseData);

        HistoricoProcessamento novoHistoricoProcessamento = new HistoricoProcessamento();
        BeanUtils.copyProperties(novoHistoricoProcessamento, processamento);
        novoHistoricoProcessamento.setId(null);
        novoHistoricoProcessamento.setEstado(responseData.getEstado());
        novoHistoricoProcessamento.setTimestamp(LocalDateTime.now());

        historicoProcessamentoRepository.persist(novoHistoricoProcessamento);
    }
}
