import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String SOURCE = "C:/Users/Shomakhov/Downloads/PegaRULES (1).log";

    public static void main(String[] args) throws IOException {
        Set<String> contracts = getListOfWrongHash(SOURCE);
        Map<String, String> detailedContracts = getDifferenceOfHashWrongContracts(SOURCE, contracts);
        analize(detailedContracts);
    }

    private static void analize(Map<String, String> contracts) {
        List <String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry: contracts.entrySet()) {
            System.out.println(entry.getKey());
            String s = entry.getValue();
            String[] data = s.split(";");
           if (data[0].equals("")){
               System.out.println("В договоре нет допсов");
           } else {
               System.out.println("Допсы по договору: "+data[0]);
              // System.out.println(data[0]);
           }
         String[]la = data[1].split(" ");
         String[]bi = data[2].split(" ");
            System.out.println("Количество убытков: "+la[2]);
            System.out.println("Количество бордеро и счетов: "+bi[2]);
            if (data[3].trim().equals(data[4].trim())){
                int sectionsNum = Integer.parseInt(data[3].trim());
                System.out.println("Количество секций в диасофте и пеге совпадает: "+sectionsNum);
                for (int i = 5; i < 5+sectionsNum; i++) {
                    System.out.println("Секция "+(i-4)+" в Пега: "+data[i].trim());
                }
                for (int i = 5+sectionsNum; i < 5+sectionsNum*2; i++) {
                    System.out.println("Секция "+(i-4-sectionsNum)+" в Диасофт: "+data[i].trim());
                }
            } else {
                System.out.println("Количество секций не совпадает: В пеге "+data[3].trim()+", в диасофт "+data[4].trim());
            }

        }
    }

    private static Map<String, String> getDifferenceOfHashWrongContracts(String source, Set<String> contractIDs) {
        Set<String> set = new HashSet<>();
        Map<String, String> results = new HashMap<>();

        try (FileInputStream fstream = new FileInputStream(source);
             BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
            String pattern = "(.*) RnrcSystemUser - (.*)";
            Pattern r = Pattern.compile(pattern);
            String strLine;
            while ((strLine = br.readLine()) != null) {
                Matcher m = r.matcher(strLine);
                if (m.find()) {
                    int start = strLine.indexOf("RnrcSystemUser") + 17;
                    int end = strLine.length();
                    //  System.out.println(strLine.substring(start, end));
                    set.add(strLine.substring(start, end));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String contract : contractIDs) {
            String pattern1 = contract + "(.*)";
            String pattern2 = "(.*) Не найдено соответствие секции по хэшу";
            String pattern3 = "(.*)совпадение(.*)";
            String pattern4 = "(.*)платежей(.*)";
            Pattern r = Pattern.compile(pattern1);
            Pattern r2 = Pattern.compile(pattern2);
            Pattern r3 = Pattern.compile(pattern3);
            Pattern r4 = Pattern.compile(pattern4);
            for (String s : set) {
                Matcher m = r.matcher(s);
                Matcher m2 = r2.matcher(s);
                Matcher m3 = r3.matcher(s);
                Matcher m4 = r4.matcher(s);
                if (m.find()) {
                    if (!m2.find() && !m3.find() && !m4.find()) {
                        String[] q = s.split(":");
                        results.put(q[0].trim(), q[1].trim());
                    }
                }
            }
        }
        try (FileOutputStream stream = new FileOutputStream("C:/tmp/differences.txt");
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream))) {
            for (String s : set) {
                writer.write(s + "\n");
            }
            writer.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return results;
    }

    private static Set<String> getListOfWrongHash(String fileName) {
        Set<String> set = new HashSet<>();
        try (FileInputStream fstream = new FileInputStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
            String pattern = "(.*): Не найдено соответствие секции по хэшу";
            Pattern r = Pattern.compile(pattern);
            String strLine;
            while ((strLine = br.readLine()) != null) {
                Matcher m = r.matcher(strLine);
                if (m.find()) {
                    int start = strLine.indexOf("RnrcSystemUser") + 17;
                    int end = strLine.indexOf(": Не найдено");
                    //    System.out.println(strLine.substring(start, end) + ", ");
                    set.add(strLine.substring(start, end));
                }
            }
            FileOutputStream stream = new FileOutputStream("C:/tmp/HashNotMatchSA.txt");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
            for (String s : set) {
                //    System.out.println(s);
                writer.write(s + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Ошибка");
        }
        return set;
    }

}