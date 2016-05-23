package vexybot;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardHide;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import vexybot.aggregator.model.GoogleStrategy;
import vexybot.aggregator.model.WikipediaStrategy;
import vexybot.dao.ChatsManager;
import vexybot.dao.NotesManager;
import vexybot.dao.NotificationsManager;
import vexybot.entity.Note;
import vexybot.services.Emoji;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Bot extends TelegramLongPollingBot {
    private String fileLocale;
    private ResourceBundle resourceBundle;

    private static ReplyKeyboardMarkup getLanguagesKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        List<String> languages = new ArrayList<>();
        languages.add("Русский");
        languages.add("English");
        for (String language : languages) {
            KeyboardRow row = new KeyboardRow();
            row.add(language);
            keyboard.add(row);
        }

        KeyboardRow row = new KeyboardRow();
        row.add("/cancel");
        keyboard.add(row);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
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
    }

    public String getBotUsername() {
        return Config.USERNAME;
    }

    public String getBotToken() {
        return Config.TOKEN;
    }

    private void handleIncomingMessage(Message message) throws Exception {
        ChatsManager.checkChat(message);
        fileLocale = ChatsManager.getLocale(message);
        resourceBundle = ResourceBundle.getBundle(Config.RESOURCE_PATH + fileLocale);
        String text = message.getText().toLowerCase();
        String status = ChatsManager.getStatus(message);
        if (text.equals("/start"))
            startSelectLocale(message);
        else if (text.equals("/cancel"))
            onCancel(message);
        else if (text.equals("/lang"))
            changeLocale(message);
        else if (status.equals(Status.START.toString()))
            start(message);
        else if (status.equals(Status.CHOOSENOTE.toString()))
            getNote(message);
        else if (status.equals(Status.CHOOSENOTEFORDEL.toString()))
            deleteNote(message);
        else if (status.equals(Status.LANGUAGESELECTION.toString()))
            onSelectLocale(message);
        else if (text.equals("/help"))
            helpMessage(message);
        else if (text.contains(new String(resourceBundle.getString("create.the.note").getBytes("ISO-8859-1"), "UTF-8") + " ")
                || text.contains(new String(resourceBundle.getString("to.create.the.note").getBytes("ISO-8859-1"), "UTF-8") + " ")
                || text.contains(new String(resourceBundle.getString("add.the.note").getBytes("ISO-8859-1"), "UTF-8") + " ")
                || text.contains(new String(resourceBundle.getString("to.add.the.note").getBytes("ISO-8859-1"), "UTF-8") + " "))
            createNote(message);
        else if (text.contains(new String(resourceBundle.getString("to.read.the.note").getBytes("ISO-8859-1"), "UTF-8"))
                || text.contains(new String(resourceBundle.getString("to.view.the.note").getBytes("ISO-8859-1"), "UTF-8"))
                || text.contains(new String(resourceBundle.getString("to.delete.the.note").getBytes("ISO-8859-1"), "UTF-8"))
                || text.contains(new String(resourceBundle.getString("delete.the.note").getBytes("ISO-8859-1"), "UTF-8"))
                || text.contains(new String(resourceBundle.getString("to.view.all.notes").getBytes("ISO-8859-1"), "UTF-8"))
                || text.contains(new String(resourceBundle.getString("all.notes").getBytes("ISO-8859-1"), "UTF-8")))
            getAllNotes(message);
        else if (text.substring(0, 11).toLowerCase().contains(new String(resourceBundle.getString("who.is.he").getBytes("ISO-8859-1"), "UTF-8") + " ")
                || text.substring(0, 11).toLowerCase().contains(new String(resourceBundle.getString("who.is.she").getBytes("ISO-8859-1"), "UTF-8") + " ")
                || text.substring(0, 11).toLowerCase().contains(new String(resourceBundle.getString("who.are").getBytes("ISO-8859-1"), "UTF-8") + " ")
                || text.substring(0, 11).toLowerCase().contains(new String(resourceBundle.getString("what.is").getBytes("ISO-8859-1"), "UTF-8") + " "))
            searchGoogle(message);
        else if (text.substring(0, 9).toLowerCase().contains("напомни "))
            addNotification(message);
        else
            doNotUnderstandMessage(message);
    }

    private void createNote(Message message) throws TelegramApiException, UnsupportedEncodingException {
        String note = message.getText();
        int chatId = Math.toIntExact(message.getChatId());
        if (ChatsManager.getLocale(message).equals("ru")) {
            if (note.toLowerCase().indexOf(new String(resourceBundle.getString("to.create.the.note").getBytes("ISO-8859-1"), "UTF-8")) == 0)
                NotesManager.addNote(chatId, note.substring(16));
            else if (note.toLowerCase().indexOf(new String(resourceBundle.getString("create.the.note").getBytes("ISO-8859-1"), "UTF-8")) == 0)
                NotesManager.addNote(chatId, note.substring(15));
            else if (note.toLowerCase().indexOf(new String(resourceBundle.getString("add.the.note").getBytes("ISO-8859-1"), "UTF-8")) == 0)
                NotesManager.addNote(chatId, note.substring(15));
            else if (note.toLowerCase().indexOf(new String(resourceBundle.getString("to.add.the.note").getBytes("ISO-8859-1"), "UTF-8")) == 0)
                NotesManager.addNote(chatId, note.substring(17));
        } else if (ChatsManager.getLocale(message).equals("en")) {
            if (note.toLowerCase().indexOf(new String(resourceBundle.getString("to.create.the.note").getBytes("ISO-8859-1"), "UTF-8")) == 0)
                NotesManager.addNote(chatId, note.substring(16));
            else if (note.toLowerCase().indexOf(new String(resourceBundle.getString("create.the.note").getBytes("ISO-8859-1"), "UTF-8")) == 0)
                NotesManager.addNote(chatId, note.substring(16));
            else if (note.toLowerCase().indexOf(new String(resourceBundle.getString("add.the.note").getBytes("ISO-8859-1"), "UTF-8")) == 0)
                NotesManager.addNote(chatId, note.substring(13));
            else if (note.toLowerCase().indexOf(new String(resourceBundle.getString("to.add.the.note").getBytes("ISO-8859-1"), "UTF-8")) == 0)
                NotesManager.addNote(chatId, note.substring(13));
        }
        sendMessage(new SendMessage()
                .setText(new String(resourceBundle.getString("after.create.note").getBytes("ISO-8859-1"), "UTF-8"))
                .setChatId(String.valueOf(chatId)));
    }

    private List<Note> getAllNotes(Message message) throws TelegramApiException, UnsupportedEncodingException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String mess = "";
        mess += Emoji.PENCIL.toString() + new String(resourceBundle.getString("your.notes").getBytes("ISO-8859-1"), "UTF-8") + "\n";
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
                    .setText(new String(resourceBundle.getString("no.notes").getBytes("ISO-8859-1"), "UTF-8")));
        else {
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText(mess));
            if (message.getText().equalsIgnoreCase(new String(resourceBundle.getString("to.read.the.note").getBytes("ISO-8859-1"), "UTF-8"))
                    || message.getText().equalsIgnoreCase(new String(resourceBundle.getString("to.view.the.note").getBytes("ISO-8859-1"), "UTF-8"))
                    || message.getText().equalsIgnoreCase(new String(resourceBundle.getString("to.view.all.notes").getBytes("ISO-8859-1"), "UTF-8"))
                    || message.getText().equalsIgnoreCase(new String(resourceBundle.getString("all.notes").getBytes("ISO-8859-1"), "UTF-8"))) {
                sendMessage(new SendMessage()
                        .setChatId(String.valueOf(message.getChatId()))
                        .setText(new String(resourceBundle.getString("info.about.get.note").getBytes("ISO-8859-1"), "UTF-8")));
                ChatsManager.setStatus(message, Status.CHOOSENOTE.toString());
            } else if (message.getText().equalsIgnoreCase(new String(resourceBundle.getString("to.delete.the.note").getBytes("ISO-8859-1"), "UTF-8"))
                    || message.getText().equalsIgnoreCase(new String(resourceBundle.getString("delete.the.note").getBytes("ISO-8859-1"), "UTF-8"))) {
                sendMessage(new SendMessage()
                        .setChatId(String.valueOf(message.getChatId()))
                        .setText(new String(resourceBundle.getString("info.about.delete.note").getBytes("ISO-8859-1"), "UTF-8")));
                ChatsManager.setStatus(message, Status.CHOOSENOTEFORDEL.toString());
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
            try {
                number = Integer.parseInt(text.substring(1)) - 1;
            } catch (NumberFormatException e1) {
                number = -1;
            }
        }
        if (number == -1) {
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText(new String(resourceBundle.getString("info.about.get.note").getBytes("ISO-8859-1"), "UTF-8")));
            return;
        }
        Note note = notes.get(number);
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(new String(resourceBundle.getString("note").getBytes("ISO-8859-1"), "UTF-8") + note.getText()));
        ChatsManager.setStatus(message, "");
    }

    private void helpMessage(Message message) throws TelegramApiException, UnsupportedEncodingException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(new String(resourceBundle.getString("help").getBytes("ISO-8859-1"), "UTF-8")));
    }

    private void onCancel(Message message) throws TelegramApiException, UnsupportedEncodingException {
        ChatsManager.setStatus(message, "");
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(new String(resourceBundle.getString("well").getBytes("ISO-8859-1"), "UTF-8"))
                .setReplayMarkup(new ReplyKeyboardHide().setSelective(true).setHideKeyboard(true)));
    }

    private void doNotUnderstandMessage(Message message) throws TelegramApiException, UnsupportedEncodingException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(Emoji.UNAMUSED_FACE.toString() + new String(resourceBundle.getString("misunderstanding").getBytes("ISO-8859-1"), "UTF-8")));
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
                    .setText(new String(resourceBundle.getString("info.about.get.note").getBytes("ISO-8859-1"), "UTF-8")));
            return;
        }
        Note note = notes.get(number);
        NotesManager.deleteNote(note.getId());
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(new String(resourceBundle.getString("after.delete.note").getBytes("ISO-8859-1"), "UTF-8")));
        ChatsManager.setStatus(message, "");
    }

    private void changeLocale(Message message) throws UnsupportedEncodingException, TelegramApiException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(new String(resourceBundle.getString("select.language").getBytes("ISO-8859-1"), "UTF-8"))
                .setReplayMarkup(getLanguagesKeyboard()));
        ChatsManager.setStatus(message, Status.LANGUAGESELECTION.toString());
    }

    private void onSelectLocale(Message message) throws UnsupportedEncodingException, TelegramApiException {
        if (message.getText().equalsIgnoreCase("Русский")) {
            ChatsManager.setLocale(message, "ru");
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText("Хорошо, буду отвечать на русском.")
                    .setReplayMarkup(new ReplyKeyboardHide().setSelective(true).setHideKeyboard(true)));
        } else if (message.getText().equalsIgnoreCase("English")) {
            ChatsManager.setLocale(message, "en");
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText("Well. I speak English.")
                    .setReplayMarkup(new ReplyKeyboardHide().setSelective(true).setHideKeyboard(true)));
        }
        ChatsManager.setStatus(message, "");
    }

    private void startSelectLocale(Message message) throws UnsupportedEncodingException, TelegramApiException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(new String(resourceBundle.getString("select.language").getBytes("ISO-8859-1"), "UTF-8"))
                .setReplayMarkup(getLanguagesKeyboard()));
        ChatsManager.setStatus(message, Status.START.toString());
    }

    private void start(Message message) throws TelegramApiException, UnsupportedEncodingException {
        onSelectLocale(message);
        fileLocale = ChatsManager.getLocale(message);
        resourceBundle = ResourceBundle.getBundle(Config.RESOURCE_PATH + fileLocale);
        sendMessage(new SendMessage().setChatId(String.valueOf(message
                .getChatId()))
                .setText(new String(resourceBundle.getString("start").getBytes("ISO-8859-1"), "UTF-8")));
        ChatsManager.setStatus(message, "");
    }

    private void searchWikipedia(Message message) {
        String s = new WikipediaStrategy().getInfo(message.getText().substring(10));
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
}
