// the main file for TOOLS, The Open Open League Simulator
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String club = "clubs.txt";
        String history = "";
        String match = "matches.txt";
        String output = "export.txt";
        boolean parse = false;
        boolean aggregate = false;
        String[] toAgg = new String[3];

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-c" -> club = args[++i];
                case "-h" -> history = args[++i];
                case "-m" -> match = args[++i];
                case "-o" -> output = args[++i];
                case "-p" -> parse = true;
                case "-a" -> {
                    aggregate = true;
                    for (int j = 0; j < toAgg.length; j++) {
                        toAgg[j] = args[++i];
                    }
                }
                case "-e" -> {
                    club = "export.txt";
                    history = "history.csv";
                }
            }
        }

        Map<String, Club> clubMap = new HashMap<>();
        Set<Match> matches = new LinkedHashSet<>();

        if (aggregate) {
            if (parse) {
                for (int i = 0; i < toAgg.length; i++) {
                    ChatParser.parse(toAgg[i]);
                    toAgg[i] = "parsed_" + toAgg[i];
                }
            }
            MatchAggregator.aggregate(toAgg);
            parse = false;
            match = "aggregatedMatch.txt";
        }

        if (parse) {
            ChatParser.parse(match);
            match = "parsed_" + match;
        }

        // populates clubs[] with clubs and their info
        Scanner clubInput = new Scanner(new File(club));
        clubInput(clubInput, clubMap);

        // populates historical elo records (if any)
        if (! history.isEmpty()) {
            Scanner historyInput = new Scanner(new File(history));
            clubHistoryInput(historyInput, clubMap);
        } else {
            history = "history.csv";
        }

        // examines match type and populates matches[] accordingly
        Scanner matchInput = new Scanner(new File(match));
        matchInput(matchInput, matches, clubMap);

        // processes each match result
        for (Match m : matches) {
            m.play();
        }

        // exports all results and updated elo histories
        List<Club> clubList = new ArrayList<>(clubMap.values());
        Collections.sort(clubList);

        PrintWriter pw = new PrintWriter(output);
        clubOutput(pw, clubList);
        PrintWriter csv = new PrintWriter(history);
        historyOutput(csv, clubList);
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
            club.clearHistory();

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

    public static void clubOutput(PrintWriter pw, List<Club> clubList) {
        for (Club c : clubList) {
            pw.println(c);
        }
        pw.close();
    }

    public static void historyOutput(PrintWriter pw, List<Club> clubList) {
        for (Club c : clubList) {
            pw.println(c.printHistory());
        }
        pw.close();
    }
}