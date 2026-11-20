class Solution {
    public int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length == 0) return 0;

        int largestArea = 0;
        Deque<int[]> stack = new ArrayDeque<>();

        stack.addFirst(new int[]{0, heights[0]});
        for (int i = 1; i < heights.length; i++) {
            
            int currentHeight = heights[i];
            int previousHeight = heights[i - 1];
            int minWidth = i;
            while (!stack.isEmpty() && currentHeight < stack.peekFirst()[1]) {
                int[] measure = stack.removeFirst();
                largestArea = Math.max(largestArea, measure[1] * (i - measure[0]));
                minWidth = measure[0];
            }

            stack.addFirst(new int []{minWidth, currentHeight});
        }

        while (!stack.isEmpty()) {
            int[] measure = stack.removeFirst();
            int index = measure[0];
            int height = measure[1];

            largestArea = Math.max(largestArea, height * (heights.length - index));
        }

        return largestArea;
    }
}
