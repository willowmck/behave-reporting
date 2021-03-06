package org.orangesoft.behave.json;

import org.orangesoft.behave.json.support.Resultsable;
import org.orangesoft.behave.json.support.Status;

public class Hook implements Resultsable {

    // Start: attributes from JSON file report
    private final Result result = null;
    private final Match match = null;

    // foe Ruby reports
    private final Embedding[] embeddings = new Embedding[0];
    // End: attributes from JSON file report

    private Status status;

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Match getMatch() {
        return match;
    }

    public Embedding[] getEmbeddings() {
        return embeddings;
    }

    public void setMedaData() {
        calculateStatus();
    }

    private void calculateStatus() {
        if (result == null) {
            status = Status.MISSING;
        } else {
            status = Status.toStatus(result.getStatus());
        }
    }
}
