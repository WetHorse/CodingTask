package org.example.worldCupScoreBoard.Repository;

import org.example.worldCupScoreBoard.Entity.FootballMatch;

import java.util.*;
import java.util.stream.Collectors;

public class FootballMatchRepository {

    private final Map<Long, FootballMatch> footballMatches = new HashMap<Long, FootballMatch>();

    public Optional<FootballMatch> findById(long id) {
        return Optional.ofNullable(footballMatches.get(id));
    }

    public boolean existsById(long id) {
        return footballMatches.containsKey(id) &&
                footballMatches.get(id) != null &&
                footballMatches.get(id).isMatchActive();
    }

    public void saveMatch(FootballMatch footballMatch) {
        footballMatches.put(footballMatch.getId(), footballMatch);
    }

    public void deleteById(long id) {
        footballMatches.remove(id);
    }

    public void delete(FootballMatch footballMatch) {
        footballMatches.remove(footballMatch.getId());
    }

    public List<FootballMatch> getAllMatches() {
        return new ArrayList<FootballMatch>(footballMatches.values());
    }

    public List<FootballMatch> getAllActiveMatches() {
        return footballMatches.values().stream().filter(FootballMatch::isMatchActive).collect(Collectors.toList());
    }

    public boolean isFootballMatchExists(String homeTeam, String awayTeam) {
        return footballMatches.values().stream()
                .anyMatch(footballMatch ->
                        footballMatch.getHomeTeam().equalsIgnoreCase(homeTeam)
                && footballMatch.getAwayTeam().equalsIgnoreCase(awayTeam));
    }

    public Optional<FootballMatch> getFootballMatchByTeams(String homeTeam, String awayTeam) {
        return footballMatches.values().stream()
                .filter(footballMatch ->
                        footballMatch.getHomeTeam().equalsIgnoreCase(homeTeam)
                && footballMatch.getAwayTeam().equalsIgnoreCase(awayTeam)).findFirst();
    }

}
