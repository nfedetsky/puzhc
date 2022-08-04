package com.softline.csrv.screen.issues;

import com.softline.csrv.entity.Issue;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;


@UiController("Issues.browse")
@UiDescriptor("IssuesBrowse.xml")
@LookupComponent("issuesesTable")
public class IssuesBrowse extends StandardLookup<Issue> {
}
