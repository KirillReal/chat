package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.repository.PersonRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonRepository persons;

    public PersonController(final PersonRepository persons) {
        this.persons = persons;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return StreamSupport.stream(this.persons.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        return this.persons.existsById(id)
                ? new ResponseEntity<>(this.persons.findById(id).get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return person != null
                ? new ResponseEntity<>(this.persons.save(person), HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        this.persons.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person p = new Person();
        p.setId(id);
        this.persons.delete(p);
        return ResponseEntity.ok().build();
    }
}
