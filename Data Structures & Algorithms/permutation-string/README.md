# Permutation in String

**Difficulty:** Medium  
**Pattern:** Sliding Window (fixed size)  
**NeetCode:** [Permutation String](https://neetcode.io/problems/permutation-string)  
**LeetCode:** [#567 – Permutation in String](https://leetcode.com/problems/permutation-in-string/)

---

## Problem

Given two strings `s1` and `s2`, return `true` if `s2` contains a permutation of `s1`, or `false` otherwise. In other words, return `true` if one of the substrings of `s2` is a rearrangement of `s1`.

```
Input:  s1 = "ab", s2 = "eidbaooo"
Output: true  ("ba" is a permutation of "ab")

Input:  s1 = "ab", s2 = "eidboaoo"
Output: false
```

---

## Key Insight

Two strings are permutations of each other if and only if their **character frequency counts are identical**. That is the same idea as [Is Anagram](../is-anagram/README.md), applied repeatedly.

Any permutation of `s1` has exactly `s1.length()` characters, so the only substrings of `s2` worth checking are the ones of that exact length. This makes it a **fixed-size sliding window**: slide a window of length `n` across `s2` and compare its frequency count with the frequency count of `s1`.

---

## Two Solutions: Brute Force to Optimal

### Solution 1: Rebuild the Frequency Map for Every Window (O(n·m))

```java
class Solution {
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        Map<Character, Integer> s1Map = new HashMap<>();
        for (int i = 0; i < s1.length(); i++) {
            s1Map.merge(s1.charAt(i), 1, Integer::sum);
        }

        int right = s1.length() - 1;
        int left = 0;
        while (right <= s2.length() - 1) {
            // fresh copy of the required counts for every window
            Map<Character, Integer> s1Chars = new HashMap<>(s1Map);
            for (int i = left; i <= right; i++) {
                // consume one required occurrence; remove the key when it reaches zero
                s1Chars.computeIfPresent(s2.charAt(i), (k, v) -> v == 1 ? null : v - 1);
            }
            if (s1Chars.isEmpty()) {
                return true; // every required character was consumed exactly
            }
            left++;
            right++;
        }

        return false;
    }
}
```

**What it does:** Builds the frequency map of `s1` once. For every window of length `n` in `s2`, it copies that map and "spends" one required occurrence per window character. If the copy ends up empty, every required character was found in the right quantity.

**Why an empty map proves a permutation:** The window has exactly `n` characters and the map holds exactly `n` required occurrences in total. The map can only be emptied if every window character consumed one occurrence, so the window must match `s1` exactly. Extra occurrences of a character are ignored, because `computeIfPresent` does nothing once the key is gone, and they leave the map non-empty elsewhere.

**The problems:**

- **Every window does O(n) work from scratch.** Total time is O((m - n + 1) · n), roughly O(n·m). It never reuses the previous window.
- **A new `HashMap` is allocated and copied per window.** That is a lot of allocation and boxing for a simple comparison.

**When to use:** Explain this first to establish correctness, then optimise by noticing that neighbouring windows share almost all of their characters.

---

### Solution 2: Two `int[26]` Arrays, Update Incrementally (O(m))

```java
class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int n = s1.length(); int m = s2.length();

        if (n > m) return false;

        int[] window = new int[26];   // frequency of s1
        int[] complete = new int[26]; // frequency of the current window in s2

        // fill s1's counts and the first window of s2 in one pass
        for (int i = 0; i < n; i++) {
            window[s1.charAt(i) - 'a']++;
            complete[s2.charAt(i) - 'a']++;
        }

        for (int i = n; i < m; i++) {
            if (Arrays.equals(window, complete)) return true;

            complete[s2.charAt(i) - 'a']++;      // character entering on the right
            complete[s2.charAt(i - n) - 'a']--;  // character leaving on the left
        }

        // the loop checks a window before sliding, so the last window is still unchecked
        return Arrays.equals(window, complete);
    }
}
```

**Four improvements over Solution 1:**

**1. Slide the window instead of rebuilding it.** Moving one position right changes exactly two counts: one character enters and one leaves. That is O(1) per step instead of O(n).

**2. Fixed-size `int[26]` arrays replace `HashMap<Character, Integer>`.** There is no boxing, no hashing and no per-window allocation. Indexing with `c - 'a'` is a direct array lookup.

**3. `Arrays.equals` replaces the "is the map empty" trick.** Comparing two 26-element arrays is a constant-time check and is easier to read.

**4. One loop fills both arrays.** Because both strings contribute exactly `n` characters to the first window, a single pass initialises everything.

**This is the solution to present in interviews.**

---

## Walk-through: `s1 = "ab"`, `s2 = "eidbaooo"`

Using Solution 2. Required counts: `a:1, b:1`. First window is `"ei"`.

```
i=2: window "ei" == ab?  No → add 'd', remove 'e' → window "id"
i=3: window "id" == ab?  No → add 'b', remove 'i' → window "db"
i=4: window "db" == ab?  No → add 'a', remove 'd' → window "ba"
i=5: window "ba" == ab?  Yes → return true ✓
```

---

## Solution Comparison

| | Solution 1 | Solution 2 |
|---|---|---|
| **Time** | O((m − n + 1) · n) ≈ O(n·m) | O(m) (26 comparisons per step) |
| **Space** | O(1): up to 26 distinct keys | O(1): two `int[26]` arrays |
| **Window update** | Rebuilt from scratch | Incremental: one in, one out |
| **Data structure** | `HashMap<Character, Integer>` copied per window | `int[26]` arrays |
| **Boxing / allocation** | Heavy | None |
| **Character set** | Any character | Lowercase `a-z` only |
| **Readability** | ★★★☆☆ | ★★★★☆ |
| **Interview recommendation** | Explain first | Present this |

---

## Complexity (Solution 2)

| | |
|---|---|
| **Time** | O(m): `n` steps to fill, then at most `m − n` slides, each doing an O(1) update plus a 26-element comparison |
| **Space** | O(1): two fixed 26-element arrays regardless of input size |

---

## Follow-up Optimisation: O(m) Without the 26-Factor

`Arrays.equals` costs 26 comparisons per step. You can drop that by tracking how many of the 26 letters currently match, and updating it only for the two letters that change:

```java
int matches = 0;
for (int i = 0; i < 26; i++) {
    if (window[i] == complete[i]) matches++;
}

for (int i = n; i < m; i++) {
    if (matches == 26) return true;

    int add = s2.charAt(i) - 'a';
    complete[add]++;
    if (complete[add] == window[add]) matches++;
    else if (complete[add] == window[add] + 1) matches--;

    int remove = s2.charAt(i - n) - 'a';
    complete[remove]--;
    if (complete[remove] == window[remove]) matches++;
    else if (complete[remove] == window[remove] - 1) matches--;
}
return matches == 26;
```

The idea: a letter's status flips between "matching" and "not matching" only when its count crosses the target value. This is worth mentioning in an interview even if you do not write it.

---

## Gotchas

- **The check happens before the slide, so the last window needs a final check.** The loop compares first and slides afterwards. When the loop ends, the final window has been built but never compared, which is why the method ends with `return Arrays.equals(window, complete)`. It also covers `m == n`, where the loop body never runs. Forgetting this line fails exactly the case where the permutation sits at the very end of `s2`.
- **The array names are swapped relative to intuition.** In Solution 2, `window` holds the counts of `s1` and `complete` holds the sliding window of `s2`. It works, but it reads backwards. Names like `s1Count` and `windowCount` are clearer.
- **Lowercase letters only.** `c - 'a'` throws `ArrayIndexOutOfBoundsException` for uppercase letters, digits or symbols. The problem guarantees lowercase English letters. For a wider alphabet, use a `HashMap` or a larger array such as `int[128]`.
- **`n > m` must return `false` immediately.** Otherwise the initial fill reads past the end of `s2`.
- **The leaving character is `s2.charAt(i - n)`.** With a window of length `n` ending at index `i` after the add, the character that falls out is `n` positions behind. An off-by-one here silently corrupts every later comparison.

---

## Interview Tips

- State the reduction immediately: *"A permutation has the same character frequencies, so I need a window of length `s1.length()` in `s2` whose frequency count equals that of `s1`."*
- Mention the naive approach first: sorting every window and comparing is O(m · n log n). Then move to frequency maps, then to the incremental window.
- Explain why the window size is fixed: any permutation of `s1` has exactly `n` characters, so no other length can match. That is what makes it a fixed-size window, not a variable one.
- When presenting Solution 2, point out that each slide changes only two counts, which is where the speed-up comes from.
- Mention the `matches` counter as the follow-up optimisation to show you know the comparison itself can be made O(1).
- Related problems: [Is Anagram](../is-anagram/README.md) (the single-comparison version of this idea), and Find All Anagrams in a String (LeetCode #438), which is the same algorithm collecting every starting index instead of returning on the first match.
- Edge cases to mention: `s1` longer than `s2`, `s1` and `s2` of equal length, a permutation at the very start or very end of `s2`, and repeated characters in `s1`.
