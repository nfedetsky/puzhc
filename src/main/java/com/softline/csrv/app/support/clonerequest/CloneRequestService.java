package com.softline.csrv.app.support.clonerequest;

import com.softline.csrv.app.support.RequestService;
import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.support.clonerequest.impl.CloneRequestByVis;
import com.softline.csrv.app.support.mdm.MdmService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.RequestStatusCode;
import com.softline.csrv.enums.RequestTypeCode;
import io.jmix.core.*;
import io.jmix.core.metamodel.model.MetaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CloneRequestService {
    private final Logger log = LoggerFactory.getLogger(CloneRequestService.class);
    private final DataManager dataManager;
    private final RequestService requestService;
    private final Metadata metadata;
    private final MetadataTools metadataTools;
    private final CloneRequestLocator cloneRequestLocator;
    private final MdmService mdmService;
    private final FileStorageLocator fileStorageLocator;
    private final UserService userService;
    private final RequestServiceBPM requestServiceBPM;

    @Autowired
    public CloneRequestService(DataManager dataManager, Metadata metadata, MetadataTools metadataTools,
                               RequestService requestService, CloneRequestLocator cloneRequestLocator
            , MdmService mdmService,  FileStorageLocator fileStorageLocator, UserService userService,
                               RequestServiceBPM requestServiceBPM
    ) {
        this.dataManager = dataManager;
        this.metadata = metadata;
        this.requestService = requestService;
        this.metadataTools = metadataTools;
        this.cloneRequestLocator = cloneRequestLocator;
        this.mdmService = mdmService;
        this.fileStorageLocator = fileStorageLocator;
        this.userService = userService;
        this.requestServiceBPM = requestServiceBPM;
    }

    public Request clone(Request request, boolean runProcess) {
        return cloneInternal(request, runProcess);
    }
    public Request cloneInternal(Request sourceRequest, boolean runProcess) {
        // Клонируем все, затем вызываем индивидуальные изменения по типу
        Request clonedRequest = metadataTools.deepCopy(sourceRequest);
        //Request clonedRequest = dataManager.create(Request.class);
        //clonedRequest = requestService.getRequestById(sourceRequest.getId());

        //Request sourceRequest = requestService.getRequestById(sourceRequest.getId());
        clonedRequest.setId(UUID.randomUUID());
        clonedRequest.setName("clone_" + sourceRequest.getKeyNum() + ":" + sourceRequest.getName());
        clonedRequest.setParentRfc(null);
        clonedRequest.setKeyNum(requestService.generatedKeyNum(sourceRequest.getRequestType().getCode()));
        clonedRequest.setAuthor(userService.getCurrentUser());
        clonedRequest.setAssignee(userService.getCurrentUser());


        // Копируем список "Затрагиваемые сервисы"
        clonedRequest.setInvolvedFunction(sourceRequest.getInvolvedFunction());

        clonedRequest.setStatus(mdmService.getStatusByCode(RequestStatusCode.OPEN.getCode()));

        // индивидуальные изменения по типу
        clonedRequest = cloneRequestLocator.getBean(RequestTypeCode.findByCode(sourceRequest.getRequestType().getCode())).cloneRequest(clonedRequest);

        clonedRequest.setAffectedFunctions(null);
        clonedRequest.setUnavlUserServices(null);

        // сохраняемся
        Request req = dataManager.save(clonedRequest);

        // Копируем список "Влияние на ФО"
        for(RequestAffectedFunction saf : sourceRequest.getAffectedFunctions()) {
            RequestAffectedFunction af = metadata.create(RequestAffectedFunction.class);
            af.setRequest(req);
            af.setFunction(saf.getFunction());
            dataManager.save(af);
        }

        // Недоступные сераисы
        for(UnavailabileServices s : sourceRequest.getUnavlUserServices()) {
                UnavailabileServices us = dataManager.create(UnavailabileServices.class);
                us.setRequest(req);
                us.setFunction(s.getFunction());
                us.setId(UUID.randomUUID());
                dataManager.save(us);
        }

        // Копируем файлы
        copyFiles(sourceRequest, req);

        if (runProcess) {
            requestServiceBPM.runProcess(req);
        }

        return req;
    }
    public void copyFiles(Request sourceRequest, Request targetRequest) {
        List<RequestFile> listFile = requestService.getRequestFileByRequest(sourceRequest);

        if (Objects.nonNull(listFile)) {
            FileStorage fileStorage = fileStorageLocator.getDefault();

            for(RequestFile rf : listFile) {
                InputStream inputStream = fileStorage.openStream(rf.getFileRef());
                FileRef fileRef = fileStorage.saveStream(rf.getName(), inputStream);

                RequestFile rfTarget = dataManager.create(RequestFile.class);
                rfTarget.setRequest(targetRequest);
                rfTarget.setFileRef(fileRef);
                rfTarget.setName(rf.getName());
                dataManager.save(rfTarget);
            }
        }
    }

}