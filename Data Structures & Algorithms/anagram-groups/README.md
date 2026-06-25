# Anagram Groups (Group Anagrams)

**Difficulty:** Medium  
**Pattern:** Hash Map Lookup  
**NeetCode:** [Anagram Groups](https://neetcode.io/problems/anagram-groups)  
**LeetCode:** [#49 – Group Anagrams](https://leetcode.com/problems/group-anagrams/)

---

## Problem

Given an array of strings `strs`, group all anagrams together and return them as a list of groups. The order of the output does not matter.

```
Input:  ["eat","tea","tan","ate","nat","bat"]
Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
```

---

## Key Insight

Anagrams share the exact same characters — just rearranged. If you sort the characters of any anagram, you always get the same string. That sorted string becomes a **canonical key** that groups all anagrams together in a HashMap.

```
"eat" → sorted → "aet"  ┐
"tea" → sorted → "aet"  ├── all map to the same key
"ate" → sorted → "aet"  ┘
"tan" → sorted → "ant"  ┐
"nat" → sorted → "ant"  ┘
"bat" → sorted → "abt"
```

---

## Solution

```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> anagrams = new HashMap<>();

        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            anagrams
                .computeIfAbsent(new String(chars), k -> new ArrayList<>())
                .add(str);
        }

        return new ArrayList<>(anagrams.values());
    }
}
```

> `computeIfAbsent` initializes the list the first time a key is seen, then returns it — cleaner than a manual null-check.

---

## Alternative: Frequency Count Key (avoids sorting)

Instead of sorting (O(k log k) per word), build a frequency array of 26 counts and use that as the key. This reduces per-word cost to O(k).

```java
for (String str : strs) {
    int[] count = new int[26];
    for (char c : str.toCharArray()) count[c - 'a']++;
    String key = Arrays.toString(count); // e.g. "[1,0,0,...,1,0,0]"
    anagrams.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
}
```

---

## Complexity

| Approach | Time | Space |
|---|---|---|
| Sort-based (submitted) | O(n · k log k) | O(n · k) |
| Frequency count | O(n · k) | O(n · k) |

Where `n` = number of strings, `k` = average string length.

---

## Interview Tips

- The sort-based approach is the standard answer and almost always accepted.
- The frequency-count alternative is a good follow-up when the interviewer asks "can you do better?" — it's worth knowing but adds implementation complexity.
- `computeIfAbsent` is idiomatic Java here — if you use `getOrDefault` + `put` instead, it works but signals less familiarity with the Map API.
