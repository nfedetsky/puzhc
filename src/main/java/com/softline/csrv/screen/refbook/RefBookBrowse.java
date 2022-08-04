package com.softline.csrv.screen.refbook;

import com.softline.csrv.entity.RefBook;
import io.jmix.core.DataManager;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.Table;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Objects;

@UiController("RefBook.browse")
@UiDescriptor("ref-book-browse.xml")
@LookupComponent("refBooksTable")
@MultipleOpen(value = false)
public class RefBookBrowse extends StandardLookup<RefBook> {

    @Autowired
    private DataManager dataManager;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private Table<RefBook> refBooksTable;

    /**
     * Этот метод заполняет автоматически поля IsVisible, IsEditable, IsHierarchical
     * у объекта класса RefBook каждый раз, когда мы нажимаем копку "Создать"
     * refBooksTable.create - кнопка create в XML
     * newEntitySupplier - метод в классе CreateAction<E>
     * @return RefBook
     */
    @Install(to = "refBooksTable.create", subject = "newEntitySupplier")
    private RefBook createEntityWithFilledFields() {
        RefBook refBook = dataManager.create(RefBook.class);
        refBook.setIsVisible(false);
        refBook.setIsEditable(false);
        refBook.setIsHierarchical(false);
        return refBook;
    }

    /**
     * Метод, который переопределяет действие при двойном нажатии внутри строки таблицы.
     * setItemClickAction - назначить действие, которое будет выполняться при двойном щелчке внутри строки таблицы.
     */
    @Subscribe
    private void transitionWhenClickingOnTableField(InitEvent event) {
        refBooksTable.setItemClickAction(new BaseAction("itemClickAction")
                .withHandler(actionPerformedEvent -> {
                    Map<String, Class<? extends StandardLookup>> dataRefBook = MapRefBook.getDataRefBook();
                    getScreenOnDoubleClick(dataRefBook.get(Objects.requireNonNull(refBooksTable.getSingleSelected()).getTableName()));
                }));
    }

    /**
     * Метод выполняет переход на другой экран при двойном клике в реестре справочников
     * screenBuilders - позволяет открывать всевозможные экраны с различными параметрами.
     * @param screenToDisplayOnDoubleClick - передаём значение Map-ы, значение - это класс, который заканчивается
     * на Browse и extends StandardLookup. Класс, который отвечает за screen entity.
     */
    private void getScreenOnDoubleClick(Class<? extends StandardLookup> screenToDisplayOnDoubleClick) {
       if (screenToDisplayOnDoubleClick != null) {
            screenBuilders.screen(this)
                    .withScreenClass(screenToDisplayOnDoubleClick)
                    .withOpenMode(OpenMode.NEW_TAB)
                    .build()
                    .show();
        }
    }

}