package com.sportradar.scoreboard;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ScoreBoardTest {

    @Test
    public void shouldStartNewMatchWithInitialScore() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();

        // When
        scoreBoard.startMatch("Mexico", "Canada");

        // Then
        Match match = scoreBoard.getMatch("Mexico", "Canada");
        assertThat(match.getHomeTeam().getScore()).isEqualTo(0);
        assertThat(match.getAwayTeam().getScore()).isEqualTo(0);
        assertThat(match.getHomeTeam().getName()).isEqualTo("Mexico");
        assertThat(match.getAwayTeam().getName()).isEqualTo("Canada");
    }
}
