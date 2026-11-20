class Solution {
    // Naive O(n^2) brute force: for each bar, expand left and right while bars are >= its height.
    // Correct, but times out on NeetCode (45/46) for large inputs such as many equal-height bars.
    public int largestRectangleArea(int[] heights) {
        int largestArea = 0;

        if (heights == null || heights.length == 0) return 0;

        for (int i = 0; i < heights.length; i++) {
            int height = heights[i];

            // check left width (includes the bar itself)
            int width = 0;
            for (int j = i; j >= 0 && heights[j] >= height; j--) {
                width++;
            }

            // check right width
            for (int j = i + 1; j < heights.length && heights[j] >= height; j++) {
                width++;
            }

            largestArea = Math.max(largestArea, height * width);
        }

        return largestArea;
    }
}
