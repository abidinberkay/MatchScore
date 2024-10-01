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
        validateTeamNames(homeTeamName, awayTeamName);

        LocalDateTime startTime = LocalDateTime.now(); // Current time as the start time

        Match match = new Match(new Team(homeTeamName, 0), new Team(awayTeamName, 0), startTime);
        String matchId = generateMatchId();
        matchRepository.addMatch(matchId, match);
        return matchId;
    }

    /**
     * Updates the score of a match.
     *
     * @param matchId      the id of the match
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @param homeScore    the score of the home team
     * @param awayScore    the score of the away team
     * @throws IllegalArgumentException if team names are invalid or if scores are negative
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
     * Retrieves a summary of all matches, including the scores of the home and away teams.
     *
     * @return a string containing the summary of all matches, formatted as "HomeTeam Score - AwayTeam Score"
     */
    public String getMatchSummary() {
        StringBuilder summary = new StringBuilder();

        matchRepository.getAllMatches().values().stream()
                .sorted(this::compareMatches)
                .forEach(match -> appendMatchSummary(summary, match));

        return summary.toString();
    }

    /**
     * Ends a match by its ID and removes it from the repository.
     *
     * @param matchId the ID of the match to end
     * @throws IllegalArgumentException if the match ID is invalid or the match does not exist
     */
    public void endMatch(String matchId) {
        Match match = matchRepository.getMatch(matchId);
        if (match == null) {
            throw new IllegalArgumentException(ErrorStrings.INVALID_MATCH_ID); // Handle invalid match ID
        }
        matchRepository.removeMatch(matchId); // Remove match from the repository
    }

    /**
     * Compares two matches for ordering based on total score and start time.
     *
     * @param m1 the first match to compare
     * @param m2 the second match to compare
     * @return a negative integer, zero, or a positive integer as the first match
     * is less than, equal to, or greater than the second match
     */
    private int compareMatches(Match m1, Match m2) {
        // Compare by total score in descending order
        int totalScoreComparison = Integer.compare(m2.getTotalScore(), m1.getTotalScore());

        // If scores are equal, compare by start time in descending order
        if (totalScoreComparison == 0) {
            return m2.getStartTime().compareTo(m1.getStartTime()); // More recent first
        }

        return totalScoreComparison; // Otherwise return the total score comparison
    }

    /**
     * Appends the summary of a match to the provided StringBuilder.
     *
     * @param summary the StringBuilder to which the match summary will be appended
     * @param match   the match to summarize
     */
    private void appendMatchSummary(StringBuilder summary, Match match) {
        summary.append(match.getHomeTeam().getName())
                .append(" ").append(match.getHomeTeam().getScore())
                .append(" - ").append(match.getAwayTeam().getScore())
                .append(" ").append(match.getAwayTeam().getName())
                .append("\n");
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
