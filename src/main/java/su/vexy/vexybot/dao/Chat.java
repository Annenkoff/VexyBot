package su.vexy.vexybot.dao;

import javax.persistence.*;

@Entity
@Table(name = "chats", schema = "a0080939_vexybot", catalog = "")
public class Chat {
    private int id;
    private String status;
    private String locale;
    private String location;
    private int morningNotification;
    private int eveningNotification;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "locale")
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "morning_notification")
    public int getMorningNotification() {
        return morningNotification;
    }

    public void setMorningNotification(int morningNotification) {
        this.morningNotification = morningNotification;
    }

    @Basic
    @Column(name = "evening_notification")
    public int getEveningNotification() {
        return eveningNotification;
    }

    public void setEveningNotification(int eveningNotification) {
        this.eveningNotification = eveningNotification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat that = (Chat) o;

        if (id != that.id) return false;
        if (morningNotification != that.morningNotification) return false;
        if (eveningNotification != that.eveningNotification) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (locale != null ? !locale.equals(that.locale) : that.locale != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + morningNotification;
        result = 31 * result + eveningNotification;
        return result;
    }
}
