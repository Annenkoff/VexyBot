package su.vexy.vexybot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import su.vexy.vexybot.services.HibernateSessionFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HibernateSessionFactory.getSessionFactory().openSession();
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        TelegramBotsApi telegramBotsApi = (TelegramBotsApi) context.getBean("telegramBotsApi"); // Create the telegram bot object.
        try {
            telegramBotsApi.registerBot((TelegramLongPollingBot) context.getBean("botTelegram"));
        } catch (TelegramApiException e) {
        }
    }
}
