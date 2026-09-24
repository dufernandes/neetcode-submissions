class Solution {
    // brute force
    public int characterReplacement(String s, int k) {
        Map<Character, Integer> frequency = new HashMap<>();
        int max = 0;
        for (int i = 0; i < s.length(); i++) {
            int mostFrequent = 0;
            for (int j = i; j < s.length(); j++) {
                int count = frequency.merge(s.charAt(j), 1, Integer::sum);
                mostFrequent = Math.max(count, mostFrequent);

                if (j - i + 1 - mostFrequent <= k) {
                    max = Math.max(max, j - i + 1);
                }
            }
            frequency.clear();
        }

        return max;
    }
}
