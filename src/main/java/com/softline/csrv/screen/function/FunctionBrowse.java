package com.softline.csrv.screen.function;

import com.softline.csrv.entity.Function;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.TreeDataGrid;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Function.browse")
@UiDescriptor("function-browse.xml")
@LookupComponent("functionsTable")
public class FunctionBrowse extends StandardLookup<Function> {

    @Autowired
    private TreeDataGrid<Function> functionsTable;
    @Autowired
    private Button viewBtn;

    /**
     * При двойном нажатии на строку таблицы будет открываться режим просмотра
     */
    @Subscribe
    private void transitionWhenClickingOnTableField(InitEvent event) {
        functionsTable.setItemClickAction(new BaseAction("itemClickAction")
                .withHandler(actionPerformedEvent -> viewBtn.click()));
    }

}