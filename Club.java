// any imports

public class Club {
    private final String name;
    private double elo;
    private int gamesPlayed;

    public Club(String[] info) {
        this.name = info[0];
        this.elo = Double.parseDouble(info[1]);
        this.gamesPlayed = Integer.parseInt(info[2]);
    }

    public void runMatch(double delta) {
        gamesPlayed++;
        updateElo(delta);
    }


    public String toString() {
        return String.format("%s  %.2f  %d", name, elo, gamesPlayed);
    }

    public void updateElo(double delta) {
        elo += delta;
    }

    // helper functions for private fields
    public String getName() {
        return name;
    }

    public double getElo() {
        return elo;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }
}
