package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {
    Person findPersonByLogin(String login);
}
