package com.softline.csrv.migration.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Issues {
    String key;

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "key='" + key + '\'' +
                '}';
    }

    public int compareTo(Issues b) {
        int keyA = Integer.parseInt(this.getKey().replaceAll("[^0-9]+", ""));
        int keyB = Integer.parseInt(b.getKey().replaceAll("[^0-9]+", ""));
        return Integer.compare(keyA, keyB);
    }
}
