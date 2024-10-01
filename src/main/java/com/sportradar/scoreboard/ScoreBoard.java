package com.sportradar.scoreboard;

import com.sportradar.scoreboard.constants.ErrorStrings;
import com.sportradar.scoreboard.model.Match;
import com.sportradar.scoreboard.model.Team;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

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
     * @throws IllegalArgumentException if team names are null, blank, or the same
     */
    public String startMatch(String homeTeamName, String awayTeamName) {
        validateTeamNames(homeTeamName, awayTeamName); // Validate team names
        Match match = new Match(new Team(homeTeamName), new Team(awayTeamName), LocalDateTime.now());
        String matchId = generateMatchId();
        matches.put(matchId, match);
        return matchId; // Return match ID for reference
    }

    /**
     * Generates a unique match ID using UUID.
     *
     * @return a unique string representing the match ID
     */
    private String generateMatchId() {
        return UUID.randomUUID().toString(); // Generate a unique UUID for the match ID
    }

    /**
     * Validates the names of the home and away teams.
     *
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @throws IllegalArgumentException if either team name is null, blank, or if the names are the same
     */
    private void validateTeamNames(String homeTeamName, String awayTeamName) {
        if (StringUtils.isBlank(homeTeamName) || StringUtils.isBlank(awayTeamName)) {
            throw new IllegalArgumentException(ErrorStrings.TEAM_NAMES_CANNOT_BE_NULL_OR_BLANK);
        }
        if (homeTeamName.equals(awayTeamName)) {
            throw new IllegalArgumentException(ErrorStrings.TEAM_NAMES_CANNOT_BE_SAME);
        }
    }
}