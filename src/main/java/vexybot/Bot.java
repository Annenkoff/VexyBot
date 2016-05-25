package vexybot;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardHide;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import vexybot.aggregator.model.GoogleStrategy;
import vexybot.dao.ChatsManager;
import vexybot.dao.NotesManager;
import vexybot.dao.NotificationsManager;
import vexybot.entity.Note;
import vexybot.services.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
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
            startSelectLocale(message);
        else if (text.equals("/cancel"))
            onCancel(message);
        else if (text.equals("/lang"))
            changeLocale(message);
        else if (text.equals("/help"))
            helpMessage(message);
        else if (status.equals(Status.START.toString()))
            start(message);
        else if (status.equals(Status.CHOOSE_NOTE.toString()))
            getNote(message);
        else if (status.equals(Status.CHOOSE_NOTE_FOR_DELETE.toString()))
            deleteNote(message);
        else if (status.equals(Status.LANGUAGE_SELECTION.toString()))
            onSelectLocale(message);
        else if (status.equals(Status.START_SELECT_STANDART_LOCATION.toString()))
            onSelectStandartLocation(message);
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
            doNotUnderstandMessage(message);
    }

    private void createNote(Message message) throws TelegramApiException, UnsupportedEncodingException {
        String note = message.getText();
        int chatId = Math.toIntExact(message.getChatId());
        if (ChatsManager.getLocale(message).equals("ru")) {
            if (note.toLowerCase().indexOf(MessageHelper.RBText(message, "to.create.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(16));
            else if (note.toLowerCase().indexOf(MessageHelper.RBText(message, "create.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(15));
            else if (note.toLowerCase().indexOf(MessageHelper.RBText(message, "add.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(15));
            else if (note.toLowerCase().indexOf(MessageHelper.RBText(message, "to.add.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(17));
        } else if (ChatsManager.getLocale(message).equals("en")) {
            if (note.toLowerCase().indexOf(MessageHelper.RBText(message, "to.create.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(16));
            else if (note.toLowerCase().indexOf(MessageHelper.RBText(message, "create.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(16));
            else if (note.toLowerCase().indexOf(MessageHelper.RBText(message, "add.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(13));
            else if (note.toLowerCase().indexOf(MessageHelper.RBText(message, "to.add.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(13));
        }
        sendMessage(new SendMessage()
                .setText(MessageHelper.RBText(message, "after.create.note"))
                .setChatId(String.valueOf(chatId)));
    }

    private List<Note> getAllNotes(Message message) throws TelegramApiException, UnsupportedEncodingException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String mess = "";
        mess += Emoji.PENCIL.toString() + MessageHelper.RBText(message, "your.notes") + "\n";
        int a = 1;
        for (Note s : notes) {
            if (s.getText().length() > 20) {
                mess += "#" + a + " - " + s.getText().substring(0, 20);
                mess += " ...";
            } else {
                mess += "#" + a + " - " + s.getText();
            }
            mess += "\n";
            a++;
        }
        if (notes.size() == 0 || notes.isEmpty())
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText(MessageHelper.RBText(message, "no.notes")));
        else {
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText(mess));
            if (message.getText().equalsIgnoreCase(MessageHelper.RBText(message, "to.read.the.note"))
                    || message.getText().equalsIgnoreCase(MessageHelper.RBText(message, "to.view.the.note"))
                    || message.getText().equalsIgnoreCase(MessageHelper.RBText(message, "to.view.all.notes"))
                    || message.getText().equalsIgnoreCase(MessageHelper.RBText(message, "all.notes"))) {
                sendMessage(new SendMessage()
                        .setChatId(String.valueOf(message.getChatId()))
                        .setText(MessageHelper.RBText(message, "info.about.get.note")));
                ChatsManager.setStatus(message, Status.CHOOSE_NOTE.toString());
            } else if (message.getText().equalsIgnoreCase(MessageHelper.RBText(message, "to.delete.the.note"))
                    || message.getText().equalsIgnoreCase(MessageHelper.RBText(message, "delete.the.note"))) {
                sendMessage(new SendMessage()
                        .setChatId(String.valueOf(message.getChatId()))
                        .setText(MessageHelper.RBText(message, "info.about.delete.note")));
                ChatsManager.setStatus(message, Status.CHOOSE_NOTE_FOR_DELETE.toString());
            }
        }
        return notes;
    }

    private void getNote(Message message) throws TelegramApiException, UnsupportedEncodingException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String text = message.getText();
        int number;
        try {
            number = Integer.parseInt(text) - 1;
        } catch (NumberFormatException e) {
            number = -1;
        }
        if (number == -1) {
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText(MessageHelper.RBText(message, "info.about.get.note")));
            return;
        }
        Note note = notes.get(number);
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(MessageHelper.RBText(message, "note") + " " + note.getText()));
        ChatsManager.removeStatus(message);
    }

    private void helpMessage(Message message) throws TelegramApiException, UnsupportedEncodingException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .enableHtml(true)
                .setText(MessageHelper.RBText(message, "help")));
    }

    private void onCancel(Message message) throws TelegramApiException, UnsupportedEncodingException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(MessageHelper.RBText(message, "well"))
                .setReplayMarkup(new ReplyKeyboardHide().setSelective(true).setHideKeyboard(true)));
        ChatsManager.removeStatus(message);
    }

    private void doNotUnderstandMessage(Message message) throws TelegramApiException, UnsupportedEncodingException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(Emoji.UNAMUSED_FACE.toString() + MessageHelper.RBText(message, "misunderstanding")));
    }

    private void deleteNote(Message message) throws TelegramApiException, UnsupportedEncodingException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String text = message.getText();
        int number;
        try {
            number = Integer.parseInt(text) - 1;
        } catch (NumberFormatException e) {
            number = -1;
        }
        if (number == -1) {
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText(MessageHelper.RBText(message, "info.about.get.note")));
            return;
        }
        Note note = notes.get(number);
        NotesManager.deleteNote(note.getId());
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(MessageHelper.RBText(message, "after.delete.note")));
        ChatsManager.removeStatus(message);
    }

    private void changeLocale(Message message) throws UnsupportedEncodingException, TelegramApiException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(MessageHelper.RBText(message, "select.language"))
                .setReplayMarkup(Keyboard.getLanguagesKeyboard()));
        ChatsManager.setStatus(message, Status.LANGUAGE_SELECTION.toString());
    }

    private void onSelectLocale(Message message) throws UnsupportedEncodingException, TelegramApiException {
        if (message.getText().equalsIgnoreCase("Русский"))
            ChatsManager.setLocale(message, "ru");
        else if (message.getText().equalsIgnoreCase("English"))
            ChatsManager.setLocale(message, "en");
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(MessageHelper.RBText(message, "after.select.language"))
                .setReplayMarkup(new ReplyKeyboardHide().setSelective(true).setHideKeyboard(true)));
        ChatsManager.removeStatus(message);
    }

    private void onSelectStandartLocation(Message message) throws IOException, TelegramApiException {
        if (message.hasText()) {
            String text = message.getText();
            if (text.equalsIgnoreCase(MessageHelper.RBText(message, "no.need")))
                onCancel(message);
            else
                ChatsManager.setLocation(message, text);
        } else if (message.hasLocation()) {
            ChatsManager.setLocation(message, Geocoder.getTextByCoordinates(message));
        } else {
            sendMessage(new SendMessage().setChatId(String.valueOf(message
                    .getChatId()))
                    .setText(MessageHelper.RBText(message, "select.standart.location")));
            return;
        }
        ChatsManager.removeStatus(message);
        sendMessage(new SendMessage().setChatId(String.valueOf(message
                .getChatId()))
                .setText(MessageHelper.RBText(message, "complete.the.setting")));
    }

    private void startSelectLocale(Message message) throws UnsupportedEncodingException, TelegramApiException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(MessageHelper.RBText(message, "select.language"))
                .setReplayMarkup(Keyboard.getLanguagesKeyboard()));
        ChatsManager.setStatus(message, Status.START.toString());
    }

    private void start(Message message) throws TelegramApiException, UnsupportedEncodingException {
        onSelectLocale(message);
        sendMessage(new SendMessage().setChatId(String.valueOf(message
                .getChatId()))
                .setText(MessageHelper.RBText(message, "start")));
        ChatsManager.setStatus(message, Status.START_SELECT_STANDART_LOCATION.toString());
        sendMessage(new SendMessage().setChatId(String.valueOf(message
                .getChatId()))
                .setText(MessageHelper.RBText(message, "select.standart.location")));
    }

    private void searchGoogle(Message message) throws TelegramApiException, IOException {
        String s = new GoogleStrategy().getInfo(message.getText());
        sendMessage(new SendMessage().setChatId(String.valueOf(message
                .getChatId()))
                .setText(s));
    }

    private void addNotification(Message message) {
        Date date = NotificationsManager.getDate(message);
        String text = NotificationsManager.getString(message);
        int chatId = Math.toIntExact(message.getChatId());
        NotificationsManager.addNote(chatId, text, date);
    }

    private void getWeather(Message message) throws IOException, JAXBException, TelegramApiException {
        String geo = "";
        String text = "";
        if (message.hasLocation()) {
            geo = Geocoder.getTextByCoordinates(message);
        } else if (message.hasText()) {
            geo = message.getText();
        }
        text = WeatherService.getText(geo);
        int temp = WeatherService.getTemp(geo);
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(text + "\nТемпература: " + temp + "°C"));
        ChatsManager.removeStatus(message);
    }

    private void selectLocation(Message message) throws TelegramApiException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(MessageHelper.RBText(message, "give.me.the.location")));
        ChatsManager.setStatus(message, Status.GET_WEATHER.toString());
    }
}
