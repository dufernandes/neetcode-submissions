class MinStack {
    private Stack<Integer> stack = new Stack<>();
    PriorityQueue<Integer> min = new PriorityQueue<>();

    public MinStack() {
        stack = new Stack<>();
    }
    
    public void push(int val) {
        stack.push(val);
        min.add(val);
    }
    
    public void pop() {
        int top = stack.pop();
        min.remove(top);
    }
    
    public int top() {
        return stack.peek();
    }
    
    public int getMin() {
        return min.peek();
    }
}
