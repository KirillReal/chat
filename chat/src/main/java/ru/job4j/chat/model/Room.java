package ru.job4j.chat.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public static Room of(String name) {
        Room r = new Room();
        r.name = name;
        return r;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String
    toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id &&
                Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
