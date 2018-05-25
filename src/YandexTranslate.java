import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class YandexTranslate {
    private static int i = 0;
    public static void main(String[] args) throws IOException {
       String s = new Scanner(System.in).nextLine();
        System.out.println(translate("ru", s));
    }

    private static String translate(String lang, String input) throws IOException {
        String urlStr = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" +
                "trnsl.1.1.20180524T143109Z.9df1b0c419b06f0e.862f16e7aee215b642d8b043d8964a0004679e98";
        URL urlObj = new URL(urlStr);
        HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes("text=" + URLEncoder.encode(input, "UTF-8") + "&lang=" + lang);

        InputStream response = connection.getInputStream();
        String json = new Scanner(response).nextLine();
        int start = json.indexOf("[");
        int end = json.indexOf("]");
        String translated = json.substring(start + 2, end - 1);
        i++;
        if (translated.equals(input) && i < 2) {
            // if return equal of entered text - we need change direction of translation
            return translate("en", input);
        } else return translated;
    }
}
