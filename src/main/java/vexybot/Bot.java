package vexybot;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import vexybot.dao.NotesManager;
import vexybot.entity.Note;

import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(message.getText());
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.enableMarkdown(true);
                handleIncomingMessage(message);
            }
        }
    }

    public String getBotUsername() {
        return Config.USERNAME;
    }

    public String getBotToken() {
        return Config.TOKEN;
    }

    private void handleIncomingMessage(Message message) {
        if (message.getText().toLowerCase().contains("/help")) {
            try {
                sendMessage(new SendMessage()
                        .setChatId(String.valueOf(message.getChatId()))
                        .setText("Что я могу:\n" +
                                "'создай заметку <текст заметки>' - создам заметку с вашим текстом и сохраню её у себя.\n" +
                                "'все заметки' - предварительно покажу все ваши заметки."));
            } catch (TelegramApiException e) {
            }
        } else if (message.getText().toLowerCase().contains("создать заметку ") || message.getText().toLowerCase().contains("создай заметку ")) {
            createNote(Integer.parseInt(String.valueOf(message.getChatId())), message.getText());
        } else if (message.getText().toLowerCase().contains("все заметки")) {
            getNote(Integer.parseInt(String.valueOf(message.getChatId())));
        }
    }

    private void createNote(int chatId, String note) {
        if (note.indexOf("создать заметку ") == -1) {
            NotesManager.addNote(chatId, note.substring(16));
        } else if (note.indexOf("создай заметку ") == -1) {
            NotesManager.addNote(chatId, note.substring(15));
        }
        try {
            sendMessage(new SendMessage()
                    .setText("Заметка создана. Ты можешь посмотреть все заметки, отправив 'все заметки'")
                    .setChatId(String.valueOf(chatId)));
        } catch (TelegramApiException e) {
        }
    }

    private List<Note> getAllNotes(int chatId) {
        List<Note> notes = NotesManager.getAllNotes(chatId);
        String mess = "";
        int a = 1;
        for (Note s : notes) {
            mess += "#" + a + " - " + s.getText();
            mess += "\n";
            a++;
        }
        try {
            if (notes.size() == 0 || notes.isEmpty()) {
                sendMessage(new SendMessage().setChatId(String.valueOf(chatId)).setText("У вас нет заметок."));
                return null;
            }
            sendMessage(new SendMessage().setChatId(String.valueOf(chatId)).setText(mess));
            return notes;
        } catch (TelegramApiException e) {
        }
        return null;
    }

    private void getNote(int chatId) {
        List<Note> notes = getAllNotes(chatId);
        try {
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(chatId))
                    .setText("Теперь введи номер заметки, которую ты хочешь посмотреть."));
        } catch (TelegramApiException e) {
        }
        Message message = new Update().getMessage();
        int i = Integer.parseInt(message.getText()) - 1;
        try {
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(chatId))
                    .setText(NotesManager.getNote(notes.get(i).getId()).getText()));
        } catch (TelegramApiException e) {
        }
    }
}
