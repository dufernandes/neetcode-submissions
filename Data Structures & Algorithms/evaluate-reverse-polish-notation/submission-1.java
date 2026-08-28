class Solution {
    
    public int evalRPN(String[] tokens) {
        Stack<Integer> numbers = new Stack<Integer>();

        for (String token : tokens) {
            if (this.isNumber(token)) {
                numbers.push(this.getNumber(token));
            } else {
                int rightNumber = numbers.pop();
                int leftNumber = numbers.pop();
                int result = 0;
                switch (token) {
                    case "+":
                        result = leftNumber + rightNumber;
                        break;
                    case "-":
                        result = leftNumber - rightNumber;
                        break;
                    case "*":
                        result = leftNumber * rightNumber;
                        break;
                    case "/":
                        result = leftNumber / rightNumber;
                        break;
                }
                
                numbers.push(result);
            }
        }
        return numbers.pop();
    }

    private Integer getNumber(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    private boolean isNumber(String token) {
        try {
            Integer.parseInt(token);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
