package su.vexy.vexybot;

import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import su.vexy.vexybot.bots.TelegramBot;
import su.vexy.vexybot.services.HibernateSessionFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HibernateSessionFactory.getSessionFactory().openSession();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(); // Create the telegram bot object.
        try {
            telegramBotsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
        }
    }
}
