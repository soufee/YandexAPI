import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContractsWithChangedSectionsOnFirstSA {
    private static final String SOURCE = "C:/Users/Shomakhov/Downloads/PegaRULES (2).log";

    public static void main(String[] args) {
        try (FileInputStream fstream = new FileInputStream(SOURCE);
             BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
            String pattern = "(.*)В контракте изменено количество секций(.*)";
         //   String pattern2 = "Ф-(.*)";
            Pattern r = Pattern.compile(pattern);
         //   Pattern r2 = Pattern.compile(pattern2);
            String strLine;
            while ((strLine = br.readLine()) != null) {
                Matcher m = r.matcher(strLine);
           //     Matcher m2 = r2.matcher(strLine);
                if (m.find()) {
                    String s = strLine.substring(strLine.indexOf("RnrcSystemUser -")+16);
                    s = s.replaceAll("В контракте изменено количество секций", "");
                    s = s.replaceAll("секциив", "секции в");
                    s = s.replaceAll("удаление и добавление секции","не проставленный ID в допсе или удаление и добавление секции ");
                    System.out.println(s);
                    // set.add(strLine.substring(start, end));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
