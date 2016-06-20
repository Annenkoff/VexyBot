package su.vexy.vexybot.helper;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.objects.Message;
import su.vexy.vexybot.Bot;
import su.vexy.vexybot.Status;
import su.vexy.vexybot.manager.ChatsManager;
import su.vexy.vexybot.manager.MessageManager;
import su.vexy.vexybot.services.Keyboard;

import java.io.UnsupportedEncodingException;

import static su.vexy.vexybot.helper.LocationHelper.startSelectStandartLocation;
import static su.vexy.vexybot.manager.MessageManager.RBText;

public class LocaleHelper implements Helper {
    public static void beforeStartSelectLocale(Message message) throws UnsupportedEncodingException, TelegramApiException {
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                RBText(message, "select.language"),
                Keyboard.getLanguagesKeyboard()));
        ChatsManager.setStatus(message, Status.START_LANGUAGE_SELECTION.toString());
    }

    public static void beforeSelectLocale(Message message) throws TelegramApiException {
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                RBText(message, "select.language"),
                Keyboard.getLanguagesKeyboard()));
        ChatsManager.setStatus(message, Status.LANGUAGE_SELECTION.toString());
    }

    public static void onStartSelectLocale(Message message, boolean isStartOperation) throws UnsupportedEncodingException, TelegramApiException {
        if (message.getText().equalsIgnoreCase("Русский"))
            ChatsManager.setLocale(message, "ru");
        else if (message.getText().equalsIgnoreCase("English"))
            ChatsManager.setLocale(message, "en");
        ChatsManager.removeStatus(message);
        if (isStartOperation)
            startSelectStandartLocation(message);
        else
            Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                    RBText(message, "after.select.language"),
                    Keyboard.hideKeyboard()));
    }
}
