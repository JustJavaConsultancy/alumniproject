package tech.justjava.alumni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.Map;

@Controller
@RequestMapping("/alumni-process")
public class AlumniProcessController {

    @Autowired
    private AlumniProcessService alumniProcessService;

    @GetMapping("/start")
    public String startProcessForm(Model model) {
        model.addAttribute("processKey", "alumniProject");
        return "alumniProcess/startProcess";
    }

    @PostMapping("/start")
    public String startProcess(@RequestParam String processKey, @RequestParam Map<String, Object> variables) {
        alumniProcessService.startProcess(processKey, variables);
        return "redirect:/alumni-process/tasks";
    }

    @GetMapping("/tasks")
    public String getTasks(Model model) {
        model.addAttribute("tasks", alumniProcessService.getTasks());
        return "alumniProcess/tasks";
    }

    @GetMapping("/task/{taskId}")
    public String getTaskForm(@PathVariable String taskId, Model model) {
        model.addAttribute("task", alumniProcessService.getTask(taskId));
        return "alumniProcess/taskForm";
    }

    @PostMapping("/task/complete")
    public String completeTask(@RequestParam String taskId, @RequestParam Map<String, Object> variables) {
        alumniProcessService.completeTask(taskId, variables);
        return "redirect:/alumni-process/tasks";
    }
}
