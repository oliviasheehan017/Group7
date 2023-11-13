import java.util.Objects;


public class Movie {
    private int id;
    private String title;
    private double rating;
    private String director;
    private String releaseDate;

    // Constructor with parameters
    public Movie(int id, String title, double rating, String director, String releaseDate) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.director = director;
        this.releaseDate = releaseDate;
    }

    // Getter and setter methods
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public String getDirector() {
        return director;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    // Equals and hashCode methods for proper comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id && Double.compare(movie.rating, rating) == 0 && title.equals(movie.title) && Objects.equals(director, movie.director) && Objects.equals(releaseDate, movie.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, rating, director, releaseDate);
    }
}
