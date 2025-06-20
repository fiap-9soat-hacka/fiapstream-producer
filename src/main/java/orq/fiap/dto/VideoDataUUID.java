package orq.fiap.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoDataUUID extends VideoData {

    private String mimeType;
    private String uuid;

    public VideoDataUUID() {
    }

    public VideoDataUUID(VideoData videoData, String mimeType, String uuid) {
        this.video = videoData.video;
        this.filename = videoData.filename;
        this.webhookUrl = videoData.webhookUrl;
        this.mimeType = mimeType;
        this.uuid = uuid;
    }

}
