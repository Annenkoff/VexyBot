package su.vexy.vexybot.bots;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import su.vexy.vexybot.Config;
import su.vexy.vexybot.Status;
import su.vexy.vexybot.helper.MessageHelper;
import su.vexy.vexybot.manager.ChatsManager;
import su.vexy.vexybot.services.Signs;

import java.io.UnsupportedEncodingException;

import static su.vexy.vexybot.helper.LocaleHelper.*;
import static su.vexy.vexybot.helper.LocationHelper.*;
import static su.vexy.vexybot.helper.MessageHelper.*;
import static su.vexy.vexybot.helper.NoteHelper.*;
import static su.vexy.vexybot.helper.SearchHelper.searchGoogle;
import static su.vexy.vexybot.helper.WeatherHelper.getWeather;

public class TelegramBot extends TelegramLongPollingBot {
    public static TelegramBot bot = new TelegramBot();

    // Проверка на обновления.
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            new Thread(() -> {
                Message message = update.getMessage();
                try {
                    handleIncomingMessage(message);
                } catch (Exception e) {
                    try {
                        doNotUnderstandMessage(message);
                    } catch (UnsupportedEncodingException e1) {
                    } catch (TelegramApiException e1) {
                    }
                }
            }).start();
        }
    }

    public String getBotUsername() {
        return Config.USERNAME;
    }

    public String getBotToken() {
        return Config.TOKEN;
    }

    // Обработка входящего сообщения.
    private void handleIncomingMessage(Message message) throws Exception {
        ChatsManager.checkChat(message);
        String text = "";
        try {
            text = message.getText().toLowerCase();
        } catch (Exception e) {
        }
        String status = ChatsManager.getStatus(message);
        if (text.equals("/start"))
            beforeStartSelectLocale(message);
        else if (text.equals("/cancel"))
            onCancel(message);
        else if (text.equals("/lang"))
            beforeSelectLocale(message);
        else if (text.equals("/help"))
            helpMessage(message);
        else if (text.contains("/sendkey805458"))
            MessageHelper.newsletter(message.getText().replace("/sendkey805458 ", ""));
        else if (status.equals(Status.START.toString()))
            start(message);
        else if (status.equals(Status.CHOOSE_NOTE.toString()))
            getNote(message);
        else if (status.equals(Status.CHOOSE_NOTE_FOR_DELETE.toString()))
            deleteNote(message);
        else if (status.equals(Status.LANGUAGE_SELECTION.toString()))
            onStartSelectLocale(message, false);
        else if (status.equals(Status.START_LANGUAGE_SELECTION.toString()))
            onStartSelectLocale(message, true);
        else if (status.equals(Status.ON_SELECT_STANDART_LOCATION.toString()))
            onSelectStandartLocation(message);
        else if (status.equals(Status.START_SELECT_STANDART_LOCATION.toString()))
            startSelectStandartLocation(message);
        else if (status.equals(Status.GET_WEATHER.toString()))
            getWeather(message);
        else if (Signs.isCreateNoteSigns(message))
            createNote(message);
        else if (Signs.isGetAllNotesSigns(message))
            getAllNotes(message);
        else if (Signs.isGoogleSigns(message))
            searchGoogle(message);
        else if (Signs.isGetWeather(message))
            selectLocation(message);
        else
            anotherAnswer(message);
    }
}
