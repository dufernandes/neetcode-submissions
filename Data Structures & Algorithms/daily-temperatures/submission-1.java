class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int[] result = new int[temperatures.length];
        Deque<int[]> stack = new ArrayDeque<>();
        for (int i = 0; i < temperatures.length; i++) {
            result[i] = 0;
            while (!stack.isEmpty() && temperatures[i] > stack.peekFirst()[0]) {
                int[] stackedItem = stack.removeFirst();
                result[stackedItem[1]] = i - stackedItem[1];  
            } 
            stack.addFirst(new int[]{temperatures[i], i});
        }

        return result;
    }
}
