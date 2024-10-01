package com.sportradar.scoreboard;

import com.sportradar.scoreboard.constants.ErrorStrings;
import com.sportradar.scoreboard.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ScoreBoardTest {
    private ScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoard();
    }

    @Test
    void startMatch_shouldAddNewMatch() {
        String matchId = scoreBoard.startMatch("Mexico", "Canada");
        // Accessing matches through the MatchRepository
        assertThat(scoreBoard.getMatchRepository().getMatch(matchId)).isNotNull();
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
    void updateScore_shouldChangeScores() {
        String matchId = scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.updateScore(matchId, "Spain", "Brazil", 10, 2); // Pass team names for validation
        Match match = scoreBoard.getMatchRepository().getMatch(matchId); // Accessing through MatchRepository
        assertThat(match.getHomeTeam().getScore()).isEqualTo(10);
        assertThat(match.getAwayTeam().getScore()).isEqualTo(2);
    }

    @Test
    void updateScore_shouldThrowException_whenMatchIdIsInvalid() {
        String invalidMatchId = "invalid-id";
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> scoreBoard.updateScore(invalidMatchId, "Spain", "Brazil", 10, 2))
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
    void getMatchSummary_shouldReturnOrderedMatches() throws InterruptedException {
        String matchId1 = scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.updateScore(matchId1, "Mexico", "Canada", 0, 5);
        Thread.sleep(1);
        String matchId2 = scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.updateScore(matchId2, "Spain", "Brazil", 10, 2);
        Thread.sleep(1);
        String matchId3 = scoreBoard.startMatch("Germany", "France");
        scoreBoard.updateScore(matchId3, "Germany", "France", 2, 2);
        Thread.sleep(1);
        String matchId4 = scoreBoard.startMatch("Uruguay", "Italy");
        scoreBoard.updateScore(matchId4, "Uruguay", "Italy", 6, 6);
        Thread.sleep(1);
        String matchId5 = scoreBoard.startMatch("Argentina", "Australia");
        scoreBoard.updateScore(matchId5, "Argentina", "Australia", 3, 1);
        Thread.sleep(1);
        String matchId6 = scoreBoard.startMatch("Norway", "Japan");
        scoreBoard.updateScore(matchId6, "Norway", "Japan", 1, 1);
        Thread.sleep(1);
        String matchId7 = scoreBoard.startMatch("Denmark", "Sweden");
        scoreBoard.updateScore(matchId7, "Denmark", "Sweden", 1, 1);

        String summary = scoreBoard.getMatchSummary();
        assertThat(summary.trim()).isEqualTo(
                "Uruguay 6 - 6 Italy\n" +
                        "Spain 10 - 2 Brazil\n" +
                        "Mexico 0 - 5 Canada\n" +
                        "Argentina 3 - 1 Australia\n" +
                        "Germany 2 - 2 France\n" +
                        "Denmark 1 - 1 Sweden\n" +
                        "Norway 1 - 1 Japan");
    }
}
