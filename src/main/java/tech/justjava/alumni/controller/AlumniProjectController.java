package tech.justjava.alumni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProjectService;

import java.util.List;

@Controller
@RequestMapping("/alumni")
public class AlumniProjectController {

    @Autowired
    private AlumniProjectService alumniProjectService;

    @GetMapping("/deploy")
    public String deployProcess(Model model) {
        try {
            alumniProjectService.deployProcess();
            model.addAttribute("message", "Process deployed successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to deploy process: " + e.getMessage());
        }
        return "deployResult";
    }

    @GetMapping("/start")
    public String showStartForm(Model model) {
        model.addAttribute("alumniRequest", new AlumniRequest());
        return "startProcess";
    }

    @PostMapping("/start")
    public String startProcess(@ModelAttribute AlumniRequest alumniRequest, Model model) {
        try {
            alumniProjectService.startProcess(alumniRequest);
            model.addAttribute("message", "Process started successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to start process: " + e.getMessage());
        }
        return "processResult";
    }

    @GetMapping("/tasks")
    public String listTasks(Model model) {
        List<AlumniRequest> tasks = alumniProjectService.getTasks();
        model.addAttribute("tasks", tasks);
        return "taskList";
    }

    @GetMapping("/task/{taskId}")
    public String showTaskForm(@PathVariable String taskId, Model model) {
        AlumniRequest task = alumniProjectService.getTask(taskId);
        model.addAttribute("task", task);
        return "taskForm";
    }

    @PostMapping("/task/{taskId}")
    public String completeTask(@PathVariable String taskId, @ModelAttribute AlumniRequest task, Model model) {
        try {
            alumniProjectService.completeTask(taskId, task);
            model.addAttribute("message", "Task completed successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to complete task: " + e.getMessage());
        }
        return "taskResult";
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        List<AlumniRequest> history = alumniProjectService.getProcessHistory();
        model.addAttribute("history", history);
        return "processHistory";
    }
}
