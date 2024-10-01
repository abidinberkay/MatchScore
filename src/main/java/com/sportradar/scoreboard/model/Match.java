package com.sportradar.scoreboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Match {
    private final Team homeTeam;
    private final Team awayTeam;
    private final LocalDateTime startTime;

    public int getTotalScore() {
        return homeTeam.getScore() + awayTeam.getScore();
    }
}