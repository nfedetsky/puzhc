package com.softline.csrv.migration.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softline.csrv.migration.DTO.Attachment;
import com.softline.csrv.migration.DTO.Comments;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {
    @JsonProperty("attachment")
    private List<Attachment> attachments;

    @JsonProperty("comment")
    private Comments comment;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("customfield_11624")
    private String organisation;

    @JsonProperty("description")
    private String description;

    @JsonProperty("customfield_11620")
    String documentDate;

    @JsonProperty("created")
    String created;


    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Comments getComment() {
        return comment;
    }

    public void setComment(Comments comment) {
        this.comment = comment;
    }

    public String getSummary() {
        return summary;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getDescription() {
        return description;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Fields{" +
                "attachments=" + attachments +
                ", comment=" + comment +
                '}';
    }
}
