package com.sportradar.scoreboard.constants;

public class ErrorStrings {
    public static final String TEAM_NAMES_CANNOT_BE_NULL_OR_BLANK = "Home and away team names cannot be null or blank.";
    public static final String TEAM_NAMES_CANNOT_BE_SAME = "Home and away team names cannot be the same.";
    public static final String SCORE_CANNOT_BE_NEGATIVE = "Score cannot be negative.";
    public static final String INVALID_MATCH_ID = "Match does not exist with given Id";
    public static final String INVALID_TEAM_NAMES = "Match teams does not match with given team names";
    public static final String TEAM_ALREADY_PLAYING = "Team already have ongoing match";

    // Private constructor to prevent instantiation
    private ErrorStrings() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
