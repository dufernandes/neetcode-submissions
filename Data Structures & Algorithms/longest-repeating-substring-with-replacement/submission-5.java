class Solution {
    public int characterReplacement(String s, int k) {
        Map<Character, Integer> frequency = new HashMap<>();
        int left = 0;
        int mostFrequentCount = 0;
        char mostFrequentChar = 'a';
        int max = 0;

        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            int count = frequency.merge(rightChar, 1, Integer::sum);

            if (count > mostFrequentCount) {
                mostFrequentCount = count;
                mostFrequentChar = rightChar;
            }

            int windowLength = right - left + 1;
            if (windowLength - mostFrequentCount <= k) {
                max = Math.max(max, windowLength);
                continue;
            }

            char leftChar = s.charAt(left);
            if (leftChar == mostFrequentChar) {
                mostFrequentCount--;
            }
            frequency.merge(leftChar, -1, Integer::sum);
            left++;
        }

        return max;
    }
}