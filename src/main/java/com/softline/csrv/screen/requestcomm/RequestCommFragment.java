package com.softline.csrv.screen.requestcomm;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestComm;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.ui.component.*;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UiController("RequestCommFragment")
@UiDescriptor("request-comm-fragment.xml")
public class RequestCommFragment extends ScreenFragment {

    @Autowired
    private RichTextArea commentDescription;
    @Autowired
    private GroupBoxLayout comment;
    @Autowired
    private InstanceContainer<RequestComm> commentDc;
    @Autowired
    private DataContext dataContext;
    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private Messages messages;
    @Autowired
    private LinkButton removeComment;
    @Autowired
    private LinkButton editComment;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private RequestService requestService;
    @Autowired
    private UserService userService;

    private RequestComm requestComm;

    @Subscribe
    public void onInit(InitEvent event) {
        requestComm = commentDc.getItem();
        String time = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        if (requestComm.getCreatedDate() != null) {
            time = requestComm.getCreatedDate().format(formatter);
        } else if (requestComm.getCreatedDate() == null) {
            LocalDateTime localDateTime = LocalDateTime.now();
            time = localDateTime.format(formatter);
        }
        comment.setCaption(requestComm.getAuthor().getDisplayName() + " добавил комментарий " + time);
        commentDescription.setValue(requestComm.getName());
        commentDescription.setEditable(false);

        if (userService.getCurrentUser().getId().equals(requestComm.getAuthor().getId())) {
            editComment.setEnabled(true);
            removeComment.setEnabled(true);
        }else {
            editComment.setEnabled(false);
            removeComment.setEnabled(false);
        }
    }

    @Subscribe("editComment")
    public void onEditCommentClick(Button.ClickEvent event) {
        commentDescription.setEditable(true);

    }

    @Subscribe("removeComment")
    public void onRemoveCommentClick(Button.ClickEvent event) {
        dataManager.remove(requestComm);
        getFragment().removeAll();
        getFragment().resetExpanded();
        getScreenData().loadAll();
        Component parent = getFragment().getParent();
        if (parent instanceof GroupBoxLayout) {
            ((GroupBoxLayout) parent).setCaption(String.format(messages.getMessage("comments.caption"), requestService.getAllComments(requestDc.getItem()).size()));
        }

    }


    @Subscribe("commentDescription")
    public void onCommentDescriptionValueChange(HasValue.ValueChangeEvent<String> event) {
        requestComm.setName(event.getValue());
        dataContext.merge(requestComm);
        commentDescription.setEditable(false);
    }

    @Subscribe("commentVbox")
    public void onCommentVboxLayoutClick(LayoutClickNotifier.LayoutClickEvent event) {
        Component childComponent = event.getClickedComponent();
        if (getHostScreen() instanceof StandardEditor<?>) {
            if (childComponent != null) {
                if (childComponent instanceof RichTextArea && !((StandardEditor<?>) getHostScreen()).isReadOnly()) {
                    commentDescription.setEditable(true);
                }
            } else {
                commentDescription.setEditable(false);
            }
        }
    }

}