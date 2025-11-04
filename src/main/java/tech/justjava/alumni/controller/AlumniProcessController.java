package tech.justjava.alumni.controller;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumni")
public class AlumniProcessController {

    @Autowired
    private AlumniProcessService alumniProcessService;

    @PostMapping("/deploy")
    public String deployProcess() {
        alumniProcessService.deployProcess();
        return "Process deployed successfully";
    }

    @PostMapping("/start")
    public String startProcess(@RequestBody AlumniRequest request) {
        String processInstanceId = alumniProcessService.startProcess(request);
        return "Process started with ID: " + processInstanceId;
    }

    @GetMapping("/tasks")
    public List<Task> getTasks(@RequestParam String assignee) {
        return alumniProcessService.getTasks(assignee);
    }

    @PostMapping("/complete")
    public String completeTask(@RequestParam String taskId, @RequestBody Map<String, Object> variables) {
        alumniProcessService.completeTask(taskId, variables);
        return "Task completed successfully";
    }
}
