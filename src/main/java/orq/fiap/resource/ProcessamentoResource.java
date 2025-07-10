package orq.fiap.resource;

import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestPath;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import orq.fiap.dto.VideoData;
import orq.fiap.repository.ProcessamentoRepository;
import orq.fiap.services.ProcessamentoService;

@Path("/processamento")
public class ProcessamentoResource {

    @Inject
    ProcessamentoService processamentoService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    @RolesAllowed({"user"})
    public Response solicitarProcessamento(VideoData videoData) throws Exception {
        String processamentoUUID = this.processamentoService.iniciarProcessamento(videoData);

        return Response
            .ok()
            .entity(processamentoUUID)
            .build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/url-video/{uuid}")
    @RolesAllowed({"user"})
    public Response getPresignedUrl(@PathParam("uuid") String uuid) {
        return Response
            .ok()
            .entity(processamentoService.getPresignedUrl(uuid))
            .build();
    }
}
