import java.util.*;

public abstract class Match {
    protected final Club homeTeam;
    protected final int homeScore;
    protected final Club awayTeam;
    protected final int awayScore;
    protected final double result;

    public Match(String s, Map<String, Club> clubList) {
        String[] info = s.split("\\s+");
        this.homeTeam = clubList.get(info[0]);
        this.homeScore = Integer.parseInt(info[1]);
        this.awayTeam = clubList.get(info[4]);
        this.awayScore = Integer.parseInt(info[3]);
        if (homeScore > awayScore) {
            this.result = 1;
        } else if (homeScore == awayScore) {
            this.result = 0.5;
        } else {
            this.result = 0;
        }
    }
    // abstract method, returns the win expectation for the home team
    public abstract double predict();

    // uses the calculated expectation to update Clubs' elo
    public void play() {
        int weight = getWeight();
        if (homeScore != awayScore) {
            double marginAdjustment = 1 + Math.log(Math.abs(homeScore - awayScore));
            weight = (int) (weight * marginAdjustment);
        }

        double homeDelta = weight * (result - predict());
        homeTeam.runMatch(homeDelta);
        awayTeam.runMatch(-homeDelta);

        System.out.println(homeTeam.toString());
        System.out.println(awayTeam.toString());
    }

    public abstract int getWeight();
}