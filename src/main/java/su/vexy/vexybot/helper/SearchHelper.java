package su.vexy.vexybot.helper;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.objects.Message;
import su.vexy.vexybot.Bot;
import su.vexy.vexybot.aggregator.GoogleStrategy;
import su.vexy.vexybot.manager.MessageManager;

import java.io.IOException;

public class SearchHelper implements Helper {
    public static void searchGoogle(Message message) throws TelegramApiException, IOException {
        String info = new GoogleStrategy().getInfo(message.getText());
        Bot.bot.sendMessage(MessageManager.getSendMessage(message, info));
    }
}
