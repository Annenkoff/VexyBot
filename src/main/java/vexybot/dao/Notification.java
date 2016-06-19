package vexybot.dao;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "notifications", schema = "vexy", catalog = "")
public class Notification {
    private int id;
    private int chatId;
    private Date dateOfNotification;
    private String description;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CHAT_ID")
    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    @Basic
    @Column(name = "DATE_OF_NOTIFICATION")
    public Date getDateOfNotification() {
        return dateOfNotification;
    }

    public void setDateOfNotification(Date dateOfNotification) {
        this.dateOfNotification = dateOfNotification;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (id != that.id) return false;
        if (chatId != that.chatId) return false;
        if (dateOfNotification != null ? !dateOfNotification.equals(that.dateOfNotification) : that.dateOfNotification != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + chatId;
        result = 31 * result + (dateOfNotification != null ? dateOfNotification.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
