import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingN {
    private static final String SOURCE = "C:/Users/Shomakhov/Downloads/PegaRULES (5).log";

    public static void main(String[] args) {
        try (FileInputStream fstream = new FileInputStream(SOURCE);
             BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
            String pattern = "(.*)Parsing2(.*)";

            Pattern r = Pattern.compile(pattern);

            String strLine;
            while ((strLine = br.readLine()) != null) {
                Matcher m = r.matcher(strLine);

                if (m.find()) {
                    String s = strLine.substring(strLine.indexOf("RnrcSystemUser -")+16);

                    System.out.println(s);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
