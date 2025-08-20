package com.chadtalty.commons.data.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A join clause description, pairing a join path with a filter that applies within the join context.
 * <p>Example: join path "customer.address" with a filter on "postal_code".
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JoinSpec {

    /** Join path (resolver-specific). */
    @NotBlank(message = "JoinSpec join must not be blank")
    private String join;

    /** Filter to apply within the joined scope. */
    private @Valid Filter filter;
}
