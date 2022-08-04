package com.softline.csrv.screen.infosystem;

import com.softline.csrv.entity.InfoSystem;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.TreeDataGrid;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("InfoSystem.browse")
@UiDescriptor("info-system-browse.xml")
@LookupComponent("infoSystemsTable")
public class InfoSystemBrowse extends StandardLookup<InfoSystem> {


    @Autowired
    private TreeDataGrid<InfoSystem> infoSystemsTable;
    @Autowired
    private Button viewBtn;

    /**
     * При двойном нажатии на строку таблицы будет открываться режим просмотра
     */
    @Subscribe
    private void transitionWhenClickingOnTableField(InitEvent event) {
        infoSystemsTable.setItemClickAction(new BaseAction("itemClickAction")
                .withHandler(actionPerformedEvent -> viewBtn.click()));
    }

}