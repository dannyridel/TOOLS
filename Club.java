// any imports

public class Club {
    private final String name;
    private double elo;
    private int gamesPlayed;

    public Club(String s) {
        String[] info = s.split(" ");
        name = info[0];
        elo = Double.parseDouble(info[1]);
        gamesPlayed = Integer.parseInt(info[2]);
    }

    public void playMatch(double delta) {
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
