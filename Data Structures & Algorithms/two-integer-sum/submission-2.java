class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> diffs = new HashMap<>();
        int[] result = null;

        diffs.put(nums[0], 0);
        for (int i = 1; i < nums.length; i++) {
            int difference = target - nums[i];
            if (diffs.containsKey(difference)) {
                result = new int[]{diffs.get(difference), i};
                break;
            } else {
                diffs.put(nums[i], i);
            }
        }

        return result;
    }
}
