public class District {
    private String name;
    BST location_pointer;

    District(String name) {
        this.name = name;
        location_pointer = new BST();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // this is a method to check if a location exists in district
    public boolean contains_location(String locationName) {
        return location_pointer.checkContains(locationName);
    }
     public int number_of_martyrs(){
        int size = 0;
        for(int i = 0;i < location_pointer.size; i++){
            size += ((Location)(location_pointer.get(i))).number_of_martyrs();
        }
        return size;
     }
    @Override
    public String toString() {
        return name;
    }

}
