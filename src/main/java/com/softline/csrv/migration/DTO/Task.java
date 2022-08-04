package com.softline.csrv.migration.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    private List<Issues> issues;

    public List<Issues> getIssues() {
        return issues;
    }

    @Override
    public String toString() {
        return "Task{" +
                "issue=" + issues +
                '}';
    }
}
