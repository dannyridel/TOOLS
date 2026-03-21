import java.util.*;

public abstract class Match {
    protected final Club homeTeam;
    protected final int homeScore;
    protected final Club awayTeam;
    protected final int awayScore;
    protected final double result;

    public Match(String s, Map<String, Club> clubList) {
        Scanner info = new Scanner(s);
        String homeTeamName = "";
        String awayTeamName = "";

        while (! info.hasNextInt()) {
            homeTeamName += info.next() + " ";
        }
        homeTeamName = homeTeamName.substring(0, homeTeamName.length() - 1);
        homeScore = info.nextInt();
        info.next();
        awayScore = info.nextInt();

        while (info.hasNext()) {
            awayTeamName += info.next() + " ";
        }
        awayTeamName = awayTeamName.substring(0, awayTeamName.length() - 1);

        homeTeam = clubList.get(homeTeamName);
        awayTeam = clubList.get(awayTeamName);

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
        double weight = getWeight();
        if (homeScore != awayScore) {
            double marginAdjustment = 1 + 0.25 * Math.log(Math.abs(homeScore - awayScore));
            weight = weight * marginAdjustment;
        }

        double homeDelta = weight * (result - predict());
        homeTeam.runMatch(homeDelta);
        awayTeam.runMatch(-homeDelta);
    }

    public String toString() {
        return homeTeam.getName() + " " + homeScore + " : " + awayScore + " " + awayTeam.getName();
    }

    public abstract int getWeight();
}