@SuppressWarnings("unchecked")
public class Cursor<T extends Comparable<T>> {
    private static int SIZE;
    CNode<T>[] cursorArray;

    Cursor(int size){
        SIZE = size;
        cursorArray = new CNode[SIZE];
    }

    public void init(){
        for(int i = 0;i < SIZE; i++){
            cursorArray[i] = new CNode<T>(null, i + 1);
        }
        cursorArray[SIZE - 1] = new CNode<T>(null, 0);
    }

    private int malloc(){
        int p = cursorArray[0].next;
        cursorArray[0].next = cursorArray[p].next;
        return p;
    }

    private void free(int p){
        cursorArray[p] = new CNode<T>(null, cursorArray[0].next);
        cursorArray[0].next = p;
    }

    @SuppressWarnings("rawtypes")
    public int createList(){
        int L = malloc();
        if(L == 0)
            System.out.println("Out of Space");
        else
            cursorArray[L] = new CNode("-", 0);
            return L;
    }

    public void insertFirst(T data, int L){
        int p = malloc();
        if(p != 0 && cursorArray[L].data.equals("-")){
            cursorArray[p] = new CNode(data, cursorArray[L].next);
            cursorArray[L].next = p;
        } else{
            System.out.println("Out of Space");
        }
    }

    public void printList(){
        for(int i = 0;i < SIZE;i++){
            System.out.println(i + " | " + cursorArray[i] + " | " + cursorArray[i].next);
        }
    }

    @SuppressWarnings("rawtypes")
    public boolean isEmpty(int list){
        return cursorArray[list].next == 0;
    }
}
