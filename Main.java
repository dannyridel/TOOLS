// the main file for TOOLS, The Open Open League Simulator
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner clubInput = new Scanner(new File("clubs.txt"));
        Scanner matchInput = new Scanner(new File("matches.txt"));

        Map<String, Club> clubList = new HashMap<>();
        Set<Match> matches = new HashSet<>();

        // populates clubs[] with clubs and their info
        clubInput(clubInput, clubList);

        // examines match type and populates matches[] accordingly
        matchInput(matchInput, matches, clubList);

        // now we have to process each match
        for (Match m : matches) {
            m.play();
        }

        PrintWriter pw = new PrintWriter("export.txt");
        output(pw, clubList);
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

    public static void output(PrintWriter pw, Map<String, Club> clubMap) {
        List<Club> clubList = new ArrayList<>(clubMap.values());
        Collections.sort(clubList);
        for (Club c : clubList) {
            pw.println(c);
        }
        pw.close();
    }
}