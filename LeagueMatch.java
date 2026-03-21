import java.util.Map;

public class LeagueMatch extends Match{
    public LeagueMatch(String s, Map<String, Club> clubList) {
        super(s, clubList);
    }

    public double predict() {
        double homeElo = homeTeam.getElo() + 40;
        return 1 / (1 + Math.pow(10, awayTeam.getElo() - homeElo) / 400);
    }

    public int getWeight() {
        return 40;
    }
}
