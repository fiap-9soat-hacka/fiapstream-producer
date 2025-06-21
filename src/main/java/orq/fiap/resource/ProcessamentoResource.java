package orq.fiap.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import orq.fiap.dto.VideoData;
import orq.fiap.services.ProcessamentoService;

@Path("/processamento")
public class ProcessamentoResource {
    @Inject
    ProcessamentoService processamentoService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response solicitarProcessamento(VideoData videoData) throws Exception {
        this.processamentoService.iniciarProcessamento(videoData);

        return Response
                .ok()
                .build();
    }
}
