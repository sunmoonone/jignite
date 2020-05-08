package f.example.ignite.cache.starter.user.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author sunmoonone
 * @version 2018/12/27
 */
public class User implements Serializable {
    private static final long serialVersionUID = 0L;

    private Long id;
    private String nick;
    private String head;
    private Integer gender;

    public User() {
    }

    public User(Long id, String nick, String head, Integer gender) {
        this.id = id;
        this.nick = nick;
        this.head = head;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) || (
                Objects.equals(nick, user.nick) &&
                        Objects.equals(head, user.head) &&
                        Objects.equals(gender, user.gender)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
