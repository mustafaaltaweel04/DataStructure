public class Stack {
    private int top;
    private Object[] stackArray;
    private int maxSize;

    public Stack(int maxSize) {
        this.maxSize = maxSize;
        stackArray = new Object[this.maxSize];
        top = -1;
    }

    public void push(Object data){
        if(top < maxSize-1){
            stackArray[++top] = data;
        }else{
            System.out.println("full stack");
        }
    }
    public Object pop(){
        if(top > -1)
            return stackArray[top--];
        return "";
        }
    public Object peek(){
        if (top == -1) {
            return null; 
        }
        return stackArray[top];
    }
    public boolean isEmpty(){
        return top == -1;
    }
    
}
