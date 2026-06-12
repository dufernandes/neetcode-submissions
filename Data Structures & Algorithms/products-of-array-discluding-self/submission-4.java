class Solution {
    public int[] productExceptSelf(int[] nums) {
        int length = nums.length;
        int[] prefix = new int[length];
        int[] postfix = new int[length];

        int product = 1;
        for (int i = 0; i < length; i++) {
            prefix[i] = nums[i] * product;
            product = prefix[i];
        }

        product = 1;
        for (int i = length - 1; i > 0; i--) {
            postfix[i] = nums[i] * product;
            product = postfix[i];
        }

        int[] response = new int[length];
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                response[i] = postfix[i + 1];
            } else if (i == length - 1) {
                response[i] = prefix[i - 1];
            } else {
                response[i] = prefix[i - 1] * postfix[i + 1];
            }
        }
         
        return response;
    }
}  
