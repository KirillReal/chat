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
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;

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
class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomController rooms;

    @Test
    @WithMockUser
    public void whenFindAll() throws Exception {
        Room r1 = Room.of("Room1");
        Room r2 = Room.of("Room2");
        r1.setId(1);
        r2.setId(2);
        when(rooms.findAll()).thenReturn(Arrays.asList(r1, r2));
        mockMvc.perform(get("/rooms/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Room1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Room2")));
        verify(rooms, times(1)).findAll();
        verifyNoMoreInteractions(rooms);
    }

    @Test
    @WithMockUser
    public void whenAdd() throws Exception {
        Room r = Room.of("Room1");
        when(rooms.create(any(Room.class))).thenReturn(new ResponseEntity<>(r, HttpStatus.OK));
        mockMvc.perform(post("/rooms/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Room1\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(rooms, times(1)).create(any(Room.class));
        ArgumentCaptor<Room> arg = ArgumentCaptor.forClass(Room.class);
        verify(rooms).create(arg.capture());
        Assertions.assertEquals(arg.getValue().getName(), "Room1");
    }

    @Test
    @WithMockUser
    public void whenUpdate() throws Exception {
        when(rooms.update(any(Room.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(put("/rooms/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\",\"name\":\"Room1\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(rooms, times(1)).update(any(Room.class));
        ArgumentCaptor<Room> arg = ArgumentCaptor.forClass(Room.class);
        verify(rooms).update(arg.capture());
        Assertions.assertEquals("Room1", arg.getValue().getName());
    }

    @Test
    @WithMockUser
    public void whenFindById() throws Exception {
        Room r = Room.of("Room1");
        r.setId(1);
        when(rooms.findById(anyInt())).thenReturn(new ResponseEntity<>(r, HttpStatus.OK));
        mockMvc.perform(get("/rooms/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Room1")));
        verify(rooms, times(1)).findById(anyInt());
        verifyNoMoreInteractions(rooms);
    }

    @Test
    @WithMockUser
    public void whenDelete() throws Exception {
        when(rooms.delete(anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(delete("/rooms/1"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(rooms, times(1)).delete(anyInt());
        verifyNoMoreInteractions(rooms);
    }

    @Test
    @WithMockUser
    public void whenFindByMessagesByRoomId() throws Exception {
        Room r = Room.of("Room1");
        r.setId(1);
        Person p = Person.of(1, "user2", "user");
        Message m1 = Message.of(1, "msg1", p);
        Message m2 = Message.of(2, "msg2", p);
        m1.setRoom(r);
        m2.setRoom(r);
        when(rooms.getMessagesByRoom(anyInt())).thenReturn(new ResponseEntity<>(Arrays.asList(m1, m2), HttpStatus.OK));
        mockMvc.perform(get("/rooms/1/messages"))
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
        verify(rooms, times(1)).getMessagesByRoom(anyInt());
        verifyNoMoreInteractions(rooms);
    }

    @Test
    @WithMockUser
    public void whenCreateMessage() throws Exception {
        Room r = Room.of("Room1");
        Person p = Person.of(1, "user2", "user");
        Message m = Message.of(1, "msg1", p);
        m.setRoom(r);
        when(rooms.createMessage(anyInt(), anyInt(), anyString())).thenReturn(new ResponseEntity<>(m, HttpStatus.OK));
        mockMvc.perform(post("/rooms/1/create")
                .param("uid", "1")
                .param("text", "msg1"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(rooms, times(1)).createMessage(anyInt(), anyInt(), anyString());
        verify(rooms).createMessage(anyInt(), anyInt(), anyString());
        verifyNoMoreInteractions(rooms);
    }

    @Test
    @WithMockUser
    public void whenDeleteMessage() throws Exception {
        when(rooms.deleteMessage(anyInt(), anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(delete("/rooms/1/delete/1"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(rooms, times(1)).deleteMessage(anyInt(), anyInt());
        verifyNoMoreInteractions(rooms);
    }
}