class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        Arrays.sort(nums);

        int length = nums.length;

        for (int i = 0; i < length - 2; i++) {
            int iValue = nums[i];

            if (i > 0 && iValue == nums[i - 1]) continue;

            int left = i + 1, right = length - 1;
            while (left < right) {
                int value = iValue + nums[left] + nums[right];
                if (value < 0) left++;
                else if (value > 0) right--;
                else {
                    result.add(List.of(iValue, nums[left], nums[right]));
                    left++; right--;

                    while (left < right && nums[left] == nums[left - 1]) left++;
                    while (left < right && nums[right] == nums[right + 1]) right--;
                }
            }
        }

        return result;
    }
}
