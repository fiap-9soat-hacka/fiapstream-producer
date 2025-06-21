package orq.fiap.dto;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Video is required.")
    protected FileUpload video;

//    @RestForm
//    @PartType(MediaType.TEXT_PLAIN)
//    @NotEmpty(message = "Filename is required")
//    private String filename;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    protected String webhookUrl;
}
