# Container With Most Water

**Difficulty:** Medium  
**Topic Tags:** Array, Two Pointers, Greedy  
**NeetCode 150:** Yes

---

## Problem

Given an integer array `heights` where `heights[i]` represents the height of a vertical line at position `i`, find two lines that together with the x-axis form a container that holds the most water.

Return the maximum amount of water the container can store.

> The container cannot be slanted — water is limited by the shorter of the two walls.

**Example:**

```
heights = [1, 8, 6, 2, 5, 4, 8, 3, 7]
```

The best pair is indices `1` and `8` (heights `8` and `7`), giving area = `min(8, 7) * (8 - 1) = 7 * 7 = 49`.

---

## Solutions

### Submission 0 & 1 — Brute Force — O(n²)

Both brute force submissions share the same logic. The only difference is cosmetic: submission 0 uses an explicit `if` comparison, while submission 1 replaces it with `Math.max()`. Both are functionally equivalent.

```java
// Submission 0 — explicit if
class Solution {
    public int maxArea(int[] heights) {
        int maxArea = 0;
        for (int i = 0; i < heights.length - 1; i++) {
            for (int j = 1; j < heights.length; j++) {
                int tempArea = Math.min(heights[i], heights[j]) * (j - i);
                if (tempArea > maxArea) {
                    maxArea = tempArea;
                }
            }
        }
        return maxArea;
    }
}
```

```java
// Submission 1 — Math.max
class Solution {
    public int maxArea(int[] heights) {
        int maxArea = 0;
        for (int i = 0; i < heights.length - 1; i++) {
            for (int j = 1; j < heights.length; j++) {
                int tempArea = Math.min(heights[i], heights[j]) * (j - i);
                maxArea = Math.max(tempArea, maxArea);
            }
        }
        return maxArea;
    }
}
```

**How it works:**

Every possible pair of lines `(i, j)` is tested. For each pair, the area is:

```
area = min(heights[i], heights[j]) * (j - i)
```

- `min(heights[i], heights[j])` — water is limited by the shorter wall
- `(j - i)` — the width between the two lines

The global maximum is tracked across all pairs.

**Why it's not optimal:** With `n` lines, there are `n*(n-1)/2` possible pairs. Every single one is evaluated — even pairs that are clearly not competitive. There is no early termination or exploitation of structure.

---

### Submission 2 — Two Pointers — O(n)

```java
class Solution {
    public int maxArea(int[] heights) {
        int maxArea = 0, left = 0, right = heights.length - 1;
        while (left < right) {
            int tempArea = Math.min(heights[left], heights[right]) * (right - left);
            maxArea = Math.max(tempArea, maxArea);
            if (heights[left] < heights[right]) left++;
            else right--;
        }
        return maxArea;
    }
}
```

**How it works:**

Start with the widest possible container: one pointer at each end. Then shrink inward, always moving the pointer on the **shorter side**.

The reasoning is greedy:

- The area is always bounded by the **shorter** of the two walls.
- Moving the pointer on the **taller** side inward can only make things worse — you lose width *and* the height ceiling stays the same or drops.
- Moving the pointer on the **shorter** side gives you a chance at a taller wall, which is the only way the area can increase despite the reduced width.

This means at every step, the eliminated candidates genuinely cannot beat the current best — making the greedy choice provably correct, not just a heuristic.

---

## Complexity Comparison

| Solution       | Time     | Space | Notes                              |
|----------------|----------|-------|------------------------------------|
| Brute Force    | O(n²)    | O(1)  | Tests every pair                   |
| Two Pointers   | O(n)     | O(1)  | Single pass; greedy pointer movement |

---

## Key Insight

The leap from O(n²) to O(n) comes from recognizing that **you never need to test every pair**. The two-pointer approach implicitly discards pairs that cannot be the answer — not by checking them and moving on, but by proving they are irrelevant before ever computing them.

That's the difference between exhaustive search and exploiting structure.
