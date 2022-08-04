package com.softline.csrv.model.external.pupe;

public class CreateCommentRequest {

    private final String source;
    private final String text;

    public CreateCommentRequest(String source, String text) {
        this.source = source;
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "CreateCommentRequest{" +
                "source:'" + source + '\'' +
                ", text:'" + text + '\'' +
                '}';
    }
}
