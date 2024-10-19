import java.io.File;
import java.util.*;

public class Driver {
    public static void main(String[] args) throws Exception{
        // LinkedList list = new LinkedList();
        // list.insertFirst("Mustafa");
        // list.insertFirst("Name is");
        // list.insertFirst("My");

        // list.printList();

        // LinkedList list2 = new LinkedList();
        // list2.insertFirst("Ahmad");
        // list2.insertFirst("I Am");
        // list2.insertFirst("Hello ");

        // list2.printList();

        // DLinkedList doubleList = new DLinkedList();
        // DNode first = new DNode(list2);
        // first.numOfDigits = 5;
        // DNode sec = new DNode(list);
        // sec.numOfDigits = 4;
        // doubleList.insertSorted(first);
        // doubleList.insertSorted(sec);
        
        // doubleList.printList();

        // System.out.println(doubleList.hasNumberOfDigits(3));

        File f = new File("words.txt");
        DLinkedList list2 = wordSplitter(f);
        list2.printList();
    }

    public static DLinkedList wordSplitter(File f) throws Exception{
        DLinkedList list = new DLinkedList();
        Scanner fileScanner = new Scanner(f);
        while (fileScanner.hasNext()) {
            String word = fileScanner.next();
            if(!list.hasNumberOfDigits(word.length())){
                DNode node = new DNode(new LinkedList());
                node.numOfDigits = word.length();
                node.data.insertFirst(word);
                list.insertSorted(node);
            }else{
                list.findIndex(word.length()).data.insertFirst(word);
            }
        }
        fileScanner.close();
        return list;
    }
}
