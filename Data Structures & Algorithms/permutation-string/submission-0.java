class Solution {
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        Map<Character, Integer> s1Map = new HashMap<>();

        for (int i = 0; i < s1.length(); i++) {
            s1Map.merge(s1.charAt(i), 1, Integer::sum); 
        }

        int counter = 0;
        int right = s1.length() - 1;
        int left = 0;
        while (right <= s2.length() - 1) {
            Map<Character, Integer> s1Chars = new HashMap<>(s1Map);
            for (int i = left; i <= right; i++) {
                s1Chars.computeIfPresent(s2.charAt(i), (k, v) -> v == 1 ? null : v - 1);
            }
            if (s1Chars.isEmpty()) {
                return true;
            }
            left++; right++;
        }

        return false;
    }
}
