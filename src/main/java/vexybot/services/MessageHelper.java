package vexybot.services;

import org.telegram.telegrambots.api.objects.Message;
import vexybot.Config;
import vexybot.dao.ChatsManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MessageHelper {
    public static String RBText(Message message, String name) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(Config.RESOURCE_PATH + ChatsManager.getLocale(message));
        try {
            return new String(resourceBundle.getString(name).getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public static String getAnswer(Message message) throws IOException {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(new FileInputStream("src/main/resources/messages.properties"), "UTF-8"));
        String text = message.getText().replace(" ", "_").toLowerCase();
        String answer = properties.getProperty(text);
        List<String> all = new ArrayList<>(Arrays.asList(answer.split("#")));
        Collections.shuffle(all);
        return all.get(0);
    }
}
