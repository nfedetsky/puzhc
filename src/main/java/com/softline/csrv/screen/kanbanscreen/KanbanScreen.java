package com.softline.csrv.screen.kanbanscreen;

import com.softline.csrv.entity.KanbanColumn;
import com.softline.csrv.entity.KanbanColumnStatus;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.enums.BpmVariableCode;
import com.softline.csrv.fragment.request.kanbanfragment.KanbanFragment;
import com.softline.csrv.fragment.request.kanbanfragment.KanbanInfoFragment;
import io.jmix.core.*;
import io.jmix.core.querycondition.Condition;
import io.jmix.core.querycondition.LogicalCondition;
import io.jmix.core.querycondition.PropertyConditionUtils;
import io.jmix.ui.Fragments;
import io.jmix.ui.UiComponents;
import io.jmix.ui.component.*;
import io.jmix.ui.component.filter.configuration.DesignTimeConfiguration;
import io.jmix.ui.component.jpqlfilter.JpqlFilterSupport;
import io.jmix.ui.component.propertyfilter.SingleFilterSupport;
import io.jmix.ui.model.*;
import io.jmix.ui.screen.*;
import io.jmix.uidata.entity.FilterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@UiController("KanBanScreen")
@UiDescriptor("kanban-screen.xml")
public class KanbanScreen extends Screen {

    @Autowired
    private Fragments fragments;
    @Autowired
    private CollectionContainer<Request> requestDc;
    @Autowired
    private Form filterForm;
    @Autowired
    private CollectionLoader<Request> requestDl;
    @Autowired
    private InstanceContainer<Request> fragmentDc;
    @Autowired
    private CollectionContainer<KanbanColumn> kanbanColumDc;
    @Autowired
    private CollectionLoader<KanbanColumn> kanbanColumDl;
    @Autowired
    private InstanceContainer<Request> infoFragmentDc;
    @Autowired
    private HBoxLayout tittle;
    @Autowired
    private HBoxLayout colum;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private GridLayout info;
    @Autowired
    private FetchPlans fetchPlans;

    private final Logger log = LoggerFactory.getLogger(KanbanScreen.class.getName());
    @Autowired
    private Filter filter;

    LoadContext.Query basicQuery;

    @Subscribe
    public void onInit(InitEvent event) {
        kanbanColumDl.load();
        List<RequestStatus> statusList = dataManager.load(KanbanColumnStatus.class)
                        .query("select e from KanbanColumnStatus e")
                        .list()
                        .stream().map(KanbanColumnStatus::getStatus).collect(Collectors.toList());

        requestDl.setParameter("statuses", statusList);
    }





/*    @Install(to = "requestDl", target = Target.DATA_LOADER)
    private List<Request> requestDlLoadDelegate(LoadContext<Request> loadContext) {
//        log.debug("loadContext.getQuery={}", loadContext.get.getSource().getQuery());
 //       log.debug("loadContext.getLoadContext.getQuery={}", event.getLoadContext().getQuery().getCondition().toString());


        List<Request> requests = new ArrayList<>();
        kanbanColumDl.load();
        kanbanColumDc.getMutableItems().forEach(kanbanColumn -> {
            List<RequestStatus> resultList = kanbanColumn.getKanbanStatusList().stream()
                    .map(KanbanColumnStatus::getStatus)
                    .collect(Collectors.toList());
            loadContext.setFetchPlan(fetchPlans.builder(Request.class)
                    .addFetchPlan(FetchPlan.BASE)
                    .add("name")
                    .add("status")
                    .add("numberKeyNum")
                    .add("keyNum")
                    .build());
            loadContext.setQueryString
                            ("select e from Request e where e.status in :list order by  e.priority.sortOrder, e.numberKeyNum desc")
                    .setMaxResults(kanbanColumn.getMaxTask())
                    .setParameter("list", resultList);
            requests.addAll(dataManager.loadList(loadContext));
        });
        return requests;

    }*/

    @Subscribe(id = "requestDl", target = Target.DATA_LOADER)
    public void onRequestDlPreLoad(CollectionLoader.PreLoadEvent<Request> event) {
        log.debug("event.getQuery={}", event.getSource().getQuery());
        log.debug("event.getLoadContext.getQuery={}", event.getLoadContext().getQuery().getCondition().toString());
        basicQuery = event.getLoadContext().getQuery();
/*        LoadContext.Query q = event.getLoadContext().getQuery();
        q.setParameters();

        Map<String, Object> processVariables = new HashMap<String, Object>();
        processVariables.put(BpmVariableCode.STATUS_TO.getCode(), outcome.getId());
        processVariables.put(BpmVariableCode.REQUEST.getCode(), request);



        event.getLoadContext().getQuery().getCondition().getParameters().add("d")
                LogicalCondition lg = LogicalCondition.and();
        event.getLoadContext().getQuery().getParameters()*/

    }

