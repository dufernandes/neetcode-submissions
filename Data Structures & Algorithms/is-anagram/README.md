# Is Anagram (Valid Anagram)

**Difficulty:** Easy  
**Pattern:** Hash Map Lookup  
**NeetCode:** [Is Anagram](https://neetcode.io/problems/is-anagram)  
**LeetCode:** [#242 – Valid Anagram](https://leetcode.com/problems/valid-anagram/)

---

## Problem

Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`, `false` otherwise. An anagram uses all the original letters exactly once in a different arrangement.

```
Input:  s = "anagram", t = "nagaram"
Output: true

Input:  s = "rat", t = "car"
Output: false
```

---

## Key Insight

Two strings are anagrams if and only if their **character frequency maps are identical**. Build one frequency map per string in a single pass, then compare them.

An early exit is possible: if `s.length() != t.length()`, they can never be anagrams.

---

## Solution

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        Map<Character, Integer> sMap = new HashMap<>();
        Map<Character, Integer> tMap = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            sMap.merge(s.charAt(i), 1, Integer::sum);
            tMap.merge(t.charAt(i), 1, Integer::sum);
        }

        return sMap.equals(tMap);
    }
}
```

> Note: `Map.merge(key, 1, Integer::sum)` is a clean Java idiom for `getOrDefault(key, 0) + 1`.

---

## Alternative: Single Map

Instead of two maps, increment for `s` and decrement for `t` in the same map. If all values end at 0, it's an anagram.

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            freq.merge(s.charAt(i), 1, Integer::sum);
            freq.merge(t.charAt(i), -1, Integer::sum);
        }

        return freq.values().stream().allMatch(v -> v == 0);
    }
}
```

---

## Complexity

| | |
|---|---|
| **Time** | O(n) — one pass through both strings simultaneously |
| **Space** | O(1) — at most 26 lowercase letters in the map (fixed alphabet) |

---

## Interview Tips

- For **Unicode / international strings**, a HashMap is necessary. For pure lowercase ASCII, a `int[26]` array works and is faster in practice.
- The single-map approach halves memory usage and is worth mentioning as an optimization.
- `Map.equals()` compares all key-value pairs — perfectly suitable here instead of manual iteration.
