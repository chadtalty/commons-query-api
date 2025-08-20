package com.chadtalty.commons.data.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base type for all filter criteria used in query construction.
 *
 * <p>Jackson polymorphism is enabled via a {@code type} discriminator in the JSON payload.
 * Subtypes must declare {@link com.fasterxml.jackson.annotation.JsonTypeName} with the matching name.
 *
 * <p>Example JSON:
 * <pre>
 * {
 *   "type": "basic",
 *   "field": "status",
 *   "operator": "equal",
 *   "value": "ACTIVE"
 * }
 * </pre>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BasicFilter.class, name = "basic"),
    @JsonSubTypes.Type(value = BetweenFilter.class, name = "between"),
    @JsonSubTypes.Type(value = ContainsFilter.class, name = "contains"),
    @JsonSubTypes.Type(value = DateTimeFilter.class, name = "datetime")
})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@NoArgsConstructor
public abstract class Filter {

    /**
     * Returns this filter's logical type.
     * <p>Note: This method is intentionally not serialized; Jackson writes the {@code type} property
     * based on the {@code @JsonTypeName} of the leaf class.
     */
    public abstract FilterType getType();

    /**
     * The name of the field/column this filter applies to.
     */
    public abstract @NotBlank String getField();
}
