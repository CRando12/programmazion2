package monkey;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ScoreManager
{
    private static final String SCORE_FILE = "./src/monkey/score.txt";

    public static void salvaPunteggio(int punteggio)
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE));
            writer.write(Integer.toString(punteggio));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int prelevaPunteggio()
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE));
            String punteggioString = reader.readLine();
            reader.close();
            if (punteggioString != null) {
                return Integer.parseInt(punteggioString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // Ritorna 0 se non è stato possibile prelevare il punteggio
    }
}
