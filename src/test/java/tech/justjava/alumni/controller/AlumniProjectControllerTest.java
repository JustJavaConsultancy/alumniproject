package tech.justjava.alumni.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProjectService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AlumniProjectControllerTest {

    @Mock
    private AlumniProjectService alumniProjectService;

    @Mock
    private Model model;

    @InjectMocks
    private AlumniProjectController alumniProjectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deployProcess_Success() {
        doNothing().when(alumniProjectService).deployProcess();

        String viewName = alumniProjectController.deployProcess(model);

        assertEquals("deployResult", viewName);
        verify(alumniProjectService, times(1)).deployProcess();
        verify(model, times(1)).addAttribute("message", "Process deployed successfully");
    }

    @Test
    void deployProcess_Failure() {
        doThrow(new RuntimeException("Deployment failed")).when(alumniProjectService).deployProcess();

        String viewName = alumniProjectController.deployProcess(model);

        assertEquals("deployResult", viewName);
        verify(alumniProjectService, times(1)).deployProcess();
        verify(model, times(1)).addAttribute("error", "Failed to deploy process: Deployment failed");
    }

    @Test
    void showStartForm() {
        String viewName = alumniProjectController.showStartForm(model);

        assertEquals("startProcess", viewName);
        verify(model, times(1)).addAttribute(eq("alumniRequest"), any(AlumniRequest.class));
    }

    @Test
    void startProcess_Success() {
        AlumniRequest alumniRequest = new AlumniRequest();
        doNothing().when(alumniProjectService).startProcess(alumniRequest);

        String viewName = alumniProjectController.startProcess(alumniRequest, model);

        assertEquals("processResult", viewName);
        verify(alumniProjectService, times(1)).startProcess(alumniRequest);
        verify(model, times(1)).addAttribute("message", "Process started successfully");
    }

    @Test
    void startProcess_Failure() {
        AlumniRequest alumniRequest = new AlumniRequest();
        doThrow(new RuntimeException("Process start failed")).when(alumniProjectService).startProcess(alumniRequest);

        String viewName = alumniProjectController.startProcess(alumniRequest, model);

        assertEquals("processResult", viewName);
        verify(alumniProjectService, times(1)).startProcess(alumniRequest);
        verify(model, times(1)).addAttribute("error", "Failed to start process: Process start failed");
    }

    @Test
    void listTasks() {
        List<AlumniRequest> tasks = Arrays.asList(new AlumniRequest(), new AlumniRequest());
        when(alumniProjectService.getTasks()).thenReturn(tasks);

        String viewName = alumniProjectController.listTasks(model);

        assertEquals("taskList", viewName);
        verify(alumniProjectService, times(1)).getTasks();
        verify(model, times(1)).addAttribute("tasks", tasks);
    }

    @Test
    void showTaskForm() {
        String taskId = "task123";
        AlumniRequest task = new AlumniRequest();
        when(alumniProjectService.getTask(taskId)).thenReturn(task);

        String viewName = alumniProjectController.showTaskForm(taskId, model);

        assertEquals("taskForm", viewName);
        verify(alumniProjectService, times(1)).getTask(taskId);
        verify(model, times(1)).addAttribute("task", task);
    }

    @Test
    void completeTask_Success() {
        String taskId = "task123";
        AlumniRequest task = new AlumniRequest();
        doNothing().when(alumniProjectService).completeTask(taskId, task);

        String viewName = alumniProjectController.completeTask(taskId, task, model);

        assertEquals("taskResult", viewName);
        verify(alumniProjectService, times(1)).completeTask(taskId, task);
        verify(model, times(1)).addAttribute("message", "Task completed successfully");
    }

    @Test
    void completeTask_Failure() {
        String taskId = "task123";
        AlumniRequest task = new AlumniRequest();
        doThrow(new RuntimeException("Task completion failed")).when(alumniProjectService).completeTask(taskId, task);

        String viewName = alumniProjectController.completeTask(taskId, task, model);

        assertEquals("taskResult", viewName);
        verify(alumniProjectService, times(1)).completeTask(taskId, task);
        verify(model, times(1)).addAttribute("error", "Failed to complete task: Task completion failed");
    }

    @Test
    void showHistory() {
        List<AlumniRequest> history = Arrays.asList(new AlumniRequest(), new AlumniRequest());
        when(alumniProjectService.getProcessHistory()).thenReturn(history);

        String viewName = alumniProjectController.showHistory(model);

        assertEquals("processHistory", viewName);
        verify(alumniProjectService, times(1)).getProcessHistory();
        verify(model, times(1)).addAttribute("history", history);
    }
}
