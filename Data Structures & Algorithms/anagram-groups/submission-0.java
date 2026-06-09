class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> anagrams = new HashMap<>();

        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            anagrams
            .computeIfAbsent(new String(chars),
                k -> new ArrayList())
                    .add(str);
        }

        return new ArrayList<>(anagrams.values());
    }
}
