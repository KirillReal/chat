package ru.job4j.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.MessageRepository;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private final RoomRepository rooms;
    @Autowired
    private final PersonRepository persons;
    @Autowired
    private final MessageRepository messages;

    public RoomController(final RoomRepository rooms, final PersonRepository persons, MessageRepository messages) {
        this.rooms = rooms;
        this.persons = persons;
        this.messages = messages;
    }

    @GetMapping("/")
    public List<Room> findAll() {
        return StreamSupport.stream(this.rooms.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        return this.rooms.existsById(id)
                ? new ResponseEntity<>(this.rooms.findById(id).get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return room != null
                ? new ResponseEntity<>(this.rooms.save(room), HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        this.rooms.save(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Room room = new Room();
        room.setId(id);
        this.rooms.delete(room);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Message>> getMessagesByRoom(@PathVariable int id) {
        return this.rooms.existsById(id)
                ? new ResponseEntity<>(this.messages.getByRoomId(id), HttpStatus.OK)
                : new ResponseEntity<>((HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/{id}/create")
    public ResponseEntity<Message> createMessage(@PathVariable int id,
                                                 @RequestParam(value = "uid") int uid,
                                                 @RequestParam(value = "text") String text) {
        if (this.persons.existsById(uid)) {
            if (this.rooms.existsById(id)) {
                Message newMsg = Message.of(0, text, this.persons.findById(uid).get());
                newMsg.setRoom(this.rooms.findById(id).get());
                return new ResponseEntity<>(this.messages.save(newMsg), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}/delete/{mid}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int id,
                                              @PathVariable int mid) {
        Message msg = new Message();
        msg.setId(mid);
        this.messages.delete(msg);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
