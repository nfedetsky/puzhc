package com.softline.csrv.migration.DTO;

public class HistoryItem {

    String field;
    String fieldType;
    String from;
    String fromString;
    String to_;
    String toString;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromString() {
        return fromString;
    }

    public void setFromString(String fromString) {
        this.fromString = fromString;
    }

    public String getTo_() {
        return to_;
    }

    public void setTo_(String to_) {
        this.to_ = to_;
    }

    public String getToString() {
        return toString;
    }

    public void setToString(String toString) {
        this.toString = toString;
    }

    @Override
    public String toString() {
        return "Item{" +
                "field='" + field + '\'' +
                ", fieldtype='" + fieldType + '\'' +
                ", from='" + from + '\'' +
                ", fromString='" + fromString + '\'' +
                ", to_='" + to_ + '\'' +
                ", toString='" + toString + '\'' +
                '}';
    }

}
