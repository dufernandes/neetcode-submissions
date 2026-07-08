class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();

        if (s.length() % 2 != 0) return false;

        for (char c : s.toCharArray()) {
            switch (c) {
                case '{': stack.push('{');
                break;
                case '[': stack.push('[');
                break;
                case '(': stack.push('(');
                break;
                case '}': if (stack.isEmpty() || stack.pop() != '{') return false;
                break;
                case ']': if (stack.isEmpty() || stack.pop() != '[') return false;
                break;
                case ')': if (stack.isEmpty() || stack.pop() != '(') return false;
                break;
                default: throw new RuntimeException("Unexpected char in input: " + c);
            }
        }
        return stack.isEmpty();
    }
}
