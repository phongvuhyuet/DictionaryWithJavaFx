package sample;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Translate {
    private static final String API_KEY = "8ade3b5975msh946e51ec3b40876p154610jsn70ccbceb1447";

    public static String translate(String text, String source, String target) {
        HttpResponse<String> response = null;
        try {
            response = Unirest.post("https://rapidapi.p.rapidapi.com/language/translate/v2")
                    .header("content-type", "application/x-www-form-urlencoded")
                    .header("accept-encoding", "application/gzip")
                    .header("x-rapidapi-host", "google-translate1.p.rapidapi.com")
                    .header("x-rapidapi-key", API_KEY)
                    .body("q=" + text.trim() + "&source=" + source + "&target=" + target)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        //Data format = prefix + result + postfix
        String data = response.getBody();
        String prefix = "{\"data\":{\"translations\":[{\"translatedText\":\"";
        String postfix = "\"}]}}";
        int translateLength = data.length() - prefix.length() - postfix.length();
        int begin = prefix.length();
        int end = begin + translateLength;
        String result = data.substring(begin, end);
        return result;
    }
}
