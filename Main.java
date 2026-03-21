// the main file for TOOLS, The Open Open League Simulator
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("clubs.txt"));
        Scanner newMatches = new Scanner(new File("matches.txt"));

        Map<String, Club> clubList = new HashMap<>();
        ArrayList<Match> matches = new ArrayList<>();

        // populates clubs[] with clubs and their info
        while (input.hasNextLine()) {
            String clubInfo = input.nextLine();
            String[] info = clubInfo.split("\\s+");
            clubList.put(info[0], new Club(info));
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
                    // matches.add(new FriendlyMatch(matchInfo, clubList));
                    break;
                default:
                    throw new IllegalStateException("Unexpected match type: " + matchType);
            }
        }

        // now we have to process each match
        for (Match m : matches) {
            m.play();
        }
    }
}