    @Subscribe(id = "requestDl", target = Target.DATA_LOADER)
    public void onRequestDlPostLoad(CollectionLoader.PostLoadEvent<Request> event) {
        log.debug("size={}", event.getLoadedEntities().size());
        initKanban();

    }



    public void initKanban() {
        initColumAndRow();
        initKanbanFragment();
    }


    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        initKanban();
    }

    private void initKanbanFragment() {
        //requestDl.load();
        addKanbanFragmentInColumn(kanbanColumDc.getMutableItems(), requestDc.getMutableItems());
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        requestDl.load();

    }


    private void initColumAndRow() {
        colum.removeAll();
        tittle.removeAll();
        kanbanColumDl.load();
        kanbanColumDc.getMutableItems().forEach(item -> {
            Form form = uiComponents.create(Form.class);
            setParameterForm(form, item);
            colum.add(form);
            Label label = uiComponents.create(Label.class);
            setParameterLabel(label, item);
            VBoxLayout vBoxLayout = uiComponents.create(VBoxLayout.class);
            setParameterVbox(vBoxLayout, item);
            vBoxLayout.addLayoutClickListener(layoutClickEvent -> {
                Component clickedComponent = layoutClickEvent.getChildComponent();
                if (clickedComponent != null) {
                    info.getComponents().forEach(component -> {
                        if (component instanceof Fragment) {
                            info.remove(component);
                        }
                    });
                    Fragment fragmentRequest = getKanbanInfoFragment(clickedComponent.getId());
                    info.add(fragmentRequest, 1, 1);
                }
            });
            form.add(vBoxLayout);
            tittle.add(label);
            log.info("Созданны столбц для " + item);
        });
    }

    private void addKanbanFragmentInColumn(List<KanbanColumn> kanbanColumnList, List<Request> requestList) {
        kanbanColumnList.forEach(kanbanColumn -> {
            Form form = (Form) colum.getComponent(kanbanColumn.getName());
            if (Objects.nonNull(form)) {
                VBoxLayout vbox = (VBoxLayout) form.getComponent(kanbanColumn.getName());
                assert vbox != null;
                kanbanColumn.getKanbanStatusList().forEach(kanbanColumnStatus -> {
                    requestList.forEach(request -> {
                        if (request.getStatus() != null) {
                            if (Objects.equals(kanbanColumnStatus.getStatus().getCode(), request.getStatus().getCode())) {
                                if (vbox.getOwnComponents().size() < kanbanColumn.getMaxTask()) {
                                    fragmentDc.setItem(request);
                                    Fragment fragment = fragments.create(this, KanbanFragment.class).getFragment();
                                    fragment.setWidth("95%");
                                    fragment.setId(request.getKeyNum());
                                    vbox.add(fragment);
                                    vbox.setVisible(true);
                                }
                            }
                        }
                    });
                });
            }
        });
    }

    private void setParameterForm(Form form, KanbanColumn kanbanColumn) {
        form.setStyleName("fklis-kanban-style");
        form.setWidthFull();
        form.setHeightFull();
        form.setAlignment(Component.Alignment.TOP_CENTER);
        form.setId(kanbanColumn.getName());
    }

    private void setParameterLabel(Label<KanbanColumn> label, KanbanColumn kanbanColumn) {
        label.setVisible(true);
        label.setValue(kanbanColumn);
        label.setAlignment(Component.Alignment.MIDDLE_CENTER);
        label.setStyleName("colored");
    }

    private void setParameterVbox(VBoxLayout vBoxLayout, KanbanColumn kanbanColumn) {
        vBoxLayout.setSpacing(true);
        vBoxLayout.setAlignment(Component.Alignment.MIDDLE_CENTER);
        vBoxLayout.setId(kanbanColumn.getName());
    }

    private Fragment getKanbanInfoFragment(@NotNull String requestKeyNum) {
        Request request = dataManager.load(Request.class).query("select e from Request e where e.keyNum = :keyNum")
                .fetchPlan("full")
                .parameter("keyNum", Objects.requireNonNull(requestKeyNum))
                .one();
        Fragment fragment = fragments.create(this, KanbanInfoFragment.class).getFragment();
        infoFragmentDc.setItem(request);
        fragment.setVisible(true);
        fragment.setId(request.getKeyNum());
        fragment.setHeightFull();
        return fragment;
    }
}
