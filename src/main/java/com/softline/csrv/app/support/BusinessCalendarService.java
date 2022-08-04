package com.softline.csrv.app.support;

import com.softline.csrv.config.BusinessCalendarCfg;
import io.jmix.appsettings.AppSettings;
import io.jmix.businesscalendar.model.BusinessCalendar;
import io.jmix.businesscalendar.repository.BusinessCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Сервис рабочих дней в календаре.
 */
@Service
public class BusinessCalendarService {

    @Autowired
    private BusinessCalendarRepository businessCalendarRepository;
    @Autowired
    private AppSettings appSettings;

    public LocalDateTime plusDays(int daysToAdd) {
        return plusDays(LocalDateTime.now(), daysToAdd);
    }

    public LocalDateTime plusDays(LocalDateTime date, int daysToAdd) {
        BusinessCalendarCfg bcCfg = appSettings.load(BusinessCalendarCfg.class);
        BusinessCalendar businessCalendar = businessCalendarRepository.getBusinessCalendarByCode(bcCfg.getBusinessCalendar());
        LocalDateTime resultDate;
        if (daysToAdd > 0) {
            resultDate = businessCalendar.plus(date, 24 * daysToAdd);
        } else {
            resultDate = businessCalendar.minus(date, 24 * daysToAdd);
        }
        return resultDate;
    }
    public LocalDateTime plusHours(LocalDateTime date, int hoursToAdd) {
        BusinessCalendarCfg bcCfg = appSettings.load(BusinessCalendarCfg.class);
        BusinessCalendar businessCalendar = businessCalendarRepository.getBusinessCalendarByCode(bcCfg.getBusinessCalendar());
        LocalDateTime resultDate;
        if (hoursToAdd > 0) {
            resultDate = businessCalendar.plus(date, hoursToAdd);
        } else {
            resultDate = businessCalendar.minus(date, hoursToAdd);
        }
        return resultDate;
    }
    public LocalDateTime plusHours(int hoursToAdd) {
        return plusHours(LocalDateTime.now(), hoursToAdd);
    }

}
