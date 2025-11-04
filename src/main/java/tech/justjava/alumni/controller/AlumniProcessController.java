package tech.justjava.alumni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumni")
public class AlumniProcessController {

    @Autowired
    private AlumniProcessService alumniProcessService;

    @PostMapping("/deploy")
    public ResponseEntity<String> deployProcess() {
        alumniProcessService.deployProcess();
        return ResponseEntity.ok("Process deployed successfully");
    }

    @PostMapping("/start")
    public ResponseEntity<String> startProcess(@RequestBody AlumniRequest request) {
        String processInstanceId = alumniProcessService.startProcess(request);
        return ResponseEntity.ok(processInstanceId);
    }

    @GetMapping("/tasks/{assignee}")
    public ResponseEntity<List<Map<String, Object>>> getTasks(@PathVariable String assignee) {
        List<Map<String, Object>> tasks = alumniProcessService.getTasks(assignee);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/complete/{taskId}")
    public ResponseEntity<String> completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        alumniProcessService.completeTask(taskId, variables);
        return ResponseEntity.ok("Task completed successfully");
    }

    @GetMapping("/process-instances")
    public ResponseEntity<List<Map<String, Object>>> getProcessInstances() {
        List<Map<String, Object>> processInstances = alumniProcessService.getProcessInstances();
        return ResponseEntity.ok(processInstances);
    }
}
