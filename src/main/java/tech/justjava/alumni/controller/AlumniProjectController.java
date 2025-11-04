package tech.justjava.alumni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProjectService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alumni")
public class AlumniProjectController {

    @Autowired
    private AlumniProjectService alumniProjectService;

    @GetMapping("/deploy")
    public String deployProcess() {
        alumniProjectService.deployProcess();
        return "redirect:/alumni/start";
    }

    @GetMapping("/start")
    public String showStartForm(Model model) {
        model.addAttribute("alumniRequest", new AlumniRequest());
        return "alumni/start";
    }

    @PostMapping("/start")
    public String startProcess(@ModelAttribute AlumniRequest alumniRequest) {
        alumniProjectService.startProcess(alumniRequest);
        return "redirect:/alumni/tasks";
    }

    @GetMapping("/tasks")
    public String getTasks(Model model) {
        List<Map<String, Object>> tasks = alumniProjectService.getTasks();
        model.addAttribute("tasks", tasks);
        return "alumni/tasks";
    }

    @GetMapping("/complete/{taskId}")
    public String showCompleteForm(@PathVariable String taskId, Model model) {
        Map<String, Object> task = alumniProjectService.getTask(taskId);
        model.addAttribute("task", task);
        return "alumni/complete";
    }

    @PostMapping("/complete/{taskId}")
    public String completeTask(@PathVariable String taskId, @RequestParam Map<String, String> variables) {
        alumniProjectService.completeTask(taskId, variables);
        return "redirect:/alumni/tasks";
    }

    @GetMapping("/history")
    public String getProcessHistory(Model model) {
        List<Map<String, Object>> history = alumniProjectService.getProcessHistory();
        model.addAttribute("history", history);
        return "alumni/history";
    }
}
