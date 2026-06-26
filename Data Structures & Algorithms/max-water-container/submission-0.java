class Solution {
    public int maxArea(int[] heights) {
        int maxArea = 0;
        for (int i = 0; i < heights.length - 1; i++) {
            for (int j = 1; j < heights.length; j++) {
                int tempArea = Math.min(heights[i], heights[j]) * (j - i);
                if (tempArea > maxArea) {
                    maxArea = tempArea;
                }
            }
        }

        return maxArea;
    }
}
