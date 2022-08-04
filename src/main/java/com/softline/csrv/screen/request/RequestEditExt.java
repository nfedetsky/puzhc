package com.softline.csrv.screen.request;

import com.google.common.collect.Lists;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import com.softline.csrv.app.support.*;
import com.softline.csrv.app.support.security.PermissionParam;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.model.RequestProcessFlowParams;
import com.softline.csrv.app.transition.setattribute.SetAttributeService;
import com.softline.csrv.app.transition.validation.RequestValidationService;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.*;
import com.softline.csrv.exception.RequestValidationException;
import com.softline.csrv.fragment.request.RequestTypeAttrFragmentLocator;
import com.softline.csrv.model.requestedit.ProcessByRequestTypeDelegate;
import com.softline.csrv.model.requestedit.ProcessByRequestTypeGroupDelegate;
import com.softline.csrv.screen.puzhcentitylog.PuzhcEntityLogBrowse;
import com.softline.csrv.screen.relatedrequest.RelatedRequestBrowse;
import com.softline.csrv.screen.requestcomm.RequestCommFragment;
import com.softline.csrv.screen.user.UserBrowse;
import io.jmix.audit.entity.LoggedEntity;
import io.jmix.auditui.screen.entitylog.EntityLogBrowser;
import io.jmix.bpm.data.form.FormOutcome;
import io.jmix.core.*;
import io.jmix.notifications.NotificationManager;
import io.jmix.notifications.entity.ContentType;
import io.jmix.reports.entity.ReportOutputType;
import io.jmix.reports.runner.ReportRunner;
import io.jmix.ui.*;
import io.jmix.ui.action.Action;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.action.DialogAction;
import io.jmix.ui.action.entitypicker.EntityClearAction;
import io.jmix.ui.action.entitypicker.EntityLookupAction;
import io.jmix.ui.app.inputdialog.DialogActions;
import io.jmix.ui.app.inputdialog.DialogOutcome;
import io.jmix.ui.app.inputdialog.InputParameter;
import io.jmix.ui.component.*;
import io.jmix.ui.component.data.table.ContainerTableItems;
import io.jmix.ui.download.DownloadFormat;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.model.*;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.navigation.UrlRouting;
import io.jmix.ui.screen.*;
import io.jmix.ui.upload.TemporaryStorage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@UiController("Request.editExt")
@UiDescriptor("request-edit-ext.xml")
@EditedEntityContainer("requestDc")
@Route(value = "request")
@PrimaryEditorScreen(Request.class)
public class RequestEditExt extends StandardEditor<Request> {
    private final String OUTCOME_BUTTON_CAPTION_TEMPLATE = "[%s]";
    private final Logger log = LoggerFactory.getLogger(RequestEditExt.class.getName());
    @Autowired
    private DataManager dataManager;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Messages messages;
    @Autowired
    private Downloader downloader;
    @Autowired
    private DataContext dataContext;
    @Autowired
    private Notifications notifications;
    @Autowired
    private CollectionLoader<RequestFile> filesDl;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private PopupButton popupDifferentActions;
    @Autowired
    private PopupButton popupExport;
    @Autowired
    private Metadata metadata;
    @Autowired
    private UserService userService;
    @Autowired
    private FileMultiUploadField attachmentsFileMultiUpload;
    @Autowired
    private TemporaryStorage temporaryStorage;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private NotificateService notificateService;
    @Autowired
    private Actions actions;
    @Autowired
    private NotificationManager notificationManager;
    @Autowired
    private RequestService requestService;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private UrlRouting urlRouting;
    @Autowired
    private RequestServiceBPM requestServiceBPM;
    @Autowired
    private Fragments fragments;
    @Autowired
    private InstanceContainer<RequestComm> commentDc;
    @Autowired
    private RequestValidationService requestValidationService;
    @Autowired
    protected ReportRunner reportRunner;
    @Autowired
    private CollectionContainer<Request> linkedRequestDc;

    @Autowired
    private TagField<LabelTag> tagField;
    @Autowired
    private TextArea<String> validationErrorTextArea;
    @Autowired
    private VBoxLayout validationErrorBox;
    @Autowired
    private CollectionContainer<User> usersDc;

    @Autowired
    private EntityComboBox<RequestType> requestTypeField;
    @Autowired
    private RichTextArea descriptionField;
    @Autowired
    private Button editModeBtn;
    @Autowired
    private Button commitBtn;
    @Autowired
    private Button addComment;
    @Autowired
    private HBoxLayout bpmActions;
    @Autowired
    private RichTextArea comment;
    @Autowired
    private GroupBoxLayout requestCommentsBox;
    @Autowired
    private GroupBoxLayout requestDetailsBox;
    @Autowired
    private TextField<RequestStatus> statusField;
    @Autowired
    private TextField<User> assigneeField;
    @Autowired
    private Table<RequestFile> attachmentsTable;
    @Autowired
    private TextField<String> inputClipboardField;
    @Autowired
    private TabSheet linkedRequestTabSheet;
    @Autowired
    private DataComponents dataComponents;
    @Autowired
    private CollectionLoader<Request> linkedRequestDl;
    @Autowired
    private ButtonsPanel attachmentsTableButtonsPanel;
    @Autowired
    private SetAttributeService setAttributeService;
    @Autowired
    private FileStorageLocator fileStorageLocator;
    @Autowired
    private TagField<User> watchersField;
    @Autowired
    private InstanceContainer<Request> requestDc;

    private Fragment currentTypeFragment = null;

    private final ProcessByRequestTypeGroupDelegate groupDelegate = ProcessByRequestTypeGroupDelegate.builder()
            .delegate(RequestTypeCode.CONTRACT, this::contractProcess)
            .delegate(RequestTypeCode.AGREEMENT, this::agreementProcess)
            .delegate(RequestTypeCode.COMPONENT_BUILD, this::componentBuildProcess)
            .delegate(RequestTypeCode.CONTENT_AGREEMENT, this::contentAgreementProcess)
            .delegate(RequestTypeCode.CORRECTION, this::correctionProcess)
            .delegate(RequestTypeCode.DOCUMENT, this::documentProcess)
            .delegate(RequestTypeCode.IS_VERSION, this::visProcess)
            .delegate(RequestTypeCode.MODIFICATION, this::modificationProcess)
            .delegate(RequestTypeCode.REMARK, this::remarkProcess)
            .delegate(RequestTypeCode.REQUEST_FOR_ANALYSIS, this::analysisProcess)
            .delegate(RequestTypeCode.REQUEST_FOR_IMPACT_ASSESSMENT, this::zovProcess)
            .delegate(RequestTypeCode.RFC, this::rfcProcess)
            .delegate(RequestTypeCode.REQUIREMENT, this::requirementProcess)
            .delegate(RequestTypeCode.VIS_AGREEMENT, this::visAgreementProcess)
            .build();


