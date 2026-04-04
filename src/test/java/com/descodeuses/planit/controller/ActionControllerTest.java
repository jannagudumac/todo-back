package com.descodeuses.planit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.descodeuses.planit.dto.ActionDTO;
import com.descodeuses.planit.security.JwtFilter;
import com.descodeuses.planit.service.ActionService;

@WebMvcTest(ActionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ActionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActionService actionService;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    void getAllReturnsActions() throws Exception {
        ActionDTO dto = new ActionDTO(1L, "Prepare soutenance", false, LocalDate.of(2025, 8, 20), "Slides", 2);
        dto.setProjetId(10L);
        dto.setMemberIds(Set.of(3L, 4L));

        when(actionService.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/action"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Prepare soutenance"))
                .andExpect(jsonPath("$[0].projetId").value(10));
    }

    @Test
    void createReturnsCreatedAction() throws Exception {
        ActionDTO dto = new ActionDTO(5L, "Write dossier", false, LocalDate.of(2025, 8, 25), "RNCP", 1);
        dto.setProjetId(7L);

        when(actionService.create(any(ActionDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/action")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"title\":\"Write dossier\"," +
                        "\"completed\":false," +
                        "\"dueDate\":\"2025-08-25\"," +
                        "\"description\":\"RNCP\"," +
                        "\"priority\":1," +
                        "\"projetId\":7}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("Write dossier"))
                .andExpect(jsonPath("$.projetId").value(7));
    }

    @Test
    void getByIdReturnsSingleAction() throws Exception {
        ActionDTO dto = new ActionDTO(9L, "Deploy app", true, LocalDate.of(2025, 8, 30), "Render and Netlify", 3);

        when(actionService.getActionById(eq(9L))).thenReturn(dto);

        mockMvc.perform(get("/api/action/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.completed").value(true));
    }
}
