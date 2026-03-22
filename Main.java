// the main file for TOOLS, The Open Open League Simulator
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String club = "clubs.txt";
        String history = "history.csv";
        String match = "matches.txt";
        String output = "export.txt";

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-c" -> club = args[++i];
                case "-h" -> history = args[++i];
                case "-m" -> match = args[++i];
                case "-o" -> output = args[++i];
            }
        }

        Scanner clubInput = new Scanner(new File(club));
        Scanner historyInput = new Scanner(new File(history));
        Scanner matchInput = new Scanner(new File(match));

        Map<String, Club> clubList = new HashMap<>();
        Set<Match> matches = new HashSet<>();

        // populates clubs[] with clubs and their info
        clubInput(clubInput, clubList);

        // populates historical elo records (if any)
        clubHistoryInput(historyInput, clubList);

        // examines match type and populates matches[] accordingly
        matchInput(matchInput, matches, clubList);

        // now we have to process each match
        for (Match m : matches) {
            m.play();
        }

        List<Club> clubs = new ArrayList<>(clubList.values());
        Collections.sort(clubs);
        PrintWriter pw = new PrintWriter(output);
        clubOutput(pw, clubs);
        PrintWriter csv = new PrintWriter(history);
        historyOutput(csv, clubs);
    }


    public static void clubInput(Scanner clubInput, Map<String, Club> clubList) {
        while (clubInput.hasNextLine()) {
            String clubInfo = clubInput.nextLine();
            Scanner info = new Scanner(clubInfo);
            String name = "";

            while (! info.hasNextDouble()) {
                name += info.next() + " ";
            }
            name = name.substring(0, name.length() - 1);
            double elo = info.nextDouble();
            int gamesPlayed = info.nextInt();

            clubList.put(name, new Club(name, elo, gamesPlayed));
        }
    }

    public static void clubHistoryInput(Scanner history, Map<String, Club> clubList) {
        while (history.hasNextLine()) {
            String[] clubHistory = history.nextLine().split(",");
            Club club = clubList.get(clubHistory[0]);
            for (int i = 1; i < clubHistory.length; i++) {
                club.setElo(Double.parseDouble(clubHistory[i]));
            }
        }
        history.close();
    }

    public static void matchInput(Scanner matchInput, Set<Match> matches, Map<String, Club> clubList) {
        String matchType = matchInput.nextLine();
        while (matchInput.hasNextLine()) {
            String nextMatch = matchInput.nextLine();
            if (nextMatch.equals("L") || nextMatch.equals("C") || nextMatch.equals("F")) {
                matchType = nextMatch;
            } else {
                switch (matchType) {
                    case "L":
                        matches.add(new LeagueMatch(nextMatch, clubList));
                        break;
                    case "C":
                        matches.add(new CupMatch(nextMatch, clubList));
                        break;
                    case "F":
                        matches.add(new FriendlyMatch(nextMatch, clubList));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected match type: " + matchType);
                }
            }
        }
    }

    public static void clubOutput(PrintWriter pw, ArrayList<Club> clubList) {
        for (Club c : clubList) {
            pw.println(c);
        }
        pw.close();
    }

    public static void historyOutput(PrintWriter pw, ArrayList<Club> clubList) {
        for (Club c : clubList) {
            pw.println(c.printHistory());
        }
        pw.close();
    }
}