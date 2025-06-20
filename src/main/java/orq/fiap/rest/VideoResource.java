package orq.fiap.rest;

import java.io.IOException;

import org.xml.sax.SAXException;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import orq.fiap.dto.VideoData;
import orq.fiap.services.EstadoService;
import orq.fiap.services.S3Service;

@Path("/video")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.TEXT_PLAIN)
public class VideoResource {

    @Inject
    S3Service s3Service;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response uploadVideos(VideoData videoData) throws IOException, SAXException, Exception {
        this.s3Service.uploadFile(videoData);

        return Response
                .ok()
                .build();
    }

    @Inject
    EstadoService estadoService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideos() {
        estadoService.setVideo();
        return Response
                .ok()
                .build();
    }
}
