import java.util.*;

public abstract class Match {
    private final Club homeTeam;
    private final int homeScore;
    private final Club awayTeam;
    private final int awayScore;

    public Match(String s, Map<String, Club> clubList) {
        String[] info = s.split(" ");
        this.homeTeam = clubList.get(info[0]);
        this.homeScore = Integer.parseInt(info[1]);
        this.awayTeam = clubList.get(info[4]);
        this.awayScore = Integer.parseInt(info[3]);
    }
    // abstract method for each match to calculate expectation of the home team
    public abstract double predict();

    // uses the calculated expectation to update Clubs' elo
    public void play() {
        double homeDelta = predict();
        homeTeam.runMatch(homeDelta);
        awayTeam.runMatch(1 - homeDelta);
    }
}