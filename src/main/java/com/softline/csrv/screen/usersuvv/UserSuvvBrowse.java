package com.softline.csrv.screen.usersuvv;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.UserSuvv;

@UiController("UserSuvv.browse")
@UiDescriptor("user-suvv-browse.xml")
@LookupComponent("userSuvvsTable")
public class UserSuvvBrowse extends StandardLookup<UserSuvv> {
}