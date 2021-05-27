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
import ru.job4j.chat.model.Role;
import java.util.Arrays;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = ChatApplication.class)
@AutoConfigureMockMvc
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleController roles;

    @Test
    @WithMockUser
    public void whenFindAll() throws Exception {
        Role r1 = Role.of(1, "ROLE_USER");
        Role r2 = Role.of(2, "ROLE_ADMIN");

        when(roles.findAll()).thenReturn(Arrays.asList(r1, r2));
        mockMvc.perform(get("/roles/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("ROLE_USER")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("ROLE_ADMIN")));
        verify(roles, times(1)).findAll();
        verifyNoMoreInteractions(roles);
    }

    @Test
    @WithMockUser
    public void whenAdd() throws Exception {
        Role r = Role.of(3, "ROLE_MODERATOR");
        when(roles.create(any(Role.class))).thenReturn(new ResponseEntity<>(r, HttpStatus.OK));
        mockMvc.perform(post("/roles/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"ROLE_MODERATOR\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(roles, times(1)).create(any(Role.class));
        ArgumentCaptor<Role> arg = ArgumentCaptor.forClass(Role.class);
        verify(roles).create(arg.capture());
        Assertions.assertEquals(arg.getValue().getName(), "ROLE_MODERATOR");
    }

    @Test
    @WithMockUser
    public void whenUpdate() throws Exception {
        when(roles.update(any(Role.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(put("/roles/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"3\",\"name\":\"ROLE_ROOT\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(roles, times(1)).update(any(Role.class));
        ArgumentCaptor<Role> arg = ArgumentCaptor.forClass(Role.class);
        verify(roles).update(arg.capture());
        Assertions.assertEquals("ROLE_ROOT", arg.getValue().getName());
    }

    @Test
    @WithMockUser
    public void whenFindById() throws Exception {
        Role r = Role.of(1, "ROLE_USER");
        when(roles.findById(anyInt())).thenReturn(new ResponseEntity<>(r, HttpStatus.OK));
        mockMvc.perform(get("/roles/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("ROLE_USER")));
        verify(roles, times(1)).findById(anyInt());
        verifyNoMoreInteractions(roles);
    }

    @Test
    @WithMockUser
    public void whenDelete() throws Exception {
        when(roles.delete(anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(delete("/roles/2"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(roles, times(1)).delete(anyInt());
        verifyNoMoreInteractions(roles);
    }
}