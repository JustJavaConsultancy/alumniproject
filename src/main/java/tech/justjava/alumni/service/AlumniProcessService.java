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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .deploy();
    }

    public String startProcess(AlumniRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("documentType", request.getDocumentType());
        variables.put("paymentMethod", request.getPaymentMethod());
        variables.put("approver", request.getApprover());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("alumniProject", variables);
        return processInstance.getId();
    }

    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
    }

    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }
}
