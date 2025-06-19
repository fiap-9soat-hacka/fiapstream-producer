package orq.fiap.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoDataUUID extends VideoData {

    public String mimeType;
    public String uuid;

    public VideoDataUUID() {
    }

    public VideoDataUUID(VideoData videoData, String mimeType, String uuid) {
        this.video = videoData.video;
        this.filename = videoData.filename;
        this.mimeType = mimeType;
        this.uuid = uuid;
    }

}