    @Subscribe("deleteFileBtn")
    public void onDeleteFileBtnClick(Button.ClickEvent event) {
        RequestFile rf = attachmentsTable.getSingleSelected();
        if (Objects.nonNull(rf)) {
            FileStorage fileStorage = fileStorageLocator.getDefault();

            dataManager.remove(rf);
            fileStorage.removeFile(rf.getFileRef());
            inputClipboardField.setValue("");

            filesDl.load();
            notifications.create()
                    .withCaption(String.format(messages.getMessage(getClass(), "RequestEditExt.deleteFile"), rf.getFileRef().getFileName()))
                    .show();
        }
    }

    @Subscribe
    public void onInit(InitEvent event) {
        initUploadListner();
    }


    @Install(to = "filesDl", target = Target.DATA_LOADER)
    private List<RequestFile> filesDlLoadDelegate(LoadContext<RequestFile> loadContext) {
        return requestService.getRequestFileByRequest(getEditedEntity());
    }
    @Install(to = "linkedRequestDl", target = Target.DATA_LOADER)
    private List<Request> linkedRequestDlLoadDelegate(LoadContext<Request> loadContext) {
            if (Objects.isNull(getEditedEntity()) || entityStates.isNew(getEditedEntity())) {
                return Lists.newArrayList();
            }
            return requestService.getLinkedRequest(getEditedEntity());
    }


    @Install(to = "attachmentsTable.fileRef", subject = "columnGenerator")
    private Component attachmentsTableFileRefColumnGenerator(RequestFile requestFile) {
        LinkButton link = uiComponents.create(LinkButton.class);
        link.setCaption(messages.getMessage(getClass(), "RequestEditExt.download"));
        link.addClickListener(event -> {
            downloader.download(requestFile.getFileRef(), DownloadFormat.OCTET_STREAM);
        });
        return link;
    }


    @Install(to = "requestTypeDl", target = Target.DATA_LOADER)
    private List<RequestType> requestTypeDlLoadDelegate(LoadContext<RequestType> loadContext) {
        List<RequestType> resultList;
        if (entityStates.isNew(getEditedEntity())) {
            resultList = dataManager.load(RequestType.class).query("select e from RequestType e where e.isManualCreate = true ").list();
        } else {
            resultList = dataManager.load(RequestType.class).query("select e from RequestType e").list();
        }
        return resultList;
    }

    private void initUploadListner() {
        attachmentsFileMultiUpload.addQueueUploadCompleteListener(event -> {
                event.getSource()
                        .getUploadsMap()
                        .forEach((fileId, fileName) -> {
                            FileRef fileRef = temporaryStorage.putFileIntoStorage(fileId, fileName);

                            RequestFile requestFile = dataManager.create(RequestFile.class);
                            requestFile.setName(fileName);
                            requestFile.setFileRef(fileRef);
                            requestFile.setRequest(getEditedEntity());
                            requestFile.setCreatedDate(LocalDateTime.now());
                            requestFile.setCreatedBy(userService.getCurrentUser().getUsername());
                            dataManager.save(requestFile);
                            filesDl.load();
                        });
                notifications.create()
                        .withCaption(String.format(messages.getMessage(getClass(), "RequestEditExt.uploadFile"), event.getSource().getUploadsMap().values()))
                        .show();
                event.getSource().clearUploads();
        });
        attachmentsFileMultiUpload.addFileUploadErrorListener(queueFileUploadErrorEvent ->
                notifications.create()
                        .withCaption(messages.getMessage(getClass(), "RequestEditExt.errorUploadFile"))
                        .show());

    }


    /**
     * Метод Назначает нового исполнителя
     */

    private void setAssignee() {
        Request request = getEditedEntity();

        // Проверяем доступ на кнопку "Назначить"
        boolean isPermitted = securityService.isSpecificPermitted(new PermissionParam(request, SecurityActionButtonCode.ASSIGN));

        if (isPermitted) {
            dialogs.createInputDialog(this)
                    .withCaption(messages.getMessage(RequestEditExt.class, "requestBrowse.setRequestAssignee"))
                    .withParameters(
                            InputParameter.parameter("user")
                                    .withField(() -> {

                                        EntityComboBox<User> field = uiComponents.create(EntityComboBox.of(User.class));
                                        field.setMetaClass(metadata.getClass(User.class));

                                        EntityLookupAction<User> entityLookupAction = actions.create(EntityLookupAction.class);

                                        entityLookupAction.setOpenMode(OpenMode.THIS_TAB);
                                        entityLookupAction.setScreenClass(UserBrowse.class);
                                        field.addAction(entityLookupAction);

                                        field.addAction(actions.create(EntityClearAction.class));
                                        field.setCaption(messages.getMessage(Request.class, "Request.assignee"));
                                        field.setOptionsContainer(usersDc);
                                        field.setWidthFull();
                                        field.setRequired(true);

                                        return field;
                                    }),
                            InputParameter.stringParameter("comment")
                                    .withField(() -> {
                                        TextArea field = uiComponents.create(TextArea.class);
                                        field.setCaption(messages.getMessage(RequestComm.class, "RequestComm"));
                                        field.setWidthFull();
                                        field.setRequired(true);
                                        field.setHeightAuto();
                                        return field;
                                    })
                    )
                    .withActions(DialogActions.OK_CANCEL)
                    .withCloseListener(closeEvent -> {
                        if (closeEvent.closedWith(DialogOutcome.OK)) {
                            String comment = closeEvent.getValue("comment");
                            User user = closeEvent.getValue("user");
                            // process entered values...
                            getEditedEntity().setAssignee(user);

                            RequestComm comm = dataManager.create(RequestComm.class);
                            comm.setRequest(getEditedEntity());
                            comm.setName(comment);
                            comm.setAuthor(userService.getCurrentUser());
                            commentDc.setItem(comm);
                            Fragment requestCommFragment = fragments.create(this, RequestCommFragment.class).getFragment();
                            requestCommentsBox.add(requestCommFragment);
                            requestCommentsBox.setCaption(String.format(messages.getMessage("comments.caption"),requestService.getAllComments(request).size() + 1));
                            dataContext.commit();
                            log.info("[{}] setAssignee by {}", getEditedEntity().getKeyNum(), userService.getCurrentUser().getDisplayName());

                            // Уведомляем исполнителя о назначении ему заявки
                            notificateService.notificateAssigned(getEditedEntity(), user);

                            refresh(getEditedEntity());
                        }
                    })
                    .show();

        } else {
            // 5555 - Проверка прав доступа
            // т.к. нет прав делать назначение, подтягиваем текст проверки и показываем
            RequestValidation validation = dataManager.load(RequestValidation.class)
                    .query("select e from RequestValidation e where e.code = :code")
                    .parameter("code", RequestValidationCode.VALIDATION_5555.getCode())
                    .one();
            validationErrorTextArea.setValue(validation.getDisplayName());
            validationErrorBox.setVisible(true);
        }
    }

