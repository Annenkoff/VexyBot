package vexybot.helper;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.objects.Message;
import vexybot.Bot;
import vexybot.Status;
import vexybot.dao.Note;
import vexybot.manager.ChatsManager;
import vexybot.manager.MessageManager;
import vexybot.manager.NotesManager;
import vexybot.services.Emoji;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class NoteHelper implements Helper {
    public static void createNote(Message message) throws TelegramApiException, UnsupportedEncodingException {
        String note = message.getText();
        int chatId = Math.toIntExact(message.getChatId());
        if (ChatsManager.getLocale(message).equals("ru")) {
            if (note.toLowerCase().indexOf(MessageManager.RBText(message, "to.create.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(16));
            else if (note.toLowerCase().indexOf(MessageManager.RBText(message, "create.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(15));
            else if (note.toLowerCase().indexOf(MessageManager.RBText(message, "add.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(15));
            else if (note.toLowerCase().indexOf(MessageManager.RBText(message, "to.add.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(17));
        } else if (ChatsManager.getLocale(message).equals("en")) {
            if (note.toLowerCase().indexOf(MessageManager.RBText(message, "to.create.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(16));
            else if (note.toLowerCase().indexOf(MessageManager.RBText(message, "create.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(16));
            else if (note.toLowerCase().indexOf(MessageManager.RBText(message, "add.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(13));
            else if (note.toLowerCase().indexOf(MessageManager.RBText(message, "to.add.the.note")) == 0)
                NotesManager.addNote(chatId, note.substring(13));
        }
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                MessageManager.RBText(message, "after.create.note")));
    }

    public static List<Note> getAllNotes(Message message) throws TelegramApiException, UnsupportedEncodingException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String mess = "";
        mess += Emoji.PENCIL.toString() + MessageManager.RBText(message, "your.notes") + "\n";
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
            Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                    MessageManager.RBText(message, "no.notes")));
        else {
            Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                    mess));
            if (message.getText().equalsIgnoreCase(MessageManager.RBText(message, "to.read.the.note"))
                    || message.getText().equalsIgnoreCase(MessageManager.RBText(message, "to.view.the.note"))
                    || message.getText().equalsIgnoreCase(MessageManager.RBText(message, "to.view.all.notes"))
                    || message.getText().equalsIgnoreCase(MessageManager.RBText(message, "all.notes"))) {
                Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                        MessageManager.RBText(message, "info.about.get.note")));
                ChatsManager.setStatus(message, Status.CHOOSE_NOTE.toString());
            } else if (message.getText().equalsIgnoreCase(MessageManager.RBText(message, "to.delete.the.note"))
                    || message.getText().equalsIgnoreCase(MessageManager.RBText(message, "delete.the.note"))) {
                Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                        MessageManager.RBText(message, "info.about.delete.note")));
                ChatsManager.setStatus(message, Status.CHOOSE_NOTE_FOR_DELETE.toString());
            }
        }
        return notes;
    }

    public static void getNote(Message message) throws TelegramApiException, UnsupportedEncodingException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String text = message.getText();
        int number;
        try {
            number = Integer.parseInt(text) - 1;
        } catch (NumberFormatException e) {
            number = -1;
        }
        if (number == -1) {
            Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                    MessageManager.RBText(message, "info.about.get.note")));
            return;
        }
        Note note = notes.get(number);
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                MessageManager.RBText(message, "note") + " " + note.getText()));
        ChatsManager.removeStatus(message);
    }

    public static void deleteNote(Message message) throws TelegramApiException, UnsupportedEncodingException {
        List<Note> notes = NotesManager.getAllNotes(Math.toIntExact(message.getChatId()));
        String text = message.getText();
        int number;
        try {
            number = Integer.parseInt(text) - 1;
        } catch (NumberFormatException e) {
            number = -1;
        }
        if (number == -1) {
            Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                    MessageManager.RBText(message, "info.about.get.note")));
            return;
        }
        Note note = notes.get(number);
        NotesManager.deleteNote(note.getId());
        Bot.bot.sendMessage(MessageManager.getSendMessage(message,
                MessageManager.RBText(message, "after.delete.note")));
        ChatsManager.removeStatus(message);
    }
}
