package com.softline.csrv.app.support;

import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.enums.StatusModelCode;
import io.jmix.bpm.data.form.FormData;
import io.jmix.bpm.data.form.FormOutcome;
import io.jmix.bpm.processform.ProcessFormDataExtractor;
import io.jmix.bpm.service.BpmTaskService;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import org.apache.commons.compress.utils.Lists;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service
@Component(RequestServiceBPM.NAME)
public class RequestServiceBPM {
    public static final String NAME = "support_RequestServiceBPM";

    @Autowired
    private RepositoryService repositoryService;

    private final CurrentAuthentication currentAuthentication;
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final ProcessFormDataExtractor processFormDataExtractor;
    private final MdmService mdmService;

    @Autowired
    private BpmTaskService bpmTaskService;
    @Autowired
    private SystemAuthenticator systemAuthenticator;
    @Autowired
    private Messages messages;






    @Autowired
    public RequestServiceBPM(CurrentAuthentication currentAuthentication,
                             TaskService taskService,
                             RuntimeService runtimeService,
                             ProcessFormDataExtractor processFormDataExtractor,
                             RepositoryService repositoryService,
                             MdmService mdmService
    ) {

        this.currentAuthentication = currentAuthentication;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.processFormDataExtractor = processFormDataExtractor;
        this.mdmService = mdmService;
    }


    private final Logger log = LoggerFactory.getLogger(RequestServiceBPM.class.getName());

    @Autowired
    private DataManager dataManager;


    @Autowired
    private RequestService requestService;

    /**
     * Запущен ли процесс по Заявке
     *
     * @param request Заявка
     */
    public boolean isProcessRunOnRequest(Request request) {
        if (!Objects.isNull(request)) {
            StatusModel statusModel = getProcessKeyToStart(request);

            if (!Objects.isNull(statusModel)) {


                // Ищем ProcessInstance с ЖЦ = statusModel.getCode()
                // и variable с наименованием "request" = request
                int i = runtimeService.createProcessInstanceQuery()
                        .processDefinitionKey(statusModel.getCode())
                        .processInstanceBusinessKey(request.getKeyNum())
                        //.variableValueEquals(BpmVariableCode.REQUEST.getCode(), request)
                        .active()
                        .list().size();
/*                int i = runtimeService.createProcessInstanceQuery()
                        .processInstanceBusinessKey(request.getKeyNum())
                        .variableValueEquals(BpmVariableCode.REQUEST.getCode(), request)
                        .active()
                        .list().size();       */

                return i > 0;
            }
        }
        return false;
    }

    /**
     * Возвращает Текущую задачу по ЖЦ по Заявке
     *
     * @param request Заявка
     */
    public Task getProcessCurrentTask(Request request) {
        List<Task> tasks = getProcessTasks(request);
        return CollectionUtils.isEmpty(tasks) ? null :
                tasks.get(0);
    }

    /**
     * Возвращает Код ЖЦ по Заявке
     *
     * @param request Заявка
     */
    public StatusModel getProcessKeyToStart(Request request) {
        Objects.requireNonNull(request, "Request cannot be null");

            if (RequestTypeCode.IS_VERSION.getCode().equals(request.getRequestType().getCode())) {
                // Если ВИС
                Function function = request.getFunction();
                if (Objects.nonNull(function)) {
                    InfoSystem infoSystem = function.getSystem();
                    if (Objects.nonNull(infoSystem)) {
                        StatusModel statusModel = requestService.getStatusModelByParams(request.getRequestType(), infoSystem);
                        if (Objects.nonNull(statusModel)) {
                            return statusModel;
                        }
                    }
                }
            }
        return requestService.getStatusModelByParams(request.getRequestType());
    }

    public StatusModel getRunningProcessKey(Request request) {
        Objects.requireNonNull(request, "Request not found");

        StatusModel statusModel = getProcessKeyToStart(request);

                // Ищем ProcessInstance с ЖЦ = statusModel.getCode()
                // и BusinessKey = request.getKeyNum
                List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
                        .processDefinitionKey(statusModel.getCode())
                        .processInstanceBusinessKey(request.getKeyNum())
                        .active()
                        .list();

        Assert.isFalse(processInstanceList.size() > 1, " Request has more then 1 running processInstance");
        return statusModel;
    }

    /**
     * Запускаем процесс по Заявке
     *
     * @param request Заявка
     */
    public boolean runProcess(Request request) {

        StatusModel statusModel = getProcessKeyToStart(request);

        // запускаем процесс
        return runProcess(request, statusModel);
    }

    /**
     * Запускаем процесс по Заявке
     *
     * @param request     Заявка
     * @param statusModel ЖЦ
     */
    private boolean runProcess(Request request, StatusModel statusModel) {
        if (Objects.nonNull(request) && Objects.nonNull(statusModel)) {
            if (!isProcessRunOnRequest(request)) {
                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put(BpmVariableCode.REQUEST.getCode(), request);

                ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(statusModel.getCode(), request.getKeyNum(), variables);
                if (Objects.nonNull(processInstance)) {
                    runtimeService.updateBusinessKey(processInstance.getProcessInstanceId(), request.getKeyNum());

                    log.info("[{}] Successfully started wf-process ({})", request.getKeyNum(), statusModel.getName());
                    log.info("[{}] ...with ProcessInstanceId={}, processDefinitionId={}", request.getKeyNum(), processInstance.getProcessInstanceId(), processInstance.getProcessDefinitionId());
                    return true;
                }
            } else {
                String msg = String.format(messages.getMessage("processAlreadyRunning"), request.getKeyNum(), statusModel.getCode());
                log.error(msg);
            }
        }
        return false;
    }
    /**
     * Завершает текущую задачу в WF с outcome
     *
     * @param request   Заявка
     * @param outcome   Результат
     */

