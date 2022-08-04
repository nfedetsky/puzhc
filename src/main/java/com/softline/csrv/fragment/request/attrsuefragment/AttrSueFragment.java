package com.softline.csrv.fragment.request.attrsuefragment;

import com.google.common.collect.ImmutableSet;
import com.softline.csrv.config.PupeIntegrationSettings;
import com.softline.csrv.entity.Request;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.model.external.pupe.PupeAttrsName;
import com.softline.csrv.service.external.PupeClientService;
import io.jmix.appsettings.AppSettings;
import io.jmix.core.common.util.ParamsMap;
import io.jmix.ui.WebBrowserTools;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.ValuePicker;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.ScreenFragment;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.Set;

import static com.softline.csrv.enums.RequestStatusCode.*;

@UiController("attrSueFragment")
@UiDescriptor("attrSueFragment.xml")
public class AttrSueFragment extends ScreenFragment {
    private final Logger log = LoggerFactory.getLogger(AttrSueFragment.class.getName());

    @Autowired
    private InstanceContainer<Request> requestDc;
    @Autowired
    private ValuePicker<String> attrSueField;
    @Autowired
    private PupeClientService pupeClientService;
    @Autowired
    private AppSettings appSettings;
    @Autowired
    private ApplicationContext applicationContext;

    private final Set<String> PUPETYPES = ImmutableSet.of(
            RequestTypeCode.CORRECTION.getCode(),
            RequestTypeCode.MODIFICATION.getCode(),
            RequestTypeCode.RFC.getCode()
            );

    @Subscribe
    public void onInit(InitEvent event) {
        attrSueField.setValue(requestDc.getItem().getAttrSueCode());
    }

    @Subscribe("attrSueField.linkOpen")
    protected void onAttrSueFieldlinkOpen(Action.ActionPerformedEvent event) {
        if (PUPETYPES.contains(requestDc.getItem().getRequestType().getCode())) {

            PupeIntegrationSettings settings = appSettings.load(PupeIntegrationSettings.class);
            String pupeUrl = pupeClientService.getApiUrl(settings.getServerUrl(), settings.getConstantUrlPupe());
            String uuid = requestDc.getItem().getAttrSue();
            pupeUrl = pupeUrl + uuid;

            log.debug("pupeUrl: {}", pupeUrl);

            applicationContext.getBean(WebBrowserTools.class).
                    showWebPage(Optional.ofNullable(pupeUrl).orElse(""),
                            ParamsMap.of("target", "_blank"));
        }
    }

    @Subscribe("attrSueField")
    public void onAttrSueFieldFieldValueChange(ValuePicker.FieldValueChangeEvent event) {
        if (PUPETYPES.contains(requestDc.getItem().getRequestType().getCode())) {
            // Если доработки или исправление, то интегрируемся в ПУПЭ
            String link = event.getText();
            if (link.isEmpty() || !link.contains("$")) {
                requestDc.getItem().setAttrSue(null);
                requestDc.getItem().setAttrSueCode(null);
            }
            int uuidIdx = link.indexOf("$");
            if (link.length() < uuidIdx + 1) {
                requestDc.getItem().setAttrSue(null);
                requestDc.getItem().setAttrSueCode(null);
            }

            //uuid
            String uuid = link.substring(uuidIdx + 1);
            uuid = PupeAttrsName.SERVICE_CALL.concat(uuid);
            log.debug("setAttrSue(uuid)={}", uuid);
            requestDc.getItem().setAttrSue(uuid);

            //IM...
            String attrImCode = pupeClientService.getSCData(uuid).getTitle();
            log.debug("setAttrSueCode(IM)={}", attrImCode);
            requestDc.getItem().setAttrSueCode(attrImCode);


            attrSueField.setValue(attrImCode);
        } else {
            // иначе просто сохраняем текст
            requestDc.getItem().setAttrSueCode(event.getText());
        }
    }
}