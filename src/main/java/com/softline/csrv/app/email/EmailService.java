package com.softline.csrv.app.email;

import com.google.common.collect.Sets;
import com.softline.csrv.app.support.UserService;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.email.EmailInfo;
import io.jmix.email.EmailInfoBuilder;
import io.jmix.email.Emailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service(EmailService.NAME)
public class EmailService {
    public static final String NAME = "support_EmailService";
    private final Logger log = LoggerFactory.getLogger(EmailService.class.getName());

    @Autowired
    private DataManager dataManager;
    @Autowired
    private SystemAuthenticator systemAuthenticator;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private UserService userService;
    @Autowired
    private Emailer emailer;


    public void sendEmail(EmailSendParams param) {

        boolean isCurrentAuth = currentAuthentication.isSet();
        String subj = param.getSubject();
        String bdy = param.getBody();

        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }


        // получаем список адресатов по роли
        Set<String> listEmails = Sets.newHashSet();

        param.getSendToByRoles().forEach(role -> listEmails.addAll(userService.getEmails(role)));


        if (!listEmails.isEmpty()) {
            // Отправляем мыло
            EmailInfo emailInfo = EmailInfoBuilder.create()
                    .setAddresses(String.join(",", listEmails))
                    .setSubject(subj)
                    .setFrom(null)
                    .setBody(bdy)
                    .setBodyContentType("text/html; charset=UTF-8")
                    .build();
            emailer.sendEmailAsync(emailInfo);
            log.info("Email sent [{}] with subject {}", param.getRequest().getKeyNum(), subj);
        } else {
            log.info("Cannot send email: sendTo is empty");
        }

        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }
    }
}