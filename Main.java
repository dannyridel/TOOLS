// the main file for TOOLS, The Open Open League Simulator
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("clubs.txt"));
        Scanner newMatches = new Scanner(new File("matches.txt"));

        Map<String, Club> clubList = new HashMap<>();
        Set<Match> matches = new HashSet<>();

        // populates clubs[] with clubs and their info
        while (input.hasNextLine()) {
            String clubInfo = input.nextLine();
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

        // examines match type and populates matches[] accordingly
        String matchType = newMatches.nextLine();
        while (newMatches.hasNextLine()) {
            String matchInfo = newMatches.nextLine();
            switch (matchType) {
                case "L":
                    matches.add(new LeagueMatch(matchInfo, clubList));
                    break;
                case "C":
                    matches.add(new CupMatch(matchInfo, clubList));
                    break;
                case "F":
                    matches.add(new FriendlyMatch(matchInfo, clubList));
                    break;
                default:
                    throw new IllegalStateException("Unexpected match type: " + matchType);
            }
        }

        // now we have to process each match
        for (Match m : matches) {
            m.play();
        }

        PrintWriter pw = new PrintWriter("export.txt");
        output(pw, clubList);
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