package su.vexy.vexybot.helper;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.objects.Message;
import su.vexy.vexybot.Bot;
import su.vexy.vexybot.manager.ChatsManager;
import su.vexy.vexybot.manager.MessageManager;
import su.vexy.vexybot.services.Emoji;
import su.vexy.vexybot.services.Keyboard;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static su.vexy.vexybot.manager.MessageManager.RBText;
import static su.vexy.vexybot.manager.MessageManager.getSendMessage;

public class MessageHelper implements Helper {
    public static void start(Message message) throws TelegramApiException, UnsupportedEncodingException {
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                RBText(message, "start")));
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                RBText(message, "start.vote") + Emoji.SMILING_FACE_WITH_OPEN_MOUTH));
    }

    public static void helpMessage(Message message) throws TelegramApiException, UnsupportedEncodingException {
        Bot.bot.sendMessage(getSendMessage(message,
                RBText(message, "help")));
    }

    public static void onCancel(Message message) throws TelegramApiException, UnsupportedEncodingException {
        Bot.bot.sendMessage(getSendMessage(message,
                RBText(message, "well"),
                Keyboard.hideKeyboard()));
        ChatsManager.removeStatus(message);
    }

    public static void doNotUnderstandMessage(Message message) throws TelegramApiException, UnsupportedEncodingException {
        Bot.bot.sendMessage(getSendMessage(message,
                Emoji.UNAMUSED_FACE.toString() + RBText(message, "misunderstanding")));
    }

    public static void anotherAnswer(Message message) throws IOException, TelegramApiException {
        String answer = MessageManager.getAnswer(message);
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                answer));
    }
}
