package tech.justjava.alumni.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AlumniProcessControllerTest {

    @Mock
    private AlumniProcessService alumniProcessService;

    @InjectMocks
    private AlumniProcessController alumniProcessController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deployProcess() {
        doNothing().when(alumniProcessService).deployProcess();

        ResponseEntity<String> response = alumniProcessController.deployProcess();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Process deployed successfully", response.getBody());
        verify(alumniProcessService, times(1)).deployProcess();
    }

    @Test
    void startProcess() {
        AlumniRequest request = new AlumniRequest();
        request.setDocumentType("transcript");
        request.setPaymentMethod("remita");
        request.setApprover("admin");

        when(alumniProcessService.startProcess(any(AlumniRequest.class))).thenReturn("processInstanceId");

        ResponseEntity<String> response = alumniProcessController.startProcess(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("processInstanceId", response.getBody());
        verify(alumniProcessService, times(1)).startProcess(request);
    }

    @Test
    void getTasks() {
        Map<String, Object> task = new HashMap<>();
        task.put("id", "taskId");
        task.put("name", "Submit Document Request");
        task.put("processInstanceId", "processInstanceId");

        when(alumniProcessService.getTasks("admin")).thenReturn(Collections.singletonList(task));

        ResponseEntity<List<Map<String, Object>>> response = alumniProcessController.getTasks("admin");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("taskId", response.getBody().get(0).get("id"));
        verify(alumniProcessService, times(1)).getTasks("admin");
    }

    @Test
    void completeTask() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approvalStatus", "approved");

        doNothing().when(alumniProcessService).completeTask("taskId", variables);

        ResponseEntity<String> response = alumniProcessController.completeTask("taskId", variables);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task completed successfully", response.getBody());
        verify(alumniProcessService, times(1)).completeTask("taskId", variables);
    }

    @Test
    void getProcessInstances() {
        Map<String, Object> processInstance = new HashMap<>();
        processInstance.put("id", "processInstanceId");
        processInstance.put("processDefinitionId", "alumniProject:1:processDefinitionId");

        when(alumniProcessService.getProcessInstances()).thenReturn(Collections.singletonList(processInstance));

        ResponseEntity<List<Map<String, Object>>> response = alumniProcessController.getProcessInstances();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("processInstanceId", response.getBody().get(0).get("id"));
        verify(alumniProcessService, times(1)).getProcessInstances();
    }
}
