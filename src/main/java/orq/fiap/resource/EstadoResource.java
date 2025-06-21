package orq.fiap.resource;

import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.services.HistoricoProcessamentoService;

@ApplicationScoped
@Path("/estado")
public class EstadoResource {
    @Inject
    HistoricoProcessamentoService historicoProcessamentoService;

    @Path("{uuid}")
    @GET
    public HistoricoProcessamento getEstadoAtual(@PathParam("uuid") String uuid) {
        return this.historicoProcessamentoService.getEstadoAtual(uuid);
    }

    @Path("{uuid}/historico")
    @GET
    public List<HistoricoProcessamento> getHistorico(@PathParam("uuid") String uuid) {
        return this.historicoProcessamentoService.getHistorico(uuid);
    }

    @Incoming("processador-responses")
    @RunOnVirtualThread
    @ActivateRequestContext
    public Response processarResposta(String mensagem)
            throws JsonProcessingException, ReflectiveOperationException {
        historicoProcessamentoService.atualizarEstado(mensagem);
        return Response.ok().build();
    }
}
