class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>(k*2);
        int[] result = new int[k];

        for (int num : nums) {
            freq.merge(num, 1, Integer::sum);
        }

        List<Integer> sortedNumbers = freq.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());;

        for (int i = 0; i < k;i++) {
            result[i] = sortedNumbers.get(i);
        }

        return result;
    }
}
