package com.softline.csrv.app.transition.validation;

import com.google.common.collect.Maps;
import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.validation.model.RequestValidationDelegate;
import com.softline.csrv.app.transition.validation.model.RequestValidationResult;
import com.softline.csrv.entity.*;
import com.softline.csrv.enums.BpmStepModeCode;
import com.softline.csrv.enums.RequestValidationCode;
import com.softline.csrv.exception.RequestValidationException;
import io.jmix.core.DataManager;
import io.jmix.core.Sort;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import org.apache.commons.compress.utils.Lists;
import org.elasticsearch.common.util.set.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service(RequestValidationService.NAME)
public class RequestValidationService {
    public static final String NAME = "puzhc_RequestValidationService";
    private final Logger log = LoggerFactory.getLogger(RequestValidationService.class.getName());
    private static final String VALIDATION_ERROR_TEMPLATE = "...%d) %s";

    private final RequestValidationLocator validationLocator;
    private final DataManager dataManager;
    private final RequestServiceBPM requestServiceBPM;

    @Autowired
    private SystemAuthenticator systemAuthenticator;
    @Autowired
    private CurrentAuthentication currentAuthentication;





    @Autowired
    public RequestValidationService(DataManager dataManager, RequestServiceBPM requestServiceBPM, RequestValidationLocator validationLocator) {
        this.dataManager = dataManager;
        this.requestServiceBPM = requestServiceBPM;
        this.validationLocator = validationLocator;
    }

    public List<RequestFlowValidation> getValidations(RequestFlowParams params) {
        boolean isCurrentAuth = currentAuthentication.isSet();

        if (!isCurrentAuth) {
            systemAuthenticator.begin();
        }

        StatusModel statusModel = requestServiceBPM.getProcessKeyToStart(params.getRequest());
        Date byDate = new Date();



        List<RequestFlowValidation> validations = dataManager.load(RequestFlowValidation.class)
                .query("select s from RequestFlowValidation s where :byDate between s.startDate and s.endDate and s.statusFrom.code = :statusFrom and s.statusTo.code = :statusTo and s.statusModel=:statusModel")
                .parameter("byDate", byDate)
                .parameter("statusFrom", params.getRequest().getStatus().getCode())
                .parameter("statusTo", params.getTargetStatus().getCode())
                .parameter("statusModel", statusModel)
                .sort(Sort.by(Sort.Direction.ASC, "sortOrder"))
                .list();

        log.info("[{}] getValidations STEPMODE={}", params.getRequest().getKeyNum(), params.getStepMode().getCode());

        //log.info("[{}] getValidations validations1.size={}", params.getRequest().getKeyNum(), validations.size());

        if (BpmStepModeCode.AUTO.getCode().equals(params.getStepMode().getCode())) {
            // в автоматическом режиме убираем проверки 5555 и 7777
            validations = validations.stream().filter(f -> !"7777".equals(f.getValidation().getCode())).collect(Collectors.toList());
            validations = validations.stream().filter(f -> !"5555".equals(f.getValidation().getCode())).collect(Collectors.toList());
            log.debug("[{}]...remove 5555 and 7777 validations", params.getRequest().getKeyNum());
        }
        //log.info("[{}] getValidations validations2.size={}", params.getRequest().getKeyNum(), validations.size());


        if (!isCurrentAuth) {
            systemAuthenticator.end();
        }

        return validations.stream()
                        .filter(v -> Objects.nonNull(RequestValidationCode.findByCode(v.getValidation().getCode())))
                        .collect(Collectors.toList());

    }


    /**
     * по кодам проверок формируем массив текста проверок
     *
     * @param e Исключение по проверкам, содержащее массив кодов проверок
     */
    public String getValidationsText(RequestValidationException e) {

        StringBuilder sb = new StringBuilder();
        Map<String, Set<RequestValidationCode>> errorsMap = e.getFailed();


        errorsMap.forEach((k, v) -> {
            sb.append(String.format("[%s]:", k));
            sb.append("\r\n");

            int num = 1;
            List<RequestValidation> list = Lists.newArrayList();

            list = dataManager.load(RequestValidation.class)
                    .query("select s from RequestValidation s where s.code in :codes")
                    .parameter("codes", v.stream().map(s->s.getCode()).collect(Collectors.toList()))
                    .sort(Sort.by(Sort.Direction.ASC, "sortOrder"))
                    .list();

            for(RequestValidation rv : list) {
                sb.append(String.format(VALIDATION_ERROR_TEMPLATE, num++, rv.getDisplayName()));
                sb.append("\r\n");
            }
        });

        return sb.toString();
    }

    public RequestValidationResult dispatchAll(RequestFlowParams params) {
        // получаем список проверк
        // запускаем проверки
        // составляем список не пройденных, и генерим исключение, если такие есть

        log.info("[{}] Run all validation:", params.getRequest().getKeyNum());
        List<RequestFlowValidation> listFlowValidations = getValidations(params);
        Set<RequestValidationCode> failed = Sets.newHashSet();
        Map<String, Set<RequestValidationCode>> resultMap = Maps.newHashMap();

        for (RequestFlowValidation fv : listFlowValidations) {

            RequestValidationDelegate delegate = validationLocator.get(RequestValidationCode.findByCode(fv.getValidation().getCode()));
            if (Objects.nonNull(delegate)) {
                boolean validated = delegate.validate(params);
                log.info("[{}] ...validation-{}={}", params.getRequest().getKeyNum(), fv.getValidation().getCode(), validated);
                if (!validated) {
                    failed.add(RequestValidationCode.findByCode(fv.getValidation().getCode()));
                }
            }
            if (failed.size() > 0) {
                resultMap.put(params.getRequest().getKeyNum(), failed);
            }

        }
        return new RequestValidationResult(resultMap);
    }


}
