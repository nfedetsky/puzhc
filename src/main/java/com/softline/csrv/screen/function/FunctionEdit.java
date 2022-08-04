package com.softline.csrv.screen.function;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Function;

@UiController("Function.edit")
@UiDescriptor("function-edit.xml")
@EditedEntityContainer("functionDc")
public class FunctionEdit extends StandardEditor<Function> {
}