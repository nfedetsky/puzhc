package com.softline.csrv.fragment.request;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.screen.request.RequestEditExt;
import io.jmix.core.EntityStates;
import io.jmix.core.LoadContext;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.*;
import io.jmix.ui.component.validation.FutureOrPresentValidator;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.apache.commons.compress.utils.Lists;
import org.jsoup.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.softline.csrv.enums.RequestTypeCode;
import org.springframework.context.ApplicationContext;
import org.xlsx4j.sml.Col;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@UiController("RequirementAttributes")
@UiDescriptor("requirement-attrs.xml")
public class RequirementAttributes extends ScreenFragment {
    private final Logger log = LoggerFactory.getLogger(RequirementAttributes.class.getName());

    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private ValuePicker revisionNumField;
    @Autowired
    private DateField<Date> implementPeriodTimeField;


    @Subscribe("revisionNumField.valueup")
    protected void onrevisionNumFieldValueUp(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getRevisionNum()).orElse(0L) + 1;
        revisionNumField.setValue(i );
        requestDc.getItem().setRevisionNum(i);
        requestDc.getItem().setRevisionDate(LocalDateTime.now());
    }
    @Subscribe("revisionNumField.valuedown")
    protected void onrevisionNumFieldValueDown(Action.ActionPerformedEvent event) {
        long i = Optional.ofNullable(requestDc.getItem().getRevisionNum()).orElse(0L) - 1;
        i = (i < 0L) ? 0 : i;

        revisionNumField.setValue(i);
        requestDc.getItem().setRevisionNum(i);
        requestDc.getItem().setRevisionDate(LocalDateTime.now());
    }



    @Subscribe
    public void onAfterInit(AfterInitEvent event) {

        if (Objects.nonNull(requestDc.getItem())) {
            RequestStatus status = requestDc.getItem().getStatus();
            if (Objects.nonNull(status)) {
                if (RequestStatusCode.OPEN.getCode().equals(status.getCode())) {
                    FutureOrPresentValidator futureOrPresentValidator = applicationContext
                            .getBean(FutureOrPresentValidator.class);
                    implementPeriodTimeField.addValidator(futureOrPresentValidator);
                }
            }
        }
    }

}