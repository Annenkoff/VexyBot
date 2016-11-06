package su.vexy.vexybot.dao;

import javax.persistence.*;

@Entity
@Table(name = "chats", schema = "a0080939_vexybot", catalog = "")
public class Chat {
    private int id;
    private String firstName;
    private String lastName;
    private String locale;
    private String location;
    private Integer morningNotification;
    private Integer eveningNotification;
    private String status;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
    public Integer getMorningNotification() {
        return morningNotification;
    }

    public void setMorningNotification(Integer morningNotification) {
        this.morningNotification = morningNotification;
    }

    @Basic
    @Column(name = "evening_notification")
    public Integer getEveningNotification() {
        return eveningNotification;
    }

    public void setEveningNotification(Integer eveningNotification) {
        this.eveningNotification = eveningNotification;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat that = (Chat) o;

        if (id != that.id) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (locale != null ? !locale.equals(that.locale) : that.locale != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (morningNotification != null ? !morningNotification.equals(that.morningNotification) : that.morningNotification != null)
            return false;
        if (eveningNotification != null ? !eveningNotification.equals(that.eveningNotification) : that.eveningNotification != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (morningNotification != null ? morningNotification.hashCode() : 0);
        result = 31 * result + (eveningNotification != null ? eveningNotification.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
