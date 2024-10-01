package com.sportradar.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

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
}
