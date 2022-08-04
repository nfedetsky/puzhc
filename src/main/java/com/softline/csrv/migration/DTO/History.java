package com.softline.csrv.migration.DTO;

import java.util.List;

/**
 * Поля одной записи из истории
 */
public class History {


    String name;
    String created;
    List<HistoryItem> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<HistoryItem> getItems() {
        return items;
    }

    public void setItems(List<HistoryItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "History{" +
                "name='" + name + '\'' +
                ", created='" + created + '\'' +
                ", items=" + items +
                '}';
    }
}