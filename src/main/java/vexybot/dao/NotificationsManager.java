package vexybot.dao;

import org.hibernate.Session;
import org.telegram.telegrambots.api.objects.Message;
import vexybot.entity.Notification;
import vexybot.util.HibernateSessionFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NotificationsManager extends Thread {
    private static DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void addNote(int chatId, String text, java.sql.Date dateOfNotification) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        Notification notification = new Notification();
        notification.setChatId(chatId);
        notification.setDescription(text);
        notification.setDateOfNotification(dateOfNotification);
        session.save(notification);
        session.getTransaction().commit();
    }

    public static java.sql.Date getDate(Message message) {
        Date date = null;
        if (message.getText().toLowerCase().contains(" в ")) {
            String dateString = message.getText().split(" в ")[1];
            int hours = Integer.parseInt(dateString.split(":")[0]);
            int min = Integer.parseInt(dateString.split(":")[1]);
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.set(Calendar.HOUR, hours);
            gregorianCalendar.set(Calendar.MINUTE, min);
            date = gregorianCalendar.getTime();
            simpleDateFormat.format(date);
        }
        java.sql.Date date2 = new java.sql.Date(date.getTime());
        simpleDateFormat.format(date2);
        return date2;
    }

    public static String getString(Message message) {
        String string = message.getText();
        String newString = string.substring(8).split(" в ")[0];
        return newString;
    }

    @Override
    public void run() {
    }
}
