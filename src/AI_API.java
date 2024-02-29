import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AI_API {

    private static Gson gson = new Gson();


    private static HttpClient httpClient = HttpClient.newHttpClient();
    //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

    public static HttpResponse<String> sendREQ(String message)  {
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer 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")
                    .POST(HttpRequest.BodyPublishers.ofString("{\n" +
                            "    \"model\": \"gpt-3.5-turbo\",\n" +
                            "    \"messages\": [\n" +
                            "      {\n" +
                            "        \"role\": \"system\",\n" +
                            "        \"content\": \"You are a helpful assistant.\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"role\": \"user\",\n" +
                            "        \"content\": \""+ message +"\"\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  }"))
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public static String reqToClipboard(HttpResponse<String> request) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String retString;
        JsonObject jsonObject = JsonParser.parseString(request.body()).getAsJsonObject();


        if (jsonObject.get("text") != null) {
            retString =  jsonObject.get("text").getAsString();
        }   else if (jsonObject.get("error") != null) {
            retString = jsonObject.get("error").getAsJsonObject().get("message").getAsString();
        } else {
            retString = "An error occurred!";
        }

        clipboard.setContents((new StringSelection(retString)), null);
        return retString;
    }


    public static String manager(String message) {

        return reqToClipboard(sendREQ(message));
    }
}
