package com.softline.csrv.entity.lsnr;

import com.softline.csrv.app.support.UserService;
import com.softline.csrv.entity.Request;
import io.jmix.core.DataManager;
import io.jmix.core.event.AttributeChanges;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.core.event.EntitySavingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component("puzhc_RequestChangedListener")
public class RequestChangedListener {
    private final Logger log = LoggerFactory.getLogger(RequestChangedListener.class.getName());

    @Autowired
    private DataManager dataManager;

    @Autowired
    private UserService userService;

    @EventListener
    public void onRequestChangedAfterCommit(EntityChangedEvent<Request> event) {
        try {
            AttributeChanges changes = event.getChanges();
            //log.debug(event.getType() + ", " + changes.getAttributes().size());

            /*
            String emailTitle = "New process task " + task.getName();
            String emailBody = "Hi " + user.getFirstName() + "\n" +
                    "The task " + task.getName() + " has been assigned.";
            EmailInfo emailInfo = EmailInfoBuilder.create()
                    .setAddresses(user.getEmail())
                    .setSubject(emailTitle)
                    .setFrom(null)
                    .setBody(emailBody)
                    .build();
            emailer.sendEmailAsync(emailInfo);
            */

            if (event.getType() == EntityChangedEvent.Type.UPDATED) {
                Request request = dataManager.load(event.getEntityId()).one();
            }
        } catch (Exception e) {
            log.error("Error processing Request EntityChangedEvent", e);
        }
    }

    @EventListener
    void onRequestSaving(EntitySavingEvent<Request> event) {
/*
        Date date = new Date();
        Request e = event.getEntity();
        //log.debug(userService.getCurrentUser().getUsername());
        //log.debug(date.toString());

        if (event.isNewEntity()) {
            e.setCreatedBy(userService.getCurrentUser().getUsername());
            e.setCreatedDate(date);
        } else {
            e.setLastModifiedBy(userService.getCurrentUser().getUsername());
            e.setLastModifiedDate(date);
        }
*/
    }
}
