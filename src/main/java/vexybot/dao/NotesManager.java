package vexybot.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import vexybot.entity.Note;
import vexybot.util.HibernateSessionFactory;

public class NotesManager {
    private SessionFactory sessionFactory;

    public static void add(int chatId, String name, String text) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Note note = new Note();
            note.setChatId(chatId);
            note.setName(name);
            note.setText(text);
            session.save(note);
            session.getTransaction().commit();
        } catch (Exception e) {
        }
    }
}
