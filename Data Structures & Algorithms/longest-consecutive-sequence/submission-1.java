class Solution {
    public int longestConsecutive(int[] nums) {
        if (nums.length == 0) return 0;
        Arrays.sort(nums);
        
        int highestCounter = 1, currentCounter = 1;
        int lastElement = nums[0];
        for (int i = 0; i < nums.length; i++) {
            int value = nums[i];
            if (lastElement == value) continue;
            
            if (value - lastElement == 1) {
                lastElement = value;
                currentCounter++;
            } else if (value - lastElement > 1) {
                lastElement = value;
                if (currentCounter > highestCounter) {
                    highestCounter = currentCounter;
                }
                currentCounter = 1;
            }
        }

        if (currentCounter > highestCounter) {
            highestCounter = currentCounter;
        }

        return highestCounter;
    }
}
