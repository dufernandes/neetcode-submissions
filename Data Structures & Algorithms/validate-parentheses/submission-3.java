class Solution {
    public boolean isValid(String s) {
        int n = s.length();

        if (n % 2 != 0) return false;

        int[] stack = new int[n];
        int top = -1;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '{': stack[++top] = '}'; break;
                case '[': stack[++top] = ']'; break;
                case '(': stack[++top] = ')'; break;
                default:
                    if (top == -1 || stack[top--] != c) return false;
            }
        }
        return top == -1;
    }
}
