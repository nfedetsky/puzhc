package com.softline.csrv.app.support;

import com.google.common.collect.ImmutableSet;
import com.softline.csrv.app.support.clonerequest.CloneRequestService;
import com.softline.csrv.app.support.linkedrequest.LinkedRequestLocator;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.searchassignee.RequestAssigneeSearch;
import com.softline.csrv.app.transition.setattribute.SetAttributeService;
import com.softline.csrv.config.AppConfig;
import com.softline.csrv.entity.Process;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.*;
import io.jmix.core.*;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;
import io.jmix.ui.navigation.UrlIdSerializer;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;
import static com.softline.csrv.enums.RequestStatusCode.CLOSED;
import static com.softline.csrv.enums.RequestStatusCode.REJECTED;



@Service(RequestService.NAME)
public class RequestService {
    public static final String NAME = "support_RequestService";
    private static final String DEFUALT_STATUS_STYLE_NAME = "fklis-status-style0";
    public static final String URL_TEMPLATE = "%s/#main/request?id=%s";
    public static final String LINK_URL_TEMPLATE = "<a href=\"%s\">%s</a>";
    public static final Set<String> notCloneableType = new HashSet<>
            (Arrays.asList("REQUEST_FOR_IMPACT_ASSESSMENT",
                    "CONTENT_AGREEMENT",
                    "REQUEST_FOR_ANALYSIS",
                    "AGREEMENT"));
    private final Logger log = LoggerFactory.getLogger(RequestService.class.getName());

    @Autowired
    private DataManager dataManager;
    @Autowired
    private SystemAuthenticator systemAuthenticator;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private RequestServiceBPM requestServiceBPM;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private RequestAssigneeSearch requestAssigneeSearch;
    @Autowired
    private NotificateService notificateService;
    @Autowired
    private LinkedRequestLocator linkedRequestLocator;
    @Autowired
    private SetAttributeService setAttributeService;
    @Autowired
    private MdmService mdmService;

    @Autowired
    private CloneRequestService cloneRequestService;
    @Autowired
    private Sequences sequences;
    @Autowired
    protected FetchPlanRepository fetchPlanRepository;



    /**
     * Получить Заявку
     *
     * @param requestId идентификатор заявки
     */
    @ManagedOperation
    public Request getRequestById(UUID requestId) {
        return dataManager.load(Request.class)
                .id(requestId)
                .fetchPlan("full")
                .optional().orElse(null);
    }
    public  <T> T getEntityById(T entity) {
        return (T) dataManager.load(entity.getClass())
                .id(Id.of(entity))
                .fetchPlan(fetchPlanRepository.getFetchPlan(entity.getClass(), entity.getClass() + "-full"))
                .optional().orElse(null);
    }
    public  <T> T getEntityById(T entity, String fetchPlan) {
        return (T) dataManager.load(entity.getClass())
                .id(Id.of(entity))
                .fetchPlan(fetchPlanRepository.getFetchPlan(entity.getClass(), fetchPlan))
                .optional().orElse(null);
    }


    /**
     * Получить Заявку
     *
     * @param keyNum номер заявки
     */
    @ManagedOperation
    public Request getRequestByKeyNum(String keyNum) {
        return dataManager.load(Request.class)
                .query("select s from Request s where s.keyNum = :keynum")
                .parameter("keynum", keyNum)
                .fetchPlan("full")
                .optional().orElse(null);
    }







