package com.softline.csrv.screen.requestflowvalidation;


import com.sun.star.util.DateTime;
import io.jmix.ui.component.ComboBox;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.TextField;
import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RequestFlowValidation;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@UiController("RequestFlowValidation.edit")
@UiDescriptor("request-flow-validation-edit.xml")
@EditedEntityContainer("requestFlowValidationDc")
public class RequestFlowValidationEdit extends StandardEditor<RequestFlowValidation> {
    @Autowired
    private DateField<Date> startDateField;
    @Autowired
    private DateField<Date> endDateField;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-mm-dd");
            try {
                startDateField.setValue(ft.parse("2015-01-01"));
                endDateField.setValue(ft.parse("9999-12-01"));
            } catch (Exception e) {

            }

    }

}