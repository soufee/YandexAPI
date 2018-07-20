import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckAllfiles {
    private static final String SOURCE = "C:/tmp/logsforparse";

    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<>();
        String files[] = new File(SOURCE).list();
        for (int i = 0; i < files.length; i++) {
            list.add(SOURCE + "/" + files[i]);
        }
        Thread[] threads = new Thread[list.size()];
        for (int i = 0; i < list.size(); i++) {
            threads[i] = new MyRunnable(list.get(i));
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++)
            threads[i].join();
    }

    static class MyRunnable extends Thread {
        String fileName;

        public MyRunnable(String s) {
            this.fileName = s;
        }

        @Override
        public void run() {
            findStackOverFlow(fileName);
        }

        private void findStackOverFlow(String fileName) {
            String PATTERN = "StackOverFlow";
            System.out.println("Начался процесс по файлу " + fileName);
            try (FileInputStream fstream = new FileInputStream(fileName);
                 BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
                String pattern = "(.*)" + PATTERN + "(.*)";

                Pattern r = Pattern.compile(pattern.toLowerCase());

                String strLine;
                while ((strLine = br.readLine()) != null) {
                    Matcher m = r.matcher(strLine.toLowerCase());
                    if (m.find()) {
                        System.out.println(strLine);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
