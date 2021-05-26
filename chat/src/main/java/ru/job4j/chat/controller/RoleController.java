package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.repository.RoleRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleRepository roles;

    public RoleController(final RoleRepository roles) {
        this.roles = roles;
    }

    @GetMapping("/")
    public List<Role> findAll() {
        return StreamSupport.stream(this.roles.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable int id) {
        return this.roles.existsById(id)
                ? new ResponseEntity<>(this.roles.findById(id).get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Role> create(@RequestBody Role role) {
        return role != null
                ? new ResponseEntity<>(this.roles.save(role), HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Role role) {
        this.roles.save(role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Role r = new Role();
        r.setId(id);
        this.roles.delete(r);
        return ResponseEntity.ok().build();
    }
}
