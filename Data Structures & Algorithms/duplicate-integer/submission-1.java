class Solution {
    public boolean hasDuplicate(int[] nums) {
        Map<Integer, Integer> duplicates = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int value = nums[i];
            if (duplicates.containsKey(value)) {
                return true;
            } else {
                duplicates.put(value, 1);
            }
        }

        return false;
    }
}