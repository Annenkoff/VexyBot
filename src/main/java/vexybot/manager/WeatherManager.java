package vexybot.manager;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import vexybot.services.Emoji;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class WeatherManager implements Manager {
    public static int getTemp(String string) throws JAXBException, IOException {
        YahooWeatherService yahooWeatherService = new YahooWeatherService();
        Channel channel = yahooWeatherService.getForecastForLocation(string, DegreeUnit.CELSIUS).all().get(0);
        return channel.getItem().getCondition().getTemp();
    }

    public static String getText(String string) throws JAXBException, IOException {
        YahooWeatherService yahooWeatherService = new YahooWeatherService();
        Channel channel = yahooWeatherService.getForecastForLocation(string, DegreeUnit.CELSIUS).all().get(0);
        String entext = channel.getItem().getCondition().getText();
        if (entext.equalsIgnoreCase("Sunny"))
            return Emoji.SUN_WITH_FACE + "Солнечно";
        else if (entext.equalsIgnoreCase("Mostly Sunny"))
            return Emoji.SUN_BEHIND_CLOUD + "Преимущественно солнечно";
        else if (entext.equalsIgnoreCase("Cloudy"))
            return Emoji.CLOUD + "Облачно";
        else if (entext.equalsIgnoreCase("Partly Cloudy"))
            return Emoji.SUN_BEHIND_CLOUD + "Частично облачно";
        else if (entext.equalsIgnoreCase("Mostly Cloudy"))
            return Emoji.CLOUD + "Преимущественно облачно";
        else if (entext.equalsIgnoreCase("Rain"))
            return Emoji.UMBRELLA_WITH_RAIN_DROPS + "Дожди";
        else if (entext.equalsIgnoreCase("Mostly Clear"))
            return Emoji.SUN_BEHIND_CLOUD + "Облачно с прояснениями";
        else if (entext.equalsIgnoreCase("Clear"))
            return Emoji.SUN_WITH_FACE + "Ясно";
        return "";
    }
}
