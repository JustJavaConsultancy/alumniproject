package tech.justjava.alumni.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AlumniProcessControllerTest {

    @Mock
    private AlumniProcessService alumniProcessService;

    @InjectMocks
    private AlumniProcessController alumniProcessController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(alumniProcessController).build();
    }

    @Test
    public void testDeployProcess() throws Exception {
        when(alumniProcessService.deployProcess()).thenReturn("deploymentId");

        mockMvc.perform(post("/api/alumni/deploy"))
                .andExpect(status().isOk())
                .andExpect(content().string("deploymentId"));
    }

    @Test
    public void testStartProcess() throws Exception {
        AlumniRequest request = new AlumniRequest();
        request.setDocumentType("transcript");
        request.setPaymentMethod("remita");
        request.setApprover("approver");

        when(alumniProcessService.startProcess(any(AlumniRequest.class))).thenReturn("processInstanceId");

        mockMvc.perform(post("/api/alumni/start")
                        .contentType("application/json")
                        .content("{\"documentType\":\"transcript\",\"paymentMethod\":\"remita\",\"approver\":\"approver\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("processInstanceId"));
    }

    @Test
    public void testGetTasks() throws Exception {
        Map<String, Object> task1 = new HashMap<>();
        task1.put("id", "task1");
        task1.put("name", "Submit Document Request");
        task1.put("processInstanceId", "process1");

        Map<String, Object> task2 = new HashMap<>();
        task2.put("id", "task2");
        task2.put("name", "Make Payment");
        task2.put("processInstanceId", "process2");

        List<Map<String, Object>> tasks = Arrays.asList(task1, task2);

        when(alumniProcessService.getTasks("approver")).thenReturn(tasks);

        mockMvc.perform(get("/api/alumni/tasks")
                        .param("assignee", "approver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("task1"))
                .andExpect(jsonPath("$[0].name").value("Submit Document Request"))
                .andExpect(jsonPath("$[0].processInstanceId").value("process1"))
                .andExpect(jsonPath("$[1].id").value("task2"))
                .andExpect(jsonPath("$[1].name").value("Make Payment"))
                .andExpect(jsonPath("$[1].processInstanceId").value("process2"));
    }

    @Test
    public void testCompleteTask() throws Exception {
        mockMvc.perform(post("/api/alumni/complete")
                        .param("taskId", "task1")
                        .contentType("application/json")
                        .content("{\"paymentReference\":\"ref123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProcessInstances() throws Exception {
        Map<String, Object> instance1 = new HashMap<>();
        instance1.put("id", "instance1");
        instance1.put("processDefinitionId", "def1");
        instance1.put("startTime", "2023-01-01T00:00:00");

        Map<String, Object> instance2 = new HashMap<>();
        instance2.put("id", "instance2");
        instance2.put("processDefinitionId", "def2");
        instance2.put("startTime", "2023-01-02T00:00:00");

        List<Map<String, Object>> instances = Arrays.asList(instance1, instance2);

        when(alumniProcessService.getProcessInstances("alumniProject")).thenReturn(instances);

        mockMvc.perform(get("/api/alumni/process-instances")
                        .param("processKey", "alumniProject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("instance1"))
                .andExpect(jsonPath("$[0].processDefinitionId").value("def1"))
                .andExpect(jsonPath("$[0].startTime").value("2023-01-01T00:00:00"))
                .andExpect(jsonPath("$[1].id").value("instance2"))
                .andExpect(jsonPath("$[1].processDefinitionId").value("def2"))
                .andExpect(jsonPath("$[1].startTime").value("2023-01-02T00:00:00"));
    }
}
