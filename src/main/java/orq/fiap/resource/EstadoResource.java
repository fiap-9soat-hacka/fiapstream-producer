package orq.fiap.resource;

import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.entity.Processamento;
import orq.fiap.services.EstadoProcessamentoService;

@ApplicationScoped
@Path("/estado")
public class EstadoResource {
    @Inject
    EstadoProcessamentoService estadoProcessamentoService;

    @Path("{uuid}")
    @GET
    @RolesAllowed({ "user" })
    public Processamento getEstadoAtual(@PathParam("uuid") String uuid) {
        return this.estadoProcessamentoService.getEstadoAtual(uuid);
    }

    @Path("{uuid}/historico")
    @GET
    @RolesAllowed({ "user" })
    public List<HistoricoProcessamento> getHistorico(@PathParam("uuid") String uuid) {
        return this.estadoProcessamentoService.getHistorico(uuid);
    }

    @Incoming("processador-responses")
    @RunOnVirtualThread
    @ActivateRequestContext
    public Response processarResposta(String mensagem)
            throws ReflectiveOperationException {
        estadoProcessamentoService.atualizarEstado(mensagem);
        return Response.ok().build();
    }
}
