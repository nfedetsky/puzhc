package com.softline.csrv.fragment.request.kanbanfragment;

import com.softline.csrv.app.support.RequestLinkService;
import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestComm;
import com.softline.csrv.entity.RequestFile;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.GroupBoxLayout;
import io.jmix.ui.component.LinkButton;
import io.jmix.ui.component.Table;
import io.jmix.ui.download.DownloadFormat;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UiController("KanbanInfoFragment")
@UiDescriptor("kanbanInfo-fragment.xml")
public class KanbanInfoFragment extends ScreenFragment {

    @Autowired
    private InstanceContainer<Request> infoFragmentDc;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Table<RequestFile> attachmentsTable;
    @Autowired
    private Messages messages;
    @Autowired
    private Downloader downloader;
    @Autowired
    private RequestService requestService;
    @Autowired
    private GroupBoxLayout groupBox;
    @Autowired
    private DataManager dataManager;


    @Install(to = "filesDl", target = Target.DATA_LOADER)
    private List<RequestFile> filesDlLoadDelegate(LoadContext<RequestFile> loadContext) {
        if (infoFragmentDc.getItemOrNull() != null) {
            return dataManager.load(RequestFile.class)
                    .query("select e from RequestFile e where e.request = :request ")
                    .parameter("request", infoFragmentDc.getItem())
                    .list();
        }
        return new ArrayList<RequestFile>();
    }

    @Install(to = "commentsDl", target = Target.DATA_LOADER)
    private List<RequestComm> commentsDlLoadDelegate(LoadContext<RequestComm> loadContext) {
        if (infoFragmentDc.getItemOrNull() != null) {
            return requestService.getAllComments(infoFragmentDc.getItem());
        }
        return new ArrayList<RequestComm>();
    }

    @Install(to = "relatedTaskDl", target = Target.DATA_LOADER)
    private List<Request> relatedTaskDlLoadDelegate(LoadContext<Request> loadContext) {
        if (infoFragmentDc.getItemOrNull() != null) {
            return requestService.getLinkedRequest(infoFragmentDc.getItem());
        }
        return new ArrayList<Request>();
    }

    @Subscribe
    public void onInit(InitEvent event) {
        initDownload();
        if (infoFragmentDc.getItemOrNull() != null) {
            groupBox.setCaption(infoFragmentDc.getItem().getKeyNum());
        }

    }


    private void initDownload() {
        attachmentsTable.addGeneratedColumn("fileRef", entity -> {
            LinkButton link = uiComponents.create(LinkButton.class);
            link.setCaption(messages.getMessage("download"));
            link.addClickListener(event -> {
                downloader.download(entity.getFileRef(), DownloadFormat.OCTET_STREAM);
            });
            return link;
        });
        attachmentsTable.setItemClickAction(new BaseAction("itemClickAction")
                .withHandler(event -> {
                    RequestFile entity = attachmentsTable.getSingleSelected();
                    if (entity != null) {
                        downloader.download(entity.getFileRef(), DownloadFormat.OCTET_STREAM);
                    }
                })
        );
    }

}