import java.util.Map;

public class CupMatch extends Match{
    public CupMatch(String s, Map<String, Club> clubList) {
        super(s, clubList);
    }

    public double predict() {
        return 1 / (1 + Math.pow(10, awayTeam.getElo() - homeTeam.getElo()) / 400);
    }

    public int getWeight() {
        return 25;
    }
}
