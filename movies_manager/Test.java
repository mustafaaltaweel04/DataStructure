import java.io.IOException;
import java.io.File;

public class Test {
    static MovieCatalog movieCatalog;

    public static void main(String[] args) throws IOException {
        movieCatalog = new MovieCatalog();
        movieCatalog.allocate(17);
        Movie movie = new Movie("Sky", "I'm going back to 505 ...", 2024, 9);
        File f = new File("movies.txt");
        File newFile = new File("moviesUpdated.txt");
        movieCatalog.loadMoviesFromFile(f);
        movieCatalog.put(movie);
        System.out.println();
        movieCatalog.printMap();
        movieCatalog.saveMoviesToFile(newFile);
    }

    

}
