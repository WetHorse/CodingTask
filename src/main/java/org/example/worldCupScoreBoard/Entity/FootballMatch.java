package org.example.worldCupScoreBoard.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class FootballMatch {

    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private LocalDateTime startTime;
    private boolean matchActive;

    public FootballMatch(String homeTeam, String awayTeam) {

        if(homeTeam == null || homeTeam.trim().isEmpty()){
            throw new IllegalArgumentException("Home team can't be null or empty");
        }

        if(awayTeam == null || awayTeam.trim().isEmpty()){
            throw new IllegalArgumentException("Away team can't be null or empty");
        }

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;

        this.startTime = LocalDateTime.now();

        this.matchActive = true;

    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FootballMatch that = (FootballMatch) o;
        return homeScore == that.homeScore && awayScore == that.awayScore && matchActive == that.matchActive && Objects.equals(homeTeam, that.homeTeam) && Objects.equals(awayTeam, that.awayTeam) && Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam, homeScore, awayScore, startTime, matchActive);
    }

    @Override
    public String toString() {
        return "FootballMatch{" +
                "homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                ", startTime=" + startTime +
                ", matchActive=" + matchActive +
                '}';
    }
}
