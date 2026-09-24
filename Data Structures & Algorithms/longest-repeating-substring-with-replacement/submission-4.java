class Solution {
    // brute force
    public int characterReplacement(String s, int k) {
        Map<Character, Integer> frequency = new HashMap<>();
        int max = 0;
        int left = 0; int right = 0; int mostFrequent = 0;
        char mostFrequentChar = 'a';
        for (right = 0; right < s.length(); right++) {
            
            int count = frequency.merge(s.charAt(right), 1, Integer::sum);
            mostFrequent = Math.max(count, mostFrequent);

            if (count == mostFrequent) {
                mostFrequentChar = s.charAt(right);
            }

            if (right - left + 1 - mostFrequent <= k) {
                max = Math.max(max, right - left + 1);
            } else {
                if (s.charAt(left) == mostFrequentChar) {
                    mostFrequent--;
                }
                frequency.merge(s.charAt(left), -1, Integer::sum);
                left++;
            }
            
        }

        return max;
    }
}
