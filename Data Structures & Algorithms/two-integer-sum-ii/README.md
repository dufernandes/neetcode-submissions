# Two Sum II (Two Integer Sum II)

**Difficulty:** Medium  
**Pattern:** Two Pointers  
**NeetCode:** [Two Integer Sum II](https://neetcode.io/problems/two-integer-sum-ii)  
**LeetCode:** [#167 – Two Sum II – Input Array Is Sorted](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/)

---

## Problem

Given a **1-indexed** sorted array `numbers`, return the indices of the two numbers that add up to `target`. There is exactly one solution. You may not use the same element twice. Must use O(1) extra space.

```
Input:  numbers = [2, 7, 11, 15], target = 9
Output: [1, 2]

Input:  numbers = [2, 3, 4], target = 6
Output: [1, 3]
```

---

## Key Insight

The array is already sorted — use that. Place one pointer at each end. If the sum is too small, move `left` right to increase it. If too large, move `right` left to decrease it. The problem guarantees exactly one solution, so the pointers will always converge on it.

This is the direct prerequisite to 3Sum — that problem reduces to running this algorithm once per element.

---

## Solution

```java
class Solution {
    public int[] twoSum(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int value = numbers[left] + numbers[right];
            if (value == target) return new int[]{left + 1, right + 1};
            else if (value < target) left++;
            else right--;
        }
        return null; // guaranteed not to reach here
    }
}
```

> Note: Return `left + 1` and `right + 1` — the problem is 1-indexed. Easy to forget under pressure.

---

## Walk-through: `[2, 7, 11, 15]`, target = `9`

```
left=0 (2), right=3 (15) → sum=17 > 9 → right--
left=0 (2), right=2 (11) → sum=13 > 9 → right--
left=0 (2), right=1 (7)  → sum=9  = 9 → return [1, 2] ✓
```

---

## Alternative: Brute Force (O(n²))

Check every pair. Ignores the sorted property entirely.

```java
class Solution {
    public int[] twoSum(int[] numbers, int target) {
        for (int i = 0; i < numbers.length - 1; i++)
            for (int j = i + 1; j < numbers.length; j++)
                if (numbers[i] + numbers[j] == target)
                    return new int[]{i + 1, j + 1};
        return null;
    }
}
```

The two-pointer solution is preferred — same O(1) space, but O(n) vs O(n²) time.

---

## Complexity

| | |
|---|---|
| **Time** | O(n) — each pointer moves at most n steps total |
| **Space** | O(1) — two pointers only, no extra structure |

---

## Interview Tips

- The sorted precondition is the signal — always ask "is the array sorted?" in Two Sum variants. If yes, two pointers. If no, HashMap.
- Don't forget the 1-indexed return — `left + 1` and `right + 1`, not `left` and `right`.
- This problem is explicitly listed as a prerequisite for 3Sum on NeetCode. Make sure you can solve it in under 2 minutes before moving on.
- The `return null` at the end is technically unreachable — the problem guarantees a solution exists — but include it to satisfy the compiler.

---

## Review Log

| Date solved | Review due |
|---|---|
| — | — |
