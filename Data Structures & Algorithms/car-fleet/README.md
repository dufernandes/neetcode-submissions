# Car Fleet

**Difficulty:** Medium  
**Pattern:** Monotonic Stack  
**NeetCode:** [Car Fleet](https://neetcode.io/problems/car-fleet)  
**LeetCode:** [#853 – Car Fleet](https://leetcode.com/problems/car-fleet/)

---

## Problem

There are `n` cars going to the same destination along a one-lane road. You are given an array `position` of starting positions and an array `speed` of speeds. The destination is at `target`.

A car can never pass another car — if it catches up, they form a **fleet** and move at the slower car's speed. Return the number of fleets that arrive at the destination.

```
Input:  target=12, position=[10,8,0,5,3], speed=[2,4,1,1,3]
Output: 3

Input:  target=10, position=[3], speed=[3]
Output: 1

Input:  target=100, position=[0,2,4], speed=[4,2,1]
Output: 1
```

---

## Key Insight

Process cars from **closest to target first**. A car behind forms a new fleet only if it takes **longer** to reach the target than the car ahead — meaning it will never catch up. If it's faster or equal, it catches up and joins the leading fleet.

This is a monotonic stack problem in disguise: you're maintaining a stack of fleet leaders, each taking strictly longer than the one in front.

---

## Two Solutions — Same Logic, Different Implementation

### Solution 1 — Explicit Stack with HashMap (more moving parts)

```java
class Solution {
    public int carFleet(int target, int[] position, int[] speed) {
        if (position.length == 1) return 1;

        Deque<Integer> fleet = new ArrayDeque<>();
        Map<Integer, Integer> cars = new HashMap<>();

        // map position → speed for lookup after sorting
        for (int i = 0; i < position.length; i++) {
            cars.put(position[i], speed[i]);
        }

        Arrays.sort(position); // sort positions ascending

        // process from closest to target (right to left)
        for (int i = position.length - 1; i >= 0; i--) {
            if (fleet.isEmpty()) {
                fleet.push(position[i]);
                continue;
            }

            // current car takes longer → new fleet leader
            if (timeToFinish(target, position[i], cars.get(position[i])) >
                timeToFinish(target, fleet.peekFirst(), cars.get(fleet.peekFirst()))) {
                fleet.push(position[i]);
            }
            // otherwise catches up → joins leading fleet, not pushed
        }

        return fleet.size();
    }

    double timeToFinish(int end, int start, int speed) {
        return (double) (end - start) / (double) speed;
    }
}
```

**What it does:** Sorts positions, builds a HashMap of position→speed for lookup, then iterates right to left pushing positions onto a stack when they represent new fleet leaders. The stack size is the number of fleets.

**The problems:**

**1. HashMap is unnecessary overhead.** Sorting `position` alone disconnects it from `speed`. The HashMap bridges that gap, but it adds O(n) space and O(n) lookup overhead for something that could be solved by sorting pairs directly.

**2. `fleet.peekFirst()` stores positions, not times.** To compare fleets, you call `timeToFinish` on the stack top every iteration — a redundant recomputation. You already computed that time when you pushed it.

**3. The explicit stack isn't needed at all.** You only ever care about the top of the stack (the current fleet leader's time). You never inspect deeper elements. A single `double leaderTime` variable replaces the entire stack.

**4. Edge case guard `if (position.length == 1) return 1`** is unnecessary — the loop handles it correctly without it.

---

### Solution 2 — Paired Sort, No Stack, No HashMap (clean and optimal)

```java
class Solution {
    public int carFleet(int target, int[] position, int[] speed) {
        // pair position and speed together before sorting
        int[][] cars = new int[position.length][];
        for (int i = 0; i < position.length; i++) {
            cars[i] = new int[]{position[i], speed[i]};
        }
        // sort by position descending — closest to target first
        Arrays.sort(cars, (a, b) -> Integer.compare(b[0], a[0]));

        int fleets = 0;
        double leaderTime = 0; // time of the current fleet leader

        for (int[] car : cars) {
            double time = timeToFinish(target, car[0], car[1]);
            if (time > leaderTime) {
                // takes longer than leader — can't catch up — new fleet
                fleets++;
                leaderTime = time;
            }
            // otherwise catches up — joins current fleet, leaderTime unchanged
        }

        return fleets;
    }

    private double timeToFinish(int target, int position, int speed) {
        return (double) (target - position) / speed;
    }
}
```

**Four improvements over Solution 1:**

**1. Pairs position and speed before sorting.** `int[][] cars` keeps position and speed together — no HashMap needed. After sorting, `car[0]` is the position and `car[1]` is the speed. Clean and direct.

**2. Sorts descending** (`b[0] - a[0]` comparator) instead of ascending with a reverse iteration. Processes closest to target first naturally — no right-to-left index gymnastics.

**3. Replaces the stack with a single `double leaderTime`.** You only ever compare against the current fleet leader — a scalar is all you need. The stack in Solution 1 tracked positions that were never inspected below the top.

**4. Eliminates the edge case guard.** The loop handles a single car correctly: it's always a new fleet (time > 0 = leaderTime).

**This is the correct solution to present in interviews.**

---

## Walk-through: `target=12, position=[10,8,0,5,3], speed=[2,4,1,1,3]`

After pairing and sorting descending: `[[10,2],[8,4],[5,1],[3,3],[0,1]]`

```
car=[10,2]: time=(12-10)/2=1.0,  1.0>0.0 → fleets=1, leaderTime=1.0
car=[8,4]:  time=(12-8)/4=1.0,   1.0>1.0? No → joins fleet 1
car=[5,1]:  time=(12-5)/1=7.0,   7.0>1.0 → fleets=2, leaderTime=7.0
car=[3,3]:  time=(12-3)/3=3.0,   3.0>7.0? No → joins fleet 2
car=[0,1]:  time=(12-0)/1=12.0, 12.0>7.0 → fleets=3, leaderTime=12.0

return 3 ✓
```

---

## Solution Comparison

| | Solution 1 | Solution 2 |
|---|---|---|
| **Sort** | position ascending + reverse iterate | pairs descending |
| **Speed lookup** | HashMap | inline via pairs |
| **Fleet tracking** | `Deque<Integer>` (stack of positions) | `double leaderTime` |
| **Redundant recomputation** | Yes — `timeToFinish` on stack top each step | No |
| **Extra space** | O(n) HashMap + O(n) stack | O(n) pairs only |
| **Edge case guard needed** | Yes | No |
| **Readability** | ★★★☆☆ | ★★★★★ |

---

## Complexity (Both Solutions)

| | |
|---|---|
| **Time** | O(n log n) — dominated by sorting |
| **Space** | O(n) — pairs array (Solution 2), HashMap + stack (Solution 1) |

---

## Gotchas

- **`time > leaderTime` not `>=`** — equal time means the car arrives at exactly the same moment as the fleet leader. It joins that fleet, not a new one.
- **Must cast to `double` before dividing** — `(double)(target - position) / speed` or you get integer division and lose precision. Example: `(12-10)/2 = 1` (correct by luck), but `(12-8)/3 = 1` instead of `1.333...`.
- **Sort descending, not ascending** — processing from furthest to target first would give wrong results. You must process closest to target first because a car ahead sets the pace.
- **A car that catches up joins the fleet but doesn't slow it down in the model** — the fleet moves at the leader's (slower) speed. You don't need to track this — only fleet count matters.
- **`leaderTime` starts at 0** — any real car takes positive time (target > position, speed > 0), so the first car always starts a new fleet correctly.

---

## Interview Tips

- State the key insight immediately: *"Process from closest to target first. A car forms a new fleet only if it takes longer to arrive than the car ahead — meaning it can never catch up."*
- Mention that this is a monotonic stack pattern — the fleet times form a strictly increasing sequence from front to back.
- When presenting Solution 2, explain why the stack isn't needed: *"I only ever compare against the current leader — I never look deeper into the stack, so a single variable replaces it entirely."*
- The `double` cast gotcha is worth stating proactively — it shows attention to numeric precision.
- Edge cases to mention: single car (always 1 fleet), all same speed (n fleets if positions differ, handled correctly), cars that arrive simultaneously (join same fleet, `>` not `>=` handles this).

---
