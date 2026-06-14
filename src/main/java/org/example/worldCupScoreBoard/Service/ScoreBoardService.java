package org.example.worldCupScoreBoard.Service;

import org.example.worldCupScoreBoard.Repository.FootballMatchRepository;

public class ScoreBoardService {

    private final FootballMatchRepository footballMatchRepository;

    public ScoreBoardService(FootballMatchRepository footballMatchRepository) {
        this.footballMatchRepository = footballMatchRepository;
    }

}
