package io.jmix.bpmui.screen.defaultstartform;

import io.jmix.ui.component.Button;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UiController("bpm_DefaultStartProcessFormImpl")
@UiDescriptor("default-start-process-form.xml")
public class DefaultStartProcessFormImpl extends DefaultStartProcessForm {
    public static final String NAME = "DefaultStartProcessFormImpl";

    private String businessKeyValue;
    private final Logger log = LoggerFactory.getLogger(DefaultStartProcessFormImpl.class.getName());

    @Subscribe("startProcessBtn")
    @Override
    public void onStartProcessBtnClick(Button.ClickEvent event) {
         log.debug("businessKeyValue==" + getBusinessKeyValue());

        runtimeService.startProcessInstanceById(processDefinition.getId(), getBusinessKeyValue(), Collections.emptyMap());
        closeWithDefaultAction();
    }

    public String getBusinessKeyValue() {
        return businessKeyValue;
    }

    public void setBusinessKeyValue(String businessKeyValue) {
        this.businessKeyValue = businessKeyValue;
    }

}
