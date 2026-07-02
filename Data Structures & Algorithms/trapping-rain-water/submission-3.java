class Solution {
    public int trap(int[] height) {

        if (height == null || height.length == 0) return 0;

        int volume = 0, maxLeft = height[0], maxRight = height[height.length - 1];
        int left = 1, right = height.length - 2;

        while (left <= right) {
            if (maxLeft <= maxRight) {
                volume += Math.max(0, maxLeft - height[left]);
                maxLeft = Math.max(maxLeft, height[left]);
                left++;
            } else {
                volume += Math.max(0, maxRight - height[right]);
                maxRight = Math.max(maxRight, height[right]);
                right--;
            }
        }

        return volume;
    }
}
