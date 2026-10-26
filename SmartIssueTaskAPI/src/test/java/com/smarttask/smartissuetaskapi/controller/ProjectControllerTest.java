package com.smarttask.smartissuetaskapi.controller;

import com.smarttask.smartissuetaskapi.entity.Project;
import com.smarttask.smartissuetaskapi.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Test
    void createBindsProjectIdAndReturnsCreatedProject() throws Exception {
        given(projectService.create(any(Project.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 100,
                                  "name": "POST_DEBUG_ABC",
                                  "description": "Testing POST request"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/projects/100"))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("POST_DEBUG_ABC"))
                .andExpect(jsonPath("$.description").value("Testing POST request"));

        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectService).create(projectCaptor.capture());
        assertEquals(100, projectCaptor.getValue().getId());
    }

    @Test
    void createAndUpdateRejectBlankProjectNames() throws Exception {
        for (String name : new String[]{"null", "\"\"", "\"   \""}) {
            String body = """
                    {
                      "id": 100,
                      "name": %s,
                      "description": "Validation test"
                    }
                    """.formatted(name);

            mockMvc.perform(post("/api/projects")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());

            mockMvc.perform(put("/api/projects/100")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        verifyNoInteractions(projectService);
    }

    @Test
    void getByIdReturnsProject() throws Exception {
        Project project = project(100);
        given(projectService.getById(100)).willReturn(project);

        mockMvc.perform(get("/api/projects/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("POST_DEBUG_ABC"));
    }

    @Test
    void getAllIncludesProject() throws Exception {
        Project project = project(100);
        given(projectService.getAll()).willReturn(List.of(project));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].name").value("POST_DEBUG_ABC"));
    }

    private Project project(Integer id) {
        Project project = mock(Project.class);
        when(project.getId()).thenReturn(id);
        when(project.getName()).thenReturn("POST_DEBUG_ABC");
        when(project.getDescription()).thenReturn("Testing POST request");
        return project;
    }
}
