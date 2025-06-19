package orq.fiap.rest.in;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataOutput;
import org.xml.sax.SAXException;

import io.quarkus.logging.Log;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import orq.fiap.dto.VideoData;
import orq.fiap.dto.VideoDataUUID;
import orq.fiap.rest.out.CommonResource;
import orq.fiap.services.S3Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Path("/video")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.TEXT_PLAIN)
public class VideoResource {

    @Inject
    S3Service s3Service;

    @Inject
    S3Client s3Client;

    @Inject
    CommonResource commonResource;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response uploadVideos(VideoData videoData) throws IOException, SAXException, Exception {
        this.s3Service.uploadFile(videoData);

        return Response
                .ok()
                .build();
    }
}
