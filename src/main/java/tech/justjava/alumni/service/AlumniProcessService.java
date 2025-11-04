package tech.justjava.alumni.service;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
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

@Service
@Transactional
public class AlumniProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AlumniRequestRepository alumniRequestRepository;

    public void deployProcess() {
        repositoryService.createDeployment()
                .addClasspathResource("processes/alumniProject.bpmn20.xml")
                .deploy();
    }

    public void startProcess(AlumniRequest alumniRequest) {
        alumniRequestRepository.save(alumniRequest);

        Map<String, Object> variables = new HashMap<>();
        variables.put("alumniRequest", alumniRequest);
        variables.put("approver", "admin"); // Default approver, can be changed based on business logic

        runtimeService.startProcessInstanceByKey("alumniProject", variables);
    }

    public List<Task> getTasks() {
        return taskService.createTaskQuery().list();
    }

    public void completeTask(String taskId, Map<String, String> formData) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (task != null) {
            Map<String, Object> variables = new HashMap<>();

            if ("submitRequest".equals(task.getTaskDefinitionKey())) {
                AlumniRequest alumniRequest = new AlumniRequest();
                alumniRequest.setDocumentType(formData.get("documentType"));
                alumniRequest.setPaymentMethod(formData.get("paymentMethod"));
                alumniRequestRepository.save(alumniRequest);
                variables.put("alumniRequest", alumniRequest);
            } else if ("makePayment".equals(task.getTaskDefinitionKey())) {
                variables.put("paymentCompleted", true);
            } else if ("approveRequest".equals(task.getTaskDefinitionKey())) {
                variables.put("requestApproved", Boolean.parseBoolean(formData.get("approved")));
            }

            taskService.complete(taskId, variables);
        }
    }

    public List<ProcessInstance> getProcessInstances() {
        return runtimeService.createProcessInstanceQuery().list();
    }
}
