package org.example.worldCupScoreBoard.Service;

import org.example.worldCupScoreBoard.Entity.FootballMatch;
import org.example.worldCupScoreBoard.Exception.MatchNotFoundException;
import org.example.worldCupScoreBoard.Repository.FootballMatchRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreBoardService {

    private final FootballMatchRepository footballMatchRepository;

    public ScoreBoardService(FootballMatchRepository footballMatchRepository) {
        this.footballMatchRepository = footballMatchRepository;
    }

    public FootballMatch getFootballMatch(int matchId) {
        FootballMatch footballMatch = footballMatchRepository.findById(matchId).orElseThrow(() -> new MatchNotFoundException("Match not found with id " + matchId));
        return footballMatch;
    }

    public FootballMatch startFootballMatch(String homeTeam, String awayTeam) {

        if (homeTeam == null) {
            throw new IllegalArgumentException("Home Team cannot be null");
        }
        if (awayTeam == null) {
            throw new IllegalArgumentException("Away Team cannot be null");
        }
        if (homeTeam.trim().isEmpty()) {
            throw new IllegalArgumentException("Home Team cannot be empty");
        }
        if (awayTeam.trim().isEmpty()) {
            throw new IllegalArgumentException("Away Team cannot be empty");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Home team and Away team must be different");
        }

        if(footballMatchRepository.isFootballMatchExists(homeTeam, awayTeam)) {
            throw new IllegalArgumentException("Football match already exists with team " + homeTeam + " and team " + awayTeam);
        }

        FootballMatch footballMatch = new FootballMatch(homeTeam, awayTeam);
        footballMatchRepository.saveMatch(footballMatch);
        return footballMatch;
    }

    public void updateScoreBoard(int homeTeamScore, int awayTeamScore, Long footballMatchId) {
        FootballMatch footballMatch = footballMatchRepository.findById(footballMatchId).orElseThrow(() -> new MatchNotFoundException("Match not found with id " + footballMatchId));
        footballMatch.updateScore(homeTeamScore, awayTeamScore);
    }

    public void updateScoreBoard(int homeTeamScore, int awayTeamScore, String homeTeam, String awayTeam) {
        FootballMatch footballMatch = footballMatchRepository.getFootballMatchByTeams(homeTeam,  awayTeam).orElseThrow(() -> new MatchNotFoundException("Match not found with team " + homeTeam + " and team " + awayTeam));
        footballMatch.updateScore(homeTeamScore, awayTeamScore);
    }

    public void endFootballMatch(String homeTeam, String awayTeam) {
        FootballMatch footballMatch = footballMatchRepository.getFootballMatchByTeams(homeTeam, awayTeam)
                .orElseThrow(() -> new MatchNotFoundException("Match not found with team " + homeTeam + " and team " + awayTeam));

        footballMatch.finishMatch();
        footballMatchRepository.delete(footballMatch);
    }

    public void endFootballMatchById(Long id){
        FootballMatch footballMatch = footballMatchRepository.findById(id).orElseThrow(() -> new MatchNotFoundException("Match not found with id " + id));
        footballMatch.finishMatch();
        footballMatchRepository.delete(footballMatch);
    }

    public List<FootballMatch> summarizeTheScoreBoard() {
        return footballMatchRepository.getAllActiveMatches().stream()
                .sorted(Comparator.comparingInt(FootballMatch::getTotalScore)
                        .reversed().thenComparing(FootballMatch::getId, Comparator.reverseOrder())).collect(Collectors.toList());
    }


}
