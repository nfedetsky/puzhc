package com.softline.csrv.app.bpm.delegate.migration;

import com.softline.csrv.migration.services.HistorySaver;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component(HistoryMigration.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HistoryMigration implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(HistoryMigration.class);

    public static final String NAME = "bpm_MigrateHistory";

    @Autowired
    private HistorySaver historySaver;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            historySaver.save();
        } catch (IOException ex) {
            log.debug("Ошибка сохранения истории изменения заявки. ", ex);
            throw new RuntimeException(ex);
        }
    }
}