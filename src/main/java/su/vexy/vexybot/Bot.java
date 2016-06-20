package su.vexy.vexybot;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import su.vexy.vexybot.manager.ChatsManager;
import su.vexy.vexybot.services.Signs;

import java.io.UnsupportedEncodingException;

import static su.vexy.vexybot.helper.SearchHelper.searchGoogle;
import static su.vexy.vexybot.helper.WeatherHelper.getWeather;

public class Bot extends TelegramLongPollingBot {
    public static Bot bot = new Bot();

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(message.getText());
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.enableMarkdown(true);
            try {
                handleIncomingMessage(message);
            } catch (Exception e) {
                try {
                    doNotUnderstandMessage(message);
                } catch (TelegramApiException e1) {
                } catch (UnsupportedEncodingException e1) {
                }
            }
        }
    }

    public String getBotUsername() {
        return Config.USERNAME;
    }

    public String getBotToken() {
        return Config.TOKEN;
    }

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