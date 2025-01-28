class HeapMin extends Heap {

    HeapMin(int size) {
        super(size);
    }

    @Override
    protected void heapifyUp() {
        int index = currIndex - 1;
        while (hasParent(index) && getParent(index) > heapArray[index]) {
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    @Override
    protected void heapifyDown() {
        int index = 0;
        while (hasLeftChild(index)) {
            int smallestChild = getLeftChildIndex(index);
            if (hasRightChild(index) && getLeftChild(index) > getRightChild(index)) {
                smallestChild = getRightChildIndex(index);
            }

            if (heapArray[index] < heapArray[smallestChild]) {
                break;
            } else {
                swap(smallestChild, index);
            }
            index = smallestChild;
        }
    }

}