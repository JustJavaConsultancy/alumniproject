package tech.justjava.alumni.controller;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alumni")
public class AlumniProcessController {

    @Autowired
    private AlumniProcessService alumniProcessService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/start")
    public String showStartForm(Model model) {
        model.addAttribute("alumniRequest", new AlumniRequest());
        return "alumni/start";
    }

    @PostMapping("/start")
    public String startProcess(@ModelAttribute AlumniRequest alumniRequest) {
        alumniProcessService.startProcess(alumniRequest);
        return "redirect:/alumni/tasks";
    }

    @GetMapping("/tasks")
    public String showTasks(Model model) {
        List<Task> tasks = alumniProcessService.getTasks();
        model.addAttribute("tasks", tasks);
        return "alumni/tasks";
    }

    @GetMapping("/task/{taskId}")
    public String showTaskForm(@PathVariable String taskId, Model model) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        model.addAttribute("task", task);
        model.addAttribute("taskId", taskId);

        if ("submitRequest".equals(task.getTaskDefinitionKey())) {
            model.addAttribute("alumniRequest", new AlumniRequest());
            return "alumni/submitRequest";
        } else if ("makePayment".equals(task.getTaskDefinitionKey())) {
            return "alumni/makePayment";
        } else if ("approveRequest".equals(task.getTaskDefinitionKey())) {
            return "alumni/approveRequest";
        }

        return "redirect:/alumni/tasks";
    }

    @PostMapping("/task/{taskId}/complete")
    public String completeTask(@PathVariable String taskId, @RequestParam Map<String, String> formData) {
        alumniProcessService.completeTask(taskId, formData);
        return "redirect:/alumni/tasks";
    }

    @GetMapping("/processes")
    public String showProcesses(Model model) {
        List<ProcessInstance> processInstances = alumniProcessService.getProcessInstances();
        model.addAttribute("processInstances", processInstances);
        return "alumni/processes";
    }
}