    public void completeTask(Request request, FormOutcome outcome) {
        Map<String, Object> processVariables = getDefaultVariables(request, outcome);
        completeTask(request, outcome, processVariables);
    }

    public Map<String, Object> getDefaultVariables(Request request, FormOutcome outcome) {
        Map<String, Object> processVariables = new HashMap<String, Object>();

        processVariables.put(BpmVariableCode.REQUEST.getCode(), request);
        processVariables.put(BpmVariableCode.STATUS_TO.getCode(), outcome.getId());

        return processVariables;
    }
    public void completeTask(Request request, FormOutcome outcome, Map<String, Object> processVariables) {
        Objects.requireNonNull(request, "Request not found");
        Objects.requireNonNull(outcome, "FormOutcome not found");

        boolean isCurrentAuth = currentAuthentication.isSet();

        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }

        Task task = getProcessCurrentTask(request);
        if (Objects.nonNull(task)) {

            log.info("[{}] completeTask, task={}, with outcome={}", request.getKeyNum(), task.getName(), outcome.getId());
            bpmTaskService.completeTaskWithOutcome(task.getId(), outcome.getId(), processVariables);

        } else {
            log.warn("[{}] No task found", request.getKeyNum());
        }
        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }
    }

    /**
     * возвращает outcome-ы по текущей Task по ЖЦ процесса по Заявке
     *
     * @param request Заявка
     */
    public List<FormOutcome> getFormOutcomesForTask(Request request) {
        Objects.requireNonNull(request, "Request not found");
        Task task = getProcessCurrentTask(request);

        if (Objects.isNull(task)) {
            return Lists.newArrayList();
        }
        return Optional.ofNullable(processFormDataExtractor.getTaskFormData(task.getId()))
                .map(FormData::getOutcomes)
                .orElse(Lists.newArrayList());
    }


    private List<Task> getProcessTasks(Request request) {
        Objects.requireNonNull(request, "Request not found");

            return taskService.createTaskQuery()
                    .processInstanceBusinessKey(request.getKeyNum())
                    .active()
                    .list();
    }

    public ProcessInstance getProcessInstance(Request request) {
        Objects.requireNonNull(request, "Request not found");
        List<ProcessInstance> list = Lists.newArrayList();

        list = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(request.getKeyNum())
                .active()
                .list();
        if (Objects.nonNull(list) && list.size() == 1) {
            return list.stream().findFirst().orElse(null);
        }
        return null;
    }



    @Transactional
    @ManagedOperation
    public void updateRequestStatusBpm(Request request, RequestStatusCode statusCode) {

        Objects.requireNonNull(request, "Request not found");
        Objects.requireNonNull(statusCode, "Status not found");
        boolean isCurrentAuth = currentAuthentication.isSet();

        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }

        Request currentRequest = requestService.getRequestById(request.getId());
        if (Objects.nonNull(currentRequest)) {
            if (Objects.nonNull(statusCode)) {
                log.info("[{}] updateRequestStatusBpm {}", currentRequest.getKeyNum(), statusCode.getCode());

                currentRequest.setStatus(mdmService.getStatusByCode(statusCode.getCode()));
                dataManager.save(currentRequest);

            } else {
                log.error("[{}] updateRequestStatusBpm: statusCode is null", currentRequest.getKeyNum());
            }
        } else {
            log.error("updateRequestStatusBpm: request is null");
        }

        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }
    }

    public void completeTask(List<Request> list, RequestStatusCode withOutcome) {
        Request request;
        boolean isCurrentAuth = currentAuthentication.isSet();

        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }



        // Завершаем задачу по Заявкам с withOutcome
        io.jmix.bpm.data.form.FormOutcome fo = new io.jmix.bpm.data.form.FormOutcome();
        fo.setId(withOutcome.getCode());
        fo.setCaption(withOutcome.getCode());

        for (Request agreement : list) {
            //request = dataManager.load(Request.class).id(agreement.getId()).one();
            if (!withOutcome.getCode().equals(agreement.getStatus().getCode())) {
                log.info("...complete current task for {} with {}", agreement.getKeyNum(), withOutcome.getCode());
                completeTask(agreement, fo);
            }
        }
        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }
    }
    public boolean isCanReopen(Request request) {

        StatusModel statusModel = getProcessKeyToStart(request);
        Date byDate = new Date();
        if (Objects.nonNull(statusModel)) {
            return dataManager.load(StatusModelToReopen.class)
                    .query("select c from StatusModelToReopen c where c.statusModel = :statusModel and c.status = :status and :byDate between c.startDate and c.endDate")
                    .parameter("statusModel", statusModel)
                    .parameter("status", request.getStatus())
                    .parameter("byDate", byDate)
                    .list().size() == 1;
        }
        return false;
    }


}


