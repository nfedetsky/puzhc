package com.softline.csrv.screen.project;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Project;

@UiController("Project.edit")
@UiDescriptor("project-edit.xml")
@EditedEntityContainer("projectDc")
public class ProjectEdit extends StandardEditor<Project> {
}