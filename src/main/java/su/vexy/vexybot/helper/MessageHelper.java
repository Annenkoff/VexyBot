package su.vexy.vexybot.helper;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import su.vexy.vexybot.bots.TelegramBot;
import su.vexy.vexybot.dao.Chat;
import su.vexy.vexybot.manager.ChatsManager;
import su.vexy.vexybot.manager.MessageManager;
import su.vexy.vexybot.services.Emoji;
import su.vexy.vexybot.services.Keyboard;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static su.vexy.vexybot.manager.MessageManager.RBText;
import static su.vexy.vexybot.manager.MessageManager.getSendMessage;

public class MessageHelper implements Helper {
    public static void start(Message message) throws TelegramApiException, UnsupportedEncodingException {
        TelegramBot.bot.sendMessage(MessageManager.getSendMessage(message,
                RBText(message, "start")));
        TelegramBot.bot.sendMessage(MessageManager.getSendMessage(message,
                RBText(message, "start.vote") + Emoji.SMILING_FACE_WITH_OPEN_MOUTH));
    }

    public static void helpMessage(Message message) throws TelegramApiException, UnsupportedEncodingException {
        TelegramBot.bot.sendMessage(getSendMessage(message,
                RBText(message, "help")));
    }

    public static void onCancel(Message message) throws TelegramApiException, UnsupportedEncodingException {
        TelegramBot.bot.sendMessage(getSendMessage(message,
                RBText(message, "well"),
                Keyboard.hideKeyboard()));
        ChatsManager.removeStatus(message);
    }

    public static void doNotUnderstandMessage(Message message) throws TelegramApiException, UnsupportedEncodingException {
        TelegramBot.bot.sendMessage(getSendMessage(message,
                Emoji.UNAMUSED_FACE.toString() + RBText(message, "misunderstanding")));
    }

    public static void anotherAnswer(Message message) throws IOException, TelegramApiException {
        String answer = MessageManager.getAnswer(message);
        TelegramBot.bot.sendMessage(MessageManager.getSendMessage(message,
                answer));
    }

    public static void sendMessage(Message message, String text) throws TelegramApiException {
        TelegramBot.bot.sendMessage(MessageManager.getSendMessage(message, text));
    }

    public static void newsletter(String text) throws TelegramApiException {
        List<Chat> chats = ChatsManager.getAllChats();
        for (Chat chat : chats) {
            TelegramBot.bot.sendMessage(MessageManager.getSendMessage(chat.getId(), text));
        }
    }
}
