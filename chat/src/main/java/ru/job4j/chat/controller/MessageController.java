package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.repository.MessageRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageRepository messages;

    public MessageController(final MessageRepository messages) {
        this.messages = messages;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return StreamSupport.stream(this.messages.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        return this.messages.existsById(id)
                ? new ResponseEntity<>(this.messages.findById(id).get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        return message != null
                ? new ResponseEntity<>(this.messages.save(message), HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        this.messages.save(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message m = new Message();
        m.setId(id);
        this.messages.delete(m);
        return ResponseEntity.ok().build();
    }
}
