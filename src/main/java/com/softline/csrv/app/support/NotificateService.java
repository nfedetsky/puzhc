package com.softline.csrv.app.support;


import com.softline.csrv.entity.*;
import com.softline.csrv.entity.Process;
import com.softline.csrv.enums.ProcessCode;
import com.softline.csrv.enums.RoleCode;
import io.jmix.core.Messages;
import io.jmix.notifications.NotificationManager;
import io.jmix.notifications.channel.impl.EmailNotificationChannel;
import io.jmix.notifications.channel.impl.InAppNotificationChannel;
import io.jmix.notifications.entity.ContentType;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;


@Service(NotificateService.NAME)
public class NotificateService {
    public static final String NAME = "support_NotificateService";
    private final Logger log = LoggerFactory.getLogger(NotificateService.class.getName());


    private final NotificationManager notificationManager;
    private final Messages messages;
    private final RequestService requestService;
    private final UserService userService;

    @Autowired
    public NotificateService(NotificationManager notificationManager,
                                  Messages messages,
                                  RequestService requestService,
                                  UserService userService) {
        this.notificationManager = notificationManager;
        this.messages = messages;
        this.requestService = requestService;
        this.userService = userService;

    }


    public void notificateAssigned(Request request, User user) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(user, "user cannot be null");

        try {
        notificationManager.createNotification().withSubject(String.format(messages.getMessage("notification.subjectRequestAssigned"), request.getKeyNum()))
                .withRecipientUsernames(user.getUsername())
                .toChannelsByNames(InAppNotificationChannel.NAME, EmailNotificationChannel.NAME )
                .withContentType(ContentType.PLAIN)
                // .withBody(String.format(messages.getMessage("notification.bodyRequestAssignedHTML"), requestService.getRequestUrl(request), request.getKeyNum()))
                .withBody(String.format(messages.getMessage("notification.bodyRequestAssigned"), request.getKeyNum()))
                .send();
        log.info("[{}] sent notificateAssigned to {}", request.getKeyNum(), user.getUsername());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    public void notificateWatcher(Request request, User user) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(user, "user cannot be null");

        try {
        notificationManager.createNotification().withSubject(String.format(messages.getMessage("notification.subjectRequestChanged"), request.getKeyNum()))
                .withRecipientUsernames(user.getUsername())
                .toChannelsByNames(EmailNotificationChannel.NAME )
                .withContentType(ContentType.PLAIN)
                //.withBody(String.format(messages.getMessage("notification.bodyRequestChangedHTML"), requestService.getRequestUrl(request), request.getKeyNum()))
                .withBody(String.format(messages.getMessage("notification.bodyRequestAssigned"), request.getKeyNum()))
                .send();
            log.info("[{}] sent notificateWatcher to {}", request.getKeyNum(), user.getUsername());

        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }


    public void notificateAssignedNotFoundOrTooMany(Request request, Set<User> users) {
        Assert.notNull(request, "request cannot be null");
        List<User> sendTo = Lists.newArrayList();
        String subject = "notification.subjectAssignedNotDefined";
        //String body = "notification.bodyAssignedNotDefinedHTML";
        String body = "notification.subjectAssignedNotDefined";

        Process process = request.getProcess();
        if (ProcessCode.RFC.getCode().equals(process.getCode())) {
            sendTo = userService.getUsers(RoleCode.FK_RFC_DISPATCHER);
        } else {
            sendTo = userService.getUsers(RoleCode.FK_SUVV_DISPATCHER);
        }

        if (users.size() > 1) {
            subject = "notification.subjectAssignedTooMany";
            //body = "notification.bodyAssignedTooManyHTML";
            body = "notification.subjectAssignedTooMany";
        }
        try {
            subject = String.format(messages.getMessage(subject), request.getKeyNum());
            body = String.format(messages.getMessage(body), request.getKeyNum());

            notificationManager.createNotification().withSubject(subject)
                    .withRecipientUsernames(sendTo.stream().map(User::getUsername).collect(Collectors.toList()))
                    .toChannelsByNames(InAppNotificationChannel.NAME, EmailNotificationChannel.NAME)
                    .withContentType(ContentType.PLAIN)
                    //.withBody(String.format(messages.getMessage(body), requestService.getRequestUrl(request), request.getKeyNum()))
                    .withBody(body)
                    .send();
            log.info("[{}] sent notificateAssignedNotFoundOrTooMany", request.getKeyNum());

        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }

    }

    public void notificateWatchers(Request request, List<User> users) {

        for(User u: users) {
            notificateWatcher(request, u);
        }
    }
}