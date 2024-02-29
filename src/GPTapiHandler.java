import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

public class GPTapiHandler {
    private static JsonArray messagesHistory = new JsonArray();
    private static String apiKey = "";

    private static HttpClient httpClient = HttpClient.newHttpClient();


    public static HttpResponse<String> sendREQ(String message)  {
        try {

            JsonObject systemMessage = new JsonObject();
            systemMessage.addProperty("role", "system");
            systemMessage.addProperty("content", "You are a helpful assistant. " +
                    "You answers are pretty short but informative except if you told otherwise");

            JsonObject userMessage = new JsonObject();
            userMessage.addProperty("role", "user");
            userMessage.addProperty("content", message);

            if (messagesHistory.isEmpty()) {
                messagesHistory.add(systemMessage);
                messagesHistory.add(userMessage);

            } else {
                messagesHistory.add(userMessage);
            }


            JsonObject payload = new JsonObject();
            payload.addProperty("model", "gpt-3.5-turbo");
            payload.add("messages", messagesHistory);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(payload)))
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


        if (jsonObject.get("choices") != null) {
            retString =  jsonObject.get("choices").getAsJsonArray().get(0).getAsJsonObject().get("message").getAsJsonObject().get("content").getAsString();
        }   else if (jsonObject.get("error") != null) {
            retString = jsonObject.get("error").getAsJsonObject().get("message").getAsString();
        } else {
            retString = "An error occurred!";
        }

        if (!retString.equals("An error occurred!")) {
            JsonObject message = new JsonObject();
            message.addProperty("role", "assistant");
            message.addProperty("content", retString);
            messagesHistory.add(message);
        }

        CopyEventHandler.setLastContent(retString);
        clipboard.setContents((new StringSelection(retString)), null);

        return retString;
    }


    public static String manager(String message, String apiKey) {
        GPTapiHandler.apiKey = apiKey;
        return reqToClipboard(sendREQ(message));
    }
}
