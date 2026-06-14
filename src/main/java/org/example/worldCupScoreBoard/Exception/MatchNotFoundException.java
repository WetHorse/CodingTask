package org.example.worldCupScoreBoard.Exception;

public class MatchNotFoundException extends RuntimeException{
    public MatchNotFoundException(String message){
        super(message);
    }
}
