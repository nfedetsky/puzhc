package com.softline.csrv.app.transition.searchassignee.impl;

import com.softline.csrv.app.support.RequestServiceBPM;
import com.softline.csrv.app.support.UserService;
import com.softline.csrv.app.transition.model.RequestFlowParams;
import com.softline.csrv.app.transition.searchassignee.IAssigneeSearch;
import com.softline.csrv.entity.RequestStatus;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.TransitionCode;
import org.apache.commons.compress.utils.Lists;
import org.elasticsearch.common.util.set.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Сервис для поиска исполнителя по RFC
 */
@Service
public class GetAssigneeByRFC implements IAssigneeSearch {
    private final Logger log = LoggerFactory.getLogger(GetAssigneeByRFC.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RequestServiceBPM requestServiceBPM;

    @Override
    public Set<User> execute(RequestFlowParams params) {
        Set<User> userList = Sets.newHashSet();
        RequestStatus currentStatus = params.getRequest().getStatus();

        TransitionCode transitionCode = TransitionCode.findByCode(params.getRequest().getStatus().getCode() + params.getTargetStatus().getCode());

        log.debug("[{}] {}.transitionCode={}", params.getRequest().getKeyNum(), getClass().getSimpleName(), transitionCode);



        if (Objects.isNull(transitionCode)) {
            return userList;
        }
        if (params.isRequestNew()) {
            userList.add(params.getRequest().getAuthor());
        } else {
            switch (transitionCode) {
                case OPEN_REJECTED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case ANALYSIS_REJECTED:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case ANALYSIS_ANALYSIS:
                    userList.add(params.getRequest().getCuratorR());
                    break;
                case CONFIRM_ANALYSIS:
                    userList.add(params.getRequest().getCuratorR());
                    break;
                case CONFIRM_CONFIRM:
                    userList.add(params.getRequest().getServiceManager());
                    break;
                case CONSENSUS_ANALYSIS:
                    userList.add(params.getRequest().getCuratorR());
                    break;
                case AGREED_IN_PROGRESS:
                    userList.add(params.getRequest().getCuratorR());
                    break;
                case REJECTED_OPEN:
                    userList.add(params.getRequest().getAuthor());
                    break;
                case OPEN_ANALYSIS:
                    userList.add(params.getRequest().getCuratorR());
                    break;
                case ANALYSIS_CONFIRM:
                    userList.add(params.getRequest().getServiceManager());
                    break;
                case CONFIRM_CONSENSUS:
                    userList.add(params.getRequest().getChangeManager());
                    break;
                case CONSENSUS_AGREED:
                    userList.add(params.getRequest().getCuratorR());
                    break;
                case IN_PROGRESS_TESTING:
                    userList.add(params.getRequest().getTestManager());
                    break;
                case TESTING_VERIFICATION:
                    userList.add(params.getRequest().getCuratorR());
                    break;
                case VERIFICATION_CHECK:
                    userList.add(params.getRequest().getIncidentManager());
                    break;
                case VERIFICATION_RECOVERY:
                    userList.add(params.getRequest().getCuratorR());
                    break;
                case RECOVERY_CHECK:
                    userList.add(params.getRequest().getIncidentManager());
                    break;
                case CHECK_CLOSED:
                    userList.add(params.getRequest().getServiceManager());
                    break;
                case VERIFICATION_PROBLEMS:
                    userList.add(params.getRequest().getAssignee());
                    break;
                case PROBLEMS_CHECK:
                    userList.add(params.getRequest().getIncidentManager());
                    break;
                case ANALYSIS_REQUEST_INFO:
                case CONFIRM_REQUEST_INFO:
                case CONSENSUS_REQUEST_INFO:
                case AGREED_REQUEST_INFO:
                case IN_PROGRESS_REQUEST_INFO:
                case TESTING_REQUEST_INFO:
                case VERIFICATION_REQUEST_INFO:
                case RECOVERY_REQUEST_INFO:
                case PROBLEMS_REQUEST_INFO:
                case CHECK_REQUEST_INFO:
                    userList.add(params.getSearchAssigneeParams().getAssignee());
                    break;
                case REQUEST_INFO_ANALYSIS:
                case REQUEST_INFO_CONFIRM:
                case REQUEST_INFO_CONSENSUS:
                case REQUEST_INFO_AGREED:
                case REQUEST_INFO_IN_PROGRESS:
                case REQUEST_INFO_TESTING:
                case REQUEST_INFO_VERIFICATION:
                case REQUEST_INFO_RECOVERY:
                case REQUEST_INFO_PROBLEMS:
                case REQUEST_INFO_CHECK:
                    userList.add(params.getRequest().getPrevAssignee());
                    break;
                default:
                    break;
            }
        }

        return userList;
    }

}