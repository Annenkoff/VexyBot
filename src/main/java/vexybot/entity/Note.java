package vexybot.entity;

import javax.persistence.*;

@Entity
@Table(name = "Notes", schema = "vex", catalog = "")
public class Note {
    private int id;
    private int chatId;
    private String text;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CHATID", nullable = false)
    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatid) {
        this.chatId = chatid;
    }

    @Basic
    @Column(name = "TEXT", nullable = false, length = 45)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note that = (Note) o;

        if (id != that.id) return false;
        if (chatId != that.chatId) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + chatId;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
