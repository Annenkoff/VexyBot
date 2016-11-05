package su.vexy.vexybot.bots.webhook;

import com.google.gson.annotations.SerializedName;

public class Attachment {
    public Type type;
    public Payload payload;

    public enum Type {
        @SerializedName("audio")
        AUDIO,
        @SerializedName("image")
        IMAGE,
        @SerializedName("video")
        VIDEO
    }
}
