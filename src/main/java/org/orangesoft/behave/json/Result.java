package org.orangesoft.behave.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.orangesoft.behave.util.Util;

public class Result {

    // Start: attributes from JSON file report
    private final String status = null;
    @JsonProperty("error_message")
    private final String errorMessage = null;
    private final Long duration = 0L;
    // End: attributes from JSON file report

    public String getStatus() {
        return status;
    }

    public long getDuration() {
        return duration;
    }

    public String getFormatedDuration() {
        return Util.formatDuration(duration);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
