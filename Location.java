public class Location {
    private String name;
    BST dates_pointer;

    Location(String name) {
        this.name = name;
        dates_pointer = new BST();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean contains_date(String date) {
        return dates_pointer.checkContains(date);
    }

    public int number_of_martyrs(){
        int size = 0;
        for(int i = 0;i < dates_pointer.size; i++){
            size += ((Dates)dates_pointer.get(i)).number_of_martyrs();
        }
        return size;
    }
    @Override
    public String toString() {
        return name;
    }
}
