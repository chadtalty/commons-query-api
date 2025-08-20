package com.chadtalty.commons.data.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Criteria with pagination controls for page-based result sets.
 *
 * <p>Page indexes are zero-based; {@code size} is bounded to a reasonable maximum.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PageableCriteria extends Criteria {

    /** Zero-based page index (0..N). */
    @NotNull @Min(0)
    private Integer page;

    /** Page size (1..500). */
    @NotNull @Min(1)
    @Max(500)
    private Integer size;

    /** Optional sort override at the pagination level. */
    @Override
    public @Valid SortSpec getSort() {
        return super.getSort();
    }
}
