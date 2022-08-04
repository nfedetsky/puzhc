package com.softline.csrv.screen.main;


import com.softline.csrv.app.BuildInfoUtil;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.config.WikiSettings;
import com.softline.csrv.entity.Request;
import io.jmix.appsettings.AppSettings;
import io.jmix.core.common.util.ParamsMap;
import io.jmix.ui.*;
import io.jmix.ui.component.*;
import io.jmix.ui.component.mainwindow.Drawer;
import io.jmix.ui.component.mainwindow.SideMenu;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.stream.Stream;

@UiController("MainScreen")
@UiDescriptor("main-screen.xml")
@Route(path = "main", root = true)
public class MainScreen extends Screen implements Window.HasWorkArea {
    private static final Logger log = LoggerFactory.getLogger(MainScreen.class);

    @Autowired
    private ScreenTools screenTools;

    @Autowired
    private AppWorkArea workArea;
    @Autowired
    private Drawer drawer;
    @Autowired
    private Button collapseDrawerButton;

    @Autowired
    private SideMenu sideMenu;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private Notifications notifications;

    @Autowired
    private Label<String> appTitleLabel;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AppSettings appSettings;
    @Autowired
    private Link descriptionLink;
    @Autowired
    private Link orderLink;
    @Autowired
    private Link communicationLink;
    @Autowired
    private Link infoLink;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private Label<String> helpLabel;
    @Autowired
    private BuildInfoUtil buildInfoUtil;


    @Override
    public AppWorkArea getWorkArea() {
        return workArea;
    }

    @Subscribe
    public void onInit(InitEvent event) {
        WikiSettings wikiSettings = appSettings.load(WikiSettings.class);

        initMenuIcons();
        SideMenu.MenuItem subItem = sideMenu.getMenuItem("wiki");
        if (subItem != null) {
            subItem.setCommand(item -> {
                applicationContext.getBean(WebBrowserTools.class).showWebPage(Optional.ofNullable(wikiSettings.getWikiUrl()).orElse("")
                        , ParamsMap.of("target", "_blank"));
            });
        }

        descriptionLink.setUrl(Optional.ofNullable(wikiSettings.getWorkWithSystemUrl()).orElse(""));
        orderLink.setUrl(Optional.ofNullable(wikiSettings.getAccessOrderUrl()).orElse(""));
        communicationLink.setUrl(Optional.ofNullable(wikiSettings.getVrfkUrl()).orElse(""));
        infoLink.setUrl(Optional.ofNullable(wikiSettings.getVrfkNewsUrl()).orElse(""));
        helpLabel.setValue(buildInfoUtil.buildInfoText(this.getClass(), log));

    }

    private void initMenuIcons() {
        Stream.of(SideMenuIcon.values())
                .forEach(sideMenuIcon -> {
                    final SideMenu.MenuItem menuItem = sideMenu.getMenuItem(sideMenuIcon.getMenuId());
                    if (menuItem != null) {
                        menuItem.setIcon(sideMenuIcon.source());
                    }

                });
    }


    @Subscribe("collapseDrawerButton")
    public void onCollapseDrawerButtonClick(Button.ClickEvent event) {
        drawer.toggle();
        if (drawer.isCollapsed()) {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_RIGHT);
        } else {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_LEFT);
        }
    }

    @Subscribe("createRequestButton")
    public void onCreateRequestButtonClick(Button.ClickEvent event) {
        screenBuilders.editor(Request.class, this)
                .newEntity()
                .build()
                .show();
    }


    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        screenTools.openDefaultScreen(
                UiControllerUtils.getScreenContext(this).getScreens());
        screenTools.handleRedirect();
    }
}
