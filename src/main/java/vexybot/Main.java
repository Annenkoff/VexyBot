package vexybot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        TelegramBotsApi telegramBotsApi = (TelegramBotsApi) context.getBean("telegramBotsApi");
        try {
            telegramBotsApi.registerBot((TelegramLongPollingBot) context.getBean("botTelegram"));
        } catch (TelegramApiException e) {
        }
    }
}
