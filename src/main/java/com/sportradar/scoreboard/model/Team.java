package com.sportradar.scoreboard.model;

import lombok.*;

/**
 * The Team class represents a sports team with a name and score.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private String name; // Name of the team
    private int score;   // Current score of the team
}
