import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class MovieSearch {
    private static final String API_KEY = "8eef2d14581fa54099cf31c74a003212";
    private static final String API_ENDPOINT = "https://api.themoviedb.org/3/search/movie";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/movies";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static User loggedInUser;

    public static void createAndShowGUI(User user) {

        if(user != null){ 
       User loggedInUser = user;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        


        JFrame frame = new JFrame("Movie Search App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        JLabel titleLabel = new JLabel("Movie Search App");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel searchLabel = new JLabel("Enter Movie Title:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(52, 152, 219));
        searchButton.setForeground(Color.WHITE);

        // Add action listener to the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                if (!searchTerm.isEmpty()) {
                    // For testing, you can print the search term to the console
                    System.out.println("Search Term: " + searchTerm);
                    // Fetch and display top 3 movie options
                    displayTopMovies(searchTerm);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a movie title.");
                }
            }
        });

        // Set layout
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(236, 240, 241));

        // Create panel for the search components
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.setBackground(new Color(52, 73, 94));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add components to the frame
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(searchPanel, BorderLayout.CENTER);

        // Set frame properties
        frame.setSize(400, 150);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

}




    private static void displayTopMovies(String searchTerm) {
    try {
        // Encode the search term to handle spaces and special characters
        String encodedSearchTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);

        // Fetch data from the TMDb API with the provided search term
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "?api_key=" + API_KEY + "&query=" + encodedSearchTerm))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            showMovieOptions(responseBody);
        } else {
            System.out.println("Error fetching data from TMDb API. Status code: " + response.statusCode());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}




private static String getDirector(int movieId) {
    // Fetch the director information from the TMDb API
    try {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + API_KEY))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject creditsJson = new JSONObject(response.body());
            JSONArray crewArray = creditsJson.getJSONArray("crew");

            for (int i = 0; i < crewArray.length(); i++) {
                JSONObject crewMember = crewArray.getJSONObject(i);
                if (crewMember.getString("job").equals("Director")) {
                    return crewMember.getString("name");
                }
            }
        } else {
            System.out.println("Error fetching director information. Status code: " + response.statusCode());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return "Director not available";
}

private static String getReleaseDate(int movieId) {
    // Fetch the release date information from the TMDb API
    try {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject movieDetailsJson = new JSONObject(response.body());

            // Check if the release date is available in the main movie details
            if (movieDetailsJson.has("release_date")) {
                String dateString = movieDetailsJson.getString("release_date");

                // Parse the date string and format it
                LocalDate date = LocalDate.parse(dateString);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy"); // Customize the date format
                return date.format(formatter);
            }
        } else {
            System.out.println("Error fetching movie details. Status code: " + response.statusCode());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return "Release date not available";
}

public static void resetSearch() {
    
}





private static void showMovieOptions(String apiResponse) {
    JFrame movieOptionsFrame = new JFrame("Top Movie Options");
    movieOptionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    // Create components
    JLabel titleLabel = new JLabel("Top Movie Options");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setForeground(new Color(52, 152, 219));

    DefaultListModel<String> movieListModel = new DefaultListModel<>();
    JList<String> movieList = new JList<>(movieListModel);
    JPanel moviePanel = new JPanel();  // New panel for movie information and buttons
    JButton addButton = new JButton("Add to Favorites");
    addButton.setBackground(new Color(46, 204, 113));
    addButton.setForeground(Color.WHITE);

    // Parse the API response and add top 3 movie options to the list
    JSONArray moviesArray = new JSONObject(apiResponse).getJSONArray("results");
    for (int i = 0; i < Math.min(moviesArray.length(), 3); i++) {
        JSONObject movieJson = moviesArray.getJSONObject(i);
        String movieTitle = movieJson.getString("title");

        // Fetch and display additional information (director and release date)
        String director = getDirector(movieJson.getInt("id"));
        String releaseDate = getReleaseDate(movieJson.getInt("id"));

        // Display movie information in the list
        String movieInfo = movieTitle + " (Director: " + director + ", Release Date: " + releaseDate + ")";
        movieListModel.addElement(movieInfo);

        // Add buttons for adding to favorites and viewing favorites
        JButton addToFavoritesButton = new JButton("Add to Favorites");
        JButton viewFavoritesButton = new JButton("View Favorites");

        // Add action listeners to the buttons
        addToFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToFavorites(movieJson.getInt("id"));
            }
        });

        viewFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewFavorites();
            }
        });

        // Add buttons to the GUI
        moviePanel.add(new JLabel(movieInfo));
        moviePanel.add(addToFavoritesButton);
        moviePanel.add(viewFavoritesButton);
    }

    // Add action listener to the add button
    addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // For testing, you can print the selected movie to the console
            String selectedMovie = movieList.getSelectedValue();
            System.out.println("Selected Movie: " + selectedMovie);
            // In a real application, you would add the selected movie to the database
            // Call the method to insert the selected movie into the database here
        }
    });

    // Set layout for the movie panel
    moviePanel.setLayout(new BoxLayout(moviePanel, BoxLayout.Y_AXIS));

    // Set layout for the frame
    movieOptionsFrame.setLayout(new BorderLayout());
    movieOptionsFrame.getContentPane().setBackground(new Color(236, 240, 241));

    // Add components to the frame
    movieOptionsFrame.add(titleLabel, BorderLayout.NORTH);
    movieOptionsFrame.add(new JScrollPane(movieList), BorderLayout.CENTER);
    movieOptionsFrame.add(moviePanel, BorderLayout.WEST);  // Add movie panel to the left
    movieOptionsFrame.add(addButton, BorderLayout.SOUTH);

    // Set frame properties
    movieOptionsFrame.setSize(600, 300);  // Adjusted size
    movieOptionsFrame.setLocationRelativeTo(null); // Center the frame on the screen
    movieOptionsFrame.setVisible(true);
}

private static void displayUserList(ResultSet resultSet) throws SQLException {
    StringBuilder listMessage = new StringBuilder("Your Favorite Movies:\n");

    while (resultSet.next()) {
        String title = resultSet.getString("Title");
        String director = resultSet.getString("Director");
        String releaseDate = resultSet.getString("ReleaseDate");

        listMessage.append("Title: ").append(title)
                .append(", Director: ").append(director)
                .append(", Release Date: ").append(releaseDate)
                .append("\n");
    }

    JOptionPane.showMessageDialog(null, listMessage.toString(), "Your Favorite Movies", JOptionPane.INFORMATION_MESSAGE);
}








private static void addToFavorites(int movieId) {
    
    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
        String query = "INSERT INTO UserMovies (UserID, MovieID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, loggedInUser.getUserId());
            preparedStatement.setInt(2, movieId);
            preparedStatement.executeUpdate();
            System.out.println("Movie added to favorites successfully.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}








private static void viewFavorites() {
    // Implement logic to display the user's favorite movies
    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
        String query = "SELECT m.* FROM Movies m JOIN UserMovies um ON m.MovieID = um.MovieID WHERE um.UserID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, loggedInUser.getUserId());
            ResultSet resultSet = preparedStatement.executeQuery();
            displayUserList(resultSet);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}



}

