package com.softline.csrv.screen.problemtype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ProblemType;

@UiController("ProblemType.edit")
@UiDescriptor("problem-type-edit.xml")
@EditedEntityContainer("problemTypeDc")
public class ProblemTypeEdit extends StandardEditor<ProblemType> {
}