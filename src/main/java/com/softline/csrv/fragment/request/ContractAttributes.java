package com.softline.csrv.fragment.request;

import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.enums.RequestStatusCode;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.ValuePicker;
import io.jmix.ui.component.validation.FutureOrPresentValidator;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@UiController("ContractAttributes")
@UiDescriptor("contract-attrs.xml")
public class ContractAttributes extends ScreenFragment {

    @Autowired
    protected ApplicationContext applicationContext;


    @Autowired
    private DateField<Date> docDateField;
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private DateField<Date> docEndDateField;
    @Autowired
    private ValuePicker revisionNumField;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        if (Objects.nonNull(requestDc.getItem())) {
            RequestStatus status = requestDc.getItem().getStatus();
            if (Objects.nonNull(status)) {
                if (RequestStatusCode.OPEN.getCode().equals(status.getCode())) {
                    FutureOrPresentValidator futureOrPresentValidator = applicationContext
                            .getBean(FutureOrPresentValidator.class);
                    docDateField.addValidator(futureOrPresentValidator);
                    docEndDateField.addValidator(futureOrPresentValidator);
                }
            }
        }
    }
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
}