    /**
     * Метод создает подменю "Назначить"
     * <p>
     * <p>
     * Метод создает подменю "Еще..."
     */
    private void addToObservable() {
        if (getEditedEntity().getWatchers().contains(userService.getCurrentUser())) {
            notifications.create(Notifications.NotificationType.WARNING)
                    .withDescription(String.format(messages.getMessage(RequestEditExt.class, "RequestEditExt.notificationNotAddToObservable")
                            , getEditedEntity().getKeyNum()))
                    .show();

        } else {
            ArrayList<User> list = new ArrayList<>(getEditedEntity().getWatchers());
/*            if (Objects.isNull(list)) {
                getEditedEntity().setWatchers(new ArrayList<>());
            }*/
            list.add(userService.getCurrentUser());
            getEditedEntity().setWatchers(list);
            dataContext.commit();
            //watchersField.setValue(getEditedEntity().getWatchers());
            notifications.create()
                    .withDescription(String.format(messages.getMessage(RequestEditExt.class, "RequestEditExt.notificationAddToObservable")
                            , getEditedEntity().getKeyNum()))
                    .show();
        }
    }

    private void deleteFromObservable() {
        if (getEditedEntity().getWatchers().contains(userService.getCurrentUser())) {
            ArrayList<User> list = new ArrayList<>(getEditedEntity().getWatchers());
            list.remove(userService.getCurrentUser());
            getEditedEntity().setWatchers(list);
            dataContext.commit();
            notifications.create()
                    .withDescription(String.format(messages.getMessage(RequestEditExt.class, "RequestEditExt.notificationDeleteFromObservable")
                            , getEditedEntity().getKeyNum()))
                    .show();

        } else {
            notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption(messages.getMessage(RequestEditExt.class, "RequestEditExt.notificationError"))
                    .withDescription(String.format(messages.getMessage(RequestEditExt.class, "RequestEditExt.notificationNotDeleteFromObservable")
                            , getEditedEntity().getKeyNum()))
                    .show();
        }

    }
    /**
     * Метод клонирования заявки
     */
    private void cloneRequest() {
        Boolean canCloned = requestService.isRequestCanBeCloned(getEditedEntity().getRequestType().getCode());
        if (canCloned) {
            if (!entityStates.isNew(getEditedEntity())) {

                Request clonedRequest = requestService.cloneRequest(getEditedEntity(), true);

                Objects.requireNonNull(screenBuilders.editor(Request.class, getWindow().getFrameOwner())
                                .withScreenClass(RequestEditExt.class)
                        .editEntity(clonedRequest)
                        .withOpenMode(OpenMode.NEW_TAB)
                        .build()
                        .show());
            }
        } else {
            notifications.create().withDescription(
                            String.format(messages.getMessage(RequestEditExt.class, "requestBrowse.notificationNotCloneable"), getEditedEntity().getRequestType().getName()))
                    .show();
        }

    }

    /**
     * Отправить ссылку на заявку
     */
    private void exportToFile(ReportOutputType type) {
        log.debug("export to file");
        ReportOutputDocument document = reportRunner.byReportCode("Request")
                .addParam("entity", getEditedEntity())
                .withOutputType(type)
                .run();

        byte[] content = document.getContent();

        DownloadFormat format;
        switch (type) {
            case DOCX:
                format = DownloadFormat.DOCX;
                break;

            case ODT:
                format = DownloadFormat.DOC;
                break;

            default:
                format = DownloadFormat.DOCX;
        }
        downloader.download(content, getEditedEntity().getKeyNum(), format);

    }

