package tech.justjava.alumni.controller;

import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AlumniProcessControllerTest {

    @Mock
    private AlumniProcessService alumniProcessService;

    @InjectMocks
    private AlumniProcessController alumniProcessController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(alumniProcessController).build();
    }

    @Test
    public void testDeployProcess() throws Exception {
        doNothing().when(alumniProcessService).deployProcess();

        mockMvc.perform(post("/api/alumni/deploy"))
                .andExpect(status().isOk())
                .andExpect(content().string("Process deployed successfully"));

        verify(alumniProcessService, times(1)).deployProcess();
    }

    @Test
    public void testStartProcess() throws Exception {
        AlumniRequest request = new AlumniRequest();
        request.setDocumentType("transcript");
        request.setPaymentMethod("remita");
        request.setApprover("approver1");

        when(alumniProcessService.startProcess(any(AlumniRequest.class))).thenReturn("processInstanceId");

        mockMvc.perform(post("/api/alumni/start")
                        .contentType("application/json")
                        .content("{\"documentType\":\"transcript\",\"paymentMethod\":\"remita\",\"approver\":\"approver1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Process started with ID: processInstanceId"));

        verify(alumniProcessService, times(1)).startProcess(any(AlumniRequest.class));
    }

    @Test
    public void testGetTasks() throws Exception {
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(alumniProcessService.getTasks(anyString())).thenReturn(tasks);

        mockMvc.perform(get("/api/alumni/tasks")
                        .param("assignee", "approver1"))
                .andExpect(status().isOk());

        verify(alumniProcessService, times(1)).getTasks(anyString());
    }

    @Test
    public void testCompleteTask() throws Exception {
        doNothing().when(alumniProcessService).completeTask(anyString(), anyMap());

        mockMvc.perform(post("/api/alumni/complete")
                        .param("taskId", "taskId")
                        .contentType("application/json")
                        .content("{\"variable\":\"value\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task completed successfully"));

        verify(alumniProcessService, times(1)).completeTask(anyString(), anyMap());
    }
}
