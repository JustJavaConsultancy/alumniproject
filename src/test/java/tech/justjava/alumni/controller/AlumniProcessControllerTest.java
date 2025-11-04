package tech.justjava.alumni.controller;

import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AlumniProcessControllerTest {

    @Mock
    private AlumniProcessService alumniProcessService;

    @Mock
    private Model model;

    @InjectMocks
    private AlumniProcessController alumniProcessController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStartProcessForm() {
        String viewName = alumniProcessController.startProcessForm(model);
        assertEquals("alumniProcess/startProcess", viewName);
        verify(model).addAttribute(eq("processKey"), eq("alumniProject"));
    }

    @Test
    public void testStartProcess() {
        String processKey = "alumniProject";
        Map<String, Object> variables = new HashMap<>();
        variables.put("documentType", "transcript");
        variables.put("paymentMethod", "remita");

        String viewName = alumniProcessController.startProcess(processKey, variables);
        assertEquals("redirect:/alumni-process/tasks", viewName);
        verify(alumniProcessService).startProcess(processKey, variables);
    }

    @Test
    public void testGetTasks() {
        List<Task> tasks = Arrays.asList(mock(Task.class));
        when(alumniProcessService.getTasks()).thenReturn(tasks);

        String viewName = alumniProcessController.getTasks(model);
        assertEquals("alumniProcess/tasks", viewName);
        verify(model).addAttribute(eq("tasks"), eq(tasks));
    }

    @Test
    public void testGetTaskForm() {
        String taskId = "1";
        Task task = mock(Task.class);
        when(alumniProcessService.getTask(taskId)).thenReturn(task);

        String viewName = alumniProcessController.getTaskForm(taskId, model);
        assertEquals("alumniProcess/taskForm", viewName);
        verify(model).addAttribute(eq("task"), eq(task));
    }

    @Test
    public void testCompleteTask() {
        String taskId = "1";
        Map<String, Object> variables = new HashMap<>();
        variables.put("approval", "approved");

        String viewName = alumniProcessController.completeTask(taskId, variables);
        assertEquals("redirect:/alumni-process/tasks", viewName);
        verify(alumniProcessService).completeTask(taskId, variables);
    }
}
