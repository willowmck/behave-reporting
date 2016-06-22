package org.orangesoft.behave.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.orangesoft.behave.util.Util;

public class Result {

    // Start: attributes from JSON file report
    private final String status = null;
    @JsonProperty("error_message")
    private final String errorMessage = null;
    private final Float duration = 0F;
    // End: attributes from JSON file report

    public String getStatus() {
        return status;
    }

    public long getDuration() {
        return ((long)(duration*1000));
    }

    public String getFormatedDuration() {
        return Util.formatDuration(getDuration());
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
