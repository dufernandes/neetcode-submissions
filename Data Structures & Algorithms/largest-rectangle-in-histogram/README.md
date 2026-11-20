# Largest Rectangle in Histogram

**Difficulty:** Hard  
**Pattern:** Monotonic Stack  
**NeetCode:** [Largest Rectangle In Histogram](https://neetcode.io/problems/largest-rectangle-in-histogram)  
**LeetCode:** [#84 – Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/)

---

## Problem

Given an array `heights` where `heights[i]` is the height of a bar of width 1, return the area of the largest rectangle that fits inside the histogram.

```
Input:  heights = [2,1,5,6,2,3]
Output: 10  (bars 5 and 6, height 5 × width 2)

Input:  heights = [2,4]
Output: 4
```

---

## Key Insight

Every maximal rectangle is limited by **one bar: its shortest one**. So for each bar `i`, ask: *how far can a rectangle of height `heights[i]` extend left and right before it hits a shorter bar?* The area is `heights[i] × width`, and the answer is the maximum over all bars.

The brute force answers that question by walking outwards from every bar. The optimal solution answers it for all bars at once with a **monotonic stack**: a bar's right boundary is discovered the moment a shorter bar arrives, and its left boundary is already known from what is stored on the stack.

---

## Two Solutions: Brute Force to Optimal

### Solution 1: Expand Left and Right from Every Bar (O(n²))

```java
class Solution {
    public int largestRectangleArea(int[] heights) {
        int largestArea = 0;

        if (heights == null || heights.length == 0) return 0;

        for (int i = 0; i < heights.length; i++) {
            int height = heights[i];

            // check left width (includes the bar itself)
            int width = 0;
            for (int j = i; j >= 0 && heights[j] >= height; j--) {
                width++;
            }

            // check right width
            for (int j = i + 1; j < heights.length && heights[j] >= height; j++) {
                width++;
            }

            largestArea = Math.max(largestArea, height * width);
        }

        return largestArea;
    }
}
```

**What it does:** For each bar, it counts how many consecutive bars to the left (including itself) and to the right are at least as tall, then multiplies that width by the bar's height.

**Why it is correct:** The widest rectangle of height `h` that contains bar `i` stretches exactly until the first shorter bar on each side. Counting `>=` (not `>`) is what lets equal-height neighbours join the rectangle.

**The problem:**

- **O(n²) in the worst case.** When many bars are tall or equal (for example `[5,5,5,...,5]`), every bar scans the entire array. This is exactly the case that makes it **time out on NeetCode (45/46 tests pass)**.
- Each bar redoes work that its neighbours already did.

**When to use:** Say this first in an interview to show the definition of the problem is understood, then optimise.

---

### Solution 2: Monotonic Stack of `{startIndex, height}` (O(n))

```java
class Solution {
    public int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length == 0) return 0;

        int largestArea = 0;
        Deque<int[]> stack = new ArrayDeque<>();

        stack.addFirst(new int[]{0, heights[0]});
        for (int i = 1; i < heights.length; i++) {

            int currentHeight = heights[i];
            int minWidth = i;
            while (!stack.isEmpty() && currentHeight < stack.peekFirst()[1]) {
                int[] measure = stack.removeFirst();
                largestArea = Math.max(largestArea, measure[1] * (i - measure[0]));
                minWidth = measure[0];
            }

            stack.addFirst(new int[]{minWidth, currentHeight});
        }

        while (!stack.isEmpty()) {
            int[] measure = stack.removeFirst();
            int index = measure[0];
            int height = measure[1];

            largestArea = Math.max(largestArea, height * (heights.length - index));
        }

        return largestArea;
    }
}
```

**What it does:** The stack holds `{startIndex, height}` pairs with non-decreasing heights. `startIndex` is the leftmost position where a rectangle of that height can begin.

- A taller (or equal) bar is simply pushed with its own index as the start.
- A **shorter** bar ends the rectangles of every taller bar on the stack. Each is popped and its area is computed as `height × (i − startIndex)`.
- The new bar inherits the `startIndex` of the **last (leftmost) bar it popped**, because a rectangle of its smaller height can stretch back that far. This is the key trick.
- After the loop, whatever remains on the stack extends to the end of the array, so its width is `heights.length − startIndex`.

**Why it is O(n):** Every bar is pushed once and popped once.

**Why storing the start index is the clever part:** Instead of looking up "the previous smaller bar" separately, the start index travels with the bar. Popped bars hand their left boundary to the bar that replaced them.

**This is the solution to present in interviews.**

---

## Walk-through: `heights = [2,1,5,6,2,3]`

Stack entries are `{start, height}`, shown bottom → top.

```
i=0: push {0,2}                                  stack: {0,2}
i=1: h=1 < 2 → pop {0,2}, area 2×(1−0)=2         start=0
     push {0,1}                                  stack: {0,1}
i=2: h=5 → push {2,5}                            stack: {0,1} {2,5}
i=3: h=6 → push {3,6}                            stack: {0,1} {2,5} {3,6}
i=4: h=2 < 6 → pop {3,6}, area 6×(4−3)=6
     h=2 < 5 → pop {2,5}, area 5×(4−2)=10 ✓      start=2
     push {2,2}                                  stack: {0,1} {2,2}
i=5: h=3 → push {5,3}                            stack: {0,1} {2,2} {5,3}

cleanup (width = 6 − start):
     {5,3}: 3×1=3
     {2,2}: 2×4=8
     {0,1}: 1×6=6

max = 10
```

---

## Solution Comparison

| | Solution 1 | Solution 2 |
|---|---|---|
| **Time** | O(n²) worst case | O(n) |
| **Space** | O(1) | O(n) |
| **Idea** | Expand outwards from every bar | Monotonic stack carrying each bar's left boundary |
| **Work per bar** | Rescans neighbours | Pushed once, popped once |
| **NeetCode result** | 45/46, times out | Passes |
| **Readability** | ★★★★★ | ★★★☆☆ |
| **Interview recommendation** | Explain first | Present this |

---

## Complexity (Solution 2)

| | |
|---|---|
| **Time** | O(n): each bar is pushed once and popped once |
| **Space** | O(n): the stack can hold every bar, for example when heights are increasing |

---

## Gotchas

- **Use `<` (strict) when popping, and `>=` in the brute force.** Equal heights are not popped, they are stacked, and the final cleanup computes their rectangles. Both choices give the same answer, but mixing the conventions is a classic source of off-by-one bugs.
- **The new bar inherits the start index of the last popped bar.** Pushing `{i, currentHeight}` always would silently shrink every rectangle that should reach back. `minWidth = i` is the default for when nothing was popped.
- **The cleanup loop is required.** Bars still on the stack at the end were never ended by a shorter bar, so their right edge is `heights.length`. Skipping it fails any increasing input such as `[1,2,3,4]`.
- **Width after popping is `i − start`, not `i − start + 1`.** Bar `i` is the shorter bar that ended the rectangle, so it is excluded.
- **`previousHeight` is unused.** In Solution 2, `int previousHeight = heights[i - 1];` is never read and can be deleted.
- **Empty input.** The guard `heights == null || heights.length == 0` also protects `heights[0]` when seeding the stack. The constraints say at least one bar, but the guard is cheap.
- **Overflow is not an issue here.** Maximum area is `10⁴ × 10⁵ = 10⁹`, which fits in an `int`. For larger limits, switch to `long`.
- **Allocating an `int[]` per push** is fine for this problem, but a stack of indices (reading heights from the array) avoids the allocations.

---

## Interview Tips

- Start with the reduction: *"Each rectangle is limited by its shortest bar, so for every bar I need the nearest shorter bar on each side."*
- Present the brute force in one sentence, give its complexity, and move on. It shows you understand the problem.
- Explain the stack invariant out loud: *"Heights on the stack never decrease. When a shorter bar arrives, the taller bars can no longer extend right, so I finalise them."*
- Highlight the trick that makes the left boundary free: the new bar takes over the start index of the last bar it popped.
- Mention the common variant: store only indices on the stack and compute the width as `i − stack.peek() − 1` (with a sentinel `-1` at the bottom, or a zero-height bar appended at the end to flush the stack).
- Related problems: [Daily Temperatures](../daily-temperatures/README.md) and [Car Fleet](../car-fleet/README.md) (same monotonic-stack family), Trapping Rain Water (two-pointer or stack), and Maximal Rectangle (LeetCode #85), which runs this algorithm once per row of a binary matrix.
- Edge cases to mention: a single bar, all equal heights, strictly increasing, strictly decreasing, and a zero-height bar splitting the histogram.
