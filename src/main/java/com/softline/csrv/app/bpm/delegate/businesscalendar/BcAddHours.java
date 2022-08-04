package com.softline.csrv.app.bpm.delegate.businesscalendar;

import com.softline.csrv.config.BusinessCalendarCfg;
import com.softline.csrv.enums.BpmVariableCode;
import io.jmix.appsettings.AppSettings;
import io.jmix.businesscalendar.repository.BusinessCalendarRepository;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*

* */


@Component(BcAddHours.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BcAddHours implements JavaDelegate {

    public static final String NAME="bpm_BusinessCalendar_AddHours";

    @Autowired
    private BusinessCalendarRepository businessCalendarRepository;
    @Autowired
    private AppSettings appSettings;

    private Expression hours;
    private Expression timervariable;

    private final Logger log = LoggerFactory.getLogger(BcAddHours.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws FlowableException {

            int aHours = Integer.parseInt(hours.getExpressionText() == null ? "0" : hours.getExpressionText());


        if (aHours > 0) {
            BusinessCalendarCfg bcCfg = appSettings.load(BusinessCalendarCfg.class);
            io.jmix.businesscalendar.model.BusinessCalendar businessCalendar = businessCalendarRepository.getBusinessCalendarByCode(bcCfg.getBusinessCalendar());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            LocalDateTime time = LocalDateTime.now();

            time = businessCalendar.plus(time, aHours);

            execution.setVariable(timervariable.getExpressionText(), formatter.format(time));

        }


            // устанавливаем STATUS_TO = null, что бы не обновляли статус
            execution.setVariable(BpmVariableCode.STATUS_TO.getCode(), null);
    }
}
