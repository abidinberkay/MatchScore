package com.sportradar.scoreboard.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * The Match class represents a match between two teams.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    private Team homeTeam;   // The home team
    private Team awayTeam;   // The away team
    private LocalDateTime startTime; // When the match started

    /**
     * Calculates the total score of the match.
     *
     * @return the total score as an integer
     */
    public int getTotalScore() {
        return homeTeam.getScore() + awayTeam.getScore(); // Sum the scores of both teams
    }
}
