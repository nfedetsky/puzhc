package com.softline.csrv.app.bpm.delegate.contentagreement;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestComm;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestStatusFinishCode;
import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.bpm.data.form.FormOutcome;
import io.jmix.core.DataManager;
import org.apache.commons.lang3.EnumUtils;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



/*
*   Отклоняем смежные заявки ЗСС
*
*/

@Component(RejectZssRelatedRequest.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RejectZssRelatedRequest implements JavaDelegate {

    public static final String NAME="bpm_RejectZssRelatedRequest";
    public static final String NAME_BN="[{}] Отклоняем смежные ЗСС";
    private final Logger log = LoggerFactory.getLogger(RejectZssRelatedRequest.class.getName());

    @Autowired
    private RequestLinkService requestLinkService;
    @Autowired
    private RequestServiceBPM requestServiceBPM;
    @Autowired
    private RequestService requestService;
    @Autowired
    private UserService userService;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private MdmService mdmService;

    private Expression request;

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        Request currentRequest = (Request) execution.getVariable(request.getExpressionText()); // получаем значение переменной процесса
        log.info(NAME_BN, currentRequest.getKeyNum());

        // Получаем список Смежных заявок ЗСС
        // Доработка
        Request reqModification = currentRequest.getRequestModification();
        // ЗСС
        List<Request> zssList =  requestLinkService.getLinkedRequest(reqModification, RequestTypeCode.CONTENT_AGREEMENT);
        zssList.remove(currentRequest);
        log.info("[{}]...found {} ZSS:", currentRequest.getKeyNum(), zssList.size());
        FormOutcome fo = new FormOutcome();



        for(Request r : zssList) {
            RequestComm comm = dataManager.create(RequestComm.class);
            comm.setRequest(r);
            comm.setName(String.format("Автоматическое отклонение в связи с отклонением смежной ЗСС %s", currentRequest.getKeyNum()));
            comm.setAuthor(userService.getCurrentUser());
            dataManager.save(comm);

            log.info("[{}]...ZSS {} {}", currentRequest.getKeyNum(), r.getKeyNum(), r.getStatus().getCode());

            switch (RequestStatusCode.findByCode(r.getStatus().getCode())) {
                case CONSENSUS:
                    fo.setId(RequestStatusCode.REJECTED.getCode());
                    fo.setCaption(fo.getId());
                    requestServiceBPM.completeTask(r, fo);
                    break;
                case ANALYSIS:
                case REQUEST_INFO:
                    fo.setId(RequestStatusCode.CONSENSUS.getCode());
                    fo.setCaption(fo.getId());
                    requestServiceBPM.completeTask(r, fo);
                    fo.setId(RequestStatusCode.REJECTED.getCode());
                    fo.setCaption(fo.getId());
                    requestServiceBPM.completeTask(r, fo);
                    break;
                case CLOSED:
                    Request rNew = requestService.getRequestById(r.getId());
                    rNew.setStatus(mdmService.getStatusByCode(RequestStatusCode.REJECTED.getCode()));
                    dataManager.save(rNew);
            }
            log.info("[{}]...update status to ZSS [{}]", currentRequest.getKeyNum(), r.getKeyNum());
        }

        // что бы запускалось обновление статуса, поиск исполнителя итд в листенере на промежуточных этапах
        execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), RequestStatusCode.REJECTED.getCode());

    }
}
