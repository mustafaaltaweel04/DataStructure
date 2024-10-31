public class Driver {
    public static void main(String[] args) {
        Cursor<String> cursor = new Cursor<>(10);
        cursor.init();
        cursor.createList();
        cursor.createList();
        cursor.insertFirst("Mustafa", 3);
        cursor.printList();
    }
}
