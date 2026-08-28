class MinStack {
    private List<Integer> stack = new ArrayList<>();
    PriorityQueue<Integer> min = new PriorityQueue<>();

    public MinStack() {
        stack = new ArrayList<>();
    }
    
    public void push(int val) {
        stack.add(val);
        min.add(val);
    }
    
    public void pop() {
        int top = stack.removeLast();
        min.remove(top);
    }
    
    public int top() {
        return stack.getLast();
    }
    
    public int getMin() {
        return min.peek();
    }
}
