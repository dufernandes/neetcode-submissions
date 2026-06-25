# Dynamic Array

**Difficulty:** Easy  
**Pattern:** Data Structure Implementation  
**NeetCode:** [Dynamic Array](https://neetcode.io/problems/dynamic-array)

---

## Problem

Implement a dynamic array (resizable array) class with the following interface:

- `DynamicArray(int capacity)` — initialize with a given capacity
- `int get(int i)` — return the element at index `i`
- `void set(int i, int n)` — set the element at index `i` to `n`
- `void pushback(int n)` — append `n` to the end; resize if needed
- `int popback()` — remove and return the last element
- `int getSize()` — return the number of elements stored
- `int getCapacity()` — return the current underlying array capacity

---

## Key Insight

A dynamic array wraps a fixed-size array and **doubles its capacity** whenever it's full. This doubling strategy — amortized analysis — ensures that `pushback` is O(1) on average, even though occasional resizes cost O(n).

Why doubling? If you grew by 1 each time, every `pushback` that triggers a resize would copy the entire array, giving O(n) per operation. Doubling means resizes happen at sizes 1, 2, 4, 8, 16, ... — the total copy work across n operations sums to O(n), so the average (amortized) cost per `pushback` is O(1).

---

## Solution

```java
class DynamicArray {
    private int[] array;
    private int count;  // number of elements actually stored

    public DynamicArray(int capacity) {
        array = new int[capacity];
    }

    public int get(int i) {
        return array[i];
    }

    public void set(int i, int n) {
        array[i] = n;
    }

    public void pushback(int n) {
        if (count == array.length) resize();
        array[count++] = n;
    }

    public int popback() {
        return array[--count];   // decrement count first, then return
    }

    private void resize() {
        int[] newArray = new int[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public int getSize() {
        return count;
    }

    public int getCapacity() {
        return array.length;
    }
}
```

> In production Java you'd use `System.arraycopy()` or `Arrays.copyOf()` instead of the manual loop in `resize()` — they're faster and more idiomatic.

---

## Walkthrough

```
DynamicArray(2)         capacity=2, size=0,  array=[_, _]
pushback(1)             capacity=2, size=1,  array=[1, _]
pushback(2)             capacity=2, size=2,  array=[1, 2]
pushback(3)  → resize!  capacity=4, size=3,  array=[1, 2, 3, _]
popback()    → 3        capacity=4, size=2,  array=[1, 2, _, _]
```

---

## Complexity

| Operation | Time | Notes |
|---|---|---|
| `get` / `set` | O(1) | Direct index access |
| `pushback` | O(1) amortized | O(n) on resize, but rare |
| `popback` | O(1) | Just decrement count |
| `resize` | O(n) | Copies entire array, doubles capacity |

Space: O(n) — at most 2× the number of stored elements.

---

## Interview Tips

- Interviewers sometimes ask you to implement this from scratch to test fundamentals. The key concepts to articulate are: **separate `size` vs `capacity`**, and **amortized O(1) via doubling**.
- `popback` does not need to zero out the slot — the `count` field is the source of truth for what's "in" the array.
- Java's `ArrayList` is exactly this under the hood — if asked, mention that and explain the growth factor (Java uses 1.5× growth, not 2×, to save memory).
- The `count++` and `--count` patterns in `pushback`/`popback` are the idiomatic way to post-increment / pre-decrement in one line. Know why `popback` uses `--count` (returns the element at the new, decremented position).
