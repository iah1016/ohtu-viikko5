package ohtu;

import java.util.HashMap;
import java.util.Map;

public class TennisGame {

    private int player1Score;
    private int player2Score;
    private final String player1Name;
    private final String player2Name;
    private final int normalWinningScore;
    private final int normalIncrementOfPoints;
    private Map<Integer, String> pointCalls;

    public TennisGame(String player1Name, String player2Name) {
        this.normalIncrementOfPoints = 1;
        this.normalWinningScore = 4;
        this.player1Score = 0;
        this.player2Score = 0;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        createPointCallsHashMap();
    }

    private void createPointCallsHashMap() {
        pointCalls = new HashMap<>();
        pointCalls.put(0, TennisTerminology.LOVE.term());
        pointCalls.put(1, TennisTerminology.FIFTEEN.term());
        pointCalls.put(2, TennisTerminology.THIRTY.term());
        pointCalls.put(3, TennisTerminology.FORTY.term());
    }

    public void wonPoint(String playerName) {
        if (playerName.equals(player1Name)) {
            player1Score += normalIncrementOfPoints;
        } else if (playerName.equals(player2Name)) {
            player2Score += normalIncrementOfPoints;
        }
    }

    public String getScore() {
        if (player1Score == player2Score) {
            return getScoreWhenGameIsEven();
        }
        if (player1Score >= normalWinningScore
                || player2Score >= normalWinningScore) {
            return getScoreWhenEitherPlayerHasAtLeastNormalWinningScore();
        }
        return getScoreWhenSpecialConditionsAreNotMet();
    }

    private String getScoreWhenGameIsEven() {
        String dashAll = "-" + TennisTerminology.ALL.term();

        switch (player1Score) {
            case 0:
                return pointCalls.get(0) + dashAll;
            case 1:
                return pointCalls.get(1) + dashAll;
            case 2:
                return pointCalls.get(2) + dashAll;
            case 3:
                return pointCalls.get(3) + dashAll;
            default:
                return TennisTerminology.DEUCE.term();
        }
    }

    private String getScoreWhenEitherPlayerHasAtLeastNormalWinningScore() {
        int pointDifference = player1Score - player2Score;
        if (pointDifference == normalIncrementOfPoints) {
            return TennisTerminology.ADVANTAGE.term() + " " + player1Name;
        }
        if (pointDifference == -normalIncrementOfPoints) {
            return TennisTerminology.ADVANTAGE.term() + " " + player2Name;
        }
        if (pointDifference >= 2 * normalIncrementOfPoints) {
            return TennisTerminology.WIN.term() + " " + player1Name;
        }
        return TennisTerminology.WIN.term() + " " + player2Name;
    }

    private String getScoreWhenSpecialConditionsAreNotMet() {
        return pointCalls.get(player1Score)
                + "-" + pointCalls.get(player2Score);
    }
}
