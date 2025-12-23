import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class CLI {

    public static final String ACTION = "action";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CLI <username>");
            return;
        }

        String username = args[0];
        String apiUrl = "https://api.github.com/users/" + username + "/events";


        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).header("Accept", "application/vnd.github+json").build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JsonReader jsonReader = Json.createReader(new StringReader(responseBody));
                JsonArray events = jsonReader.readArray();
                jsonReader.close();

                System.out.println("Recent activity for " + username + ":\n");

                for (int i = 0; i < Math.min(10, events.size()); i++) {
                    JsonObject event = events.getJsonObject(i);
                    String type = event.getString("type");
                    JsonObject repo = event.getJsonObject("repo");
                    String repoName = Objects.nonNull(repo) ? repo.getString("name") : "Unknown";

                    String activity = formatActivity(type, repoName, event);
                    if (Objects.nonNull(activity)) {
                        System.out.println("- " + activity);
                    }
                }
            } else if (response.statusCode() == 404) {
                System.out.println("User not found: " + username);
            } else {
                System.out.println("Error getting user activity: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String formatActivity(String type, String repoName, JsonObject event) {
        try {
            JsonObject payload = event.getJsonObject("payload");
            if (Objects.isNull(payload)) {
                return String.format("Unknown event in %s", repoName);
            }
            switch (type) {
                case "PushEvent":
                    if (payload.containsKey("commit")) {
                        int commitCount = payload.getJsonArray("commits").size();
                        return String.format("Pushed %d commit(s) to %s", commitCount, repoName);
                    }
                    return String.format("Pushed %s", repoName);
                case "IssueEvent":
                    String action = payload.containsKey(ACTION) ? payload.getString(ACTION) : "updated";
                    return String.format("%s issue in %s: ", "opened".equals(action) ? "Opened" : "Updated", repoName);
                case "PullRequestEvent":
                    String prAction = payload.containsKey(ACTION) ? payload.getString(ACTION) : "updated";
                    return String.format("%s pull request in %s: ", "opened".equals(prAction) ? "Opened" : "Updated", repoName);
                case "WatchEvent":
                    return String.format("Starred %s", repoName);
                case "ForkEvent":
                    return String.format("Forked %s", repoName);
                case "CreateEvent":
                    return String.format("Created %s in %s", payload.containsKey("ref_type") ? payload.getString("ref_type") : "something", repoName);
                case "DeleteEvent":
                    return String.format("Removed %s in %s", payload.containsKey("ref_type") ? payload.getString("ref_type") : "something", repoName);
                default:
                    return String.format("Activity type %s in %s", type, repoName);
            }
        } catch (Exception e) {
            System.err.println("Error formatting activity: " + e.getMessage());
            return String.format("Activity in %s (error processing)", repoName);
        }
    }
}
