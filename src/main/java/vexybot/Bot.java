package vexybot;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardHide;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import vexybot.dao.ChatsManager;
import vexybot.dao.NotesManager;
import vexybot.entity.Note;

import java.io.UnsupportedEncodingException;
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
                } catch (TelegramApiException e) {
                } catch (UnsupportedEncodingException e) {
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

    private void handleIncomingMessage(Message message) throws TelegramApiException, UnsupportedEncodingException {
        ChatsManager.checkChat(message);
        fileLocale = ChatsManager.getLocale(message);
        resourceBundle = ResourceBundle.getBundle(Config.RESOURCE_PATH + fileLocale);
        String text = message.getText().toLowerCase();
        String status = ChatsManager.getStatus(message);
        if (text.equals("/cancel"))
            onCancel(message);
        else if (text.equals("/lang"))
            changeLocale(message);
        else if (status.equals("CHOOSENOTE"))
            getNote(message);
        else if (status.equals("CHOOSENOTEFORDEL"))
            deleteNote(message);
        else if (status.equals("LANGUAGESELECTION"))
            onSelectLocale(message);
        else if (text.equals("/help"))
            helpMessage(message);
        else if (text.contains("создать заметку ") || text.contains("создай заметку ") || text.contains("добавь заметку ") || text.contains("добавить заметку "))
            createNote(message);
        else if (text.contains("прочитать заметку")
                || text.contains("посмотреть заметку")
                || text.contains("удалить заметку")
                || text.contains("удали заметку")
                || text.contains("посмотреть заметки")
                || text.contains("все заметки"))
            getAllNotes(message);
        else
            doNotUnderstandMessage(message);
    }

    private void createNote(Message message) throws TelegramApiException, UnsupportedEncodingException {
        String note = message.getText();
        int chatId = Math.toIntExact(message.getChatId());
        if (note.toLowerCase().indexOf("создать заметку ") == 0) NotesManager.addNote(chatId, note.substring(16));
        else if (note.toLowerCase().indexOf("создай заметку ") == 0) NotesManager.addNote(chatId, note.substring(15));
        else if (note.toLowerCase().indexOf("добавь заметку ") == 0) NotesManager.addNote(chatId, note.substring(15));
        else if (note.toLowerCase().indexOf("добавить заметку ") == 0) NotesManager.addNote(chatId, note.substring(17));
        else {
            sendMessage(new SendMessage()
                    .setText(new String(resourceBundle.getString("after.create.note.lose").getBytes("ISO-8859-1"), "UTF-8"))
                    .setChatId(String.valueOf(chatId)));
            return;
        }
        sendMessage(new SendMessage()
                .setText(new String(resourceBundle.getString("after.create.note").getBytes("ISO-8859-1"), "UTF-8"))
                .setChatId(String.valueOf(chatId)));
    }

    private List<Note> getAllNotes(Message message) throws TelegramApiException, UnsupportedEncodingException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String mess = "";
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
            if (message.getText().equalsIgnoreCase("прочитать заметку")
                    || message.getText().equalsIgnoreCase("посмотреть заметку")
                    || message.getText().equalsIgnoreCase("посмотреть заметки")
                    || message.getText().equalsIgnoreCase("все заметки")) {
                sendMessage(new SendMessage()
                        .setChatId(String.valueOf(message.getChatId()))
                        .setText(new String(resourceBundle.getString("info.about.get.note").getBytes("ISO-8859-1"), "UTF-8")));
                ChatsManager.setStatus(message, "CHOOSENOTE");
            } else if (message.getText().equalsIgnoreCase("удалить заметку")
                    || message.getText().equalsIgnoreCase("удали заметку")) {
                sendMessage(new SendMessage()
                        .setChatId(String.valueOf(message.getChatId()))
                        .setText(new String(resourceBundle.getString("info.about.delete.note").getBytes("ISO-8859-1"), "UTF-8")));
                ChatsManager.setStatus(message, "CHOOSENOTEFORDEL");
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
                .setText("Заметка>> " + note.getText()));
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
                .setText(new String(resourceBundle.getString("misunderstanding").getBytes("ISO-8859-1"), "UTF-8")));
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
        ChatsManager.setStatus(message, "LANGUAGESELECTION");
    }

    private void onSelectLocale(Message message) throws UnsupportedEncodingException, TelegramApiException {
        if (message.getText().equalsIgnoreCase("Русский"))
            ChatsManager.setLocale(message, "ru");
        else if (message.getText().equalsIgnoreCase("English"))
            ChatsManager.setLocale(message, "en");
        ChatsManager.setStatus(message, "");
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText(new String(resourceBundle.getString("after.select.language").getBytes("ISO-8859-1"), "UTF-8"))
                .setReplayMarkup(new ReplyKeyboardHide().setSelective(true).setHideKeyboard(true)));
    }
}
