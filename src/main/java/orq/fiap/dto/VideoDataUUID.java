package orq.fiap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VideoDataUUID {
    private String filename;
    private String uuid;
    private String mimeType;
    private String webhookUrl;

    public VideoDataUUID() {
    }
}
