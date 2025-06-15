package orq.fiap.dto;

import java.io.File;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

public class VideoData {

    @RestForm("video")
    public File video;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String filename;
}
