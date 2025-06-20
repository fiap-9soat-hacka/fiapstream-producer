package orq.fiap.resource;

import java.io.IOException;

import org.xml.sax.SAXException;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import orq.fiap.dto.VideoData;
import orq.fiap.services.ProcessamentoService;
import software.amazon.awssdk.services.s3.S3Client;

@Path("/video")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.TEXT_PLAIN)
public class VideoResource {

    @Inject
    ProcessamentoService processamentoService;

    @Inject
    S3Client s3Client;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response uploadVideos(VideoData videoData) throws IOException, SAXException, Exception {
        this.processamentoService.iniciarProcessamento(videoData);

        return Response
                .ok()
                .build();
    }
}
