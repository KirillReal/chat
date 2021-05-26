package ru.job4j.chat.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.chat.ChatApplication;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ChatApplication.class)
@AutoConfigureMockMvc
class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageController messages;

    @Test
    public void whenFindAll() throws Exception {
        Room r = Room.of("Room1");
        Person p = Person.of(1, "user2", "user");
        Message m1 = Message.of(1, "msg1", p);
        Message m2 = Message.of(2, "msg2", p);
        m1.setRoom(r);
        m2.setRoom(r);

        when(messages.findAll()).thenReturn(Arrays.asList(m1, m2));
        mockMvc.perform(get("/messages/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].text", is("msg1")))
                .andExpect(jsonPath("$[0].room.name", is("Room1")))
                .andExpect(jsonPath("$[0].person.id", is(1)))
                .andExpect(jsonPath("$[0].person.login", is("user2")))
                .andExpect(jsonPath("$[0].person.password", is("user")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].text", is("msg2")))
                .andExpect(jsonPath("$[1].room.name", is("Room1")))
                .andExpect(jsonPath("$[1].person.id", is(1)))
                .andExpect(jsonPath("$[1].person.login", is("user2")))
                .andExpect(jsonPath("$[1].person.password", is("user")));
        verify(messages, times(1)).findAll();
        verifyNoMoreInteractions(messages);
    }

    @Test
    public void whenAdd() throws Exception {
        Room r = Room.of("Room1");
        Person p = Person.of(1, "user2", "user");
        Message m = Message.of(1, "msg1", p);
        m.setRoom(r);

        when(messages.create(any(Message.class))).thenReturn(new ResponseEntity<>(m, HttpStatus.OK));
        mockMvc.perform(post("/messages/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\":\"msg1\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(messages, times(1)).create(any(Message.class));
        ArgumentCaptor<Message> arg = ArgumentCaptor.forClass(Message.class);
        verify(messages).create(arg.capture());
        assertEquals(arg.getValue().getText(), "msg1");
    }

    @Test
    public void whenUpdate() throws Exception {
        when(messages.update(any(Message.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(put("/messages/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\",\"text\":\"msg1\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(messages, times(1)).update(any(Message.class));
        ArgumentCaptor<Message> arg = ArgumentCaptor.forClass(Message.class);
        verify(messages).update(arg.capture());
        assertEquals("msg1", arg.getValue().getText());
    }

    @Test
    public void whenFindById() throws Exception {
        Room r = Room.of("Room1");
        Person p = Person.of(1, "user2", "user");
        Message m = Message.of(1, "msg1", p);
        m.setRoom(r);

        when(messages.findById(anyInt())).thenReturn(new ResponseEntity<>(m, HttpStatus.OK));
        mockMvc.perform(get("/messages/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("msg1")))
                .andExpect(jsonPath("$.room.name", is("Room1")))
                .andExpect(jsonPath("$.person.id", is(1)))
                .andExpect(jsonPath("$.person.login", is("user2")))
                .andExpect(jsonPath("$.person.password", is("user")));
        verify(messages, times(1)).findById(anyInt());
        verifyNoMoreInteractions(messages);
    }

    @Test
    public void whenDelete() throws Exception {
        when(messages.delete(anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(delete("/messages/2"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(messages, times(1)).delete(anyInt());
        verifyNoMoreInteractions(messages);
    }
}