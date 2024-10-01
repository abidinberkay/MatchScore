package com.sportradar.scoreboard.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private String name;

    @Setter
    private int score = 0; // Default score initialized to 0

    // New constructor to allow instantiation with only the team name
    public Team(String name) {
        this.name = name;
    }
}