    @Transactional
    public void updateRequestStatus(Request request, RequestStatusCode statusCode) {
        boolean isCurrentAuth = currentAuthentication.isSet();

        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }
        if (Objects.nonNull(request)) {
            if (Objects.nonNull(statusCode)) {
                request.setStatus(mdmService.getStatusByCode(statusCode.getCode()));
                log.info("Update RequestStatus: " + request.getKeyNum() + ", " + statusCode.getCode());
            } else {
                log.debug("status is null");
            }

        } else {
            log.debug("request is null");
        }

        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }
    }


    public StatusModel getStatusModelByParams(RequestType requestType) {
        boolean isCurrentAuth = currentAuthentication.isSet();

        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }

        Date byDate = new Date();
        RequestTypeStatusModelLink requestTypeStatusModelLink = dataManager.load(RequestTypeStatusModelLink.class)
                .query("select e from RequestTypeStatusModelLink e where e.requestType = :requestType and e.infoSystem is null and :date between e.startDate and e.endDate")
                .parameter("requestType", requestType)
                .parameter("date", byDate)
                .optional().orElse(null);

        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }

        if (Objects.nonNull(requestTypeStatusModelLink)) {
            return requestTypeStatusModelLink.getStatusModel();
        } else {
            return null;
        }
    }

    public StatusModel getStatusModelByParams(RequestType requestType, InfoSystem infoSystem) {
        Date byDate = new Date();
        boolean isCurrentAuth = currentAuthentication.isSet();

        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }

        RequestTypeStatusModelLink requestTypeStatusModelLink = dataManager.load(RequestTypeStatusModelLink.class)
                .query("select e from RequestTypeStatusModelLink e where e.requestType = :requestType and e.infoSystem = :infoSystem and :date between e.startDate and e.endDate")
                .parameter("requestType", requestType)
                .parameter("infoSystem", infoSystem)
                .parameter("date", byDate)
                .optional().orElse(null);

        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }

        if (Objects.nonNull(requestTypeStatusModelLink)) {
            return requestTypeStatusModelLink.getStatusModel();
        } else {
            return null;
        }
    }



    public String generatedKeyNum(String requestTypeCode) {
        final String KEY_TEMPLATE = "%s-%d";
        final String SEQUENCE_TEMPLATE = "%s_request_number";
        String processCode = mdmService.getRequestType(requestTypeCode).getProcess().getCode();
        long nextValue = sequences.createNextValue(Sequence.withName(String.format(SEQUENCE_TEMPLATE, processCode))
                .setStartValue(1)
                .setIncrement(1));

        return String.format(KEY_TEMPLATE, processCode, nextValue);
    }

    public void alignSequence() {
        log.debug("alignSequence");
        List<Process> processes = dataManager.load(Process.class).all().list();
        for (Process process : processes) {
            String proc = process.getCode();
            Long maxKeyNum = dataManager.loadValue(
                    "select max(r.numberKeyNum) from Request r  where r.keyNum like :key",
                    Long.class)
                    .parameter("key", proc + "%")
                    .one();

            if (maxKeyNum == null) {
                log.error("Don't find issues for process {}", process.getCode());
                continue;
            }

            maxKeyNum = maxKeyNum / 10;
            maxKeyNum = maxKeyNum * 10 + 10;

            log.debug("set maxKeyNum {} for {}", maxKeyNum, process.getCode() + "_request_number");
            sequences.createNextValue(Sequence.withName(process.getCode() + "_request_number")
                    .setStartValue(maxKeyNum)
                    .setIncrement(1));
        }
    }





    /**
     * Метод возвращает по заявке List вложенных файлов
     *
     * @param request - заявка, по которой будет происходить поиск
     * @return List вложенных файлов
     */
    public List<RequestFile> getRequestFileByRequest(Request request) {
        return dataManager.load(RequestFile.class)
                .query("select e from RequestFile e where e.request = :request order by e.createdDate")
                .parameter("request", request)
                .list();
    }

    /**
     * Метод возвращает взможность клонирования Типа Заявки
     *
     * @param requestType тип заявок
     * @return может ли указанные тип Заявки быть склонирован
     */
    public boolean isRequestCanBeCloned(String requestType) {
        Date byDate = new Date();
        boolean can = dataManager.load(RequestType.class)
                .query("select e from RequestType e where e.code = :code and e.isCanCloned = :can and :date between e.startDate and e.endDate")
                .parameter("code", requestType)
                .parameter("can", true)
                .parameter("date", byDate)
                .list().size() > 0;
        return can;
    }

    /**
     * Метод возвращает все комментарии к заявке.
     *
     * @param request Заявка
     * @return Lust of RequestComm
     */
    public List<RequestComm> getAllComments(Request request) {
        return dataManager.load(RequestComm.class)
                .query("select e from RequestComm e where e.request =:request")
                .parameter("request", request)
                .list();
    }

    public Request createRequest(Request request) {
        beforeSave(request, true, true);
        request = dataManager.save(request);
        afterSave(request);
        return request;
    }

    public void beforeSave(Request request, boolean reloadRequestType, boolean checkForCreatingManual) {
        if (entityStates.isNew(request)) {
            Objects.requireNonNull(request.getRequestType());
            if (reloadRequestType) {
                Objects.requireNonNull(request.getRequestType().getCode());
                request.setRequestType(mdmService.getRequestType(request.getRequestType().getCode()));
                Objects.requireNonNull(request.getRequestType());
            }
            // Если заявка создается в ручном режиме то проверяется можем ли ее создать в ручную
            if (checkForCreatingManual) {
                Assert.isTrue(request.getRequestType().getIsManualCreate(), "ManualCreate is not allowed");
            }
        }

            // проставляем значения по умолчанию
            RequestFlowParams params = new RequestFlowParams(request, RequestStatusCode.OPEN, userService.getCurrentUser(), entityStates.isNew(request), BpmStepModeCode.MANUAL);
            setAttributeService.setAttribute(params);
            request = params.getRequest();
    }

    public void afterSave(Request request) {
        // при сохранении, проверяем, если процесс не запущен - запускаем WF
        // Но, после сохранения, запускать процесс можно только если заявка переоткрывается
        boolean enabled = !EnumUtils.isValidEnum(RequestStatusFinishCode.class, request.getStatus().getCode());
        if (enabled) {
            if (!requestServiceBPM.isProcessRunOnRequest(request)) {
                requestServiceBPM.runProcess(request);
            }
        }
    }

    public String getRequestUrl(Request request) {
        try {
            Object entityId = request.getId();
            String serialized = UrlIdSerializer.serializeId(entityId);
            String puzhcUrl = appConfig.getPuzhcUrl();

            return String.format(URL_TEMPLATE, puzhcUrl, serialized);

        } catch (RuntimeException e) {
            //log.error(e.getMessage());
            return null;
        }
    }

    public String getRequestUrlAsHTMLLink(Request request) {
        try {
            return String.format(LINK_URL_TEMPLATE, getRequestUrl(request), request.getKeyNum());

        } catch (RuntimeException e) {
            //log.error(e.getMessage());
            return request.getKeyNum();
        }
    }

    /**
     * Осуществляет поиск исполнителя по Заявке, и отсылает уведомления по результатам поиска
     * устанавливает Исполнителя или null
     *
     * @param params       параметры

     **/
