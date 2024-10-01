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
}
