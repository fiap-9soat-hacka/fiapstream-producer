package orq.fiap.dto;

import java.io.File;

import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;
import lombok.Setter;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Getter
@Setter
public class VideoData {
    @RestForm("video")
    public File video;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String filename;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String webhookUrl;
}
