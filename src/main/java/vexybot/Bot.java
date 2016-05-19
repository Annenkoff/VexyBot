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
                try {
                    handleIncomingMessage(message);
                } catch (TelegramApiException e) {
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

    private void handleIncomingMessage(Message message) throws TelegramApiException {
        String text = message.getText().toLowerCase();
        ChatsManager.checkChat(message);
        String status = ChatsManager.getStatus(message);
        if (text.equals("/cancel"))
            onCancelOperation(message);
        else if (status.equals("CHOOSENOTE")) {
            getNote(message);
        } else if (status.equals("CHOOSENOTEFORDEL")) {
            deleteNote(message);
        } else if (text.equals("/help"))
            sendHelpMessage(message);
        else if (text.contains("создать заметку ") || text.contains("создай заметку "))
            createNote(message);
        else if (text.contains("прочитать заметку") || text.contains("посмотреть заметку") || text.contains("удалить заметку"))
            getAllNotes(message);
        else
            doNotUnderstandMessage(message);
    }

    private void createNote(Message message) throws TelegramApiException {
        String note = message.getText().toLowerCase();
        int chatId = Math.toIntExact(message.getChatId());
        if (note.indexOf("создать заметку ") == 0) {
            NotesManager.addNote(chatId, note.substring(16));
        } else if (note.indexOf("создай заметку ") == 0) {
            NotesManager.addNote(chatId, note.substring(15));
        } else {
            sendMessage(new SendMessage()
                    .setText("Заметку создать не получилось.")
                    .setChatId(String.valueOf(chatId)));
            return;
        }
        sendMessage(new SendMessage()
                .setText("Заметка создана. Ты можешь посмотреть все заметки, отправив 'посмотреть заметку'")
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
            if (message.getText().equalsIgnoreCase("прочитать заметку") || message.getText().equalsIgnoreCase("посмотреть заметку")) {
                sendMessage(new SendMessage()
                        .setChatId(String.valueOf(message.getChatId()))
                        .setText("Введи номер заметки, которую ты хочешь посмотреть полностью.\n" +
                                "Если ты хочешь прекратить работу с заметками, введи /cancel"));
                ChatsManager.setStatus(message, "CHOOSENOTE");
            } else if (message.getText().equalsIgnoreCase("удалить заметку")) {
                sendMessage(new SendMessage()
                        .setChatId(String.valueOf(message.getChatId()))
                        .setText("Введи номер заметки, которую ты хочешь удалить.\n" +
                                "Если ты хочешь прекратить работу с заметками, введи /cancel"));
                ChatsManager.setStatus(message, "CHOOSENOTEFORDEL");
            }
        }
        return notes;
    }

    private void getNote(Message message) throws TelegramApiException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String text = message.getText();
        int number = Integer.parseInt(text) - 1;
        if (number < 0 || number > notes.size() - 1) {
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText("Нет заметки с таким номером."));
            return;
        }
        Note note = notes.get(number);
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText("Заметка>> " + note.getText()));
        ChatsManager.setStatus(message, "");
    }

    private void sendHelpMessage(Message message) throws TelegramApiException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText("Что я могу:\n" +
                        "'создай заметку <текст заметки>' - создам заметку с вашим текстом и сохраню её у себя.\n" +
                        "'посмотреть заметку' - покажу полностью выбранную заметку.\n" +
                        "'удалить заметку' - удалю выбранную заметку."));
    }

    private void onCancelOperation(Message message) throws TelegramApiException {
        ChatsManager.setStatus(message, "");
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText("Ок."));
    }

    private void doNotUnderstandMessage(Message message) throws TelegramApiException {
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText("Извини, но я тебя не понял. Введи /help , чтобы узнать о моих возможностях."));
    }

    private void deleteNote(Message message) throws TelegramApiException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String text = message.getText();
        int number = Integer.parseInt(text) - 1;
        if (number < 0 || number > notes.size() - 1) {
            sendMessage(new SendMessage()
                    .setChatId(String.valueOf(message.getChatId()))
                    .setText("Нет заметки с таким номером."));
            return;
        }
        Note note = notes.get(number);
        NotesManager.deleteNote(note.getId());
        sendMessage(new SendMessage()
                .setChatId(String.valueOf(message.getChatId()))
                .setText("Заметка удалена."));
        ChatsManager.setStatus(message, "");
    }
}
