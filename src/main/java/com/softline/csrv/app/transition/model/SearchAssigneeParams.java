package com.softline.csrv.app.transition.model;

import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.RoleCode;
import org.elasticsearch.common.util.set.Sets;

import java.util.Set;

public class SearchAssigneeParams {

    private RoleCode roleCode;
    private Function function;
    private Set<User> assigneeList = Sets.newHashSet();
    private boolean isChangedAssignee = false;
    private User assignee = null;

    public SearchAssigneeParams(RoleCode roleCode, Function function) {
        this.roleCode = roleCode;
        this.function = function;
        this.isChangedAssignee = false;
        this.assignee = null;
    }
    public SearchAssigneeParams(User assignee) {
        this.roleCode = null;
        this.function = null;
        this.isChangedAssignee = false;
        this.assignee = assignee;
    }

    public RoleCode getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(RoleCode roleCode) {
        this.roleCode = roleCode;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Set<User> getAssigneeList() {
        return assigneeList;
    }

    public void setAssigneeList(Set<User> assigneeList) {
        this.assigneeList = assigneeList;
    }

    public boolean isChangedAssignee() {
        return isChangedAssignee;
    }

    public void setChangedAssignee(boolean changedAssignee) {
        this.isChangedAssignee = changedAssignee;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User toAssignee) {
        this.assignee = toAssignee;
    }
}