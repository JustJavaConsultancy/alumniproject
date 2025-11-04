package tech.justjava.alumni.service;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.justjava.alumni.entity.AlumniRequest;
import tech.justjava.alumni.repository.AlumniRequestRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlumniProjectService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AlumniRequestRepository alumniRequestRepository;

    public void deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/alumniProject.bpmn20.xml")
                .deploy();
    }

    public void startProcess(AlumniRequest alumniRequest) {
        alumniRequestRepository.save(alumniRequest);

        Map<String, Object> variables = new HashMap<>();
        variables.put("documentType", alumniRequest.getDocumentType());
        variables.put("paymentMethod", alumniRequest.getPaymentMethod());
        variables.put("requestId", alumniRequest.getId());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("alumniProject", variables);
        alumniRequest.setProcessInstanceId(processInstance.getId());
        alumniRequestRepository.save(alumniRequest);
    }

    public List<Map<String, Object>> getTasks() {
        List<Task> tasks = taskService.createTaskQuery().list();
        return tasks.stream().map(task -> {
            Map<String, Object> taskMap = new HashMap<>();
            taskMap.put("id", task.getId());
            taskMap.put("name", task.getName());
            taskMap.put("assignee", task.getAssignee());
            return taskMap;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> getTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("id", task.getId());
        taskMap.put("name", task.getName());
        taskMap.put("assignee", task.getAssignee());
        return taskMap;
    }

    public void completeTask(String taskId, Map<String, String> variables) {
        taskService.complete(taskId, variables);
    }

    public List<Map<String, Object>> getProcessHistory() {
        // Implement process history retrieval logic
        return null;
    }
}
