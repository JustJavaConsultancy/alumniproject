package tech.justjava.alumni.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProjectService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AlumniProjectControllerTest {

    @Mock
    private AlumniProjectService alumniProjectService;

    @InjectMocks
    private AlumniProjectController alumniProjectController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(alumniProjectController).build();
    }

    @Test
    public void testDeployProcess() throws Exception {
        doNothing().when(alumniProjectService).deployProcess();

        mockMvc.perform(get("/alumni/deploy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alumni/start"));

        verify(alumniProjectService, times(1)).deployProcess();
    }

    @Test
    public void testShowStartForm() throws Exception {
        mockMvc.perform(get("/alumni/start"))
                .andExpect(status().isOk())
                .andExpect(view().name("alumni/start"))
                .andExpect(model().attributeExists("alumniRequest"));
    }

    @Test
    public void testStartProcess() throws Exception {
        AlumniRequest alumniRequest = new AlumniRequest();
        alumniRequest.setDocumentType("transcript");
        alumniRequest.setPaymentMethod("remita");

        doNothing().when(alumniProjectService).startProcess(alumniRequest);

        mockMvc.perform(post("/alumni/start")
                        .flashAttr("alumniRequest", alumniRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alumni/tasks"));

        verify(alumniProjectService, times(1)).startProcess(alumniRequest);
    }

    @Test
    public void testGetTasks() throws Exception {
        Map<String, Object> task1 = new HashMap<>();
        task1.put("id", "1");
        task1.put("name", "Submit Document Request");
        task1.put("assignee", "user1");

        Map<String, Object> task2 = new HashMap<>();
        task2.put("id", "2");
        task2.put("name", "Make Payment");
        task2.put("assignee", "user2");

        List<Map<String, Object>> tasks = Arrays.asList(task1, task2);

        when(alumniProjectService.getTasks()).thenReturn(tasks);

        mockMvc.perform(get("/alumni/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("alumni/tasks"))
                .andExpect(model().attributeExists("tasks"));

        verify(alumniProjectService, times(1)).getTasks();
    }

    @Test
    public void testShowCompleteForm() throws Exception {
        Map<String, Object> task = new HashMap<>();
        task.put("id", "1");
        task.put("name", "Submit Document Request");
        task.put("assignee", "user1");

        when(alumniProjectService.getTask("1")).thenReturn(task);

        mockMvc.perform(get("/alumni/complete/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("alumni/complete"))
                .andExpect(model().attributeExists("task"));

        verify(alumniProjectService, times(1)).getTask("1");
    }

    @Test
    public void testCompleteTask() throws Exception {
        Map<String, String> variables = new HashMap<>();
        variables.put("paymentStatus", "success");

        doNothing().when(alumniProjectService).completeTask("1", variables);

        mockMvc.perform(post("/alumni/complete/1")
                        .param("paymentStatus", "success"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alumni/tasks"));

        verify(alumniProjectService, times(1)).completeTask("1", variables);
    }

    @Test
    public void testGetProcessHistory() throws Exception {
        Map<String, Object> history1 = new HashMap<>();
        history1.put("processInstanceId", "1");
        history1.put("status", "Completed");
        history1.put("startTime", "2023-01-01T00:00:00");
        history1.put("endTime", "2023-01-02T00:00:00");

        Map<String, Object> history2 = new HashMap<>();
        history2.put("processInstanceId", "2");
        history2.put("status", "Failed");
        history2.put("startTime", "2023-01-03T00:00:00");
        history2.put("endTime", "2023-01-04T00:00:00");

        List<Map<String, Object>> history = Arrays.asList(history1, history2);

        when(alumniProjectService.getProcessHistory()).thenReturn(history);

        mockMvc.perform(get("/alumni/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("alumni/history"))
                .andExpect(model().attributeExists("history"));

        verify(alumniProjectService, times(1)).getProcessHistory();
    }
}
