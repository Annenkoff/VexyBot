package vexybot.manager;

import org.hibernate.Query;
import org.hibernate.Session;
import vexybot.dao.Note;
import vexybot.services.HibernateSessionFactory;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotesManager implements Manager {
    public static void addNote(int chatId, String text) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Note note = new Note();
            note.setChatId(chatId);
            note.setText(text);
            java.util.Date date = new java.util.Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.format(date);
            note.setDateOfCreation(new java.sql.Date(date.getTime()));
            session.save(note);
            session.getTransaction().commit();
        } catch (Exception e) {
        }
    }

    public static void deleteNote(int id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        Note note = (Note) session.get(Note.class, id);
        session.delete(note);
        session.getTransaction().commit();
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
