package io.jmix.bpmui.screen.dynamicform;

import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@UiController("bpm_DynamicStartProcessFormImpl")
@UiDescriptor("dynamic-start-process-form.xml")
public class DynamicStartProcessFormImpl extends DynamicStartProcessForm {

    private String businessKeyValue;

    @Override
    public String getBusinessKeyValue() {
        return businessKeyValue == null ? super.getBusinessKeyValue() : businessKeyValue;
    }

    public void setBusinessKeyValue(String businessKeyValue) {
        this.businessKeyValue = businessKeyValue;
    }

}
