package tech.justjava.alumni.service;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.justjava.alumni.entity.AlumniRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlumniProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    public void deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/alumniProject.bpmn20.xml")
                .name("Alumni Document Request Process")
                .deploy();
    }

    public String startProcess(AlumniRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("documentType", request.getDocumentType());
        variables.put("paymentMethod", request.getPaymentMethod());
        variables.put("approver", request.getApprover());

        return runtimeService.startProcessInstanceByKey("alumniProject", variables).getId();
    }

    public List<Map<String, Object>> getTasks(String assignee) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        return tasks.stream().map(task -> {
            Map<String, Object> taskInfo = new HashMap<>();
            taskInfo.put("id", task.getId());
            taskInfo.put("name", task.getName());
            taskInfo.put("processInstanceId", task.getProcessInstanceId());
            return taskInfo;
        }).collect(Collectors.toList());
    }

    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    public List<Map<String, Object>> getProcessInstances() {
        return runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("alumniProject")
                .list()
                .stream()
                .map(instance -> {
                    Map<String, Object> instanceInfo = new HashMap<>();
                    instanceInfo.put("id", instance.getId());
                    instanceInfo.put("processDefinitionId", instance.getProcessDefinitionId());
                    instanceInfo.put("startTime", instance.getStartTime());
                    return instanceInfo;
                })
                .collect(Collectors.toList());
    }
}
