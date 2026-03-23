import java.util.*;

public class Club implements Comparable<Club> {
    private final String name;
    private int gamesPlayed;
    private final Deque<Double> eloHistory;

    public Club(String name, double elo, int gamesPlayed) {
        this.eloHistory = new LinkedList<>();
        this.name = name;
        this.eloHistory.addLast(elo);
        this.gamesPlayed = gamesPlayed;
    }

    public void runMatch(double delta) {
        gamesPlayed++;
        updateElo(delta);
    }

    //public output file
    public String toString() {
        return String.format("%s  %.2f  %d", name, eloHistory.getLast(), gamesPlayed);
    }

    public int compareTo(Club other) {
        return Double.compare(other.getElo(), getElo());
    }

    public String printHistory() {
        String toReturn = name + ",";
        for (double d : eloHistory) {
            toReturn += String.format("%.2f,", d);
        }
        return toReturn.substring(0, toReturn.length() - 1);
    }

    public void clearHistory() {
        eloHistory.clear();
    }

    public void updateElo(double delta) {
        setElo(getElo() + delta);
    }

    public void setElo(double elo) {
        while (eloHistory.size() == 100) {
            eloHistory.removeFirst();
        }
        eloHistory.addLast(elo);
    }

    // helper functions for private fields
    public String getName() {
        return name;
    }

    public double getElo() {
        return eloHistory.getLast();
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }
}