package su.vexy.vexybot.manager;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import su.vexy.vexybot.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MessageManager implements Manager {
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
        properties.load(new InputStreamReader(new FileInputStream("src/run/resources/messages/messages.properties"), "UTF-8"));
        String text = message.getText().replace(" ", "_").toLowerCase();
        String answer = properties.getProperty(text);
        List<String> all = new ArrayList<>(Arrays.asList(answer.split("#")));
        Collections.shuffle(all);
        return all.get(0);
    }

    public static SendMessage getSendMessage(Message message, String text, ReplyKeyboard keyboard) {
        return new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(text)
                .enableHtml(true);
    }

    public static SendMessage getSendMessage(int chatId, String text, ReplyKeyboard keyboard) {
        return new SendMessage()
                .setChatId(String.valueOf(chatId))
                .setText(text)
                .enableHtml(true);
    }

    public static SendMessage getSendMessage(Message message, String text) {
        return new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(text)
                .enableHtml(true);
    }

    public static SendMessage getSendMessage(int chatId, String text) {
        return new SendMessage()
                .setChatId(String.valueOf(chatId))
                .setText(text)
                .enableHtml(true);
    }
}
