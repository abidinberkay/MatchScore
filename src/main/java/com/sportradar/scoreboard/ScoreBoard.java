package com.sportradar.scoreboard;

import com.sportradar.scoreboard.constants.ErrorStrings;
import com.sportradar.scoreboard.model.Match;
import com.sportradar.scoreboard.model.Team;
import com.sportradar.scoreboard.repository.MatchRepository;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The ScoreBoard class represents a scoreboard for tracking ongoing matches.
 * It allows starting new matches, updating scores, and retrieving match information.
 */
@Getter
public class ScoreBoard {
    private final MatchRepository matchRepository = new MatchRepository(); // Initialize repository

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
        Match match = new Match(new Team(homeTeamName, 0), new Team(awayTeamName, 0), LocalDateTime.now());
        String matchId = generateMatchId();
        matchRepository.addMatch(matchId, match); // Use repository to add match
        return matchId; // Return match ID for reference
    }

    /**
     * Update score of a match
     *
     * @param matchId the id of the match
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @param homeScore the score of the home team
     * @param awayScore the score of the away team
     */
    public void updateScore(String matchId, String homeTeamName, String awayTeamName, int homeScore, int awayScore) {
        validateTeamNames(homeTeamName, awayTeamName); // Validate team names

        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException(ErrorStrings.SCORE_CANNOT_BE_NEGATIVE); // Handle negative scores
        }

        Match match = matchRepository.getMatch(matchId);
        if (match == null) {
            throw new IllegalArgumentException(ErrorStrings.INVALID_MATCH_ID); // Handle invalid match ID
        }

        // Check if the match teams match the given team names before updating scores
        if (!match.getHomeTeam().getName().equals(homeTeamName) || !match.getAwayTeam().getName().equals(awayTeamName)) {
            throw new IllegalArgumentException(ErrorStrings.INVALID_TEAM_NAMES); // Handle mismatched team names
        }

        // Update scores
        match.getHomeTeam().setScore(homeScore);
        match.getAwayTeam().setScore(awayScore);
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
