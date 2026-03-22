import java.util.*;
import java.io.*;

public class MatchAggregator {
    public static void aggregate(String[] inputFiles) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("aggregatedMatch.txt");
        Scanner[] inputs = new Scanner[inputFiles.length];
        for (int i = 0; i < inputFiles.length; i++) {
            inputs[i] = new Scanner(new File(inputFiles[i]));
        }

        // magic number here idk how to fix
        while (inputs[0].hasNextLine() || inputs[1].hasNextLine() || inputs[2].hasNextLine()) {
            if (inputs[0].hasNextLine()) {
                pw.println(inputs[0].nextLine());
            }
            if (inputs[1].hasNextLine()) {
                pw.println(inputs[1].nextLine());
            }
            if (inputs[2].hasNextLine()) {
                pw.println(inputs[2].nextLine());
            }
        }

        pw.close();
    }
}
