package su.vexy.vexybot.bots.send;

import com.google.gson.annotations.SerializedName;
import su.vexy.vexybot.bots.common.Action;

public class Button {
    private final Type type;
    private final String title;
    private final Action payload;
    private final String url;

    private Button(Type type, String title, String url, Action action) {
        this.type = type;
        this.title = title;
        this.url = url;
        this.payload = action;
    }

    public static Button Url(String title, String url) {
        return new Button(Type.WebUrl, title, url, null);
    }

    public static Button Postback(String title, Action action) {
        return new Button(Type.Postback, title, null, action);
    }

    private enum Type {
        @SerializedName("postback")
        Postback,
        @SerializedName("web_url")
        WebUrl
    }
}
