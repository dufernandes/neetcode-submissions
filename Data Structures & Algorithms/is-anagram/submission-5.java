class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        Map<Character, Integer> sMap = new HashMap<>();
        Map<Character, Integer> tMap = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char sChar = s.charAt(i);
            char tChar = t.charAt(i);

            sMap.merge(sChar, 1, Integer::sum);
            tMap.merge(tChar, 1, Integer::sum);
        }

        for (Map.Entry<Character, Integer> sSet : sMap.entrySet()) {
            if (tMap.get(sSet.getKey()) == null && sSet.getValue() == null) {
                continue;
            }

            if (tMap.get(sSet.getKey()) == null && sSet.getValue() != null) {
                return false;
            }

            if (tMap.get(sSet.getKey()) != null && sSet.getValue() == null) {
                return false;
            }

            if (!tMap.get(sSet.getKey()).equals(sSet.getValue())) {
                return false;
            }
        }

        return true;
    }
}
