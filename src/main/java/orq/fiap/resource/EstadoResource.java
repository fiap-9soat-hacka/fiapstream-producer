package orq.fiap.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import orq.fiap.entity.HistoricoProcessamento;
import orq.fiap.services.HistoricoProcessamentoService;

import java.util.List;

@ApplicationScoped
@Path("/estado")
public class EstadoResource {
    @Inject
    HistoricoProcessamentoService historicoProcessamentoService;

    @Path("{uuid}")
    @GET
    public HistoricoProcessamento getEstadoAtual(@PathParam("uuid") String uuid){
        return this.historicoProcessamentoService.getEstadoAtual(uuid);
    }

    @Path("{uuid}/historico")
    @GET
    public List<HistoricoProcessamento> getHistorico(@PathParam("uuid") String uuid){
        return this.historicoProcessamentoService.getHistorico(uuid);
    }
}
