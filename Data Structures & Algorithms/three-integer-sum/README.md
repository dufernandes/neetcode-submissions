# Three Sum (3Sum)

**Difficulty:** Medium  
**Pattern:** Two Pointers  
**NeetCode:** [Three Sum](https://neetcode.io/problems/three-integer-sum)  
**LeetCode:** [#15 – 3Sum](https://leetcode.com/problems/3sum/)

---

## Problem

Given an integer array `nums`, return all unique triplets `[nums[i], nums[j], nums[k]]` such that they sum to zero. The solution set must not contain duplicate triplets.

```
Input:  nums = [-1, 0, 1, 2, -1, -4]
Output: [[-1, -1, 2], [-1, 0, 1]]

Input:  nums = [0, 0, 0]
Output: [[0, 0, 0]]
```

---

## Key Insight

Reduce the problem from 3Sum to 2Sum. Sort the array, fix one element `nums[i]`, then run two pointers on the remaining subarray to find pairs that sum to `-nums[i]`.

Sorting is what makes both the two-pointer technique and duplicate skipping possible — equal elements are adjacent, so duplicates can be detected by comparing with the previous value.

---

## Solution

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        int length = nums.length;
        for (int i = 0; i < length - 2; i++) {
            int iValue = nums[i];
            if (i > 0 && iValue == nums[i - 1]) continue; // skip duplicate i
            int left = i + 1, right = length - 1;
            while (left < right) {
                int value = iValue + nums[left] + nums[right];
                if (value < 0) left++;
                else if (value > 0) right--;
                else {
                    result.add(List.of(iValue, nums[left], nums[right]));
                    left++; right--;
                    while (left < right && nums[left] == nums[left - 1]) left++;   // skip duplicate left
                    while (left < right && nums[right] == nums[right + 1]) right--; // skip duplicate right
                }
            }
        }
        return result;
    }
}
```

> Note: Duplicate skipping happens at **three levels** — for `i`, for `left`, and for `right`. Missing any one of them produces duplicate triplets in the result.

---

## Walk-through: `[-1, 0, 1, 2, -1, -4]`

After sorting: `[-4, -1, -1, 0, 1, 2]`

```
i=0, iValue=-4 → left=1, right=5 → sum always > 0 or < 0, no triplet
i=1, iValue=-1 → left=2, right=5
  left=2 (-1), right=5 (2) → sum=0 → add [-1,-1,2], left=3, right=4
  left=3 (0), right=4 (1)  → sum=0 → add [-1,0,1],  left=4, right=3 → exit
i=2, iValue=-1 → duplicate of nums[1], skip
i=3, iValue=0  → left=4, right=5 → sum=3 > 0, right-- → exit
```

Output: `[[-1,-1,2], [-1,0,1]]` ✓

---

## Brute Force (O(n³))

Three nested loops, check every combination. Use a Set to deduplicate.

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Set<List<Integer>> result = new HashSet<>();
        Arrays.sort(nums);
        int n = nums.length;
        for (int i = 0; i < n - 2; i++)
            for (int j = i + 1; j < n - 1; j++)
                for (int k = j + 1; k < n; k++)
                    if (nums[i] + nums[j] + nums[k] == 0)
                        result.add(List.of(nums[i], nums[j], nums[k]));
        return new ArrayList<>(result);
    }
}
```

The optimal solution reduces this to O(n²) by using two pointers instead of the innermost loop.

---

## Complexity

| | |
|---|---|
| **Time** | O(n²) — O(n log n) sort + O(n²) two-pointer pass |
| **Space** | O(1) — excluding the output list |

---

## Interview Tips

- Always sort first — it enables both two pointers and duplicate skipping.
- Duplicate skipping is the hardest part. Explain all three levels clearly:
  - Skip `i` if it equals `nums[i-1]`
  - After finding a triplet, skip `left` if it equals `nums[left-1]`
  - After finding a triplet, skip `right` if it equals `nums[right+1]`
- Early termination optimization worth mentioning: if `nums[i] > 0`, all remaining elements are positive and no triplet can sum to zero — break early.
- This problem is a prerequisite for 4Sum — the same pattern extends naturally.
