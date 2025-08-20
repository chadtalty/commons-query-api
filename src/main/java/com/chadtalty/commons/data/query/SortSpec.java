package com.chadtalty.commons.data.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sorting specification with separate ascending/descending field lists.
 *
 * <p>Notes:
 * <ul>
 *   <li>Field names are restricted to alphanumerics, underscores, and dots.</li>
 *   <li>A validation guard ensures the same field is not present in both lists.</li>
 * </ul>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SortSpec {

    /** Fields to sort ascending (earlier first). */
    private List<
                    @Pattern(regexp = "[A-Za-z0-9_\\.]+", message = "field must be alphanumeric with dots/underscores")
                    String>
            ascending;

    /** Fields to sort descending (later first). */
    private List<
                    @Pattern(regexp = "[A-Za-z0-9_\\.]+", message = "field must be alphanumeric with dots/underscores")
                    String>
            descending;

    /** Ensures no field appears in both ascending and descending lists. */
    @AssertTrue(message = "ascending and descending sets must be disjoint")
    public boolean isDisjoint() {
        if (ascending == null || descending == null) return true;
        Set<String> a = new HashSet<>(ascending);
        for (String d : descending) {
            if (a.contains(d)) return false;
        }
        return true;
    }
}
