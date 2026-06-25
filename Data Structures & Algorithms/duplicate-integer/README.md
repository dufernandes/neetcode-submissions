# Duplicate Integer (Contains Duplicate)

**Difficulty:** Easy  
**Pattern:** Hash Map Lookup  
**NeetCode:** [Duplicate Integer](https://neetcode.io/problems/duplicate-integer)  
**LeetCode:** [#217 – Contains Duplicate](https://leetcode.com/problems/contains-duplicate/)

---

## Problem

Given an integer array `nums`, return `true` if any value appears more than once, `false` otherwise.

```
Input:  nums = [1, 2, 3, 1]
Output: true

Input:  nums = [1, 2, 3, 4]
Output: false
```

---

## Key Insight

Two classic approaches exist with different tradeoffs:

**Option A — Sort first:** Once sorted, duplicates are always adjacent. A single pass then detects them. O(n log n) time, O(1) space (in-place sort).

**Option B — HashSet:** Track seen values. If the current number is already in the set, it's a duplicate. O(n) time, O(n) space.

The submitted solution uses Option A (sort).

---

## Solution (Sort-based — submitted)

```java
class Solution {
    public boolean hasDuplicate(int[] nums) {
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                return true;
            }
        }
        return false;
    }
}
```

---

## Alternative Solution (HashSet — optimal time)

```java
class Solution {
    public boolean hasDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (!seen.add(num)) return true; // add() returns false if already present
        }
        return false;
    }
}
```

---

## Complexity

| Approach | Time | Space |
|---|---|---|
| Sort | O(n log n) | O(1) |
| HashSet | O(n) | O(n) |

---

## Interview Tips

- The HashSet solution is the go-to answer interviewers expect — it demonstrates the O(1) lookup pattern that reappears constantly.
- `Set.add()` returning `false` on duplicates is a clean Java idiom worth knowing.
- The sort approach is valid when the interviewer asks for O(1) space — it's a meaningful tradeoff to discuss.