/*    @Transactional
    public void setAssigneeAndNotificate(RequestFlowParams params) {
        Assert.notNull(params.getRequest(), "request cannot be null");
        Assert.notNull(params.getTargetStatus(), "endStatus cannot be null");

        List<User> assigneeList = requestAssigneeSearch.getAssignee(params);
        log.info("[{}] requestService searchAssignee result: array.size={}", params.getRequest().getKeyNum(), assigneeList.size());

        if (assigneeList.size() == 0) {
            // Ничего делать не надо - исполнитель не меняется

        } else {
            if (assigneeList.size() == 1) {
                User newAssignee = assigneeList.stream().filter(Objects::nonNull).findFirst().orElse(null);

                params.getRequest().setAssignee(newAssignee);
                //dataManager.save(params.getRequest());
                if (Objects.isNull(newAssignee)) {
                    notificateService.notificateAssignedNotFoundOrTooMany(params.getRequest(), assigneeList);
                } else {
                    notificateService.notificateAssigned(params.getRequest(), newAssignee);
                }
            } else {
                //assigneeList.size() > 1
                params.getRequest().setAssignee(null);
                //dataManager.save(params.getRequest());
                notificateService.notificateAssignedNotFoundOrTooMany(params.getRequest(), assigneeList);
            }
            log.info("[{}] ...new Assignee are set", params.getRequest().getKeyNum());
        }

    }*/

    public void searchAndSetAssignee(RequestFlowParams params) {
        Assert.notNull(params.getRequest(), "request cannot be null");
        Assert.notNull(params.getTargetStatus(), "endStatus cannot be null");

        Set<User> assigneeList = requestAssigneeSearch.getAssignee(params);
        log.info("[{}] requestService search result: result array.size={}", params.getRequest().getKeyNum(), assigneeList.size());

        if (assigneeList.size() == 0) {
            // Ничего делать не надо - исполнитель не меняется
            // Уведомления не высылаем
            log.info("[{}] ...assignee are not changed (=0)", params.getRequest().getKeyNum());
        } else {
            if (assigneeList.size() == 1) {
                // Нашли одного!
                User newAssignee = assigneeList.stream().filter(Objects::nonNull).findFirst().orElse(null);
                if (Objects.nonNull(newAssignee)) {
                    if (!newAssignee.equals(params.getRequest().getAssignee())) {
                        params.getRequest().setAssignee(newAssignee);
                        params.getSearchAssigneeParams().setChangedAssignee(true); // ставим флаг, что сменился исполнитель
                        log.info("[{}] ...new assignee are set", params.getRequest().getKeyNum());

                    } else {
                        params.getSearchAssigneeParams().setChangedAssignee(false); // ставим флаг, что НЕ сменился исполнитель
                        log.info("[{}] new assignee are equals to current", params.getRequest().getKeyNum());
                    }
                } else {
                    params.getRequest().setAssignee(null);
                    params.getSearchAssigneeParams().setChangedAssignee(true); // ставим флаг, что сменился исполнитель
                    log.info("[{}] ...new assignee are null", params.getRequest().getKeyNum());
                }
            } else {
                //assigneeList.size() > 1
                params.getRequest().setAssignee(null);
                params.getSearchAssigneeParams().setChangedAssignee(true); // ставим флаг, что сменился исполнитель
                log.info("[{}] ...assignee set to null (many)", params.getRequest().getKeyNum());
            }
        }
    }
    public void notificateForAssignee(RequestFlowParams params) {
        Assert.notNull(params.getRequest(), "request cannot be null");
        if (Objects.nonNull(params.getRequest().getAssignee())) {
            if (params.getSearchAssigneeParams().isChangedAssignee()) {
                notificateService.notificateAssigned(params.getRequest(), params.getRequest().getAssignee());
            }
        } else {
            notificateService.notificateAssignedNotFoundOrTooMany(params.getRequest(), params.getSearchAssigneeParams().getAssigneeList());
        }
    }


    public String getStatusStyleName(Request request) {
        String styleName = request.getStatus().getStyleName();
        if (Objects.isNull(styleName)) {
            styleName = DEFUALT_STATUS_STYLE_NAME;
        }
        return styleName;
    }

    public long getKeyNumLong(String keyNum) {
        //String clearKeyNum = keyNum.replaceAll("\\D+","");
        String clearKeyNum = keyNum.replaceAll("[^0-9]", "");
        return Long.parseLong(clearKeyNum);
    }
    /**
     * Метод возвращает все связанные заявки
     *
     * @param request связанныее с
     * @return
     */
    public List<Request> getLinkedRequest(Request request) {
        Assert.notNull(request, "request cannot be null");
        StatusModel statusModel = requestServiceBPM.getRunningProcessKey(request);
        return linkedRequestLocator.getBean(StatusModelCode.findByCode(statusModel.getCode()))
                .getLinkedRequest(request).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Request cloneRequest(Request request, boolean runProcess) {
        return cloneRequestService.clone(request, runProcess);
    }

    public List<StatusModelAttributeEditable> getEditableAttributes(Request request) {
        Date byDate  = new Date();
        StatusModel sm;
        if (entityStates.isNew(request) ) {
           sm  = requestServiceBPM.getProcessKeyToStart(request);
        } else {
           sm = requestServiceBPM.getRunningProcessKey(request);
        }
        List<StatusModelAttributeEditable> list = Lists.newArrayList();
        list = dataManager.load(StatusModelAttributeEditable.class)
                .query("select e from StatusModelAttributeEditable e where e.statusModel = :statusModel and e.status = :status and :byDate between e.startDate and e.endDate")
                .parameter("statusModel", sm)
                .parameter("status", request.getStatus())
                .parameter("byDate", byDate)
                .list();
        log.debug("[{}] getEditableAttributes ({}) list.size={}", request.getKeyNum(), sm.getCode(), list.size());
        return list;
    }


    public boolean isCanEditRequest(Request request) {
        boolean enabled = false;
        if (Objects.nonNull(request.getAssignee())) {
            enabled = request.getAssignee().equals(userService.getCurrentUser())
                    && (!EnumUtils.isValidEnum(RequestStatusFinishCode.class, request.getStatus().getCode()));
        } else {
            enabled = false;
        }
        return enabled;
    }
}
