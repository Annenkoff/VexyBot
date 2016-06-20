package vexybot.helper;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.objects.Message;
import vexybot.Bot;
import vexybot.manager.ChatsManager;
import vexybot.manager.MessageManager;
import vexybot.manager.WeatherManager;
import vexybot.services.Geocoder;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class WeatherHelper implements Helper {
    public static void getWeather(Message message) throws IOException, JAXBException, TelegramApiException {
        String geo = "";
        String text;
        if (message.hasLocation()) {
            geo = Geocoder.getTextByCoordinates(message);
        } else if (message.hasText()) {
            geo = message.getText();
        }
        text = WeatherManager.getText(geo);
        int temp = WeatherManager.getTemp(geo);
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                text + "\nТемпература: " + temp + "°C"));
        ChatsManager.removeStatus(message);
    }
}
