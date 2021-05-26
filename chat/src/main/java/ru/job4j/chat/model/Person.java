package ru.job4j.chat.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String login;
    private String password;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "ROLE_ID_FK"))
    private Role role;

    public static Person of(int id, String login, String password) {
        Person p = new Person();
        p.id = id;
        p.login = login;
        p.password = password;
        p.role = Role.of(1, "ROLE_USER");
        return p;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id &&
                Objects.equals(login, person.login) &&
                Objects.equals(password, person.password) &&
                Objects.equals(role, person.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role);
    }
}
