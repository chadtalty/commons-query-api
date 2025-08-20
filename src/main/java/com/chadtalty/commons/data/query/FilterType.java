package com.chadtalty.commons.data.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration of filter kinds used by the polymorphic {@link Filter} hierarchy.
 * <p>Values serialize to lower-case strings to match API payloads.
 */
public enum FilterType {
    BASIC("basic"),
    BETWEEN("between"),
    CONTAINS("contains"),
    DATE_TIME("datetime");

    private final String value;

    FilterType(String value) {
        this.value = value;
    }

    /** Serializes enum as a lower-case token (e.g., {@code "basic"}). */
    @JsonValue
    public String getValue() {
        return this.value;
    }

    /** Deserializes from a lower-case token. */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FilterType from(String v) {
        if (v == null) return null;
        String norm = v.trim().toLowerCase();
        for (FilterType t : values()) {
            if (t.value.equals(norm)) return t;
        }
        throw new IllegalArgumentException("Unknown filter type: " + v);
    }
}
