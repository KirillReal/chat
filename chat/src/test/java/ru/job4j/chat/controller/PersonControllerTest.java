package ru.job4j.chat.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.chat.ChatApplication;
import ru.job4j.chat.model.Person;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ChatApplication.class)
@AutoConfigureMockMvc
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonController persons;

    @Test
    @WithMockUser
    public void whenFindAll() throws Exception {
        Person p1 = Person.of(1, "user", "user");
        Person p2 = Person.of(2, "admin", "admin");

        when(persons.findAll()).thenReturn(Arrays.asList(p1, p2));
        mockMvc.perform(get("/persons/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].login", is("user")))
                .andExpect(jsonPath("$[0].password", is("user")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].login", is("admin")))
                .andExpect(jsonPath("$[1].password", is("admin")));
        verify(persons, times(1)).findAll();
        verifyNoMoreInteractions(persons);
    }

    @Test
    @WithMockUser
    public void whenAdd() throws Exception {
        Person p = Person.of(1, "user2", "user");
        when(persons.create(any(Person.class))).thenReturn(new ResponseEntity<>(p, HttpStatus.OK));
        mockMvc.perform(post("/persons/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"user2\",\"password\":\"user\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(persons, times(1)).create(any(Person.class));
        ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
        verify(persons).create(arg.capture());
        Assertions.assertEquals(arg.getValue().getLogin(), "user2");
        Assertions.assertEquals(arg.getValue().getPassword(), "user");
    }

    @Test
    @WithMockUser
    public void whenUpdate() throws Exception {
        when(persons.update(any(Person.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(put("/persons/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\",\"login\":\"user\",\"password\":\"user\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(persons, times(1)).update(any(Person.class));
        ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
        verify(persons).update(arg.capture());
        Assertions.assertEquals("user", arg.getValue().getLogin());
        Assertions.assertEquals("user", arg.getValue().getPassword());
    }

    @Test
    @WithMockUser
    public void whenFindById() throws Exception {
        Person p = Person.of(1, "user2", "user");
        when(persons.findById(anyInt())).thenReturn(new ResponseEntity<>(p, HttpStatus.OK));
        mockMvc.perform(get("/persons/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.login", is("user2")))
                .andExpect(jsonPath("$.password", is("user")));
        verify(persons, times(1)).findById(anyInt());
        verifyNoMoreInteractions(persons);
    }

    @Test
    @WithMockUser
    public void whenDelete() throws Exception {
        when(persons.delete(anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(delete("/persons/2"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(persons, times(1)).delete(anyInt());
        verifyNoMoreInteractions(persons);
    }
}