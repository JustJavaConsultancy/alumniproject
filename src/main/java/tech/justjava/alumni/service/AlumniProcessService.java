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
public class AlumniProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AlumniRequestRepository alumniRequestRepository;

    @Transactional
    public String deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/alumniProject.bpmn20.xml")
                .deploy();
        return deployment.getId();
    }

    @Transactional
    public String startProcess(AlumniRequest request) {
        AlumniRequest savedRequest = alumniRequestRepository.save(request);

        Map<String, Object> variables = new HashMap<>();
        variables.put("documentType", savedRequest.getDocumentType());
        variables.put("paymentMethod", savedRequest.getPaymentMethod());
        variables.put("approver", savedRequest.getApprover());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("alumniProject", variables);
        return processInstance.getId();
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

    @Transactional
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    public List<Map<String, Object>> getProcessInstances(String processKey) {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processKey).list();
        return processInstances.stream().map(processInstance -> {
            Map<String, Object> instanceInfo = new HashMap<>();
            instanceInfo.put("id", processInstance.getId());
            instanceInfo.put("processDefinitionId", processInstance.getProcessDefinitionId());
            instanceInfo.put("startTime", processInstance.getStartTime());
            return instanceInfo;
        }).collect(Collectors.toList());
    }
}
