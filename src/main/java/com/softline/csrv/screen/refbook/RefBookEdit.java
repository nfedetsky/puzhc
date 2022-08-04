package com.softline.csrv.screen.refbook;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.RefBook;

@UiController("RefBook.edit")
@UiDescriptor("ref-book-edit.xml")
@EditedEntityContainer("refBookDc")
public class RefBookEdit extends StandardEditor<RefBook> {
}