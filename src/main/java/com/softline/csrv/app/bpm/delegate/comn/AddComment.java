package com.softline.csrv.app.bpm.delegate.comn;

import com.softline.csrv.app.support.UserService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestComm;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.enums.RequestStatusCode;
import io.jmix.core.DataManager;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;


@Component(AddComment.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AddComment implements JavaDelegate {
    private final Logger log = LoggerFactory.getLogger(AddComment.class.getName());
    public static final String NAME="bpm_AddComment";
    public static final String NAME_BN="[{}] Добавление комментария к Заявке ";
    @Autowired
    private DataManager dataManager;
    @Autowired
    private UserService userService;



    private Expression requestlist;
    private Expression request;
    private Expression comment;
    private Expression status;


    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

        Request currentRequest = (Request) execution.getVariable(request.getExpressionText());

        log.info(NAME_BN, currentRequest.getKeyNum());


        ArrayList<Request> reqlist = new ArrayList<Request>();
        reqlist = (ArrayList) execution.getVariable(requestlist.getExpressionText());

        RequestComm comm = dataManager.create(RequestComm.class);
        comm.setRequest(currentRequest);
        //"Автоматическое отклонение в связи с отклонением смежных ЗСС:
        comm.setName(String.format( comment.getExpressionText() + ": %s", reqlist.stream().map(k->k.getKeyNum()).collect(Collectors.joining(", "))) );
        comm.setAuthor(userService.getCurrentUser());
        dataManager.save(comm);

            // устанавливаем статус у Заявки
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);



    }
}
