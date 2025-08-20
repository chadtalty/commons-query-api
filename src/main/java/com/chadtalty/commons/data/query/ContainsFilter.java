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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A membership filter requiring a field's value to be within a given set.
 *
 * <p>Example:
 * <pre>
 * { "type":"contains", "field":"status", "operator":"in", "values":["OPEN","PENDING"] }
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@JsonTypeName("contains")
@JsonPropertyOrder({"field", "operator", "values"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContainsFilter extends Filter {

    /** Filter kind. Not serialized; the {@code type} property is handled by Jackson polymorphism. */
    @Override
    @JsonIgnore
    public FilterType getType() {
        return FilterType.CONTAINS;
    }

    /** Impl-defined constant operator (always {@code in}). */
    @NotNull(message = "contains filter operator must not be null") private final Operator operator = Operator.IN;

    /** Target field (alphanumeric, underscores, dots). */
    @NotBlank(message = "contains filter field must not be blank")
    @Pattern(regexp = "[A-Za-z0-9_\\.]+", message = "field must be alphanumeric with dots/underscores")
    private String field;

    /** Non-empty set of accepted values. */
    @NotEmpty(message = "contains filter values must not be empty")
    private List<String> values;

    /** Single operator for this filter. */
    public enum Operator {
        IN("in");

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
            if (IN.value.equals(norm)) return IN;
            throw new IllegalArgumentException("Unknown contains operator: " + v);
        }
    }
}
