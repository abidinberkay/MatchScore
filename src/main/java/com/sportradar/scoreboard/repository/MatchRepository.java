package com.sportradar.scoreboard.repository;

import com.sportradar.scoreboard.model.Match;

import java.util.HashMap;
import java.util.Map;

/**
 * The MatchRepository class manages the collection of matches.
 */
public class MatchRepository {
    private final Map<String, Match> matches = new HashMap<>(); // Store matches by their ID

    /**
     * Adds a new match to the repository.
     *
     * @param matchId the ID of the match
     * @param match   the Match object to add
     */
    public void addMatch(String matchId, Match match) {
        matches.put(matchId, match);
    }

    /**
     * Retrieves a match by its ID.
     *
     * @param matchId the ID of the match
     * @return the Match object or null if not found
     */
    public Match getMatch(String matchId) {
        return matches.get(matchId);
    }

    /**
     * Retrieves all matches.
     *
     * @return a map of all matches
     */
    public Map<String, Match> getAllMatches() {
        return matches;
    }

    /**
     * Removes a match from the repository by its ID.
     *
     * @param matchId the ID of the match to remove
     * @return the removed Match object or null if not found
     */
    public Match removeMatch(String matchId) {
        return matches.remove(matchId);
    }
}
