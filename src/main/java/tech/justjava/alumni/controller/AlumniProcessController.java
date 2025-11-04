package tech.justjava.alumni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.justjava.alumni.service.AlumniProcessService;
import tech.justjava.alumni.entity.AlumniRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumni")
public class AlumniProcessController {

    @Autowired
    private AlumniProcessService alumniProcessService;

    @PostMapping("/deploy")
    public String deployProcess() {
        return alumniProcessService.deployProcess();
    }

    @PostMapping("/start")
    public String startProcess(@RequestBody AlumniRequest request) {
        return alumniProcessService.startProcess(request);
    }

    @GetMapping("/tasks")
    public List<Map<String, Object>> getTasks(@RequestParam String assignee) {
        return alumniProcessService.getTasks(assignee);
    }

    @PostMapping("/complete")
    public void completeTask(@RequestParam String taskId, @RequestBody Map<String, Object> variables) {
        alumniProcessService.completeTask(taskId, variables);
    }

    @GetMapping("/process-instances")
    public List<Map<String, Object>> getProcessInstances(@RequestParam String processKey) {
        return alumniProcessService.getProcessInstances(processKey);
    }
}
