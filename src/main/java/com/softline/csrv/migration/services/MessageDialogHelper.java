package com.softline.csrv.migration.services;

import io.jmix.ui.Dialogs;
import io.jmix.ui.component.ContentMode;
import org.springframework.stereotype.Service;

@Service
public class MessageDialogHelper {
    protected Dialogs dialogs;

    public void setDialogs(Dialogs dialogs) {
        this.dialogs = dialogs;
    }

    public void createMessage(String title, String message) {
        dialogs.createMessageDialog()
                .withCaption(title)
                .withMessage(message)
                .withContentMode(ContentMode.HTML)
                .show();
    }
}
