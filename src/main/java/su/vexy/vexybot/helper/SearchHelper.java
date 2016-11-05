package su.vexy.vexybot.helper;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import su.vexy.vexybot.TelegramBot;
import su.vexy.vexybot.aggregator.GoogleStrategy;
import su.vexy.vexybot.manager.MessageManager;

import java.io.IOException;

public class SearchHelper implements Helper {
    public static void searchGoogle(Message message) throws IOException, TelegramApiException {
        String info = new GoogleStrategy().getInfo(message.getText());
        TelegramBot.bot.sendMessage(MessageManager.getSendMessage(message, info));
    }
}
