# Longest Consecutive Sequence

**Difficulty:** Medium  
**Pattern:** Hash Map Lookup  
**NeetCode:** [Longest Consecutive Sequence](https://neetcode.io/problems/longest-consecutive-sequence)  
**LeetCode:** [#128 – Longest Consecutive Sequence](https://leetcode.com/problems/longest-consecutive-sequence/)

---

## Problem

Given an unsorted array of integers `nums`, return the length of the longest consecutive sequence of integers that can be formed from the values in the array (order in the array doesn't matter).

```
Input:  nums = [0, 3, 2, 5, 4, 6, 1, 1]
Output: 7
// Values 0,1,2,3,4,5,6 are all present → sequence length 7
// The duplicate 1 collapses to a single value in the set
```

---

## Key Insight

**Only start counting from the beginning of a sequence.** A number `n` is the start of a sequence if and only if `n - 1` is **not** in the set. From each start, extend the sequence by checking `n+1`, `n+2`, ... until a gap is found.

This avoids redundant work: without this guard, starting from every element gives O(n²). With it, each number is visited at most twice — O(n).

```
Set = {0,1,2,3,4,5,6}

0: is 0-1=-1 in set? No → start here. Count: 0,1,2,3,4,5,6 → length 7
1: is 1-1=0 in set? Yes → skip (already counted above)
2: is 2-1=1 in set? Yes → skip
... and so on
```

---

## Solution (O(n log n) Sort-based — submitted)

```java
class Solution {
    public int longestConsecutive(int[] nums) {
        if (nums.length == 0) return 0;
        Arrays.sort(nums);

        int highest = 1, current = 1;
        int last = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int value = nums[i];
            if (value == last) continue;           // skip duplicates

            if (value - last == 1) {
                current++;                          // extend sequence
            } else {
                highest = Math.max(highest, current);
                current = 1;                        // reset
            }
            last = value;
        }

        return Math.max(highest, current);          // don't forget the final sequence
    }
}
```

---

## Optimal Solution (O(n) HashSet)

```java
class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums) set.add(n);

        int longest = 0;

        for (int n : set) {
            if (set.contains(n - 1)) continue;  // only start from sequence beginnings

            int length = 1;
            while (set.contains(n + length)) length++;
            longest = Math.max(longest, length);
        }

        return longest;
    }
}
```

---

## Complexity

| Approach | Time | Space |
|---|---|---|
| Sort (submitted) | O(n log n) | O(1) |
| HashSet (optimal) | O(n) | O(n) |

---

## Interview Tips

- The problem explicitly requires O(n) — know the HashSet approach. The sort solution is a valid intermediate step to discuss before presenting the optimal.
- The "only start at sequence beginnings" guard is the entire trick. Without it the HashSet solution is still O(n²).
- The sort approach has a subtle bug trap: **forgetting to compare the final sequence** after the loop ends. Always update `highest` after the loop.
- Duplicates are handled naturally by both approaches: HashSet deduplicates on insertion; the sort approach skips equal adjacent values with `if (value == last) continue`.
