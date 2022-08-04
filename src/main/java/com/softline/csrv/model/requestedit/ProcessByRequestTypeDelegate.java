package com.softline.csrv.model.requestedit;


import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.model.RequestProcessFlowParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.function.Consumer;

/**
 * Делегат
 */
public class ProcessByRequestTypeDelegate {
    public static final String NAME = "puzhc_ProcessRequestTypeDelegate";
    private final Logger log = LoggerFactory.getLogger(ProcessByRequestTypeDelegate.class);


    private final Consumer<RequestProcessFlowParams> delegate;

    public ProcessByRequestTypeDelegate(Consumer<RequestProcessFlowParams> delegate) {
        this.delegate = delegate;
    }

    public void execute(RequestProcessFlowParams param) {
        delegate.accept(param);
    }

}