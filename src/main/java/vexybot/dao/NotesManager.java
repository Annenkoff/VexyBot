package vexybot.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import vexybot.entity.Note;
import vexybot.util.HibernateSessionFactory;

import java.util.List;

public class NotesManager {
    public static void addNote(int chatId, String text) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Note note = new Note();
            note.setChatId(chatId);
            note.setText(text);
            session.save(note);
            session.getTransaction().commit();
        } catch (Exception e) {
        }
    }

    public static void deleteNote(int id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Note note = (Note) session.get(Note.class, id);
            session.delete(note);
            session.getTransaction().commit();
        } catch (Exception e) {

        }
    }

    public static List<Note> getAllNotes(int chatId) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("FROM Note where chatId=:chatId");
        query.setParameter("chatId", chatId);
        return query.list();
    }

    public static Note getNote(int id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Note note = (Note) session.get(Note.class, id);
        return note;
    }
}
