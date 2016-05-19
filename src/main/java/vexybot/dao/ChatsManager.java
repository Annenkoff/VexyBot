package vexybot.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.telegram.telegrambots.api.objects.Message;
import vexybot.entity.Chat;
import vexybot.util.HibernateSessionFactory;

public class ChatsManager {
    public static void checkChat(Message message) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        int chatId = Math.toIntExact(message.getChatId());
        Query query = session.createQuery("FROM Chat WHERE id=:chatId");
        query.setParameter("chatId", chatId);
        if (query.list().size() == 0 || query.list().isEmpty())
            addNote(message);
    }

    public static void addNote(Message message) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        Chat chat = new Chat();
        chat.setId(Math.toIntExact(message.getChatId()));
        chat.setStatus("");
        session.save(chat);
        session.getTransaction().commit();
    }

    public static void setStatus(Message message, String status) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        int chatId = Math.toIntExact(message.getChatId());
        session.beginTransaction();
        Chat chat = (Chat) session.get(Chat.class, chatId);
        Chat newChat = new Chat();
        newChat.setId(chatId);
        newChat.setStatus(status);
        session.delete(chat);
        session.save(newChat);
        session.getTransaction().commit();
    }

    public static String getStatus(Message message) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        int chatId = Math.toIntExact(message.getChatId());
        Chat chat = (Chat) session.get(Chat.class, chatId);
        return chat.getStatus();
    }
}
