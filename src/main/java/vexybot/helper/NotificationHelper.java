package vexybot.helper;

import org.telegram.telegrambots.api.objects.Message;
import vexybot.manager.NotificationsManager;

import java.sql.Date;

public class NotificationHelper implements Helper {
    private void addNotification(Message message) {
        Date date = NotificationsManager.getDate(message);
        String text = NotificationsManager.getString(message);
        int chatId = Math.toIntExact(message.getChatId());
        NotificationsManager.addNote(chatId, text, date);
    }
}
