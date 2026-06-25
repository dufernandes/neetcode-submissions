# Two Sum

**Difficulty:** Easy  
**Pattern:** Hash Map Lookup  
**NeetCode:** [Two Integer Sum](https://neetcode.io/problems/two-integer-sum)  
**LeetCode:** [#1 – Two Sum](https://leetcode.com/problems/two-sum/)

---

## Problem

Given an array of integers `nums` and a target integer `target`, return the indices of the two numbers that add up to `target`. Each input has exactly one solution, and you may not use the same element twice.

```
Input:  nums = [2, 7, 11, 15], target = 9
Output: [0, 1]   // nums[0] + nums[1] == 9
```

---

## Key Insight

For every number `x` in the array, its required complement is `target - x`. Instead of scanning the array again to find that complement (O(n) nested → O(n²) total), store each number's index in a HashMap as you go. On each step, check if the complement was already seen — if yes, you're done.

```
target = 9, nums = [2, 7, 11, 15]

i=0: need 9-2=7 → not in map. Store {2:0}
i=1: need 9-7=2 → found at index 0! Return [0, 1] ✓
```

---

## Solution

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> diffs = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int difference = target - nums[i];
            if (diffs.containsKey(difference)) {
                return new int[]{diffs.get(difference), i};
            }
            diffs.put(nums[i], i);
        }

        return null; // guaranteed to have a solution per problem constraints
    }
}
```

---

## Complexity

| | |
|---|---|
| **Time** | O(n) — single pass through the array |
| **Space** | O(n) — HashMap stores at most n elements |

---

## Why not sort + two pointers?

Sorting is O(n log n) and, critically, **destroys the original indices** — which the problem requires you to return. The HashMap approach preserves indices and is faster.

---

## Interview Tips

- The naive O(n²) brute force (nested loops) is the starting point — mention it, then explain why the HashMap improves it.
- The map key is the number **value**, and the value is its **index** — easy to mix up under pressure.
- The check must come **before** the insert, otherwise a number might match itself (e.g. `target=6, num=3`).
