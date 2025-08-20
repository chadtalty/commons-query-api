package com.chadtalty.commons.data.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A complete criteria object comprising join specifications, filters, and sorting.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Criteria {

    /** Optional join specifications applied before filtering. */
    private List<@Valid JoinSpec> joins;

    /** Zero or more filters combined according to application rules (typically AND). */
    private List<@Valid Filter> filters;

    /** Optional sort specification. */
    private @Valid SortSpec sort;
}
