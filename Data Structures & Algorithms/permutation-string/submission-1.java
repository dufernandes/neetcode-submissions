class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int n = s1.length(); int m = s2.length();

        if (n > m) return false;

        int[] window = new int[26];
        int[] complete = new int[26];

        for (int i = 0; i < n; i++) {
            window[s1.charAt(i) - 'a']++;
            complete[s2.charAt(i) - 'a']++;
        }

        for (int i = n; i < m; i++) {
            if (Arrays.equals(window, complete) == true) return true;

            complete[s2.charAt(i) - 'a']++;
            complete[s2.charAt(i - n) - 'a']--;
        }

        return Arrays.equals(window, complete);
    }
}
