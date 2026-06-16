class Solution {
    public int[] productExceptSelf(int[] nums) {
        int length = nums.length;
        int[] prefix = new int[length];
        int[] postfix = new int[length];

        // prefix[i] = product of all elements from 0 to i (inclusive)
        int product = 1;
        for (int i = 0; i < length; i++) {
            prefix[i] = nums[i] * product;
            product = prefix[i];
        }

        // postfix[i] = product of all elements from i to length-1 (inclusive)
        product = 1;
        for (int i = length - 1; i >= 0; i--) {  // fix: was i > 0, missing postfix[0]
            postfix[i] = nums[i] * product;
            product = postfix[i];
        }

        // result[i] = product of everything to the left * product of everything to the right
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            int left = (i == 0) ? 1 : prefix[i - 1];
            int right = (i == length - 1) ? 1 : postfix[i + 1];
            result[i] = left * right;
        }

        return result;
    }
}