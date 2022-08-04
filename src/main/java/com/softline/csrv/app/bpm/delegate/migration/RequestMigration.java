 package com.softline.csrv.app.bpm.delegate.migration;

 import com.softline.csrv.migration.services.MigrateRequestsService;
 import org.flowable.engine.delegate.DelegateExecution;
 import org.flowable.engine.delegate.JavaDelegate;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.config.BeanDefinition;
 import org.springframework.context.annotation.Scope;
 import org.springframework.stereotype.Component;


@Component(RequestMigration.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RequestMigration implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(RequestMigration.class);

    public static final String NAME="bpm_RequestMigration";

    @Autowired
    private MigrateRequestsService migrateRequestsService;

    @Override
    public void execute(DelegateExecution execution) {
        log.info("{} process started...", NAME);

        migrateRequestsService.migrateRequests();
    }
}
