class Solution {

    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/");
    
    public int evalRPN(String[] tokens) {
        Deque<Integer> numbers = new ArrayDeque<>();

        for (String token : tokens) {
            if (OPERATORS.contains(token)) {
                int right = numbers.pop();
                int left = numbers.pop();
                numbers.push(apply(token, left, right));
            } else {
                numbers.push(Integer.parseInt(token));
            }
        }
        return numbers.pop();
    }

    private int apply(String operator, int left, int right) {
        return switch (operator) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
