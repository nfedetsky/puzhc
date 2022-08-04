package com.softline.csrv.model.external.pupe;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Атрибуты запросов к ПУПЕ
 */
public class PupeAttrs {

    private final Map<String, String> attrs;

    public PupeAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    public Map<String, String> toMap() {
        return Maps.newHashMap(attrs);
    }

    public static PupeAttrsBuilder builder() {
        return new PupeAttrsBuilder();
    }

    public static class PupeAttrsBuilder {

        private final Map<String, String> attrs = Maps.newHashMap();

        public PupeAttrsBuilder attr(String name, String value) {
            attrs.put(name, value);
            return this;
        }

        public PupeAttrs build() {
            return new PupeAttrs(attrs);
        }

    }

    @Override
    public String toString() {
        return "PupeAttrs{" +
                "attrs=" + attrs +
                '}';
    }
}
