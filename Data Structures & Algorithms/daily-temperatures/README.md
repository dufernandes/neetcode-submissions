# Daily Temperatures

**Difficulty:** Medium  
**Pattern:** Monotonic Stack  
**NeetCode:** [Daily Temperatures](https://neetcode.io/problems/daily-temperatures)  
**LeetCode:** [#739 – Daily Temperatures](https://leetcode.com/problems/daily-temperatures/)

---

## Problem

Given an array of integers `temperatures` representing daily temperatures, return an array `result` where `result[i]` is the number of days you have to wait after day `i` to get a warmer temperature. If there is no future day with a warmer temperature, `result[i] = 0`.

```
Input:  temperatures = [73,74,75,71,69,72,76,73]
Output: [1,1,4,2,1,1,0,0]

Input:  temperatures = [30,40,50,60]
Output: [1,1,1,0]

Input:  temperatures = [30,60,90]
Output: [1,1,0]
```

---

## Key Insight

For each day, you need the **next greater element** to its right. This is the classic use case for a **monotonic decreasing stack** — a stack that always holds elements in decreasing order, and resolves waiting days the moment a warmer temperature is found.

When you encounter a temperature warmer than the top of the stack, you've found the answer for that day. Pop it, calculate the distance, and repeat until the stack top is no longer cooler.

---

## Two Solutions — Brute Force to Optimal

### Solution 1 — Nested Loops (simple, wrong complexity)

```java
class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int[] result = new int[temperatures.length];
        for (int i = 0; i < temperatures.length; i++) {
            result[i] = 0;
            for (int j = i; j < temperatures.length; j++) {
                if (temperatures[j] > temperatures[i]) {
                    result[i] = j - i;
                    break;
                }
            }
        }
        return result;
    }
}
```

**What it does:** For every day `i`, scan every future day `j` until a warmer temperature is found. Store the distance `j - i`.

**Why it works:** Straightforward — compare every pair. The `break` exits the inner loop as soon as the first warmer day is found.

**The problem:** O(n²) time. For an input of 100,000 days, this performs up to 10 billion comparisons. It passes small test cases but will TLE (Time Limit Exceeded) on large inputs.

**When to use:** Explain this first in an interview to establish correctness, then optimise.

---

### Solution 2 — Monotonic Stack (optimal)

```java
class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int[] result = new int[temperatures.length];
        Deque<int[]> stack = new ArrayDeque<>(); // stores [temperature, index]

        for (int i = 0; i < temperatures.length; i++) {
            result[i] = 0;

            // current temperature is warmer than stack top — resolve waiting days
            while (!stack.isEmpty() && temperatures[i] > stack.peekFirst()[0]) {
                int[] stackedItem = stack.removeFirst();
                result[stackedItem[1]] = i - stackedItem[1];
            }

            // push current day onto stack — still waiting for a warmer day
            stack.addFirst(new int[]{temperatures[i], i});
        }

        return result;
    }
}
```

**What it does:** Maintains a stack of days that are still waiting for a warmer temperature, in decreasing temperature order. When a warmer day arrives, it resolves all the days on the stack that were cooler.

**Why `Deque` and not `Stack`:** `ArrayDeque` is the idiomatic Java choice — not synchronised, no legacy `Vector` overhead, faster in practice. `addFirst`/`removeFirst`/`peekFirst` operate on the front as a stack (LIFO).

**Why store `[temperature, index]`:** You need both the temperature for comparison and the index to calculate the distance. Storing both in an `int[]` pair avoids a second lookup.

**Complexity:** O(n) time — each element is pushed and popped at most once.

---

## Walk-through: `[73, 74, 75, 71, 69, 72, 76, 73]`

```
i=0, temp=73: stack empty → push [73,0] → stack=[[73,0]]
i=1, temp=74: 74>73 → pop [73,0], result[0]=1-0=1 → push [74,1] → stack=[[74,1]]
i=2, temp=75: 75>74 → pop [74,1], result[1]=2-1=1 → push [75,2] → stack=[[75,2]]
i=3, temp=71: 71<75 → push [71,3] → stack=[[71,3],[75,2]]
i=4, temp=69: 69<71 → push [69,4] → stack=[[69,4],[71,3],[75,2]]
i=5, temp=72: 72>69 → pop [69,4], result[4]=5-4=1
              72>71 → pop [71,3], result[3]=5-3=2
              72<75 → stop → push [72,5] → stack=[[72,5],[75,2]]
i=6, temp=76: 76>72 → pop [72,5], result[5]=6-5=1
              76>75 → pop [75,2], result[2]=6-2=4
              stack empty → push [76,6] → stack=[[76,6]]
i=7, temp=73: 73<76 → push [73,7] → stack=[[73,7],[76,6]]

End: stack=[[73,7],[76,6]] → result[7]=0, result[6]=0 (already initialised to 0)

Output: [1,1,4,2,1,1,0,0] ✓
```

---

## Solution Comparison

| | Solution 1 | Solution 2 |
|---|---|---|
| **Time** | O(n²) | O(n) |
| **Space** | O(1) | O(n) — stack holds at most n elements |
| **Readability** | ★★★★★ | ★★★☆☆ |
| **Handles large input** | ✗ TLE | ✓ |
| **Interview recommendation** | Explain first | Present this |

---

## Complexity (Solution 2)

| | |
|---|---|
| **Time** | O(n) — each element pushed and popped at most once |
| **Space** | O(n) — worst case all days pushed (strictly decreasing temperatures) |

---

## Gotchas

- **`result[i] = 0` initialisation** — Java initialises `int[]` to 0 by default, so the explicit assignment in Solution 1 is redundant but harmless. Days with no warmer future day are correctly left at 0.
- **Stack holds pairs, not just temperatures** — you need the index to calculate distance. A common mistake is storing only the temperature and losing the ability to compute `i - stackedItem[1]`.
- **`peekFirst()` not `peek()`** — when using `ArrayDeque` as a stack with `addFirst`, always use `peekFirst()` and `removeFirst()` for consistency. Mixing `peek()` (which is `peekLast()`) with `addFirst()` reads the wrong end.
- **Remaining stack elements get 0** — days still in the stack at the end have no warmer future day. Since `result` is initialised to 0, no cleanup is needed.
- **Strictly greater, not greater-or-equal** — the problem asks for a *warmer* day, meaning `temperatures[i] > stack top`. Equal temperatures do not resolve the wait.

---

## Interview Tips

- Name the pattern explicitly: *"This is the next greater element problem — the classic use case for a monotonic decreasing stack."*
- Start with Solution 1 to show you understand the problem, then say: *"This is O(n²). I can do it in O(n) with a monotonic stack."*
- Explain the stack invariant clearly: *"The stack always holds days in decreasing temperature order — days still waiting for a warmer day. The moment I find a warmer temperature, I resolve everything below it on the stack."*
- When asked why you store index alongside temperature: *"I need the index to calculate the number of days between now and the waiting day — just the temperature isn't enough."*
- Common follow-up: *"What if you wanted the next greater element's value instead of the distance?"* — same pattern, store `temperatures[i]` in the result instead of `i - stackedItem[1]`.

---
