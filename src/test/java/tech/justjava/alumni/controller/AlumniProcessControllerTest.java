package tech.justjava.alumni.controller;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AlumniProcessControllerTest {

    @Mock
    private AlumniProcessService alumniProcessService;

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private TaskService taskService;

    @Mock
    private Model model;

    @InjectMocks
    private AlumniProcessController alumniProcessController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showStartForm() {
        String viewName = alumniProcessController.showStartForm(model);
        assertEquals("alumni/start", viewName);
        verify(model).addAttribute(eq("alumniRequest"), any(AlumniRequest.class));
    }

    @Test
    void startProcess() {
        AlumniRequest alumniRequest = new AlumniRequest();
        String viewName = alumniProcessController.startProcess(alumniRequest);
        assertEquals("redirect:/alumni/tasks", viewName);
        verify(alumniProcessService).startProcess(alumniRequest);
    }

    @Test
    void showTasks() {
        List<Task> tasks = Arrays.asList(mock(Task.class), mock(Task.class));
        when(alumniProcessService.getTasks()).thenReturn(tasks);

        String viewName = alumniProcessController.showTasks(model);
        assertEquals("alumni/tasks", viewName);
        verify(model).addAttribute("tasks", tasks);
    }

    @Test
    void showTaskForm() {
        String taskId = "1";
        Task task = mock(Task.class);
        when(taskService.createTaskQuery()).thenReturn(mock(TaskService.TaskServiceQuery.class));
        when(taskService.createTaskQuery().taskId(taskId)).thenReturn(mock(TaskService.TaskServiceQuery.class));
        when(taskService.createTaskQuery().taskId(taskId).singleResult()).thenReturn(task);

        when(task.getTaskDefinitionKey()).thenReturn("submitRequest");

        String viewName = alumniProcessController.showTaskForm(taskId, model);
        assertEquals("alumni/submitRequest", viewName);
        verify(model).addAttribute("task", task);
        verify(model).addAttribute("taskId", taskId);
    }

    @Test
    void completeTask() {
        String taskId = "1";
        String viewName = alumniProcessController.completeTask(taskId, null);
        assertEquals("redirect:/alumni/tasks", viewName);
        verify(alumniProcessService).completeTask(taskId, null);
    }

    @Test
    void showProcesses() {
        List<ProcessInstance> processInstances = Arrays.asList(mock(ProcessInstance.class), mock(ProcessInstance.class));
        when(alumniProcessService.getProcessInstances()).thenReturn(processInstances);

        String viewName = alumniProcessController.showProcesses(model);
        assertEquals("alumni/processes", viewName);
        verify(model).addAttribute("processInstances", processInstances);
    }
}
