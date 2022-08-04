package com.softline.csrv.screen.project;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Project;

@UiController("Project.browse")
@UiDescriptor("project-browse.xml")
@LookupComponent("projectsTable")
public class ProjectBrowse extends StandardLookup<Project> {
}