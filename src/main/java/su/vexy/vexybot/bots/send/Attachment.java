package su.vexy.vexybot.bots.send;

import com.google.gson.annotations.SerializedName;

public class Attachment {

    final private Type type;
    final private Payload payload;

    private Attachment(Type type, Payload payload) {
        this.type = type;
        this.payload = payload;
    }

    public static Attachment Template(Payload payload) {
        return new Attachment(Type.TEMPLATE, payload);
    }

    public static Attachment Image(String url) {
        return new Attachment(Type.IMAGE, Payload.Image(url));
    }

    public boolean addElement(Element element) {
        return payload.addElement(element);
    }

    public boolean addButton(Button button) {
        return payload.addButton(button);
    }

    public enum Type {
        @SerializedName("template")
        TEMPLATE,
        @SerializedName("image")
        IMAGE
    }
}
