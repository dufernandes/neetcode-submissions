# Min Stack

**Difficulty:** Medium  
**Pattern:** Stack  
**NeetCode:** [Minimum Stack](https://neetcode.io/problems/minimum-stack)  
**LeetCode:** [#155 – Min Stack](https://leetcode.com/problems/min-stack/)

---

## Problem

Design a stack that supports push, pop, top, and retrieving the minimum element — all in O(1) time.

```
MinStack minStack = new MinStack();
minStack.push(-2);
minStack.push(0);
minStack.push(-3);
minStack.getMin(); // returns -3
minStack.pop();
minStack.top();    // returns 0
minStack.getMin(); // returns -2
```

---

## Key Insight

A regular stack gives you O(1) push, pop, and top — but getMin() would require scanning the whole stack: O(n). The challenge is tracking the minimum without losing that information when elements are popped.

The solution: **maintain a parallel min-stack** that tracks the current minimum at every level of the main stack. When you pop the main stack, you also pop the min stack — so the minimum is always known for any stack state, in O(1).

---

## Three Solutions — Evolution from Broken to Optimal

### Solution 1 — `ArrayList` + `PriorityQueue` (works, but wrong complexity)

```java
class MinStack {
    private List<Integer> stack = new ArrayList<>();
    PriorityQueue<Integer> min = new PriorityQueue<>();

    public MinStack() {
        stack = new ArrayList<>();
    }

    public void push(int val) {
        stack.add(val);
        min.add(val);         // O(log n)
    }

    public void pop() {
        int top = stack.removeLast();
        min.remove(top);      // O(n) — linear scan to find element
    }

    public int top() {
        return stack.getLast();
    }

    public int getMin() {
        return min.peek();    // O(1)
    }
}
```

**What it does:** Uses a `PriorityQueue` (min-heap) to always have the minimum at the top.

**Why it fails the problem constraints:**
- `push()` is O(log n) — heap insertion
- `pop()` is O(n) — `PriorityQueue.remove(element)` performs a linear scan to find the element before removing it

The problem requires all operations in O(1). This solution violates that for both push and pop.

**When to use:** Never in production or interviews for this problem. Useful only as a first-attempt to demonstrate you understand the constraint before optimising.

---

### Solution 2 — `Stack` + `PriorityQueue` (same problem, legacy stack added)

```java
class MinStack {
    private Stack<Integer> stack = new Stack<>();
    PriorityQueue<Integer> min = new PriorityQueue<>();

    public MinStack() {
        stack = new Stack<>();
    }

    public void push(int val) {
        stack.push(val);
        min.add(val);         // O(log n)
    }

    public void pop() {
        int top = stack.pop();
        min.remove(top);      // O(n)
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return min.peek();
    }
}
```

**What changed from Solution 1:** `ArrayList` replaced with `Stack`, and `removeLast()` replaced with `pop()`, `getLast()` with `peek()`.

**What didn't change:** The `PriorityQueue` is still there, so push is still O(log n) and pop is still O(n). This is a cosmetic improvement — the fundamental complexity problem is unchanged.

**Additional concern:** `Stack` extends `Vector` which is synchronized — unnecessary overhead in a single-threaded context. This is actually a step backward from `ArrayList` in terms of Java idiom.

**Verdict:** No meaningful improvement over Solution 1. Both fail the O(1) requirement.

---

### Solution 3 — Two Stacks (correct, O(1) all operations)

```java
class MinStack {
    private Stack<Integer> stack = new Stack<>();
    private Stack<Integer> min = new Stack<>();

    public MinStack() {
        stack = new Stack<>();
    }

    public void push(int val) {
        stack.push(val);
        if (min.isEmpty() || val <= min.peek()) {
            min.push(val);           // new minimum — track it
        } else {
            min.push(min.peek());    // not a new minimum — duplicate current min
        }
    }

    public void pop() {
        stack.pop();
        min.pop();    // both stacks always same size — pop in sync
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return min.peek();    // current minimum always at top of min stack
    }
}
```

**The key insight:** Push the current minimum onto the min stack on every push — whether or not the new value is smaller. This keeps both stacks the same size, so popping in sync is always correct and the minimum for any stack state is always at the top of the min stack.

**All operations are O(1):**
- `push()` — one push to each stack
- `pop()` — one pop from each stack
- `top()` — peek main stack
- `getMin()` — peek min stack

**This is the correct solution.** Present this in interviews.

---

## Walk-through: `push(-2), push(0), push(-3), pop()`

```
push(-2): stack=[-2],      min=[-2]        (min empty → push -2)
push(0):  stack=[-2,0],    min=[-2,-2]     (0 > -2 → duplicate current min)
push(-3): stack=[-2,0,-3], min=[-2,-2,-3]  (-3 ≤ -2 → new minimum)

getMin() → min.peek() = -3 ✓

pop():    stack=[-2,0],    min=[-2,-2]     (both stacks popped in sync)

top()    → stack.peek() = 0 ✓
getMin() → min.peek() = -2 ✓
```

---

## Solution Comparison

| | Solution 1 | Solution 2 | Solution 3 |
|---|---|---|---|
| **Main stack** | `ArrayList` | `Stack` | `Stack` |
| **Min structure** | `PriorityQueue` | `PriorityQueue` | `Stack` |
| **push()** | O(log n) | O(log n) | O(1) ✓ |
| **pop()** | O(n) | O(n) | O(1) ✓ |
| **top()** | O(1) | O(1) | O(1) ✓ |
| **getMin()** | O(1) | O(1) | O(1) ✓ |
| **Space** | O(n) | O(n) | O(n) |
| **Meets constraints** | ✗ | ✗ | ✓ |

---

## Complexity (Solution 3)

| | |
|---|---|
| **Time** | O(1) for all operations |
| **Space** | O(n) — two stacks, each at most n elements |

---

## Gotchas

- **`val <= min.peek()` not `val < min.peek()`** — use `<=` when pushing to the min stack. If you push duplicate values (e.g. two `-3`s), only tracking strictly less than means the first `-3` pops off the min stack but the second `-3` is no longer tracked — `getMin()` would return the wrong value after a pop.
- **Both stacks always the same size** — push and pop must happen on both stacks together, every time. If they get out of sync, `getMin()` returns the wrong minimum.
- **`Stack` is legacy in Java** — in a real codebase, use `Deque<Integer> stack = new ArrayDeque<>()`. In an interview, either is acceptable but mentioning `ArrayDeque` signals Java awareness.
- **The min stack stores values, not indices** — this is correct because you only ever need the minimum value, not where it came from.

---

## Interview Tips

- Start by explaining why a single stack isn't enough — getMin() would be O(n). Then introduce the parallel min stack as the solution.
- Walk through the `<=` vs `<` distinction before writing code — interviewers often probe this edge case explicitly.
- If asked "what if you used a PriorityQueue instead?" — explain that remove() is O(n) because the heap must scan for the element, breaking the O(1) constraint. Solutions 1 and 2 exist to demonstrate exactly this reasoning.
- Follow-up question: *"Can you do it with O(1) extra space?"* — yes, by storing the difference between the value and the current minimum, encoding both in a single stack. This is an advanced optimisation and rarely required.

---
