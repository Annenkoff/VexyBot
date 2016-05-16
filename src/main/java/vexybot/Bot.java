package vexybot;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import vexybot.dao.NotesManager;

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
        if (message.getText().contains("создать заметку") || message.getText().contains("создай заметку")) {
            createNote(Integer.parseInt(String.valueOf(message.getChatId())), message.getText());
            try {
                sendMessage(new SendMessage()
                        .setText("Заметка создана. Ты можешь посмотреть все заметки командой /notes")
                        .setReplayToMessageId(message.getMessageId())
                        .setChatId(String.valueOf(message.getChatId())));
            } catch (TelegramApiException e) {
            }
        }
    }

    private void createNote(int id, String note) {
        if (note.contains("\n")) {
            String[] s = note.split("\n");
            if (s.length > 2) {
                String string = "";
                for (int a = 2; a < s.length; a++) {
                    string += s[a];
                    string += "\n";
                }
                NotesManager.add(id, s[1], string);
            }
        } else {

        }
    }
}
