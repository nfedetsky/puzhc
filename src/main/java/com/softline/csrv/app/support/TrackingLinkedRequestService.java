package com.softline.csrv.app.support;


import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.RequestLink;
import io.jmix.core.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.Objects;



@Service
@Component(TrackingLinkedRequestService.NAME)
public class TrackingLinkedRequestService {
    public static final String NAME = "support_TrackingLinkedRequestService";
    private final Logger log = LoggerFactory.getLogger(TrackingLinkedRequestService.class.getName());

    @Autowired
    private DataManager dataManager;

    /**
     * В методе реализована логика устновки отслеживания для изменяемых Полей на форме
     * @param changedRequest
     * @param requestFrom
     * @param trackedLink
     * @return RequestLink
     */

    public RequestLink OnTrackedItemChangedAsTo(Request changedRequest, Request requestFrom, RequestLink trackedLink) {

        if (Objects.nonNull(changedRequest)) {
            if (Objects.nonNull(trackedLink)) {
                // если выбрали не NULL и ранее отслеживали, то меняем на новый
                trackedLink.setRequestFrom(requestFrom);
                trackedLink.setRequestTo(changedRequest);
                trackedLink.setRequestType(changedRequest.getRequestType());
                return dataManager.save(trackedLink);

            } else {
                // если выбрали не NULL и ранее НЕ отслеживали, то начинаем отслеживать
                trackedLink = dataManager.create(RequestLink.class);
                trackedLink.setRequestFrom(requestFrom);
                trackedLink.setRequestTo(changedRequest);
                trackedLink.setRequestType(changedRequest.getRequestType());
                return dataManager.save(trackedLink);

            }
        } else {
            // выбрали NULL но отслеживали
            if (Objects.nonNull(trackedLink)) {
                dataManager.remove(trackedLink);
                return null;
            }
        }
        return trackedLink;
    }
    /**
     * В методе реализована логика устновки отслеживания для изменяемых Полей на форме
     * @param changedRequest
     * @param requestTo
     * @param trackedLink
     * @return RequestLink
     */
    public RequestLink OnTrackedItemChangedAsFrom(Request changedRequest, Request requestTo, RequestLink trackedLink) {

        if (Objects.nonNull(changedRequest)) {
            if (Objects.nonNull(trackedLink)) {
                // если выбрали не NULL и ранее отслеживали, то меняем на новый
                trackedLink.setRequestFrom(changedRequest);
                trackedLink.setRequestTo(requestTo);
                trackedLink.setRequestType(requestTo.getRequestType());
                return dataManager.save(trackedLink);

            } else {
                // если выбрали не NULL и ранее НЕ отслеживали, то начинаем отслеживать
                trackedLink = dataManager.create(RequestLink.class);
                trackedLink.setRequestFrom(changedRequest);
                trackedLink.setRequestTo(requestTo);
                trackedLink.setRequestType(requestTo.getRequestType());
                return dataManager.save(trackedLink);

            }
        } else {
            // выбрали NULL но отслеживали
            if (Objects.nonNull(trackedLink)) {
                dataManager.remove(trackedLink);
                return null;
            }
        }
        return trackedLink;
    }

}