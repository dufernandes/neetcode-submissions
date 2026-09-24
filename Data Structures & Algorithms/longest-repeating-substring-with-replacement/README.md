# Longest Repeating Character Replacement

**Difficulty:** Medium  
**Pattern:** Sliding Window  
**NeetCode:** [Longest Repeating Character Replacement](https://neetcode.io/problems/longest-repeating-substring-with-replacement)  
**LeetCode:** [#424 – Longest Repeating Character Replacement](https://leetcode.com/problems/longest-repeating-character-replacement/)

---

## Problem

Given a string `s` and an integer `k`, you can replace at most `k` characters in the string with any letter. Return the length of the longest substring containing the same letter after performing at most `k` replacements.

```
Input:  s = "ABAB", k = 2
Output: 4  (replace both 'A's or both 'B's → "AAAA" or "BBBB")

Input:  s = "AABABBA", k = 1
Output: 4  (replace the one 'B' in "AABA" → "AAAA")
```

---

## Key Insight

A window is valid when the number of characters that need replacing does not exceed `k`:

```
replacements needed = window length - count of most frequent character in window
valid when: (right - left + 1) - mostFrequent <= k
```

Expand `right` freely. When the window becomes invalid, shrink from `left`. Track the most frequent character count — if it changes, the window validity changes.

The critical optimisation across all three solutions: you only need to track the **count of the most frequent character**, not recalculate it from scratch each time.

---

## Three Solutions — Evolution from O(n²) to O(n)

### Solution 1 — Nested Loops, Brute Force (O(n²))

```java
class Solution {
    public int characterReplacement(String s, int k) {
        Map<Character, Integer> frequency = new HashMap<>();
        int max = 0;

        for (int i = 0; i < s.length(); i++) {
            int mostFrequent = 0;
            for (int j = i; j < s.length(); j++) {
                int count = frequency.merge(s.charAt(j), 1, Integer::sum);
                mostFrequent = Math.max(count, mostFrequent);

                if (j - i + 1 - mostFrequent <= k) {
                    max = Math.max(max, j - i + 1);
                }
            }
            frequency.clear(); // reset for next starting position
        }

        return max;
    }
}
```

**What it does:** For every starting position `i`, expands `j` to the right building a frequency map. At each step checks if the window is valid and updates the maximum.

**Why it works:** Correctly applies the validity formula at every possible window. `frequency.clear()` resets the map for each new starting position.

**The problem:** O(n²) time — two nested loops over the string. `frequency.clear()` is called n times, each clearing up to 26 entries. On large inputs this TLEs.

**When to use:** Explain first to establish the formula and the logic before optimising.

---

### Solution 2 — Single Pass, but Flawed mostFrequent Tracking (O(n), subtle bug)

```java
class Solution {
    public int characterReplacement(String s, int k) {
        Map<Character, Integer> frequency = new HashMap<>();
        int max = 0;
        int left = 0, right = 0;
        int mostFrequent = 0;
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
                // shrink window from left
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
```

**Two improvements over Solution 1:**

**1. Single pass — O(n).** No nested loop, no `frequency.clear()`. `left` only moves forward when the window is invalid.

**2. Tracks `mostFrequentChar`** to decide whether to decrement `mostFrequent` when shrinking. If the character leaving the window (`s.charAt(left)`) is the most frequent, the count drops.

**The subtle flaw:** `mostFrequentChar` is updated only when `count == mostFrequent` — but this can assign the wrong character when two characters tie for the highest count. If `mostFrequentChar` is set incorrectly, the shrink logic decrements `mostFrequent` for the wrong character, producing incorrect window sizes.

This passes many test cases but can fail on edge cases with tied character frequencies. It is a stepping stone, not a production solution.

---

### Solution 3 — Single Pass, Correct mostFrequent Tracking (O(n), correct)

```java
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

            // update most frequent only on strict improvement
            if (count > mostFrequentCount) {
                mostFrequentCount = count;
                mostFrequentChar = rightChar;
            }

            int windowLength = right - left + 1;
            if (windowLength - mostFrequentCount <= k) {
                max = Math.max(max, windowLength);
                continue; // window valid — keep expanding
            }

            // window invalid — shrink from left
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
```

**What changed from Solution 2:**

**`count > mostFrequentCount` instead of `count == mostFrequent`.** This is the fix. Updating `mostFrequentChar` only on strict improvement — not on equality — avoids the tie-breaking ambiguity that caused Solution 2's edge case failures.

**`continue` after valid window** makes the control flow explicit: expand right when valid, shrink left when not. Cleaner than the if/else structure in Solution 2.

**This is the correct solution. Present this in interviews.**

---

## Walk-through: `"AABABBA"`, k=1

Using Solution 3:

```
right=0, c='A': freq={A:1}, mostFrequent=1 (A), window=1, valid → max=1
right=1, c='A': freq={A:2}, mostFrequent=2 (A), window=2, valid → max=2
right=2, c='B': freq={A:2,B:1}, mostFrequent=2 (A), window=3, 3-2=1<=1 valid → max=3
right=3, c='A': freq={A:3,B:1}, mostFrequent=3 (A), window=4, 4-3=1<=1 valid → max=4
right=4, c='B': freq={A:3,B:2}, mostFrequent=3 (A), window=5, 5-3=2>1 invalid
               leftChar='A'==mostFrequentChar → mostFrequent=2, freq={A:2,B:2}, left=1
right=5, c='B': freq={A:2,B:3}, mostFrequent=3 (B), window=5, 5-3=2>1 invalid
               leftChar='A'!=mostFrequentChar(B), freq={A:1,B:3}, left=2
right=6, c='A': freq={A:2,B:3}, mostFrequent=3 (B), window=5, 5-3=2>1 invalid
               leftChar='B'==mostFrequentChar → mostFrequent=2, freq={A:2,B:2}, left=3

return 4 ✓
```

---

## Solution Comparison

| | Solution 1 | Solution 2 | Solution 3 |
|---|---|---|---|
| **Time** | O(n²) | O(n) | O(n) |
| **Space** | O(1) — 26 chars max | O(1) — 26 chars max | O(1) — 26 chars max |
| **Correctness** | ✓ | ✗ edge cases | ✓ |
| **mostFrequent update** | per inner loop | on equality (`==`) | on strict improvement (`>`) |
| **Interview recommendation** | Explain first | Don't present | Present this |

---

## Complexity (Solution 3)

| | |
|---|---|
| **Time** | O(n) — `right` advances n times, `left` only moves forward |
| **Space** | O(1) — at most 26 uppercase letters in the frequency map |

---

## Gotchas

- **`count > mostFrequentCount` not `count >= mostFrequentCount`** — the strict greater-than is what fixes Solution 2's bug. On equality, keep the existing `mostFrequentChar` — it's already correct.
- **`mostFrequent` only decrements when the leaving character is the most frequent** — if a less frequent character leaves the window, `mostFrequentCount` stays the same. The window may not actually be valid at that point, but the next iteration will correct it.
- **`left` moves at most one step per iteration** — you never loop to shrink. This is intentional: the window never needs to shrink by more than one because you're looking for the maximum length, not all valid windows.
- **Space is O(1) not O(n)** — the frequency map holds at most 26 uppercase English letters, regardless of input length.
- **`frequency.merge(char, -1, Integer::sum)`** — can leave entries with value 0 in the map. This is harmless because a count of 0 never beats `mostFrequentCount`.

---

## Interview Tips

- State the validity formula immediately: *"A window is valid when its length minus the count of the most frequent character is at most k."*
- Present Solution 1 first to establish the formula, then say: *"I can do this in O(n) with a sliding window — expand right, shrink left when invalid."*
- When presenting Solution 3, explain the `>` vs `==` distinction proactively — it's the subtlest part and interviewers will probe it.
- The "left moves at most once per iteration" insight is worth stating: *"I never need to shrink more than one step because I'm maximising the window length, not enumerating all valid windows."*
- Edge cases to mention: all same character (k irrelevant, answer is full length), k >= s.length() (answer is full length), single character string.

---
