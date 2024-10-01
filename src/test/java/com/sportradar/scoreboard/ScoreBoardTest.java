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
        assertThat(scoreBoard.getMatches()).containsKey(matchId);
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
        scoreBoard.updateScore(matchId, 10, 2);
        Match match = scoreBoard.getMatches().get(matchId);
        assertThat(match.getHomeTeam().getScore()).isEqualTo(10);
        assertThat(match.getAwayTeam().getScore()).isEqualTo(2);
    }
}