    /**
     * Отправить ссылку на заявку
     */
    private void sendRequestLinkByEmail() {

        dialogs.createInputDialog(this)
                .withCaption(messages.getMessage(RequestEditExt.class, "requestBrowse.sendRequestLinkByEmail"))
                .withParameters(
                        InputParameter.parameter("link")
                                .withField(() -> {
                                    TextField<String> field = uiComponents.create(TextField.class);

                                    field.setValue(urlRouting.getRouteGenerator()
                                            .getEditorRoute(getEditedEntity(), RequestEditExt.class));
                                    field.setCaption(messages.getMessage(Request.class, "Request"));
                                    field.setWidthFull();
                                    field.setRequired(true);
                                    field.setEditable(false);
                                    return field;
                                }),
                        InputParameter.parameter("user")
                                .withField(() -> {
                                    EntityPicker<User> field = uiComponents.create(EntityPicker.of(User.class));
                                    field.setMetaClass(metadata.getClass(User.class));

                                    EntityLookupAction<User> entityLookupAction = actions.create(EntityLookupAction.class);

                                    entityLookupAction.setOpenMode(OpenMode.DIALOG);
                                    entityLookupAction.setScreenClass(UserBrowse.class);
                                    field.addAction(entityLookupAction);

                                    field.addAction(actions.create(EntityClearAction.class));
                                    field.setCaption(messages.getMessage(User.class, "User"));
                                    field.setWidthFull();
                                    field.setRequired(true);
                                    return field;
                                }),
                        InputParameter.stringParameter("comment")
                                .withField(() -> {
                                    TextArea field = uiComponents.create(TextArea.class);
                                    field.setCaption(messages.getMessage(RequestComm.class, "RequestComm"));
                                    field.setWidthFull();
                                    field.setRequired(true);
                                    field.setHeightAuto();
                                    return field;
                                })
                )
                .withActions(DialogActions.OK_CANCEL)
                //.withWidth("600")
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        String link = closeEvent.getValue("link");
                        String comment = closeEvent.getValue("comment");
                        User user = closeEvent.getValue("user");
                        // process entered values...
                        try {
                            notificationManager.createNotification()
                                    .withSubject(messages.getMessage(RequestEditExt.class, "requestBrowse.sendRequestLinkByEmail"))
                                    .withRecipientUsernames(user.getUsername())
                                    .toChannelsByNames("Email")
                                    .withContentType(ContentType.PLAIN)
                                    .withBody(comment + System.lineSeparator() + link)
                                    .send();
                            notifications.create().withDescription(messages.getMessage("notification.sentSuccessfully"))
                                    .show();

                        } catch (Exception e) {
                            notifications.create()
                                    .withCaption(messages.getMessage("captionError"))
                                    .withDescription(e.getMessage())
                                    .show();

                        }

                    }
                })
                .show();
    }

    /**
     * Метод onBeforeShow
     * <p>
     * param event
     */
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        boolean readOnly = isReadOnly();

        popupDifferentActions.addAction(new BaseAction("appoint")
                .withCaption(messages.getMessage(RequestEditExt.class, "requestBrowse.assignee"))
                .withHandler(actionPerformedEvent -> setAssignee()));
        popupDifferentActions.addAction(new BaseAction("addToObservable")
                .withCaption(messages.getMessage(RequestEditExt.class, "requestBrowse.addToObservable"))
                .withHandler(actionPerformedEvent -> addToObservable()));
        popupDifferentActions.addAction(new BaseAction("deleteFromObservable")
                .withCaption(messages.getMessage(RequestEditExt.class, "requestBrowse.deleteFromObservable"))
                .withHandler(actionPerformedEvent -> deleteFromObservable()));
        popupDifferentActions.addAction(new BaseAction("cloneRequest")
                .withCaption(messages.getMessage(RequestEditExt.class, "requestBrowse.cloneRequest"))
                .withHandler(actionPerformedEvent -> cloneRequest()));
        popupDifferentActions.addAction(new BaseAction("sendRequestLinkByEmail")
                .withCaption(messages.getMessage(RequestEditExt.class, "requestBrowse.sendRequestLinkByEmail"))
                .withHandler(actionPerformedEvent -> sendRequestLinkByEmail()));
        popupDifferentActions.addAction(new BaseAction("showEntityLog")
                .withCaption(messages.getMessage(RequestEditExt.class, "requestBrowse.showEntityLog"))
                .withHandler(actionPerformedEvent -> showEntityLog()));
        popupExport.addAction(new BaseAction("exportToFile")
                .withCaption(messages.getMessage(RequestEditExt.class, "requestBrowse.exportToFileAsDocx"))
                .withHandler(actionPerformedEvent -> exportToFile(ReportOutputType.DOCX)));
        popupExport.addAction(new BaseAction("exportToFileOdt")
                .withCaption(messages.getMessage(RequestEditExt.class, "requestBrowse.exportToFileAsOdt"))
                .withHandler(actionPerformedEvent -> exportToFile(ReportOutputType.ODT)));

        tagField.setEnterPressHandler(new TagField.NewTagProvider<LabelTag>() {
            @Nullable
            @Override
            public LabelTag create(String text) {
                LabelTag tag = metadata.create(LabelTag.class);
                tag.setStartDate(new Date());
                tag.setEndDate(new Date());
                tag.setCreatedDate(new Date());
                tag.setLastModifiedDate(new Date());
                tag.setName(text);
                tag.setCreatedBy(userService.getCurrentUser().getUsername());

                return tag;
            }
        });


        // рисуем кнопки согласно настройками BPM
        refreshBpmOutcomesButtons();


        if (entityStates.isNew(getEditedEntity())) {
            attachmentsFileMultiUpload.setEnabled(false);
            addComment.setEnabled(false);
            comment.setEditable(false);
            editModeBtn.setEnabled(false);
        } else {
            requestTypeField.setEditable(false);
            editModeBtn.setEnabled(readOnly && requestService.isCanEditRequest(getEditedEntity()));
            //commitBtn.setEnabled(editModeBtn.isEnabled());

        }

        loadComments();
    }

    public void showEntityLog() {
        if (!entityStates.isNew(getEditedEntity())) {

            PuzhcEntityLogBrowse screen = screenBuilders.lookup(PuzhcEntityLog.class, this)
                    .withScreenClass(PuzhcEntityLogBrowse.class)
                    .withOpenMode(OpenMode.THIS_TAB)
                    .build();
            screen.setRefRequest(getEditedEntity());
            screen.show();
        }
    }

    @Subscribe
    public void onAfterCommitChanges(AfterCommitChangesEvent event) {
        boolean enabled = !EnumUtils.isValidEnum(RequestStatusFinishCode.class, getEditedEntity().getStatus().getCode());
        if (enabled) {
            requestService.afterSave(getEditedEntity());
        }
        openEditScreen(true);
    }

    /**
     * Метод который отресовывает все комментарии к заявки при инициализации экрана.
     */
    private void loadComments() {
        requestCommentsBox.getComponents().forEach(item -> {
            if (item instanceof Fragment) {
                requestCommentsBox.remove(item);
            }
        });

        List<RequestComm> list = requestService.getAllComments(getEditedEntity());
        requestCommentsBox.setCaption(String.format(messages.getMessage("comments.caption"), list.size()));

        list.forEach(comment -> {
            dataManager.load(RequestComm.class).id(comment.getId()).optional().ifPresent(loadedComment -> {
                commentDc.setItem(loadedComment);
                Fragment fragmentOneComment = fragments.create(this, RequestCommFragment.class).getFragment();
                requestCommentsBox.add(fragmentOneComment);
            });
        });
    }

    private Button createBpmOutcomeButton(FormOutcome outcome) {
        Button button = uiComponents.create(Button.class);
        button.setCaption(String.format(OUTCOME_BUTTON_CAPTION_TEMPLATE, outcome.getCaption()));
        button.addClickListener(event -> processBpmOutcomeButtonClick(outcome));
        return button;
    }

    private void processBpmOutcomeButtonClick(FormOutcome outcome) {

        //dataContext.commit();
        validationErrorBox.setVisible(false);

        Request request = getEditedEntity();
        RequestStatus statusFrom = request.getStatus();
        String statusTo = outcome.getId();


        log.info(String.format("[%s] OutcomeButtonClick -> from %s to %s by %s",
                Optional.ofNullable(request)
                        .map(r -> r.getKeyNum())
                        .orElse(null),
                Optional.ofNullable(statusFrom)
                        .map(r -> r.getCode())
                        .orElse(null),
                Optional.ofNullable(statusTo)
                        .orElse(null),
                Optional.ofNullable(userService.getCurrentUser().getUsername())
                        .orElse(null)));


            ProcessByRequestTypeDelegate d = groupDelegate.getDelegateMap().get(RequestTypeCode.findByCode(request.getRequestType().getCode()));
            if (Objects.nonNull(d)) {
                RequestProcessFlowParams params = new  RequestProcessFlowParams(request, outcome.getId(), userService.getCurrentUser());
                d.execute(params);
            } else {
            log.error("[{}] ProcessByRequestTypeDelegate is null", request.getKeyNum());
        }
        setEditableByFlow();
    }

    private Button createButtonReOpenProcess() {
        Button button = uiComponents.create(Button.class);
        button.setCaption(String.format(OUTCOME_BUTTON_CAPTION_TEMPLATE, messages.getMessage(RequestEditExt.class, "RequestEditExt.runProcess")));
        button.addClickListener(event -> {
            log.info("[{}] re-open WF", getEditedEntity().getKeyNum());

            requestServiceBPM.runProcess(getEditedEntity());

            // проставляем значения по умолчанию после Переоткрытия
            RequestFlowParams params = new RequestFlowParams(requestService.getRequestById(getEditedEntity().getId()), RequestStatusCode.OPEN, userService.getCurrentUser(), false);
            setAttributeService.setAttribute(params);
            //Request request = getEditedEntity();
            dataManager.save(params.getRequest());

            //request = params.getRequest();
//            request = dataContext.merge(params.getRequest());
            refreshFromDB(params.getRequest().getId());
            //dataContext.commit();
            refreshBpmOutcomesButtons();

        });
        return button;
    }

    private void refresh(Request request) {
        //log.info("[{}] refresh status1={}", request.getKeyNum(),request.getStatus().getCode());

        Request req = getEditedEntity();
        //req = dataContext.merge(request);
        req = request;
        //log.info("[{}] refresh status2={}", req.getKeyNum(),req.getStatus().getCode());
        //log.info("[{}] refresh status3={}", getEditedEntity().getKeyNum(),getEditedEntity().getStatus().getCode());

        //requestDc.setItem(req);
        //log.info("[{}] refresh status3={}", req.getKeyNum(),req.getStatus().getCode());
        //log.info("[{}] refresh status4={}", requestDc.getItem().getKeyNum(),requestDc.getItem().getStatus().getCode());
        //dataContext.commit();

        refreshBpmOutcomesButtons();

    }
    private void refreshFromDB(UUID requestId) {
        Request request = requestService.getRequestById(requestId);
        request = dataContext.merge(request);
        dataContext.commit();
        //requestDc.setItem();
        refreshBpmOutcomesButtons();
    }


    private void defaultProcess(RequestProcessFlowParams params) {
        //TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getAction());
        String prevStatusCode = params.getRequest().getStatus().getCode();

        FormOutcome fo = new FormOutcome();
        fo.setId(params.getAction());
        fo.setCaption(fo.getId());
        log.debug("[{}] defaultProcess, Action={}, prevStatus={}", params.getRequest().getKeyNum(), params.getAction(), prevStatusCode);

        try {
            if (RequestStatusCode.REQUEST_INFO.getCode().equals( params.getAction())) {
                // Запросить информацию
                requestInfoDialog(fo, true);

            } else if (RequestStatusCode.REQUEST_INFO.getCode().equals(prevStatusCode)) {
                // Предоставить информацию
                requestInfoDialog(fo, false);
            } else {

                Map<String, Object> processVariables = requestServiceBPM.getDefaultVariables(params.getRequest(), fo);
                processVariables.put(BpmVariableCode.STEPMODE.getCode(), BpmStepModeCode.MANUAL.getCode());
                requestServiceBPM.completeTask(params.getRequest(), fo, processVariables);


                    refreshFromDB(params.getRequest().getId());
                //dataContext.commit();

                //Заявка находится в статусе, который требует проверки Заявки Исполнителем
                // Статус_конечный=Статус_начальный
                if (RequestStatusCode.VALIDATION.getCode().equals(params.getAction())) {
                    notifications.create().withDescription(messages.getMessage("notification.validationOk"))
                            .withPosition(Notifications.Position.BOTTOM_RIGHT)
                            .show();
                }
            }

            } catch (RequestValidationException e) {
            validationErrorTextArea.setValue(requestValidationService.getValidationsText(e));
            validationErrorBox.setVisible(true);
        }
    }
    private void contractProcess(RequestProcessFlowParams params) {
        TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getAction());

        if (Objects.isNull(transitionCode)) {
            log.debug("[{}] contractProcess transitionCode is null", params.getRequest().getKeyNum());
            return;

        }

        switch (transitionCode) {
            case OPEN_CLOSED:
            case IMPLEMENTATION_CLOSED:
                // если контракт и закрываем, то предупреждаем
                if (Objects.nonNull(params.getRequest().getDocEndDate())) {
                    if (params.getRequest().getDocEndDate().compareTo(new Date()) > 0) {
                        dialogs.createOptionDialog()
                                .withCaption(messages.getMessage("dialogs.confirm"))
                                .withMessage(String.format(messages.getMessage("dialogs.wantToCloseContract"), params.getRequest().getKeyNum()))
                                .withActions(
                                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY)
                                                .withHandler(e -> {
                                                    defaultProcess(params);
                                                }),
                                        new DialogAction(DialogAction.Type.NO)
                                )
                                .show();
                    } else {
                        defaultProcess(params);
                    }
                }
                break;
            default:
                defaultProcess(params);

        }
    }

    private void agreementProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void componentBuildProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void contentAgreementProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void correctionProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void documentProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void visProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void modificationProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void remarkProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void analysisProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void zovProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void rfcProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void requirementProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    private void visAgreementProcess(RequestProcessFlowParams params) {
        defaultProcess(params);
    }

    @Subscribe("requestTypeField")
    public void onRequestTypeFieldValueChange(HasValue.ValueChangeEvent<RequestType> event) {
        log.info("[{}] TypeFieldValueChange={}", getEditedEntity().getKeyNum(), event.getValue().getCode());

        Request request = getEditedEntity();

        if (entityStates.isNew(request)) {
            descriptionField.clear();
            requestService.beforeSave(request, false, true);
            this.getWindow().setCaption(String.format("%s %s", messages.getMessage(getClass(), "RequestEditExt.caption"), request.getKeyNum()));
        }

        if (RequestTypeCode.IS_VERSION.getCode().equals(request.getRequestType().getCode())) {
            popupDifferentActions.addAction(new BaseAction("connections")
                    .withCaption(messages.getMessage(RequestEditExt.class, "RequestEditExt.linkedRequest"))
                    .withHandler(actionPerformedEvent -> showRelatedRFC()));
        }

        Class<? extends ScreenFragment> fragmentClass = Optional.ofNullable(event.getValue())
                .map(BaseDictionary::getCode)
                .map(RequestTypeAttrFragmentLocator::getAttrFragmentForRequestType)
                .orElse(null);


        if (fragmentClass != null) {
            requestDetailsBox.removeAll();
            currentTypeFragment = fragments.create(this, fragmentClass).getFragment();
            if (Objects.nonNull(currentTypeFragment)) {
                setEditableByFlow();
                requestDetailsBox.add(currentTypeFragment);
                requestDetailsBox.setVisible(true);
            }
        } else {
            requestDetailsBox.setVisible(false);
        }

    }

    public void setEditableByFlow() {
        log.debug("[{}] setEditableByFlow", getEditedEntity().getKeyNum());

        if (Objects.nonNull(currentTypeFragment)) {
            //log.debug("[{}] currentTypeFragment={}", getEditedEntity().getKeyNum(), currentTypeFragment.getId());

            Collection<Component> fragmentComponents = currentTypeFragment.getComponents().stream().filter(f-> { return (f instanceof Component.Editable) || (f instanceof Requirable) || ( f instanceof Fragment); }).collect(Collectors.toList());
            Collection<Component> windowComponents = this.getWindow().getComponents().stream().filter(f->f instanceof Component.Editable).collect(Collectors.toList());
           // log.debug("[{}] fragmentComponents.count={}, windowComponents={}", getEditedEntity().getKeyNum(), fragmentComponents.size(), windowComponents.size());
            //log.debug("[{}] isReadOnly()={}", getEditedEntity().getKeyNum(), isReadOnly());

            if (!isReadOnly()) {

                // редактируемость открываем только если режим редактирования и только по атрибутам по постановке (ЖЦ, статус)
                List<StatusModelAttributeEditable> attrList = requestService.getEditableAttributes(requestDc.getItem());
                //log.debug("[{}] getEditableAttributes={}", getEditedEntity().getKeyNum(), attrList.size());

                for(StatusModelAttributeEditable attr : attrList) {
                    if (fragmentComponents.stream().anyMatch(c-> attr.getCode().equals(c.getId()))) {
                        Component component = fragmentComponents.stream().filter(c -> attr.getCode().equals(c.getId())).findFirst().orElse(null);

                        if (Objects.nonNull(component)) {
                           // log.debug("[{}] set Component={}", getEditedEntity().getKeyNum(), component.getId());

                            if (component instanceof Fragment) {
                                ((Fragment) component).setEnabled(true);
                               // log.debug("[{}] Fragment {} setEnabled", attr.getCode());
                            } else if (component instanceof Component.Editable) {
                                ((Component.Editable) component).setEditable(true);
                                //log.debug("[{}] Component {} setEditable", getEditedEntity().getKeyNum(), attr.getCode());
                                if (component instanceof ValuePicker) {
                                    Collection<Action> actions = ((ValuePicker<?>) component).getActions();
                                    //boolean isvaluePickerWithAction = ((ValuePicker<?>) component).getActions().stream().allMatch(s->{return "valueup".equals(s.getId() || "valuedown".equals(s.getId()});
                                    ((ValuePicker<?>) component).getActions().stream().forEach(a-> a.setEnabled(true));
                                }
                            }

                            if (attr.getIsMandatory() && (component instanceof Requirable)) {
                                ((Requirable) component).setRequired(attr.getIsMandatory());
                                //log.debug("[{}] Component {} setRequired", getEditedEntity().getKeyNum(), attr.getCode());
                            }

                        }
                    } else if (windowComponents.stream().anyMatch(c-> attr.getCode().equals(c.getId()))) {
                        Component component = windowComponents.stream().filter(c -> attr.getCode().equals(c.getId())).findFirst().orElse(null);

                        if (Objects.nonNull(component)) {
                            ((Component.Editable) component).setEditable(true);
                            //log.debug("[{}] window Component {} setEditable", getEditedEntity().getKeyNum(), attr.getCode());

                            if (attr.getIsMandatory() && (component instanceof Requirable)) {
                                ((Requirable) component).setRequired(attr.getIsMandatory());
                                //log.debug("[{}] window Component {} setRequired", getEditedEntity().getKeyNum(), attr.getCode());
                            }
                        }
                    }
                }
            }

        } else {
            //log.debug("[{}] currentTypeFragment is null", getEditedEntity().getKeyNum());
        }
    }

    @Install(to = "requestTypeField", subject = "optionIconProvider")
    private String requestTypeFieldOptionIconProvider(RequestType requestType) {
        return "font-icon:" + requestType.getIconPath();
    }

    

    private void showRelatedRFC() {
        // только для ВИС
        RelatedRequestBrowse screen = screenBuilders.lookup(Request.class, this)
                .withScreenClass(RelatedRequestBrowse.class)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(requests -> {
                    Request nextRequest = requests.iterator().next();
                    nextRequest = requestService.getRequestById(nextRequest.getId());
                    nextRequest.setRequestVis(null);
                    dataManager.save(nextRequest);
                })
                .build();
        screen.setRelatedToRequest(getEditedEntity());
        screen.setRelatedTypeCode(RequestTypeCode.RFC);
        screen.show();
    }

    @Subscribe("statusField")
    public void onStatusFieldValueChange(HasValue.ValueChangeEvent<RequestStatus> event) {
        statusField.setStyleName(requestService.getStatusStyleName(getEditedEntity()));
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        event.getSource().getWindow()
                .setCaption(String.format("%s %s", messages.getMessage(getClass(), "RequestEditExt.caption"), getEditedEntity().getKeyNum()));

    }

    @Subscribe("validationBoxCloseButton")
    public void onValidationBoxCloseButtonClick(Button.ClickEvent event) {
        validationErrorBox.setVisible(false);
    }

    /**
     * Запросить/Предоставить информацию
     */
    private void requestInfoDialog(FormOutcome outcome, Boolean requestInfo) {

        String messageCode = "RequestEditExt.provideInfo";

        if (requestInfo) {
            messageCode = "RequestEditExt.requestInfo";
        }

        dialogs.createInputDialog(this)
                .withCaption(messages.getMessage(RequestEditExt.class, messageCode))
                .withParameters(
                        InputParameter.parameter("user")
                                .withField(() -> {
                                    EntityComboBox<User> field = uiComponents.create(EntityComboBox.of(User.class));
                                    field.setMetaClass(metadata.getClass(User.class));

                                    EntityLookupAction<User> entityLookupAction = actions.create(EntityLookupAction.class);

                                    entityLookupAction.setOpenMode(OpenMode.THIS_TAB);
                                    entityLookupAction.setScreenClass(UserBrowse.class);
                                    field.addAction(entityLookupAction);

                                    field.addAction(actions.create(EntityClearAction.class));
                                    field.setCaption(messages.getMessage(Request.class, "Request.assignee"));
                                    field.setOptionsContainer(usersDc);
                                    field.setWidthFull();
                                    field.setRequired(true);
                                    if (!requestInfo) {
                                        field.setValue(getEditedEntity().getPrevAssignee());
                                    }
                                    field.setEditable(requestInfo);
                                    return field;
                                }),
                        InputParameter.stringParameter("comment")
                                .withField(() -> {
                                    RichTextArea field = uiComponents.create(RichTextArea.class);
                                    field.setCaption(messages.getMessage(RequestComm.class, "RequestComm"));
                                    field.setWidthFull();
                                    field.setRequired(true);
                                    field.setHeightAuto();
                                    return field;
                                })
                )
                .withActions(DialogActions.OK_CANCEL)
                .withWidth("700")
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        requestInfoProcess(closeEvent.getValue("comment"), closeEvent.getValue("user"), outcome, requestInfo);
                    }
                })
                .show();
    }

    public void requestInfoProcess(String comment, User user, FormOutcome outcome, boolean requestInfo) {
        Request request = getEditedEntity();
        RequestComm comm = dataManager.create(RequestComm.class);
        comm.setRequest(request);
        comm.setName(comment);
        comm.setAuthor(userService.getCurrentUser());
        commentDc.setItem(comm);
        Fragment requestCommFragment = fragments.create(this, RequestCommFragment.class).getFragment();
        requestCommentsBox.add(requestCommFragment);
        requestCommentsBox.setCaption(String.format(messages.getMessage("comments.caption"),requestService.getAllComments(request).size() + 1));

        Map<String, Object> processVariables = requestServiceBPM.getDefaultVariables(request, outcome);
        processVariables.put(BpmVariableCode.USER_TO_ASSIGNEE.getCode(), user);
        processVariables.put(BpmVariableCode.STEPMODE.getCode(), BpmStepModeCode.MANUAL.getCode());
        try {
            requestServiceBPM.completeTask(request, outcome, processVariables);
            refreshFromDB(request.getId());
        } catch (RequestValidationException e) {
            RequestComm mergeComm = dataContext.merge(comm);
            dataContext.remove(mergeComm);
            dataContext.commit();
            requestCommentsBox.remove(requestCommFragment);
            requestCommentsBox.setCaption(String.format(messages.getMessage("comments.caption"),requestService.getAllComments(request).size()));

            validationErrorTextArea.setValue(requestValidationService.getValidationsText(e));
            validationErrorBox.setVisible(true);
        }
    }

    /**
     * После выбора поля Автор автоматически устанавливает такоеже значение в поле Исполнитель
     * <p>
     * param event
     * Работает только при создании новой заявки и если поле исполнитель пустое
     *
     * @param event param event
     */
    @Subscribe("authorField")
    public void onAuthorFieldValueChange(HasValue.ValueChangeEvent<User> event) {
        if (entityStates.isNew(getEditedEntity()) & assigneeField.getValue() == null) {
            assigneeField.setValue(event.getValue());
        }
    }

    /**
     * Кнопка добавления комментария
     *
     * @param event
     */
    @Subscribe("addComment")
    public void onAddCommentClick(Button.ClickEvent event) {
        if (comment.getValue() != null) {
            RequestComm requestComm = dataManager.create(RequestComm.class);
            requestComm.setRequest(getEditedEntity());
            requestComm.setName(comment.getValue());
            requestComm.setAuthor(userService.getCurrentUser());

            //requestDc.getItem().getListRequestComm().add(requestComm);

            requestCommentsBox.setCaption(String.format(messages.getMessage("comments.caption"), requestService.getAllComments(getEditedEntity()).size()));
            commentDc.setItem(requestComm);
            dataManager.save(requestComm);
            Fragment requestCommFragment = fragments.create(this, RequestCommFragment.class).getFragment();
            requestCommentsBox.add(requestCommFragment);
            requestCommentsBox.setCaption(String.format(messages.getMessage("comments.caption"),requestService.getAllComments(getEditedEntity()).size()));

            comment.clear();
        } else {
            notifications.create().withCaption("Поле комментария не заполнено").show();
        }
    }

    /**
     * Копирование ссылки на файл в Clipboard
     **/
    @Subscribe("clipboardTrigger")
    protected void onClipboardTrigger(ClipboardTrigger.CopyEvent event) {

        if (event.isSuccess() && (Objects.nonNull(inputClipboardField.getValue()))) {
            notifications.create()
                    .withCaption(messages.getMessage(getClass(), "RequestEditExt.linkToFileCopied"))
                    .show();
        }
    }

    /**
     * при каждом выделении строки на таблице копируем ссылку на файл в textfield
     **/
    @Subscribe("attachmentsTable")
    public void onAttachmentsTableSelection(Table.SelectionEvent<RequestFile> event) {
        RequestFile entity = attachmentsTable.getSingleSelected();
        if (entity != null) {
            inputClipboardField.setValue(entity.getFileRef().getPath());
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        attachmentsFileMultiUpload.setEnabled(!readOnly);
        attachmentsTableButtonsPanel.setEnabled(!readOnly);

    }

    /**
     * Метод отвечает за отрисовку таблицы и вкладок Связанных заявок.
     */
    private void refreshLinkedRequest(Request request) {
        if (entityStates.isNew(request)) {
            return;
        }

        linkedRequestTabSheet.removeAllTabs();
        linkedRequestDc.getItems().forEach(req -> {
            Table<Request> table = uiComponents.create(Table.class);

            table.setId(req.getRequestType().getName());
            List<Request> requestListTyped = linkedRequestDc.getItems().stream()
                    .filter(item -> item.getRequestType().getCode().equals(req.getRequestType().getCode()))
                    .collect(Collectors.toList());

            CollectionContainer<Request> container = dataComponents.createCollectionContainer(Request.class);

            container.setItems(requestListTyped);
            container.setFetchPlan(linkedRequestDc.getFetchPlan());
            table.setItems(new ContainerTableItems<>(container));
            table.addGeneratedColumn(messages.getMessage(RequestEditExt.class, "RequestEditExt.keyNum"), 0, entity -> {
                LinkButton linkButton = uiComponents.create(LinkButton.class);
                linkButton.setCaption(entity.getKeyNum());
                linkButton.addClickListener(clickEvent -> {
                    Screen screen = screenBuilders.editor(Request.class, this)
                            .withOpenMode(OpenMode.NEW_TAB)
                            .editEntity(entity)
                            .build();
                    if (screen instanceof StandardEditor) {
                        ((StandardEditor<?>) screen).setReadOnly(true);
                    }
                    screen.show();
                });

                return linkButton;
            });
            table.addGeneratedColumn(messages.getMessage(RequestEditExt.class, "RequestEditExt.function"), 1, entity -> {
                Label<String> label = uiComponents.create(Label.class);
                if (entity.getFunction() != null) {
                    label.setValue(entity.getFunction().getName());
                }
                return label;
            });
            table.addGeneratedColumn(messages.getMessage(RequestEditExt.class, "RequestEditExt.affectedFunctions"), 2, entity -> {
                Label<String> label = uiComponents.create(Label.class);
                StringBuilder sb = new StringBuilder();
                if (!entity.getAffectedFunctions().isEmpty()) {
                    for (RequestAffectedFunction function : entity.getAffectedFunctions()) {
                        sb.append(function.getFunction().getName()).append("\n");
                    }
                    label.setValue(sb.toString());
                }
                return label;
            });
            table.addGeneratedColumn(messages.getMessage(RequestEditExt.class, "RequestEditExt.status"), 4, entity -> {
                Label<String> label = uiComponents.create(Label.class);
                if (entity.getStatus() != null) {
                    label.setValue(entity.getStatus().getName());
                }
                return label;
            });
            table.addGeneratedColumn(messages.getMessage(RequestEditExt.class, "RequestEditExt.assigne"), 5, entity -> {
                Label<String> label = uiComponents.create(Label.class);
                if (entity.getAssignee() != null) {
                    label.setValue(entity.getAssignee().getDisplayName());
                }
                return label;
            });
            if (linkedRequestTabSheet.getTab(req.getRequestType().getName()) != null) {
                linkedRequestTabSheet.setSelectedTab(Objects.requireNonNull(linkedRequestTabSheet.getTab(req.getRequestType().getName())));
            } else {
                linkedRequestTabSheet.addTab(req.getRequestType().getName(), table);
            }

        });
    }

    @Subscribe("linkedRequestBox")
    public void onLinkedRequestBoxExpandedStateChange(Collapsable.ExpandedStateChangeEvent event) {
        if (event.isExpanded()) {
            linkedRequestDl.load();
            refreshLinkedRequest(getEditedEntity());
        }
    }

    @Install(to = "usersDl", target = Target.DATA_LOADER)
    private List<User> usersDlLoadDelegate(LoadContext<User> loadContext) {
        return dataManager.load(User.class).all().list();
    }

    @Subscribe("editMode")
    public void onEditMode(Action.ActionPerformedEvent event) {
        openEditScreen(false);
    }

    /**
     * Открываем новый экран просмотра\редактирования заявки
     */
    public void openEditScreen(boolean readOnly) {
        RequestEditExt edit = screenBuilders.editor(Request.class, this)
                .withOpenMode(OpenMode.THIS_TAB)
                .withScreenClass(RequestEditExt.class)
                .newEntity(getEditedEntity())
//                .withParentDataContext(getScreenData().getDataContextOrNull())
                .build();
        if (readOnly) {
            edit.setReadOnly(true);
        }
        closeWithDefaultAction();
        edit.show();

    }
    /**
     * Метод рисует кнопки согласно настройками BPM
     * <p>
     * param
     */
    private void refreshBpmOutcomesButtons() {
        boolean readOnly = isReadOnly();

        editModeBtn.setEnabled(readOnly  && requestService.isCanEditRequest(getEditedEntity()));
        //commitBtn.setEnabled(editModeBtn.isEnabled());

        bpmActions.removeAll();
        Request request = getEditedEntity();
        if (!entityStates.isNew(request)) {
            boolean isProcessRunning = requestServiceBPM.isProcessRunOnRequest(request);
            List<FormOutcome> outcomes = requestServiceBPM.getFormOutcomesForTask(request);
            if ((!CollectionUtils.isEmpty(outcomes)) && isProcessRunning) {
                outcomes.forEach(o -> bpmActions.add(createBpmOutcomeButton(o)));
            } else if (!isProcessRunning) {
                if (requestServiceBPM.isCanReopen(request) && userService.getCurrentUser().equals(request.getAssignee())) {
                    bpmActions.add(createButtonReOpenProcess());
                }
            }
        }
    }
}