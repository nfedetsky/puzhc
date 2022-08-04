package com.softline.csrv.app.bpm.delegate.migration;

import com.softline.csrv.migration.services.LinksSaver;
import io.jmix.ui.model.DataContext;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component(LinksMigration.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LinksMigration implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(LinksMigration.class);

    public static final String NAME="bpm_LinksMigration";

    @Autowired
    private LinksSaver linksSaver;

    @Override
    public void execute(DelegateExecution execution) {
        try {
        linksSaver.save();
        } catch (IOException ex) {
            log.debug("Ошибка сохранения связей. ", ex);
            throw new RuntimeException(ex);
        }
    }

}
