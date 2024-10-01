package com.sportradar.scoreboard;

import com.sportradar.scoreboard.model.Match;
import com.sportradar.scoreboard.model.Team;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The ScoreBoard class represents a scoreboard for tracking ongoing matches.
 * It allows starting new matches, updating scores, and retrieving match information.
 *
 * @author berkaysimsek
 * @version 0.0.1
 */
@Getter
public class ScoreBoard {
    private final Map<String, Match> matches = new LinkedHashMap<>();

    /**
     * Starts a new match with the given home team and away team.
     *
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @return the MatchId representing the started match id
     */
    public String startMatch(String homeTeamName, String awayTeamName) {
        Match match = new Match(new Team(homeTeamName), new Team(awayTeamName), LocalDateTime.now());
        String matchId = generateMatchId();
        matches.put(matchId, match);
        return matchId; // Return match ID for reference
    }

    private String generateMatchId() {
        return UUID.randomUUID().toString(); // Generate a unique UUID for the match ID
    }
}