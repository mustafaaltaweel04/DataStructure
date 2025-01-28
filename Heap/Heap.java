import java.util.Arrays;

public abstract class Heap {
    protected int maxSize;
    protected int currIndex;
    protected int[] heapArray;

    Heap(int size) {
        maxSize = size;
        heapArray = new int[maxSize];
    }

    protected abstract void heapifyDown();

    protected abstract void heapifyUp();

    protected int getLeftChild(int index) {
        return heapArray[getLeftChildIndex(index)];
    }

    protected int getRightChild(int index) {
        return heapArray[getRightChildIndex(index)];
    }

    protected int getParent(int index) {
        return heapArray[getParentIndex(index)];
    }

    protected int getLeftChildIndex(int index) {
        return 2 * index + 1;
    }

    protected int getRightChildIndex(int index) {
        return 2 * index + 2;
    }

    protected int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    protected boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < currIndex;
    }

    protected boolean hasRightChild(int index) {
        return getRightChildIndex(index) < currIndex;
    }

    protected boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    protected void swap(int indexA, int indexB) {
        int a = heapArray[indexA];
        heapArray[indexA] = heapArray[indexB];
        heapArray[indexB] = a;
    }

    protected void resizeif() {
        if (currIndex == maxSize) {
            heapArray = Arrays.copyOf(heapArray, maxSize * 2);
            maxSize *= 2;
        }
    }

    public void push(int val) {
        resizeif();
        heapArray[currIndex++] = val;
        heapifyUp();
    }

    public int poll() {
        int latest = heapArray[0];
        heapArray[0] = heapArray[currIndex - 1];
        currIndex--;
        heapifyDown();
        return latest;
    }

    public int peek() {
        return heapArray[0];
    }

    public void print() {
        for (int i = 0; i < currIndex; i++) {
            System.out.print(heapArray[i] + " ");
        }
        System.out.println();
    }

}
