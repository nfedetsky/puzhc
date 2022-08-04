package com.softline.csrv.app.bpm.delegate.migration;

import com.softline.csrv.migration.services.CommentSaver;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component(CommentsMigration.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CommentsMigration implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(CommentsMigration.class);

    public static final String NAME="bpm_CommentsMigration";

    @Autowired
    private CommentSaver commentSaver;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            commentSaver.save();
        } catch (IOException ex) {
            log.debug("Ошибка сохранения связей. ", ex);
            throw new RuntimeException(ex);
        }
    }
}
