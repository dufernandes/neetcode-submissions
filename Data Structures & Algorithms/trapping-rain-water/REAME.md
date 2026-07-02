# Trapping Rain Water

**Difficulty:** Hard  
**Pattern:** Two Pointers  
**NeetCode:** [Trapping Rain Water](https://neetcode.io/problems/trapping-rain-water)  
**LeetCode:** [#42 – Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/)

---

## Problem

Given an array of non-negative integers `height` representing an elevation map where each bar has width 1, compute how much water can be trapped after raining.

```
Input:  height = [0,1,0,2,1,0,1,3,2,1,2,1]
Output: 6

Input:  height = [4,2,0,3,2,5]
Output: 9
```

---

## Key Insight

Water trapped at any position `i` is determined by the **shorter of the tallest bars to its left and right**, minus the height of the bar itself:

```
water[i] = max(0, min(maxLeft[i], maxRight[i]) - height[i])
```

If either side has no taller bar, no water is trapped at that position — it drains off. The total is the sum across all positions.

---

## Three Solutions — Ordered by Interview Value

### Solution 1 — Prefix/Suffix Arrays (O(n) time, O(n) space)

The clearest solution. Precompute the maximum height to the left and right of each position in two separate passes, then calculate water in a third pass.

```java
class Solution {
    public int trap(int[] height) {
        int[] maxLeft  = new int[height.length];
        int[] maxRight = new int[height.length];

        // max height to the LEFT of each index (excluding itself)
        maxLeft[0] = 0;
        for (int i = 1; i < height.length; i++) {
            maxLeft[i] = Math.max(maxLeft[i - 1], height[i - 1]);
        }

        // max height to the RIGHT of each index (excluding itself)
        maxRight[height.length - 1] = 0;
        for (int i = height.length - 2; i >= 0; i--) {
            maxRight[i] = Math.max(maxRight[i + 1], height[i + 1]);
        }

        // accumulate water at each position
        int volume = 0;
        for (int i = 0; i < height.length; i++) {
            volume += Math.max(0, Math.min(maxLeft[i], maxRight[i]) - height[i]);
        }
        return volume;
    }
}
```

**Complexity:** Time O(n), Space O(n)

**When to use:** Start with this in an interview. It's the most readable and easiest to explain. Establish correctness first, then optimise.

**Gotcha:** `maxLeft[i]` stores the max height strictly to the **left** of `i` (not including `i` itself) — that's why `maxLeft[0] = 0` and the loop starts from `height[i - 1]`. Same logic applies to `maxRight`. Getting this direction wrong produces off-by-one errors.

---

### Solution 2 — Two Pointers (O(n) time, O(1) space)

The optimal solution. Eliminates the two arrays by observing that you only need to know **which side is the limiting constraint** to compute water — you don't need both sides computed upfront.

```java
class Solution {
    public int trap(int[] height) {
        if (height == null || height.length == 0) return 0;

        int volume = 0;
        int maxLeft  = height[0];
        int maxRight = height[height.length - 1];
        int left  = 1;
        int right = height.length - 2;

        while (left <= right) {
            if (maxLeft <= maxRight) {
                // left side is the constraint — process left pointer
                volume += Math.max(0, maxLeft - height[left]);
                maxLeft = Math.max(maxLeft, height[left]);
                left++;
            } else {
                // right side is the constraint — process right pointer
                volume += Math.max(0, maxRight - height[right]);
                maxRight = Math.max(maxRight, height[right]);
                right--;
            }
        }
        return volume;
    }
}
```

**Complexity:** Time O(n), Space O(1)

**When to use:** Offer this as the optimisation after explaining Solution 1. This is the answer interviewers are hoping you'll reach.

---

## Walk-through: `[0,1,0,2,1,0,1,3,2,1,2,1]`

Using the two-pointer solution:

```
Initial: maxLeft=0, maxRight=1, left=1, right=10

left=1  (h=1): maxLeft(0) ≤ maxRight(1) → water=max(0,0-1)=0, maxLeft=1, left=2
left=2  (h=0): maxLeft(1) ≤ maxRight(1) → water=max(0,1-0)=1, maxLeft=1, left=3
left=3  (h=2): maxLeft(1) ≤ maxRight(1) → water=max(0,1-2)=0, maxLeft=2, left=4
right=10(h=2): maxLeft(2) > maxRight(1) → water=max(0,1-2)=0, maxRight=2, right=9
right=9 (h=1): maxLeft(2) = maxRight(2) → water=max(0,2-1)=1, maxRight=2, right=8
...

Total: 6 ✓
```

---

## Solution Comparison

| | Prefix/Suffix Arrays | Two Pointers |
|---|---|---|
| **Time** | O(n) | O(n) |
| **Space** | O(n) | O(1) |
| **Readability** | ★★★★★ | ★★★☆☆ |
| **Interview order** | Explain first | Optimise to this |
| **Gotchas** | Index direction on maxLeft/maxRight | Understanding why the weaker side is safe to process |

---

## Why the Two-Pointer Solution Works (the Hard Part)

The non-obvious insight: when `maxLeft ≤ maxRight`, you **know** the water at the left pointer is constrained by `maxLeft` — even without knowing what's to its right — because you already know something on the right is at least as tall as `maxLeft`. The right side cannot be the bottleneck. You can safely compute and move on.

This is the reasoning that trips people up. Without understanding this, the two-pointer solution looks like magic.

---

## Gotchas

- **Pointers start at 1 and length-2**, not 0 and length-1. The first and last bars can never trap water — they have no wall on one side.
- **`maxLeft` and `maxRight` are initialised to `height[0]` and `height[length-1]`** in the two-pointer solution — not 0. They represent the tallest bar seen so far on each side, including the boundary bar.
- **`Math.max(0, ...)` prevents negative water** at positions taller than both neighbours. Never skip this.
- **Null/empty guard** — add `if (height == null || height.length == 0) return 0` before accessing `height[0]`. Missing this causes `ArrayIndexOutOfBoundsException` on empty input.
- **This is a Hard problem.** In an interview, reaching Solution 1 cleanly and explaining it well is already a pass. Reaching Solution 2 and explaining *why* it works is a strong pass.

---

## Interview Tips

- Always start with the key insight: *"Water at position i is limited by the shorter wall on either side."*
- Draw the formula `water[i] = max(0, min(maxLeft, maxRight) - height[i])` before writing any code.
- Propose Solution 1 first. Tell the interviewer: *"This is O(n) time and O(n) space. I can optimise to O(1) space with two pointers if you'd like."*
- When explaining Solution 2, explicitly explain why it's safe to process the weaker side — that's what separates candidates who memorised the solution from those who understand it.
- Edge cases to mention: empty array, all same height, strictly increasing, strictly decreasing — the last two trap zero water.
