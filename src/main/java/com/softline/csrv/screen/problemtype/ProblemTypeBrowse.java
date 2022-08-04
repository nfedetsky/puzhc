package com.softline.csrv.screen.problemtype;

import io.jmix.ui.screen.*;
import com.softline.csrv.entity.ProblemType;

@UiController("ProblemType.browse")
@UiDescriptor("problem-type-browse.xml")
@LookupComponent("problemTypesTable")
public class ProblemTypeBrowse extends StandardLookup<ProblemType> {
}