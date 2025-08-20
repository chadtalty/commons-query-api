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
import jakarta.validation.constraints.AssertTrue;
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
 * A range filter bounding a field between two instants (inclusive).
 *
 * <p>Example:
 * <pre>
 * {
 *   "type":"between",
 *   "field":"created_at",
 *   "operator":"between",
 *   "start_date_time":"2025-01-01T00:00:00.000Z",
 *   "end_date_time":"2025-01-31T23:59:59.999Z"
 * }
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonTypeName("between")
@JsonPropertyOrder({"field", "operator", "startDateTime", "endDateTime"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BetweenFilter extends Filter {

    /** Filter kind. Not serialized; the {@code type} property is handled by Jackson polymorphism. */
    @Override
    @JsonIgnore
    public FilterType getType() {
        return FilterType.BETWEEN;
    }

    /** Impl-defined constant operator (always {@code between}). */
    @NotNull(message = "between_filter operator must not be null") private final Operator operator = Operator.BETWEEN;

    /** Target field (alphanumeric, underscores, dots). */
    @NotBlank(message = "between_filter field must not be blank")
    @Pattern(regexp = "[A-Za-z0-9_\\.]+", message = "field must be alphanumeric with dots/underscores")
    private String field;

    /** Inclusive start instant (UTC ISO-8601). */
    @NotNull(message = "between_filter start_date_time must not be null") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Instant startDateTime;

    /** Inclusive end instant (UTC ISO-8601). */
    @NotNull(message = "between_filter end_date_time must not be null") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Instant endDateTime;

    /** Validates {@code start <= end}. */
    @AssertTrue(message = "start_date_time must be before or equal to end_date_time")
    @JsonIgnore
    public boolean isValidRange() {
        return startDateTime == null || endDateTime == null || !startDateTime.isAfter(endDateTime);
    }

    /** Single operator for this filter. */
    public enum Operator {
        BETWEEN("between");

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
            if (BETWEEN.value.equals(norm)) return BETWEEN;
            throw new IllegalArgumentException("Unknown between operator: " + v);
        }
    }
}
