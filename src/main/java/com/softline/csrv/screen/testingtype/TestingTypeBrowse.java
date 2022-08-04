package com.softline.csrv.screen.testingtype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.TestingType;

@UiController("TestingType.browse")
@UiDescriptor("testing-type-browse.xml")
@LookupComponent("testingTypesTable")
public class TestingTypeBrowse extends StandardLookup<TestingType> {
}