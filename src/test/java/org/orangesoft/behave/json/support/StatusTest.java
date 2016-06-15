package org.orangesoft.behave.json.support;

import org.orangesoft.behave.json.support.Status;
import static org.orangesoft.behave.json.support.Status.FAILED;
import static org.orangesoft.behave.json.support.Status.MISSING;
import static org.orangesoft.behave.json.support.Status.PASSED;
import static org.orangesoft.behave.json.support.Status.PENDING;
import static org.orangesoft.behave.json.support.Status.SKIPPED;
import static org.orangesoft.behave.json.support.Status.UNDEFINED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StatusTest {

    @Test
    public void valuesOf_ReturnsOrderedStatuses() {

        // given
        // tables displays result with following order
        final Status[] reference = { PASSED, FAILED, SKIPPED, PENDING, UNDEFINED, MISSING };

        // when
        Status[] orderedStatuses = Status.values();

        // then
        assertThat(orderedStatuses).isEqualTo(reference);
    }

    @Test
    public void getName_ReturnsNameAsLowerCase() {

        // given
        final Status status = PASSED;
        final String refName = "passed";
        
        // when
        String rawName = status.getRawName();

        // then
        assertThat(rawName).isEqualTo(refName);
    }

    @Test
    public void getLabel_ReturnsNameStartingFromUpperCase() {

        // given
        final Status status = UNDEFINED;
        final String refLabel = "Undefined";

        // when
        String label = status.getLabel();

        // then
        assertThat(label).isEqualTo(refLabel);
    }

    @Test
    public void hasPassed_ReturnsTrueForPASSED() {

        // given
        Status status = PASSED;

        // when
        boolean isPassed = status.isPassed();

        // then
        assertThat(isPassed).isTrue();
    }

    @Test
    public void hasPassed_ReturnsFalseForNoPASSED() {

        // given
        Status status = MISSING;

        // when
        boolean isPassed = status.isPassed();

        // then
        assertThat(isPassed).isFalse();
    }
}
