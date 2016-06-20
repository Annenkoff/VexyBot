package vexybot.helper;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.objects.Message;
import vexybot.Bot;
import vexybot.Status;
import vexybot.manager.ChatsManager;
import vexybot.manager.MessageManager;

import java.io.UnsupportedEncodingException;

import static vexybot.helper.LocationHelper.startSelectStandartLocation;
import static vexybot.manager.MessageManager.RBText;
import static vexybot.services.Keyboard.getLanguagesKeyboard;
import static vexybot.services.Keyboard.hideKeyboard;

public class LocaleHelper implements Helper {
    public static void beforeStartSelectLocale(Message message) throws UnsupportedEncodingException, TelegramApiException {
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                RBText(message, "select.language"),
                getLanguagesKeyboard()));
        ChatsManager.setStatus(message, Status.START_LANGUAGE_SELECTION.toString());
    }

    public static void beforeSelectLocale(Message message) throws TelegramApiException {
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                RBText(message, "select.language"),
                getLanguagesKeyboard()));
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
                    hideKeyboard()));
    }
}
