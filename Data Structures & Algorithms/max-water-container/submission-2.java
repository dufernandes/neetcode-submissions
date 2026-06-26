class Solution {
    public int maxArea(int[] heights) {
        int maxArea = 0, left = 0, right = heights.length - 1;
        while (left < right) {
            int tempArea = Math.min(heights[left], heights[right]) * (right - left);
            maxArea = Math.max(tempArea, maxArea);
            if (heights[left] < heights[right]) left++;
            else right--;
        }

        return maxArea;
    }
}
