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
import tech.justjava.alumni.repository.AlumniRequestRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlumniProjectService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AlumniRequestRepository alumniRequestRepository;

    @Transactional
    public void deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/alumniProject.bpmn20.xml")
                .name("Alumni Document Request Process")
                .deploy();
    }

    @Transactional
    public void startProcess(AlumniRequest alumniRequest) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("documentType", alumniRequest.getDocumentType());
        variables.put("paymentMethod", alumniRequest.getPaymentMethod());

        runtimeService.startProcessInstanceByKey("alumniProject", variables);

        alumniRequest.setStatus("Started");
        alumniRequestRepository.save(alumniRequest);
    }

    public List<AlumniRequest> getTasks() {
        List<Task> tasks = taskService.createTaskQuery().list();
        return tasks.stream()
                .map(task -> {
                    AlumniRequest alumniRequest = new AlumniRequest();
                    alumniRequest.setId(task.getId());
                    alumniRequest.setName(task.getName());
                    alumniRequest.setAssignee(task.getAssignee());
                    return alumniRequest;
                })
                .collect(Collectors.toList());
    }

    public AlumniRequest getTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        AlumniRequest alumniRequest = new AlumniRequest();
        alumniRequest.setId(task.getId());
        alumniRequest.setName(task.getName());
        alumniRequest.setAssignee(task.getAssignee());
        return alumniRequest;
    }

    @Transactional
    public void completeTask(String taskId, AlumniRequest task) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("documentType", task.getDocumentType());
        variables.put("paymentMethod", task.getPaymentMethod());
        variables.put("paymentVerified", task.getPaymentVerified());
        variables.put("requestApproved", task.getRequestApproved());

        taskService.complete(taskId, variables);

        AlumniRequest alumniRequest = alumniRequestRepository.findById(taskId).orElse(new AlumniRequest());
        alumniRequest.setStatus("Completed");
        alumniRequestRepository.save(alumniRequest);
    }

    public List<AlumniRequest> getProcessHistory() {
        return alumniRequestRepository.findAll();
    }
}
