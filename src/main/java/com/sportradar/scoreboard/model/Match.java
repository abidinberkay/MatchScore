package com.sportradar.scoreboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The Match class represents a match between two teams.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    private Team homeTeam;   // The home team
    private Team awayTeam;   // The away team
    private LocalDateTime startTime; // When the match started
}