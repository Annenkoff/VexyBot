package vexybot;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import vexybot.dao.ChatsManager;
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
        ChatsManager.checkChat(message);
        try {
            if (message.getText().equalsIgnoreCase("/help"))
                sendHelpMessage(message);
            else if (message.getText().toLowerCase().contains("создать заметку ") || message.getText().toLowerCase().contains("создай заметку "))
                createNote(Integer.parseInt(String.valueOf(message.getChatId())), message.getText());
            else if (message.getText().toLowerCase().contains("все заметки"))
                getAllNotes(message);
        } catch (TelegramApiException e) {
        }
    }

    private void createNote(int chatId, String note) throws TelegramApiException {
        if (note.indexOf("создать заметку ") == -1) {
            NotesManager.addNote(chatId, note.substring(16));
        } else if (note.indexOf("создай заметку ") == -1) {
            NotesManager.addNote(chatId, note.substring(15));
        } else {
            sendMessage(new SendMessage()
                    .setText("Заметку создать не получилось.")
                    .setChatId(String.valueOf(chatId)));
            return;
        }
        sendMessage(new SendMessage()
                .setText("Заметка создана. Ты можешь посмотреть все заметки, отправив 'все заметки'")
                .setChatId(String.valueOf(chatId)));
    }

    private List<Note> getAllNotes(Message message) throws TelegramApiException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String mess = "";
        int a = 1;
        for (Note s : notes) {
            mess += "#" + a + " - " + s.getText();
            mess += "\n";
            a++;
        }
        if (notes.size() == 0 || notes.isEmpty()) {
            sendMessage(new SendMessage().setChatId(String.valueOf(message.getChatId())).setText("У вас нет заметок."));
        } else {
            sendMessage(new SendMessage().setChatId(String.valueOf(message.getChatId())).setText(mess));
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText("Введите номер заметки, которую вы хотите посмотреть.\n" +
                            "Если вы хотите прекратить работу с заметками, введите /cancel"));
            ChatsManager.setStatus(message, "CHOOSENOTE");
        }
        return notes;
    }

    private void getNote(int chatId) throws TelegramApiException {

    }

    private void sendHelpMessage(Message message) throws TelegramApiException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText("Что я могу:\n" +
                        "'создай заметку <текст заметки>' - создам заметку с вашим текстом и сохраню её у себя.\n" +
                        "'все заметки' - предварительно покажу все ваши заметки."));
    }
}
