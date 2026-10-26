package com.smarttask.smartissuetaskapi.controller;

import com.smarttask.smartissuetaskapi.service.IssueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IssueController.class)
class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IssueService issueService;

    @Test
    void createAndUpdateRejectBlankIssueTitles() throws Exception {
        for (String title : new String[]{"null", "\"\"", "\"   \""}) {
            String body = """
                    {
                      "id": 2,
                      "title": %s,
                      "description": "Validation test",
                      "priority": "MEDIUM",
                      "status": "OPEN"
                    }
                    """.formatted(title);

            mockMvc.perform(post("/api/issues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());

            mockMvc.perform(put("/api/issues/2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        verifyNoInteractions(issueService);
    }
}
