public class Movie implements Comparable<Movie> {
    private String title;
    private String description;
    private int releaseDate;
    private double rate;
    
    public Movie(String title, String description, int releaseDate, double rate) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "title=" + title + ", releaseDate=" + releaseDate + ", rate="
                + rate ;
    }

    @Override
    public int compareTo(Movie o) {
        return this.getTitle().compareTo(o.title);
    }

    
    
}