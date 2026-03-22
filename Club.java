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
        double elo = getElo();
        if (elo - other.getElo() > 0) {
            return -1;
        } else if (elo == other.getElo()) {
            return 0;
        } else {
            return 1;
        }
    }

    public String printHistory() {
        String toReturn = "";
        for (double d : eloHistory) {
            toReturn += d + ",";
        }
        return toReturn.substring(0, toReturn.length() - 1);
    }

    public void updateElo(double delta) {
        if (eloHistory.size() == 50) {
            eloHistory.removeFirst();
        }
        eloHistory.addLast(eloHistory.getLast() + delta);
    }

    public void setElo(double elo) {
        if (eloHistory.size() == 50) {
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