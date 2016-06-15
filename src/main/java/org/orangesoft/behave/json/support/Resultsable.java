package org.orangesoft.behave.json.support;

import org.orangesoft.behave.json.Match;
import org.orangesoft.behave.json.Result;

/**
 * Ensures that class delivers method for counting results and matches.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public interface Resultsable {

    Result getResult();

    Status getStatus();

    Match getMatch();
}
