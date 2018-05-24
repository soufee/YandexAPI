import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {

    }


//        FileInputStream fstream = new FileInputStream("C:/tmp/FullStatistic.txt");
//        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//        String strLine;
//            while ((strLine = br.readLine()) != null) {
//               int size = strLine.length();
//                if (strLine.contains(";")) {
//                    int first = strLine.indexOf(";")+2;
//                    String s = strLine.substring(first, size);
//                    System.out.println(s);
//                }
//
//
//            }
//            }

//        try {
//            FileInputStream fstream = new FileInputStream("C:/tmp/2.txt");
//            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//            String pattern = "(.*): Не найдено соответствие секции по хэшу";
//            Pattern r = Pattern.compile(pattern);
//            Set<String> set = new HashSet<>();
//            String strLine;
//            while ((strLine = br.readLine()) != null) {
//                Matcher m = r.matcher(strLine);
//                if (m.find()) {
//                  //  System.out.println(strLine);
//                    int start = strLine.indexOf("RnrcSystemUser") + 17;
//                    int end = strLine.indexOf(": Не найдено");
//                    set.add(strLine.substring(start, end));
//                }
//            }
//            FileOutputStream stream = new FileOutputStream("C:/tmp/HashNotMatchSA.txt");
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
//            for (String s : set) {
//                System.out.println(s);
//                writer.write(s + "\n");
//            }
//            writer.flush();
//        } catch (IOException e) {
//            System.out.println("Ошибка");
//        }

//        FileInputStream fstream1 = new FileInputStream("C:/tmp/HashNotMatch.txt");
//        BufferedReader br1 = new BufferedReader(new InputStreamReader(fstream1));
//        FileInputStream fstream2 = new FileInputStream("C:/tmp/HashNotMatchSA.txt");
//        BufferedReader br2 = new BufferedReader(new InputStreamReader(fstream2));
//        String strLine;
//        List<String> contracts = new ArrayList<>();
//        while ((strLine = br1.readLine()) != null) {
//            contracts.add(strLine);
//        }
//
//        strLine = "";
//        List<String> sa = new ArrayList<>();
//        while ((strLine = br2.readLine()) != null) {
//            sa.add(strLine);
//        }
//
//        contracts.removeAll(sa);
//
//      contracts.forEach(System.out::println);


}
