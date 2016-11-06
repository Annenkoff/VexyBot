package su.vexy.vexybot.manager;

import org.hibernate.Query;
import org.hibernate.Session;
import org.telegram.telegrambots.api.objects.Message;
import su.vexy.vexybot.dao.Chat;
import su.vexy.vexybot.services.HibernateSessionFactory;

public class ChatsManager implements Manager {
    public static void checkChat(Message message) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        int chatId = Math.toIntExact(message.getChatId());
        Query query = session.createQuery("FROM Chat WHERE id=:chatId");
        query.setParameter("chatId", chatId);
        if (query.list().size() == 0 || query.list().isEmpty())
            addChat(message);
    }

    public static void addChat(Message message) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        Chat chat = new Chat();
        chat.setId(Math.toIntExact(message.getChatId()));
        chat.setUserName(message.getChat().getUserName());
        chat.setFirstName(message.getChat().getFirstName());
        chat.setLastName(message.getChat().getLastName());
        chat.setLocale("ru");
        session.save(chat);
        session.getTransaction().commit();
    }

    public static void setStatus(Message message, String status) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        int chatId = Math.toIntExact(message.getChatId());
        session.beginTransaction();
        Chat chat = session.get(Chat.class, chatId);
        chat.setStatus(status);
        session.merge(chat);
        session.getTransaction().commit();
    }

    public static String getStatus(Message message) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        int chatId = Math.toIntExact(message.getChatId());
        Chat chat = session.get(Chat.class, chatId);
        return chat.getStatus();
    }

    public static String getLocale(Message message) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        int chatId = Math.toIntExact(message.getChatId());
        Chat chat = session.get(Chat.class, chatId);
        return chat.getLocale();
    }

    public static void setLocale(Message message, String locale) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        int chatId = Math.toIntExact(message.getChatId());
        session.beginTransaction();
        Chat chat = session.get(Chat.class, chatId);
        chat.setLocale(locale);
        session.merge(chat);
        session.getTransaction().commit();
    }

    public static void setLocation(Message message, String location) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        int chatId = Math.toIntExact(message.getChatId());
        session.beginTransaction();
        Chat chat = session.get(Chat.class, chatId);
        chat.setLocation(location);
        session.merge(chat);
        session.getTransaction().commit();
    }

    public static void removeStatus(Message message) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        int chatId = Math.toIntExact(message.getChatId());
        session.beginTransaction();
        Chat chat = session.get(Chat.class, chatId);
        chat.setStatus("");
        session.merge(chat);
        session.getTransaction().commit();
    }
}
