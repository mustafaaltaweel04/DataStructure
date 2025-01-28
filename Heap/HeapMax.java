class HeapMax extends Heap {

    HeapMax(int size) {
        super(size);
    }

    @Override
    protected void heapifyUp() {
        int index = currIndex - 1;
        while (hasParent(index) && getParent(index) < heapArray[index]) {
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    @Override
    protected void heapifyDown() {
        int index = 0;
        while (hasLeftChild(index)) {
            int largerChild = getLeftChildIndex(index);
            if (hasRightChild(index) && getLeftChild(index) < getRightChild(index)) {
                largerChild = getRightChildIndex(index);
            }

            if (heapArray[index] > heapArray[largerChild]) {
                break;
            } else {
                swap(largerChild, index);
            }
            index = largerChild;
        }
    }

}
