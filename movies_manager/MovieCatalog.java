import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class MovieCatalog {
    private HNode[] hashArray;
    private int initSize;

    public void allocate(int size) {
        if (isPrime(size)) {
            initSize = size;
        } else {
            initSize = getNextPrime(size);
        }
        System.out.println("[ALLOCTE] allocation size of " + size);
        hashArray = new HNode[initSize];
        for(int i = 0;i < hashArray.length;i++){
            hashArray[i] = new HNode();
            hashArray[i].key = 0;
            hashArray[i].status = 'E';
            hashArray[i].tree = new AVLTree<>();
        }
    }

    void put(Movie movie) {
        System.out.println("[INSERT] inserting movie " + movie.toString() + " ...");
        int key = generateKeyByTitle(movie.getTitle());
        int index = hash(key, 2);

        if (hashArray[index] == null) {
            hashArray[index] = new HNode(new AVLTree<>());
        }

        AVLTree<Movie> currTree = hashArray[index].tree;
        currTree.insert(movie);
        hashArray[index].status = 'F';
        hashArray[index].key = key;
        System.out.println("[DEBUG] Current tree height: " + currTree.getHeight());

        if (currTree.getHeight() > 3) {
            rehash();
        }
    }

    Movie get(String title) {
        System.out.println("[GET] searching movie by title " + title + " ...");
        int key = generateKeyByTitle(title);
        int index = hash(key, 2);

        AVLTree<Movie> tree = hashArray[index].tree;
        return tree.search(title);
    }

    void erase(String title) {
        System.out.println("[DELETE] deleting movie by title " + title + " ...");
        int key = generateKeyByTitle(title);
        int index = hash(key, 2);

        AVLTree<Movie> tree = hashArray[index].tree;
        tree.delete(title);
    }

    void printMap() {
        System.out.println("[PRINT] printing hashmap ...");
        for (int i = 0; i < hashArray.length; i++) {
            if (hashArray[i] == null) {
                System.out.println(i + ":\t# \t|\tnull \t|\tE");
            } else
                System.out.println(i + ":\t" + hashArray[i].toString());
        }
    }

    public void deallocate() {
        System.out.println("[DEALLOCATE] deallocating hashmap ...");
        for (int i = 0; i < hashArray.length; i++) {
            hashArray[i].tree = null;
            hashArray[i].key = 0;
            hashArray[i].status = 'E';
        }
    }

    public void loadMoviesFromFile(File f) throws IOException {
        System.out.println("[LOAD] loading movies to file ...");
        Scanner scanner = new Scanner(f);
        while (scanner.hasNextLine()) {
            String titleLine = scanner.nextLine();
            if (titleLine.isEmpty()) {
                continue;
            }
            String title = titleLine.split(":")[1].trim();
            String description = scanner.nextLine().split(":")[1].trim();
            int date = Integer.parseInt(scanner.nextLine().split(":")[1].trim());
            double rate = Double.parseDouble(scanner.nextLine().split(":")[1].trim());

            Movie movie = new Movie(title, description, date, rate);
            this.put(movie);

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }

        System.out.println("[LOAD] Movies loaded successfully.");
        scanner.close();

    }

    public void saveMoviesToFile(File f) throws IOException {
        System.out.println("[WRITE] writing all movies to file ...");
        FileWriter writer = new FileWriter(f);

        for (HNode node : hashArray) {
            if (node != null && node.tree != null) {
                List<Movie> movies = node.tree.getAllValues();
                for (Movie movie : movies) {
                    writer.write("Title: " + movie.getTitle() + "\n");
                    writer.write("Description: " + movie.getDescription() + "\n");
                    writer.write("Date: " + movie.getReleaseDate() + "\n");
                    writer.write("Rate: " + movie.getRate() + "\n\n");
                }
            }
        }
        System.out.println("[WRITE] all movies has been written successfully.");
        writer.close();
    }

    public List<Movie> getAllMovies() {
        System.out.println("[GET ALL] Retrieving all movies from the catalog ...");
        List<Movie> allMovies = new ArrayList<>();

        for (HNode node : hashArray) {
            if (node != null && node.tree != null) {
                allMovies.addAll(node.tree.getAllValues());
            }
        }

        System.out.println("[GET ALL] Total movies retrieved: " + allMovies.size());
        return allMovies;
    }

    public List<Movie> getAllMoviesSorted() {
        List<Movie> allMovies = getAllMovies();
        allMovies.sort(Comparator.comparing(Movie::getTitle));
        return allMovies;
    }

    public AVLTree<Movie> getTreeByIndex(int index){
        return hashArray[index].tree;
    }

    public int size(){
        return initSize;
    }

    // ------------- helper functions --------------

    private int hash(int key, int i) {
        return (h1(key, i) + i * h2(key, i)) % this.initSize;
    }

    private int h1(int key, int i) {
        return (key + i * i) % this.initSize;
    }

    private int h2(int key, int i) {
        return (key + i) % this.initSize;
    }

    private void rehash() {
        System.out.println("[REHASH] Starting rehashing process ...");

        HNode[] oldHashArray = hashArray;

        int newSize = getNextPrime(initSize * 2);
        allocate(newSize);

        for (HNode node : oldHashArray) {
            if (node != null && node.tree != null) {
                List<Movie> movies = node.tree.getAllValues();
                for (Movie movie : movies) {
                    put(movie);
                }
            }
        }
        System.out.println("[REHASH] Rehashing completed.");
    }

    private int generateKeyByTitle(String title) {
        int key = 0;
        for (int i = 0; i < title.length(); i++) {
            key += title.charAt(i) * (int) Math.pow(7, i);
        }
        System.out.println("[KEY] key generated: " + key);
        return Math.abs(key);
    }

    private boolean isPrime(int n) {
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private int getNextPrime(int n) {
        while (!isPrime(++n)) {

        }
        return n;
    }
}
