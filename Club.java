

public class Club {
    private double elo;
    private int gamesPlayed;

    public void playMatch(double delta) {
        gamesPlayed++;
        updateElo(delta);
    }

    public String toString() {
        // TODO
        return super.toString();
    }

    public void updateElo(double delta) {
        elo += delta;
    }

    public double getElo() {
        return elo;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }
}
