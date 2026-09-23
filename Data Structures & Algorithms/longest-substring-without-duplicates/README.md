# Longest Substring Without Repeating Characters

**Difficulty:** Medium  
**Pattern:** Sliding Window  
**NeetCode:** [Longest Substring Without Repeating Characters](https://neetcode.io/problems/longest-substring-without-duplicate-characters)  
**LeetCode:** [#3 – Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)

---

## Problem

Given a string `s`, find the length of the longest substring without duplicate characters.

```
Input:  s = "abcabcbb"
Output: 3  ("abc")

Input:  s = "bbbbb"
Output: 1  ("b")

Input:  s = "pwwkew"
Output: 3  ("wke")
```

---

## Key Insight

Use a sliding window with two pointers — `left` and `right`. Expand `right` freely. When a duplicate is found inside the current window, jump `left` directly to the position **after the last occurrence** of the duplicate character — skipping past it in one move instead of stepping one character at a time.

A `HashMap` stores the last seen index of each character, enabling O(1) jumps.

---

## Solution

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastIndex = new HashMap<>();
        int left = 0, longest = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);

            // duplicate inside the current window — jump left past it
            if (lastIndex.containsKey(c) && lastIndex.get(c) >= left) {
                left = lastIndex.get(c) + 1;
            }

            lastIndex.put(c, right);
            longest = Math.max(longest, right - left + 1);
        }

        return longest;
    }
}
```

> Note: `lastIndex.get(c) >= left` is the critical guard — it ensures the previous occurrence of `c` is actually inside the current window, not a stale entry from before `left` moved. Without it, `left` could jump backwards.

---

## Walk-through: `"abcabcbb"`

```
right=0, c='a': not seen → lastIndex={a:0}, window=[0,0], longest=1
right=1, c='b': not seen → lastIndex={a:0,b:1}, window=[0,1], longest=2
right=2, c='c': not seen → lastIndex={a:0,b:1,c:2}, window=[0,2], longest=3
right=3, c='a': seen at 0, 0>=left(0) → left=1
                lastIndex={a:3,b:1,c:2}, window=[1,3], longest=3
right=4, c='b': seen at 1, 1>=left(1) → left=2
                lastIndex={a:3,b:4,c:2}, window=[2,4], longest=3
right=5, c='c': seen at 2, 2>=left(2) → left=3
                lastIndex={a:3,b:4,c:5}, window=[3,5], longest=3
right=6, c='b': seen at 4, 4>=left(3) → left=5
                lastIndex={a:3,b:6,c:5}, window=[5,6], longest=3
right=7, c='b': seen at 6, 6>=left(5) → left=7
                lastIndex={a:3,b:7,c:5}, window=[7,7], longest=3

return 3 ✓
```

---

## Alternative: Sliding Window with HashSet (simpler, slower jumps)

Instead of jumping `left` directly, shrink the window one step at a time by removing characters from a `HashSet`.

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> window = new HashSet<>();
        int left = 0, longest = 0;

        for (int right = 0; right < s.length(); right++) {
            while (window.contains(s.charAt(right))) {
                window.remove(s.charAt(left));
                left++;
            }
            window.add(s.charAt(right));
            longest = Math.max(longest, right - left + 1);
        }

        return longest;
    }
}
```

**Difference:** The HashSet version shrinks `left` one step at a time — O(n) total but with more iterations in the worst case. The HashMap version jumps `left` directly to the right position — fewer iterations, same asymptotic complexity but faster in practice.

**When to use the HashSet version:** When you want simpler code and don't need the index-jump optimisation. Both are valid interview answers — the HashMap version shows deeper understanding.

---

## Complexity

| | |
|---|---|
| **Time** | O(n) — `right` moves forward n times, `left` only moves forward |
| **Space** | O(min(n, m)) — where m is the size of the character set (26 for lowercase, 128 for ASCII) |

---

## Gotchas

- **`lastIndex.get(c) >= left` is mandatory** — without this guard, a character seen before the current window would incorrectly move `left` backwards. Example: `"abba"` — when `right=3` sees `'a'` last seen at index 0, but `left` has already moved to 2. Without the guard, `left` jumps back to 1, incorrectly expanding the window to include a duplicate `'b'`.
- **`left` only moves forward** — sliding window invariant. The `>= left` check enforces this.
- **`lastIndex.put(c, right)` always updates** — even when you jump `left`, you still update the map with the new position. The old entry is overwritten, which is correct.
- **Window size is `right - left + 1`** — not `right - left`. Off-by-one here gives the wrong answer on single-character windows.
- **Empty string** — `s = ""` returns 0 correctly, the loop never executes.

---

## Interview Tips

- Name the pattern: *"This is a variable-size sliding window with a HashMap to track last seen indices."*
- Mention the HashSet alternative first if you need time to think: *"A simpler approach uses a HashSet and shrinks left one step at a time. I can optimise it with a HashMap to jump left directly."*
- Explain the `>= left` guard proactively — interviewers almost always ask about it. *"Without this, left could jump backwards if the character was seen before the current window started."*
- Walk through `"abba"` as the edge case — it's the canonical example that exposes the missing guard bug.
- Follow-up: *"What if the string contains Unicode?"* — the HashMap handles it natively. A fixed-size array (common int[128] optimisation) would not cover all Unicode characters.

---
