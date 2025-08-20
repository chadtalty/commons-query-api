package com.chadtalty.commons.data.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A simple comparison filter operating on a single field and value.
 *
 * <p>Example:
 * <pre>
 * { "type":"basic", "field":"age", "operator":"greater_than", "value":"30" }
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonTypeName("basic")
@JsonPropertyOrder({"field", "operator", "value"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BasicFilter extends Filter {

    /** Filter kind. Not serialized; the {@code type} property is handled by Jackson polymorphism. */
    @Override
    @JsonIgnore
    public FilterType getType() {
        return FilterType.BASIC;
    }

    /** Target field (alphanumeric, underscores, dots). */
    @NotBlank(message = "basic filter field must not be blank")
    @Pattern(regexp = "[A-Za-z0-9_\\.]+", message = "field must be alphanumeric with dots/underscores")
    private String field;

    /** Comparison operator. */
    @NotNull(message = "basic filter operator must not be null") private Operator operator;

    /** Right-hand value as a string; parse/convert downstream as needed. */
    @NotBlank(message = "basic filter value must not be blank")
    private String value;

    /**
     * Operators supported by {@link BasicFilter}.
     */
    public enum Operator {
        EQUAL("equal"),
        NOT_EQUAL("not_equal"),
        GREATER_THAN("greater_than"),
        LESS_THAN("less_than"),
        GREATER_THAN_OR_EQUAL("greater_than_or_equal"),
        LESS_THAN_OR_EQUAL("less_than_or_equal");

        private final String value;

        Operator(String value) {
            this.value = value;
        }

        /** Serializes as lower-case token. */
        @JsonValue
        public String getValue() {
            return value;
        }

        /** Deserializes from lower-case token (case/space tolerant). */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public static Operator from(String v) {
            if (v == null) return null;
            String norm = v.trim().toLowerCase();
            for (Operator o : values()) {
                if (o.value.equals(norm)) return o;
            }
            throw new IllegalArgumentException("Unknown basic operator: " + v);
        }
    }
}
