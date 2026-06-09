class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> diffs = new HashMap<>();

        diffs.put(nums[0], 0);
        for (int i = 1; i < nums.length; i++) {
            int difference = target - nums[i];
            if (diffs.get(difference) != null) {
                return new int[]{diffs.get(difference), i};
            } else {
                diffs.put(nums[i], i);
            }
        }

        return null;
    }
}
