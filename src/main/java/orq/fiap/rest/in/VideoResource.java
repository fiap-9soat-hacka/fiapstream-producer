package orq.fiap.rest.in;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.xml.sax.SAXException;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import orq.fiap.dto.FormData;
import orq.fiap.services.S3Service;

@Path("/video")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.TEXT_PLAIN)
public class VideoResource {

    @Inject
    S3Service s3Service;

    public static class VideoUploadForm {
        @RestForm("files")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public List<File> files;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response uploadVideos(FormData form) throws IOException, SAXException, Exception {
        this.s3Service.uploadFile(form);

        return Response
                .ok()
                .build();
    }
}
