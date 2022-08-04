package com.softline.csrv.model.requestedit;

import com.google.common.collect.Maps;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.model.RequestProcessFlowParams;
import com.softline.csrv.enums.RequestTypeCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
public class ProcessByRequestTypeGroupDelegate {
    public static final String NAME = "puzhc_RequestEditTypeDelegate";
    private final Logger log = LoggerFactory.getLogger(ProcessByRequestTypeGroupDelegate.class);


    private final Map<RequestTypeCode, ProcessByRequestTypeDelegate> delegateMap;

    public ProcessByRequestTypeGroupDelegate(Map<RequestTypeCode, ProcessByRequestTypeDelegate> delegateMap) {
        this.delegateMap = delegateMap;
    }

    public static RequestEditTypeDelegateBuilder builder() {
        return new RequestEditTypeDelegateBuilder();
    }

    public Map<RequestTypeCode, ProcessByRequestTypeDelegate> getDelegateMap() {
        return delegateMap;
    }

    public static final class RequestEditTypeDelegateBuilder {

        private final Map<RequestTypeCode, ProcessByRequestTypeDelegate> map = Maps.newHashMap();

        public RequestEditTypeDelegateBuilder delegate(RequestTypeCode requestTypeCode,
                                                       Consumer<RequestProcessFlowParams> delegateFunc) {
            map.put(requestTypeCode, new ProcessByRequestTypeDelegate(delegateFunc));
            return this;
        }

        public ProcessByRequestTypeGroupDelegate build() {
            return new ProcessByRequestTypeGroupDelegate(map);
        }
    }

}