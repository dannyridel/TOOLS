import java.util.*;
import java.io.*;

public class ChatParser {
    public static void parse(String s) throws FileNotFoundException {
        Scanner input = new Scanner(new File(s));
        PrintWriter pw = new PrintWriter("parsed_"+s);

        boolean active = false;

        while (input.hasNextLine()) {
            String line = input.nextLine();

            if (line.isEmpty()) {
                continue;
            }
            if (line.contains("All matches have ended across the league.")) {
                active = true;
                line = "L";
            } else if (line.contains(" PM") || line.contains(" AM") || line.contains("=") || line.contains("{")) {
                active = false;
            }

            if (active) {
                line = line.replace("Luchadores", "Los Cabos United");
                pw.println(line);
            }
        }

        input.close();
        pw.close();
    }
}
