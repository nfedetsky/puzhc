package com.softline.csrv.screen.testingtype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.TestingType;

@UiController("TestingType.edit")
@UiDescriptor("testing-type-edit.xml")
@EditedEntityContainer("testingTypeDc")
public class TestingTypeEdit extends StandardEditor<TestingType> {
}