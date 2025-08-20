package com.chadtalty.commons.data.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A date-time comparison filter operating on a single instant value.
 *
 * <p>Example:
 * <pre>
 * { "type":"datetime", "field":"updated_at", "operator":"after", "value":"2025-08-01T00:00:00.000Z" }
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonTypeName("datetime")
@JsonPropertyOrder({"field", "operator", "value"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DateTimeFilter extends Filter {

    /** Filter kind. Not serialized; the {@code type} property is handled by Jackson polymorphism. */
    @Override
    @JsonIgnore
    public FilterType getType() {
        return FilterType.DATE_TIME;
    }

    /** Target field (alphanumeric, underscores, dots). */
    @NotBlank(message = "date-time filter field must not be blank")
    @Pattern(regexp = "[A-Za-z0-9_\\.]+", message = "field must be alphanumeric with dots/underscores")
    private String field;

    /** Temporal comparison operator. */
    @NotNull(message = "date-time filter operator must not be null") private Operator operator;

    /** Instant value (UTC ISO-8601). */
    @NotNull(message = "date-time filter value must not be null") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Instant value;

    /** Operators supported by {@link DateTimeFilter}. */
    public enum Operator {
        AFTER("after"),
        AFTER_OR_EQUAL("after_or_equal"),
        BEFORE("before"),
        BEFORE_OR_EQUAL("before_or_equal"),
        EQUAL("equal"),
        NOT_EQUAL("not_equal");

        private final String value;

        Operator(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public static Operator from(String v) {
            if (v == null) return null;
            String norm = v.trim().toLowerCase();
            for (Operator o : values()) {
                if (o.value.equals(norm)) return o;
            }
            throw new IllegalArgumentException("Unknown datetime operator: " + v);
        }
    }
}
