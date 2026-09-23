class Solution {
    public int lengthOfLongestSubstring(String s) {
    
        Map<Character, Integer> lastIndex = new HashMap<>();
        int left = 0; int longest = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);

            if (lastIndex.containsKey(c) && lastIndex.get(c) >= left) {
                left = lastIndex.get(c) + 1;
            }
            
            lastIndex.put(c, right);
            longest = Math.max(longest, right - left + 1);
        }

        return longest;
    }
}
