package su.vexy.vexybot.bots.send;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("recipient_id")
    String recipientId;
    @SerializedName("message_id")
    String messageId;
}
