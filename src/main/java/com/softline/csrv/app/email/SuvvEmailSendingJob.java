package com.softline.csrv.app.email;

import io.jmix.email.Emailer;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SuvvEmailSendingJob implements Job {
    @Autowired
    Emailer emailer;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        emailer.processQueuedEmails();
    }
}
