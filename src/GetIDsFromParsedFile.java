import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetIDsFromParsedFile {
    private static final String SOURCE = "C:/Users/Shomakhov/Desktop/Юля.txt";

    public static void main(String[] args) {
        try (FileInputStream fstream = new FileInputStream(SOURCE);
             BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
            String pattern = "О-(.*)";
            String pattern2 = "Ф-(.*)";
            Pattern r = Pattern.compile(pattern);
            Pattern r2 = Pattern.compile(pattern2);
            String strLine;
            while ((strLine = br.readLine()) != null) {
                Matcher m = r.matcher(strLine);
                Matcher m2 = r2.matcher(strLine);
                if (m.find()||m2.find()) {
                    System.out.print(strLine+" ");
                    // set.add(strLine.substring(start, end));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
