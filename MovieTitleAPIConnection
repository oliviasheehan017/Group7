import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TitleAPI {
    public static void main(String[] args) {
        try {
            // Replace with your API endpoint
            String apiUrl = "https://streaming-availability.p.rapidapi.com/search/title";

            // Set required parameters
            String title = "Inception";
            String country = "US";

            // Encode parameters for URL
            title = URLEncoder.encode(title, "UTF-8");
            country = URLEncoder.encode(country, "UTF-8");

            // Append parameters to the URL
            apiUrl += "?title=" + title + "&country=" + country;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request headers (e.g., API key)
            connection.setRequestProperty("X-RapidAPI-Key", "0d8e055d03mshd8fd2ff9fe4b668p13092ejsn887a2b7dcdcc");

            // Set request method
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Now 'response' contains the API response in JSON format
                System.out.println(response.toString());
            } else {
                System.out.println("Failed to connect to the API. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
