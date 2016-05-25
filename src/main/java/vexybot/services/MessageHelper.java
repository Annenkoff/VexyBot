package vexybot.services;

import org.telegram.telegrambots.api.objects.Message;
import vexybot.Config;
import vexybot.dao.ChatsManager;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

public class MessageHelper {
    public static String RBText(Message message, String name) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(Config.RESOURCE_PATH + ChatsManager.getLocale(message));
        try {
            return new String(resourceBundle.getString(name).getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
}
