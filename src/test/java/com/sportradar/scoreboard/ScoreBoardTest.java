package com.sportradar.scoreboard;

import com.sportradar.scoreboard.constants.ErrorStrings;
import com.sportradar.scoreboard.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ScoreBoardTest {
    private ScoreBoard scoreBoard;

    private final String scoreBoardSummaryResult = """
        Uruguay 6 - 6 Italy
        Spain 10 - 2 Brazil
        Mexico 0 - 5 Canada
        Argentina 3 - 1 Australia
        Germany 2 - 2 France
        Denmark 1 - 1 Sweden
        Norway 1 - 1 Japan""";

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoard();
    }

    @Test
    void startMatch_shouldAddNewMatch() {
        String matchId = scoreBoard.startMatch("Mexico", "Canada");
        assertMatchExists(matchId);
    }

    @Test
    void startMatch_shouldThrowException_whenHomeTeamNameIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.startMatch(null, "Canada"))
                .withMessage(ErrorStrings.TEAM_NAMES_CANNOT_BE_NULL_OR_BLANK);
    }

    @Test
    void startMatch_shouldThrowException_whenAwayTeamNameIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.startMatch("Mexico", null))
                .withMessage(ErrorStrings.TEAM_NAMES_CANNOT_BE_NULL_OR_BLANK);
    }

    @Test
    void startMatch_shouldThrowException_whenHomeTeamNameIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.startMatch("", "Canada"))
                .withMessage(ErrorStrings.TEAM_NAMES_CANNOT_BE_NULL_OR_BLANK);
    }

    @Test
    void startMatch_shouldThrowException_whenAwayTeamNameIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.startMatch("Mexico", ""))
                .withMessage(ErrorStrings.TEAM_NAMES_CANNOT_BE_NULL_OR_BLANK);
    }

    @Test
    void startMatch_shouldThrowException_whenTeamNamesAreSame() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.startMatch("Mexico", "Mexico"))
                .withMessage(ErrorStrings.TEAM_NAMES_CANNOT_BE_SAME);
    }

    @Test
    void startMatch_shouldThrowException_whenTeamAlreadyHasOngoingMatch() {
        // Start a match for team "Spain"
        scoreBoard.startMatch("Spain", "Brazil");

        // Try to start another match where "Spain" is a home team
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.startMatch("Spain", "Argentina"))
                .withMessage(ErrorStrings.TEAM_ALREADY_PLAYING);

        // Try to start another match where "Spain" is an away team
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.startMatch("Germany", "Spain"))
                .withMessage(ErrorStrings.TEAM_ALREADY_PLAYING);
    }

    @Test
    void updateScore_shouldChangeScores() {
        String matchId = scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.updateScore(matchId, "Spain", "Brazil", 10, 2);
        assertScores(matchId, 10, 2);
    }

    @Test
    void updateScore_shouldThrowException_whenMatchIdIsInvalid() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.updateScore("invalid-id", "Spain", "Brazil", 10, 2))
                .withMessage(ErrorStrings.INVALID_MATCH_ID);
    }

    @Test
    void updateScore_shouldThrowException_whenHomeScoreIsNegative() {
        String matchId = scoreBoard.startMatch("Argentina", "Chile");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.updateScore(matchId, "Argentina", "Chile", -1, 2))
                .withMessage(ErrorStrings.SCORE_CANNOT_BE_NEGATIVE);
    }

    @Test
    void updateScore_shouldThrowException_whenAwayScoreIsNegative() {
        String matchId = scoreBoard.startMatch("Italy", "France");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.updateScore(matchId, "Italy", "France", 1, -2))
                .withMessage(ErrorStrings.SCORE_CANNOT_BE_NEGATIVE);
    }

    @Test
    void updateScore_shouldThrowException_whenTeamNamesDoNotMatch() {
        String matchId = scoreBoard.startMatch("Germany", "England");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.updateScore(matchId, "Germany", "Brazil", 3, 1))
                .withMessage(ErrorStrings.INVALID_TEAM_NAMES);
    }

    @Test
    void getMatchSummary_shouldReturnOrderedMatches() {
        startAndScoreMatch("Mexico", "Canada", 0, 5);
        startAndScoreMatch("Spain", "Brazil", 10, 2);
        startAndScoreMatch("Germany", "France", 2, 2);
        startAndScoreMatch("Uruguay", "Italy", 6, 6);
        startAndScoreMatch("Argentina", "Australia", 3, 1);
        startAndScoreMatch("Norway", "Japan", 1, 1);
        startAndScoreMatch("Denmark", "Sweden", 1, 1);

        String summary = scoreBoard.getMatchSummary();
        assertThat(summary.trim()).isEqualTo(scoreBoardSummaryResult);
    }

    @Test
    void endMatch_shouldRemoveMatch() {
        String matchId = scoreBoard.startMatch("Germany", "France");
        scoreBoard.endMatch(matchId);
        assertMatchDoesNotExist(matchId);
    }

    @Test
    void endMatch_shouldThrowException_whenMatchIdIsInvalid() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.endMatch("invalid-id"))
                .withMessage(ErrorStrings.INVALID_MATCH_ID);
    }

    // Helper Methods
    private void assertMatchExists(String matchId) {
        assertThat(scoreBoard.getMatchRepository().getMatch(matchId)).isNotNull();
    }

    private void assertMatchDoesNotExist(String matchId) {
        assertThat(scoreBoard.getMatchRepository().getMatch(matchId)).isNull();
    }

    private void assertScores(String matchId, int homeScore, int awayScore) {
        Match match = scoreBoard.getMatchRepository().getMatch(matchId);
        assertThat(match.getHomeTeam().getScore()).isEqualTo(homeScore);
        assertThat(match.getAwayTeam().getScore()).isEqualTo(awayScore);
    }

    private void startAndScoreMatch(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        String matchId = scoreBoard.startMatch(homeTeam, awayTeam);
        scoreBoard.updateScore(matchId, homeTeam, awayTeam, homeScore, awayScore);
    }
}
