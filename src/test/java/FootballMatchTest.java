import org.example.worldCupScoreBoard.Entity.FootballMatch;
import org.example.worldCupScoreBoard.Exception.MatchNotFoundException;
import org.example.worldCupScoreBoard.Repository.FootballMatchRepository;
import org.example.worldCupScoreBoard.Service.ScoreBoardService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FootballMatchTest {

    private FootballMatchRepository footballMatchRepository;
    private ScoreBoardService scoreBoardService;

    @BeforeEach
    void setUp() {
        footballMatchRepository = new FootballMatchRepository();
        scoreBoardService = new ScoreBoardService(footballMatchRepository);
    }

    @Nested
    @DisplayName("\n Start Test")
    class StartTest {

        @Test
        @DisplayName("Exception should drop message")
        void dropMatchNotFoundException(){
            String message = "Match not found";
            MatchNotFoundException matchNotFoundException = new MatchNotFoundException(message);
            assertEquals(message, matchNotFoundException.getMessage());
        }

        @Test
        @DisplayName("Game should start with zero score")
        void gameShouldStartWithZeroScore() {
            FootballMatch footballMatch = scoreBoardService.startFootballMatch("Belarus", "Lithuania");

            assert(footballMatch != null);
            assertEquals("Belarus", footballMatch.getHomeTeam());
            assertEquals("Lithuania", footballMatch.getAwayTeam());
            assertEquals(0, footballMatch.getHomeScore());
            assertEquals(0, footballMatch.getAwayScore());
            assertTrue(footballMatch.isMatchActive());
        }

        @Test
        @DisplayName("Home team can't be null")
        void homeTeamCannotBeNull() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scoreBoardService.startFootballMatch(null, "Lithuania"));
            assertTrue(illegalArgumentException.getMessage().contains("Home Team cannot be null"));
        }

        @Test
        @DisplayName("Away team can't be null")
        void awayTeamCannotBeNull() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scoreBoardService.startFootballMatch("Belarus", null));
            assertTrue(illegalArgumentException.getMessage().contains("Away Team cannot be null"));
        }

        @Test
        @DisplayName("Game can't be started with the same teams")
        void gameCannotBeStartedWithTheSameTeams() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> scoreBoardService.startFootballMatch("Belarus", "Belarus"));
            assertEquals(exception.getMessage(), "Home team and Away team must be different");
        }


        @Test
        @DisplayName("Home team cannot be empty")
        void homeTeamCannotBeEmpty() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scoreBoardService.startFootballMatch("", "Lithuania"));
            assertTrue(illegalArgumentException.getMessage().contains("Home Team cannot be empty"));
        }

        @Test
        @DisplayName("Away team cannot be empty")
        void awayTeamCannotBeEmpty() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scoreBoardService.startFootballMatch("Belarus", ""));
            assertTrue(illegalArgumentException.getMessage().contains("Away Team cannot be empty"));
        }
    }

    @Nested
    @DisplayName("Update Score tests")
    class UpdateScoreTest {

        @Test
        @DisplayName("Update score test")
        void updateScoreTest() {
            FootballMatch footballMatch = scoreBoardService.startFootballMatch("Belarus", "Lithuania");
            scoreBoardService.updateScoreBoard(5, 3, footballMatch.getId());

            assertEquals(5, footballMatch.getHomeScore());
            assertEquals(3, footballMatch.getAwayScore());
        }


        @Test
        @DisplayName("Drop exception when updating finished match")
        void dropExceptionWhenUpdatingFinishedMatch() {
            FootballMatch footballMatch = scoreBoardService.startFootballMatch("Belarus", "Lithuania");
            scoreBoardService.endFootballMatchByIt(footballMatch.getId());

            MatchNotFoundException matchNotFoundException = assertThrows(MatchNotFoundException.class, () -> scoreBoardService.updateScoreBoard(5, 3, footballMatch.getId()));
            assertEquals("Match not found with id " + footballMatch.getId(), matchNotFoundException.getMessage());
        }

        @Test
        @DisplayName("Drop exception when match not exists")
        void dropExceptionWhenMatchNotExists() {
            MatchNotFoundException matchNotFoundException = assertThrows(MatchNotFoundException.class, () -> scoreBoardService.updateScoreBoard(5, 3, 1L));
            assertEquals("Match not found with id 1", matchNotFoundException.getMessage());
        }

        @Test
        @DisplayName("Drop exception when score is negative")
        void dropExceptionWhenScoreIsNegative() {
            FootballMatch footballMatch = scoreBoardService.startFootballMatch("Belarus", "Lithuania");

            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> scoreBoardService.updateScoreBoard(-5, 3, footballMatch.getId()));
            assertEquals("Score can't be negative", illegalArgumentException.getMessage());
        }
    }

    @Nested
    @DisplayName("Finish Match tests")
    class FinishGameTests {

        @Test
        @DisplayName("Match should be active")
        void matchIsActive() {
            FootballMatch footballMatch = scoreBoardService.startFootballMatch("Belarus", "Lithuania");
            scoreBoardService.endFootballMatchByIt(footballMatch.getId());

            assertFalse(footballMatch.isMatchActive());
            assertTrue(footballMatchRepository.findById(footballMatch.getId()).isEmpty());
        }

        @Test
        @DisplayName("Drop exception when game is not finished")
        void dropExceptionWhenGameIsNotFinished() {
            MatchNotFoundException matchNotFoundException = assertThrows(MatchNotFoundException.class, () -> scoreBoardService.endFootballMatchByIt(999L));
            assertEquals("Match not found with id 999", matchNotFoundException.getMessage());
        }

        @Test
        @DisplayName("Game should be removed from active")
        void gameIsRemovedFromActive() {
            FootballMatch footballMatch0 = scoreBoardService.startFootballMatch("Belarus", "Lithuania");
            FootballMatch footballMatch1 = scoreBoardService.startFootballMatch("Czech Republic", "Latvia");

            assertEquals(2, footballMatchRepository.getAllActiveMatches().size());
            scoreBoardService.endFootballMatchByIt(footballMatch0.getId());

            assertEquals(1, footballMatchRepository.getAllActiveMatches().size());
            assertEquals(footballMatch1.getId(), footballMatchRepository.getAllActiveMatches().get(0).getId());
        }
    }

    @Nested
    @DisplayName("ScoreBoard summarize tests")
    class ScoreBoardSummarizeTests {

        @Test
        @DisplayName("Return empty list when no games")
        void shouldReturnEmptyListWhenNoGames() {
            List<FootballMatch> summary = scoreBoardService.summarizeTheScoreBoard();

            assertTrue(summary.isEmpty());
        }

        @Test
        @DisplayName("Sort by recently added when total scores are equal")
        void shouldSortByRecencyWhenTotalScoresEqual() throws InterruptedException {

            FootballMatch match1 = scoreBoardService.startFootballMatch("Ukraine", "Canada");
            scoreBoardService.updateScoreBoard(2, 2, match1.getId());

            FootballMatch match2 = scoreBoardService.startFootballMatch("Estonia", "Suomi");
            scoreBoardService.updateScoreBoard(3, 1, match2.getId());

            List<FootballMatch> summary = scoreBoardService.summarizeTheScoreBoard();

            assertEquals(2, summary.size());
            assertEquals("Estonia", summary.get(0).getHomeTeam());
            assertEquals("Ukraine", summary.get(1).getHomeTeam());
        }

        @Test
        @DisplayName("Sort by total score")
        void shouldSortByTotalScoreDescending() {
            FootballMatch match1 = scoreBoardService.startFootballMatch("France", "Canada");
            scoreBoardService.updateScoreBoard(0, 5, match1.getId());

            FootballMatch match2 = scoreBoardService.startFootballMatch("Latvia", "Sweden");
            scoreBoardService.updateScoreBoard(10, 2, match2.getId());

            FootballMatch match3 = scoreBoardService.startFootballMatch("Germany", "Spain");
            scoreBoardService.updateScoreBoard(2, 2, match3.getId());

            List<FootballMatch> summary = scoreBoardService.summarizeTheScoreBoard();

            assertEquals(3, summary.size());
            assertEquals("Latvia", summary.get(0).getHomeTeam());
            assertEquals("France", summary.get(1).getHomeTeam());
            assertEquals("Germany", summary.get(2).getHomeTeam());
        }

        @Test
        @DisplayName("Ignore finished games in summary")
        void shouldNotIncludeFinishedGamesInSummary() {
            FootballMatch match0 = scoreBoardService.startFootballMatch("Poland", "Sweden");
            scoreBoardService.updateScoreBoard(8, 3, match0.getId());

            FootballMatch match1 = scoreBoardService.startFootballMatch("Germany", "Russia");
            scoreBoardService.updateScoreBoard(7, 1, match1.getId());

            scoreBoardService.endFootballMatchByIt(match0.getId());

            List<FootballMatch> summary = scoreBoardService.summarizeTheScoreBoard();

            assertEquals(1, summary.size());
            assertEquals("Germany", summary.get(0).getHomeTeam());
        }

        @Test
        @DisplayName("Should handle zero score games correctly")
        void shouldHandleZeroScoreGamesCorrectly() throws InterruptedException {
            FootballMatch match0 = scoreBoardService.startFootballMatch("Belgium", "Austria");
            scoreBoardService.updateScoreBoard(0, 0, match0.getId());

            FootballMatch match1 = scoreBoardService.startFootballMatch("Romania", "Egypt");
            scoreBoardService.updateScoreBoard(0, 0, match1.getId());

            List<FootballMatch> summary = scoreBoardService.summarizeTheScoreBoard();

            assertEquals(2, summary.size());
            assertEquals("Romania", summary.get(0).getHomeTeam());
            assertEquals("Belgium", summary.get(1).getHomeTeam());
        }

    }

    @Nested
    @DisplayName("Post-update integration test")
    class PostUpdateIntegrationTest {

        @Test
        @DisplayName("Complete whole flow test")
        void completeWorkflowTest() throws InterruptedException {

            FootballMatch footballMatch0 = scoreBoardService.startFootballMatch("Brazil", "Germany");
            FootballMatch footballMatch1 = scoreBoardService.startFootballMatch("France", "Italy");

            scoreBoardService.updateScoreBoard(1, 0, footballMatch0.getId());
            scoreBoardService.updateScoreBoard(2, 2, footballMatch1.getId());

            List<FootballMatch> summary0 = scoreBoardService.summarizeTheScoreBoard();
            assertEquals(2, summary0.size());

            scoreBoardService.updateScoreBoard(7, 1, footballMatch0.getId());


            FootballMatch footballMatch2 = scoreBoardService.startFootballMatch("Argentina", "Netherlands");
            scoreBoardService.updateScoreBoard(3, 0, footballMatch2.getId());

            List<FootballMatch> finalSummary = scoreBoardService.summarizeTheScoreBoard();
            assertEquals(3, finalSummary.size());

            assertEquals("Brazil", finalSummary.get(0).getHomeTeam());
            assertEquals(7, finalSummary.get(0).getHomeScore());


            scoreBoardService.endFootballMatchByIt(footballMatch1.getId());

            List<FootballMatch> afterFinish = scoreBoardService.summarizeTheScoreBoard();
            assertEquals(2, afterFinish.size());
        }

        @Test
        @DisplayName("Hold order after score updates")
        void shouldMaintainOrderAfterScoreUpdates() throws InterruptedException {
            FootballMatch footballMatch0 = scoreBoardService.startFootballMatch("Argentina", "Netherlands");
            scoreBoardService.updateScoreBoard(1, 1, footballMatch0.getId());


            FootballMatch footballMatch1 = scoreBoardService.startFootballMatch("France", "Italy");
            scoreBoardService.updateScoreBoard(5, 0, footballMatch1.getId());

            List<FootballMatch> summary0 = scoreBoardService.summarizeTheScoreBoard();
            assertEquals("France", summary0.get(0).getHomeTeam());

            scoreBoardService.updateScoreBoard(10, 0, footballMatch0.getId());

            List<FootballMatch> summary2 = scoreBoardService.summarizeTheScoreBoard();
            assertEquals("Argentina", summary2.get(0).getHomeTeam());
            assertEquals(10, summary2.get(0).getHomeScore());
        }
    }

}
