package org.example.worldCupScoreBoard.Entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FootballMatch {

    private static final AtomicLong idCounter = new AtomicLong(1);

    private final Long id;
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

        if(homeTeam.equals(awayTeam)){
            throw new IllegalArgumentException("Teams can't be the same");
        }

        this.id = idCounter.getAndIncrement();

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;

        this.startTime = LocalDateTime.now();

        this.matchActive = true;

    }

    private Long getId() {
        return id;
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

    public int getAwayScore() {
        return awayScore;
    }

    public LocalDateTime getStartTime() {

        return startTime;
    }

    public boolean isMatchActive() {
        return matchActive;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FootballMatch that = (FootballMatch) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FootballMatch{" +
                "id=" + id +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                ", startTime=" + startTime +
                ", matchActive=" + matchActive +
                '}';
    }
}
