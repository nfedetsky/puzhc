package com.softline.csrv.screen.organization;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.Organization;

@UiController("Organization.edit")
@UiDescriptor("organization-edit.xml")
@EditedEntityContainer("organizationDc")
public class OrganizationEdit extends StandardEditor<Organization> {
}