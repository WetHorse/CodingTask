package org.example;

import org.example.worldCupScoreBoard.Entity.FootballMatch;
import org.example.worldCupScoreBoard.Repository.FootballMatchRepository;
import org.example.worldCupScoreBoard.Service.ScoreBoardService;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        FootballMatchRepository footballMatchRepository = new FootballMatchRepository();
        ScoreBoardService scoreBoardService = new ScoreBoardService(footballMatchRepository);

        FootballMatch footballMatch0 = scoreBoardService.startFootballMatch("Belarus", "Lithuania");
        scoreBoardService.updateScoreBoard(4, 2, footballMatch0.getId());

        FootballMatch footballMatch1 = scoreBoardService.startFootballMatch("Ireland", "Portugal");
        scoreBoardService.updateScoreBoard(0, 3, footballMatch1.getId());

        FootballMatch footballMatch2 = scoreBoardService.startFootballMatch("Germany", "Russia");
        scoreBoardService.updateScoreBoard(7, 1, footballMatch2.getId());

        FootballMatch footballMatch3 = scoreBoardService.startFootballMatch("Italy", "France");
        scoreBoardService.updateScoreBoard(1, 1, footballMatch3.getId());

        FootballMatch footballMatch4 = scoreBoardService.startFootballMatch("Ukraine", "Canada");
        scoreBoardService.updateScoreBoard(2, 2, footballMatch4.getId());

        FootballMatch footballMatch5 = scoreBoardService.startFootballMatch("Estonia", "Suomi");
        scoreBoardService.updateScoreBoard(3, 1, footballMatch5.getId());

        FootballMatch footballMatch6 = scoreBoardService.startFootballMatch("Belgium", "Austria");
        scoreBoardService.updateScoreBoard(0, 0, footballMatch6.getId());

        FootballMatch footballMatch7 = scoreBoardService.startFootballMatch("Poland", "Sweden");
        scoreBoardService.updateScoreBoard(8, 3, footballMatch7.getId());

        FootballMatch footballMatch8 = scoreBoardService.startFootballMatch("Romania", "Egypt");
        scoreBoardService.updateScoreBoard(0, 0, footballMatch8.getId());

        //summarize
        scoreBoardSummary(scoreBoardService);

        //update score
        System.out.println("Match: " + footballMatch1.getHomeTeam() + " " + footballMatch1.getAwayTeam() + "updated score\n");
        scoreBoardService.updateScoreBoard(6, 6, footballMatch1.getId());

        System.out.println("Match: " + footballMatch7.getHomeTeam() + " " + footballMatch1.getAwayTeam() + "updated score\n");
        scoreBoardService.updateScoreBoard(8, 5, footballMatch7.getId());

        //finish football match
        System.out.println("Match: " + footballMatch5.getHomeTeam() + " " + footballMatch5.getAwayTeam() + "match finished\n");
        scoreBoardService.endFootballMatchByIt(footballMatch1.getId());

        //summarize again

        System.out.println("Changes applied");
        scoreBoardSummary(scoreBoardService);
    }


    public static void scoreBoardSummary(ScoreBoardService scoreBoardService) {

        System.out.println();
        System.out.println("---ScoreBoard summary---\n");

        List<FootballMatch> scoreBoard = scoreBoardService.summarizeTheScoreBoard();

        for(int i = 0; i<scoreBoard.size(); i++){
            FootballMatch footballMatch = scoreBoard.get(i);
            System.out.println(
                    i + 1 + ". "
                            + footballMatch.getId() + ": "
                            + footballMatch.getHomeTeam() + " " +footballMatch.getHomeScore() + " - "
                            + footballMatch.getAwayTeam() + " " +footballMatch.getAwayScore() + " : Total score - "
                            + footballMatch.getTotalScore());
        }
        System.out.println();
    }
}